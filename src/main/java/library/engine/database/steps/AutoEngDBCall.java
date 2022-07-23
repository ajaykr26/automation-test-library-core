package library.engine.database.steps;

import io.cucumber.java.en.Given;
import library.engine.database.AutoEngDBBaseSteps;

import java.io.IOException;

import static library.engine.core.AutoEngCoreParser.parseValue;

public class AutoEngDBCall extends AutoEngDBBaseSteps {

    @Given("^the user executes the \"([^\"]*)\" query$")
    public void executeQuery(String queryName) throws IOException {
        queryName = getDBObject(parseValue(queryName));

        if (queryName != null) {
            callDBQuery(queryName);
        }
    }

}
