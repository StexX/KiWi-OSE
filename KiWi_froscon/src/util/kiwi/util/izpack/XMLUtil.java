

package kiwi.util.izpack;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;


/**
 * Sums together a lot of XML related
 *
 * @author mradules
 */
final class XMLUtil {

    /**
     * Don't let anybody to instantiate this class.
     */
    private XMLUtil() {
        // UNIMPLMENENTED
    }

    /**
     * Builds a DOM for the given file, the file is specified
     * with its path (String).
     *
     * @param path the path for the file.
     * @return a DOM for the given file.
     * @throws NullPointerException if the <code>path</code>
     *             argument is null.
     * @throws ParserConfigurationException if the file can not
     *             be parsed from any reasons.
     * @throws SAXException by any XML related exception.
     * @throws IOException by any IO related exception.
     * @see #getDocument(File)
     */
    static Document getDocument(String path)
            throws ParserConfigurationException, SAXException, IOException {

        if (path == null) {
            throw new NullPointerException("The path argument can not be null");
        }

        if (path.isEmpty()) {
            throw new FileNotFoundException(
                    "The path argument can not an empty String.");
        }

        return getDocument(new File(path));
    }

    /**
     * Builds a DOM for the given file.
     *
     * @param file the file to parse..
     * @return a DOM for the given file.
     * @throws NullPointerException if the <code>file</code>
     *             argument is null.
     * @throws FileNotFoundException if the file does not exist.
     * @throws ParserConfigurationException if the file can not
     *             be parsed from any reasons.
     * @throws SAXException by any XML related exception.
     * @throws IOException by any IO related exception.
     * @see #getDocument(String)
     */
    public static Document getDocument(File file) throws ParserConfigurationException,
            SAXException, IOException {

        if (file == null) {
            throw new NullPointerException("The path argument can not be null");
        }

        if (!file.exists()) {
            final String msg =
                    String.format("The file [%s] does not exist.", file);
            throw new FileNotFoundException(msg);
        }

        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        final DocumentBuilder db = dbf.newDocumentBuilder();
        final Document doc = db.parse(file);

        return doc;

    }

    public static Document getDocument(InputStream inputStream)
            throws ParserConfigurationException, SAXException, IOException {

        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        final DocumentBuilder db = dbf.newDocumentBuilder();
        final Document doc = db.parse(inputStream);

        return doc;

    }

    /**
     * Format and save a specified <code>Document</code>.
     *
     * @param output here is the <code>Document</code> saved.
     * @param document the document to save.
     * @throws IOException if the save operation fails from any
     *             IO related reasons.
     */
    public static void persist(final OutputStream output, final Document document)
            throws IOException {
        try {
            final Source source = new DOMSource(document); 
            final Transformer xformer = TransformerFactory.newInstance().newTransformer();
            final Result result = new StreamResult(output);
            xformer.transform(source, result);
        } catch (Exception e) {
            // MIhai : This is not the best way to treat all exception but at 
            // least I rethrow the exception cause.
            throw new IOException(e);
        } 
    }

}
