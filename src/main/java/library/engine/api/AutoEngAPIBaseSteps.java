package library.engine.api;

import com.intuit.karate.KarateException;
import com.intuit.karate.Runner;
import com.intuit.karate.core.KarateParser;
import com.intuit.karate.template.KarateEngineContext;
import com.intuit.karate.template.KarateTemplateEngine;
import library.api.utils.Constants;
import library.api.utils.JSONFormatter;
import library.common.FileHelper;
import library.common.JSONHelper;
import library.common.TestContext;
import library.engine.api.steps.AutoEngAPICall;
import library.engine.core.AutoEngCoreBaseStep;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.testng.reporters.Files;

import javax.enterprise.inject.New;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static library.engine.core.AutoEngCoreConstants.API;
import static library.engine.core.AutoEngCoreParser.parseValue;
import static library.engine.core.AutoEngCoreParser.replaceParameterValues;
import static library.engine.core.objectmatcher.ObjectFinder.getMatchingAPIFeature;
import static library.reporting.ReportFactory.getReporter;

public class AutoEngAPIBaseSteps extends AutoEngCoreBaseStep {

    private static final String STATUS_FAIL = "FAIL";
    protected static final String ROOT_ATTRIBUTE = "root";
    protected static final String VALIDATION_TAG = "VALIDATION.";
    protected static final String COMPARISON_OPERATOR = "comparisonOperator";
    protected static final String RESPONSE_STATUS = "responseStatus";
    protected static final String RESPONSE_STATUS_KEY = "fw.responseStatus";
    protected static final String PARENT_ATTRIBUTE_NAME = "parentAttributeName";
    protected static final String RESPONSE_HEADER = "responseHeaders";
    protected static final String RESPONSE_HEADER_KEY = "fw.responseHeaders";
    protected static final String RESPONSE = "response";
    protected static final String RESPONSE_KEY = "fw.lastAPIResponse";
    protected static final String ATTRIBUTE_NAME = "attributeName";
    protected static final String ATTRIBUTE_VALUE = "attributeValue";
    protected static final String CLASSPATH_API_FEATURE_FILES = "classpath:%s%s";
    protected static final String REQUEST_URI = "requestURI";
    protected static final String REQUEST_HEADERS = "requestHeaders";
    protected static final String REQUEST_BODY = "requestBody";
    protected static final String RESPONSE_XML = "responseXml";
    protected static final String RESPONSE_XML_KEY = "responseXMLKey";
    protected static final String WITH_PARENT = "withParent";
    protected static final String PARENT_FEATURE_PATTERN = "%s%s.feature";
    protected static final String FEATURE_PATTERN = "%s.feature";
    protected static final String TOPIC_KEY = "fw.activeKafkaTopic";


    protected enum StoreType {
        SINGLE, ATINDEX, ATLAST, ATKEY, LASTCOUNT, FILTERBY, FILTERBYTWO, FULLARRAY
    }


    protected void callServicesWithTag(String featureName, Map<String, Object> args) {
        String pathToFeature = getPathToFeature(featureName);
        if (pathToFeature != null) {
            args.put("fullPathToFeature", String.format(CLASSPATH_API_FEATURE_FILES, Constants.API_OBJECT_FOLDER, pathToFeature));
            args.put("pathToFeature", String.format(CLASSPATH_API_FEATURE_FILES, Constants.API_OBJECT_FOLDER, pathToFeature.split(featureName + ".feature")[0]));
        } else {
            logger.warn("unable to get path to feature");
        }
        callServices(String.format(CLASSPATH_API_FEATURE_FILES, Constants.CALL_UTILS_PATH, "callServicesWithTagName.feature"), args);
    }

    protected void callServicesWithoutTag(String featureName, Map<String, Object> args) {
        callServices(String.format(CLASSPATH_API_FEATURE_FILES, Constants.API_OBJECT_FOLDER, getPathToFeature(featureName)), args);
    }

    protected String getSetFeature(String parentAttributeName, StoreType storeType) {
        String baseFeatureName = "";

        switch (storeType) {
            case ATINDEX:
                baseFeatureName = "setAttributeAtIndex";
                break;
            case SINGLE:
            default:
                baseFeatureName = "setAttribute";
                break;
        }
        if (parentAttributeName.equalsIgnoreCase(ROOT_ATTRIBUTE)) {
            return String.format(FEATURE_PATTERN, baseFeatureName);
        } else {
            return String.format(PARENT_FEATURE_PATTERN, baseFeatureName, WITH_PARENT);
        }
    }

    protected String getStoreFeature(String parentAttributeName, StoreType storeType) {
        String baseFeatureName = "";

        switch (storeType) {
            case ATINDEX:
                baseFeatureName = "storeAttributeValueAtIndexWithInParentAttribute";
                break;
            case ATKEY:
                baseFeatureName = "storeAttributeValueAtKeyWithInParentAttribute";
                break;
            case SINGLE:
            default:
                baseFeatureName = "storeAttributeValueWithInRootAttribute";
                break;
        }
        if (parentAttributeName.equalsIgnoreCase(ROOT_ATTRIBUTE)) {
            return String.format(FEATURE_PATTERN, baseFeatureName);
        } else {
            return String.format(PARENT_FEATURE_PATTERN, baseFeatureName, WITH_PARENT);
        }
    }

    private String getPathToFeature(String featureName) {
        String tempPath = FileHelper.findFileInPath(Constants.API_OBJECT_PATH, featureName + ".feature");
        if (tempPath != null) {
            return tempPath.split(Constants.API_OBJECT_FOLDER)[1];
        } else {
            return null;
        }
    }

    private String getPathToXML(String featureName) {
        return FileHelper.findFileInPath(Constants.SERVICES_PATH, featureName + ".xml");
    }

    private String getPathToJson(String featureName) {
        return FileHelper.findFileInPath(Constants.SERVICES_PATH, featureName + ".json");
    }

    private String getPathToSourceFile(String filetype, String sourceFilename) {
        return filetype.equalsIgnoreCase("json") ? FileHelper.findFileInPath(Constants.SERVICES_PATH, sourceFilename + ".json") : FileHelper.findFileInPath(Constants.SERVICES_PATH, sourceFilename + ".xml");
    }

    protected void setAttributeValue(String featureName, Map<String, Object> args, String attributeName) {
        try {
            String file = String.format(CLASSPATH_API_FEATURE_FILES, Constants.SET_UTILS_PATH, featureName);
            Map<String, Object> result = runServices(file, args);//------------------------
            TestContext.getInstance().testdataPut(RESPONSE, result.get(RESPONSE));
            getReporter().addStepLog(String.format("set %s attribute to %s", attributeName, result.get("replacedValue")));
            logger.info("Updated Response JSON: {}", result.get(RESPONSE));
            logger.info(String.format("set %s attribute to %s", attributeName, result.get("replacedValue")));
        } catch (KarateException karateException) {
            getReporter().addStepLog(STATUS_FAIL, karateException.getMessage());
            logger.error(karateException.getMessage());
            throw karateException;
        }
    }

    protected void storeAttributeValue(String featureName, Map<String, Object> args, String dictionaryKey) {
        try {
            String file = String.format(CLASSPATH_API_FEATURE_FILES, Constants.STORE_UTILS_PATH, featureName);
            Map<String, Object> result = runServices(file, args);//------------------------
            TestContext.getInstance().testdataPut(dictionaryKey, result.get("attributeValueToStore"));
            getReporter().addStepLog(String.format("stored value %s: %s", dictionaryKey, result.get("attributeValueToStore")));
            logger.info(String.format("stored value %s: %s", dictionaryKey, result.get("attributeValueToStore")));
        } catch (KarateException karateException) {
            getReporter().addStepLog(STATUS_FAIL, karateException.getMessage());
            logger.error(karateException.getMessage());
            throw karateException;
        }
    }

    public Map runServices(String path, Map<String, Object> args) {
        return Runner.runFeature(path, args, true);
    }

    protected String getAPIObject(String featureName) {
        String featureFileName = getMatchingAPIFeature(featureName).getFlatFileObjectName();
        if (featureFileName != null) {
            return featureFileName;
        } else {
            try {
                throw new FileNotFoundException("the " + featureName + ".feature is not found in the apiobjects folder");
            } catch (FileNotFoundException exception) {
                logger.error(exception.getMessage());
            }
        }
        return null;
    }

    protected Map<String, Object> getParams(String paramList) {
        List<String> listOfParams = Arrays.asList(paramList.split("\\|"));

        if (!listOfParams.isEmpty() && listOfParams.get(0).equalsIgnoreCase("ALL")) {
            return new HashMap<>(TestContext.getInstance().testdata());
        } else {
            return listOfParams.stream()
                    .map(String::trim)
                    .collect(Collectors.toMap(this::parseDictionaryKey, this::parseValueToObject));
        }
    }

    protected Map<String, Object> getArgs(String argString) {
        String[] listOfArgsMap = argString.split(":");
        Map<String, Object> args = new HashMap<>();
        for (String arg : listOfArgsMap) {
            args.put(arg.split("\\|")[0], arg.split("\\|")[1]);
        }
        return args;
    }

    private void callServices(String pathToFeature, Map<String, Object> args) {
        try {
            TestContext.getInstance().setActiveWindowType(API);
            Map<String, Object> result = runServices(pathToFeature, args);
            TestContext.getInstance().testdataPut(RESPONSE_STATUS, result.get(RESPONSE_STATUS));
            TestContext.getInstance().testdataPut(RESPONSE_HEADER, result.get(RESPONSE_HEADER));
            if (result.get(RESPONSE_XML) != null) {
                TestContext.getInstance().testdataPut(RESPONSE_XML, result.get(RESPONSE_XML));
            } else {
                TestContext.getInstance().testdataPut(RESPONSE, result.get(RESPONSE));
            }
            logRequestResponseData(result);
        } catch (KarateException karateException) {
            getReporter().addStepLog(STATUS_FAIL, "Step Failed: " + args.get("featureName") + "execution is failed. see error message: " + karateException.getMessage());
            throw karateException;
        }
    }

    private void logRequestResponseData(Map<String, Object> result) {
        Map<String, Object> reportInfo = getReportInfo(result);
        logAndReportText("Request URI: ", reportInfo.get(REQUEST_URI));
        logAndReportDataTable(reportInfo.get(REQUEST_HEADERS));
        logAndReportJSON("Request body: ", reportInfo.get(REQUEST_BODY));
        logAndReportText("Response code: %s", reportInfo.get(RESPONSE_STATUS));
        if (result.get(RESPONSE_XML) != null) {
            logAndReportXml("Response XML: %s", reportInfo.get(RESPONSE_XML));
        } else {
            logAndReportJSON("Response JSON:  %s", reportInfo.get(RESPONSE));
        }
    }

    private Map<String, Object> getReportInfo(Map<String, Object> result) {
        if (result.get("reportInfo") != null) {
            return (HashMap) result.get("reportInfo");
        } else {
            Map<String, Object> reportInfo = new HashMap<>();
            reportInfo.put(REQUEST_URI, result.get(REQUEST_URI));
            reportInfo.put(REQUEST_HEADERS, result.get(REQUEST_HEADERS));
            reportInfo.put(REQUEST_BODY, result.get(REQUEST_BODY));
            reportInfo.put(RESPONSE, result.get(RESPONSE));
            reportInfo.put(RESPONSE_HEADER, result.get(RESPONSE_HEADER));
            reportInfo.put(RESPONSE_XML, result.get(RESPONSE_XML));
            reportInfo.put(RESPONSE_STATUS, result.get(RESPONSE_STATUS));
            reportInfo.put(RESPONSE_XML_KEY, result.get(RESPONSE_XML_KEY));
            return reportInfo;
        }
    }

    private void logAndReportText(String textToLog, Object attributeToLog) {
        if (attributeToLog != null && !attributeToLog.toString().isEmpty()) {
            logger.debug(() -> String.format(textToLog, attributeToLog));
            getReporter().addStepLog(String.format(textToLog, attributeToLog));
        }
    }

    private void logAndReportDataTable(Object attributeToLog) {
        if (attributeToLog != null) {
            logger.debug(attributeToLog);
            getReporter().addDataTable("Request Headers: ", (Map) attributeToLog);
        }
    }

    private void logAndReportJSON(String logTitle, Object attributeToLog) {
        if (attributeToLog != null && !attributeToLog.toString().isEmpty()) {
            PropertiesConfiguration prop = new PropertiesConfiguration();
            String jsonPayload = JSONFormatter.formatJSON(attributeToLog);
            jsonPayload = filterIgnoredTagFromPayload(jsonPayload, prop);
            logger.debug((String.format(logTitle, jsonPayload)));
            getReporter().addTextLogContent(logTitle, jsonPayload);
        }
    }

    private void logAndReportXml(String logTitle, Object attributeToLog) {
        if (attributeToLog != null && !attributeToLog.toString().isEmpty()) {
            PropertiesConfiguration prop = new PropertiesConfiguration();
            String xmlPayload = JSONFormatter.formatJSON(attributeToLog);
            xmlPayload = filterIgnoredTagFromPayload(xmlPayload, prop);
            logger.debug((String.format(logTitle, xmlPayload)));
            getReporter().addTextLogContent(logTitle, xmlPayload);
        }
    }

    private String filterIgnoredTagFromPayload(String jsonPayload, PropertiesConfiguration props) {
        boolean xmlVal = Boolean.parseBoolean(props.getString("ignoreTagXML"));
        boolean jsonVal = Boolean.parseBoolean(props.getString("ignoreAttributeJSON"));
        if (xmlVal) {
            List<String> list = props.getList("ignoreTagInformation").stream().map(token -> (String) token).collect(Collectors.toList());
            for (String token : list) {
                Pattern pattern = Pattern.compile("<" + token + ">" + "(\\S+)" + "</" + token + ">");
                Matcher matcher = pattern.matcher(jsonPayload);
                if (matcher.find()) {
                    jsonPayload = matcher.replaceAll(Matcher.quoteReplacement(matcher.group(0).replace(matcher.group(1), "*****")));
                }
            }
        } else if (jsonVal) {
            List<String> list = props.getList("ignoreTagInformation").stream().map(token -> (String) token).collect(Collectors.toList());
            for (String token : list) {
                Pattern pattern = Pattern.compile("\"" + token + "\"" + ":" + " " + "(\\S+)");
                Matcher matcher = pattern.matcher(jsonPayload);
                if (matcher.find()) {
                    jsonPayload = matcher.replaceAll(Matcher.quoteReplacement(matcher.group(0).replace(matcher.group(1), "*****")));
                }
            }
        }
        return jsonPayload;

    }

    protected void replaceParamsInXMLFile(String sourceFileName, String targetFileName) throws IOException {
        final String pathToSourceFile = getPathToXML(sourceFileName);
        String requestString = FileHelper.getFileAsString(pathToSourceFile, "\n");

        if (requestString != null) {
            requestString = replaceParameterValues(requestString);
            PropertiesConfiguration prop = new PropertiesConfiguration();
            boolean val = Boolean.parseBoolean(prop.getString("ignoreTagsXML"));
            String fetchedVal = prop.getString("ignoreTagInformation");
            if (val) {
                List<String> list = prop.getList("ignoreTagInformation").stream().map(token -> (String) token).collect(Collectors.toList());
                for (String token : list) {
                    if (fetchedVal != null && !fetchedVal.isEmpty()) {
                        Pattern pattern = Pattern.compile("<" + token + ">" + "(\\S+)" + "</" + token + ">");
                        Matcher matcher = pattern.matcher(requestString);
                        if (!matcher.find()) {
                            TestContext.getInstance().testdataPut(REQUEST_BODY, requestString);
                        }
                    }
                }
            } else {
                TestContext.getInstance().testdataPut(REQUEST_BODY, requestString);
            }
            final String newFileName = Paths.get(String.format("%s/%s.xml", Paths.get(pathToSourceFile).getParent().toString(), targetFileName)).toAbsolutePath().toString();
            Files.writeFile(requestString, new File(newFileName));
        }
    }

    protected void replaceParamsInJsonFile(String sourceFileName, String targetFileName) throws IOException {
        final String pathToSourceFile = getPathToJson(sourceFileName);
        String requestString = FileHelper.getFileAsString(pathToSourceFile, "\n");

        if (requestString != null) {
            requestString = replaceParameterValues(requestString);
            PropertiesConfiguration prop = new PropertiesConfiguration();
            boolean val = Boolean.parseBoolean(prop.getString("ignoreTagsXML"));
            String fetchedVal = prop.getString("ignoreTagInformation");
            if (val) {
                List<String> list = prop.getList("ignoreTagInformation").stream().map(token -> (String) token).collect(Collectors.toList());
                for (String token : list) {
                    if (fetchedVal != null && !fetchedVal.isEmpty()) {
                        Pattern pattern = Pattern.compile("<" + token + ">" + "(\\S+)" + "</" + token + ">");
                        Matcher matcher = pattern.matcher(requestString);
                        if (!matcher.find()) {
                            TestContext.getInstance().testdataPut(REQUEST_BODY, requestString);
                        }
                    }
                }
            } else {
                TestContext.getInstance().testdataPut(REQUEST_BODY, requestString);
            }
            final String newFileName = Paths.get(String.format("%s/%s.json", Paths.get(pathToSourceFile).getParent().toString(), targetFileName)).toAbsolutePath().toString();
            Files.writeFile(requestString, new File(newFileName));
        }
    }

    protected void replaceParamsInRequest(String filetype, String sourceFileName, String targetFileName) throws IOException {
        final String pathToXmlTemplate = getPathToSourceFile(filetype, sourceFileName);
        String requestString = FileHelper.getFileAsString(pathToXmlTemplate, "\n");

        if (requestString != null) {
            requestString = replaceParameterValues(requestString);
            PropertiesConfiguration prop = new PropertiesConfiguration();
            boolean val = filetype.equalsIgnoreCase("json") ? Boolean.parseBoolean(prop.getString("ignoreTagsJSON")) : Boolean.parseBoolean(prop.getString("ignoreTagsXML"));
            String fetchedVal = prop.getString("ignoreTagInformation");
            if (val) {
                List<String> list = prop.getList("ignoreTagInformation").stream().map(token -> (String) token).collect(Collectors.toList());
                for (String token : list) {
                    if (fetchedVal != null && !fetchedVal.isEmpty()) {
                        Pattern pattern = Pattern.compile("<" + token + ">" + "(\\S+)" + "</" + token + ">");
                        Matcher matcher = pattern.matcher(requestString);
                        if (!matcher.find()) {
                            TestContext.getInstance().testdataPut(REQUEST_BODY, requestString);
                        }
                    }
                }
            } else {
                TestContext.getInstance().testdataPut(REQUEST_BODY, requestString);
            }
            String NewRequestFile = filetype.equalsIgnoreCase("json") ? "%s/%s.json" : "%s/%s.xml";
            final String newFileName = Paths.get(String.format(NewRequestFile, Paths.get(pathToXmlTemplate).getParent().toString(), targetFileName)).toAbsolutePath().toString();
            Files.writeFile(requestString, new File(newFileName));
        }
    }


}
