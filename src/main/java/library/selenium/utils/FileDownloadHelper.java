package library.selenium.utils;

import library.common.Constants;
import library.common.Property;
import library.selenium.core.Element;
import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import javax.net.ssl.HostnameVerifier;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.UUID;

public class FileDownloadHelper {
    private WebDriver driver;
    private URI fileURI;
    private boolean mimicWebDriverCookieState = true;
    private boolean followRedirects = true;
    protected final Logger logger = LogManager.getLogger(FileDownloadHelper.class);

    public FileDownloadHelper(WebDriver driver) {
        this.driver = driver;
    }

    public void setURISpecifiedInAnchorElement(Element element) throws URISyntaxException {
        if (element.element().getTagName().equals("a")) {
            fileURI = new URI(element.getAttribute("href"));
        } else {

            throw new IllegalArgumentException("you have not specified anchor element");
        }
    }

    public File downloadFile(String extension) {
        if (fileURI == null) throw new IllegalArgumentException("no file URI specified");
        File downloadedFile = new File(getFileName(extension));
        OkHttpClient client = getOkHttpClient();
        Request request = getRequest();

        try (Response response = client.newCall(request).execute()){

            FileUtils.copyInputStreamToFile(response.body().byteStream(), downloadedFile);
            return downloadedFile;
        } catch (IOException | NullPointerException exception) {
            logger.error("", exception.getMessage());
            return null;
        }

    }

    private Request getRequest() {
        String urlToUse = null;
        try {
            urlToUse = fileURI.toURL().toString();
        } catch (MalformedURLException exception) {
            logger.warn("");
            urlToUse = fileURI.toString();
        }
        return new Request.Builder()
                .url(urlToUse)
                .build();
    }

    private OkHttpClient getOkHttpClient() {
        CookieJar cookieJar = null;
        if (mimicWebDriverCookieState) {
            cookieJar = new FileDownloadCookieJar(driver.manage().getCookies());

        } else {
            cookieJar = new FileDownloadCookieJar();
        }
        return new OkHttpClient.Builder().cookieJar(cookieJar)
                .hostnameVerifier(fileDownloadVerifier())
                .followRedirects(followRedirects)
                .build();
    }

    private HostnameVerifier fileDownloadVerifier() {
        return (hostname, session) -> {
            if (hostname.equalsIgnoreCase(session.getPeerHost())) {
                return true;
            } else {
                if (Boolean.parseBoolean(Property.getVariable("fw.ignoreHostNameMismatch"))) {
                    return false;
                } else {
                    logger.warn("");
                    return true;
                }
            }
        };
    }

    private String getFileName(String extension) {
        String filename;
        if (FilenameUtils.getExtension(fileURI.toString()).isEmpty()) {
            if (FilenameUtils.indexOfExtension(extension) == -1) {
                extension = "." + extension;
            }
            filename = String.format("%s%s", UUID.randomUUID(), extension);
        } else {
            filename = FilenameUtils.getName(fileURI.toString());
        }
        return Paths.get(Constants.DOWNLOAD_PATH + filename).toAbsolutePath().toString();
    }
}
