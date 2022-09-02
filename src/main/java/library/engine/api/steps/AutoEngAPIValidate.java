package library.engine.api.steps;

import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.Given;
import library.api.utils.JSONFormatter;
import library.common.JSONHelper;
import library.common.TestContext;
import library.engine.api.AutoEngAPIBaseSteps;
import library.engine.core.validator.AssertHelper;
import library.engine.core.validator.ComparisonOperator;
import library.engine.core.validator.ComparisonType;
import org.json.JSONArray;


import static library.api.utils.XmlHelper.getValueFromXmlDocumentByXpath;
import static library.engine.core.AutoEngCoreParser.parseValue;

public class AutoEngAPIValidate extends AutoEngAPIBaseSteps {

    @Given("^the user validate that the api response status code is \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public void sendRequestWithoutTag(String comparisonOperator, String valueToFind, String validationId, String failureFlag) {
        valueToFind = parseValue(valueToFind);
        String actualResult = TestContext.getInstance().testdataGet(RESPONSE_STATUS).toString();
        AssertHelper validator = new AssertHelper(ComparisonType.COMPARE_STRING, ComparisonOperator.valueOfLabel(comparisonOperator), failureFlag);
        validator.performValidation(actualResult, valueToFind, validationId, "validation successful");
    }

    @Given("^the user validates that the value at jsonpath \"([^\"]*)\" in the api response \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public void setAttributeInPayload(String jsonPath, String dictionaryKey) {
        dictionaryKey = parseDictionaryKey(dictionaryKey);
        String valueToStore = JsonPath.read(TestContext.getInstance().testdataGet(RESPONSE), jsonPath);
        TestContext.getInstance().testdataPut(dictionaryKey, valueToStore);
    }

    @Given("^the user validates that the value at xpath \"([^\"]*)\" in the api response \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public void storeValueFromXmlResponse(String xpath, String dictionaryKey) {
        dictionaryKey = parseDictionaryKey(dictionaryKey);
        String valueToStore = getValueFromXmlDocumentByXpath(TestContext.getInstance().testdataGet(RESPONSE_XML).toString(), xpath);
        TestContext.getInstance().testdataPut(dictionaryKey, valueToStore);
    }


}
