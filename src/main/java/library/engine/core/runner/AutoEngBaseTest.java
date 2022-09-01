package library.engine.core.runner;

import io.cucumber.testng.CucumberOptions;
import library.cucumber.selenium.BaseTest;
import org.testng.annotations.AfterTest;

@CucumberOptions(
        plugin = {"library.engine.core.runner.AutoEngFormatter",
                "io.qameta.allure.cucumber4jvm.AllureCucumber4Jvm",
                "json:target/cucumber-reports/runReport.json",
                "rerun:target/failed-scenario/failed-scenario.txt"},
        features = {"classpath:features", "classpath:apiobjects" })
public class AutoEngBaseTest extends BaseTest {

}
