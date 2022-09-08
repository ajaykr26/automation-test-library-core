package library.engine.web.steps;

import io.cucumber.java.en.Then;
import library.common.TestContext;
import library.engine.web.AutoEngWebBaseSteps;
import library.selenium.core.Element;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

import static library.engine.core.AutoEngCoreParser.*;

public class AutoEngWebStoreText extends AutoEngWebBaseSteps {

    @Then("^the user stores text from the \"([^\"]*)\" element at the \"([^\"]*)\" page in data dictionary key \"([^\"]*)\"$")
    public void storeTextFromElement(String objectName, String pageName, String dictionaryKey) {
        dictionaryKey = parseValue(dictionaryKey);
        String valueToStore = getElement(objectName, pageName).getText();
        TestContext.getInstance().testdataPut(dictionaryKey, valueToStore);
    }

    @Then("^the user stores value from the \"([^\"]*)\" element at the \"([^\"]*)\" page in data dictionary key \"([^\"]*)\"$")
    public void storeValueFromElement(String objectName, String pageName, String dictionaryKey) {
        dictionaryKey = parseValue(dictionaryKey);
        String valueToStore = getElement(objectName, pageName).getValue();
        TestContext.getInstance().testdataPut(dictionaryKey, valueToStore);
    }

    @Then("^the user stores text from the window alert into the data dictionary key \"([^\"]*)\"$")
    public void storeValueFromAlert(String dictionaryKey) {
        try {
            getWait().until(ExpectedConditions.alertIsPresent());
            String textToStore = getDriver().switchTo().alert().getText();
            TestContext.getInstance().testdataPut(dictionaryKey, textToStore);
        } catch (NoAlertPresentException exception) {
            logger.error(exception.getMessage());
        }
    }

    @Then("^the user stores text from matching cell where row num \"([^\"]*)\" and col num \"([^\"]*)\" in the \"([^\"]*)\" table at the \"([^\"]*)\" page into the data dictionary key \"([^\"]*)\"$")
    public void storeValueFromMatchingCellOfRowColIndex(String rowIndex, String columnIndex, String objectName, String pageName, String dictionaryKey) {
        String actualResult = getElement(objectName, pageName).getDataCellElement(Integer.parseInt(rowIndex), Integer.parseInt(columnIndex)).getText().trim();
        TestContext.getInstance().testdataPut(parseDictionaryKey(dictionaryKey), actualResult);
    }

    @Then("^the user stores text from matching cell where row num \"([^\"]*)\" and col num \"([^\"]*)\" in the \"([^\"]*)\" table at the \"([^\"]*)\" page in the data dictionary to the \"([^\"]*)\" list$")
    public void storeValueFromMatchingCellOfRowColIndexList(String rowIndex, String columnIndex, String objectName, String pageName, String dictionaryKey) {
        String actualResult = getElement(objectName, pageName).getDataCellElement(Integer.parseInt(rowIndex), Integer.parseInt(columnIndex)).getText().trim();
        if (getListFromDictionary(dictionaryKey).size() >0) {
            List<String> list = getListFromDictionary(dictionaryKey);
            list.add(actualResult);
            TestContext.getInstance().testdataPut(parseDictionaryKey(dictionaryKey), list);
        } else {
            List<String> list = new ArrayList<>();
            list.add(actualResult);
            TestContext.getInstance().testdataPut(parseDictionaryKey(dictionaryKey), list);
        }
    }

    @Then("^the user add the \"([^\"]*)\" list from data dictionary to the \"([^\"]*)\" list$")
    public void addTheListToListOfList(String listToAdd, String listToBeAdded) {

        if (getListFromDictionary(listToBeAdded) == null) {
            List<String> list2 = getListFromDictionary(listToAdd);
            List<String> list1 = new ArrayList<>(list2);
            TestContext.getInstance().testdataPut(parseDictionaryKey(listToBeAdded), list1);
        } else {
            List<String> list2 = getListFromDictionary(listToBeAdded);
            List<String> list1 = new ArrayList<>(list2);
            TestContext.getInstance().testdataPut(parseDictionaryKey(listToBeAdded), list1);
        }
//

    }

    @Then("^the user stores text from matching cell in \"([^\"]*)\" col where \"([^\"]*)\" col contains \"([^\"]*)\" in the \"([^\"]*)\" table at the \"([^\"]*)\" page into the data dictionary key \"([^\"]*)\"$")
    public void validateValueInMatchingColumn(String columnName, String columnName1, String valueToFind1, String objectName, String pageName, String dictionaryKey) {
        Element element = getElement(objectName, pageName);
        int rowNum = element.getDataRowNumInCol(valueToFind1, element.getHeadCellNum(columnName1));
        String actualResult = element.getDataCellElement(rowNum, element.getHeadCellNum(columnName)).getText().trim();
        TestContext.getInstance().testdataPut(parseDictionaryKey(dictionaryKey), actualResult);
    }

}
