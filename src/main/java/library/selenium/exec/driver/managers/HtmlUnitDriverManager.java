package library.selenium.exec.driver.managers;


import com.gargoylesoftware.htmlunit.BrowserVersion;
import library.selenium.exec.driver.enums.Browsers;
import library.selenium.exec.driver.factory.DriverContext;
import library.selenium.exec.driver.factory.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;


public class HtmlUnitDriverManager extends DriverManager {

    protected Logger logger = LogManager.getLogger(this.getClass().getName());

    @Override
    public void createDriver() {
        Browsers browserType = Browsers.get(DriverContext.getInstance().getBrowserName());

        switch (browserType) {
            case CHROME:
                driver = new HtmlUnitDriver(BrowserVersion.CHROME, true);
                break;
            case FIREFOX:
                driver = new HtmlUnitDriver(BrowserVersion.FIREFOX, true);
                break;
            case EDGE:
                driver = new HtmlUnitDriver(BrowserVersion.EDGE, true);
                break;
        }

    }

    @Override
    public void updateResults(String result) {

    }


}