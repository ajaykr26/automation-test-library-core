package library.engine.core.validator;

public enum ComparisonOperator {
    EQ("Equal To"),
    EQIC("Equal To Ignore Case"),
    GTE("Greater Than or Equal To"),
    GT("Greater Than"),
    LTE("Less Than or Equal To"),
    LT("Less Than"),
    NE("Not Equal To"),
    CONTAINS("Contains"),
    NOT_CONTAINS("Does Not Contains");

    public final String label;

    ComparisonOperator(String label) {
        this.label = label;
    }

    public static ComparisonOperator valueOfLabel(String label) {
        for (ComparisonOperator comparisonOperator : values()) {
            if (comparisonOperator.label.equalsIgnoreCase(label)) {
                return comparisonOperator;
            }
        }
        throw new IllegalArgumentException(String.format("Comparison type of '%s' not supported", label));
    }
}
