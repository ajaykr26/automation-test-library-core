package library.engine.api.steps;

import io.cucumber.java.en.Given;
import library.common.TestContext;
import library.engine.api.AutoEngAPIBaseSteps;

import java.util.HashMap;
import java.util.Map;

public class AutoEngAPISet extends AutoEngAPIBaseSteps {

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
