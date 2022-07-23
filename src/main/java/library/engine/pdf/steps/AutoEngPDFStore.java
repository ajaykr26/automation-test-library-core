package library.engine.pdf.steps;

import io.cucumber.java.en.Given;
import library.common.PDFHelper;
import library.common.TestContext;
import library.engine.pdf.AutoEngPDFBaseSteps;
import org.apache.pdfbox.pdmodel.PDDocument;

import static library.engine.core.AutoEngCoreParser.*;
import static library.reporting.ReportFactory.getReporter;

public class AutoEngPDFStore extends AutoEngPDFBaseSteps {
    @Given("^the user store the content of the currently active pdf into the data dictionary with key \"([^\"]*)\"$")
    public void storePDFContent(String dictionaryKey) throws IllegalAccessException {
        PDDocument document = getActivePDF();
        String pdfDocumentText = PDFHelper.getPDFText(document);
        dictionaryKey = parseDictionaryKey(dictionaryKey);

        TestContext.getInstance().testdata().put(CURRENT_PAGE_PDF, 0);
        TestContext.getInstance().testdataPut(dictionaryKey, pdfDocumentText);
        getReporter().addStepLog(String.format(STORED_VALUE, pdfDocumentText, dictionaryKey));
    }

    @Given("^the user store the content of the currently active pdf from page \"([^\"]*)\" for number of pages \"([^\"]*)\" into the data dictionary with key \"([^\"]*)\"$")
    public void storePDFContentsBetweenPages(String pageStart, String pageCount, String dictionaryKey) throws IllegalAccessException {
        PDDocument document = getActivePDF();
        pageStart = parseValue(pageStart);
        pageCount = parseValue(pageCount);
        int pageEnd = Integer.parseInt(pageStart) + Integer.parseInt(pageCount) - 1;

        String pdfDocumentText = PDFHelper.getPDFText(document, Integer.parseInt(pageStart), pageEnd);
        dictionaryKey = parseDictionaryKey(dictionaryKey);

        TestContext.getInstance().testdata().put(CURRENT_PAGE_PDF, 0);
        TestContext.getInstance().testdataPut(dictionaryKey, pdfDocumentText);
        getReporter().addStepLog(String.format(STORED_VALUE, pdfDocumentText, dictionaryKey));
    }

    @Given("^the user store the page number of the currently active pdf which contains value \"([^\"]*)\" into the data dictionary with key \"([^\"]*)\"$")
    public void storePDFPageNumberContainingValue(String textToFind, String dictionaryKey) throws IllegalAccessException {
        PDDocument document = getActivePDF();
        textToFind = parseValue(textToFind);
        int pdfPageNum = PDFHelper.getPDFPageNumber(document, textToFind);
        dictionaryKey = parseDictionaryKey(dictionaryKey);

        TestContext.getInstance().testdata().put(CURRENT_PAGE_PDF, 0);
        TestContext.getInstance().testdataPut(dictionaryKey, pdfPageNum);
        getReporter().addStepLog(String.format(STORED_VALUE, pdfPageNum, dictionaryKey));
    }

    @Given("^the user store the currently active pdf on page \"([^\"]*)\" into the data dictionary with key \"([^\"]*)\"$")
    public void storePDFContainsTextOnPage(String pageStart, String dictionaryKey) throws IllegalAccessException {
        PDDocument document = getActivePDF();
        pageStart = parseValue(pageStart);
        dictionaryKey = parseDictionaryKey(dictionaryKey);

        TestContext.getInstance().testdata().put(CURRENT_PAGE_PDF, 0);
        String pdfDocumentText = PDFHelper.getPDFText(document, Integer.parseInt(pageStart), Integer.parseInt(pageStart));
        TestContext.getInstance().testdataPut(dictionaryKey, pdfDocumentText);
        getReporter().addStepLog(String.format(STORED_VALUE, pdfDocumentText, dictionaryKey));
    }
}