package library.cucumber.api;


import io.cucumber.testng.CucumberFeatureWrapper;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.PickleEventWrapper;
import io.cucumber.testng.TestNGCucumberRunner;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@CucumberOptions(strict = true, glue = "library")
public class BaseTest {

    private TestNGCucumberRunner testNGCucumberRunner;

    @Test(groups = "cucumber", description = "Run Cucumber Scenario", dataProvider = "scenarios")
    public void scenario(PickleEventWrapper pickleEventWrapper, CucumberFeatureWrapper cucumberFeatureWrapper) throws Throwable {
        testNGCucumberRunner.runScenario(pickleEventWrapper.getPickleEvent());
    }

    @DataProvider
    public Object[][] scenarios(){
        return testNGCucumberRunner.provideScenarios();
    }
}
