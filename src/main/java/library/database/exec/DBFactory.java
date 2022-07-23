package library.database.exec;

import library.database.exec.manager.*;
import org.apache.logging.log4j.LogManager;

public class DBFactory {
    protected DBFactory() {
    }

    public static DBManager createDB() {
        DatabaseType databaseType = DatabaseType.get(DBTContext.getInstance().getDataBaseType());
        switch (databaseType) {
            case ORACLE:
                return new OracleDBManager();
            case TERADATA:
                return new TeradataManager();
            case DB2:
                return new DB2Manager();
            case MONGO:
                return new MongoDBManager();
            case SQLSERVER:
                return new SQLServerManager();
            default:
                String error = String.format("uncategorized database type: %s", databaseType);
                LogManager.getLogger().error(error);
                throw new IllegalArgumentException(error);
        }
    }
}
