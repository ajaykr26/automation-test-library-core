package library.engine.core.runner;

import cucumber.api.PickleStepTestStep;
import cucumber.api.Result;
import cucumber.api.event.*;
import cucumber.runtime.formatter.TestSourcesModelProxy;
import library.common.*;
import library.reporting.ExtentReporter;
import org.apache.commons.configuration2.PropertiesConfiguration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static cucumber.api.Result.Type;
import static library.common.StringHelper.getClassShortName;
import static library.engine.core.AutoEngCoreConstants.*;
import static library.reporting.ExtentReporter.getExtentReports;
import static library.reporting.ReportFactory.getReporter;
import static library.engine.core.AutoEngCoreParser.*;


public class AutoEngFormatter implements ConcurrentEventListener {
    private final EventHandler<TestSourceRead> featureStartedHandler = this::handleFeatureStartedHandler;
    private final EventHandler<TestCaseStarted> caseStartedHandler = this::handleTestCaseStarted;
    private final EventHandler<TestCaseFinished> caseFinishedHandler = this::handleTestCaseFinished;
    private final EventHandler<TestStepStarted> stepStartedHandler = this::handleTestStepStarted;
    private final EventHandler<TestStepFinished> stepFinishedHandler = this::handleTestStepFinished;
    private final TestSourcesModelProxy testSources = new TestSourcesModelProxy();
    private final ThreadLocal<String> currentFeatureFile = new InheritableThreadLocal<>();
    private final ThreadLocal<ScreenshotHandler> screenshotHandler = new InheritableThreadLocal<>();
    private final ThreadLocal<String> screenshotPath = new InheritableThreadLocal<>();
    private final ThreadLocal<Map<String, String>> screenshotBehaviorMap = new InheritableThreadLocal<>();
    private final ThreadLocal<Map<String, String>> classToWindowMapping = new InheritableThreadLocal<>();
    private final ThreadLocal<Result.Type> lastStepStatus = new InheritableThreadLocal<>();
    private static final String IGNORE = "Ignore";
    private static final String ERROR_RESPONSE = "-1";


    public void setEventPublisher(EventPublisher eventPublisher) {
        eventPublisher.registerHandlerFor(TestSourceRead.class, featureStartedHandler);

        eventPublisher.registerHandlerFor(TestCaseStarted.class, caseStartedHandler);
        eventPublisher.registerHandlerFor(TestCaseFinished.class, caseFinishedHandler);

        eventPublisher.registerHandlerFor(TestStepStarted.class, stepStartedHandler);
        eventPublisher.registerHandlerFor(TestStepFinished.class, stepFinishedHandler);

    }

    private void handleFeatureStartedHandler(final TestSourceRead event) {
        testSources.addTestSourceReadEvent(event.uri, event);
        setRuntimeProperties();
    }

    private void handleTestCaseStarted(TestCaseStarted event) {
        currentFeatureFile.set(event.testCase.getUri());
        if (System.getProperty("fw.report").equalsIgnoreCase("EXTENT")) {
            getExtentReports(Constants.EXTENT_REPORT_PATH);
            ExtentReporter.startTest(event.testCase.getName());
        }

        System.setProperty("fw.cucumberTest", "true");
        System.setProperty("fw.featureName", this.testSources.getFeature(this.currentFeatureFile.get()).getName());
        System.setProperty("fw.scenarioName", event.testCase.getName());
        System.setProperty("fw.logFileName", String.format("%s/%s",
                System.getProperty("fw.featureName"),
                System.getProperty("fw.scenarioName")));
        screenshotBehaviorMap.set(readScreenshotMap());
        classToWindowMapping.set(getWindowTypeMap());
        screenshotHandler.set(new ScreenshotHandler());
        lastStepStatus.set(Type.PASSED);
    }

    private void handleTestCaseFinished(TestCaseFinished testCaseFinished) {
        currentFeatureFile.remove();
        screenshotBehaviorMap.remove();
        screenshotPath.remove();
        classToWindowMapping.remove();
        screenshotHandler.remove();
        lastStepStatus.remove();
    }

    private void handleTestStepStarted(TestStepStarted event) {

        if (event.testStep instanceof PickleStepTestStep) {
            setWindowType(event.testStep.getCodeLocation());
            final PickleStepTestStep testStep = (PickleStepTestStep) event.testStep;
            String stepText = getStepDescription(testStep, lastStepStatus.get());
            getReporter().addStepLog(getStepStatus(lastStepStatus.get()), stepText);
            if (isBeforeStep(event.testStep.getCodeLocation(), testStep.getPickleStep().getText())) {
                screenshotPath.set(screenshotHandler.get().addScreenshotPath(stepText, lastStepStatus.get()));
                addScreenCaptureToReport(screenshotPath.get());
            } else {
                screenshotPath.set(ERROR_RESPONSE);

            }
        }
    }

    private void handleTestStepFinished(TestStepFinished event) {
        lastStepStatus.set(event.result.getStatus());
        if (event.testStep instanceof PickleStepTestStep) {
            final PickleStepTestStep testStep = (PickleStepTestStep) event.testStep;
            String stepText = getStepDescription(testStep, lastStepStatus.get());
//            getReporter().addStepLog(getStepStatus(event.result.getStatus()), stepText);
            if (screenshotPath.get().equalsIgnoreCase(ERROR_RESPONSE)) {
                screenshotPath.set(screenshotHandler.get().addScreenshotPath(stepText, event.result.getStatus()));
                addScreenCaptureToReport(screenshotPath.get());
            }
            if (event.result.getStatus() == Type.FAILED) {
                getReporter().addStepLog("FAIL", event.result.getErrorMessage());
            }
        }
    }

    private String getStepStatus(Type type) {
        switch (type) {
            case FAILED:
                return "FAIL";
            case PASSED:
                return "PASS";
            case SKIPPED:
                return "SKIP";
            case AMBIGUOUS:
                return "ERROR";
            default:
                return "INFO";
        }
    }

    private String getStepDescription(PickleStepTestStep testStep, Result.Type status) {
        final String stepKeyword = Optional.ofNullable(testSources.getKeywordFromSource(currentFeatureFile.get(), testStep.getStepLine())).orElse("UNDEFINED");
        final String stepText;
        if (isStepExecuted(status)) {
            stepText = replaceDirectoryKeysWithValues(testStep.getPickleStep().getText());
        } else {
            stepText = testStep.getPickleStep().getText();
        }
        return String.format("%s %s", stepKeyword, stepText);
    }

    private String replaceDirectoryKeysWithValues(String textToReplace) {
        if (!isStoreSentence(textToReplace) && !isEncryptedSentence(textToReplace)) {
            return replaceParameterValues(textToReplace);
        } else {
            return textToReplace;
        }
    }

    private boolean isStoreSentence(String text) {
        if (text.matches("(.*)concatenated (string|value) of \"([^\"]*)\"(.*)")) {
            return false;
        } else {
            return (text.matches("store in new key \"([^\"]*)\"(.*)"));
        }
    }

    private boolean isEncryptedSentence(String text) {

        return (text.matches("(.*)encrypted(.*)"));

    }

    private static void addScreenCaptureToReport(String screenshotPath) {
        if (!screenshotPath.equalsIgnoreCase(ERROR_RESPONSE)) {
            getReporter().addScreenCaptureFromPath(screenshotPath);
            System.setProperty("fw.screenshotAbsolutePath", screenshotPath);
        }
    }

    private boolean isBeforeStep(String codeLocation, String stepText) {
        return (screenshotBehaviorMap.get().get(getClassShortName(codeLocation)) != null || (stepText.contains("the user sends keys")));
    }

    private void setWindowType(String classLocation) {
        String classShortName = getClassShortName(classLocation);

        Map<String, String> matchingWindowType = classToWindowMapping.get()
                .entrySet()
                .stream()
                .filter(entry -> classShortName.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        matchingWindowType.values().forEach(value -> TestContext.getInstance().setActiveWindowType(value));
    }

    private Map<String, String> readScreenshotMap() {
        Map<String, String> tempMap = JSONHelper.loadJSONMapFromResources(AutoEngFormatter.class, "ScreenshotBehavior.json");
        if (tempMap != null && !tempMap.isEmpty()) {
            return tempMap.entrySet()
                    .stream()
                    .filter(entry -> entry.getKey().equalsIgnoreCase("before"))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        } else {
            return Collections.emptyMap();
        }
    }

    private Map<String, String> getWindowTypeMap() {
        Map<String, String> tempMap = new HashMap<>();

        tempMap.put("AutoEngAPI", API);
        tempMap.put("AutoEngDB", DB);
        tempMap.put("AutoEngMobile", MOBILE);
        tempMap.put("AutoEngDesktop", DESKTOP);
        tempMap.put("AutoEngWeb", SELENIUM);
        tempMap.put("AutoEngMainframe", LEANFT);
        tempMap.put("AutoEngUtility", IGNORE);
        tempMap.put("AutoEngStore", IGNORE);
        tempMap.put("AutoEngKafka", KAFKA);

        return tempMap;

    }

    private boolean isStepExecuted(Result.Type status) {
        return status == Type.FAILED || status == Type.PASSED;
    }

    private void setRuntimeProperties() {

        PropertiesConfiguration props = Property.getProperties(Constants.RUNTIME_PROP_FILE);
        if (props != null) {
            String screenshotOnEveryStep = props.getString("screenshotOnEveryStep");
            if (screenshotOnEveryStep != null) {
                System.setProperty("fw.screenshotOnEveryStep", screenshotOnEveryStep);
            }
            String screenshotOnValidation = props.getString("screenshotOnValidation");
            if (screenshotOnValidation != null) {
                System.setProperty("fw.screenshotOnValidation", screenshotOnValidation);
            }
            String windowSwitchDelay = props.getString("windowSwitchDelay");
            if (windowSwitchDelay != null) {
                System.setProperty("fw.windowSwitchDelay", windowSwitchDelay);
            }
            String saveRunReport = props.getString("saveRunReport");
            if (saveRunReport != null) {
                System.setProperty("fw.saveRunReport", saveRunReport);
            }
            String waitForPageToLoad = props.getString("waitForPageToLoad");
            if (waitForPageToLoad != null) {
                System.setProperty("fw.waitForPageToLoad", waitForPageToLoad);
            }
            String explicitWait = props.getString("explicitWait");
            if (explicitWait != null) {
                System.setProperty("fw.explicitWait", explicitWait);
            }
            String addScreenshotToWord = props.getString("addScreenshotToWord");
            if (addScreenshotToWord != null) {
                System.setProperty("fw.addScreenshotToWord", addScreenshotToWord);
            }
            String screenshotOnFailure = props.getString("screenshotOnFailure");
            if (screenshotOnFailure != null) {
                System.setProperty("fw.screenshotOnFailure", screenshotOnFailure);
            }
            String scrollingScreenshot = props.getString("scrollingScreenshot");
            if (screenshotOnFailure != null) {
                System.setProperty("fw.scrollingScreenshot", scrollingScreenshot);
            }

            String screenshotDelay = props.getString("screenshotDelay");
            if (screenshotOnFailure != null) {
                System.setProperty("fw.screenshotDelay", screenshotDelay);
            }

            String seleniumGridURL = props.getString("seleniumGridURL");
            if (seleniumGridURL != null) {
                System.setProperty("fw.seleniumGridURL", seleniumGridURL);
            }
            String report = props.getString("report");
            if (report != null) {
                System.setProperty("fw.report", report);
            }
            String waitForConnectionRequest = props.getString("waitForConnectionRequest");
            if (waitForConnectionRequest != null) {
                System.setProperty("fw.waitForConnectionRequest", waitForConnectionRequest);
            }
        }
    }
}
