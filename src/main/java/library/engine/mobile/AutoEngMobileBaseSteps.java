package library.engine.mobile;

import library.engine.core.AutoEngCoreBaseStep;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;

public class AutoEngMobileBaseSteps extends AutoEngCoreBaseStep {

    protected void launchMobileApplication(String applicationName, WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Map<String, String> startapp = new HashMap<>();
        startapp.put("name", applicationName);
        js.executeScript("mobile:application:open", startapp);
    }

    protected void closeMobileApplication(String applicationName, WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Map<String, String> startapp = new HashMap<>();
        startapp.put("name", applicationName);
        js.executeScript("mobile:application:close", startapp);
    }

}
