package library.common;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileHelper {
    private static final int MAX_DEPTH = 10;

    private FileHelper() {

    }

    private static final Logger logger = LogManager.getLogger(FileHelper.class);

    public static void copyDir(String src, String destination, boolean overwrite) {
        try {
            Files.walk(Paths.get(src)).forEach(a -> {
                Path b = Paths.get(destination, a.toString().substring(src.length()));
                try {
                    if (!a.toString().equals(src))
                        Files.copy(a, b, overwrite ? new CopyOption[]{StandardCopyOption.REPLACE_EXISTING} : new CopyOption[]{});
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createDirectoryPath(String path) {
        File testDirectory = new File(path);
        if (!testDirectory.exists()) {
            testDirectory.mkdirs();
        }

    }

    public static String findFileInPath(String rootDir, String filename) {
        Path filePath = Paths.get(rootDir);
        String fullPath = null;
        String fileNameWithTrailingWhiteSpace = allowWhiteSpaceInFileName(filename);

        try (Stream<Path> files = Files.find(filePath, MAX_DEPTH,
                (path, attribute) -> {
                    File file = path.toFile();
                    return !file.isDirectory() && file.getName().matches(fileNameWithTrailingWhiteSpace);
                })) {
            final List<Path> filesList = files.collect(Collectors.toList());

            if (!filesList.isEmpty()) {
                fullPath = filesList.get(0).toString();
                if (filesList.size() > 1) {
                    logger.warn("found more than one file match. returning first match");
                }
            }
        } catch (IOException ioException) {
            return null;
        }
        return fullPath;
    }

    public static String allowWhiteSpaceInFileName(String fileName) {
        return fileName;
    }

    public static String getFileAsString(String jsFilePath, String delimiter) {
        String stringToReturn = null;
        try (BufferedReader br = Files.newBufferedReader(Paths.get(jsFilePath), StandardCharsets.ISO_8859_1)) {
            stringToReturn = br.lines().collect(Collectors.joining(delimiter));
        } catch (IOException | NullPointerException exception) {
            logger.error(exception.getMessage());
        }
        return stringToReturn;
    }

    public static void loadDataParameterFromPropsFile(String propsFilePath, String paramPrefix) {
        PropertiesConfiguration props = new PropertiesConfiguration();
        Iterator<String> iterator = props.getKeys();
        while (iterator.hasNext()) {
            String param = iterator.next();
            if (param != null && param.matches("^" + Pattern.quote(paramPrefix) + "\\.\\w+$")) {
                TestContext.getInstance().testdataPut(param.split("\\.")[1], Property.getProperty(propsFilePath, param));
            }
        }
    }

    public static Path writeTempFile(String prefix, String extension, String fileContent) {
        try {
            if (!extension.contains(".")) {
                extension = "." + extension;
            }
            Path tempFile = Files.createTempFile(prefix, extension);
            tempFile.toFile().deleteOnExit();

            Files.write(tempFile, fileContent.getBytes(StandardCharsets.UTF_8));
            return tempFile;
        } catch (IOException exception) {
            logger.error(exception);
            return null;
        }
    }

    public static String getFileNameExtension(Path path) {
        String filename = path.getFileName().toString();
        filename = FilenameUtils.getExtension(filename);
        return filename;
    }

    public static String getFileNameWithoutExtension(Path path) {
        String filename = path.getFileName().toString();
        filename = FilenameUtils.removeExtension(filename);
        return filename;
    }
}

