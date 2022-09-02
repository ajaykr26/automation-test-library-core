package library.engine.api.steps;

import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.Given;
import library.common.TestContext;
import library.engine.api.AutoEngAPIBaseSteps;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.HashMap;
import java.util.Map;

import static library.api.utils.XmlHelper.getValueFromXmlDocumentByXpath;


public class AutoEngAPIStore extends AutoEngAPIBaseSteps {

    @Given("^the user stores value from the api response at jsonpath \"([^\"]*)\" in the data dictionary with dictionary key \"([^\"]*)\"$")
    public void setAttributeInPayload(String jsonPath, String dictionaryKey) {
        dictionaryKey = parseDictionaryKey(dictionaryKey);
        String valueToStore = JsonPath.read(TestContext.getInstance().testdataGet(RESPONSE), jsonPath);
        TestContext.getInstance().testdataPut(dictionaryKey, valueToStore);
    }

    @Given("^the user stores value from the api response at xpath \"([^\"]*)\" in the data dictionary with dictionary key \"([^\"]*)\"$")
    public void storeValueFromXmlResponse(String xpath, String dictionaryKey) {
        dictionaryKey = parseDictionaryKey(dictionaryKey);
        String valueToStore = getValueFromXmlDocumentByXpath(TestContext.getInstance().testdataGet(RESPONSE_XML).toString(), xpath);
        TestContext.getInstance().testdataPut(dictionaryKey, valueToStore);
    }

}
