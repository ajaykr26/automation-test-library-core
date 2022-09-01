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
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;


public class FirefoxDriverManager extends DriverManager {

    protected Logger logger = LogManager.getLogger(this.getClass().getName());

    @Override
    protected void createDriver() {

        PropertiesConfiguration propertiesConfiguration = Property.getProperties(Constants.RUNTIME_PROP_FILE);
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        Capabilities caps = new Capabilities();

        if (Property.getVariable("cukes.webdrivermanager").equalsIgnoreCase("true")) {
            if (Property.getVariable("cukes.driverversion") != null) {
                WebDriverManager.firefoxdriver().driverVersion(Property.getVariable("cukes.driverversion")).setup();
            } else {
                WebDriverManager.firefoxdriver().setup();

            }
        } else {
            System.setProperty("webdriver.gecko.driver", getDriverPath("geckodriver"));
        }
        if (propertiesConfiguration != null) {
            String arguments = propertiesConfiguration.getString("arguments." + DriverContext.getInstance().getBrowserName().replaceAll("\\s", ""));
            firefoxOptions.addArguments(arguments);
        }
        firefoxOptions.merge(caps.getDesiredCapabilities());
        driver = new FirefoxDriver(firefoxOptions);

    }

    @Override
    public void updateResults(String result) {

    }
}
