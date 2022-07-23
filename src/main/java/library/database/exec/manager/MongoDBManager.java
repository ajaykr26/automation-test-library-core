package library.database.exec.manager;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import library.common.Property;
import library.database.exec.DBManager;
import library.database.exec.DBTContext;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;
import java.util.stream.Collectors;

import static library.database.core.Constants.ENVIRONMENTPATH;

public class MongoDBManager extends DBManager {
    private static final String ENV_PROP = "cukes.env";
    private final String dbURL = DBTContext.getInstance().getDataBaseURL();
    private final String dbUserName = getDBUserName(getPathToEnvProps());
    private final String dbPassword = getDBPassword(getPathToEnvProps());
    private final String databaseName = getDatabaseName();
    private final String JAVAX_NET_SSL_TRUST_STORE = "javax.net.ssl.trustStore";
    private String currentCertFile = "";

    @Override
    protected void createConnection() {
        final String uri = String.format("mongodb://%s:%s@%s%s?authSource=admin&ssl=true", dbUserName, dbPassword, dbURL, databaseName);
        final String propsFilePath = ENVIRONMENTPATH + Property.getVariable(ENV_PROP) + PROPERTIES_EXT;
        final String certificateKey = String.join("_",
                DBTContext.getInstance().getAppName(),
                DBTContext.getInstance().getDataBaseType(), "certs");
        currentCertFile = Property.getVariable(JAVAX_NET_SSL_TRUST_STORE);
        final String certificatePath = ENVIRONMENTPATH + Property.getProperty(propsFilePath, certificateKey);
        System.setProperty(JAVAX_NET_SSL_TRUST_STORE, certificatePath);
        MongoClientOptions.builder().sslEnabled(true).build();

        try {
            MongoClientURI mongoClientURI = new MongoClientURI(uri);
            mongoClient = new MongoClient(mongoClientURI);
            MongoDatabase database = mongoClient.getDatabase(databaseName);
            if (database != null) {
                DBTContext.getInstance().setDatabaseName(databaseName);
            }
            DBTContext.getInstance().setConnActive(true);
        } catch (Exception exception) {
            logger.error("unable to connect to mongoDB");
            throw exception;
        }
    }

    @Override
    protected void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            DBTContext.getInstance().setConnActive(false);
            logger.debug("database connection closed successfully");
            System.setProperty(JAVAX_NET_SSL_TRUST_STORE, currentCertFile);
        } else {
            logger.warn("MongoDB connection not open");
        }
    }

    @Override
    protected List<Map<String, Object>> runQuery(String query) {
        Map<String, Object> map = new Gson().fromJson(query, Map.class);
        final String collectionName = (String) getQueryAttribute("mongoCollection", map);
        final String jsonPathForResultBody = getJsonPath((String) getQueryAttribute("resultParam", map));
        final Document queryBson = getBson(map.get("query"));
        final Document sortBson = getBson(map.get("sort"));
        final Document matchBson = getBson(map.get("match"));
        final Document groupBson = getBson(map.get("group"));
        final Document projectBson = getBson(map.get("project"));
        final MongoCollection<Document> collection = mongoClient.getDatabase(databaseName).getCollection(collectionName);

        List<Map<String, Object>> resultList;
        if (!matchBson.isEmpty() && !groupBson.isEmpty() && !projectBson.isEmpty()) {
            resultList = getAggregateQueryResultList(collection, matchBson, groupBson, projectBson);
        } else {
            resultList = getQueryResultList(collection, queryBson, sortBson, jsonPathForResultBody);
        }
        return resultList;
    }

    private String getJsonPath(String paramName) {
        if (paramName != null && !paramName.isEmpty()) {
            StringJoiner jsonPath = new StringJoiner(".");
            jsonPath.add("$");
            jsonPath.add(paramName);
            return jsonPath.toString();
        } else {
            return "$";
        }
    }

    private Object getQueryAttribute(String attributeName, Map<String, Object> queryMap) {
        Object attributeValue = queryMap.get(attributeName);
        if (attributeValue == null) {
            throw new IllegalArgumentException(String.format("no value provided for %s please update the query", attributeName));
        } else {
            return attributeValue;
        }
    }

    private Document getBson(Object jsonString) {
        try {
            if (jsonString != null) {
                return Document.parse(new Gson().toJson(jsonString));
            }
        } catch (JsonParseException exception) {
            logger.error("unable to parse query string: {} querying with empty JSON body", exception.getMessage());
        }
        return Document.parse("{}");
    }

    private List<Map<String, Object>> getAggregateQueryResultList(MongoCollection<Document> collection, Document matchBson, Document groupBson, Document projectBson) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        Map<String, Object> resultSet;
        List<Bson> aggregationList = Arrays.asList(matchBson, groupBson, projectBson);

        try (MongoCursor<Document> cursor = collection.aggregate(aggregationList).iterator()) {
            while (cursor.hasNext()) {
                if (System.getProperty("fw.dbNullFlag").equals("false")) {
                    String resultString = String.valueOf(cursor.next().toJson());
                    resultSet = new Gson().fromJson(resultString, Map.class);
                    resultList.add(resultSet.entrySet().stream().filter(entry -> entry.getKey() != null).filter(entry -> entry.getValue() != null)
                            .collect((Collectors.toMap((entry -> entry.getKey().toUpperCase()), Map.Entry::getValue))));
                } else {
                    String resultString = String.valueOf(cursor.next().toJson());
                    resultSet = new Gson().fromJson(resultString, Map.class);
                    resultList.add(resultSet.entrySet().stream().filter(entry -> entry.getKey() != null)
                            .collect((Collectors.toMap((entry -> entry.getKey().toUpperCase()),
                                    entry -> Optional.ofNullable(entry.getValue()).orElse("null")))));
                }
            }
        }
        return resultList;
    }

    private List<Map<String, Object>> getQueryResultList(MongoCollection<Document> collection, Document queryBson, Document sortBson, String jsonPathForResultBody) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        Map<String, Object> resultSet;

        try (MongoCursor<Document> cursor = collection.find(queryBson).sort(sortBson).
                limit(Integer.parseInt(System.getProperty("fw.DBMaxRowCount"))).iterator()) {
            while (cursor.hasNext()) {
                if (System.getProperty("fw.dbNullFlag").equals("false")) {
                    ReadContext context = JsonPath.parse(String.valueOf(cursor.next().toJson()));
                    resultSet = context.read(jsonPathForResultBody, Map.class);
                    resultList.add(resultSet.entrySet().stream().filter(entry -> entry.getKey() != null).filter(entry -> entry.getValue() != null)
                            .collect((Collectors.toMap((entry -> entry.getKey().toUpperCase()), Map.Entry::getValue))));
                } else {
                    ReadContext context = JsonPath.parse(String.valueOf(cursor.next().toJson()));
                    resultSet = context.read(jsonPathForResultBody, Map.class);
                    resultList.add(resultSet.entrySet().stream().filter(entry -> entry.getKey() != null).filter(entry -> entry.getValue() != null)
                            .collect((Collectors.toMap((entry -> entry.getKey().toUpperCase()),
                                    entry -> Optional.ofNullable(entry.getValue()).orElse("null")))));
                }
            }
        }
        return resultList;
    }
}
