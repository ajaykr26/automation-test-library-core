package library.engine.web;

import library.common.Constants;
import library.common.JSONHelper;
import library.common.TestContext;
import library.engine.core.AutoEngCoreBaseStep;

import library.selenium.core.Element;
import library.selenium.exec.BasePO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.time.Duration;
import java.util.*;
import java.util.List;

import static library.engine.core.AutoEngCoreParser.parseValue;
import static library.reporting.ReportFactory.getReporter;

public class AutoEngWebBaseSteps extends AutoEngCoreBaseStep {
    protected Logger logger = LogManager.getLogger(AutoEngWebBaseSteps.class.getName());

    private BasePO basePO;

    public AutoEngWebBaseSteps() {
    }

    public BasePO getBasePO() {
        logger.debug("obtaining an instance of base page object");
        if (basePO == null)
            basePO = new BasePO();
        return basePO;
    }

    public void selectCheckboxOrRadioBtnFromGroup(String value, String by, String objectName, String pageName, int... retries) {
        List<Element> chekBoxItems = getElements(objectName, pageName);
        for (Element chekBoxItem : chekBoxItems) {
            if (chekBoxItem.findMatchingCheckboxOrRadioBtnFromGroup(value, by)) {
                chekBoxItem.click();
                getReporter().addStepLog(String.format(CLICKED_VALUE, value));
                break;
            }
        }
    }

    public void unselectCheckboxOrRadioBtnFromGroup(String value, String by, String objectName, String pageName, int... retries) {
        List<Element> chekBoxItems = getElements(objectName, pageName);
        for (Element chekBoxItem : chekBoxItems) {
            if (!chekBoxItem.findMatchingCheckboxOrRadioBtnFromGroup(value, by)) {
                chekBoxItem.click();
                getReporter().addStepLog(String.format(CLICKED_VALUE, value));
                break;
            }
        }
    }

    public void selectOptionFromRadioButtonGroup(String objectName, String value, String by, String pageName) {
        object = getObject(objectName, pageName);
        List<WebElement> radioButtonGroup = getDriver().findElements(object);
        for (WebElement rb : radioButtonGroup) {
            if (by.equals("Value")) {
                if (rb.getAttribute("value").equals(value) && !rb.isSelected())
                    rb.click();
            } else if (by.equals("Text")) {
                if (rb.getText().equals(value) && !rb.isSelected())
                    rb.click();
            }
        }
    }

    public void enterText(String text, String objectName, String pageName) {
        object = getObject(objectName, pageName);
        getWait().until(ExpectedConditions.presenceOfElementLocated(object));
        getDriver().findElement(object).sendKeys(text);
    }

    public void clearText(String objectName, String pageName) {
        object = getObject(objectName, pageName);
        getWait().until(ExpectedConditions.presenceOfElementLocated(object));
        getDriver().findElement(object).clear();
    }

    public void checkCheckbox(String objectName, String pageName) {
        object = getObject(objectName, pageName);

        WebElement checkbox = getWait().until(ExpectedConditions.presenceOfElementLocated(object));
        if (!checkbox.isSelected())
            checkbox.click();
    }

    public void uncheckCheckbox(String objectName, String pageName) {
        object = getObject(objectName, pageName);

        WebElement checkbox = getWait().until(ExpectedConditions.presenceOfElementLocated(object));
        if (checkbox.isSelected())
            checkbox.click();
    }

    public void selectRadioButton(String objectName, String pageName) {
        object = getObject(objectName, pageName);
        WebElement radioButton = getWait().until(ExpectedConditions.presenceOfElementLocated(object));
        if (!radioButton.isSelected())
            radioButton.click();
    }

    public void scrollToElement(String objectName, String pageName) {
        object = getObject(objectName, pageName);
        getWait().until(ExpectedConditions.presenceOfElementLocated(object));
        JavascriptExecutor executor = (JavascriptExecutor) getDriver();
        executor.executeScript("arguments[0].scrollIntoView();", getElement(object));
    }

    public void waitForElementToBeDisplayed(String objectName, String pageName, String duration) {
        WebDriverWait wait = (new WebDriverWait(getDriver(), Duration.ofSeconds(Integer.parseInt(duration))));
        wait.until(ExpectedConditions.visibilityOfElementLocated(getObject(objectName, pageName)));
    }

    public void waitForElementToBeClickable(String objectName, String pageName, String duration) {
        WebDriverWait wait = (new WebDriverWait(getDriver(), Duration.ofSeconds(Integer.parseInt(duration))));
        wait.until(ExpectedConditions.elementToBeClickable(getObject(objectName, pageName)));
    }

//    -----------------------------

    public String getElementText(String locatorType, String locatorText) {
        object = getObjectLocatedBy(locatorType, locatorText);
        getWait().until(ExpectedConditions.presenceOfElementLocated(object));
        element = new Element(getDriver(), object);
        return element.getText();

    }

    public boolean isElementEnabled(String locatorType, String locatorText) {
        object = getObjectLocatedBy(locatorType, locatorText);
        getWait().until(ExpectedConditions.presenceOfElementLocated(object));
        return getDriver().findElement(object).isEnabled();
    }

    public String getElementAttribute(String locatorType, String locatorText, String attributeName) {
        object = getObjectLocatedBy(locatorType, locatorText);
        getWait().until(ExpectedConditions.presenceOfElementLocated(object));

        return element.getAttribute(attributeName);
    }

    public void StoreElementText(String locatorType, String locatorText, String dictionaryKey) {
        dictionaryKey = parseValue(dictionaryKey);
        object = getObjectLocatedBy(locatorType, locatorText);
        getWait().until(ExpectedConditions.presenceOfElementLocated(object));
        element = new Element(getDriver(), object);
        String elementText = element.getText();
        TestContext.getInstance().testdataPut(dictionaryKey, elementText);

    }

    public void checkElementText(String locatorType, String actualValue, String locatorText) {
        String elementText = getElementText(locatorType, locatorText);


    }

    public void validateElementText(String matchType, String locatorType, String actualValue, String locatorText) {
        String elementText = getElementText(locatorType, locatorText);
        switch (matchType) {
            case "exact-match":
                if (!elementText.equals(actualValue)) {
                    getReporter().addStepLog("Text Matched");
                }
            case "not matched":
                if (!elementText.equals(actualValue)) {
                    getReporter().addStepLog("Text Not Matched");
                } else {
                }
        }

    }

    public void checkElementPartialText(String locatorType, String actualValue, String locatorText) {
        String elementText = getElementText(locatorType, locatorText);

    }

    public void checkElementEnable(String locatorType, String locatorText) {
        boolean result = isElementEnabled(locatorType, locatorText);

    }

    public void checkElementAttribute(String locatorType, String attributeName, String attributeValue, String locatorText) {
        String attrVal = getElementAttribute(locatorType, locatorText, attributeName);

    }

    public boolean isElementDisplayed(String locatorType, String locatorText) {
        object = getObjectLocatedBy(locatorType, locatorText);
        getWait().until(ExpectedConditions.presenceOfElementLocated(object));
        return getDriver().findElement(object).isDisplayed();
    }

    public void isCheckboxChecked(String locatorType, String locatorText, boolean shouldBeChecked) {
        WebElement checkbox = getWait().until(ExpectedConditions.presenceOfElementLocated(getObjectLocatedBy(locatorType, locatorText)));

    }

    public void isRadioButtonSelected(String locatorType, String locatorText, boolean shouldBeSelected) {
        WebElement radioButton = getWait().until(ExpectedConditions.presenceOfElementLocated(getObjectLocatedBy(locatorType, locatorText)));

    }

    public void isOptionFromRadioButtonGroupSelected(String locatorType, String by, String option, String locatorText, boolean shouldBeSelected) {
        List<WebElement> radioButtonGroup = getWait().until(ExpectedConditions.presenceOfAllElementsLocatedBy(getObjectLocatedBy(locatorType, locatorText)));

        for (WebElement rb : radioButtonGroup) {
            if (by.equals("value")) {
                if (rb.getAttribute("value").equals(option)) {

                }
            } else if (rb.getText().equals(option)) {

            }
        }
    }

    public void waitForElementToDisplay(String locatorType, String locatorText, String duration) {
        By byEle = getObject(locatorType, locatorText);
        WebDriverWait wait = (new WebDriverWait(getDriver(), Duration.ofSeconds(Integer.parseInt(duration))));
        wait.until(ExpectedConditions.visibilityOfElementLocated(byEle));
    }

    public void waitForElementToClick(String locatorType, String locatorText, String duration) {
        By byEle = getObject(locatorType, locatorText);
        WebDriverWait wait = (new WebDriverWait(getDriver(), Duration.ofSeconds(Integer.parseInt(duration))));
        wait.until(ExpectedConditions.elementToBeClickable(byEle));
    }

    public void isOptionFromDropdownSelected(String locatorType, String by, String option, String locatorText, boolean shouldBeSelected) {
        Select selectList = null;
        WebElement dropdown = getWait().until(ExpectedConditions.presenceOfElementLocated(getObjectLocatedBy(locatorType, locatorText)));
        selectList = new Select(dropdown);

        String actualValue = "";
        if (by.equals("text"))
            actualValue = selectList.getFirstSelectedOption().getText();
        else
            actualValue = selectList.getFirstSelectedOption().getAttribute("value");


    }

    //    --------------------------------------
    public String getPageTitle() {
        return getDriver().getTitle();
    }

    public String getAlertText() {
        return getDriver().switchTo().alert().getText();
    }

    public void keyboardActions(String action, String key) {
        Actions actions = new Actions(getDriver());
        switch (action.toLowerCase()) {
            case "press":
                actions.keyDown(Keys.valueOf(key.toUpperCase())).build().perform();
                break;
            case "release":
                actions.keyUp(Keys.valueOf(key.toUpperCase())).build().perform();
                break;
            case "send":
            default:
                actions.sendKeys(Keys.valueOf(key.toUpperCase())).build().perform();
                break;

        }
    }

    public void validateTitle(String title) {
        String pageTitle = getPageTitle();
    }

    public void validatePartialTitle(String partialTitle) {
        String pageTitle = getPageTitle();

    }

    public void selectValueFromDropdown(Select selectElement, String by, String value) {
        switch (by) {
            case "Index":
                int index = Integer.parseInt(value);
                selectElement.selectByIndex(index - 1);
                break;
            case "Value":
                selectElement.selectByValue(value);
                break;
            case "Text":
                selectElement.selectByVisibleText(value);
                break;
        }
    }

    public void checkAlertText(String text) {
        getAlertText().equals(text);
    }

    public void scrollPage(String to) {
        JavascriptExecutor executor = (JavascriptExecutor) getDriver();
        if (to.equals("end"))
            executor.executeScript("window.scrollTo(0,Math.max(document.documentElement.scrollHeight,document.body.scrollHeight,document.documentElement.clientHeight));");
        else if (to.equals("top"))
            executor.executeScript("window.scrollTo(Math.max(document.documentElement.scrollHeight,document.body.scrollHeight,document.documentElement.clientHeight),0);");
    }

    public void maximizeBrowser() {
        getDriver().manage().window().maximize();
    }

    public void wait(String time) throws NumberFormatException, InterruptedException {
        Thread.sleep(Integer.parseInt(time) * 1000);
    }

    public Element getMatchingCellElement(String textToFind, Element element) {
        List<Element> table_rows = element.findElements(By.tagName("tr"));
        int rows_count = table_rows.size();
        int row = 0;
        int column = 0;
        int columns_count = 0;
        List<Element> columns_row = null;
        first:
        for (row = 0; row < rows_count; row++) {
            columns_row = table_rows.get(row).findElements(By.tagName("td"));
            columns_count = columns_row.size();
            for (column = 0; column < columns_count; column++) {
                if (columns_row.get(column).getText().equals(textToFind)) {
                    break first;
                }
            }
        }
        if (columns_row != null) {
            return columns_row.get(column);
        } else {
            return null;
        }
    }

    public Element getMatchingCellElement(String textToFind, String columnName, Element element) {
        List<Element> table_headers = element.findElements(By.xpath("//th | //div/span[@class='ui-grid-header-cell-label ng-binding']"));
        List<Element> table_rows = element.findElements(By.xpath("//tr | //div[@class='ui-grid-canvas']/div"));
        int col;
        List<Element> columns = new ArrayList<>();
        First:
        for (col = 1; col <= table_headers.size(); col++) {
            if (table_headers.get(col).getText().trim().equals(columnName))
                for (int row = 1; row <= table_rows.size(); row++) {
                    columns = getElements(By.xpath("//tr" + "[" + row + "]//td | " +
                            "//div[@class='ui-grid-canvas']/div" + "[" + row + "]//div[@role='gridcell']"));
                    if (columns.get(col).getText().equals(textToFind)) {
                        break First;
                    }
                }
        }


        if (columns.get(col) != null) {
            return columns.get(col);
        } else {
            return null;
        }

    }


    public void waitForPageToLoad() {
        jsPageLoad();
        jQueryPageLoad();
    }

    public void jsPageLoad() {
        logger.debug("checking if DOM is loaded");
        final JavascriptExecutor javascriptExecutor = (JavascriptExecutor) getDriver();
        boolean isDomLoaded = javascriptExecutor.executeScript("return document.readyState").equals("complete");
        if (!isDomLoaded) {
            getWait().until((ExpectedCondition<Boolean>) driver -> (javascriptExecutor.executeScript("return document.readyState").equals("complete")));
        }
    }

    public void jQueryPageLoad() {
        logger.debug("checking if any JQuery operation completed");
        final JavascriptExecutor javascriptExecutor = (JavascriptExecutor) getDriver();
        if ((Boolean) javascriptExecutor.executeScript("return typeof jQuery != 'undefined'")) {
            boolean isJQueryReady = (Boolean) javascriptExecutor.executeScript("return jQuery.active==0");
            if (!isJQueryReady) {
                getWait().until((ExpectedCondition<Boolean>) driver -> (Boolean) javascriptExecutor.executeScript("return window.jQuery.active===0"));
            }
        }
    }

}
