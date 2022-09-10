package library.engine.api.steps;

import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.Given;
import library.api.utils.Constants;
import library.common.TestContext;
import library.engine.api.AutoEngAPIBaseSteps;

import java.util.HashMap;
import java.util.Map;

import static library.api.utils.XmlHelper.getValueFromXmlDocumentByXpath;


public class AutoEngAPIStore extends AutoEngAPIBaseSteps {

    @Given("^the user gets the value at jsonpath \"([^\"]*)\" in the api response and stores in the data dictionary with dictionary key \"([^\"]*)\"$")
    public void setAttributeInPayload(String jsonPath, String dictionaryKey) {
        dictionaryKey = parseDictionaryKey(dictionaryKey);
        String valueToStore = JsonPath.read(TestContext.getInstance().testdataGet(RESPONSE), jsonPath);
        storeValueIntoDataDictionary(valueToStore, dictionaryKey);
    }

    @Given("^the user gets the value at xpath \"([^\"]*)\" in the api response and stores in the data dictionary with dictionary key \"([^\"]*)\"$")
    public void storeValueFromXmlResponse(String xpath, String dictionaryKey) {
        dictionaryKey = parseDictionaryKey(dictionaryKey);
        String valueToStore = getValueFromXmlDocumentByXpath(TestContext.getInstance().testdataGet(RESPONSE_XML).toString(), xpath);
        storeValueIntoDataDictionary(valueToStore, dictionaryKey);
    }

    @Given("^the user gets the value at path \"([^\"]*)\" in the api response \"([^\"]*)\" and stores in data dictionary with dictionary key \"([^\"]*)\"$")
    public void storeValueLocatedAtPath( String locator, String fileFormat,String dictionaryKey) {
        Map<String, Object> args = new HashMap<>();
        if (fileFormat.equalsIgnoreCase("json")) {
            args.put(JSONPATH, locator);
            args.put(TAG_NAME, "@json");
            args.put(RESPONSE, TestContext.getInstance().testdataGet(RESPONSE));
        } else {
            args.put(XPATH, locator);
            args.put(TAG_NAME, "@xml");
            args.put(RESPONSE_XML, TestContext.getInstance().testdataGet(RESPONSE_XML));
        }
        String featureNameToCall = String.format("%s%s%s.feature", Feature.store, Feature.AttributeValue, Feature.LocatedAtPath);
        args.put(FEATURE_PATH_TO_CALL, String.format(CLASSPATH_API_FEATURE_FILES, Constants.STORE_UTILS_PATH, featureNameToCall));
        storeAttributeValue(args, dictionaryKey);
    }

    @Given("^the user gets the value of attribute \"([^\"]*)\" within the root attribute in the api response \"([^\"]*)\" and stores in data dictionary with dictionary key \"([^\"]*)\"$")
    public void getAttributeValueWithInRootAttribute(String attributeName, String responseFileFormat, String dictionaryKey) {
        Map<String, Object> args = new HashMap<>();
        args.put(ATTRIBUTE_NAME, attributeName);
        if (responseFileFormat.equalsIgnoreCase("json")) {
            args.put(TAG_NAME, "@json");
            args.put(RESPONSE, TestContext.getInstance().testdataGet(RESPONSE));
        } else {
            args.put(TAG_NAME, "@xml");
            args.put(RESPONSE_XML, TestContext.getInstance().testdataGet(RESPONSE_XML));
        }
        String featureNameToCall = String.format("%s%s%s.feature", Feature.store, Feature.AttributeValue, Feature.WithInRootAttribute);
        args.put(FEATURE_PATH_TO_CALL, String.format(CLASSPATH_API_FEATURE_FILES, Constants.STORE_UTILS_PATH, featureNameToCall));
        storeAttributeValue( args, dictionaryKey);
    }

    @Given("^the user gets the value of attribute \"([^\"]*)\" within the parent attribute \"([^\"]*)\" in the api response \"([^\"]*)\" and stores in data dictionary with dictionary key \"([^\"]*)\"$")
    public void storeAttributeValueWithInParentAttribute(String attributeName, String responseFileFormat, String parentAttributeName, String dictionaryKey) {
        Map<String, Object> args = new HashMap<>();
        args.put(PARENT_ATTRIBUTE_NAME, parentAttributeName);
        args.put(ATTRIBUTE_NAME, attributeName);
        if (responseFileFormat.equalsIgnoreCase("json")) {
            args.put(TAG_NAME, "@json");
            args.put(RESPONSE, TestContext.getInstance().testdataGet(RESPONSE));
        } else {
            args.put(TAG_NAME, "@xml");
            args.put(RESPONSE_XML, TestContext.getInstance().testdataGet(RESPONSE_XML));
        }
        String featureNameToCall = String.format("%s%s%s.feature", Feature.store, Feature.AttributeValue, Feature.WithInParentAttribute);
        args.put(FEATURE_PATH_TO_CALL, String.format(CLASSPATH_API_FEATURE_FILES, Constants.STORE_UTILS_PATH, featureNameToCall));
        storeAttributeValue( args, dictionaryKey);
    }

}
