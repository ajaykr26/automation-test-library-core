package library.common;

import com.google.gson.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONHelper {

    private JSONHelper() {
    }

    private static final Logger logger = LogManager.getLogger(JSONHelper.class.getName());

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
                logger.warn("unrecognized response body type: {}", jsonString);
                return jsonString.toString();
            }
        } catch (JsonSyntaxException exception) {
            logger.debug("not a valid json: {}", jsonString);
            return jsonString.toString();
        }

    }

    public static JSONObject getJSONObject(String filepath, String... key) {
        try {
            FileReader reader = new FileReader(filepath);
            JSONTokener token = new JSONTokener(reader);
            return (JSONObject) (key.length > 0 ? new JSONObject(token).get(key[0]) : new JSONObject(token));
        } catch (FileNotFoundException | JSONException e) {
            logger.info("{}.json file not found. returning empty data", System.getProperty("fw.featureName"));
            return null;
        }
    }


    public static JSONArray getJSONArray(String filepath, String... key) {
        try {
            FileReader reader = new FileReader(filepath);
            JSONTokener token = new JSONTokener(reader);
            return (JSONArray) (key.length > 0 ? new JSONObject(token).get(key[0]) : new JSONObject(token));
        } catch (FileNotFoundException e) {
            logger.error(e);
            return null;
        }
    }

    public static <T> T getDataPOJO(String filepath, Class<T> clazz) {
        Gson gson = new Gson();
        File file = new File(filepath);
        T dataobject = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            dataobject = gson.fromJson(bufferedReader, clazz);
        } catch (FileNotFoundException e) {
            logger.error(e);
        }
        return dataobject;
    }

    public static Map<String, String> getJSONToMap(JSONObject json) {
        Map<String, String> map = new HashMap<>();
        try {
            String[] keys = JSONObject.getNames(json);
            for (String key : keys) {
                map.put(key, json.get(key).toString());
            }
            return map;
        } catch (NullPointerException exception) {
            logger.debug("empty data found in json file");
            return map;
        }
    }

    public static Map<String, Object> getJSONToMap(String filename, String... key) {
        Map<String, Object> map = new HashMap<>();

        try {
            FileReader reader = new FileReader(filename);
            JSONTokener token = new JSONTokener(reader);
            JSONObject jsonObject =  (JSONObject) (key.length > 0 ? new JSONObject(token).get(key[0]) : new JSONObject(token));

            String[] keys = JSONObject.getNames(jsonObject);
            for (String k : keys) {
                map.put(k, jsonObject.get(k).toString());
            }
            return map;
        } catch (NullPointerException | FileNotFoundException exception) {
            logger.debug("empty data found in json file");
            return map;
        }
    }
    public static List<Map<String, String>> getJSONAsListOfMaps(String path) {
        Gson gson = new GsonBuilder().create();
        try {
            return gson.fromJson(new FileReader(path), List.class);
        } catch (FileNotFoundException e) {
            return Collections.emptyList();
        }
    }

    public static Map<String, String> getJSONDataAsMap(String filepath) {
        Gson gson = new Gson();
        try {
            FileReader reader = new FileReader(filepath);
            return gson.fromJson(reader, Map.class);
        } catch (FileNotFoundException fileNotFoundException) {
            logger.error(fileNotFoundException);
        }
        return null;
    }

    public static String mapToJSONArrayString(Map<String, String> map) {
        Gson gson = new Gson();
        return gson.toJson(map);
    }

    public static String mapToJSON(Map<String, Object> map, String path) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter jsonWriter = new FileWriter(path)) {
            gson.toJson(map, Map.class, jsonWriter);
            File jsonFile = new File(path);
            if (jsonFile.exists()) {
                return jsonFile.getAbsolutePath();
            } else {
                return null;
            }
        } catch (IOException ioException) {
            logger.error("unable to write file at path: {}", path);
            return null;
        } catch (JsonSyntaxException jsonSyntaxException) {
            logger.debug("not a valid JSON");
            return null;
        }
    }

    public static Map<String, String> getJSONObjectToMap(String filepath) {
        return getJSONToMap(getJSONObject(filepath));
    }

    public static Map<String, String> getJSONToListOfMap(String filepath) {
        return getJSONToMap(getJSONObject(filepath));
    }

    public static Map<String, String> loadJSONMapFromResources(Class clazz, String filepath) {
        InputStream inputStream = clazz.getClassLoader().getResourceAsStream(filepath);

        if (inputStream != null) {
            return new Gson().fromJson(new InputStreamReader(inputStream), Map.class);
        } else {
            return null;
        }
    }

}

