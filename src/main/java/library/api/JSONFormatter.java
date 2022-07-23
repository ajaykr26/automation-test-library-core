package library.api;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;

import java.util.List;
import java.util.Map;

public class JSONFormatter {

    private JSONFormatter() {
    }

    private static Logger getLogger() {
        return LogManager.getLogger(JSONFormatter.class);
    }

    public static String formatJSON(Object jsonString) {
        try {
            if (jsonString instanceof String) {
                JsonElement jsonElement = JsonParser.parseString(jsonString.toString());
                return new GsonBuilder().setPrettyPrinting().setLenient().create().toJson(jsonElement);
            } else if (jsonString instanceof Map) {
                return new GsonBuilder().setPrettyPrinting().create().toJson(jsonString, Map.class);
            } else if (jsonString instanceof JSONArray) {
                return new GsonBuilder().setPrettyPrinting().create().toJson(jsonString, JSONArray.class);
            } else if (jsonString instanceof List) {
                return new GsonBuilder().setPrettyPrinting().create().toJson(jsonString, List.class);
            } else {
                getLogger().warn("unrecognized response body type: {}", jsonString);
                return jsonString.toString();
            }
        } catch (JsonSyntaxException exception) {
            getLogger().debug("not a valid json: {}", jsonString);
            return jsonString.toString();
        }


    }
}
