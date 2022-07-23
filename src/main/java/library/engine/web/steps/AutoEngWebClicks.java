package library.engine.web.steps;

import io.cucumber.java.en.Then;
import library.engine.web.AutoEngWebBaseSteps;

public class AutoEngWebClicks extends AutoEngWebBaseSteps {

    @Then("^the user clicks on the \"([^\"]*)\" element at the \"([^\"]*)\" page$")
    public void userClicksOnElement(String objectName, String pageName) {
        getElement(objectName, pageName).click();
    }

    @Then("^the user double clicks on the \"([^\"]*)\" element at the \"([^\"]*)\" page$")
    public void userDoubleClicksOnElement(String objectName, String pageName) {
        getElement(objectName, pageName).doubleClick();
    }

    @Then("^the user right clicks on the \"([^\"]*)\" element at the \"([^\"]*)\" page$")
    public void userRightClicksOnElement(String objectName, String pageName) {
        getElement(objectName, pageName).rightClick();
    }

    @Then("^the user js clicks on the \"([^\"]*)\" element at the \"([^\"]*)\" page$")
    public void userClickJsOnElement(String objectName, String pageName) {
        getElement(objectName, pageName).clickJS();
    }

    @Then("^the user clicks on the matching cell where row num is \"([^\"]*)\" and column num is \"([^\"]*)\" in the \"([^\"]*)\" table at the \"([^\"]*)\" page$")
    public void clickOnMatchingCellRowColIndex(String rowIndex, String columnIndex, String objectName, String pageName) {
        getElement(objectName, pageName).getHeadCellElement(Integer.parseInt(rowIndex), Integer.parseInt(columnIndex)).click();
    }

    @Then("^the user double clicks on the matching cell where row num is \"([^\"]*)\" and column num is \"([^\"]*)\" in the \"([^\"]*)\" table at the \"([^\"]*)\" page$")
    public void doubleClickOnMatchingCellRowColIndex(String rowIndex, String columnIndex, String objectName, String pageName) {
        getElement(objectName, pageName).getHeadCellElement(Integer.parseInt(rowIndex), Integer.parseInt(columnIndex)).doubleClick();
    }

}
