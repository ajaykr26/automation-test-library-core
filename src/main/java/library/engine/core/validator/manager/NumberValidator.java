package library.engine.core.validator.manager;

import library.common.TestContext;
import library.engine.core.validator.FailureFlag;
import library.engine.core.validator.ValidatorManager;

import static org.assertj.core.api.Assertions.assertThat;

public class NumberValidator extends ValidatorManager {
    private FailureFlag failureFlag;

    public NumberValidator(String failureFlag) {
        this.failureFlag = FailureFlag.valueOfLabel(failureFlag);
    }

    @Override
    protected void assertEqualTo(Object actual, Object expected, String assertMsg) {
        if (failureFlag == FailureFlag.HARD_STOP_ON_FAILURE) {
            assertThat(Double.parseDouble(actual.toString())).as(assertMsg).isEqualTo(Double.parseDouble(expected.toString()));
        } else {
            TestContext.getInstance().softAssertions().assertThat(Double.parseDouble(actual.toString())).as(assertMsg).isEqualTo(Double.parseDouble(expected.toString()));
        }
    }

    @Override
    protected void assertEqualsToIgnoreCase(Object actual, Object expected, String assertMsg) {
        throw new UnsupportedOperationException("assertEqualsToIgnoreCase not supported for Compare_Number");

    }

    @Override
    protected void assertNotEqualsTo(Object actual, Object expected, String assertMsg) {
        if (failureFlag == FailureFlag.HARD_STOP_ON_FAILURE) {
            assertThat(Double.parseDouble(actual.toString())).as(assertMsg).isNotEqualTo(Double.parseDouble(expected.toString()));
        } else {
            TestContext.getInstance().softAssertions().assertThat(Double.parseDouble(actual.toString())).as(assertMsg).isNotEqualTo(Double.parseDouble(expected.toString()));
        }
    }

    @Override
    protected void assertLessThan(Object actual, Object expected, String assertMsg) {
        if (failureFlag == FailureFlag.HARD_STOP_ON_FAILURE) {
            assertThat(Double.parseDouble(actual.toString())).as(assertMsg).isLessThan(Double.parseDouble(expected.toString()));
        } else {
            TestContext.getInstance().softAssertions().assertThat(Double.parseDouble(actual.toString())).as(assertMsg).isLessThan(Double.parseDouble(expected.toString()));
        }
    }

    @Override
    protected void assertLessThanOrEqualTo(Object actual, Object expected, String assertMsg) {
        if (failureFlag == FailureFlag.HARD_STOP_ON_FAILURE) {
            assertThat(Double.parseDouble(actual.toString())).as(assertMsg).isLessThanOrEqualTo(Double.parseDouble(expected.toString()));
        } else {
            TestContext.getInstance().softAssertions().assertThat(Double.parseDouble(actual.toString())).as(assertMsg).isLessThanOrEqualTo(Double.parseDouble(expected.toString()));
        }
    }

    @Override
    protected void assertGreaterThan(Object actual, Object expected, String assertMsg) {
        if (failureFlag == FailureFlag.HARD_STOP_ON_FAILURE) {
            assertThat(Double.parseDouble(actual.toString())).as(assertMsg).isGreaterThan(Double.parseDouble(expected.toString()));
        } else {
            TestContext.getInstance().softAssertions().assertThat(Double.parseDouble(actual.toString())).as(assertMsg).isGreaterThan(Double.parseDouble(expected.toString()));
        }
    }

    @Override
    protected void assertGreaterThanOrEqualTo(Object actual, Object expected, String assertMsg) {
        if (failureFlag == FailureFlag.HARD_STOP_ON_FAILURE) {
            assertThat(Double.parseDouble(actual.toString())).as(assertMsg).isGreaterThanOrEqualTo(Double.parseDouble(expected.toString()));
        } else {
            TestContext.getInstance().softAssertions().assertThat(Double.parseDouble(actual.toString())).as(assertMsg).isGreaterThanOrEqualTo(Double.parseDouble(expected.toString()));
        }
    }

    @Override
    protected void assertContains(Object actual, Object expected, String assertMsg) {
        throw new UnsupportedOperationException("assertContains not supported for Compare_Number");

    }

    @Override
    protected void assertNotContains(Object actual, Object expected, String assertMsg) {
        throw new UnsupportedOperationException("assertNotContains not supported for Compare_Number");

    }
}