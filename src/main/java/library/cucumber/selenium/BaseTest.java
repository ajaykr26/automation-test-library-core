package library.cucumber.selenium;

import io.cucumber.testng.CucumberFeatureWrapper;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.PickleEventWrapper;
import io.cucumber.testng.TestNGCucumberRunner;
import library.common.Constants;
import library.common.Formatter;
import library.common.JSONHelper;
import library.common.Property;
import library.common.TestContext;
import org.apache.commons.compress.utils.Lists;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.*;

import static library.reporting.ReportFactory.getReporter;

@CucumberOptions(
        strict = true,
        glue = "library"
)
public class BaseTest extends library.selenium.exec.BaseTest {

    private final TestNGCucumberRunner testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());

    @Test(dataProvider = "techStackWithScenarioList")
    public void scenario(Map<String, String> map, PickleEventWrapper pickleEventWrapper, CucumberFeatureWrapper cucumberFeatureWrapper) throws Throwable {
        testNGCucumberRunner.runScenario(pickleEventWrapper.getPickleEvent());
    }


    @DataProvider(name = "techStackJSON", parallel = true)
    private Object[][] techStackJSON() {
        List<Map<String, String>> listOfTechStack = JSONHelper.getJSONAsListOfMaps(Constants.TECHSTACKS);
        if (!listOfTechStack.isEmpty()) {
            Object[][] objects = new Object[listOfTechStack.size()][1];
            for (int i = 0; i < listOfTechStack.size(); i++) {
                objects[i][0] = listOfTechStack.get(i);
            }
            return objects;
        } else {
            if (Property.getVariable("cukes.techstack") != null) {
                logger.warn("techstack json file not found {}. defaulting to local chrome driver.", Constants.TECHSTACKS);
            }
            Map<String, String> techStack = new HashMap<>();
            techStack.put("serverName", "local");
            techStack.put("browserName", "chrome");
            return new Object[][]{Collections.singletonList(techStack).toArray()};
        }
    }

    @DataProvider(name = "techStackWithScenarioList", parallel = true)
    public Object[][] combineDataProvider() {
        List<Object[]> techStackList = Lists.newArrayList();
        List<Object[]> scenarioList = Lists.newArrayList();
        List<List<Object>> comboList = Lists.newArrayList();

        techStackList.addAll(Arrays.asList(techStackJSON()));
        scenarioList.addAll(Arrays.asList(testNGCucumberRunner.provideScenarios()));

        techStackList.forEach(techStack ->
                scenarioList.forEach(scenario ->
                        comboList.add(Arrays.asList(techStack[0], scenario[0], scenario[1]))));
        Object[][] comboArray = new Object[comboList.size()][3];

        for (int i = 0; i < comboList.size(); i++) {
            comboArray[i][0] = comboList.get(i).get(0);
            comboArray[i][1] = comboList.get(i).get(1);
            comboArray[i][2] = comboList.get(i).get(2);
        }
        return comboArray;
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        testNGCucumberRunner.finish();
        if (Boolean.parseBoolean(System.getProperty("fw.saveRunReport"))) {
            getReporter().saveReport();
        }
    }

}
