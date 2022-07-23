package library.engine.core;

import io.cucumber.core.api.Scenario;
import io.cucumber.java8.En;

public class AutoEngCoreHooks implements En {
    public AutoEngCoreHooks() {
        Before(15, (Scenario scenario) -> {
        });
        After(15, (Scenario scenario) -> {
        });
    }
}
