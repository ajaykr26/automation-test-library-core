package library.engine.api.runner;

import io.cucumber.testng.CucumberOptions;
import library.api.Constants;
import library.common.Property;
import library.engine.core.runner.AutoEngBaseTest;
import org.testng.annotations.BeforeClass;

public class AutoEngBaseTestAPI extends AutoEngBaseTest {
    @BeforeClass
    public void beforeClass() {
        if (Property.getVariable("cukes.env") != null) {
            System.setProperty("karate.env", Property.getVariable("cukes.env"));
        }
        if (Property.getVariable("fw.threadsForAPI") != null) {
            System.setProperty("threads", Property.getVariable("fw.threadsForAPI"));
        } else {
            System.setProperty("threads", "3");
        }
        System.setProperty("fw.apiObjectsFolder", Constants.API_OBJECT_FOLDER);

    }
}
