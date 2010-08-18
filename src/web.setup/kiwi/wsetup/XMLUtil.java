

package kiwi.wsetup;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;


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
    static Document getDocument(File file) throws ParserConfigurationException,
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

    static Document getDocument(InputStream inputStream)
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
    static void persist(final OutputStream output, final Document document)
            throws IOException {

        final OutputFormat format = new OutputFormat();
        format.setEncoding("UTF-8");
        format.setIndenting(true);
        format.setLineWidth(0);
        format.setIndent(4);
        format.setPreserveEmptyAttributes(true);

        final XMLSerializer serializer = new XMLSerializer(output, format);
        serializer.setNamespaces(true);
        serializer.serialize(document);
    }

}
