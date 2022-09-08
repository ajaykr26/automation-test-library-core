package library.api.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
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

    public static void setValueIntoXmlDocumentByXpath(String xmlString,String valueToSet, String xPath) {
        Document document = convertStringToXMLDocument(xmlString);
        try {
            XPathFactory xpf = XPathFactory.newInstance();
            XPath xpath = xpf.newXPath();
            XPathExpression expression = xpath.compile(xPath);

            Node node = (Node) expression.evaluate(document, XPathConstants.NODE);
            node.setTextContent(valueToSet);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }
}
