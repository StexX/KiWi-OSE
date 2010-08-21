

package mars.util;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class ItemCollector {

    private static final String user = "mihai.radulescu@salzburgresearch.at";

    private static final String passwd = "mihai123";

    private final static XPathFactory pathFactory = XPathFactory.newInstance();

    private final static DocumentBuilder DOMBuilder;
    static {
        try {
            DOMBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // this is a very ugly way to trick a static block
            // TODO : define your own extension.
            throw new RuntimeException(e);
        }
    }

    private ItemCollector()  {
    }

    public static List<String> getItemsArchiveNumber(String uri)
            throws ParserConfigurationException, IOException, SAXException,
            XPathException {

        final Document baseDOM = buidDocument(uri);

        final NodeList elements = baseDOM.getElementsByTagName("element");

        final List<String> result = new ArrayList<String>();
        for (int index = 0; index < elements.getLength(); index++) {
            final Element element = (Element) elements.item(index);
            final String archiveNr = element.getAttribute("archiveNr");
            if (archiveNr != null && !archiveNr.isEmpty()) {
                result.add(archiveNr);
            }
        }

        return result;
    }

    private static Document buidDocument(String uri)
            throws ParserConfigurationException, IOException, SAXException {
        final DocumentBuilder builder =
                DocumentBuilderFactory.newInstance().newDocumentBuilder();
        final InputStream inputStream = RESTUtil.getAsStream(user, passwd, uri);
        final Document document = builder.parse(inputStream);

        return document;
    }

    private static String buildElementURL(String archiveNr) {
        final StringBuffer result = new StringBuffer();
        result
                .append("http://pf.mediamid.com/mediamanager/REST/auth/path/metadata/");
        result.append(archiveNr);
        result.append(".xml");

        return result.toString();
    }

    public static List<String> getItemsURL(String uri)
            throws ParserConfigurationException, IOException, SAXException,
            XPathException {
        final List<String> itemsArchveNrs = getItemsArchiveNumber(uri);
        final List<String> result = new ArrayList<String>();
        for (final String itemOID : itemsArchveNrs) {
            final String itemURL = buildElementURL(itemOID);
            result.add(itemURL);
        }
        return result;
    }

    public static List<Document> getItems(String uri)
            throws ParserConfigurationException, IOException, SAXException,
            XPathException {

        final List<Document> result = new ArrayList<Document>();
        final List<String> itemsURL = getItemsURL(uri);

        for (final String itemURL : itemsURL) {
            final InputStream inputStream =
                    RESTUtil.getAsStream(user, passwd, itemURL);
            if (inputStream == null) {
                final StringBuilder msg = new StringBuilder();
                msg.append("The resource [");
                msg.append("] can not be accessed.");

                throw new NullPointerException(msg.toString());
            }

            final Document itemDOM = DOMBuilder.parse(inputStream);
            result.add(itemDOM);
        }

        return result;
    }

    public static Document getItemAfterArchiveNumber(String archiveNr) throws IOException, SAXException {
        final String url = buildElementURL(archiveNr);
        return getItem(url);
    }


    public static Document getItem(String itemURL) throws IOException, SAXException {

        final InputStream inputStream =
                RESTUtil.getAsStream(user, passwd, itemURL);

        final Document result = DOMBuilder.parse(inputStream);

        return result;
    }


    public static List<String> getItemRelationURLs(Document item) {
        final NodeList relations = item.getElementsByTagName("Relation");

        final List<String> result = new ArrayList<String>();
        for (int relIndex = 0; relIndex < relations.getLength(); relIndex++) {
            final Element relation = (Element) relations.item(relIndex);
            final String href = relation.getAttribute("ns3:href");
            final String URL = buildElementURL(href);
            result.add(URL);
        }

        return result;
    }

    public static List<Document> getItemRelationDocuments(Document item) throws ParserConfigurationException, IOException, SAXException {
        final List<String> relationURLs = getItemRelationURLs(item);
        final List<Document> result = new ArrayList<Document>();

        for (String url : relationURLs) {
            final Document document = buidDocument(url);
            result.add(document);
        }

        return result;
    }
}
