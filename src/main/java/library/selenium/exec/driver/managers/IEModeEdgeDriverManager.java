package library.selenium.exec.driver.managers;


import io.github.bonigarcia.wdm.WebDriverManager;
import library.common.Constants;
import library.common.Property;
import library.selenium.exec.driver.factory.Capabilities;
import library.selenium.exec.driver.factory.DriverContext;
import library.selenium.exec.driver.factory.DriverManager;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class IEModeEdgeDriverManager extends DriverManager {

    protected Logger logger = LogManager.getLogger(this.getClass().getName());

    @Override
    protected void createDriver() {
        InternetExplorerOptions ieOptions = new InternetExplorerOptions();
        Capabilities caps = new Capabilities();
        System.setProperty("webdriver.ie.driver", getDriverPath("IEDriverServer"));
        ieOptions.attachToEdgeChrome();
        ieOptions.ignoreZoomSettings();
        ieOptions.withEdgeExecutablePath(Constants.EDGE_PATH);

        ieOptions.merge(caps.getDesiredCapabilities());
        driver = new InternetExplorerDriver(ieOptions);
    }

    @Override
    public void updateResults(String result) {

    }
}


