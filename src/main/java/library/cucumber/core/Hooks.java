package library.cucumber.core;

import io.cucumber.core.api.Scenario;
import io.cucumber.java8.En;
import library.common.*;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.Map;

import static library.reporting.ReportFactory.getReporter;


public class Hooks implements En {

    protected static Logger logger = LogManager.getLogger(Hooks.class.getName());

    public Hooks() {
        Before(10, (Scenario scenario) -> {
            loadTestDataFromJsonFile(scenario.getName());
            loadTestDataFromExcelFile(scenario.getName());
            loadTestDataPropertiesFile();
        });
        After(20, (Scenario scenario) -> {//2

            TestContext.getInstance().resetTestdata();
            TestContext.getInstance().resetPropData();
            checkForSoftAssertFailure();
        });
    }

    private void checkForSoftAssertFailure() {
        try {
            TestContext.getInstance().softAssertions().assertAll();
        } catch (AssertionError assertionError) {
            getReporter().failScenario(assertionError.fillInStackTrace());
            logger.error(assertionError.getMessage());
            TestContext.getInstance().resetSoftAssert();
            throw assertionError;
        }
    }

    private void loadTestDataFromJsonFile(String scenario) {
        String featureName = System.getProperty("fw.featureName");
        String dataFileJSON = Constants.FEATURE_PATH + featureName + ".json";

        Map<String, String> jsonDataTable = JSONHelper.getJSONToMap(JSONHelper.getJSONObject(dataFileJSON, scenario));
        TestContext.getInstance().testdata().putAll(jsonDataTable);

    }

    private void loadTestDataFromExcelFile(String scenario) {
        String featureName = System.getProperty("fw.featureName");

        Map<String, Object> excelDataTable = ExcelHelper.getDataAsMap(Constants.TESTDATA_EXCEL, featureName).get(scenario);
        if (excelDataTable != null) TestContext.getInstance().testdata().putAll(excelDataTable);

    }

    private void loadTestDataPropertiesFile() {
        TestContext.getInstance().propData().putAll(Property.getPropertiesAsMap(Constants.ENVIRONMENT_PROP_FILE));
        TestContext.getInstance().propData().putAll(Property.getPropertiesAsMap(Constants.SECURE_TEXT_PROP_FILE));
    }

}
