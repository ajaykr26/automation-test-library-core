package library.selenium.exec.driver.enums;

import java.util.Arrays;
import java.util.Optional;

public enum Platforms {
    IOS("ios"),
    ANDROID("android");

    private String plateformType;

    Platforms(String plateformType) {
        this.plateformType = plateformType;
    }

    public String getPlateformType() {
        return plateformType;
    }

    public static Platforms get(String plateformType) {
        Optional<Platforms> first = Arrays.stream(Platforms.values())
                .filter(browser -> browser.getPlateformType()
                        .equalsIgnoreCase(plateformType))
                .findFirst();

        return first.orElse(null);
    }

}
