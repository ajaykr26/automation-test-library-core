package library.common;

import java.util.Map;
import java.util.stream.Collectors;

public class Formatter {
    Formatter() {
    }

    private static final String DATA_TABLE_LOG_FORMAT = "%-1s %-40s %-1s %-40s %-1s";
    private static final String DATA_TABLE_LOG_DIVIDER = "----------------------------------------";
    private static final String DATA_TABLE_LOG_COLUMN_SEPARATOR = "|";
    private static final String DATA_TABLE_LOG_COLUMN_SEPARATOR_BEGIN = "\n" + DATA_TABLE_LOG_COLUMN_SEPARATOR;

    public static String getDataDictionaryAsFormattedTable() {
        StringBuilder dataTable = new StringBuilder();

        dataTable.append(getDataTableRow(DATA_TABLE_LOG_DIVIDER, DATA_TABLE_LOG_DIVIDER));
        dataTable.append(getDataTableRow("Data Key", "Data Value"));
        dataTable.append(getDataTableRow(DATA_TABLE_LOG_DIVIDER, DATA_TABLE_LOG_DIVIDER));
        getFilteredDataTableAsMap().entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> dataTable.append(getDataTableRow(entry.getKey(), entry.getValue().toString())));
        dataTable.append(getDataTableRow(DATA_TABLE_LOG_DIVIDER, DATA_TABLE_LOG_DIVIDER));
        return dataTable.toString();
    }

    public static String getPropertiesDataAsFormattedTable() {
        StringBuilder dataTable = new StringBuilder();

        dataTable.append(getDataTableRow(DATA_TABLE_LOG_DIVIDER, DATA_TABLE_LOG_DIVIDER));
        dataTable.append(getDataTableRow("Data Key", "Data Value"));
        dataTable.append(getDataTableRow(DATA_TABLE_LOG_DIVIDER, DATA_TABLE_LOG_DIVIDER));
        TestContext.getInstance().propData().entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> dataTable.append(getDataTableRow(entry.getKey(), entry.getValue().toString())));
        dataTable.append(getDataTableRow(DATA_TABLE_LOG_DIVIDER, DATA_TABLE_LOG_DIVIDER));
        return dataTable.toString();
    }

    public static String getDataDictionaryAndPropertiesDataAsFormattedTable() {
        return getDataDictionaryAsFormattedTable().concat(getPropertiesDataAsFormattedTable());
    }

    private static String getDataTableRow(String key, String value) {
        return String.format(DATA_TABLE_LOG_FORMAT, DATA_TABLE_LOG_COLUMN_SEPARATOR_BEGIN, key, DATA_TABLE_LOG_COLUMN_SEPARATOR, value, DATA_TABLE_LOG_COLUMN_SEPARATOR);
    }

    public static Map<String, Object> getFilteredDataTableAsMap() {
        return TestContext.getInstance().testdata().entrySet().stream()
                .filter(entry -> entry.getKey() != null)
                .filter(entry -> !entry.getKey().contains("fw."))
                .filter(entry -> !entry.getKey().contains("Validation."))
                .filter(entry -> !entry.getKey().contains("priorData"))
                .filter(entry -> !entry.getKey().matches(".*(?i)(password|pwd|pass|user|username|userid)(?i).*"))
                .filter(entry -> !entry.getKey().matches(".*(?i)(response|responseXml|responseHeaders|responseStatus|requestBody)(?i).*"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static Map<String, Object> getFilteredValidationTableAsMap() {
        return TestContext.getInstance().testdata().entrySet()
                .stream()
                .filter(entry -> !entry.getKey().contains("VALIDATION."))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static Map<String, Object> getFilteredPriorDataTableAsMap() {
        return TestContext.getInstance().testdata().entrySet()
                .stream()
                .filter(entry -> !entry.getKey().contains("priorData"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}