package library.common;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.FileHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Property {
    protected static final Logger logger = LogManager.getLogger(Property.class);

    private Property() {

    }

    public static PropertiesConfiguration getProperties(String propsFilePath) {
        PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration();
        try {
            File propsFile = new File(propsFilePath);
            if (propsFile.exists()) {
                return new Configurations().properties(propsFile);
            }

        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
        return null;
    }

    public static void setProperty(String propFilePath, String key, String value) {
        PropertiesConfiguration props = getProperties(propFilePath);
        if (props != null) {
            props.setProperty(key, value);
            try {
                FileHandler fileHandler = new FileHandler(props);
                fileHandler.save(propFilePath);
            } catch (ConfigurationException e) {
                logger.warn("unable to set property of '{}'. {} ", key, e.getMessage());
            }
        }

    }

    public static String getProperty(String propFilePath, String key) {
        PropertiesConfiguration propertiesConfiguration = getProperties(propFilePath);
        if (propertiesConfiguration != null) {
            return propertiesConfiguration.getString(key);
        }
        return null;
    }

    public static String[] getPropertyArray(String propFilePath, String key) {
        PropertiesConfiguration propertiesConfiguration = getProperties(propFilePath);
        String[] keyValue = null;
        if (propertiesConfiguration != null) {
            keyValue = propertiesConfiguration.getStringArray(key);

        }
        return keyValue;
    }

    public static String getVariable(String propName) {
        String val = System.getProperty(propName, null);
        val = (val == null ? System.getenv(propName) : val);
        return val;
    }

    public static Map<String, Object> getPropertiesAsMap(String propFilePath) {
        PropertiesConfiguration props = getProperties(propFilePath);
        Map<String, Object> propsMap = new HashMap<>();
        if (props != null) {
            Iterator<String> iterator = props.getKeys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                if (key != null) {
                    propsMap.put(key, props.getProperty(key).toString());
                }
            }
        }
        return propsMap;
    }

}
