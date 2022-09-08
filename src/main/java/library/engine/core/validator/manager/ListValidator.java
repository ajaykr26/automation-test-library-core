package library.engine.core.validator.manager;

import library.common.TestContext;
import library.engine.core.validator.FailureFlag;
import library.engine.core.validator.ValidatorManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ListValidator extends ValidatorManager {
    private final FailureFlag failureFlag;

    public ListValidator(String failureFlag) {
        this.failureFlag = FailureFlag.valueOfLabel(failureFlag);
    }

    @Override
    protected void assertEqualTo(Object actual, Object expected, String assertMsg) {
        if (failureFlag == FailureFlag.HARD_STOP_ON_FAILURE) {
            assertThat((List) actual).as(assertMsg).hasSameElementsAs((List) expected);
        } else {
            TestContext.getInstance().softAssertions().assertThat((List) actual).as(assertMsg).hasSameElementsAs((List) expected);
        }
    }

    @Override
    protected void assertEqualsToIgnoreCase(Object actual, Object expected, String assertMsg) {
        throw new UnsupportedOperationException("assertEqualsToIgnoreCase not supported for Compare_List");

    }

    @Override
    protected void assertNotEqualsTo(Object actual, Object expected, String assertMsg) {
        if (failureFlag == FailureFlag.HARD_STOP_ON_FAILURE) {
            assertThat((List) actual).as(assertMsg).isNotSameAs(expected);
        } else {
            TestContext.getInstance().softAssertions().assertThat((List) actual).as(assertMsg).isNotSameAs(expected);
        }
    }

    @Override
    protected void assertLessThan(Object actual, Object expected, String assertMsg) {
        throw new UnsupportedOperationException("assertLessThan not supported for Compare_List");

    }

    @Override
    protected void assertLessThanOrEqualTo(Object actual, Object expected, String assertMsg) {
        throw new UnsupportedOperationException("assertLessThanOrEqualTo not supported for Compare_List");

    }

    @Override
    protected void assertGreaterThan(Object actual, Object expected, String assertMsg) {
        throw new UnsupportedOperationException("assertGreaterThan not supported for Compare_List");

    }

    @Override
    protected void assertGreaterThanOrEqualTo(Object actual, Object expected, String assertMsg) {
        throw new UnsupportedOperationException("assertGreaterThanOrEqualTo not supported for Compare_List");
    }

    @Override
    protected void assertContains(Object actual, Object expected, String assertMsg) {
        if (failureFlag == FailureFlag.HARD_STOP_ON_FAILURE) {
            assertThat((List) actual).as(assertMsg).containsAnyElementsOf((List) expected);
        } else {
            TestContext.getInstance().softAssertions().assertThat((List) actual).as(assertMsg).containsAnyElementsOf((List) expected);
        }
    }

    @Override
    protected void assertNotContains(Object actual, Object expected, String assertMsg) {
        if (failureFlag == FailureFlag.HARD_STOP_ON_FAILURE) {
            assertThat((List) actual).as(assertMsg).doesNotContainAnyElementsOf((List) expected);
        } else {
            TestContext.getInstance().softAssertions().assertThat((List) actual).as(assertMsg).doesNotContainAnyElementsOf((List) expected);
        }
    }
}