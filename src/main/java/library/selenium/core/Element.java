package library.selenium.core;

import io.appium.java_client.MobileDriver;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import io.cucumber.java.sl.In;
import library.common.Constants;
import library.common.JSONHelper;
import library.common.Property;
import library.common.TestContext;
import library.selenium.exec.driver.factory.DriverContext;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.core.api.SoftAssertions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.*;

import java.security.Key;
import java.time.Duration;
import java.util.*;

import static library.engine.core.objectmatcher.ObjectFinder.getMatchingElement;
import static library.engine.core.objectmatcher.ObjectFinder.getMatchingObject;
import static library.reporting.ReportFactory.getReporter;


public class Element {

    private WebElement element;
    private final WebDriver driver;
    private WebDriverWait wait;
    private By by;
    private static final Logger logger = LogManager.getLogger(Element.class.getName());
    public static final String SCROLLING_TO_ELEMENT_FAILED = "scrolling to element failed";

    public WebDriver getDriver() {
        logger.debug("obtaining the driver object for current thread");
        return driver;
    }

    public WebDriverWait getWait() {
        logger.debug("obtaining the wait object for current thread");
        return wait;
    }

    private int getWaitDuration() {
        final int defaultWait = 10;
        int duration;
        try {
            duration = Integer.parseInt(System.getProperty("fw.explicitWait"));
        } catch (Exception e) {
            duration = defaultWait;
        }
        return duration;
    }

    // -------------------element
    public Element(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(getWaitDuration()));
    }

    public Element(WebDriver driver, WebElement e) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(getWaitDuration()));
        this.element = e;
    }

    public Element(WebDriver driver, WebElement e, By by) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(getWaitDuration()));
        this.element = e;
        this.by = by;
    }

    public Element(WebDriver driver, By by, int... delay) {
        this.driver = driver;
        this.by = by;
        try {
            wait = new WebDriverWait(driver, delay.length > 0 ? Duration.ofSeconds(delay[0]) : Duration.ofSeconds(getWaitDuration()));
            this.element = wait.until(ExpectedConditions.presenceOfElementLocated(by));
            logger.debug("element located successfully:" + by.toString());
        } catch (Exception e) {
            this.element = null;
            logger.debug("element not located:" + by.toString());
            logger.debug(e.getMessage());
        }
    }

    public Element(WebDriver driver, ExpectedCondition<?> exp, int... delay) {
        this.driver = driver;
        this.by = null;
        try {
            wait = new WebDriverWait(driver, delay.length > 0 ? Duration.ofSeconds(delay[0]) : Duration.ofSeconds(getWaitDuration()));
            this.element = (WebElement) wait.until(exp);
        } catch (Exception e) {
            this.element = null;
            logger.debug("element not located:" + by.toString());
            logger.debug(e.getMessage());
        }
    }

    public Element $(String locatorType, String locatorText) {
        By by = getObjectLocatedBy(locatorType, locatorText);
        return new Element(driver, (WebElement) wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(this.element, by)), by);
    }

    public List<Element> $$(String locatorType, String locatorText) {
        List<WebElement> els = (List<WebElement>) wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(this.element, getObjectLocatedBy(locatorType, locatorText)));
        List<Element> list = new ArrayList<Element>();
        for (WebElement el : els) {
            list.add(new Element(driver, el));
        }
        return list;
    }

    public Element findElement(String locatorType, String locatorText) {
        By by = getObjectLocatedBy(locatorType, locatorText);
        return new Element(driver, (WebElement) wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(this.element, by)), by);
    }

    public List<Element> findElements(String locatorType, String locatorText) {
        List<WebElement> els = (List<WebElement>) wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(this.element, getObjectLocatedBy(locatorType, locatorText)));
        List<Element> list = new ArrayList<Element>();
        for (WebElement el : els) {
            list.add(new Element(driver, el));
        }
        return list;
    }

    public Element $(By by) {
        return new Element(driver, (WebElement) getWait().until(ExpectedConditions.presenceOfNestedElementLocatedBy(this.element, by)), by);
    }

    public List<Element> $$(By by) {
        List<WebElement> els = (List<WebElement>) wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(this.element, by));
        List<Element> list = new ArrayList<Element>();
        for (WebElement el : els) {
            list.add(new Element(driver, el));
        }
        return list;
    }

    public Element findElement(By by) {
        return new Element(driver, (WebElement) wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(this.element, by)), by);
    }

    public List<Element> findElements(By by) {
        List<WebElement> els = (List<WebElement>) wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(this.element, by));
        List<Element> list = new ArrayList<Element>();
        for (WebElement el : els) {
            list.add(new Element(driver, el));
        }
        return list;
    }

    public By by() {
        return by;
    }

    public WebElement element() {
        return element;
    }

    private By getObjectLocatedBy(String locatorType, String locatorText) {
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

    //----------------text
    public Element visible(int... retries) {
        this.element = wait.until(ExpectedConditions.visibilityOf(this.element));
        return this;
    }

    public Element clickable(int... retries) {
        try {
            this.element = wait.until(ExpectedConditions.elementToBeClickable(this.element));
        } catch (Exception e) {
            if (!(retries.length > 0 && retries[0] == 0)) {
                this.refind(retries);
                return this.clickable(0);
            } else {
                throw e;
            }
        }
        return this;
    }

    public String getText(int... retries) {
        String str = null;
        try {
            str = this.element.getText();
        } catch (Exception e) {
            if (!(retries.length > 0 && retries[0] == 0)) {
                this.refind(retries);
                return this.getText(0);
            } else {
                throw e;
            }
        }
        return str;
    }

    public String getValue(int... retries) {
        String str = null;
        try {
            str = this.element.getAttribute("value");
        } catch (Exception e) {
            if (!(retries.length > 0 && retries[0] == 0)) {
                this.refind(retries);
                return this.getValue(0);
            } else {
                throw e;
            }
        }
        return str;
    }

    public String getAttribute(String attr, int... retries) {
        try {
            return this.element.getAttribute(attr);
        } catch (Exception e) {
            if (!(retries.length > 0 && retries[0] == 0)) {
                this.refind(retries);
                return this.getAttribute(attr, 0);
            } else {
                throw e;
            }
        }
    }

    public Map<String, String> getAttributes(int... retries) {

        JavascriptExecutor js = (JavascriptExecutor) driver;
        StringBuilder script = new StringBuilder()
                .append("var items = {}; ")
                .append("for (index = 0; index < arguments[0].attributes.length; ++index) ").append("{ ")
                .append("items[arguments[0].attributes[index].name] = arguments[0].attributes[index].value ")
                .append("}; ")
                .append("return items;");

        Map<String, String> list;

        try {
            list = (Map<String, String>) js.executeScript(script.toString(), this.element);
            return list;
        } catch (Exception e) {
            if (!(retries.length > 0 && retries[0] == 0)) {
                this.refind(retries);
                list = (Map<String, String>) js.executeScript(script.toString(), this.element);
                return list;
            } else {
                throw e;
            }
        }
    }

    public void clear(int... retries) {
        try {
            this.element().clear();
        } catch (Exception e) {
            if (!(retries.length > 0 && retries[0] == 0)) {
                this.refind(retries);
                this.clear(0);
            } else {
                throw e;
            }
        }
    }

    public void sendKeys(String val, int... retries) {
        try {
            try {
                this.element().clear();
                this.element().sendKeys(val);
            } catch (Exception e) {
                if (!(retries.length > 0 && retries[0] == 0)) {
                    this.refind(retries);
                    this.sendKeys(val, 0);
                } else {
                    throw e;
                }
            }
        } catch (Exception e) {
            if (checkSendKeysJS()) {
                sendKeysJS(val);
            } else {
                throw e;
            }
        }
        if (!this.getValue().equals(val)){
            sendKeys(val);
        }
    }

    //----------------click
    public void click(int... retries) {
        try {
            try {
                this.element().click();
            } catch (Exception e) {
                if (!(retries.length > 0 && retries[0] == 0)) {
                    this.refind(retries);
                    this.click(0);
                } else {
                    throw e;
                }
            }
        } catch (Exception e) {
            if (checkClickJS()) {
                clickJS();
            } else {
                throw e;
            }
        }
    }

    public Element doubleClick(int... retries) {
        Actions actions = new Actions(driver);
        try {
            actions.doubleClick(this.element).build().perform();
        } catch (Exception e) {
            if (!(retries.length > 0 && retries[0] == 0)) {
                this.refind(retries);
                return this.doubleClick(0);
            } else {
                throw e;
            }
        }
        return this;
    }

    public Element rightClick(int... retries) {
        Actions actions = new Actions(driver);
        try {
            actions.contextClick(this.element).build().perform();
        } catch (Exception e) {
            if (!(retries.length > 0 && retries[0] == 0)) {
                this.refind(retries);
                return this.rightClick(0);
            } else {
                throw e;
            }
        }
        return this;
    }

    public void clickJS() {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", this.element);
    }

    public void sendKeysJS(String val) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].setAttribute('value', '" + val + "')", this.element);
    }

    public Element moveAndClick(WebElement elChild) {
        Actions action = new Actions(driver);
        action.moveToElement(this.element).build().perform();
        elChild.click();
        return this;
    }

    public void clickAndHold() {
        Actions action = new Actions(driver);
        action.clickAndHold(this.element).build().perform();
    }


    //----------send keys
    public void sendKeysChord(String val, int... retries) {
        try {
            this.element.sendKeys(Keys.chord(Keys.CONTROL, "a"), val);
        } catch (Exception e) {
            if (!(retries.length > 0 && retries[0] == 0)) {
                this.refind(retries);
                this.sendKeysChord(val, 0);
            } else {
                throw e;
            }
        }
    }

    public void sendKeysChord(Keys key, int... retries) {
        try {
            this.element.sendKeys(Keys.chord(Keys.CONTROL, "a"), key);
        } catch (Exception e) {
            if (!(retries.length > 0 && retries[0] == 0)) {
                this.refind(retries);
                this.sendKeysChord(key, 0);
            } else {
                throw e;
            }
        }
    }

    public void sendKeyboardKeys(String keyboardKeys, int... retries) {
        JSONArray keysMap = JSONHelper.getJSONArray(Constants.WEB_KEYS_PATH, keyboardKeys);
        StringBuilder keys = new StringBuilder();
        for (Object o : keysMap) {
            keys.append(Keys.valueOf(o.toString()));
        }
        try {
            this.element.sendKeys(keys);
        } catch (Exception e) {
            if (!(retries.length > 0 && retries[0] == 0)) {
                this.refind(retries);
                this.sendKeyboardKeys(keyboardKeys, 0);
            } else {
                throw e;
            }
        }
    }

    public List<String> getAllText(List<Element> els) {
        List<String> elementsText = new ArrayList<String>();
        for (Element el : els) {
            elementsText.add(el.element().getText());
        }
        return elementsText;
    }

    public Element move() {
        Actions action = new Actions(driver);
        action.moveToElement(this.element).build().perform();
        return this;
    }

    public Element release() {
        Actions action = new Actions(driver);
        action.release().build().perform();
        return this;
    }

    public Element highlight() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String script = "arguments[0].style.border";
        String border = "3px solid blue";
        js.executeScript(script + "='" + border + "'", this.element);
        return this;
    }

    public void refind(int... retries) {
        logger.info("Attempting to refind the element: " + by.toString());
        int attempts = 0;
        boolean retry = true;
        int maxRetry = retries.length > 0 ? retries[0] : getFindRetries();
        while (attempts < maxRetry && retry) {
            try {
                logger.debug("retry number " + attempts);
                this.element = getWait().until(ExpectedConditions.presenceOfElementLocated(by));
                this.element.getTagName();
                retry = false;
            } catch (Exception e) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e1) {
                    logger.debug(e1.getMessage());
                }
            }
            attempts++;
        }
    }

    public static int getFindRetries() {
        final int defaultFindRetries = 10;
        int refind;
        try {
            refind = Integer.parseInt(System.getProperty("cukes.selenium.defaultFindRetries"));
        } catch (Exception e) {
            refind = defaultFindRetries;
        }
        return refind;
    }

    public Boolean checkClickJS() {
        Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
        String browserName = cap.getBrowserName().toLowerCase().replaceAll("\\s", "");
        try {
            return Boolean.parseBoolean(TestContext.getInstance().propDataGet("clickUsesJavaScript." + browserName).toString());
        } catch (NullPointerException exception) {
            return false;
        }
    }

    public Boolean checkSendKeysJS() {
        Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
        String browserName = cap.getBrowserName().toLowerCase().replaceAll("\\s", "");
        try {
            return Boolean.parseBoolean(TestContext.getInstance().propDataGet("sendKeysUsesJavaScript." + browserName).toString());
        } catch (NullPointerException exception) {
            return false;
        }
    }

    public Element scroll() {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        if (isOkToScroll()) {
            if ((driver instanceof ChromeDriver) || driver instanceof RemoteWebDriver && ((RemoteWebDriver) driver).getCapabilities().getBrowserName().equalsIgnoreCase("chrome")) {
                try {
                    js.executeScript("arguments[0].scrollIntoView({behavior:'smooth',block:'center'});", element());
                } catch (Exception exception) {
                    logger.warn(SCROLLING_TO_ELEMENT_FAILED, exception);
                }
            } else if ((driver instanceof MobileDriver) && System.getProperty("fw.platformName").equals("android")) {
                scrollToElementOnAndroid();
            } else if ((driver instanceof MobileDriver) && System.getProperty("fw.platformName").equals("ios")) {
                scrollToElementOniOS();
            }
        } else {
            try {
                js.executeScript("arguments[0].scrollIntoView();", element());
            } catch (Exception exception) {
                logger.warn(SCROLLING_TO_ELEMENT_FAILED, exception);
            }
        }
        return this;
    }

    private void scrollToElementOniOS() {
        try {
            Actions actions = new Actions(driver);
            actions.moveToElement(element()).build().perform();
        } catch (Exception exception) {
            logger.warn(SCROLLING_TO_ELEMENT_FAILED, exception);
        }

    }

    private void scrollToElementOnAndroid() {
        int attempt = 2;
        try {
            while (attempt != 0) {
                Dimension size = driver.manage().window().getSize();
                int startVerticalY = (int) (size.height * 0.8);
                int endVerticalY = (int) (size.height * 0.21);
                int startVerticalX = (int) (size.width / 2.1);
                new TouchAction((PerformsTouchActions) driver).press((PointOption.point(startVerticalX, startVerticalY)))
                        .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)))
                        .moveTo(PointOption.point(startVerticalX, endVerticalY))
                        .release().perform();
                attempt--;
            }
        } catch (Exception exception) {
            logger.warn(SCROLLING_TO_ELEMENT_FAILED, exception);
        }
    }

    private boolean isOkToScroll() {
        return isAutoScrollEnabled() && !(driver instanceof AndroidDriver) && !(driver instanceof IOSDriver) && element() != null;
    }

    private boolean isAutoScrollEnabled() {
        return !Boolean.parseBoolean(String.valueOf(TestContext.getInstance().testdataGet("fw.autoScroll")));
    }

    public Element scroll(String hAlign, String vAlign) {

        if (!(driver instanceof AndroidDriver) && !(driver instanceof IOSDriver)) {
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: \"smooth\", block: \"" + vAlign + "\", inline: \"" + hAlign + "\"}););", element());
            } catch (Exception e) {
                logger.warn("scrolling to element failed", e);
            }
        }
        return this;
    }

    public SoftAssertions softAssertions() {
        return TestContext.getInstance().softAssertions();
    }

    public boolean validateElement(String Operation) {
        switch (Operation) {
            case "displayed":
                return this.element().isDisplayed();
            case "enabled":
                return this.element().isEnabled();
            case "disabled":
                return !this.element().isEnabled();
            case "selected":
                return this.element().isSelected();
            case "empty":
                return this.getValue() == null;
            case "not empty":
                return this.getValue() != null;
        }
        return false;
    }

    public void waitForElementToBe(String waitForElementToBe) {
        switch (waitForElementToBe.toLowerCase()) {
            case "displayed":
                getWait().until(ExpectedConditions.visibilityOf(this.element()));
                break;
            case "clickable":
                getWait().until(ExpectedConditions.elementToBeClickable(this.element()));
                break;
            case "selected":
                getWait().until(ExpectedConditions.elementToBeSelected(this.element()));
                break;
        }
    }

    //----------------dropdown
    public List<String> getListOfDropdownOptionText() {
        try {
            return getDropdownOptionsTextOfSelectTag();
        } catch (UnexpectedTagNameException exception) {
            return getDropdownOptionsTextOfNonSelectTag();
        }
    }

    public List<String> getDropdownOptionsTextOfNonSelectTag() {
        List<String> optionsText = new ArrayList<String>();
        List<String> tagNames = Arrays.asList("div", "a");
        for (String tagName : tagNames) {
            List<WebElement> options = this.element.findElements(By.tagName(tagName));
            if (options.size() != 0)
                for (WebElement option : options) {
                    optionsText.add(option.getText());
                }
        }
        return optionsText;
    }

    public List<String> getDropdownOptionsTextOfSelectTag() {
        List<String> optionsText = new ArrayList<String>();
        List<WebElement> options = this.dropdown().getOptions();
        for (WebElement option : options) {
            optionsText.add(option.getText());
        }
        return optionsText;
    }

    public List<String> getListOfDropdownOptionsValues() {
        try {
            return getListOfDropdownOptionsValuesOfSelectTag();
        } catch (UnexpectedTagNameException exception) {
            return getListOfDropdownOptionsValuesOfNonSelectTag();
        }
    }

    public List<String> getListOfDropdownOptionsValuesOfSelectTag() {
        List<String> optionsText = new ArrayList<String>();
        List<WebElement> options = this.dropdown().getOptions();
        for (WebElement option : options) {
            optionsText.add(option.getAttribute("value"));
        }
        return optionsText;
    }

    public List<String> getListOfDropdownOptionsValuesOfNonSelectTag() {
        List<String> optionsText = new ArrayList<String>();
        List<String> tagNames = Arrays.asList("div", "a");
        for (String tagName : tagNames) {
            List<WebElement> options = this.element.findElements(By.tagName(tagName));
            if (options.size() != 0)
                for (WebElement option : options) {
                    optionsText.add(option.getAttribute("value"));
                }
        }
        return optionsText;
    }

    public List<String> getDropdownOptGroupsText() {
        List<String> optGroupsText = new ArrayList<String>();
        List<WebElement> optGroups = this.element().findElements(By.tagName("optgroup"));
        for (WebElement optGroup : optGroups) {
            optGroupsText.add(optGroup.getText());
        }
        return optGroupsText;
    }

    public List<WebElement> getDropdownOptGroupsElements() {
        return this.element().findElements(By.tagName("optgroup"));
    }

    public List<String> getDropdownOptionsTextWithinGroup(String group) {
        List<String> optionsText = new ArrayList<String>();
        List<WebElement> options = this.element().findElements(By.xpath("//optgroup[@label=" + group + "]/option"));
        for (WebElement option : options) {
            optionsText.add(option.getText());
        }
        return optionsText;
    }

    public List<WebElement> getDropdownOptionsElementsWithinGroup(String group) {
        return this.element().findElements(By.xpath("//optgroup[@label=" + group + "]/option"));
    }

    public Select dropdown(int... retries) {
        Select sel = null;
        try {
            sel = new Select(this.element);
        } catch (Exception e) {
            if (!(retries.length > 0 && retries[0] == 0)) {
                this.refind(retries);
                this.dropdown(0);
            } else {
                throw e;
            }
        }
        return sel;
    }

    public void selectValueFromDropdown(String value, String by, int... retries) {
        try {
            selectValueFromDropdownSelectTag(value, by, retries);
        } catch (UnexpectedTagNameException exception) {
            selectValueFromDropdownNonSelectTag(value, retries);
        }
    }

    public void selectValueFromDropdownSelectTag(String value, String by, int... retries) {
        Select select = dropdown();
        switch (by) {
            case "Index":
                select.selectByIndex(Integer.parseInt(value));
                break;
            case "Value":
                select.selectByValue(value);
                break;
            case "VisibleText":
            default:
                select.selectByVisibleText(value);
        }
    }

    public void selectValueFromDropdownNonSelectTag(String value, int... retries) {
        this.click();
        List<WebElement> options = this.element.findElements(By.tagName("div|a|ul|li"));
        if (options.size() != 0)
            for (WebElement option : options) {
                if (option.getAttribute("value").equals(value)) {
                    option.click();
                }
            }
    }

    //-----------checkbox and ra dio button
    public void selectCheckboxOrRadioBtn(int... retries) {
        try {
            if (!this.element.isSelected())
                this.element.click();
        } catch (Exception e) {
            if (!(retries.length > 0 && retries[0] == 0)) {
                this.refind(retries);
                this.selectCheckboxOrRadioBtn(0);
            } else {
                throw e;
            }
        }
    }

    public void unselectCheckboxOrRadioBtn(int... retries) {
        try {
            if (this.element.isSelected())
                this.element.click();
        } catch (Exception e) {
            if (!(retries.length > 0 && retries[0] == 0)) {
                this.refind(retries);
                this.unselectCheckboxOrRadioBtn(0);
            } else {
                throw e;
            }
        }
    }

    public boolean findMatchingCheckboxItemToClick(String valueToClick) {
        switch (valueToClick.toLowerCase()) {
            case "true":
            case "on":
                return !this.element().isSelected();
            case "false":
            case "off":
                return this.element().isSelected();
            default:
                if (this.getValue() != null) {
                    return this.getValue().equals(valueToClick) && !this.element().isSelected();
                }
        }
        return false;
    }

    public boolean findMatchingCheckboxOrRadioBtnFromGroup(String value, String by, int... retries) {
        if (by.equals("Value")) {
            return element.getAttribute("value").equals(value) && !element.isSelected();
        } else if (by.equals("VisibleText")) {
            return element.getText().equals(value) && !element.isSelected();
        }
        return false;
    }

    //------------table
    public int getTableRowCount() {
        return getDataRowCount() + getHeadRowCount();
    }

    public int getHeadRowCount() {
        return this.findElements(By.tagName("th")).size();
    }

    public int getDataRowCount() {
        return this.findElements(By.tagName("tr")).size();
    }

    public int getHeadColumnCount(int rowIndex) {
        return getHeadRowElements(rowIndex).size();
    }

    public int getDataColumnCount(int rowIndex) {
        return getDataRowElements(rowIndex).size();
    }

    public List<Element> getHeadRowElements(int rowIndex) {
        return this.findElements(By.tagName("tr")).get(rowIndex).findElements(By.tagName("th"));
    }

    public List<Element> getDataRowElements(int rowIndex) {
        return this.findElements(By.tagName("tr")).get(rowIndex).findElements(By.tagName("td"));
    }

    public List<Element> getAllRows() {
        return this.findElements(By.tagName("tr"));
    }

    public Element getRow(int row) {
        return this.findElements(By.tagName("tr")).get(row);
    }

    public Element getDataCellElement(int rowIndex, int columnIndex) {
        return getDataRowElements(rowIndex).get(columnIndex);
    }

    public Element getHeadCellElement(int rowIndex, int columnIndex) {
        return getHeadRowElements(rowIndex).get(columnIndex);
    }

    public Element getDataRowElementInCol(String valToFind, int colNum) {
        List<Element> rows = this.findElements(By.tagName("tr"));
        Element el = null;
        for (int i = 1; i < rows.size(); i++) {
            if (rows.get(i).findElements(By.tagName("td")).get(colNum).getText().equalsIgnoreCase(valToFind)) {
                el = rows.get(i);
                break;
            }
        }
        return el;
    }

    public int getDataRowNumInCol(String valToFind, int colNum) {
        List<Element> rows = this.findElements(By.tagName("tr"));
        for (int i = 1; i < rows.size(); i++) {
            if (rows.get(i).findElements(By.tagName("td")).get(colNum).getText().equalsIgnoreCase(valToFind)) {
                return i;
            }
        }
        return -1;
    }

    public int getHeadCellNum(String colName, int...retries) {

        try {
            List<Element> headElements = getHeadRowElements(0);
            for (int i = 1; i < headElements.size(); i++) {
                if (headElements.get(i).getText().equalsIgnoreCase(colName)) {
                    return i;
                }
            }
            return -1;
        } catch (Exception e) {
            if (!(retries.length > 0 && retries[0] == 0)) {
                this.refind(retries);
                this.getHeadCellNum(colName, 0);
            } else {
                throw e;
            }
            return -1;

        }
    }

    public ArrayList<ArrayList<String>> getTableDataAsArray() {
        ArrayList<ArrayList<String>> tableData = new ArrayList<ArrayList<String>>();
        List<WebElement> rows = this.element().findElements(By.tagName("tr"));
        int rowNum = rows.size();
        for (WebElement row : rows) {
            List<WebElement> cols = row.findElements(By.tagName("td"));
            if (cols.size() > 0) {
                ArrayList<String> rowData = new ArrayList<String>();
                for (WebElement col : cols) {
                    rowData.add(col.getText().trim());
                }
                tableData.add(rowData);
            }
        }
        return tableData;
    }

}
