package library.selenium.exec.driver.factory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileDriver;
import library.common.Constants;
import library.common.Property;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.Set;


public abstract class DriverManager {
    protected WebDriver driver;
    protected WebDriverWait wait;
    private final Logger logger = LogManager.getLogger(this.getClass().getName());

    protected DriverManager() {
    }

    public WebDriver getDriver() {
        if (driver == null) {
            createDriver();
        }
        if (driver instanceof MobileDriver && ((AppiumDriver) driver).getContext().equals("NATIVE_APP")) {
            ((AppiumDriver) driver).hideKeyboard();
        }
        return driver;
    }

    public WebDriverWait getWebDriverWait() {
        if (wait == null) {
            wait = new WebDriverWait(driver, Duration.ofSeconds(getWaitDuration()));
        }
        return wait;
    }

    public void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    public int getWaitDuration() {
        final int defaultWait = 30;
        int duration;
        try {
            duration = Integer.parseInt(System.getProperty("fw.explicitWait"));
        } catch (Exception e) {
            duration = defaultWait;
        }
        return duration;
    }

    public String getDriverPath(String driver) {
        String extension = System.getProperty("os.name").split(" ")[0].equalsIgnoreCase("windows") ? ".exe" : " ";
        String driverPath = Property.getVariable("cukes.driverPath");
        return (driverPath == null ? Constants.DRIVER_PATH + System.getProperty("os.name").split(" ")[0].toLowerCase() + File.separator + driver + extension : driverPath);
    }

    public boolean switchContext(String contextToFind) {
        if (driver instanceof MobileDriver) {
            int waitBetween = Integer.parseInt(System.getProperty("fw.waitBetweenConnectionRequest"));
            int retry = 0;
            while (retry < 2) {
                try {
                    Set<String> contextNames = ((AppiumDriver) driver).getContextHandles();
                    for (String currentContext : contextNames) {
                        if (checkAndSwitchContext(contextToFind, waitBetween, currentContext))
                            return true;
                    }
                } catch (WebDriverException | InterruptedException exception) {
                    logger.error(exception);
                    Thread.currentThread().interrupt();
                }
                retry++;
            }
        }
        return false;
    }

    private boolean checkAndSwitchContext(String contextToFind, int waitBetween, String currentContext) throws InterruptedException {
        if (currentContext.contains(contextToFind)) {
            ((AppiumDriver) driver).context(contextToFind);
            if (((AppiumDriver) driver).getContext().equals(contextToFind)) {
                logger.debug("Driver switched to context: {}", contextToFind);
                return true;
            } else {
                logger.debug("retrying switching to context: {}", contextToFind);
                Thread.sleep(waitBetween);
            }
        }
        return false;
    }

    protected abstract void createDriver();

    public abstract void updateResults(String result);

}


