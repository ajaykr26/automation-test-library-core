package library.engine.core.validator;


public abstract class ValidatorManager {

    protected abstract void assertEqualTo(Object actual, Object expected, String assertMsg);

    protected abstract void assertEqualsToIgnoreCase(Object actual, Object expected, String assertMsg);

    protected abstract void assertNotEqualsTo(Object actual, Object expected, String assertMsg);

    protected abstract void assertLessThan(Object actual, Object expected, String assertMsg);

    protected abstract void assertLessThanOrEqualTo(Object actual, Object expected, String assertMsg);

    protected abstract void assertGreaterThan(Object actual, Object expected, String assertMsg);

    protected abstract void assertGreaterThanOrEqualTo(Object actual, Object expected, String assertMsg);

    protected abstract void assertContains(Object actual, Object expected, String assertMsg);

    protected abstract void assertNotContains(Object actual, Object expected, String assertMsg);

}
