package library.api.utils;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;

public class XmlHelper {

    public static Document convertStringToXMLDocument(String xmlString) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(xmlString)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getValueFromXmlDocumentByXpath(String xmlString, String xpath) {
        Document doc = convertStringToXMLDocument(xmlString);
        XPath xp = XPathFactory.newInstance().newXPath();
        try {
            return xp.evaluate(xpath, doc);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
