package library.api;

import java.io.File;

public class Constants {

    private Constants() {
    }

    public static final String USER_DIR = System.getProperty("user.dir");
    public static final String GENERATED_CLASSPATH = USER_DIR + "/target/test-classes/";
    public static final String API_OBJECT_FOLDER = "apiobjects";
    public static final String API_OBJECT_PATH = GENERATED_CLASSPATH + API_OBJECT_FOLDER + File.separator;
    public static final String ENVIRONMENT_PATH = USER_DIR + "/src/test/resources/config/environment";
    public static final String STORE_UTILS_PATH = "karate/services/store/";
    public static final String VALIDATE_UTILS_PATH = "karate/services/validate/";
    public static final String SET_UTILS_PATH = "karate/services/set/";
    public static final String REMOVE_UTILS_PATH = "karate/services/remove/";
    public static final String CALL_UTILS_PATH = "karate/services/call/";
    public static final String TESTDATA_PATH = USER_DIR + "/src/test/resources/testdata/";


}
