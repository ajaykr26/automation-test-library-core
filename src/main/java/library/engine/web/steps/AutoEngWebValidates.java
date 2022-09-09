package library.engine.web.steps;

import io.cucumber.java.en.Then;
import library.common.Constants;
import library.engine.core.validator.AssertHelper;
import library.engine.core.validator.ComparisonOperator;
import library.engine.core.validator.ComparisonType;
import library.engine.web.AutoEngWebBaseSteps;
import library.selenium.core.Element;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static library.common.StringHelper.getRandomString;
import static library.engine.core.AutoEngCoreParser.getListFromDictionary;
import static library.engine.core.AutoEngCoreParser.parseValue;
import static library.selenium.core.Screenshot.compareScreenshot;
import static library.selenium.core.Screenshot.getImageFromUrl;

public class AutoEngWebValidates extends AutoEngWebBaseSteps {

    @Then("^the user \"([^\"]*)\" to validate that the text at the \"([^\"]*)\" element at the \"([^\"]*)\" page is \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\", \"([^\"]*)\"$")
    public void validateText(String comparisonType, String methodObject, String pageObject, String comparisonOperator, String valueToFind, String validationId, String failureFlag) {
        valueToFind = parseValue(valueToFind);
        String actualResult = getElement(methodObject, pageObject).getText().trim();
        AssertHelper validator = new AssertHelper(ComparisonType.valueOfLabel(comparisonType), ComparisonOperator.valueOfLabel(comparisonOperator), failureFlag);
        validator.performValidation(actualResult, valueToFind, validationId, "validation successful");

    }

    @Then("^the user \"([^\"]*)\" to validate that the value at the \"([^\"]*)\" element at the \"([^\"]*)\" page is \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\", \"([^\"]*)\"$")
    public void validateValue(String comparisonType, String methodObject, String pageObject, String comparisonOperator, String valueToFind, String validationId, String failureFlag) {
        valueToFind = parseValue(valueToFind);
        String actualResult = getElement(methodObject, pageObject).getValue().trim();
        AssertHelper validator = new AssertHelper(ComparisonType.valueOfLabel(comparisonType), ComparisonOperator.valueOfLabel(comparisonOperator), failureFlag);
        validator.performValidation(actualResult, valueToFind, validationId, "validation successful");

    }

    @Then("^the user validates that the \"([^\"]*)\" element is \"([^\"]*)\" at the \"([^\"]*)\" page \"([^\"]*)\" \"([^\"]*)\"$")
    public void validateElementStatus(String methodObject, String elementStatus, String pageObject, String validationId, String failureFlag) {
        boolean actualResult = getElement(methodObject, pageObject).validateElement(elementStatus);

        AssertHelper validator = new AssertHelper(ComparisonType.COMPARE_STRING, ComparisonOperator.EQ, failureFlag);
        validator.performValidation(actualResult, true, validationId, "validation successful");

    }

    @Then("^the user validates that the image at the \"([^\"]*)\" url is same as the image at the \"([^\"]*)\" location \"([^\"]*)\" \"([^\"]*)\"$")
    public void validateImage(String imageUrl, String filename, String validationId, String failureFlag) throws IOException {
        filename = parseValue(filename);
        imageUrl = parseValue(imageUrl);
        getImageFromUrl(imageUrl, Constants.ACTUAL_IMAGE_PATH, filename);
        boolean actualResult = compareScreenshot(new File(Constants.TESTDATA_PATH + filename), new File(Constants.ACTUAL_IMAGE_PATH + filename));
        AssertHelper validator = new AssertHelper(ComparisonType.COMPARE_STRING, ComparisonOperator.EQ, failureFlag);
        validator.performValidation(actualResult, true, validationId, "validation successful");
    }

    @Then("^the user \"([^\"]*)\" to validate that the value in the row num \"([^\"]*)\" and the column num \"([^\"]*)\" in the \"([^\"]*)\" table at the \"([^\"]*)\" page is \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public void validateMatchingCellRowColIndex(String comparisonType, String rowIndex, String columnIndex, String methodObject, String pageObject, String comparisonOperator, String valueToFind, String validationId, String failureFlag) {
        String actualResult = getElement(methodObject, pageObject).getHeadCellElement(Integer.parseInt(rowIndex), Integer.parseInt(columnIndex)).getText();
        AssertHelper validator = new AssertHelper(ComparisonType.valueOfLabel(comparisonType), ComparisonOperator.valueOfLabel(comparisonOperator), failureFlag);
        validator.performValidation(actualResult, valueToFind, validationId, "validation successful");
    }

    @Then("^the user \"([^\"]*)\" to validate that the \"([^\"]*)\" value is found in the \"([^\"]*)\" column in the \"([^\"]*)\" table at the \"([^\"]*)\" page \"([^\"]*)\" \"([^\"]*)\"$")
    public void validateValueInMatchingColumn(String comparisonType, String valueToFind, String columnName, String methodObject, String pageObject, String validationId, String failureFlag) {
        Element element = getElement(methodObject, pageObject);
        String actualResult = element.getDataRowElementInCol(valueToFind, element.getHeadCellNum(columnName)).getText().trim();
        AssertHelper validator = new AssertHelper(ComparisonType.valueOfLabel(comparisonType), ComparisonOperator.CONTAINS, failureFlag);
        validator.performValidation(actualResult, parseValue(valueToFind), validationId, "validation successful");
    }

    @Then("^the user validates that the list of \"([^\"]*)\" dropdown options at the \"([^\"]*)\" page is \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public void validateDropdownValues(String methodObject, String pageObject, String comparisonOperator, String valueToMatch, String validationId, String failureFlag) {
        List<String> actualResult = getElement(methodObject, pageObject).getListOfDropdownOptionText();
        AssertHelper validator = new AssertHelper(ComparisonType.COMPARE_LIST, ComparisonOperator.valueOfLabel(comparisonOperator), failureFlag);
        validator.performValidation(actualResult, getListFromDictionary(valueToMatch), validationId, "validation successful");
    }

    @Then("^the user validates that the \"([^\"]*)\" file is downloaded \"([^\"]*)\" \"([^\"]*)\"$")
    public void validateValueInMatchingColumn(String fileName, String validationId, String failureFlag) {
        boolean actualResult = isFileDownloaded(Constants.DOWNLOAD_PATH, parseValue(fileName));
        AssertHelper validator = new AssertHelper(ComparisonType.COMPARE_STRING, ComparisonOperator.EQ, failureFlag);
        validator.performValidation(actualResult, true, validationId, "validation successful");
    }

    @Then("^the user validates that the \"([^\"]*)\" input field at the \"([^\"]*)\" page has input char limit of \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public void validateAllowedInputCharLimit(String methodObject, String pageObject, String inputCharLimit, String validationId, String failureFlag) {
        int allowedCharLength = Integer.parseInt(inputCharLimit);
        Element element = getElement(methodObject, pageObject);

        element.sendKeys(getRandomString(allowedCharLength - 1));
        AssertHelper validator = new AssertHelper(ComparisonType.COMPARE_NUMBER, ComparisonOperator.EQ, failureFlag);
        validator.performValidation(element.getValue().length(), allowedCharLength - 1, validationId, "validation successful");

        element.sendKeys(getRandomString(allowedCharLength));
        validator = new AssertHelper(ComparisonType.COMPARE_NUMBER, ComparisonOperator.EQ, failureFlag);
        validator.performValidation(element.getValue().length(), allowedCharLength, validationId, "validation successful");

        element.sendKeys(getRandomString(allowedCharLength + 1));
        validator = new AssertHelper(ComparisonType.COMPARE_NUMBER, ComparisonOperator.EQ, failureFlag);
        validator.performValidation(element.getValue().length(), allowedCharLength, validationId, "validation successful");

    }


}
