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

    @Given("^the user sets the value \"([^\"]*)\" with the key \"([^\"]*)\" at the jsonpath \"([^\"]*)\" in the json request file \"([^\"]*)\"$")
    public static void putValueInRequestJsonObject(String value, String key, String jsonpath, String filename) {
        value = parseValue(value);
        JSONObject jsonObject = JSONHelper.getJSONObject(Constants.SERVICES_PATH + filename);
        if (jsonObject != null) {
            try (FileWriter file = new FileWriter(Constants.SERVICES_PATH + filename)) {
                file.write(JsonPath.parse(jsonObject.toString()).put(jsonpath, key, value).jsonString());
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Given("^the user sets the value \"([^\"]*)\" at the jsonpath \"([^\"]*)\" in the json request file \"([^\"]*)\"$")
    public static void addValueInRequestJsonArray(String value, String jsonpath, String filename) {
        value = parseValue(value);
        JSONObject jsonObject = JSONHelper.getJSONObject(Constants.SERVICES_PATH + filename);
        if (jsonObject != null) {
            try (FileWriter file = new FileWriter(Constants.SERVICES_PATH + filename)) {
                file.write(JsonPath.parse(jsonObject.toString()).add(jsonpath, value).jsonString());
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Given("^the user replaces parameter value in the \"([^\"]*)\" api request xml file and save to the new xml file \"([^\"]*)\"$")
    public void callListOfAPI(String xmlTemplate, String xmlTarget) throws IOException {
        replaceParamsInXMLFile(xmlTemplate, xmlTarget);
    }

    @Given("^the user replaces parameter value in the \"([^\"]*)\" api request json file and save to the new json file \"([^\"]*)\"$")
    public void callListOfAPIJ(String xmlTemplate, String xmlTarget) throws IOException {
        replaceParamsInJsonFile(xmlTemplate, xmlTarget);
    }

    @Given("^the user replaces parameter value in the api request \"([^\"]*)\" file \"([^\"]*)\" and save to the new file \"([^\"]*)\"$")
    public void replaceParamsInRequestFile(String filetype, String sourceFileName, String targetFileName) throws IOException {
        replaceParamsInRequest(filetype, sourceFileName, targetFileName);
    }


    @Given("^the user set \"([^\"]*)\" to \"([^\"]*)\" within parent attribute \"([^\"]*)\" in the API response at key \"([^\"]*)\"$")
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

    @Given("^the user set \"([^\"]*)\" to \"([^\"]*)\" in the API response at key \"([^\"]*)\"$")
    public void setAttributeInPayload(String attributeName, String attributeValue, String dictionaryKey) {
        setAttributeInPayloadWithParent(attributeName, attributeValue, ROOT_ATTRIBUTE, dictionaryKey);
    }

    @Given("^the user set \"([^\"]*)\" to \"([^\"]*)\" at index \"([^\"]*)\" within parent attribute \"([^\"]*)\" in the API response at key \"([^\"]*)\"$")
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
