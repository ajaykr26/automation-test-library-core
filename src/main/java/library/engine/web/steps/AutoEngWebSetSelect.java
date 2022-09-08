package library.engine.web.steps;

import io.cucumber.java.en.Then;
import library.engine.web.AutoEngWebBaseSteps;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static library.engine.core.AutoEngCoreParser.*;

public class AutoEngWebSetSelect extends AutoEngWebBaseSteps {

    @Then("^the user selects \"([^\"]*)\" value by \"([^\"]*)\" from the \"([^\"]*)\" dropdown at the \"([^\"]*)\" page$")
    public void selectValueFromDropdown(String value, String by, String objectName, String pageName) {
        getElement(objectName, pageName).selectValueFromDropdown(value, by);
    }

    @Then("^the user selects \"([^\"]*)\" checkbox at the \"([^\"]*)\" page$")
    public void selectCheckbox(String objectName, String pageName) {
        getElement(objectName, pageName).selectCheckboxOrRadioBtn();
    }

    @Then("^the user unselects \"([^\"]*)\" checkbox at the \"([^\"]*)\" page$")
    public void unselectCheckbox(String objectName, String pageName) {
        getElement(objectName, pageName).unselectCheckboxOrRadioBtn();

    }

    @Then("^the user selects \"([^\"]*)\" value by \"([^\"]*)\" from the \"([^\"]*)\" checkbox group at the \"([^\"]*)\" page$")
    public void selectCheckboxFromGroup(String value, String by, String objectName, String pageName) {
        value = parseValue(value);
        selectCheckboxOrRadioBtnFromGroup(value, by, objectName, pageName);
    }

    @Then("^the user unselects \"([^\"]*)\" value by \"([^\"]*)\" from the \"([^\"]*)\" checkbox group at the \"([^\"]*)\" page$")
    public void unselectCheckboxFromGroup(String value, String by, String objectName, String pageName) {
        value = parseValue(value);
        unselectCheckboxOrRadioBtnFromGroup(value, by, objectName, pageName);
    }

    @Then("^the user selects \"([^\"]*)\" radio button at the \"([^\"]*)\" page$")
    public void selectRadioBtn(String objectName, String pageName) {
        getElement(objectName, pageName).selectCheckboxOrRadioBtn();
    }

    @Then("^the user unselects \"([^\"]*)\" radio button at the \"([^\"]*)\" page$")
    public void unselectRadioBtn(String objectName, String pageName) {
        getElement(objectName, pageName).unselectCheckboxOrRadioBtn();
    }

    @Then("^the user selects \"([^\"]*)\" value by \"([^\"]*)\" from the \"([^\"]*)\" radio button group at the \"([^\"]*)\" page$")
    public void selectRadioBtnFromGroup(String value, String by, String objectName, String pageName) {
        value = parseValue(value);
        selectCheckboxOrRadioBtnFromGroup(value, by, objectName, pageName);
    }

    @Then("^the user unselects \"([^\"]*)\" value by \"([^\"]*)\" from the \"([^\"]*)\" radio button group at the \"([^\"]*)\" page$")
    public void unselectRadioBtnFromGroup(String value, String by, String objectName, String pageName) {
        value = parseValue(value);
        unselectCheckboxOrRadioBtnFromGroup(value, by, objectName, pageName);
    }

    @Then("^the user enters \"([^\"]*)\" into the \"([^\"]*)\" input field at the \"([^\"]*)\" page$")
    public void enterValueIntoInputField(String textToEnter, String objectName, String pageName) {
        textToEnter = parseValue(textToEnter);
        getElement(objectName, pageName).sendKeys(textToEnter);

    }

    @Then("^the user enters encrypted \"([^\"]*)\" into the \"([^\"]*)\" input field at the \"([^\"]*)\" page$")
    public void enterEncryptedValueIntoInputField(String credential, String objectName, String pageName) {
        credential = parseSecuredValue(credential);
        getElement(objectName, pageName).sendKeys(credential);

    }

    @Then("^the user enters \"([^\"]*)\" into the window alert textbox$")
    public void enterTextInToAlertTextbook(String textToEnter) {
        textToEnter = parseValue(textToEnter);
        try {
            getWait().until(ExpectedConditions.alertIsPresent());
            getDriver().switchTo().alert().sendKeys(textToEnter);
        } catch (NoAlertPresentException exception) {
            logger.error(exception.getMessage());
        }
    }

    @Then("^the user clears the value at \"([^\"]*)\" input field at the \"([^\"]*)\" page$")
    public void clearTextFromInputField(String objectName, String pageName) {
        getElement(objectName, pageName).clear();
    }

    @Then("^the user sends \"([^\"]*)\" key into the \"([^\"]*)\" element at the \"([^\"]*)\" page$")
    public void sendKeysToElement(String keyToSend, String objectName, String pageName) {
        keyToSend = parseValue(keyToSend);
        getElement(objectName, pageName).sendKeyboardKeys(keyToSend);

    }

    @Then("^the user \"([^\"]*)\" the \"([^\"]*)\" key$")
    public void userPressKeyDown(String action, String key) {
        keyboardActions(action, key);
    }

}
