package library.database.exec.map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DBConnector {

    @SerializedName("dbDriver")
    @Expose
    private String dbDriver;

    @SerializedName("dbURL")
    @Expose
    private String dbURL;

    public String getDbDriver() {
        return dbDriver;
    }

    public void setDbDriver(String dbDriver) {
        this.dbDriver = dbDriver;
    }

    public String getDbURL() {
        return dbURL;
    }

    public void setDbURL(String dbURL) {
        this.dbURL = dbURL;
    }

}
