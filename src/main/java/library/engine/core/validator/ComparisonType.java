package library.engine.core.validator;

public enum ComparisonType {
    COMPARE_NUMBER("Compare_Number"),
    COMPARE_DATE("Compare_Date"),
    COMPARE_STRING("Compare_String"),
    COMPARE_LIST("Compare_List"),
    COMPARE_PARTIAL_TEXT("Compare_PartialText"),
    COMPARE_API_RESPONSE("Compare_API_Response"),
    COMPARE_IMAGE("Compare_Image");

    public final String label;

    ComparisonType(String label) {
        this.label = label;
    }

    public static ComparisonType valueOfLabel(String label) {
        for (ComparisonType ComparisonType : values()) {
            if (ComparisonType.label.equalsIgnoreCase(label)) {
                return ComparisonType;
            }
        }
        throw new IllegalArgumentException(String.format("Comparison type of '%s' not supported", label));
    }
}
