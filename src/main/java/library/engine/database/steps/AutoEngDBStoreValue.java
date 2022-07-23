package library.engine.database.steps;

import io.cucumber.java.en.Given;
import library.engine.database.AutoEngDBBaseSteps;

import static library.engine.core.AutoEngCoreParser.*;

public class AutoEngDBStoreValue extends AutoEngDBBaseSteps {
    @Given("^the user store query result into the data dictionary with key \"([^\"]*)\"$")
    public void storeQueryResultIntoDataDictionary(String dictionaryKey) {
        dictionaryKey = parseDictionaryKey(dictionaryKey);
        storeQueryResult(dictionaryKey);
    }

    @Given("^the user stores the \"([^\"]*)\" column values into the data dictionary with key \"([^\"]*)\"$")
    public void storeQueryResultIntoDataDictionary(String columnName, String dictionaryKey) {
        dictionaryKey = parseDictionaryKey(dictionaryKey);
        storeColumnValueFromQueryResult("0", parseValue(columnName), dictionaryKey);
    }

    @Given("^the user stores the index \"([^\"]*)\" of the \"([^\"]*)\" column values into the data dictionary with key \"([^\"]*)\"$")
    public void storeQueryResultIntoDataDictionary(String index, String columnName, String dictionaryKey) {
        dictionaryKey = parseDictionaryKey(dictionaryKey);
        storeColumnValueFromQueryResult(index, parseValue(columnName), dictionaryKey);
    }

}
