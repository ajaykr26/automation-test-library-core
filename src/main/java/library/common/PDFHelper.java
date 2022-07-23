package library.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PDFHelper {
    private static final String ERROR_EXTRACT_MSG = "error: extract not possible";
    private static final String FILENAME_PATTERN = "%s%s-page%s%s";

    private PDFHelper() {

    }

    private static final Logger logger = LogManager.getLogger(PDFHelper.class.getName());

    public static PDDocument getDoc(String path) {
        PDDocument doc = null;
        try {
            doc = PDDocument.load(new File(path));
        } catch (IOException exception) {
            logger.error(exception);
        }
        return doc;
    }

    public static PDDocument getDoc(byte[] content) {
        PDDocument doc = null;
        try {
            doc = PDDocument.load(content);
        } catch (IOException exception) {
            logger.error(exception);
        }
        return doc;
    }

    public static PDPage getPDFPage(PDDocument doc, int num) {
        return doc.getPage(num);
    }

    public static PDPageTree getPDFPages(PDDocument doc) {
        return doc.getPages();
    }

    public static String getPDFText(PDDocument doc) {
        String extractText = null;
        try {
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            extractText = pdfTextStripper.getText(doc);
        } catch (IOException exception) {
            logger.error(exception);
            extractText = ERROR_EXTRACT_MSG;
        }
        return extractText;
    }

    public static String getPDFText(PDDocument doc, int startPage, int... endPage) {
        String extractText = null;
        try {
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            pdfTextStripper.setStartPage(startPage);
            if (endPage.length > 0) {
                pdfTextStripper.setEndPage(endPage[0]);
            } else {
                pdfTextStripper.setEndPage(startPage);
            }
            extractText = pdfTextStripper.getText(doc);
        } catch (IOException exception) {
            logger.error(exception);
            extractText = ERROR_EXTRACT_MSG;
        }
        return extractText;
    }

    public static String[] getPDFText(PDDocument doc, String token, int startPage, int... endPage) {
        String extractText = null;
        try {
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            pdfTextStripper.setSortByPosition(true);
            pdfTextStripper.setStartPage(startPage);
            if (endPage.length > 0) {
                pdfTextStripper.setEndPage(endPage[0]);
            } else {
                pdfTextStripper.setEndPage(startPage);
            }
            extractText = pdfTextStripper.getText(doc);
        } catch (IOException exception) {
            logger.error(exception);
            extractText = ERROR_EXTRACT_MSG;
        }
        return extractText.split(token);
    }

    public static String getPDFText(PDDocument doc, int pageNum, Rectangle rectangle) {
        String extractText = null;
        try {
            PDPage page = doc.getPage(pageNum - 1);
            PDFTextStripperByArea pdfStripper = new PDFTextStripperByArea();
            pdfStripper.setSortByPosition(true);
            pdfStripper.addRegion("area", rectangle);
            pdfStripper.extractRegions(page);
            extractText = pdfStripper.getTextForRegion("area");
        } catch (IOException exception) {
            logger.error(exception);
            extractText = ERROR_EXTRACT_MSG;
        }
        return extractText;
    }

    public static void closePDFDoc(PDDocument doc) {
        try {
            doc.close();
        } catch (IOException exception) {
            logger.error(exception);
        }
    }

    public static List<File> takeScreenshotOfFullDoc(PDDocument document, String filepath) {
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        File file = null;
        List<File> listOfScreenShorts = new ArrayList<>();

        for (int pageNum = 0; pageNum < document.getNumberOfPages(); pageNum++) {
            try {
                file = new File(String.format(FILENAME_PATTERN, filepath, StringHelper.removeSpecialChars(getPDFFileName(document)), pageNum + 1, ".png"));
                file.getParentFile().mkdirs();
                ImageIO.write(pdfRenderer.renderImage(pageNum), "PNG", file);
                listOfScreenShorts.add(file);
            } catch (IOException exception) {
                logger.error(exception);
            }

        }
        return listOfScreenShorts;

    }

    public static File takeScreenshotOfPage(PDDocument document, String filepath, int pageNum) {
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        File file = null;
        pageNum = pageNum > 0 ? pageNum : 1;
        try {
            file = new File(String.format(FILENAME_PATTERN, filepath, StringHelper.removeSpecialChars(getPDFFileName(document)), pageNum + 1, ".png"));
            file.getParentFile().mkdir();
            ImageIO.write(pdfRenderer.renderImage(pageNum), "PNG", file);
        } catch (IOException exception) {
            logger.error(exception);
        }

        return file;

    }

    private static String getPDFFileName(PDDocument document) {
        String title = document.getDocumentInformation().getTitle();
        if (title == null || title.isEmpty()) {
            title = "PDF";
        }
        return String.format("%s%s", title, UUID.randomUUID());
    }

    public static int getPDFPageNumber(PDDocument document, String valueToFind) {
        String extractText = null;
        int pageNum = 0;
        for (int i = 0; i < document.getNumberOfPages(); i++) {
            try {
                PDFTextStripper pdfTextStripper = new PDFTextStripper();
                pdfTextStripper.setStartPage(i);
                pdfTextStripper.setEndPage(i + 1);
                extractText = pdfTextStripper.getText(document);
                if (Boolean.parseBoolean(System.getProperty("fw.ignoreWhiteSpaceOnPDFCompare"))) {
                    extractText = String.join(" ", Arrays.asList(extractText.split("\\s+")));
                }
                if (extractText.contains(valueToFind.trim())) {
                    pageNum = i + 1;
                    break;
                }
            } catch (IOException ioException) {
                logger.error(ioException);
            }
        }
        return pageNum;
    }

    public static int getPageNumberInBetweenDoc(PDDocument document, String valueToFind, int pageStart, int pageEnd) {
        String extractText = null;
        int pageNum = 0;
        for (int i = pageStart; i < pageEnd; i++) {
            try {
                PDFTextStripper pdfTextStripper = new PDFTextStripper();
                pdfTextStripper.setStartPage(i - 1);
                pdfTextStripper.setEndPage(i);
                extractText = pdfTextStripper.getText(document);
                if (Boolean.parseBoolean(System.getProperty("fw.ignoreWhiteSpaceOnPDFCompare"))) {
                    extractText = String.join(" ", Arrays.asList(extractText.split("\\s+")));
                }
                if (extractText.contains(valueToFind.trim())) {
                    pageNum = i;
                    break;
                }
            } catch (IOException ioException) {
                logger.error(ioException);
            }
        }
        return pageNum;
    }
}
