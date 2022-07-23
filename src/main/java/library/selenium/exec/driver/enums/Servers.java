package library.selenium.exec.driver.enums;

import java.util.Arrays;
import java.util.Optional;

public enum Servers {
    LOCAL("local"),
    SELENIUMGRID("selenium-grid"),
    SAUCELABS("saucelabs"),
    BROWSERSTACK("browserstack"),
    APPIUM("appium"),
    PERFECTO("perfecto"),
    HTMLUNIT("htmlunit");

    private final String serverName;

    Servers(String serverName) {
        this.serverName = serverName;
    }

    public String getServerName() {
        return serverName;
    }

    public static Servers get(String serverName) {
        Optional<Servers> first = Arrays.stream(Servers.values())
                .filter(server -> server.getServerName()
                        .equalsIgnoreCase(serverName))
                .findFirst();

        return first.orElse(LOCAL);
    }

}
