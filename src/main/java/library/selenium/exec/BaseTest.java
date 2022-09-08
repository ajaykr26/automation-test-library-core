package library.selenium.exec;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import library.common.Property;
import library.selenium.exec.driver.factory.DriverContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.Map;

import static library.reporting.ExtentReporter.getExtentReports;


public class BaseTest {


    private static final String PROJECT_NAME = "PROJECT_NAME";
    private static final String BUILD_NUMBER = "BUILD_NUMBER";
    protected final Logger logger = LogManager.getLogger(this.getClass().getName());
    private static AppiumDriverLocalService service;
    public static String reportPath;

    public BaseTest() {
    }

    @BeforeSuite
    public void globalSetup() {
        if (Property.getVariable("cukes.techstack").startsWith("APPIUM")) {
            service = AppiumDriverLocalService.buildDefaultService();
            service.start();
        }

    }

    @AfterSuite
    public void tearDown() {
        if (service != null) {
            service.stop();
        }
        if (getExtentReports()!=null) {
            getExtentReports().flush();
        }

    }

    @BeforeMethod
    public void startUp(Method method, Object[] args) {
        Test test = method.getAnnotation(Test.class);
        Map<String, String> map = (Map<String, String>) args[0];
        if (!System.getProperties().containsKey("fw.cucumberTest"))
            System.setProperty("fw.testDescription", test.description() + "(" + map.get("description") + ")");
        if (Property.getVariable(PROJECT_NAME) != null && !Property.getVariable(PROJECT_NAME).isEmpty())
            System.setProperty("fw.projectName", Property.getVariable(PROJECT_NAME));
        if (Property.getVariable(BUILD_NUMBER) != null && !Property.getVariable(BUILD_NUMBER).isEmpty())
            System.setProperty("fw.buildNumber", Property.getVariable(BUILD_NUMBER));
        DriverContext.getInstance().setDriverContext(map);

    }

    @AfterMethod(groups = {"quitDriver"}, alwaysRun = true)
    public void closeDown(ITestResult result) {
        if (!System.getProperties().containsKey(("fw.cucumberTest"))) {
            DriverContext.getInstance().getDriverManager().updateResults(result.isSuccess() ? "passed" : "failed");
            DriverContext.getInstance().quit();
        }
    }

}
