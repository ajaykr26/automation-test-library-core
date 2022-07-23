package library.engine.web.steps;

import io.cucumber.java.en.Given;
import library.common.TestContext;
import library.engine.web.AutoEngWebBaseSteps;
import library.selenium.exec.driver.factory.DriverContext;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.Set;

import static library.engine.core.AutoEngCoreConstants.SELENIUM;
import static library.engine.core.AutoEngCoreParser.parseValue;

public class AutoEngWebLaunch extends AutoEngWebBaseSteps {

    @Given("^the user launches the \"([^\"]*)\" application in a new (?:(window|tab))$")
    public void launchApplication(String applicationName, String location) throws InterruptedException {
        TestContext.getInstance().setActiveWindowType(SELENIUM);
        applicationName = parseValue(applicationName);
        if (location.equalsIgnoreCase("window")) {
            getDriver().navigate().to(applicationName);
            TestContext.getInstance().pushWindowHandles(getDriver().getWindowHandle());
        } else if (location.equalsIgnoreCase("tab")) {
            object = getObjectLocatedBy("cssSelector", "body");
            getElement(object).sendKeys(Keys.CONTROL + "t");
            TestContext.getInstance().windowHandles().add(getDriver().getWindowHandle());
            for (String windowHandle : getDriver().getWindowHandles()) {
                if (!TestContext.getInstance().windowHandles().contains(windowHandle)) {
                    getDriver().switchTo().window(windowHandle);
                    TestContext.getInstance().pushWindowHandles(windowHandle);
                }
            }
            getDriver().get(applicationName);
        }
    }

    @Given("^the user launches the \"([^\"]*)\" application in \"([^\"]*)\" browser in a new (?:(window|tab))$")
    public void launchApplication(String applicationName, String teckstack, String location) {
        TestContext.getInstance().setActiveWindowType(SELENIUM);
        applicationName = parseValue(applicationName);
        teckstack = parseValue(teckstack);
        DriverContext.getInstance().quit();
        DriverContext.getInstance().setDriverContext(teckstack);
        if (location.equalsIgnoreCase("window")) {
            getDriver().navigate().to(applicationName);
            TestContext.getInstance().pushWindowHandles(getDriver().getWindowHandle());
        } else if (location.equalsIgnoreCase("tab")) {
            object = getObjectLocatedBy("cssSelector", "body");
            getElement(object).sendKeys(Keys.CONTROL + "t");
            TestContext.getInstance().windowHandles().add(getDriver().getWindowHandle());
            for (String windowHandle : getDriver().getWindowHandles()) {
                if (!TestContext.getInstance().windowHandles().contains(windowHandle)) {
                    getDriver().switchTo().window(windowHandle);
                    TestContext.getInstance().pushWindowHandles(windowHandle);
                }
            }
            getDriver().get(applicationName);
        }
    }

    @Given("^the user closes all the active browser window$")
    public void closeApplication() {
        DriverContext.getInstance().quit();
    }

}
