package library.engine.api.steps;

import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.Given;
import jdk.internal.org.xml.sax.InputSource;
import library.common.TestContext;
import library.engine.api.AutoEngAPIBaseSteps;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;


public class AutoEngAPIStore extends AutoEngAPIBaseSteps {

    @Given("^the user stores value from the api response at jsonpath \"([^\"]*)\" in the data dictionary with dictionary key \"([^\"]*)\"$")
    public void setAttributeInPayload(String jsonPath, String dictionaryKey) {
        dictionaryKey = parseDictionaryKey(dictionaryKey);
        String valueToStore = JsonPath.read(TestContext.getInstance().testdataGet(RESPONSE_KEY), jsonPath);
        TestContext.getInstance().testdataPut(dictionaryKey, valueToStore);
    }


    @Given("^the user stores value from the api response at xpath \"([^\"]*)\" in the data dictionary with dictionary key \"([^\"]*)\"$")
    public void storeValueFromXmlResponse(String xpath, String dictionaryKey) {
        dictionaryKey = parseDictionaryKey(dictionaryKey);
        String xmlResponse = TestContext.getInstance().testdataGet(RESPONSE_XML_KEY).toString();
        String valueToStore = getValueFromXmlStringByXpath(xmlResponse, xpath);
        TestContext.getInstance().testdataPut(dictionaryKey, valueToStore);
    }


    @Given("^the user store value from the API response within parent attribute \"([^\"]*)\" at index \"([^\"]*)\" at key \"([^\"]*)\"$")
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
