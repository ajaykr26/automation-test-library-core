package library.engine.api.steps;

import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.Given;
import library.api.utils.Constants;
import library.common.JSONHelper;
import library.common.TestContext;
import library.engine.api.AutoEngAPIBaseSteps;
import org.apache.poi.util.XMLHelper;
import org.json.JSONObject;
import org.json.XML;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static library.engine.core.AutoEngCoreParser.parseValue;

public class AutoEngAPISet extends AutoEngAPIBaseSteps {

    @Given("^the user replaces parameter value in the api request xml file \"([^\"]*)\" and save to the new xml file \"([^\"]*)\"$")
    public void replaceParamsInRequestXmlFile(String sourceFileName, String targetFileName) throws IOException {
        replaceParamsInXMLFile(sourceFileName, targetFileName);
    }

    @Given("^the user replaces parameter value in the api request json file \"([^\"]*)\" and save to the new json file \"([^\"]*)\"$")
    public void replaceParamsInRequestJsonFile(String sourceFileName, String targetFileName) throws IOException {
        replaceParamsInJsonFile(sourceFileName, targetFileName);
    }

    @Given("^the user replaces parameter value in the api request \"([^\"]*)\" file \"([^\"]*)\" and save to the new file \"([^\"]*)\"$")
    public void replaceParamsInRequestFile(String filetype, String sourceFileName, String targetFileName) throws IOException {
        if (filetype.equalsIgnoreCase("json")) {
            replaceParamsInJsonFile(sourceFileName, targetFileName);
        } else if (filetype.equalsIgnoreCase("xml")) {
            replaceParamsInXMLFile(sourceFileName, targetFileName);
        } else {
            logger.error("File type {} not supported", filetype);
        }
    }

    @Given("^the user sets \"([^\"]*)\" to \"([^\"]*)\" within the parent attribute \"([^\"]*)\" in the api response and store response in data dictionary with dictionary key \"response\"$")
    public void setAttributeInPayloadWithParent(String attributeName, String attributeValue, String parentAttributeName) {
        Map<String, Object> args = new HashMap<>();
        args.put(PARENT_ATTRIBUTE_NAME, parentAttributeName);
        args.put(ATTRIBUTE_NAME, attributeName);
        args.put(RESPONSE, TestContext.getInstance().testdataGet(RESPONSE));
        args.put(ATTRIBUTE_VALUE, parseValueToObject(attributeValue));

        final String featureToRun = getSetFeature(args.get("parentAttributeName").toString(), StoreType.SINGLE);
        setAttributeValue(featureToRun, args, attributeName);
    }


    @Given("^the user gets \"([^\"]*)\" to \"([^\"]*)\" within the parent attribute \"([^\"]*)\" in the api response and store response in data dictionary with dictionary key \"([^\"]*)\"$")
    public void getAttributeValueWithInParentAttribute(String attributeName, String parentAttributeName, String dictionaryKey) {
        Map<String, Object> args = new HashMap<>();
        args.put(PARENT_ATTRIBUTE_NAME, parentAttributeName);
        args.put(ATTRIBUTE_NAME, attributeName);
        args.put(RESPONSE, TestContext.getInstance().testdataGet(RESPONSE));

        final String featureToRun = getStoreFeature(args.get("parentAttributeName").toString(), StoreType.SINGLE);
        storeAttributeValue(featureToRun, args, dictionaryKey);
    }

    @Given("^the user sets \"([^\"]*)\" to \"([^\"]*)\" in the api response and store response in data dictionary with dictionary key \"response\"$")
    public void setAttributeInPayload(String attributeName, String attributeValue) {
        setAttributeInPayloadWithParent(attributeName, attributeValue, ROOT_ATTRIBUTE);
    }

    @Given("^the user gets \"([^\"]*)\" attribute value from api response within root attribute and store in data dictionary with dictionary key \"([^\"]*)\"$")
    public void getAttributeValueWithInRootAttribute(String attributeName, String dictionaryKey) {
        getAttributeValueWithInParentAttribute(attributeName, ROOT_ATTRIBUTE, dictionaryKey);
    }

    @Given("^the user sets \"([^\"]*)\" to \"([^\"]*)\" at index \"([^\"]*)\" within parent attribute \"([^\"]*)\" in the api response and store response in data dictionary with dictionary key \"response\"$")
    public void setAttributeInPayloadWithParent(String attributeName, String attributeValue, String arrayIndex, String parentAttributeName) {
        Map<String, Object> args = new HashMap<>();
        args.put(PARENT_ATTRIBUTE_NAME, parentAttributeName);
        args.put(ATTRIBUTE_NAME, attributeName);
        args.put(ATTRIBUTE_VALUE, parseValueToObject(attributeValue));
        args.put("arrayIndex", Integer.parseInt(arrayIndex));

        final String featureToRun = getSetFeature(args.get("parentAttributeName").toString(), StoreType.ATINDEX);
        setAttributeValue(featureToRun, args, attributeName);
    }
}
