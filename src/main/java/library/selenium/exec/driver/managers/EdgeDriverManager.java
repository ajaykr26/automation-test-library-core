package library.selenium.exec.driver.managers;


import io.github.bonigarcia.wdm.WebDriverManager;
import library.common.Property;
import library.selenium.exec.driver.factory.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;


public class EdgeDriverManager extends DriverManager {

    protected Logger logger = LogManager.getLogger(this.getClass().getName());

    @Override
    protected void createDriver() {
        if (Property.getVariable("cukes.webdrivermanager") != null && Property.getVariable("cukes.webdrivermanager").equalsIgnoreCase("true")) {
            if (Property.getVariable("cukes.msedgedriver") != null) {
                WebDriverManager.iedriver().driverVersion(Property.getVariable("cukes.msedgedriver")).setup();
            } else {
                WebDriverManager.edgedriver().setup();

            }
        } else {
            System.setProperty("webdriver.edge.driver", getDriverPath("msedgedriver"));

        }
        EdgeOptions edgeOptions = new EdgeOptions();
        driver = new EdgeDriver(edgeOptions);
        driver.manage().window().maximize();
    }

    @Override
    public void updateResults(String result) {

    }
}


