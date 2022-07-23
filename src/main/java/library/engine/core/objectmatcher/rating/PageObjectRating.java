package library.engine.core.objectmatcher.rating;

public class PageObjectRating {

    private String pageObjectName;
    private Double similarityScore;
    private Class<?> pageObjectClazz;
    private String searchResultMessage;
    private String pageObjectList;

    public PageObjectRating(String pageObjectName, Double similarityScore, Class<?> pageObjectClazz) {
        this.pageObjectName = pageObjectName;
        this.similarityScore = similarityScore;
        this.pageObjectClazz = pageObjectClazz;
        this.pageObjectList = null;
    }

    public PageObjectRating(String pageObjectName, Double similarityScore, Class<?> pageObjectClazz, String searchResultMessage, String pageObjectList) {
        this.pageObjectName = pageObjectName;
        this.similarityScore = similarityScore;
        this.pageObjectClazz = pageObjectClazz;
        this.searchResultMessage = searchResultMessage;
        this.pageObjectList = pageObjectList;
    }

    public String getPageObjectName() {
        return pageObjectName;
    }

    public void setPageObjectName(String pageObjectName) {
        this.pageObjectName = pageObjectName;
    }

    public Double getSimilarityScore() {
        return similarityScore;
    }

    public void setSimilarityScore(Double similarityScore) {
        this.similarityScore = similarityScore;
    }

    public Class<?> getPageObjectClazz() {
        return pageObjectClazz;
    }

    public void setPageObjectClazz(Class<?> pageObjectClazz) {
        this.pageObjectClazz = pageObjectClazz;
    }

    public String getSearchResultMessage() {
        return searchResultMessage;
    }

    public void setSearchResultMessage(String searchResultMessage) {
        this.searchResultMessage = searchResultMessage;
    }

    public String getPageObjectList() {
        return pageObjectList;
    }

    public void setPageObjectList(String pageObjectList) {
        this.pageObjectList = pageObjectList;
    }
}
