package library.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipHelper {

    public static void zipAndCopyDirectory(String srcDirPath, String targetFilePath) throws IOException {
        Path targetPath = Files.createFile(Paths.get(targetFilePath));
        Path srcPath = Paths.get(srcDirPath);
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(targetPath));
             Stream<Path> pathStream = Files.walk(srcPath)) {
            pathStream.filter(path -> !Files.isDirectory(path)).forEach(path -> {
                ZipEntry zipEntry = new ZipEntry(srcPath.relativize(path).toString());
                try {
                    zs.putNextEntry(zipEntry);
                    Files.copy(path, zs);
                    zs.close();
                } catch (IOException ioException) {
                    System.err.println(ioException.getMessage());
                }
            });

        }
    }
}
