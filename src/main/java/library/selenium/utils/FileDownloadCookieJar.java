package library.selenium.utils;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class FileDownloadCookieJar implements CookieJar {

    private final Set<Cookie> cookies = new HashSet<>();

    public FileDownloadCookieJar() {

    }

    public FileDownloadCookieJar(Set<org.openqa.selenium.Cookie> seleniumCookieSet) {
        for (org.openqa.selenium.Cookie seleniumCookie : seleniumCookieSet) {
            cookies.add(new Cookie.Builder().name(seleniumCookie.getName())
                    .value(seleniumCookie.getValue())
                    .domain(removeLeadingDot(seleniumCookie.getDomain()))
                    .build());
        }
    }

    private String removeLeadingDot(String domain) {
        if (domain.startsWith(".")) {
            return domain.substring(1);
        } else
            return domain;
    }

    @Override
    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
        cookies.addAll(list);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl httpUrl) {

        List<Cookie> validCookie = new ArrayList<>();
        for (Cookie cookie : cookies) {
            if (cookie.expiresAt() < System.currentTimeMillis()) {

            } else {
                validCookie.add(cookie);
            }
        }
        return validCookie;
    }
}
