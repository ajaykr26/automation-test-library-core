package library.selenium.exec.driver.managers;


import library.common.Constants;
import library.selenium.exec.driver.factory.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class IEModeEdgeDriverManager extends DriverManager {

    protected Logger logger = LogManager.getLogger(this.getClass().getName());

    @Override
    protected void createDriver() {

        System.setProperty("webdriver.ie.driver", getDriverPath("IEDriverServer32"));
        InternetExplorerOptions ieOptions = new InternetExplorerOptions();
        ieOptions.attachToEdgeChrome();
        ieOptions.ignoreZoomSettings();
        ieOptions.withEdgeExecutablePath(Constants.EDGE_PATH);

        driver = new InternetExplorerDriver(ieOptions);
        driver.manage().window().maximize();

    }

    @Override
    public void updateResults(String result) {

    }
}


