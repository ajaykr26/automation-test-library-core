package library.selenium.exec.driver.managers;

import library.selenium.exec.driver.factory.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class FirefoxDriverManager extends DriverManager {

    protected Logger logger = LogManager.getLogger(this.getClass().getName());

    @Override
    protected void createDriver() {

    }

    @Override
    public void updateResults(String result) {

    }
}
