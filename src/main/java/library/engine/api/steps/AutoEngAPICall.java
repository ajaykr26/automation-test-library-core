package library.engine.api.steps;

import io.cucumber.java.en.Given;
import library.api.utils.Constants;
import library.common.TestContext;
import library.engine.api.AutoEngAPIBaseSteps;

import java.io.IOException;
import java.util.Map;
import java.util.StringTokenizer;

import static org.apache.commons.lang3.reflect.MethodUtils.invokeMethod;


public class AutoEngAPICall extends AutoEngAPIBaseSteps {
    public static final String FEATURE_NAME = "featureName";

    @Given("^the user sends request using the \"([^\"]*)\" karate feature$")
    public void sendRequestWithoutTag(String featureName) {
        featureName = getAPIObject(featureName);
        Map<String, Object> args = getAPICallParamList("ALL");
        args.put(FEATURE_NAME, featureName);
        callAPIWithoutTag(featureName, args);
    }

    @Given("^the user sends request using the \"([^\"]*)\" karate feature with parameters \"([^\"]*)\"$")
    public void sendRequestWithoutTag(String featureName, String paramList) {
        featureName = getAPIObject(featureName);
        Map<String, Object> args = getAPICallParamList(paramList);
        args.put(FEATURE_NAME, featureName);
        callAPIWithoutTag(featureName, args);
    }

    @Given("^the user sends request using the \"([^\"]*)\" karate feature with tag \"([^\"]*)\"$")
    public void sendRequestWithTag(String featureName, String tagName) {
        featureName = getAPIObject(featureName);
        Map<String, Object> args = getAPICallParamList("ALL");
        args.put(FEATURE_NAME, featureName);
        args.put("tagName", tagName);
        callAPIWithTag(featureName, args);
    }

    @Given("^the user calls list of API using \"([^\"]*)\" karate features$")
    public void callListOfAPI(String featureList) {
        StringTokenizer stringTokenizer = new StringTokenizer(featureList, "|");
        Map<String, Object> args = TestContext.getInstance().testdata();
        while (stringTokenizer.hasMoreTokens()) {//----------------------
            Map<String, Object> result = runAPIFeatureFile(String.format("classpath:%s/services/%s.feature", Constants.API_OBJECT_FOLDER, stringTokenizer.nextToken()), args);
            TestContext.getInstance().testdataPut(stringTokenizer.nextToken(), result);
        }
    }

    @Given("^the user replaces parameter value in the \"([^\"]*)\" API request XML file and save to the \"([^\"]*)\" new XML file $")
    public void callListOfAPI(String xmlTemplate, String xmlTarget) throws IOException {
        replaceParamsInXMLFile(xmlTemplate, xmlTarget);
    }

}
