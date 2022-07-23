package library.engine.core;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.cucumber.java8.En;
import library.common.Constants;
import library.common.Encryptor;
import library.common.Property;
import library.common.TestContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static library.reporting.ReportFactory.getReporter;

public class AutoEngCoreParser implements En {
    protected static final String ERROR = "ERROR";
    protected static final String REGEX_KEY = "(?:@|#)\\((\\S*?)\\)";

    protected static final String ENV_PROP = "cukes.env";
    protected static final String PROPERTIES_EXT = ".properties";

    protected static final Logger logger = LogManager.getLogger(AutoEngCoreParser.class.getName());

    public static String parseValue(String string) {
        String parsedKeyJSON = parseKeyJSON(string);
        String parsedKeyProps = parseKeyProps(string);
        if (parsedKeyJSON != null) {
            try {
                return TestContext.getInstance().testdataGet(parsedKeyJSON).toString();
            } catch (NullPointerException exception) {
                logger.error(String.format("can not find the key %s in the data dictionary. please check json file", parsedKeyJSON));
                throw exception;
            }
        } else if (parsedKeyProps != null) {
            try {
                return TestContext.getInstance().propDataGet(parsedKeyProps).toString();
            } catch (NullPointerException exception) {
                logger.error(String.format("can not find the key %s in the data dictionary. please check properties file", parsedKeyProps));
                throw exception;
            }
        }
        return string;

    }

    public static String parseSecuredValue(String encryptedString) {
        encryptedString = Property.getProperty(Constants.SECURE_TEXT_PROP_FILE, parseKeyProps(encryptedString));
        String decryptedString;
        if (encryptedString != null) {
            decryptedString = Encryptor.decrypt(encryptedString);
            return decryptedString;
        } else {
            return null;
        }
    }

    public static String parseKeyJSON(String string) {
        String dataParsed = null;

        Pattern patternJSON = Pattern.compile("#\\((.*)\\)");
        Matcher matcherJSON = patternJSON.matcher(string);

        if (matcherJSON.matches()) {
            return matcherJSON.group(1);
        } else {
            return null;
        }
    }

    public static String parseKeyProps(String string) {
        Pattern patternProp = Pattern.compile("@\\((.*)\\)");
        Matcher matcherProp = patternProp.matcher(string);

        if (matcherProp.matches()) {
            return matcherProp.group(1);
        } else {
            return null;
        }

    }

    public static Object getValueToObject(String value) {
        Object valToParse;
        Pattern pattern = Pattern.compile(REGEX_KEY);
        Matcher matcher = pattern.matcher(value);

        if (matcher.matches()) {
            valToParse = matcher.group(0);
        } else {
            valToParse = value;
        }
        return valToParse;
    }

    public static String replaceParameterValues(String stringToReplace) {
        StringBuffer stringBuffer = new StringBuffer();
        Pattern pattern = Pattern.compile(REGEX_KEY);
        Matcher matcher = pattern.matcher(stringToReplace);
        while (matcher.find()) {
            String fullKey = matcher.group(0);
            matcher.appendReplacement(stringBuffer, Matcher.quoteReplacement((getDictionaryValOrRealVal(fullKey))));
        }
        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }

    public static String getDictionaryValOrRealVal(String value) {
        String parsedKeyJSON = parseKeyJSON(value);
        String parsedKeyProps = parseKeyProps(value);
        try {
            if (parsedKeyJSON != null) {
                if (TestContext.getInstance().testdataGet(parsedKeyJSON).toString() != null)
                    return TestContext.getInstance().testdataGet(parsedKeyJSON).toString();
            } else if (parsedKeyProps != null) {
                if (Property.getProperty(Constants.ENVIRONMENT_PROP_FILE, parsedKeyProps) != null)
                    return Property.getProperty(Constants.ENVIRONMENT_PROP_FILE, parsedKeyProps);
            }
        } catch (NullPointerException ignored) {

        }

        return value;
    }

    public static String parseDictionaryKey(String value) {
        String valToParse;
        Pattern pattern = Pattern.compile(REGEX_KEY);
        Matcher matcher = pattern.matcher(value);

        if (matcher.matches()) {
            valToParse = matcher.group(0);
        } else {
            valToParse = value;
        }
        return valToParse;
    }

    public static Object parseValueToObject(String value) {
        return AutoEngCoreParser.getValueToObject(value);
    }

    public static List<String> getListFromDictionary(String dictionaryKey) {
        try {
            final Object jsonArray = TestContext.getInstance().testdataGet(parseDictionaryKey(dictionaryKey));
            if (jsonArray != null) {
                if (jsonArray instanceof List) {
                    return (List<String>) jsonArray;
                } else {
                    return new Gson().fromJson(jsonArray.toString(), List.class);
                }
            }
        } catch (JsonSyntaxException exception) {
            getReporter().addStepLog(String.format("did not find a json array. found: %s. error: %s", TestContext.getInstance().testdataGet(dictionaryKey)), exception.getMessage());
        }
        return Collections.emptyList();
    }

    public static String getValueOrVariable(String value) {
        String valToParse;
        Pattern pattern = Pattern.compile(REGEX_KEY);
        Matcher matcher = pattern.matcher(value);

        if (matcher.matches()) {
            valToParse = matcher.group(0);
        } else {
            valToParse = value;
        }
        return valToParse;
    }

}

