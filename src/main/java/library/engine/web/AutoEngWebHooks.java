package library.engine.web;

import io.cucumber.core.api.Scenario;
import io.cucumber.java8.En;
import library.common.TestContext;
import library.engine.core.objectmatcher.fetch.FetchFlatFileObjects;
import library.engine.core.objectmatcher.fetch.FetchPageObjects;

public class AutoEngWebHooks implements En {
    public AutoEngWebHooks() {
        Before(35, (Scenario scenario) -> {
            TestContext.getInstance().setOfPO().addAll(FetchPageObjects.populateListOfPO());
        });
        After(40, (Scenario scenario) -> {
        });
    }
}
