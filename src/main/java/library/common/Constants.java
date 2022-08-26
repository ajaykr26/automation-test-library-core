package library.common;

import java.io.File;

public class Constants {

    public static final String USER_DIR = System.getProperty("user.dir");
    public static final String BASE_PATH = USER_DIR + "/src/test/resources/";
    public static final String TESTDATA_PATH = BASE_PATH + "testdata/";
    public static final String PROPERTIES_PATH = BASE_PATH + "config/properties/";
    public static final String WEB_KEYS_PATH = BASE_PATH + "config/keymaps/webkeys.json";
    public static final String TECHSTACK_PATH = BASE_PATH + "config/techstacks/";
    public static final String DOWNLOAD_PATH = USER_DIR + "/downloads/";
    public static final String EDGE_PATH = "C:/Program Files (x86)/Microsoft/Edge/Application/msedge.exe";

    public static final String DRIVER_PATH = USER_DIR + "/drivers/";
    public static final String SCREENSHOT_PATH = USER_DIR + "/target/allure-results/screenshots/";
    public static final String ALLURE_RESULT_PATH = USER_DIR + "/target/allure-results";
    public static final String EXTENT_REPORT_PATH = USER_DIR + "/target/extent-reports/";
    public static final String ACTUAL_IMAGE_PATH = USER_DIR + "/target/actual-image/";
    public static final String FEATURE_PATH = BASE_PATH + "features/";
    ;
    public static final String CLASSPATH = USER_DIR + "/target/test-classes/";
    public static final String SELENIUM_PATH = BASE_PATH + "config/selenium/";
    public static final String API_OBJECT_FOLDER = "apiobjects";
    public static final String STORE_UTILS_PATH = "/api/services/store/";
    public static final String VALIDATE_UTILS_PATH = "/api/services/validate/";
    public static final String SET_UTILS_PATH = "/api/services/set/";
    public static final String REMOVE_UTILS_PATH = "/api/services/remove/";
    public static final String CALL_UTILS_PATH = "/api/services/call/";
    public static final String PAGE_OBJECT_JAR_PATH = USER_DIR + "/target/test-classes/pageobjects/";
    public static final String PAGE_OBJECT_JAVA_PATH = USER_DIR + "/src/test/java/pageobjects/";
    public static final String PAGE_OBJECT_EXTERNAL_JAR_PATH = USER_DIR + "/target/lib/";

    public static final String TECHSTACKS = TECHSTACK_PATH + Property.getVariable("cukes.techstack") + ".json";
    public static final String ENVIRONMENT_PROP_FILE = BASE_PATH + "config/environments/" + Property.getVariable("cukes.environment") + ".properties";
    public static final String SECURE_TEXT_PROP_FILE = BASE_PATH + "config/environments/SecureText-" + Property.getVariable("cukes.environment") + ".properties";
    public static final String RUNTIME_PROP_FILE = PROPERTIES_PATH + "runtime.properties";
    public static final String TESTCASE_EXCEL = BASE_PATH + "scripts/TestCase.xlsx";
    public static final String TESTDATA_EXCEL = BASE_PATH + "testdata/StaticTestData.xlsx";
    public static final String API_OBJECT = CLASSPATH + API_OBJECT_FOLDER + File.separator;

}
