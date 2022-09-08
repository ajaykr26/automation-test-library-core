package library.reporting;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Link;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StatusDetails;
import io.qameta.allure.model.StepResult;
import io.qameta.allure.util.ResultsUtils;
import library.common.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.xmlbeans.impl.xb.xsdschema.All;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static library.reporting.ReportFactory.getReporter;

public class AllureReporter implements ReportManager {

    protected static final Logger logger = LogManager.getLogger(AllureReporter.class);

    public AllureReporter() {
    }

    public void addStepLog(String message) {
        Allure.step(message, Status.SKIPPED);
    }

    public void addStepLog(String type, String message) {
        switch (type) {
            case "FAIL":
                Allure.step(message, Status.FAILED);
                break;
            case "PASS":
                Allure.step(message, Status.PASSED);
                break;
            case "SKIP":
                Allure.step(message, Status.SKIPPED);
                break;
            case "ERROR":
                Allure.step(message, Status.BROKEN);
                break;
            default:
                Allure.step(message);
        }
    }

    public void addScreenCaptureFromPath(String imagePath) {
        Path content = Paths.get(imagePath);
        try (InputStream inputStream = Files.newInputStream(content)) {
            Allure.addAttachment("Screenshot", inputStream);
        } catch (IOException exception) {
            logger.error("Screenshot failed: {}", exception.getMessage());
        }
    }

    public void addDataTable(String tableName, Map<String, Object> dataTable) {
        final StringBuilder dataTableTabSeparated = new StringBuilder();
        if (dataTable != null && !dataTable.isEmpty()) {
            dataTable.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> {
                        dataTableTabSeparated.append(String.join("\t", entry.getKey(), entry.getValue().toString()));
                        dataTableTabSeparated.append("\n");
                    });

            final String attachmentSource = Allure.getLifecycle().prepareAttachment(tableName, "text\tab-separated-values", "csv");
            Allure.getLifecycle().writeAttachment(attachmentSource, new ByteArrayInputStream(dataTableTabSeparated.toString().getBytes(StandardCharsets.UTF_8)));
        }
    }

    public void failScenario(Throwable message) {
        AllureLifecycle lifecycle = Allure.getLifecycle();
        final StatusDetails statusDetails = ResultsUtils.getStatusDetails(message).orElse(new StatusDetails());
        lifecycle.getCurrentTestCase().ifPresent(tcUUID -> lifecycle.updateTestCase(tcUUID, testResult -> testResult.setStatus(Status.FAILED).setStatusDetails(statusDetails)));
    }

    public String getReportPath() {
        String defaultReportPath = Constants.ALLURE_RESULT_PATH;
        String reportPath = Property.getVariable("reportPath");
        return (reportPath == null ? defaultReportPath : reportPath);
    }

    public void saveReport() {
        String currentTimeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        FileHelper.createDirectoryPath(".\\run-reports");
        String allureResultTargetPath = "run-reports/allure-report-" + currentTimeStamp + ".zip";
        try {
            ZipHelper.zipAndCopyDirectory("target/allure-results", allureResultTargetPath);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void addTextLogContent(String logTitle, String content) {
        Allure.attachment(logTitle, content);
    }

    public static void addScreenCaptureFromPath(String imagePath, String title) {
        Path content = Paths.get(imagePath);
        try (InputStream inputStream = Files.newInputStream(content)) {
            Allure.addAttachment(title, inputStream);
        } catch (IOException exception) {
            logger.error("Screenshot failed: {}", exception.getMessage());
        }
    }

    public void addAttachmentToReport(String filepath, String title) {
        Path content = Paths.get(filepath);
        try (InputStream inputStream = Files.newInputStream(content)) {
            Allure.addAttachment(title, inputStream);
        } catch (IOException exception) {
            logger.error(exception.getMessage());
        }
    }

    public static String getScreenshotPath() {
        return getReporter().getReportPath() + "/screenshots/" + TestContext.getInstance().testdataGet("fw.scenarioName") + "/";
    }

    private static String getTestIDFromAfterHook() {
        Optional<String> testID = Allure.getLifecycle().getCurrentTestCase();
        if (testID.isPresent()) {
            Pattern pattern = Pattern.compile(String.format("^%s(.*)after", TestContext.getInstance().testdataGet("fw.featureName")));
            Matcher testIDMatcher = pattern.matcher(testID.get());
            if (testIDMatcher.find()) {
                return testIDMatcher.group(1);
            }
        }
        return "-1";
    }

    public static void addTestIDHistoryObject(String testID) {
        Map<String, String> updateHistory = TestContext.getInstance().testdataToClass("fw.updateHistory", Map.class);
        if (updateHistory == null) {
            updateHistory = new HashMap<>();
        }
        updateHistory.put("allureTestiD", testID);
        TestContext.getInstance().testdataPut("fw.updateHistory", updateHistory);
    }

    public static void setStepStatus(String step, Status status) {
        Allure.step(step, status);
    }

    public static String getReportName() {
        DateFormat dataFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return "RunReport_" + dataFormat.format(new Date()) + ".html";
    }

    public static void startFinalStep(Boolean scenarioIsFailed) {
        String testID = getTestIDFromAfterHook();
        String stepUUID = testID + "finalStep";
        Status stepStatus;
        String stepMessage;
        if (scenarioIsFailed) {
            stepStatus = Status.FAILED;
            stepMessage = "Scenario Failed";
        } else {
            stepStatus = Status.PASSED;
            stepMessage = "Scenario Passed";
        }
        Allure.getLifecycle().startStep(testID, stepUUID, new StepResult().setName(stepMessage).setStatus(stepStatus));
    }

    public static void stopFinalStep() {
        String testID = getTestIDFromAfterHook();
        String stepUUID = testID + "finalStep";
        Allure.getLifecycle().stopStep(stepUUID);
        addTestIDHistoryObject(testID);
    }

    public static void addLink(String name, String url) {
        String testID = getTestIDFromAfterHook();
        Link link = (new Link()).setName(name).setUrl(url);
        Allure.getLifecycle().updateTestCase(testID, testResult -> testResult.getLinks().add(link));
    }


}
