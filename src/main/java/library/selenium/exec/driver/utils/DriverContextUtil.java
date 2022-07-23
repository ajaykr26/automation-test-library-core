package library.selenium.exec.driver.utils;

import library.common.Constants;
import library.common.JSONHelper;
import library.selenium.exec.driver.factory.DriverContext;

import java.util.List;
import java.util.Map;

public class DriverContextUtil {
    private static final String DRIVERPATH = "cukes.driverPath";
    private static final String IEDRIVERSERVER = "IEDriverServer";
    private static final String CHROMEDRIVER = "chromedriver";

    private DriverContextUtil() {
    }

    public static void setDriverContext(String teckstack) {
        List<Map<String, String>> listOfTechstacks = JSONHelper.getJSONAsListOfMaps(Constants.TECHSTACK_PATH + teckstack.toUpperCase() + ".json");
        DriverContext.getInstance().setDriverContext(listOfTechstacks.get(0));

    }

    public static void updateDriverContext(String browserName) {
        Map<String, String> currentTechStackMap = DriverContext.getInstance().getTechStack();
        String currentTechStack = System.getProperty("cukes.techstack");
        String newTechStack = "";
        switch (browserName) {
            case "internet explorer":
                newTechStack = currentTechStackMap.get("serverName").equalsIgnoreCase("remote") ? "REMOTE_IEXPLORE" : "LOCAL_IEXPLORE";
                break;
            case "chrome":
                newTechStack = currentTechStackMap.get("serverName").equalsIgnoreCase("remote") ? "REMOTE_CHROME" : "LOCAL_CHROME";
                break;
            default:
                break;
        }
        System.setProperty("cukes.techstack", newTechStack);
        List<Map<String, String>> listOfTechstacks = JSONHelper.getJSONAsListOfMaps(Constants.TECHSTACKS.replace(currentTechStack, newTechStack));
        DriverContext.getInstance().setDriverContext(listOfTechstacks.get(0));

    }

    public static void setDriverPath(String browserName) {
        String driverPath = System.getProperty(DRIVERPATH);
        if (driverPath != null) {
            switch (browserName) {
                case "chrome":
                    driverPath = getChromeDriverPath(driverPath);
                    break;
                case "internet explorer":
                    driverPath = getIEDriverPath(driverPath);
                    break;
            }
            System.setProperty(DRIVERPATH, driverPath);
        }

    }

    public static String getIEDriverPath(String driverPath) {
        if (driverPath.contains(CHROMEDRIVER)) {
            if (driverPath.contains("Program Files")) {
                driverPath = System.getProperty(DRIVERPATH).replace(CHROMEDRIVER, "IE Drivers\\IEDriverServer");

            } else {
                driverPath = System.getProperty(DRIVERPATH).replace(CHROMEDRIVER, IEDRIVERSERVER);

            }
        }
        return driverPath;
    }

    public static String getChromeDriverPath(String driverPath) {
        if (driverPath.contains(IEDRIVERSERVER)) {
            if (driverPath.contains("Program Files")) {
                driverPath = System.getProperty(DRIVERPATH).replace("IE Drivers\\IEDriverServer", CHROMEDRIVER);
            } else {
                driverPath = System.getProperty(DRIVERPATH).replace(IEDRIVERSERVER, CHROMEDRIVER);

            }
        }
        return driverPath;
    }
}
