package library.engine.mobile;

import io.cucumber.core.api.Scenario;
import io.cucumber.java8.En;
import library.selenium.exec.driver.factory.DriverContext;

public class AutoEngMobileHooks implements En {

    public AutoEngMobileHooks() {
        Before(35, (Scenario scenario) -> {
        });
        After(40, (Scenario scenario) -> {
            if (DriverContext.getInstance().getTechStack() != null) {
                DriverContext.getInstance().quit();
            }
        });
    }


}
