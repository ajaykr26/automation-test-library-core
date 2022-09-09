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

    @Given("^the user sets \"([^\"]*)\" to \"([^\"]*)\" within the parent attribute \"([^\"]*)\" in the api response at key \"([^\"]*)\"$")
    public void setAttributeInPayloadWithParent(String attributeName, String attributeValue, String parentAttributeName, String dictionaryKey) {
        dictionaryKey = parseDictionaryKey(dictionaryKey);
        Map<String, Object> args = new HashMap<>();
        args.put(PARENT_ATTRIBUTE_NAME, parentAttributeName);
        args.put(ATTRIBUTE_NAME, attributeName);
        args.put(RESPONSE, TestContext.getInstance().testdataGet(dictionaryKey));
        args.put(ATTRIBUTE_VALUE, parseValueToObject(attributeValue));

        final String featureToRun = getSetFeature(args.get("parentAttributeName").toString(), StoreType.SINGLE);
        setAPIAttribute(featureToRun, args, attributeName, dictionaryKey);
    }

    @Given("^the user sets \"([^\"]*)\" to \"([^\"]*)\" in the api response at key \"([^\"]*)\"$")
    public void setAttributeInPayload(String attributeName, String attributeValue, String dictionaryKey) {
        setAttributeInPayloadWithParent(attributeName, attributeValue, ROOT_ATTRIBUTE, dictionaryKey);
    }

    @Given("^the user sets \"([^\"]*)\" to \"([^\"]*)\" at index \"([^\"]*)\" within parent attribute \"([^\"]*)\" in the api response at key \"([^\"]*)\"$")
    public void setAttributeInPayloadWithParent(String attributeName, String attributeValue, String arrayIndex, String parentAttributeName, String dictionaryKey) {
        dictionaryKey = parseDictionaryKey(dictionaryKey);
        Map<String, Object> args = new HashMap<>();
        args.put(PARENT_ATTRIBUTE_NAME, parentAttributeName);
        args.put(ATTRIBUTE_NAME, attributeName);
        args.put(RESPONSE, TestContext.getInstance().testdataGet(dictionaryKey));
        args.put(ATTRIBUTE_VALUE, parseValueToObject(attributeValue));
        args.put("arrayIndex", Integer.parseInt(arrayIndex));

        final String featureToRun = getSetFeature(args.get("parentAttributeName").toString(), StoreType.ATINDEX);
        setAPIAttribute(featureToRun, args, attributeName, dictionaryKey);
    }
}
