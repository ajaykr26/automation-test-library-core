package library.reporting;

public class ReportFactory {
    public static ReportManager getReporter() {
        String report = System.getProperty("fw.report");
        switch (report.toUpperCase()) {
            case "ALLURE":
                return new AllureReporter();
            case "EXTENT":
                return new ExtentReporter();
            default:
                throw new UnsupportedOperationException("invalid report type: " + report);
        }
    }
}
