package library.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Protocol;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.qameta.allure.Allure;
import library.common.*;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import static library.selenium.exec.BaseTest.reportPath;

public class ExtentReporter implements ReportManager {

    private static ExtentReports extentReports;
    private static Map<Integer, ExtentTest> extentTestMap = new HashMap<Integer, ExtentTest>();
    private static final Logger logger = LogManager.getLogger(ExtentReporter.class);

    public void addStepLog(String message) {
        addStepLog("INFO", message);
    }

    public void addStepLog(String status, String message) {
        switch (status) {
            case "FAIL":
                getTest().log(Status.FAIL, message);
                break;
            case "PASS":
                getTest().log(Status.PASS, message);
                break;
            case "SKIP":
                getTest().log(Status.SKIP, message);
                break;
            case "ERROR":
                getTest().log(Status.WARNING, message);
                break;
            default:
                getTest().info(message);
        }

    }

    public void failScenario(Throwable message) {

    }

    public void addDataTable(String tableName, Map<String, Object> dataTable) {
        final StringBuilder dataTableTabSeparated = new StringBuilder();
        if (dataTable != null && !dataTable.isEmpty()) {
            dataTable.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> {
                        dataTableTabSeparated.append(String.join(":", entry.getKey(), entry.getValue().toString()));
                        dataTableTabSeparated.append("\n");
                    });

            getTest().info(dataTableTabSeparated.toString());
        }


    }

    public void addScreenCaptureFromPath(String imagePath) {
        imagePath = ".\\screenshots" + imagePath.split("screenshots")[1];
        getTest().info(MediaEntityBuilder.createScreenCaptureFromPath(imagePath).build());
    }

    public void saveReport() {
        String currentTimeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        FileHelper.createDirectoryPath(".\\run-reports");
        String extentReportTargetPath = "run-reports/extent-report-" + currentTimeStamp + ".zip";
        try {
            ZipHelper.zipAndCopyDirectory("target/extent-reports", extentReportTargetPath);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public String getReportPath() {
        String defaultReportPath = Constants.EXTENT_REPORT_PATH;
        String reportPath = Property.getVariable("reportPath");
        return (reportPath == null ? defaultReportPath : reportPath);
    }

    public void addTextLogContent(String logTitle, String content) {
            getTest().info(MarkupHelper.createJsonCodeBlock(content));
    }

    public synchronized static ExtentReports getExtentReports(String filePath) {
        if (extentReports == null) {
            extentReports = setReporter(filePath);
        }
        return extentReports;
    }

    public synchronized static ExtentReports getExtentReports() {
        return extentReports;
    }

    private static ExtentReports setReporter(String reportPath) {
        FileHelper.createDirectoryPath(reportPath);

        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        ExtentReports extent = new ExtentReports();
        extent.attachReporter(spark);
        spark.config().setCss("css-string");
        spark.config().setDocumentTitle("automation test report");
        spark.config().setEncoding("utf-8");
        spark.config().setJs("js-string");
        spark.config().setProtocol(Protocol.HTTPS);
        spark.config().setReportName("build name");
        spark.config().setTheme(Theme.STANDARD);
        spark.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");


        extentReports = new ExtentReports();
        extentReports.attachReporter(spark);


        extentReports.setSystemInfo("application", Property.getProperty(Constants.ENVIRONMENT_PROP_FILE, "application"));
        extentReports.setSystemInfo("environment", System.getProperty(Constants.ENVIRONMENT_PROP_FILE, "environment"));

        return extentReports;

    }

    public static synchronized ExtentTest getTest() {
        return extentTestMap.get((int) (long) (Thread.currentThread().getId()));
    }

    public static synchronized ExtentTest getTest(String testName, String desc) {
        ExtentTest extentTest = extentReports.createTest(testName, desc);
        return extentTestMap.get((int) (long) (Thread.currentThread().getId()));
    }

    public static synchronized void startTest(String testName) {
        startTest(testName, "");
    }

    public static synchronized void startTest(String testName, String desc) {
        ExtentTest extentTest = extentReports.createTest(testName, desc);
        extentTestMap.put((int) (long) (Thread.currentThread().getId()), extentTest);
    }

    public static synchronized void endTest() {
        extentReports.removeTest(extentTestMap.get((int) (long) (Thread.currentThread().getId())));
    }

    private static List<String> getAddress(String address) {
        List<String> addressList = new ArrayList<String>();

        if (address.isEmpty())
            return addressList;

        if (address.indexOf(";") > 0) {
            String[] addresses = address.split(";");

            addressList.addAll(Arrays.asList(addresses));
        } else {
            addressList.add(address);
        }

        return addressList;
    }

    public static void addScreenCaptureFromPath(String imagePath, String title) {
        getTest().addScreenCaptureFromPath(imagePath, "Screenshot");
    }

    public static String getScreenshotPath() {
        return reportPath + "/screenshots/" + TestContext.getInstance().testdataGet("fw.scenarioName");
    }

    public static void failTest(Throwable message) {

    }

}
