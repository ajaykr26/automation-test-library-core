package library.engine.core.objectmatcher.rating;

public class FlatFileObjectRating {

    private String flatFileObjectName;
    private Double similarityScore;
    private String searchResultMessage;
    private String flatFileObjectList;

    public FlatFileObjectRating(String flatFileObjectName, Double similarityScore) {
        this.flatFileObjectName = flatFileObjectName;
        this.similarityScore = similarityScore;
    }

    public FlatFileObjectRating(String flatFileObjectName, Double similarityScore, String searchResultMessage, String flatFileObjectList) {
        this.flatFileObjectName = flatFileObjectName;
        this.similarityScore = similarityScore;
        this.searchResultMessage = searchResultMessage;
        this.flatFileObjectList = flatFileObjectList;
    }

    public String getFlatFileObjectName() {
        return flatFileObjectName;
    }

    public void setFlatFileObjectName(String flatFileObjectName) {
        this.flatFileObjectName = flatFileObjectName;
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

    public String getFlatFileObjectList() {
        return flatFileObjectList;
    }

    public void setFlatFileObjectList(String flatFileObjectList) {
        this.flatFileObjectList = flatFileObjectList;
    }


}
