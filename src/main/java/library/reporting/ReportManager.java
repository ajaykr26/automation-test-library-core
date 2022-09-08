package library.reporting;

import java.util.Map;

public interface ReportManager {

    void addStepLog(String message);

    void addStepLog(String status, String message);

    void failScenario(Throwable message);

    void addDataTable(String tableName, Map<String, Object> dataTable);

    void addScreenCaptureFromPath(String screenshotPath);

    void saveReport();

    String getReportPath();

    void addTextLogContent(String logTitle, String content);

    void addAttachmentToReport(String filepath, String title);

}
