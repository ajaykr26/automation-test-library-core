package library.engine.web.steps;

import io.cucumber.java.en.Then;
import library.engine.web.AutoEngWebBaseSteps;

public class AutoEngWebClicks extends AutoEngWebBaseSteps {

    @Then("^the user clicks on the \"([^\"]*)\" element at the \"([^\"]*)\" page$")
    public void userClicksOnElement(String methodObject, String pageObject) {
        getElement(methodObject, pageObject).click();
    }

    @Then("^the user double clicks on the \"([^\"]*)\" element at the \"([^\"]*)\" page$")
    public void userDoubleClicksOnElement(String methodObject, String pageObject) {
        getElement(methodObject, pageObject).doubleClick();
    }

    @Then("^the user right clicks on the \"([^\"]*)\" element at the \"([^\"]*)\" page$")
    public void userRightClicksOnElement(String methodObject, String pageObject) {
        getElement(methodObject, pageObject).rightClick();
    }

    @Then("^the user js clicks on the \"([^\"]*)\" element at the \"([^\"]*)\" page$")
    public void userClickJsOnElement(String methodObject, String pageObject) {
        getElement(methodObject, pageObject).clickJS();
    }

    @Then("^the user clicks on the matching cell where row num is \"([^\"]*)\" and column num is \"([^\"]*)\" in the \"([^\"]*)\" table at the \"([^\"]*)\" page$")
    public void clickOnMatchingCellRowColIndex(String rowIndex, String columnIndex, String methodObject, String pageObject) {
        getElement(methodObject, pageObject).getHeadCellElement(Integer.parseInt(rowIndex), Integer.parseInt(columnIndex)).click();
    }

    @Then("^the user double clicks on the matching cell where row num is \"([^\"]*)\" and column num is \"([^\"]*)\" in the \"([^\"]*)\" table at the \"([^\"]*)\" page$")
    public void doubleClickOnMatchingCellRowColIndex(String rowIndex, String columnIndex, String methodObject, String pageObject) {
        getElement(methodObject, pageObject).getHeadCellElement(Integer.parseInt(rowIndex), Integer.parseInt(columnIndex)).doubleClick();
    }

}
