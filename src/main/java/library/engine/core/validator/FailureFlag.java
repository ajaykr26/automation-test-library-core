package library.engine.core.validator;

public enum FailureFlag {
    HARD_STOP_ON_FAILURE("HardStopOnFailure"),
    CONTINUE_ON_FAILURE("ContinueOnFailure");

    public final String label;

    FailureFlag(String label) {
        this.label = label;
    }

    public static FailureFlag valueOfLabel(String label) {
        for (FailureFlag failureFlag : values()) {
            if (failureFlag.label.equalsIgnoreCase(label)) {
                return failureFlag;
            }
        }
        throw new IllegalArgumentException(String.format("failure flag '%s' not supported", label));
    }
}
