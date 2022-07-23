package library.engine.core.runner;

import cucumber.api.Result;
import io.cucumber.java8.En;
import library.common.Constants;
import library.common.Property;
import library.common.TestContext;
import library.selenium.exec.driver.factory.DriverContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.io.File;

import static cucumber.api.Result.Type.FAILED;
import static cucumber.api.Result.Type.PASSED;
import static library.engine.core.AutoEngCoreConstants.PDF;
import static library.engine.core.AutoEngCoreConstants.SELENIUM;
import static library.reporting.AllureReporter.getScreenshotPath;
import static library.selenium.core.Screenshot.grabScreenshot;
import static library.selenium.core.Screenshot.saveScreenshot;

public class ScreenshotHandler implements En {

    private boolean waitForPageToLoad;
    private static final String VALIDATE = "validate";
    private static final String SCROLLING_SCREENSHOT_FLAG = "fw.scrollingScreenshot";
    public static final String ERROR_RESPONSE = "-1";

    private final Logger logger = LogManager.getLogger(this.getClass().getName());

    public String addScreenshotPath(String stepText, Result.Type status) {
        String screenshotPath = ERROR_RESPONSE;

        setWaitForPageLoad(stepText);
        if (isStepExecuted(status) && !isStepRequiredScreenshot(stepText)) {
            if (Boolean.parseBoolean(System.getProperty("fw.screenshotOnEveryStep"))) {
                screenshotPath = takeScreenshot();
            } else if (isValidationStep(stepText) && Boolean.parseBoolean(System.getProperty("fw.screenshotOnValidation"))) {
                screenshotPath = takeScreenshot();
            } else if (status == FAILED || isSoftAssertionFailure(stepText) && Boolean.parseBoolean(System.getProperty("fw.screenshotOnFailure"))) {
                screenshotPath = takeScreenshot();
            }
        }
        return screenshotPath;

    }

    private String takeScreenshot() {
        String screenshotPath = ERROR_RESPONSE;
        File file = null;
        String activeWindowType = TestContext.getInstance().getActiveWindowType();
        if (activeWindowType != null) {
            switch (activeWindowType) {
                case SELENIUM:
                    file = getWebScreenshot();
                    break;
                case PDF:
                default:
                    break;
            }
        }
        if (file != null && file.exists()) {
            screenshotPath = file.getAbsolutePath();
        }
        return screenshotPath;
    }

    private File getWebScreenshot() {
        try {
            if (DriverContext.getInstance().getDriver().getWindowHandle() != null && !(DriverContext.getInstance().getDriver() instanceof HtmlUnitDriver)) {
                if (waitForPageToLoad) {
                    logger.info("implement");
                }
                return saveScreenshot(grabScreenshot(DriverContext.getInstance().getDriver()), getScreenshotPath());
            }
        } catch (NoSuchWindowException exception) {
            logger.debug("window already closed, skipping screenshot");
        }

        return null;
    }

    private void setWaitForPageLoad(String stepText) {
        waitForPageToLoad = Boolean.parseBoolean(System.getProperty("fw.waitForPageLoad")) &&
                isStepRefreshRequired(stepText);
        if (Boolean.parseBoolean(System.getProperty("fw.scrollingScreenshot"))) {
            try {
                if (stepText.contains(VALIDATE)) {
                    System.setProperty(SCROLLING_SCREENSHOT_FLAG, "true");
                } else {
                    System.setProperty(SCROLLING_SCREENSHOT_FLAG, "false");
                }
            } catch (Exception exception) {
                logger.warn("Validate scrollingScreenshot flag in runtime.properties");
            }
        }

    }

    private boolean isStepRefreshRequired(String stepText) {
        return stepText.contains(VALIDATE) || stepText.contains("clicks");
    }

    private boolean isStepRequiredScreenshot(String stepText) {
        return stepText.contains("pop up") || stepText.contains("closes") || stepText.contains("pop-up") || stepText.contains("alert");
    }

    private boolean isStepExecuted(Result.Type status) {
        return status == FAILED || status == PASSED;
    }

    private boolean isValidationStep(String stepText) {
        return stepText.contains(VALIDATE);
    }

    private boolean isSoftAssertionFailure(String stepText) {
        return stepText.contains("ContinueOnFailure") && !TestContext.getInstance().softAssertions().wasSuccess();
    }

}
