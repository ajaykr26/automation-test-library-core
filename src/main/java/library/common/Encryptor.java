package library.common;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Encryptor {

    private static final Logger logger = LogManager.getLogger(Encryptor.class);

    private Encryptor() {
    }

    private static String parseSecureText(String propFilePath, String encryptedStringKey) {
        String encryptedString = Property.getProperty(propFilePath, encryptedStringKey);
        if (encryptedString != null) {
            return decrypt(encryptedString);
        } else {
            logger.error("entry for key '{}' was not found in the SecureText property file", encryptedStringKey);
        }
        return null;
    }

    private static String encrypt(String plainString) {
        if (plainString != null) {
            byte[] byteArray = Base64.encodeBase64(plainString.getBytes());
            return new String(byteArray);
        } else {
            logger.error("please enter plain text to be encrypted");
        }
        return null;
    }

    public static String decrypt(String encryptedString) {
        if (encryptedString != null) {
            byte[] decodedByteArray = Base64.decodeBase64(encryptedString.getBytes());
            return new String(decodedByteArray);
        } else {
            logger.error("encrypted string not found in the SecureText property file");
        }
        return null;
    }

}
