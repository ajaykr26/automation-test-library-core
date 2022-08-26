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
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;


public class EdgeDriverManager extends DriverManager {

    protected Logger logger = LogManager.getLogger(this.getClass().getName());

    @Override
    protected void createDriver() {
        PropertiesConfiguration propertiesConfiguration = Property.getProperties(Constants.RUNTIME_PROP_FILE);
        EdgeOptions edgeOptions = new EdgeOptions();
        Capabilities caps = new Capabilities();

        if (Property.getVariable("cukes.webdrivermanager").equalsIgnoreCase("true")) {
            if (Property.getVariable("cukes.driverversion") != null) {
                WebDriverManager.edgedriver().driverVersion(Property.getVariable("cukes.driverversion")).setup();
            } else {
                WebDriverManager.edgedriver().setup();

            }
        } else {
            System.setProperty("webdriver.edge.driver", getDriverPath("msedgedriver"));
        }
        if (propertiesConfiguration != null) {
            String arguments = propertiesConfiguration.getString("arguments." + DriverContext.getInstance().getBrowserName().replaceAll("\\s", ""));
            edgeOptions.addArguments(arguments);
            edgeOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        }
        edgeOptions.merge(caps.getDesiredCapabilities());
        driver = new EdgeDriver(edgeOptions);
    }

    @Override
    public void updateResults(String result) {

    }
}


