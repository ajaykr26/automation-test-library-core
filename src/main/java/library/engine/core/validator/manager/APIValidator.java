package library.engine.core.validator.manager;

import library.engine.core.validator.FailureFlag;
import library.engine.core.validator.ValidatorManager;

public class APIValidator extends ValidatorManager {
    private FailureFlag failureFlag;

    public APIValidator(String failureFlag) {
        this.failureFlag = FailureFlag.valueOfLabel(failureFlag);
    }

    @Override
    protected void assertEqualTo(Object actual, Object expected, String assertMsg) {

    }

    @Override
    protected void assertEqualsToIgnoreCase(Object actual, Object expected, String assertMsg) {

    }

    @Override
    protected void assertNotEqualsTo(Object actual, Object expected, String assertMsg) {

    }

    @Override
    protected void assertLessThan(Object actual, Object expected, String assertMsg) {

    }

    @Override
    protected void assertLessThanOrEqualTo(Object actual, Object expected, String assertMsg) {

    }

    @Override
    protected void assertGreaterThan(Object actual, Object expected, String assertMsg) {

    }

    @Override
    protected void assertGreaterThanOrEqualTo(Object actual, Object expected, String assertMsg) {

    }

    @Override
    protected void assertContains(Object actual, Object expected, String assertMsg) {

    }

    @Override
    protected void assertNotContains(Object actual, Object expected, String assertMsg) {

    }
}