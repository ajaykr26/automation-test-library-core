package library.engine.web.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import library.common.Constants;
import library.common.ExcelHelper;
import library.common.TestContext;
import library.engine.web.AutoEngWebBaseSteps;
import library.selenium.core.Element;
import library.selenium.utils.FileDownloadHelper;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;

import static library.engine.core.AutoEngCoreParser.*;
import static library.engine.core.objectmatcher.ObjectFinder.invokeMethod;
import static library.reporting.ReportFactory.getReporter;

public class AutoEngWebUtils extends AutoEngWebBaseSteps {


    @Then("^the user waits for the page to load$")
    public void waitForPageLoad() {
        waitForPageToLoad();
    }


    @Then("^the user writes the value of \"([^\"]*)\" from data dictionary to excel \"([^\"]*)\" file at the \"([^\"]*)\" worksheet$")
    public void writeValueFromDataDictionaryToExcel(String dictionaryKey, String filepath, String worksheet) {
        filepath = parseValue(filepath);
        File fileToUpdate = new File(filepath);
        if (!fileToUpdate.isAbsolute()) {
            fileToUpdate = new File(Paths.get(Constants.TESTDATA_PATH + filepath).toString());
        }
        ExcelHelper.UpdateDictionaryValueToExistingExcelFile(fileToUpdate, dictionaryKey);
    }


    @Then("^the user waits for the \"([^\"]*)\" element to be \"([^\"]*)\" at the \"([^\"]*)\" page$")
    public void waitForElementToBe(String objectName, String expectedStatus, String pageName) {
        getElement(objectName, pageName).waitForElementToBe(expectedStatus);

    }

    @Then("^the user \"([^\"]*)\" the window alert$")
    public void acceptOrDismissAlert(String action) {
        getWait().until(ExpectedConditions.alertIsPresent());
        try {
            switch (action) {
                case "accept":
                    getDriver().switchTo().alert().accept();
                    break;
                case "dismiss":
                    getDriver().switchTo().alert().dismiss();
                    break;
            }
        } catch (NoAlertPresentException exception) {
            logger.error(exception.getMessage());
        }
    }

    @Then("^the user navigates back on the browser$")
    public void navigateBack() {
        getDriver().navigate().back();
    }

    @Then("^the user navigates forward on the browser$")
    public void navigateForward() {
        getDriver().navigate().forward();
    }

    @Then("^the user refreshes the browser")
    public void refresh() {
        getDriver().navigate().refresh();
    }

    @Then("^the user navigates to the \"([^\"]*)\" url$")
    public void navigateToUrl(String url) {
        url = parseValue(url);
        getDriver().navigate().to(url);
    }

    @Then("^the user switches to the \"([^\"]*)\" frame$")
    public void switchToFrameByFrameNameOrID(String frameNameOrId) {
        getDriver().switchTo().frame(frameNameOrId);
    }

    @Then("^the user switches to the frame located by \"([^\"]*)\" is \"([^\"]*)\"$")
    public void switchToFrameByFrameLocator(String locator) {
        object = getObjectLocatedBy("xpath", locator);
        getWait().until(ExpectedConditions.presenceOfElementLocated(object));
        getDriver().switchTo().frame(getElement(object).element());
    }

    @Then("^the user switches to the frame contains element located by \"([^\"]*)\" is \"([^\"]*)\"$")
    public void switchToFrameByElementLocatedBy(String locator) {
        List<Element> frames = getElementsLocatedBy("xpath", "//iframe");
        for (Element frame : frames) {
            try {
                getDriver().switchTo().parentFrame();
                getDriver().switchTo().frame(frame.element());
            } catch (NoSuchFrameException exception) {
                continue;
            }
            object = getObjectLocatedBy("xpath", locator);
            elements = getElements(object);
            if (elements.size() > 0)
                break;
        }
    }

    @Then("^the user switches to the default content")
    public void switchToDefaultContent() {
        getDriver().switchTo().defaultContent();
    }

    @Then("^the user switches to the parent frame")
    public void switchToParentFrame() {
        getDriver().switchTo().parentFrame();
    }

    @Then("^the user switches to the (?:(window)||(tab)) where title is \"([^\"]*)\" \"([^\"]*)\"$")
    public void switchToWindowByTitle(String comparison, String winTile) {
        for (String windowHandle : getDriver().getWindowHandles()) {
            String windowTitle = getDriver().switchTo().window(windowHandle).getTitle();
            switch (comparison) {
                case "CONTAINS":
                    if (windowTitle.contains(winTile)) {
                        TestContext.getInstance().pushWindowHandles(windowHandle);
                        break;
                    }
                case "EQUALS TO":
                    if (windowTitle.equalsIgnoreCase(winTile)) {
                        TestContext.getInstance().pushWindowHandles(windowHandle);
                        break;
                    }
            }
        }
    }

    @Then("^the user switches to the next window$")
    public void switchToNextWindow() {
        for (String windowHandle : getDriver().getWindowHandles()) {
            if (!TestContext.getInstance().windowHandles().contains(windowHandle)) {
                getDriver().switchTo().window(windowHandle);
                TestContext.getInstance().pushWindowHandles(windowHandle);
                break;
            }
        }
    }

    @Then("^the user switches to the last active window$")
    public void switchToLastActiveWindow() {
        try {
            TestContext.getInstance().popWindowHandle();
            getDriver().switchTo().window(TestContext.getInstance().peekLastWindowHandle());
        } catch (NoSuchWindowException exception) {
            TestContext.getInstance().popWindowHandle();
            getDriver().switchTo().window(TestContext.getInstance().peekLastWindowHandle());
        }

    }

    @Then("^the user switches to (?:window||tab) where the url is \"([^\"]*)\" \"([^\"]*)\"$")
    public void switchToWindowByURL(String comparison, String url) {
        url = parseValue(url);

        for (String windowHandle : getDriver().getWindowHandles()) {
            String currentUrl = getDriver().switchTo().window(windowHandle).getCurrentUrl();

            switch (comparison) {
                case "CONTAINS":
                    if (currentUrl.contains(url)) {
                        TestContext.getInstance().pushWindowHandles(windowHandle);
                        break;
                    }
                case "EQUALS TO":
                    if (currentUrl.equalsIgnoreCase(url)) {
                        TestContext.getInstance().pushWindowHandles(windowHandle);
                        break;
                    }

            }
        }
    }

    @Then("^the user switches to (?:window||tab) contains element located by \"([^\"]*)\" is \"([^\"]*)\"$")
    public void switchToWindowByLocator(String locator, String value) {
        for (String windowHandle : getDriver().getWindowHandles()) {
            getDriver().switchTo().window(windowHandle);
            elements = getElements(getObjectLocatedBy(locator, value));
            if (elements.size() > 0) {
                TestContext.getInstance().pushWindowHandles(windowHandle);
                break;
            }

        }
    }

    @Then("^the user closes the current window in focus and switches to default window$")
    public void closeCurrentWindowAndSwitchDefaultWindow() {
        getDriver().close();
        getDriver().switchTo().defaultContent();
    }

    @Then("^the user closes the current window in focus$")
    public void closeCurrentWindow() {
        getDriver().close();
    }

    @Then("^the user adds cookies with key \"([^\"]*)\" and value \"([^\"]*)\" to the current session$")
    public void addCookie(String key, String value) {
        getDriver().manage().addCookie(new Cookie(key, value));
    }

    @Then("^the user deletes cookies with name \"([^\"]*)\" from the current session$")
    public void deleteCookie(String name) {
        getDriver().manage().deleteCookieNamed(name);
    }

    @Then("^the user deletes all cookies from the current session$")
    public void deleteAllCookies() {
        getDriver().manage().deleteAllCookies();
    }

    @Then("^the user hovers on the \"([^\"]*)\" element at the \"([^\"]*)\" page$")
    public void hoverOnElement(String objectName, String pageName) {
        Actions action = new Actions(getDriver());
        action.moveToElement(getElement(objectName, pageName).element()).build().perform();
    }

    @Then("^the user uploads the \"([^\"]*)\" file to the \"([^\"]*)\" element at the \"([^\"]*)\" page$")
    public void uploadFiles(String filename, String objectName, String pageName) {
        filename = parseValue(filename);
        String filepath = Constants.TESTDATA_PATH + filename;
        File fileToUpload = new File(filepath);
        if (fileToUpload.exists()) {
            getElement(objectName, pageName).sendKeys(filepath);
        } else {
            String message = String.format("file not found at path: %s", fileToUpload);
            logger.error(message);
            getReporter().addStepLog("ERROR", message);
        }
    }

    @Then("^the user downloads the file from the \"([^\"]*)\" element at the \"([^\"]*)\" page and store the path to data dictionary with key \"([^\"]*)\"$")
    public void downloadFile(String objectName, String pageName, String dictionaryKey) throws URISyntaxException {
        String pathToCurrentCerts = setTrustStoreBasedOnEnv();
        FileDownloadHelper downloadHelper = new FileDownloadHelper(getDriver());
        Element downloadLink = getElement(objectName, pageName);

        downloadHelper.setURISpecifiedInAnchorElement(downloadLink);
        File downloadedFile = downloadHelper.downloadFile("");
        if (downloadedFile != null && downloadedFile.exists()) {
            TestContext.getInstance().testdataPut(parseDictionaryKey(dictionaryKey), downloadedFile.getAbsolutePath());
            getReporter().addStepLog(String.format("file downloaded to path: %s", downloadedFile.getAbsolutePath()));
        } else {
            getReporter().addStepLog("unable to download file");
        }
        setTrustStore(pathToCurrentCerts);
    }

    @Given("^the user performs the \"([^\"]*)\" action at the \"([^\"]*)\" Page$")
    public void performProcess(String processName, String pageObject) throws InstantiationException, IllegalAccessException {
        invokeMethod(processName, pageObject);
    }

}
