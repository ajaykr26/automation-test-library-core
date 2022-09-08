package library.engine.core.validator.manager;

import library.common.TestContext;
import library.engine.core.validator.FailureFlag;
import library.engine.core.validator.ValidatorManager;

import static org.assertj.core.api.Assertions.assertThat;

public class StringValidator extends ValidatorManager {
    private final FailureFlag failureFlag;

    public StringValidator(String failureFlag) {
        this.failureFlag = FailureFlag.valueOfLabel(failureFlag);
    }

    @Override
    protected void assertEqualTo(Object actual, Object expected, String assertMsg) {
        if (failureFlag == FailureFlag.HARD_STOP_ON_FAILURE) {
            assertThat(actual.toString()).as(assertMsg).isEqualTo(expected.toString());
        } else {
            TestContext.getInstance().softAssertions().assertThat(actual.toString()).as(assertMsg).isEqualTo(expected.toString());
        }
    }

    @Override
    protected void assertEqualsToIgnoreCase(Object actual, Object expected, String assertMsg) {
        if (failureFlag == FailureFlag.HARD_STOP_ON_FAILURE) {
            assertThat(actual.toString()).as(assertMsg).isEqualToIgnoringCase(expected.toString());
        } else {
            TestContext.getInstance().softAssertions().assertThat(actual.toString()).as(assertMsg).isEqualToIgnoringCase(expected.toString());
        }
    }

    @Override
    protected void assertNotEqualsTo(Object actual, Object expected, String assertMsg) {
        if (failureFlag == FailureFlag.HARD_STOP_ON_FAILURE) {
            assertThat(actual.toString()).as(assertMsg).isNotEqualTo(expected.toString());
        } else {
            TestContext.getInstance().softAssertions().assertThat(actual.toString()).as(assertMsg).isNotEqualTo(expected.toString());
        }
    }

    @Override
    protected void assertLessThan(Object actual, Object expected, String assertMsg) {
        if (failureFlag == FailureFlag.HARD_STOP_ON_FAILURE) {
            assertThat(actual.toString()).as(assertMsg).isLessThan(expected.toString());
        } else {
            TestContext.getInstance().softAssertions().assertThat(actual.toString()).as(assertMsg).isLessThan(expected.toString());
        }
    }

    @Override
    protected void assertLessThanOrEqualTo(Object actual, Object expected, String assertMsg) {
        if (failureFlag == FailureFlag.HARD_STOP_ON_FAILURE) {
            assertThat(actual.toString()).as(assertMsg).isLessThanOrEqualTo(expected.toString());
        } else {
            TestContext.getInstance().softAssertions().assertThat(actual.toString()).as(assertMsg).isLessThanOrEqualTo(expected.toString());
        }
    }

    @Override
    protected void assertGreaterThan(Object actual, Object expected, String assertMsg) {
        if (failureFlag == FailureFlag.HARD_STOP_ON_FAILURE) {
            assertThat(actual.toString()).as(assertMsg).isGreaterThan(expected.toString());
        } else {
            TestContext.getInstance().softAssertions().assertThat(actual.toString()).as(assertMsg).isGreaterThan(expected.toString());
        }
    }

    @Override
    protected void assertGreaterThanOrEqualTo(Object actual, Object expected, String assertMsg) {
        if (failureFlag == FailureFlag.HARD_STOP_ON_FAILURE) {
            assertThat(actual.toString()).as(assertMsg).isGreaterThanOrEqualTo(expected.toString());
        } else {
            TestContext.getInstance().softAssertions().assertThat(actual.toString()).as(assertMsg).isGreaterThanOrEqualTo(expected.toString());
        }
    }

    @Override
    protected void assertContains(Object actual, Object expected, String assertMsg) {
        if (failureFlag == FailureFlag.HARD_STOP_ON_FAILURE) {
            assertThat(actual.toString()).as(assertMsg).contains(expected.toString());
        } else {
            TestContext.getInstance().softAssertions().assertThat(actual.toString()).as(assertMsg).contains(expected.toString());
        }
    }

    @Override
    protected void assertNotContains(Object actual, Object expected, String assertMsg) {
        if (failureFlag == FailureFlag.HARD_STOP_ON_FAILURE) {
            assertThat(actual.toString()).as(assertMsg).doesNotContain(expected.toString());
        } else {
            TestContext.getInstance().softAssertions().assertThat(actual.toString()).as(assertMsg).doesNotContain(expected.toString());
        }
    }
}
