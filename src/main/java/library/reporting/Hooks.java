package library.reporting;

import io.cucumber.core.api.Scenario;
import io.cucumber.java8.En;
import library.common.Constants;
import library.common.Property;
import library.common.TestContext;
import library.common.Formatter;

import static library.reporting.ReportFactory.getReporter;
import static library.selenium.core.Screenshot.insertImageToWord;

public class Hooks implements En {

    public Hooks() {
        After(10, (Scenario scenario) -> {//3
            String logfile = Property.getVariable("fw.logFileName") + ".log";
            getReporter().addAttachmentToReport(Constants.LOG_PATH + logfile, logfile);
            if (scenario.isFailed()) {
                String screenshotOnFailure = System.getProperty("fw.screenshotOnFailure");
                if (Boolean.parseBoolean(screenshotOnFailure)) {
                    String screenshotPath = TestContext.getInstance().testdataGet("fw.screenshotAbsolutePath").toString();
                    if (screenshotPath != null) {
                        getReporter().addScreenCaptureFromPath(screenshotPath);
                    }
                }
            }

            insertImageToWord(scenario.getName(), Boolean.parseBoolean(System.getProperty("fw.addScreenshotToWord")));
        });

    }
}
