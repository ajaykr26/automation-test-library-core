package library.engine.api;

import io.cucumber.core.api.Scenario;
import io.cucumber.java8.En;
import library.common.TestContext;
import library.engine.core.objectmatcher.fetch.FetchFlatFileObjects;


public class AutoEngAPIHooks implements En {
    public AutoEngAPIHooks() {
        Before(35, (Scenario scenario) -> {
            TestContext.getInstance().setOfAPI().addAll(FetchFlatFileObjects.populateListOfAPIObjects());
        });

    }
}
