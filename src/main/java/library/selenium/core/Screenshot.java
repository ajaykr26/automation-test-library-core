package library.selenium.core;

import library.common.Constants;
import library.common.Property;
import library.common.TestContext;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Screenshot {

    private Screenshot() {
    }

    private static final Logger logger = LogManager.getLogger(Screenshot.class.getName());

    public static File grabScreenshot(WebDriver driver) {
        String screenshotType = System.getProperty("fw.scrollingScreenshot");

        try {
            if (driver != null) {
                if (driver.getWindowHandle() != null) {
                    return screenshotType.equalsIgnoreCase("true") ? grabScrollingScreenshot(driver) : grabDisplayAreScreenshot(driver);
                } else {
                    logger.warn("driver in not launched, skipping the screenshot");
                }
            }

        } catch (NoSuchWindowException exception) {
            logger.warn("skipping screenshot, Error: {}", exception.getMessage());
        }
        return null;
    }

    public static File grabDisplayAreScreenshot(WebDriver driver) {
        try {
            Thread.sleep(Integer.parseInt(System.getProperty("fw.screenshotDelay")));
        } catch (InterruptedException | NumberFormatException exception) {
            logger.error(exception.getMessage());
            Thread.currentThread().interrupt();
        }
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
    }

    public static File grabScrollingScreenshot(WebDriver driver) {
        try {

            Thread.sleep(Integer.parseInt(System.getProperty("fw.screenshotDelay")));
        } catch (InterruptedException | NumberFormatException exception) {
            logger.error(exception.getMessage());
            Thread.currentThread().interrupt();
        }

        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
    }

    public static File saveScreenshot(File screenshot, String filepath) {
        UUID uuid = UUID.randomUUID();
        File file = new File(filepath + uuid + ".png");
        if (screenshot != null) {
            try {
                FileUtils.moveFile(screenshot, file);
            } catch (Exception exception) {
                logger.error(exception.getMessage());
            }
            return file;
        }
        return null;
    }

    public static boolean compareScreenshot(File fileExpected, File fileActual) throws IOException {
        boolean matchFlag = true;
        try {
            BufferedImage bufferedImageActual = ImageIO.read(fileActual);
            BufferedImage bufferedImageExpected = ImageIO.read(fileExpected);
            DataBuffer dataBufferFileActual = bufferedImageActual.getData().getDataBuffer();
            DataBuffer dataBufferFileExpected = bufferedImageExpected.getData().getDataBuffer();

            int sizeFileActual = dataBufferFileActual.getSize();
            for (int i = 0; i < sizeFileActual; i++) {
                if (dataBufferFileActual.getElem(i) != dataBufferFileExpected.getElem(i)) {
                    matchFlag = false;
                    break;
                }
            }
        } catch (FileNotFoundException exception) {
            logger.error(exception.getMessage());
        }
        return matchFlag;
    }

    public static void getImageFromUrl(String imageUrl, String destinationFile, String filename) throws IOException {
        URL url = new URL(imageUrl);
        InputStream inputStream = url.openStream();
        File file = new File(destinationFile + filename);
        if (!new File(destinationFile).exists()) new File(destinationFile).mkdir();
        if (file.exists()) file.delete();
        Files.copy(inputStream, file.toPath());
        inputStream.close();
    }

    public static void insertImageToWord(String scenario, Boolean flag) throws IOException, InvalidFormatException {
        if (flag) {
            String directoryPath = Constants.SCREENSHOT_PATH + scenario;
            final Stream<Path> fileStream = Files.list(Paths.get(directoryPath));
            List<File> files = fileStream
                    .map(Path::toFile)
                    .sorted(Comparator.comparing(File::lastModified))
                    .collect(Collectors.toList());
            XWPFDocument doc = new XWPFDocument();
            XWPFParagraph p = doc.createParagraph();
            XWPFRun xwpfRun = p.createRun();

            for (File file : files) {
                int format = XWPFDocument.PICTURE_TYPE_PNG;
                p.setAlignment(ParagraphAlignment.CENTER);
                xwpfRun.addBreak();
                xwpfRun.addPicture(new FileInputStream(file), format, file.getName(), Units.toEMU(480), Units.toEMU(240));
            }
            FileOutputStream out = new FileOutputStream(directoryPath + File.separator + scenario + ".doc");
            doc.write(out);
            out.close();
        }
    }

}
