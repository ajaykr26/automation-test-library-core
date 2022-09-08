package library.engine.core;

import io.cucumber.java8.En;
import library.common.Constants;
import library.common.Property;
import library.common.TestContext;
import library.engine.core.objectmatcher.ObjectNotFoundException;
import library.selenium.core.Element;
import library.selenium.core.Locator;
import library.selenium.exec.BasePO;
import library.selenium.exec.driver.factory.DriverContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static library.engine.core.objectmatcher.ObjectFinder.*;
import static library.reporting.ReportFactory.getReporter;

public class AutoEngCoreBaseStep implements En {

    protected final Logger logger = LogManager.getLogger(AutoEngCoreBaseStep.class);

    protected static final String ENV_PROP = "cukes.env";
    protected static final String PROPERTIES_EXT = ".properties";
    protected static final String ERROR = "ERROR";
    protected static final String REGEX_VALIDATION = "\"([^\"]*)\"";
    protected static final String REGEX_KEY = "(:?@\\(\\S*?\\)|#\\(\\S*?\\))";
    protected static final String CLICKED_VALUE = "Clicked value: \"%s\"";
    protected static final String SELECTED_VALUE = "Selected value: \"%s\"";
    protected static final String ENTERED_VALUE = "Entered value: \"%s\"";
    protected static final String STORED_VALUE = "Value stored in data dictionary: {\"%s\":\"%s\"}";
    protected static final String FORMATTED_AS = "Formatted as Day suffix {}";
    protected static final String JSON = ".json";
    protected static final String ZONE = "ZONE_";
    protected static final String JAVAX_NET_SSL_TRUST_STORE = "javax.net.ssl.trustStore";
    protected static final String ELEMENT_REF = "fw.elementRef";
    public static final String WINDOW_SWITCH_DELAY = "fw.windowSwitchDelay";
    public static final String COULD_NOT_FIND_UNIQUE_ROW = "Could not find unique row with {} having and {} having and {} having {}";
    private BasePO baseWebPO;
    protected static Element element = null;
    protected static By object = null;
    protected static List<Element> elements = new ArrayList<>();


    public AutoEngCoreBaseStep() {
    }

    public enum ScreenshotType {
        DISPLAY, AREA, SCROLLING
    }

    public WebDriver getDriver() {
        logger.debug("obtaining an instance of base page object");
        return DriverContext.getInstance().getDriver();
    }

    public WebDriverWait getWait() {
        logger.debug("obtaining the wait for current thread");
        return DriverContext.getInstance().getWebDriverWait();
    }

    public By getObjectLocatedBy(String locatorType, String locatorValue) {
        switch (Locator.getLocator(locatorType)) {
            case ID:
                return By.id(locatorValue);
            case NAME:
                return By.name(locatorValue);
            case CLASS_NAME:
                return By.className(locatorValue);
            case XPATH:
                return By.xpath(locatorValue);
            case CSS:
                return By.cssSelector(locatorValue);
            case LINK_TEXT:
                return By.linkText(locatorValue);
            case PARTIAL_LINK_TEXT:
                return By.partialLinkText(locatorValue);
            case TAGE_NAME:
                return By.tagName(locatorValue);
            default:
                return null;

        }
    }

    public By getObject(String objectName, String pageName) {
        return getMatchingObject(objectName, pageName);
    }

    public Element getElementLocatedBy(String locator, String value) {
        object = getObjectLocatedBy(locator, value);
        getWait().until(ExpectedConditions.visibilityOfElementLocated(object));
        return new Element(getDriver(), object);

    }

    public Element getElement(String objectName, String pageName) {
        return getMatchingElement(objectName, pageName);
    }

    public List<Element> getElementsLocatedBy(String locator, String value) {
        object = getObjectLocatedBy(locator, value);
        getWait().until(ExpectedConditions.presenceOfAllElementsLocatedBy(object));
        return new Element(getDriver()).findElements(object);
    }

    protected List<Element> getElements(String objectName, String pageName) {
        List<Element> elements = getMatchingElements(objectName, pageName);
        if (elements.size() > 0) {
            TestContext.getInstance().testdataPut(ELEMENT_REF, elements);
            return elements;
        } else {
            throw new ObjectNotFoundException(String.format("the \"%s\" element is not present on the page: \"%s\"", objectName, pageName));
        }
    }

    public Element getElement(By by) {
        getWait().until(ExpectedConditions.visibilityOfElementLocated(by));
        return new Element(getDriver(), by);
    }

    public List<Element> getElements(By by) {
        getWait().until(ExpectedConditions.visibilityOfElementLocated(object));
        return new Element(getDriver()).findElements(object);
    }

    public SoftAssertions softAssertions() {
        return TestContext.getInstance().softAssertions();
    }

    protected String setTrustStoreBasedOnEnv() {
        String envPropsFile = Constants.ENVIRONMENT_PROP_FILE;
        String certFile = Property.getProperty(envPropsFile, "certFileForEnv");
        if (certFile != null) {
            String pathForCerts = Paths.get(Constants.ENVIRONMENT_PROP_FILE + certFile).toAbsolutePath().toString();
            String currentCertFile = Property.getVariable(JAVAX_NET_SSL_TRUST_STORE);
            logger.debug(currentCertFile);
            return currentCertFile;
        } else {
            logger.warn("path not defined");
            return "";
        }
    }

    protected void setTrustStore(String pathToCertFile) {
        if (pathToCertFile != null) {
            System.setProperty(JAVAX_NET_SSL_TRUST_STORE, pathToCertFile);
            logger.debug("switching {} to {}", JAVAX_NET_SSL_TRUST_STORE, pathToCertFile);
        }
    }

    public void performElementOperation(String Operation, String value, String by) {
        switch (Operation) {
            case "sendKeys":
                element.clickable().sendKeys(value);
                break;
            case "click":
                element.clickable().click();
                break;
            case "select":
                switch (by) {
                    case "VisibleText":
                        element.clickable().dropdown().selectByVisibleText(value);
                        break;
                    case "Index":
                        element.clickable().dropdown().selectByIndex(Integer.parseInt(value));
                        break;
                    case "Value":
                        element.clickable().dropdown().selectByValue(value);
                        break;
                }
                break;
            case "validate":
                switch (by) {
                    case "VisibleText":
                        softAssertions().assertThat(element.getText()).isEqualTo(value);
                        break;
                    case "selectedOption":
                        softAssertions().assertThat(element.dropdown().getFirstSelectedOption()).isEqualTo(value);
                        break;
                    case "visible":
                        softAssertions().assertThat(element.element().isDisplayed()).isEqualTo(Boolean.parseBoolean(value));
                        break;
                    case "enabled":
                        softAssertions().assertThat(element.element().isEnabled()).isEqualTo(Boolean.parseBoolean(value));
                        break;
                    case "selected":
                        softAssertions().assertThat(element.element().isSelected()).isEqualTo(Boolean.parseBoolean(value));
                        break;
                    default:
                        softAssertions().assertThat(element.getAttribute(by)).isEqualTo(value);
                        break;
                }
                break;
            case "wait":
                switch (by) {
                    case "visible":
                        getWait().until(ExpectedConditions.visibilityOf(element.element()));
                        break;
                    case "clickable":
                        getWait().until(ExpectedConditions.elementToBeClickable(element.element()));
                        break;
                    case "text":
                        getWait().until(ExpectedConditions.textToBePresentInElement(element.element(), value));
                        break;
                }
                break;
        }
    }

    protected String parseDictionaryKey(String keyName) {
        return getValueOrVariable(keyName);
    }

    private String getValueOrVariable(String value) {
        return AutoEngCoreParser.getValueOrVariable(value);
    }

    protected Object parseValueToObject(String value) {
        return AutoEngCoreParser.getValueToObject(value);
    }

    public boolean isFileDownloaded(String downloadPath, String fileName) {
        File dir = new File(downloadPath);
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.getName().equals(fileName)) {
                file.deleteOnExit();
                return true;
            }
        }
        return false;
    }

    protected void storeValueIntoDataDictionary(String valueToStore, String dictionaryKey) {
        TestContext.getInstance().testdataPut(dictionaryKey, valueToStore);
        getReporter().addStepLog(String.format(STORED_VALUE, dictionaryKey, valueToStore));
        logger.info(String.format(STORED_VALUE, dictionaryKey, valueToStore));
    }

}
