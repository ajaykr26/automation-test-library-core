package library.engine.pdf.steps;

import io.cucumber.java.en.Given;
import library.common.Constants;
import library.common.PDFHelper;
import library.common.TestContext;
import library.engine.pdf.AutoEngPDFBaseSteps;
import org.apache.pdfbox.pdmodel.PDDocument;

import static library.engine.core.AutoEngCoreConstants.PDF;
import static library.engine.core.AutoEngCoreParser.parseValue;

public class AutoEngPDFOpen extends AutoEngPDFBaseSteps {
    public static final String PDF_EXTENSION = ".pdf";

    @Given("^the user opens the \"([^\"]*)\" pdf kept in testdata folder$")
    public void thePdfIsOpened(String filepath) {
        TestContext.getInstance().setActiveWindowType(PDF);
        filepath = parseValue(Constants.TESTDATA_PATH + filepath + ".pdf");
        PDDocument document = PDFHelper.getDoc(filepath);
        if (document != null) {
            TestContext.getInstance().testdataPut(ACTIVE_PDF, document);
            TestContext.getInstance().testdata().put(CURRENT_PAGE_PDF, 0);
        } else {
            throw new IllegalArgumentException(String.format("could not find PDF at path: %s", filepath));
        }
    }

    @Given("^the user opens the \"([^\"]*)\" pdf kept at \"([^\"]*)\" path$")
    public void thePdfIsOpened(String filename, String filepath) {
        TestContext.getInstance().setActiveWindowType(PDF);
        filepath = parseValue(filepath);
        filename = parseValue(filename);
        if (filepath.endsWith("\\") || filepath.endsWith("/"))
            filepath = filepath + filename + PDF_EXTENSION;
        else
            filepath = filepath + "/" + filename + PDF_EXTENSION;

        PDDocument document = PDFHelper.getDoc(filepath);
        if (document != null) {
            TestContext.getInstance().testdataPut(ACTIVE_PDF, document);
            TestContext.getInstance().testdata().put(CURRENT_PAGE_PDF, 0);
        } else {
            throw new IllegalArgumentException(String.format("could not find PDF at path: %s", filepath));
        }
    }

    @Given("^the active pdf is closed$")
    public void thePdfIsClosed() throws IllegalAccessException {
        PDFHelper.closePDFDoc(getActivePDF());
        TestContext.getInstance().testdata().remove(ACTIVE_PDF);
    }
}

