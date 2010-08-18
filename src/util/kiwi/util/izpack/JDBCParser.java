/**
 *
 */


package kiwi.util.izpack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Used to manage and manipulate the JDBC related information. <br>
 * From design reasons this class can not be extend.
 *
 * @author mradules
 */
final class JDBCParser {

    /**
     * The connection url node name.
     */
    private static final String CONNECTION_URL_NODE_NAME = "connection-url"; //$NON-NLS-1$

    /**
     * The driver class node name.
     */
    private static final String DRIVER_CLASS_NODE_NAME = "driver-class"; //$NON-NLS-1$

    /**
     * The user password node name.
     */
    private static final String PASSWORD_NODE_NAME = "password"; //$NON-NLS-1$

    /**
     * The user node name.
     */
    private static final String USER_NODE_NAME = "user-name"; //$NON-NLS-1$

    /**
     * The JNDI node name.
     */
    private static final String JNDI_NODE_NAME = "jndi-name"; //$NON-NLS-1$

    /**
     * The value for the JNDI node corresponding to to the CEQ application.
     */
    private static final String KIWI_JNDI_NAME = "KiWiDatasource"; //$NON-NLS-1$

    /**
     * The DOM for the JDBC configuration file (data source file).
     */
    private final Document document;

    /**
     * Contains all the JDBC setting for KiWi.
     */
    private Node kiwiNode;

    private File destinationFile;

    /**
     * Used to manage and manipulate JDBC related information.
     *
     * @param document
     *            the DOM correspond to the data source xml file, can not be
     *            null.
     * @throws NullPointerException
     *             if the <code>document</code> is null.
     */
    JDBCParser(Document document) {

        if (document == null) {
            throw new NullPointerException("The document can not be null.");
        }

        this.document = document;
        final NodeList nodes = document.getElementsByTagName(JNDI_NODE_NAME);

        for (int index = 0; index < nodes.getLength(); index++) {
            final Element item = (Element) nodes.item(index);
            final String textContent = item.getTextContent();

            if (KIWI_JNDI_NAME.equals(textContent)) {
                kiwiNode = item.getParentNode();
                continue;
            }
        }
    }

    /**
     * Returns the JNDI name for the KiWi data source.
     *
     * @return the JNDI name for the KiWi data source.
     */
    String getKiWiJndiName() {
        final NodeList elements = ((Element) kiwiNode)
                .getElementsByTagName(JNDI_NODE_NAME);

        if (elements.getLength() != 1) {
            throw new IllegalStateException(
                    "The KiWiDatasource node has a wrong contain.");
        }

        final Node item = elements.item(0);
        final String textContent = item.getTextContent();

        return textContent;
    }

    /**
     * Extract the text content for a given child node, the node is located
     * after its tag name and it is searched from a given parent node.
     *
     * @param parentNode
     *            the parent node - from here is the search done.
     * @param nodeName
     *            the node name to search.
     * @return the text content for the
     */
    private String getNodeContent(Node parentNode, String nodeName) {
        final NodeList elements = ((Element) parentNode)
                .getElementsByTagName(nodeName);

        if (elements.getLength() != 1) {
            final String parentTextContent = parentNode.getTextContent();
            final String msg = String
                    .format(
                            "More than one occuernce for the node : [%s] with the parent node [%s], this is illegal.",
                            nodeName, parentTextContent);
            throw new IllegalStateException(msg);
        }

        final Node item = elements.item(0);
        final String textContent = item.getTextContent();

        return textContent;
    }

    /**
     * Returns the JDBC driver name used by the KiWi database.
     *
     * @return the JDBC driver name used by the KiWi application.
     */
    String getKiWiDriver() {
        final String result = getNodeContent(kiwiNode, DRIVER_CLASS_NODE_NAME);
        return result;
    }

    void setKiWiDriver(String driver) {
        setNodeContent((Element) kiwiNode, DRIVER_CLASS_NODE_NAME, driver);
    }

    /**
     * Returns the URL used to connect to the KiWi database.
     *
     * @return the URL used by the KiWi database.
     */
    String getKiWiConnectionURL() {
        final String result = getNodeContent(kiwiNode, CONNECTION_URL_NODE_NAME);
        return result;
    }

    void setKiWiConnectionURL(String url) {
        setNodeContent((Element) kiwiNode, CONNECTION_URL_NODE_NAME, url);
    }

    /**
     * Returns the actual user for the KiWi database.
     *
     * @return the actual user for the KiWi database.
     */
    String getKiWiUser() {
        final String result = getNodeContent(kiwiNode, USER_NODE_NAME);
        return result;
    }

    void setKiWiUser(String user) {
        setNodeContent((Element) kiwiNode, USER_NODE_NAME, user);
    }

    private void setNodeContent(Element parentNode, String tagName,
            String newContent) {
        final NodeList elementsByTagName = parentNode
                .getElementsByTagName(tagName);

        if (elementsByTagName.getLength() != 1) {
            throw new IllegalStateException("More than one " + tagName
                    + " node ");
        }

        final Element item = (Element) elementsByTagName.item(0);
        item.setTextContent(newContent);
    }

    /**
     * Returns the actual user's password for the KiWi database.
     *
     * @return the actual user's password for the KiWi database.
     */
    String getKiWiPassword() {
        final String result = getNodeContent(kiwiNode, PASSWORD_NODE_NAME);
        return result;
    }

    void setKiWiPassword(String passwd) {
        setNodeContent((Element) kiwiNode, PASSWORD_NODE_NAME, passwd);
    }

    /**
     * Restores the default values for all the involved values.s
     */
    void restoreDefault() {
        // TODO : I am not shore how the default must look like.
    }

    /**
     * Persists the actual state in in an XML file placed on a given path.
     *
     * @param path
     *            the path to the xml file.
     * @throws IOException
     *             by any IO related exceptions.
     */
    void persist(String path) throws IOException {
        if (path == null) {
            throw new NullPointerException(
                    "The destination file can not eb null.");
        }

        final File dest = new File(path);

        final String parentStr = dest.getParent();
        final File parent = new File(parentStr);
        if (!parent.exists()) {
            parent.mkdirs();
        }

        final FileOutputStream outputStream = new FileOutputStream(dest);
        XMLUtil.persist(outputStream, document);
    }

    File getDestinationFile() {
        return destinationFile;
    }

    void setDestinationFile(File destinationFile) {
        this.destinationFile = destinationFile;
    }

    void persist() throws IOException {
        persist(destinationFile.getAbsolutePath());
    }
}
