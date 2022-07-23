package library.engine.core.objectmatcher.rating;

public class FieldRating {


    private String fieldName;
    private Double similarityScore;
    private String searchResultMessage;
    private String filedList;

    public FieldRating(String fieldName, Double similarityScore) {
        this.fieldName = fieldName;
        this.similarityScore = similarityScore;
        this.searchResultMessage = "";
        this.filedList = null;
    }

    public FieldRating(String fieldName, Double similarityScore, String searchResultMessage, String filedList) {
        this.fieldName = fieldName;
        this.similarityScore = similarityScore;
        this.searchResultMessage = searchResultMessage;
        this.filedList = filedList;
    }


    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Double getSimilarityScore() {
        return similarityScore;
    }

    public void setSimilarityScore(Double similarityScore) {
        this.similarityScore = similarityScore;
    }

    public String getSearchResultMessage() {
        return searchResultMessage;
    }

    public void setSearchResultMessage(String searchResultMessage) {
        this.searchResultMessage = searchResultMessage;
    }

    public String getFiledList() {
        return filedList;
    }

    public void setFiledList(String filedList) {
        this.filedList = filedList;
    }

}
