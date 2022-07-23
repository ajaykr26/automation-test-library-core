package library.database.exec.manager;

import library.database.exec.DBManager;
import oracle.jdbc.driver.OracleDriver;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class OracleDBManager extends DBManager {
    @Override
    protected void createConnection() {
        try {
            DriverManager.registerDriver(new OracleDriver());
            connection = super.createSQLDBConnection();
        }catch (SQLException exception){
            logger.error("unable to connect to DB {}", exception.getMessage(), exception);
        }

    }

    @Override
    protected void closeConnection() {
        try {
            super.closeSQLDBConnection();
        }catch (SQLException exception){
            logger.error("unable to close connection to DB {}", exception.getMessage(), exception);
        }
    }

    @Override
    protected List<Map<String, Object>> runQuery(String query) {
        return super.executeSQLDBQuery(query);
    }
}
