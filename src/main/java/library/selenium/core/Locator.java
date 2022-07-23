package library.selenium.core;

import java.util.Arrays;
import java.util.Optional;

public enum Locator {
    XPATH("xpath"),
    NAME("name"),
    ID("id"),
    TEXT("text"),
    CSS("cssSelector"),
    CLASS_NAME("className"),
    LINK_TEXT("linkText"),
    PARTIAL_LINK_TEXT("partialLinkText"),
    TAGE_NAME("tagName");

    private final String locatorName;

    Locator(String locatorName) {
        this.locatorName = locatorName;
    }

    public String getLocatorName() {
        return locatorName;
    }

    public static Locator getLocator(String locatorName) {
        Optional<Locator> first = Arrays.stream(Locator.values())
                .filter(browser -> browser.getLocatorName()
                        .equalsIgnoreCase(locatorName))
                .findFirst();

        return first.orElse(XPATH);
    }

}

