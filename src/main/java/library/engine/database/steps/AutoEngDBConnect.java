package library.engine.database.steps;

import io.cucumber.java.en.Given;
import library.engine.database.AutoEngDBBaseSteps;

import java.io.IOException;

public class AutoEngDBConnect extends AutoEngDBBaseSteps {

    @Given("^DB connection for application \"([^\"]*)\" is established$")
    public void createDBConnection(String appNameDBType) throws IOException {
        createConnection(appNameDBType);
    }

    @Given("^DB connection for application \"([^\"]*)\" is closed")
    public void closeDBConnection(String appNameDBType) throws IOException {
        closeDBConnection();
    }
}
