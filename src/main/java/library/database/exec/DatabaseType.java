package library.database.exec;

import java.util.Arrays;
import java.util.Optional;

public enum DatabaseType {
    ORACLE("oracle"),
    TERADATA("teradata"),
    DB2("db2"),
    MONGO("mongo"),
    SQLSERVER("sqlserver");

    private final String databaseName;

    DatabaseType(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public static DatabaseType get(String databaseName) {
        Optional<DatabaseType> first = Arrays.stream(DatabaseType.values())
                .filter(database -> database.getDatabaseName()
                        .equalsIgnoreCase(databaseName))
                .findFirst();

        return first.orElse(null);
    }

}
