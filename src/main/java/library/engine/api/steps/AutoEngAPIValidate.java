package library.engine.api.steps;

import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.Given;
import library.common.TestContext;
import library.engine.api.AutoEngAPIBaseSteps;
import library.engine.core.validator.AssertHelper;
import library.engine.core.validator.ComparisonOperator;
import library.engine.core.validator.ComparisonType;


import static library.api.utils.XmlHelper.getValueFromXmlDocumentByXpath;
import static library.engine.core.AutoEngCoreParser.parseValue;

public class AutoEngAPIValidate extends AutoEngAPIBaseSteps {

    @Given("^the user \"([^\"]*)\" to validate that the api response status code is \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public void validateStatusCode(String comparisonType, String comparisonOperator, String valueToFind, String validationId, String failureFlag) {
        valueToFind = parseValue(valueToFind);
        String actualResult = TestContext.getInstance().testdataGet(RESPONSE_STATUS).toString();
        AssertHelper validator = new AssertHelper(ComparisonType.valueOfLabel(comparisonType), ComparisonOperator.valueOfLabel(comparisonOperator), failureFlag);
        validator.performValidation(actualResult, valueToFind, validationId, "validation successful");
    }

    @Given("^the user \"([^\"]*)\" to validate that the value located at \"([^\"]*)\" path in api response \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public void validateValueAtJsonpath(String comparisonType, String locator, String format, String comparisonOperator, String valueToFind, String validationId, String failureFlag) {
        valueToFind = parseValue(valueToFind);
        String actualResult = "";
        if (format.equalsIgnoreCase("json")) {
            actualResult = JsonPath.read(TestContext.getInstance().testdataGet(RESPONSE), locator);
        } else {
            actualResult = getValueFromXmlDocumentByXpath(TestContext.getInstance().testdataGet(RESPONSE_XML).toString(), locator);
        }
        AssertHelper validator = new AssertHelper(ComparisonType.valueOfLabel(comparisonType), ComparisonOperator.valueOfLabel(comparisonOperator), failureFlag);
        validator.performValidation(actualResult, valueToFind, validationId, "validation successful");
    }

}
