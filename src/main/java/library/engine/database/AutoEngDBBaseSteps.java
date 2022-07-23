package library.engine.database;

import library.common.FileHelper;
import library.common.Property;
import library.common.TestContext;
import library.database.core.Constants;
import library.database.exec.DBTContext;
import library.engine.core.AutoEngCoreBaseStep;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static library.database.core.Constants.ENVIRONMENTPATH;
import static library.engine.core.objectmatcher.ObjectFinder.getMatchingDBQuery;
import static library.reporting.ReportFactory.getReporter;
import static library.engine.core.AutoEngCoreParser.*;



public class AutoEngDBBaseSteps extends AutoEngCoreBaseStep {
    private static final String PROPS_FILE_PATH = ENVIRONMENTPATH + Property.getVariable(ENV_PROP) + PROPERTIES_EXT;
    private static final String STATUS_FAIL = "FAIL";
    private static final String UNDERSCORE = "_";
    private static final String ERROR_RESPONSE = "-1";
    private static final String VALIDATION_TAG = "VALIDATION.";
    protected static final String QUERY_RESULT = "fw.queryResult";

    private static final String QUERY_RESULT_LOG_FORMAT = "%-1s %-40s %-1s %-40s %-1s";
    private static final String QUERY_RESULT_LOG_DIVIDER = "----------------------------------------";
    private static final String QUERY_RESULT_LOG_COLUMN_SEPARATOR = "|";
    private static final String QUERY_RESULT_LOG_COLUMN_SEPARATOR_BEGIN = "\n" + QUERY_RESULT_LOG_COLUMN_SEPARATOR;


    public AutoEngDBBaseSteps() {
    }

    protected void closeDBConnection() {
        DBTContext.getInstance().closeConnection();
    }

    protected String getDBObject(String queryName) {
        String queryFileName = getMatchingDBQuery(queryName).getFlatFileObjectName();

        if (queryFileName != null) {
            return queryFileName;
        } else {
            try {
                throw new FileNotFoundException("");
            } catch (FileNotFoundException exception) {
                logger.error(exception.getMessage());
            }
        }
        return null;
    }

    protected void callDBQuery(String queryName) throws IOException {
        try {
            List<Map<String, Object>> result = runDBQueryFile(getPathToQueryFile(queryName));
            TestContext.getInstance().testdataPut(QUERY_RESULT, result);
            logQueryResult(result, queryName);
        } catch (IOException exception) {
            getReporter().addStepLog(STATUS_FAIL, "Step failed: " + queryName + " execution is failed. see error message" + exception.getMessage());
            throw exception;
        }
    }

    private void logQueryResult(List<Map<String, Object>> queryResult, String queryName) {
        if (!queryResult.isEmpty()) {
            Map<String, Object> firstResultRow = prepResultSetForReport(queryResult.get(0));
            logger.debug(getQueryResultAsFormattedTable(firstResultRow, queryName));
            getReporter().addDataTable("Query output data table", prepResultSetForReport(firstResultRow));
        } else {
            logger.warn("no query result returned");
        }
    }

    private Map<String, Object> prepResultSetForReport(Map<String, Object> resultSet) {
        return resultSet.entrySet().stream().map(entry -> {
            if (entry.getValue() == null)
                entry.setValue("null");
            return entry;
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private String getQueryResultAsFormattedTable(Map<String, Object> resultRow, String queryName) {
        StringBuilder queryResultRow = new StringBuilder();

        queryResultRow.append(String.format("%n%nFirst row of the %s query result: ", queryName));
        queryResultRow.append(getDataTableRow(QUERY_RESULT_LOG_DIVIDER, QUERY_RESULT_LOG_DIVIDER));
        queryResultRow.append(getDataTableRow("DB Column Name", "DB Column Value"));
        queryResultRow.append(getDataTableRow(QUERY_RESULT_LOG_DIVIDER, QUERY_RESULT_LOG_DIVIDER));
        resultRow.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> queryResultRow.append(getDataTableRow(entry.getKey(), entry.getValue().toString())));
        queryResultRow.append(getDataTableRow(QUERY_RESULT_LOG_DIVIDER, QUERY_RESULT_LOG_DIVIDER));
        return queryResultRow.toString();
    }

    private static String getDataTableRow(String key, String value) {
        return String.format(QUERY_RESULT_LOG_FORMAT, QUERY_RESULT_LOG_COLUMN_SEPARATOR_BEGIN, key, QUERY_RESULT_LOG_COLUMN_SEPARATOR, value, QUERY_RESULT_LOG_COLUMN_SEPARATOR);
    }

    private List<Map<String, Object>> runDBQueryFile(String filepath) throws IOException {
        String query = FileHelper.getFileAsString(filepath, "\n");
        query = replaceParameterValues(query);
        logger.info(query);
        return DBTContext.getInstance().executeQuery(query);
    }

    private String getPathToQueryFile(String queryName) {
        if (DBTContext.getInstance().getConnection() != null) {
            queryName = queryName + ".sql";
        } else {
            queryName = queryName + ".json";
        }
        return FileHelper.findFileInPath(Constants.DBOBJECTPATH, queryName);
    }

    protected void createConnection(String appNameDBType) {
        TestContext.getInstance().setActiveWindowType("DB");
        if (appNameDBType != null && appNameDBType.matches("^[a-zA-Z0-9]+?_{1}[a-zA-Z0-9]+?$")) {
            String dbConnectionString = Property.getProperty(PROPS_FILE_PATH, appNameDBType);
            if (dbConnectionString != null) {
                DBTContext.getInstance().setAppName(appNameDBType.split(UNDERSCORE)[0]);
                DBTContext.getInstance().setDataBaseType(appNameDBType.split(UNDERSCORE)[1]);
                DBTContext.getInstance().setDataBaseURL(dbConnectionString);

                DBTContext.getInstance().createConnection();

                FileHelper.loadDataParameterFromPropsFile(PROPS_FILE_PATH, "db");
            } else {
                throw new IllegalArgumentException(String.format("did not find a connection string for '%s' DB properties file: '%s'", appNameDBType, PROPS_FILE_PATH));
            }
        }
    }

    protected void storeQueryResult(String queryResultKey) {
        List<Map<String, Object>> result = (List<Map<String, Object>>) TestContext.getInstance().testdataGet(QUERY_RESULT);
        if (result != null && !result.isEmpty()) {
            TestContext.getInstance().testdataPut(queryResultKey, result);
        } else {
            String errorMsg = "The query result did not have records";
            logger.warn(errorMsg);
            getReporter().addStepLog("FAIL", errorMsg);
        }
    }

    protected void storeColumnValueFromQueryResult(String index, String colName, String dictionaryKey) {
        Object valueToStore = getActualValueFromDBResult(TestContext.getInstance().testdataToClass(QUERY_RESULT, List.class), colName, Integer.parseInt(index));
        TestContext.getInstance().testdataPut(dictionaryKey, valueToStore);
        getReporter().addStepLog(String.format(STORED_VALUE, valueToStore, dictionaryKey));
    }

    protected Object getActualValueFromDBResult(List<Map<String, Object>> result, String colName, int index) {
        if (result != null && !result.isEmpty() && colName != null) {
            if (index > 0 && index <= result.size()) {
                return convertNullToString(String.valueOf(result.get(index - 1).get(colName)));
            } else if (index == 0) {
                return convertNullToString(String.valueOf(result.get(0).get(colName)));
            } else {
                throw new IllegalArgumentException(String.format("invalid index provided to the DB query result set: %s", index));
            }
        } else {
            String errorMsg = "the query result is empty";
            logger.warn(errorMsg);
            getReporter().addStepLog("FAIL", errorMsg);
            return ERROR_RESPONSE;
        }
    }

    private String convertNullToString(String colValFromDB) {
        return colValFromDB == null ? "null" : colValFromDB;
    }
}
