package library.database.exec.manager;

import com.teradata.jdbc.TeraDriver;
import library.database.exec.DBManager;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class TeradataManager extends DBManager {
    @Override
    protected void createConnection() {
        try {
            DriverManager.registerDriver(new TeraDriver());
            connection = super.createSQLDBConnection();
        } catch (SQLException exception) {
            logger.error("unable to connect to DB {}", exception.getMessage(), exception);
        }

    }

    @Override
    protected void closeConnection() {
        try {
            super.closeSQLDBConnection();
        } catch (SQLException exception) {
            logger.error("unable to close connection to DB {}", exception.getMessage(), exception);
        }
    }

    @Override
    protected List<Map<String, Object>> runQuery(String query) {
        return super.executeSQLDBQuery(query);
    }
}
