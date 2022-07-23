package library.selenium.exec.driver.factory;

import library.common.Constants;
import library.common.JSONHelper;
import library.common.Property;
import library.selenium.exec.driver.utils.DriverContextUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.*;


public class DriverContext {

    private static final ThreadLocal<DriverContext> instance = ThreadLocal.withInitial(DriverContext::new);
    private Map<String, String> techStack = null;
    private boolean keepBrowserOpen = false;
    private DriverManager driverManager;

    private DriverContext() {
    }

    public static synchronized DriverContext getInstance() {
        return instance.get();
    }

    public static void removeInstance() {
        instance.remove();
    }

    public WebDriver getDriver() {
        if (driverManager == null) {
            driverManager = DriverFactory.createDriver();
        }
        return driverManager.getDriver();
    }

    public WebDriverWait getWebDriverWait() {
        return driverManager.getWebDriverWait();
    }

    public void setDriverContext(Map<String, String> techStack) {
        setTechStack(techStack);
    }

    public void setDriverContext(String teckstack) {
        techStack = JSONHelper.getJSONAsListOfMaps(Constants.TECHSTACK_PATH + teckstack.toUpperCase() + ".json").get(0);
        setTechStack(techStack);
    }

    public void setDriverPath(String browserName) {
        DriverContextUtil.setDriverPath(browserName);
    }

    public Map<String, String> getTechStack() {
        return this.techStack;
    }

    public void setTechStack(Map<String, String> techStack) {
        this.techStack = techStack;
    }

    public DriverManager getDriverManager() {
        return driverManager;
    }

    public Boolean getKeepBrowserOpen() {
        return this.keepBrowserOpen;
    }

    public void setKeepBrowserOpen(Boolean keepBrowserOpen) {
        this.keepBrowserOpen = keepBrowserOpen;
    }

    public String getBrowserName() {
        return this.techStack.get("browserName");
    }

    public String getServerName() {
        return this.techStack.get("serverName");
    }

    public String getBrowserVersion() {
        if (techStack == null) {
            return null;
        } else {
            return this.techStack.get("version") == null ? this.techStack.get("browser_version") : this.techStack.get("version");
        }
    }

    public String getPlatform() {
        if (techStack == null) {
            return null;
        } else {
            return this.techStack.get("platform") == null ? this.techStack.get("os") + "_" + this.techStack.get("os_version") : this.techStack.get("platform");
        }
    }

    public void quit() {
        if (driverManager != null) {
            driverManager.quitDriver();
            driverManager = null;
        }
    }

}


