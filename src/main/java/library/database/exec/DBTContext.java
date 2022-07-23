package library.database.exec;

import com.mongodb.MongoClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DBTContext {
    private static final ThreadLocal<DBTContext> instance = ThreadLocal.withInitial(DBTContext::new);
    private final Logger logger = LogManager.getLogger(this.getClass().getName());
    private String appName = null;
    private String dataBaseType = null;
    private String dataBaseURL = null;
    private Connection connection = null;
    private boolean isConnActive = false;
    private String databaseName = null;
    private MongoClient mongoClient = null;
    private DBManager dbManager;

    private DBTContext() {
    }

    public static synchronized DBTContext getInstance() {
        return instance.get();
    }

    public static void removeInstance() {
        instance.remove();
    }

    public void closeConnection() {
        try {
            dbManager.closeConnection();
        } catch (Exception exception) {
            logger.error(exception.getMessage());
        }
    }

    public void createConnection() {
        if (dbManager != null && isConnActive()) {
            closeConnection();
        } else {
            dbManager = DBFactory.createDB();
        }
        dbManager.createConnection();
    }

    public List<Map<String, Object>> executeQuery(String query) {
        if (dbManager != null) {
            return dbManager.runQuery(query);
        } else {
            return Collections.emptyList();
        }
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getDataBaseType() {
        return dataBaseType;
    }

    public void setDataBaseType(String dataBaseType) {
        this.dataBaseType = dataBaseType;
    }

    public String getDataBaseURL() {
        return dataBaseURL;
    }

    public void setDataBaseURL(String dataBaseURL) {
        this.dataBaseURL = dataBaseURL;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean isConnActive() {
        return isConnActive;
    }

    public void setConnActive(boolean connActive) {
        isConnActive = connActive;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public void setMongoClient(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public DBManager getDbManager() {
        return dbManager;
    }

    public void setDbManager(DBManager dbManager) {
        this.dbManager = dbManager;
    }


}
