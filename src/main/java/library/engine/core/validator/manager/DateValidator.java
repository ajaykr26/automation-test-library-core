package library.engine.core.validator.manager;

import library.common.TestContext;
import library.engine.core.validator.FailureFlag;
import library.engine.core.validator.ValidatorManager;

import static org.assertj.core.api.Assertions.assertThat;

public class DateValidator extends ValidatorManager {

    private FailureFlag failureFlag;

    public DateValidator(String failureFlag) {
        this.failureFlag = FailureFlag.valueOfLabel(failureFlag);
    }

    @Override
    protected void assertEqualTo(Object actual, Object expected, String assertMsg) {
        if (failureFlag == FailureFlag.HARD_STOP_ON_FAILURE) {
            assertThat(actual.toString()).as(assertMsg).isEqualTo(expected.toString());
        } else {
            TestContext.getInstance().softAssertions().assertThat(actual).as(assertMsg).isEqualTo(expected);
        }
    }

    @Override
    protected void assertEqualsToIgnoreCase(Object actual, Object expected, String assertMsg) {
        throw new UnsupportedOperationException("assertEqualsToIgnoreCase not supported for Compare_Date");

    }

    @Override
    protected void assertNotEqualsTo(Object actual, Object expected, String assertMsg) {
        if (failureFlag == FailureFlag.HARD_STOP_ON_FAILURE) {
            assertThat(actual.toString()).as(assertMsg).isNotEqualTo(expected.toString());
        } else {
            TestContext.getInstance().softAssertions().assertThat(actual).as(assertMsg).isNotEqualTo(expected);
        }
    }

    @Override
    protected void assertLessThan(Object actual, Object expected, String assertMsg) {
        if (failureFlag == FailureFlag.HARD_STOP_ON_FAILURE) {
            assertThat(actual.toString()).as(assertMsg).isEqualTo(expected.toString());
        } else {
            TestContext.getInstance().softAssertions().assertThat(actual).as(assertMsg).isEqualTo(expected);
        }
    }

    @Override
    protected void assertLessThanOrEqualTo(Object actual, Object expected, String assertMsg) {
        if (failureFlag == FailureFlag.HARD_STOP_ON_FAILURE) {
            assertThat(actual.toString()).as(assertMsg).isEqualTo(expected.toString());
        } else {
            TestContext.getInstance().softAssertions().assertThat(actual).as(assertMsg).isEqualTo(expected);
        }
    }

    @Override
    protected void assertGreaterThan(Object actual, Object expected, String assertMsg) {
        if (failureFlag == FailureFlag.HARD_STOP_ON_FAILURE) {
            assertThat(actual.toString()).as(assertMsg).isEqualTo(expected.toString());
        } else {
            TestContext.getInstance().softAssertions().assertThat(actual).as(assertMsg).isEqualTo(expected);
        }
    }

    @Override
    protected void assertGreaterThanOrEqualTo(Object actual, Object expected, String assertMsg) {
        if (failureFlag == FailureFlag.HARD_STOP_ON_FAILURE) {
            assertThat(actual.toString()).as(assertMsg).isEqualTo(expected.toString());
        } else {
            TestContext.getInstance().softAssertions().assertThat(actual).as(assertMsg).isEqualTo(expected);
        }
    }

    @Override
    protected void assertContains(Object actual, Object expected, String assertMsg) {
        if (failureFlag == FailureFlag.HARD_STOP_ON_FAILURE) {
            assertThat(actual.toString()).as(assertMsg).isEqualTo(expected.toString());
        } else {
            TestContext.getInstance().softAssertions().assertThat(actual).as(assertMsg).isEqualTo(expected);
        }
    }

    @Override
    protected void assertNotContains(Object actual, Object expected, String assertMsg) {
        if (failureFlag == FailureFlag.HARD_STOP_ON_FAILURE) {
            assertThat(actual.toString()).as(assertMsg).isEqualTo(expected.toString());
        } else {
            TestContext.getInstance().softAssertions().assertThat(actual).as(assertMsg).isEqualTo(expected);
        }
    }
}
