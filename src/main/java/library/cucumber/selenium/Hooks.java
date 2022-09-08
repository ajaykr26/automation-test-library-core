package library.cucumber.selenium;

import io.cucumber.core.api.Scenario;
import io.cucumber.java8.En;
import library.common.Formatter;
import library.common.TestContext;
import library.selenium.exec.driver.factory.DriverContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static library.reporting.ReportFactory.getReporter;

public class Hooks implements En {
    protected static final Logger logger = LogManager.getLogger(Hooks.class.getName());

    public Hooks() {
        Before(30, (Scenario scenario) -> {
        });
        After(30, (Scenario scenario) -> {//1
            logger.info(Formatter.getDataDictionaryAsFormattedTable());
            logger.info(Formatter.getMapAsFormattedTable(TestContext.getInstance().propData()));
            if (DriverContext.getInstance().getDriverManager() != null) {
                if (scenario.isFailed()) {
                    takeScreenShotOnFailure();
                    if (!DriverContext.getInstance().getKeepBrowserOpen())
                        DriverContext.getInstance().quit();
                } else {
                    DriverContext.getInstance().quit();
                }
                DriverContext.getInstance().getDriverManager().updateResults(scenario.isFailed() ? "failed" : "passed");
            }

        });
    }

    private void takeScreenShotOnFailure() {


    }
}