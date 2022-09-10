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

    @Given("^the user sets \"([^\"]*)\" to \"([^\"]*)\" within the parent attribute \"([^\"]*)\" in the api response \"([^\"]*)\" and store in data dictionary with dictionary key \"response\"$")
    public void setAttributeInResponseWithinParent(String attributeName, String attributeValue, String parentAttributeName, String fileFormat) {
        Map<String, Object> args = new HashMap<>();
        args.put(PARENT_ATTRIBUTE_NAME, parentAttributeName);
        args.put(ATTRIBUTE_NAME, attributeName);
        args.put(ATTRIBUTE_VALUE, parseValueToObject(attributeValue));
        if (fileFormat.equalsIgnoreCase("json")) {
            args.put(TAG_NAME, "@json");
            args.put(RESPONSE, TestContext.getInstance().testdataGet(RESPONSE));
        } else {
            args.put(TAG_NAME, "@xml");
            args.put(RESPONSE_XML, TestContext.getInstance().testdataGet(RESPONSE_XML));
        }
        String featureNameToCall = String.format("%s%s%s.feature", Feature.set, Feature.AttributeValue, Feature.WithInParentAttribute);
        args.put(FEATURE_PATH_TO_CALL, String.format(CLASSPATH_API_FEATURE_FILES, Constants.STORE_UTILS_PATH, featureNameToCall));
        setAttributeValue(args, attributeName);
    }

    @Given("^the user sets \"([^\"]*)\" to \"([^\"]*)\" in the api response \"([^\"]*)\" and store in data dictionary with dictionary key \"response\"$")
    public void setAttributeInResponseWithinRoot(String attributeName, String attributeValue, String fileFormat) {
        setAttributeInResponseWithinParent(attributeName, attributeValue, ROOT_ATTRIBUTE, fileFormat);
    }

    @Given("^the user sets \"([^\"]*)\" to \"([^\"]*)\" at index \"([^\"]*)\" within parent attribute \"([^\"]*)\" in the api response \"([^\"]*)\" and store in data dictionary with dictionary key \"response\"$")
    public void setAttributeInResponseWithinParentAtIndex(String attributeName, String attributeValue, String index, String parentAttributeName, String fileFormat) {
        Map<String, Object> args = new HashMap<>();
        args.put(PARENT_ATTRIBUTE_NAME, parentAttributeName);
        args.put(ATTRIBUTE_NAME, attributeName);
        args.put(ATTRIBUTE_VALUE, parseValueToObject(attributeValue));
        args.put(INDEX, Integer.parseInt(index));
        if (fileFormat.equalsIgnoreCase("json")) {
            args.put(TAG_NAME, "@json");
            args.put(RESPONSE, TestContext.getInstance().testdataGet(RESPONSE));
        } else {
            args.put(TAG_NAME, "@xml");
            args.put(RESPONSE_XML, TestContext.getInstance().testdataGet(RESPONSE_XML));
        }
        String featureNameToCall = String.format("%s%s%s.feature", Feature.set, Feature.AttributeValue, Feature.WithInParentAttribute);
        args.put(FEATURE_PATH_TO_CALL, String.format(CLASSPATH_API_FEATURE_FILES, Constants.STORE_UTILS_PATH, featureNameToCall));
        setAttributeValue(args, attributeName);
    }
}
