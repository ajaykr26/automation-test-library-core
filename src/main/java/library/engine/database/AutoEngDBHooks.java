package library.engine.database;

import io.cucumber.core.api.Scenario;
import io.cucumber.java8.En;
import library.common.Property;
import library.common.TestContext;
import library.database.exec.DBTContext;
import library.engine.core.objectmatcher.fetch.FetchFlatFileObjects;
import org.apache.commons.configuration2.PropertiesConfiguration;

import static library.database.core.Constants.DBRUNTIMEPATH;

public class AutoEngDBHooks implements En {

    public AutoEngDBHooks() {
        Before(35, (Scenario scenario) -> {
            TestContext.getInstance().setOfDB().addAll(FetchFlatFileObjects.populateListOfDBObjects());
            setDBRuntimeProperties();
        });
        After(40, (Scenario scenario) -> {
            if (DBTContext.getInstance().isConnActive()) {
                DBTContext.getInstance().closeConnection();
                DBTContext.removeInstance();
            }
        });
    }

    private void setDBRuntimeProperties() {
        PropertiesConfiguration props = Property.getProperties(DBRUNTIMEPATH);
        if (props != null) {
            String dbMaxRowCount = props.getString("DBMaxRowCount") == null ? "100" : props.getString("DBMaxRowCount");
            System.setProperty("fw.DBMaxRowCount", dbMaxRowCount);
            String dbNullFlag = props.getString("dbNullFlag") == null ? "false" : props.getString("dbNullFlag");
            System.setProperty("fw.dbNullFlag", dbNullFlag);
        }
    }
}