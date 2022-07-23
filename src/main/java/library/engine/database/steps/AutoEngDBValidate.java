package library.engine.database.steps;

import io.cucumber.java.en.Given;
import library.common.TestContext;
import library.engine.core.validator.AssertHelper;
import library.engine.database.AutoEngDBBaseSteps;

import java.util.List;

import static library.engine.core.AutoEngCoreParser.*;

public class AutoEngDBValidate extends AutoEngDBBaseSteps {

    @Given("^the user validates that the DB contains \"([^\"]*)\" value in the \"([^\"]*)\" column \"([^\"]*)\" \"([^\"]*)\"$")
    public void validateValueInDBColumn(String columnName, String expectedValue, String validationID, String onFailureFlag) {
        validateColInDB("Compare-String", columnName, "Contains", expectedValue, validationID, onFailureFlag);
    }

    @Given("^the user validates \"([^\"]*)\" that the \"([^\"]*)\" DB column is \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public void validateColInDB(String comparisonType, String columnName, String comparisonOperator, String expectedValue, String validationID, String onFailureFlag) {
        columnName = parseDictionaryKey(columnName);
        expectedValue = parseValue(expectedValue);

        Object actualValue = getActualValueFromDBResult(TestContext.getInstance().testdataToClass(QUERY_RESULT, List.class), columnName, 1);

        AssertHelper validator = new AssertHelper(comparisonType, comparisonOperator, onFailureFlag);
        validator.performValidation(actualValue, expectedValue, validationID, String.format("'%s' DB column comparison", columnName));
    }

}
