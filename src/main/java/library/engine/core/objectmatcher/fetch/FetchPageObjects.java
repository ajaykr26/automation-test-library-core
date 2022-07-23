package library.engine.core.objectmatcher.fetch;

import library.common.Constants;
import library.engine.core.AutoEngCoreConstants;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FetchPageObjects {

    private FetchPageObjects() {
    }

    private static final Logger logger = LogManager.getLogger(FetchFlatFileObjects.class.getName());

    public static Set<Class<?>> populateListOfPO() {
        Set<Class<?>> pageClassSet = new HashSet<>();
        File directory = null;
        directory = new File(Constants.PAGE_OBJECT_JAR_PATH);
        if (!directory.exists()) {
            File folder = new File(Constants.PAGE_OBJECT_EXTERNAL_JAR_PATH);
            if (folder.exists()) {
                File[] files = folder.listFiles();
                for (File file : files) {
                    getPOClassName(pageClassSet, file);
                }
            }
            return pageClassSet;
        }
        List<String> javaClasses = findClassesInPath(Constants.PAGE_OBJECT_JAR_PATH);
        List<PageObject> packageDetail = findJavaFiles(Constants.PAGE_OBJECT_JAVA_PATH);

        packageDetail.stream().filter(a -> javaClasses.contains(a.getClassName())).collect(Collectors.toList()).stream().
                forEach(pageObject -> {
                    try {
                        pageClassSet.add(Class.forName(pageObject.getPackageName().substring(8, pageObject.getPackageName().indexOf(';')) + "." + pageObject.getClassName()));
                    } catch (ClassNotFoundException exception) {

                    }
                });
        return pageClassSet;
    }

    private static void getPOClassName(Set<Class<?>> pageClassSet, File file) {
        try (JarInputStream pageObjectJarFile = new JarInputStream(new FileInputStream(file))) {
            JarEntry pageObj = pageObjectJarFile.getNextJarEntry();
            while (pageObj != null) {
                if (pageObj.getName().endsWith(".class")) {
                    String className = pageObj.getName().replace("/", ".");
                    if (className.startsWith("pageobjects")) {
                        String myClass = className.substring(0, className.lastIndexOf('.'));
                        pageClassSet.add(Class.forName(myClass));
                    }
                }
                pageObj = pageObjectJarFile.getNextJarEntry();
            }
        } catch (IOException | ClassNotFoundException exception) {
            logger.error(exception.getMessage(), exception);
        }
    }

    private static List<String> findClassesInPath(String directory) {
        Path filepath = Paths.get(directory);
        final List<String> fileList = new ArrayList<>();

        try (Stream<Path> files = Files.find(filepath, 5, (path, attributes) -> {
            File file = path.toFile();
            return !file.isDirectory() && file.getName().endsWith(".class");
        })) {

            files.forEach(file -> fileList.add(getFileNameWithoutExtension(file)));
            if (fileList.isEmpty()) {
                logger.error("no java file foun in the path \"{}\"", directory);
            } else {
                return fileList;
            }
        } catch (IOException ioException) {
            logger.error(ioException.getMessage(), ioException);
        }
        return fileList;
    }

    private static List<PageObject> findJavaFiles(String directory) {
        Path filepath = Paths.get(directory);
        final List<PageObject> pageObjectList = new ArrayList<>();

        try (Stream<Path> files = Files.find(filepath, 5, (path, attributes) -> {
            File file = path.toFile();
            return !file.isDirectory() && file.getName().endsWith(".java");
        })) {

            files.forEach(file -> {
                try {
                    pageObjectList.add(new PageObject(getFileNameWithoutExtension(file), getPackageName(file)));
                } catch (IOException ioException) {
                    logger.error(ioException);
                }
            });
            if (pageObjectList.isEmpty()) {
                logger.error("no java file found in the path \"{}\"", directory);
            } else {
                return pageObjectList;
            }
        } catch (IOException ioException) {
            logger.error(ioException.getMessage(), ioException);
        }
        return pageObjectList;
    }

    private static String getPackageName(Path path) throws IOException {
        try {
            return Files.readAllLines(path).get(0);
        } catch (MalformedInputException exception) {
            return Files.readAllLines(path, StandardCharsets.ISO_8859_1).get(0);
        }
    }

    private static String getFileNameWithoutExtension(Path path) {
        String filename = path.getFileName().toString();
        filename = FilenameUtils.removeExtension(filename);
        return filename;
    }

    static class PageObject {
        String className;
        String packageName;

        PageObject(String className, String packageName) {
            this.className = className;
            this.packageName = packageName;
        }

        public String getClassName() {
            return className;
        }

        public String getPackageName() {
            return packageName;
        }
    }

}
