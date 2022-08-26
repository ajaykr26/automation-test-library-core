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
import org.openqa.selenium.remote.CapabilityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ChromeDriverManager extends DriverManager {

    protected Logger logger = LogManager.getLogger(this.getClass().getName());

    @Override
    public void createDriver() {
        PropertiesConfiguration propertiesConfiguration = Property.getProperties(Constants.RUNTIME_PROP_FILE);
        ChromeOptions chromeOptions = new ChromeOptions();
        Capabilities caps = new Capabilities();

        if (Property.getVariable("cukes.webdrivermanager").equalsIgnoreCase("true")) {
            if (Property.getVariable("cukes.driverversion") != null) {
                WebDriverManager.chromedriver().driverVersion(Property.getVariable("cukes.driverversion")).setup();
            } else {
                WebDriverManager.chromedriver().setup();

            }
        } else {
            System.setProperty("webdriver.chrome.driver", getDriverPath("chromedriver"));
        }
        if (propertiesConfiguration != null) {
            String arguments = propertiesConfiguration.getString("arguments." + DriverContext.getInstance().getBrowserName().replaceAll("\\s", ""));
            chromeOptions.addArguments(arguments);
            chromeOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        }
        chromeOptions.merge(caps.getDesiredCapabilities());

        driver = new ChromeDriver(chromeOptions);
    }

    @Override
    public void updateResults(String result) {

    }
}


