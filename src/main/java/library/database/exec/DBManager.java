package library.database.exec;

import com.mongodb.MongoClient;
import library.common.Property;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.*;

import static library.database.core.Constants.ENVIRONMENTPATH;

public abstract class DBManager {
    public static final String UNABLE_TO_FIND_KEY = "unable to find '%s' key in the '%s' file";
    public static final String ENV_PROP = "cukes.env";
    public static final String PROPERTIES_EXT = ".properties";
    protected final Logger logger = LogManager.getLogger(this.getClass().getName());

    protected Connection connection = null;
    protected MongoClient mongoClient = null;

    protected abstract void createConnection();

    protected abstract void closeConnection();

    protected abstract List<Map<String, Object>> runQuery(String query);

    protected Connection createSQLDBConnection() throws SQLException {
        String propsFilePath = getPathToEnvProps();
        return createSQLDBConnection(getDBUserName(propsFilePath), getDBPassword(propsFilePath));
    }

    protected Connection createSQLDBConnection(String dbUsername, String dbPassword) throws SQLException {
        String dbURL = DBTContext.getInstance().getDataBaseURL();

        connection = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
        logger.debug("database connection established successfully");

        DBTContext.getInstance().setConnActive(true);
        DBTContext.getInstance().setConnection(connection);
        return connection;
    }

    protected void closeSQLDBConnection() throws SQLException {
        if (connection != null) {
            connection.close();
            DBTContext.getInstance().setConnActive(false);
            DBTContext.getInstance().setConnection(null);
            logger.debug("database connection closed successfully");
        }
    }

    protected String getDBUserName(String propsFilePath) {
        String usernameKey = String.join("_", DBTContext.getInstance().getAppName(),
                DBTContext.getInstance().getDataBaseType(), "dbUserName");
        String userName = Property.getProperty(propsFilePath, usernameKey);
        if (userName != null) {
            return userName;
        } else {
            throw new IllegalArgumentException(String.format(UNABLE_TO_FIND_KEY, usernameKey, propsFilePath));
        }
    }

    protected String getDBPassword(String propsFilePath) {
        String usernameKey = String.join("_", DBTContext.getInstance().getAppName(),
                DBTContext.getInstance().getDataBaseType(), "dbPassword");
        String password = Property.getProperty(propsFilePath, usernameKey);
        if (password != null) {
            return password;
        } else {
            throw new IllegalArgumentException(String.format(UNABLE_TO_FIND_KEY, usernameKey, propsFilePath));
        }
    }

    protected String getPathToEnvProps() {
        return ENVIRONMENTPATH + "securetext-" + Property.getVariable(ENV_PROP) + PROPERTIES_EXT;
    }

    protected String getDatabaseName() {
        String propsFilePath = ENVIRONMENTPATH + Property.getVariable(ENV_PROP) + PROPERTIES_EXT;
        String databaseNameKey = String.join("_", DBTContext.getInstance().getAppName(),
                DBTContext.getInstance().getDataBaseType(), "databaseName");
        String password = Property.getProperty(propsFilePath, databaseNameKey);
        if (password != null) {
            return password;
        } else {
            throw new IllegalArgumentException(String.format(UNABLE_TO_FIND_KEY, databaseNameKey, propsFilePath));
        }
    }

    protected List<Map<String, Object>> executeSQLDBQuery(String query) {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            int dbMaxRowCount = System.getProperty("fw.DBMaxRowCount") != null ? Integer.parseInt(System.getProperty("fw.DBMaxRowCount")) : 100;
            statement.setMaxRows(dbMaxRowCount);
            return readDatabaseRows(resultSet);

        } catch (SQLException sqlException) {
            return Collections.emptyList();
        }
    }

    protected static List<Map<String, Object>> readDatabaseRows(ResultSet resultSet) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        Clob clob = null;

        while (resultSet.next()) {
            Map<String, Object> map = new HashMap<>();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String key = metaData.getCatalogName(i).toUpperCase();
                if (metaData.getColumnType(i) == Types.CLOB) {
                    clob = resultSet.getClob(key);
                    String clobValue = clob.getSubString(1, (int) clob.length());
                    map.put(key, clobValue);
                } else {
                    map.put(key, resultSet.getObject(key));
                }
            }
            list.add(map);
        }
        return list;
    }

}
