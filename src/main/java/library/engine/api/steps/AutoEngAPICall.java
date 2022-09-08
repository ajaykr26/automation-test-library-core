package library.engine.api.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import library.api.utils.Constants;
import library.common.TestContext;
import library.engine.api.AutoEngAPIBaseSteps;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static library.engine.core.objectmatcher.ObjectFinder.invokeMethod;


public class AutoEngAPICall extends AutoEngAPIBaseSteps {
    public static final String FEATURE_NAME = "featureName";

    @Given("^the user loads request payload from \"([^\"]*)\" file$")
    public void loadPayload(String filename, String fileType) {
        if (filename.equalsIgnoreCase("json")) {
            Map<String, Object> args = getAPICallParamList("ALL");
        }
    }

    @Given("^the user calls \"([^\"]*)\" api service$")
    public void callServicesWithoutTagName(String featureName) {
        featureName = getAPIObject(featureName);
        Map<String, Object> args = getAPICallParamList("ALL");
        args.put(FEATURE_NAME, featureName);
        callAPIWithoutTag(featureName, args);
    }

    @Given("^the user calls \"([^\"]*)\" api service with tag name \"([^\"]*)\"$")
    public void callServicesWithTagName(String featureName, String tagName) {
        featureName = getAPIObject(featureName);
        Map<String, Object> args = new HashMap<>();
        args.put(FEATURE_NAME, featureName);
        args.put("tagName", tagName);
        callAPIWithTagName(featureName, args);
    }

    @Given("^the user calls \"([^\"]*)\" api service with tag name \"([^\"]*)\" and arguments \"(.*)\"$")
    public void callServicesWithTagNameAndArgs(String featureName, String tagName, String argList) {
        featureName = getAPIObject(featureName);
        Map<String, Object> args = getAPICallArgs(argList);
        args.put(FEATURE_NAME, featureName);
        args.put("tagName", tagName);
        callAPIWithTagName(featureName, args);
    }

    @Given("^the user calls \"([^\"]*)\" api service with parameters \"([^\"]*)\"$")
    public void callServicesWithParams(String featureName, String paramList) {
        featureName = getAPIObject(featureName);
        Map<String, Object> args = getAPICallParamList(paramList);
        args.put(FEATURE_NAME, featureName);
        callAPIWithoutTag(featureName, args);
    }

    @Given("^the user calls list of \"([^\"]*)\" api services$")
    public void callListOfServices(String featureList) {
        StringTokenizer stringTokenizer = new StringTokenizer(featureList, "|");
        Map<String, Object> args = TestContext.getInstance().testdata();
        while (stringTokenizer.hasMoreTokens()) {//----------------------
            Map<String, Object> result = runAPIFeatureFile(String.format("classpath:%s/services/%s.feature", Constants.API_OBJECT_FOLDER, stringTokenizer.nextToken()), args);
            TestContext.getInstance().testdataPut(stringTokenizer.nextToken(), result);
        }
    }

    //-------------------
    @Given("^the user sends request to the \"([^\"]*)\" API$")
    public void sendRequestWithoutTag(String featureName) {
        featureName = getAPIObject(featureName);
        Map<String, Object> args = getAPICallParamList("ALL");
        args.put(FEATURE_NAME, featureName);
        callAPIWithoutTag(featureName, args);
    }

    @Given("^the user sends request to the \"([^\"]*)\" API with parameters \"([^\"]*)\"$")
    public void sendRequestWithoutTag(String featureName, String paramList) {
        featureName = getAPIObject(featureName);
        Map<String, Object> args = getAPICallParamList(paramList);
        args.put(FEATURE_NAME, featureName);
        callAPIWithoutTag(featureName, args);
    }

    @Given("^the user sends request to the \"([^\"]*)\" API with tag \"([^\"]*)\"$")
    public void sendRequestWithTag(String featureName, String tagName) {
        featureName = getAPIObject(featureName);
        Map<String, Object> args = getAPICallParamList("ALL");
        args.put(FEATURE_NAME, featureName);
        args.put("tagName", tagName);
        callAPIWithTagName(featureName, args);
    }

    @Given("^the user calls list of API \"([^\"]*)\"$")
    public void callListOfAPI(String featureList) {
        StringTokenizer stringTokenizer = new StringTokenizer(featureList, "|");
        Map<String, Object> args = TestContext.getInstance().testdata();
        while (stringTokenizer.hasMoreTokens()) {
            Map<String, Object> result = runAPIFeatureFile(String.format("classpath:%s/services/%s.feature", Constants.API_OBJECT_FOLDER, stringTokenizer.nextToken()), args);
            TestContext.getInstance().testdataPut(stringTokenizer.nextToken(), result);
        }
    }

    @Given("^the user replaces parameter value in the \"([^\"]*)\" API request XML file and save to the \"([^\"]*)\" new XML file $")
    public void callListOfAPI(String xmlTemplate, String xmlTarget) throws IOException {
        replaceParamsInXMLFile(xmlTemplate, xmlTarget);
    }

    @Given("^the user performs the \"([^\"]*)\" API process defined in the \"([^\"]*)\" API object$")
    public void performAPIProcess(String apiProcess, String apiObject) throws IOException, InstantiationException, IllegalAccessException {
        invokeMethod(apiObject, apiProcess);
    }

}
