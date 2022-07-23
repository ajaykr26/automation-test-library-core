package library.database.exec.manager;

import com.ibm.db2.jcc.DB2Driver;
import library.database.exec.DBManager;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DB2Manager extends DBManager {
    @Override
    protected void createConnection() {
        try {
            DriverManager.registerDriver(new DB2Driver());
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