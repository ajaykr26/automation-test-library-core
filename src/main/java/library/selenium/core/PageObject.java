package library.selenium.core;

import library.common.CommonPageObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static library.engine.core.objectmatcher.ObjectFinder.getMatchingObject;


public class PageObject extends CommonPageObject {

    protected Logger logger = LogManager.getLogger(PageObject.class.getName());
    protected WebDriver driver;
    protected By object;
    protected WebDriverWait wait;
    protected SoftAssertions softAssertions = null;

    public PageObject(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(getWaitDuration()));

    }

    public WebDriver getDriver() {
        logger.debug("obtaining the driver object for current thread");
        return driver;
    }

    public WebDriverWait getWait() {
        logger.debug("obtaining the wait object for current thread");
        return wait;
    }

    public Element $(By by) {
        getWait().until(ExpectedConditions.presenceOfElementLocated(by));
        Element el = new Element(driver, by);
        return el.scroll();
    }

    public List<Element> $$(By by) {
        List<WebElement> els = getWait().until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
        List<Element> list = setElements(els);
        if (list.size() > 0) {
            list.get(0).scroll();
        }
        return list;
    }

    public Element $(String locatorType, String locatorValue) {
        Element el = new Element(driver, getObjectLocatedBy(locatorType, locatorValue));
        return el.scroll();
    }

    public List<Element> $$(String locatorType, String locatorValue) {
        List<WebElement> els = getWait().until(ExpectedConditions.presenceOfAllElementsLocatedBy(getObjectLocatedBy(locatorType, locatorValue)));
        List<Element> elements = setElements(els);
        if (elements.size() > 0) {
            elements.get(0).scroll();
        }
        return elements;
    }

    public Element $(By by, ExpectedCondition<?> exp, int... delay) {
        Element el = new Element(driver, exp, delay);
        return el.scroll();
    }

    public Element $(By by, By sub, int... delay) {

        Element el = new Element(driver, ExpectedConditions.presenceOfNestedElementLocatedBy(by, sub), delay);
        return el.scroll();
    }

    public List<Element> $$(ExpectedCondition<List<WebElement>> exp, int... delay) {
        WebDriverWait wait = new WebDriverWait(getDriver(), delay.length > 0 ? Duration.ofSeconds(delay[0]) : Duration.ofSeconds(getWaitDuration()));
        List<WebElement> els = wait.until(exp);
        List<Element> elements = setElements(els);
        if (elements.size() > 0) {
            elements.get(0).scroll();
        }
        return elements;
    }

    public List<Element> $$(By by, By sub, int... delay) {
        WebDriverWait wait = new WebDriverWait(getDriver(), delay.length > 0 ? Duration.ofSeconds(delay[0]) : Duration.ofSeconds(getWaitDuration()));
        List<WebElement> els = wait.until(ExpectedConditions.presenceOfNestedElementsLocatedBy(by, sub));
        List<Element> elements = setElements(els);
        if (elements.size() > 0) {
            elements.get(0).scroll();
        }
        return elements;
    }

    protected By getObjectLocatedBy(String locatorType, String locatorText) {
        switch (Locator.getLocator(locatorType)) {
            case ID:
                return By.id(locatorText);
            case NAME:
                return By.name(locatorText);
            case CLASS_NAME:
                return By.className(locatorText);
            case XPATH:
                return By.xpath(locatorText);
            case CSS:
                return By.cssSelector(locatorText);
            case LINK_TEXT:
                return By.linkText(locatorText);
            case PARTIAL_LINK_TEXT:
                return By.partialLinkText(locatorText);
            case TAGE_NAME:
                return By.tagName(locatorText);
            default:
                return null;

        }
    }

    public Element getElementLocatedBy(String locatorType, String locatorText) {
        object = getObjectLocatedBy(locatorType, locatorText);
        getWait().until(ExpectedConditions.presenceOfElementLocated(object));
        return new Element(getDriver(), object);

    }

    public List<Element> getElementsLocatedBy(String locatorType, String locatorText) {
        object = getObjectLocatedBy(locatorType, locatorText);
        getWait().until(ExpectedConditions.presenceOfAllElementsLocatedBy(object));
        return new Element(getDriver()).findElements(object);
    }

    public Element getElement(By by) {
        getWait().until(ExpectedConditions.presenceOfElementLocated(by));
        return new Element(getDriver(), by);
    }

    public List<Element> getElements(By by) {
        List<WebElement> els = getWait().until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
        List<Element> list = setElements(els);
        if (list.size() > 0) {
            list.get(0).scroll();
        }
        return list;
    }

    public Element getElement(String objectName, String pageName) {
        object = getMatchingObject(objectName, pageName);
        getWait().until(ExpectedConditions.presenceOfElementLocated(object));
        return new Element(getDriver(), object);
    }

    public List<Element> getElements(String objectName, String pageName) {
        object = getMatchingObject(objectName, pageName);
        getWait().until(ExpectedConditions.presenceOfElementLocated(object));
        return new Element(getDriver()).findElements(object);
    }

    public Element findElement(By by) {
        Element el = new Element(driver, by);
        return el.scroll();
    }

    public Element findElement(String locatorType, String locatorValue) {
        Element el = new Element(driver, getObjectLocatedBy(locatorType, locatorValue));
        return el.scroll();
    }

    public Element findElement(ExpectedCondition<?> exp, int... delay) {
        Element el = new Element(driver, exp, delay);
        return el.scroll();
    }

    public Element findElement(By by, By sub, int... delay) {
        Element el = new Element(driver, ExpectedConditions.presenceOfNestedElementLocatedBy(by, sub), delay);
        return el.scroll();
    }

    public List<Element> findElements(By by, int... delay) {
        WebDriverWait wait = new WebDriverWait(getDriver(), delay.length > 0 ? Duration.ofSeconds(delay[0]) : Duration.ofSeconds(getWaitDuration()));
        try {
            List<WebElement> els = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
            List<Element> elements = setElements(els);
            if (elements.size() > 0) {
                elements.get(0).scroll();
            }
            return elements;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public List<Element> findElements(String locatorType, String locatorValue) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(getWaitDuration()));
        try {
            List<WebElement> els = getWait().until(ExpectedConditions.presenceOfAllElementsLocatedBy(getObjectLocatedBy(locatorType, locatorValue)));
            List<Element> elements = setElements(els);
            if (elements.size() > 0) {
                elements.get(0).scroll();
            }
            return elements;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public List<Element> findElements(ExpectedCondition<List<WebElement>> exp, int... delay) {
        WebDriverWait wait = new WebDriverWait(getDriver(), delay.length > 0 ? Duration.ofSeconds(delay[0]) : Duration.ofSeconds(getWaitDuration()));
        try {
            List<WebElement> els = wait.until(exp);
            List<Element> elements = setElements(els);
            if (elements.size() > 0) {
                elements.get(0).scroll();
            }
            return elements;
        } catch (Exception e) {
            return Collections.emptyList();
        }

    }

    public List<Element> findElements(By by, By sub, int... delay) {
        try {
            List<WebElement> els = getWait().until(ExpectedConditions.presenceOfNestedElementsLocatedBy(by, sub));
            List<Element> elements = setElements(els);
            if (elements.size() > 0) {
                elements.get(0).scroll();
            }
            return elements;
        } catch (Exception e) {
            logger.error(e.toString());
            return Collections.emptyList();
        }
    }

    public List<Element> setElements(List<WebElement> els) {
        List<Element> list = new ArrayList<Element>();
        for (WebElement el : els) {
            list.add(new Element(driver, el));
        }
        return list;
    }

    public int getWaitDuration() {
        final int defaultWait = 10;
        int duration;
        try {
            duration = Integer.parseInt(System.getProperty("fw.explicitWait"));
        } catch (Exception e) {
            duration = defaultWait;
        }
        return duration;
    }

}
