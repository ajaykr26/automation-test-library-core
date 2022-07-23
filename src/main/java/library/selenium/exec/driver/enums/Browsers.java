package library.selenium.exec.driver.enums;

import java.util.Arrays;
import java.util.Optional;

public enum Browsers {
    CHROME("chrome"),
    FIREFOX("firefox"),
    IEMODEEDGE("internet explorer"),
    EDGE("MicrosoftEdge"),
    HTMLUNIT("htmlunit"),
    PHANTOMJS("phantomjs");

    private final String browserName;

    Browsers(String browserName) {
        this.browserName = browserName;
    }

    public String getBrowserName() {
        return browserName;
    }

    public static Browsers get(String browserName) {
        Optional<Browsers> first = Arrays.stream(Browsers.values())
                .filter(browser -> browser.getBrowserName()
                        .equalsIgnoreCase(browserName))
                .findFirst();

        return first.orElse(CHROME);
    }

}
