package library.engine.pdf;

import io.cucumber.core.api.Scenario;
import io.cucumber.java8.En;

public class AutoEngPDFHooks implements En {
    public AutoEngPDFHooks() {
        Before(35, (Scenario scenario) -> {
        });
        After(30, (Scenario scenario) -> {
        });
    }
}
