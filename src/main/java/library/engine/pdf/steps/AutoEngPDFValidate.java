package library.engine.pdf.steps;

import io.cucumber.java.en.Then;
import library.common.PDFHelper;
import library.common.TestContext;
import library.engine.core.validator.AssertHelper;
import library.engine.core.validator.ComparisonOperator;
import library.engine.core.validator.ComparisonType;
import library.engine.pdf.AutoEngPDFBaseSteps;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.assertj.core.api.Assertions;

import static library.engine.core.AutoEngCoreParser.parseValue;
import static library.reporting.ReportFactory.getReporter;

public class AutoEngPDFValidate extends AutoEngPDFBaseSteps {

    private static final String HARD_STOP_ON_FAILURE = "HardStopOnFailure";

    @Then("^the user validates the orientation is in ((?:landscape|portrait)?) mode \"([^\"]*)\" \"([^\"]*)\"$")
    public void thePDFIsInPortraitMode(String orientation, String validationId, String failureFlag) throws IllegalAccessException {
        PDDocument document = getActivePDF();
        PDPage page = PDFHelper.getPDFPage(document, 0);
        PDRectangle mediaBox = page.getMediaBox();

        TestContext.getInstance().testdata().put(CURRENT_PAGE_PDF, -1);
        final String compareDescription = String.format("expecting pdf to be in %s orientation", orientation);
        TestContext.getInstance().testdata().put(VALIDATION_TAG + validationId, compareDescription);

        if (orientation.equalsIgnoreCase("landscape")) {
            boolean isLandScape = mediaBox.getWidth() > mediaBox.getHeight();
            boolean expectedOrientation = orientation.equalsIgnoreCase("landscape");
            if (failureFlag.endsWith(HARD_STOP_ON_FAILURE)) {
                Assertions.assertThat(isLandScape).isEqualTo(expectedOrientation);
            } else {
                softAssertions().assertThat(isLandScape).isEqualTo(expectedOrientation);
                if (isLandScape != expectedOrientation) {
                    getReporter().addStepLog(STATUS_FAILED, VALIDATION_FAILED + compareDescription);
                }
            }
        } else if (orientation.equalsIgnoreCase("portrait")) {
            boolean isPortrait = mediaBox.getWidth() < mediaBox.getHeight();
            boolean expectedOrientation = orientation.equalsIgnoreCase("portrait");
            if (failureFlag.endsWith(HARD_STOP_ON_FAILURE)) {
                Assertions.assertThat(isPortrait).isEqualTo(expectedOrientation);
            } else {
                softAssertions().assertThat(isPortrait).isEqualTo(expectedOrientation);
                if (isPortrait != expectedOrientation) {
                    getReporter().addStepLog(STATUS_FAILED, VALIDATION_FAILED + compareDescription);
                }
            }
        }
    }

    @Then("^the user validates the currently active pdf \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public void thePdfContainsText(String comparisonOperator, String textToFind, String validationId, String failureFlag) throws IllegalAccessException {
        PDDocument document = getActivePDF();
        textToFind = parseValue(textToFind);
        String pdfDocumentText = PDFHelper.getPDFText(document);
        String actualResult = searchPDFForPattern(textToFind, pdfDocumentText);

        int pdfPageNum = PDFHelper.getPDFPageNumber(document, textToFind);

        if (pdfPageNum == 0) {
            TestContext.getInstance().testdata().put(CURRENT_PAGE_PDF, -1);
        } else {
            TestContext.getInstance().testdata().put(CURRENT_PAGE_PDF, pdfPageNum);
        }
        AssertHelper validator = new AssertHelper(ComparisonType.COMPARE_STRING, ComparisonOperator.valueOfLabel(comparisonOperator), failureFlag);
        validator.performValidation(actualResult.trim(), textToFind, validationId, "PDF text comparison");
    }

    @Then("^the user validates the currently active pdf \"([^\"]*)\" \"([^\"]*)\" on page \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public void thePdfContainsTextOnPage(String comparisonOperator, String textToFind, int pageNum, String validationId, String failureFlag) throws IllegalAccessException {
        thePdfContainsTextBetweenPages(comparisonOperator, textToFind, pageNum, pageNum, validationId, failureFlag);
    }

    @Then("^the user validates the currently active pdf \"([^\"]*)\" \"([^\"]*)\" between pages \"([^\"]*)\" to \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public void thePdfContainsTextBetweenPages(String comparisonOperator, String textToFind, int pageStart, int pageEnd, String validationId, String failureFlag) throws IllegalAccessException {
        PDDocument document = getActivePDF();
        textToFind = parseValue(textToFind);
        String pdfDocumentText = PDFHelper.getPDFText(document, pageStart, pageEnd);
        String actualResult = searchPDFForPattern(textToFind, pdfDocumentText);

        int pdfPageNum = PDFHelper.getPageNumberInBetweenDoc(document, textToFind, pageStart, pageEnd);

        TestContext.getInstance().testdata().put(CURRENT_PAGE_PDF, pdfPageNum);

        if (pdfPageNum == 0) {
            TestContext.getInstance().testdata().put(PGNUM_START_PDF, pageStart);
            TestContext.getInstance().testdata().put(PGNUM_END_PDF, pageEnd);
        }
        AssertHelper validator = new AssertHelper(ComparisonType.COMPARE_STRING, ComparisonOperator.valueOfLabel(comparisonOperator), failureFlag);
        validator.performValidation(actualResult.trim(), textToFind, validationId, String.format("PDF text comparison between pages '%s' and '%s'", pageStart, pageEnd));
    }


}
