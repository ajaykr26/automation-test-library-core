package library.selenium.exec.driver.managers;


import library.common.Constants;
import library.selenium.exec.driver.factory.Capabilities;
import library.selenium.exec.driver.factory.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;


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


