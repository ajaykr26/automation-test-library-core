package library.engine.pdf;

import library.common.PDFHelper;
import library.common.TestContext;
import library.engine.core.AutoEngCoreBaseStep;
import library.reporting.AllureReporter;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static library.reporting.ReportFactory.getReporter;

public class AutoEngPDFBaseSteps extends AutoEngCoreBaseStep {
    protected static final String VALIDATION_TAG = "VALIDATION.";
    protected static final String STATUS_FAILED = "FAIL";
    protected static final String VALIDATION_FAILED = "Validation Failed: ";
    protected static final String ACTIVE_PDF = "fw.activePDF";
    protected static final String CURRENT_PAGE_PDF = "fw.PDFCurrentPageNumber";
    protected static final String PGNUM_START_PDF = "fw.PDFPageNumStart";
    protected static final String PGNUM_END_PDF = "fw.PDFPageNumEnd";

    protected String searchPDFForPattern(String textToFind, String textToSearch) {
        if (Boolean.parseBoolean(System.getProperty("fw.ignoreWhiteSpaceOnPDFCompare"))) {
            textToSearch = removeNewLineAndOtherWhiteSpace(textToSearch);
        }

        Pattern pattern = Pattern.compile(getPDFRegex(textToFind));
        Matcher matcher = pattern.matcher(textToSearch);
        List<String> matches = new ArrayList<>();
        int numMatches = 0;

        while (matcher.find()) {
            matches.add(matcher.group(1) + matcher.group(2));
            numMatches++;
        }

        matches.add(String.format("Number of matches found: %s", numMatches));
        return String.join("\n", matches);
    }

    protected String searchPDFForPatternDynamic(String textToFind1, String textToSearch, String textToFind2) {
        if (Boolean.parseBoolean(System.getProperty("fw.ignoreWhiteSpaceOnPDFCompare"))) {
            textToSearch = removeNewLineAndOtherWhiteSpace(textToSearch);
        }
        getReporter().addStepLog(String.format("looking for \"%s\" and \"%s\" in the same row", textToFind1, textToFind2));

        Pattern pattern = Pattern.compile(getPDFPatternRegex(textToFind1, textToFind2));
        Matcher matcher = pattern.matcher(textToSearch);
        List<String> matches = new ArrayList<>();

        if (matcher.find()) {
            matches.add(matcher.group(1) + matcher.group(2) + matcher.group(3));
        } else {
            return null;
        }
        return String.join("", matches);
    }

    protected String removeNewLineAndOtherWhiteSpace(String textToModify) {
        return String.join(" ", Arrays.asList(textToModify.split("\\s+")));
    }

    private String getPDFRegex(String textToFind) {
        String numCharacters = System.getProperty("fw.numLeadingCharactersForPDFCompare");
        String pattern = String.format("(\\s{0,%s}\\s*)(%s)", numCharacters, Pattern.quote(textToFind));
        if (Boolean.parseBoolean(System.getProperty("fw.ignoreCaseOnPDFCompare"))) {
            pattern = "(?i)" + pattern;
        }
        return pattern;
    }

    private String getPDFPatternRegex(String textToFind1, String textToFind2) {
        String pattern = String.format("(%s)([//s A-Za-z0-9:]{12,14})(%s)", Pattern.quote(textToFind1), Pattern.quote(textToFind2));
        if (Boolean.parseBoolean(System.getProperty("fw.ignoreCaseOnPDFCompare"))) {
            pattern = "(?i)" + pattern;
        }
        return pattern;
    }

    protected void addPDFScreenshots(PDDocument document) {
        List<File> pdfDocAsList = PDFHelper.takeScreenshotOfFullDoc(document, AllureReporter.getScreenshotPath());
        for (File screeshot : pdfDocAsList) {
            if (screeshot.exists()) {
                getReporter().addScreenCaptureFromPath(screeshot.getAbsolutePath());
            }
        }
    }

    protected void addPDFScreenshotOfPage(PDDocument document, int pageNum) {
        File screenshotOfPage = PDFHelper.takeScreenshotOfPage(document, AllureReporter.getScreenshotPath(), pageNum);
        if (screenshotOfPage.exists()) {
            getReporter().addScreenCaptureFromPath(screenshotOfPage.getAbsolutePath());
        }
    }

    protected PDDocument getActivePDF() throws IllegalAccessException {
        PDDocument document = TestContext.getInstance().testdataToClass(ACTIVE_PDF, PDDocument.class);

        if (document != null) {
            return document;
        } else {
            throw new IllegalAccessException("no pdf are active. please open a pdf.");
        }
    }
}
