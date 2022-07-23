package library.engine.core.validator;

import library.common.TestContext;
import library.engine.core.validator.manager.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static library.engine.core.AutoEngCoreConstants.VALIDATION_TAG;
import static library.reporting.ReportFactory.getReporter;

public class AssertHelper {

    private final ThreadLocal<ValidatorManager> validatorManager = new InheritableThreadLocal<>();
    private final ThreadLocal<ComparisonType> comparisonType = new InheritableThreadLocal<>();
    private final ThreadLocal<ComparisonOperator> comparisonOperator = new InheritableThreadLocal<>();
    private final ThreadLocal<String> failureFlag = new InheritableThreadLocal<>();
    protected final Logger logger = LogManager.getLogger(AssertHelper.class);

    public AssertHelper(String comparisonType, String comparisonOperator, String failureFlag) {
        this.comparisonType.set(ComparisonType.valueOfLabel(comparisonType));
        this.comparisonOperator.set(ComparisonOperator.valueOfLabel(comparisonOperator));
        this.validatorManager.set(setValidator(failureFlag));
        this.failureFlag.set(failureFlag);
    }

    public AssertHelper(ComparisonType comparisonType, ComparisonOperator comparisonOperator, String failureFlag) {
        this.comparisonType.set((comparisonType));
        this.comparisonOperator.set((comparisonOperator));
        this.validatorManager.set(setValidator(failureFlag));
        this.failureFlag.set(failureFlag);
    }

    private ValidatorManager setValidator(String failureFlag) {
        switch (comparisonType.get()) {
            case COMPARE_DATE:
                return new DateValidator(failureFlag);
            case COMPARE_STRING:
                return new StringValidator(failureFlag);
            case COMPARE_NUMBER:
                return new NumberValidator(failureFlag);
            case COMPARE_LIST:
                return new ListValidator(failureFlag);
            case COMPARE_API_RESPONSE:
                return new APIValidator(failureFlag);
            default:
                throw new IllegalArgumentException(String.format("comparison type of '%s' not supported", comparisonType.get()));
        }
    }

    public void performValidation(Object actual, Object expected, String validationId, String assertMsg) {
        setExpectedAndActual(actual, expected, validationId);
        try {
            switch (comparisonOperator.get()) {
                case EQ:
                    this.validatorManager.get().assertEqualTo(actual, expected, assertMsg);
                    break;
                case EQIC:
                    this.validatorManager.get().assertEqualsToIgnoreCase(actual, expected, assertMsg);
                    break;
                case NE:
                    this.validatorManager.get().assertNotEqualsTo(actual, expected, assertMsg);
                    break;
                case GT:
                    this.validatorManager.get().assertGreaterThan(actual, expected, assertMsg);
                    break;
                case GTE:
                    this.validatorManager.get().assertGreaterThanOrEqualTo(actual, expected, assertMsg);
                    break;
                case LT:
                    this.validatorManager.get().assertLessThan(actual, expected, assertMsg);
                    break;
                case LTE:
                    this.validatorManager.get().assertLessThanOrEqualTo(actual, expected, assertMsg);
                    break;
                case CONTAINS:
                    this.validatorManager.get().assertContains(actual, expected, assertMsg);
                    break;
                case NOT_CONTAINS:
                    this.validatorManager.get().assertNotContains(actual, expected, assertMsg);
                    break;
            }
            if (!TestContext.getInstance().softAssertions().wasSuccess() && this.failureFlag.get().equalsIgnoreCase("ContinueOnFailure")) {
                getReporter().addStepLog("FAIL", getResultMessage(actual.toString(), expected.toString()));
            }
            cleanUpThreadLocal();
        } catch (AssertionError assertionError) {
            logger.error(assertionError.getMessage());
            throw assertionError;
        }

    }

    private void cleanUpThreadLocal() {
        validatorManager.remove();
        comparisonType.remove();
        comparisonOperator.remove();
        failureFlag.remove();
    }

    private String getResultMessage(String actual, String expected) {
        return String.format("%s -> expecting \"%s\" to be %s \"%s\".", comparisonType.get(), actual, comparisonOperator, expected);
    }

    private void setExpectedAndActual(Object actual, Object expected, String validationId) {
        String expectedResult = String.format("Expected value:  \"%s\"", getResultAsString(expected));
        String actualResult = String.format("Actual value:  \"%s\"", getResultAsString(actual));

        getReporter().addStepLog(expectedResult);
        getReporter().addStepLog(actualResult);
        logger.debug(expectedResult);
        logger.debug(actualResult);

        TestContext.getInstance().testdataPut(VALIDATION_TAG + validationId, getResultMessage(actual.toString(), expected.toString()));
    }

    private Object getResultAsString(Object result) {
        if (comparisonType.get() == ComparisonType.COMPARE_NUMBER) {
            return Double.parseDouble(result.toString());
        } else {
            return result;
        }
    }
}
