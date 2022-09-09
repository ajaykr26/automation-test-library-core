package library.engine.api.steps;

import io.cucumber.java.en.Given;
import library.api.utils.Constants;
import library.common.TestContext;
import library.engine.api.AutoEngAPIBaseSteps;

import java.util.Map;
import java.util.StringTokenizer;

import static library.engine.core.objectmatcher.ObjectFinder.invokeMethod;


public class AutoEngAPICall extends AutoEngAPIBaseSteps {
    public static final String FEATURE_NAME = "featureName";

    @Given("^the user calls api service from \"([^\"]*)\" api object$")
    public void callServicesWithoutTagName(String featureName) {
        featureName = getAPIObject(featureName);
        Map<String, Object> args = getParams("ALL");
        args.put(FEATURE_NAME, featureName);
        callServicesWithoutTag(featureName, args);
    }

    @Given("^the user calls api service from \"([^\"]*)\" api object with params \"([^\"]*)\"$")
    public void callServicesWithParams(String featureName, String params) {
        featureName = getAPIObject(featureName);
        Map<String, Object> args = getParams(params);
        args.put(FEATURE_NAME, featureName);
        callServicesWithoutTag(featureName, args);
    }

    @Given("^the user calls api service from \"([^\"]*)\" api object by tag name \"([^\"]*)\"$")
    public void callServicesWithTagName(String featureName, String tagName) {
        featureName = getAPIObject(featureName);
        Map<String, Object> args = getParams("ALL");
        args.put(FEATURE_NAME, featureName);
        args.put("tagName", tagName);
        callServicesWithTag(featureName, args);
    }

    @Given("^the user calls api service from \"([^\"]*)\" api object by tag name \"([^\"]*)\" with params \"(.*)\"$")
    public void callServicesWithTagNameAndArgs(String featureName, String tagName, String params) {
        featureName = getAPIObject(featureName);
        Map<String, Object> args = getParams(params);
        args.put(FEATURE_NAME, featureName);
        args.put("tagName", tagName);
        callServicesWithTag(featureName, args);
    }

    @Given("^the user calls list of api services from \"([^\"]*)\" api object$")
    public void callListOfServices(String featureList) {
        StringTokenizer stringTokenizer = new StringTokenizer(featureList, "|");
        Map<String, Object> args = TestContext.getInstance().testdata();
        while (stringTokenizer.hasMoreTokens()) {//----------------------
            Map<String, Object> result = runServices(String.format("classpath:%s/services/%s.feature", Constants.API_OBJECT_FOLDER, stringTokenizer.nextToken()), args);
            TestContext.getInstance().testdataPut(stringTokenizer.nextToken(), result);
        }
    }

    @Given("^the user calls the api service from \"([^\"]*)\" page object by method name \"([^\"]*)\"$")
    public void callServicesByMethodName(String pageObject, String methodObject) throws InstantiationException, IllegalAccessException {
        invokeMethod(methodObject, pageObject);
    }

}
