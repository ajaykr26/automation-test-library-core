package library.database.exec.map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class App {
    @SerializedName("databaseToUse")
    @Expose
    private String databaseToUse;

    public String getDatabaseToUse() {
        return databaseToUse;
    }

    public void setDatabaseToUse(String databaseToUse) {
        this.databaseToUse = databaseToUse;
    }
}
