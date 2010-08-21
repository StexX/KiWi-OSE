

package kiwi.util.izpack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Used to manage and manipulate the information stored in the
 * <code>application.xml</code> file. <br>
 * From design reasons this class can not be extend.
 *
 * @author mradules
 */
final class ApplicationsParser {

    /**
     * The ear module name prefix characteristic to a kiwi extension.
     */
    private static final String KIWI_EXTENSION_NAME_PREFIX = "kiwiext-"; //$NON-NLS-1$

    /**
     * The name for the EJB node.
     */
    private static final String EJB_NODE_NAME = "ejb"; //$NON-NLS-1$

    /**
     * The DOM which contains the application.xml.
     */
    private final Document document;

    /**
     * All the KiWi related nodes.
     */
    private final Set<Node> applicationNodes;

    /**
     * All the kiwi extension names - and them state, if the state flag is false
     * then the kiwi extension will be removed on the next persist action.
     */
    private final Map<String, Boolean> applications;

    /**
     *
     */
    private List<String> selApplications;

    private File destinationFile;

    /**
     * Builds a <code>ApplicationsParser</code>
     *
     * @param document
     *            the DOM correspond to the application.xml file.
     */
    ApplicationsParser(Document document) {
        this.document = document;
        applicationNodes = getApplications(document, KIWI_EXTENSION_NAME_PREFIX);
        applications = new HashMap<String, Boolean>();
        for (final Node applicationNode : applicationNodes) {
            applications.put(applicationNode.getTextContent(), Boolean.TRUE);
        }
        selApplications = new ArrayList<String>();
    }

    /**
     * Extracts all the ejb nodes which have a text content that start with the
     * given prefix. If this method return null then the provided DOM does not
     * contains any EJB KiWi related nodes.
     *
     * @param document
     *            the DOM to serch.
     * @param moduleNamePrefix
     *            the prefix for the node text content.
     * @return a set which contains all the KiWi EJB nodes.
     */
    private Set<Node> getApplications(Document document, String moduleNamePrefix) {
        final NodeList elements = document.getElementsByTagName(EJB_NODE_NAME);
        final Set<Node> result = new HashSet<Node>();
        for (int index = 0; index < elements.getLength(); index++) {
            final Node item = elements.item(index);

            final String textContent = item.getTextContent();
            if (textContent.startsWith(moduleNamePrefix)) {
                result.add(item);
            }
        }

        return result;
    }

    /**
     * Returns an alphabetic sorted list which contains all the KiWi extension
     * names, if the list is empty then the system does not contains any KiWi
     * related application.
     *
     * @return an alphabetic sorted list which contains all the KiWi extension
     *         names.
     */
    List<String> getApplicationNames() {
        final List<String> result = new ArrayList<String>();
        for (final String appName : applications.keySet()) {
            result.add(appName);
        }

        Collections.sort(result);

        return result;
    }

    void setSelectedApplication(List<String> selApplications) {
        this.selApplications = selApplications;
    }

    void removeApplication(String application) {
        selApplications.remove(application);
    }

    private void removeApplications() {

        if (selApplications.isEmpty()) {
            // avoid to remove all application
            return;
        }

        final List<Node> removed = new ArrayList<Node>();

        for (final Node node : applicationNodes) {
            final String textContent = node.getTextContent();
            if (!selApplications.contains(textContent)) {
                // if the node is not in the selected list the it
                // must be deleted.

                // here I remove the ejb node from the module
                // node, this steep is not needed if I remove the
                // parent node (at least I think so).
                final Node moduleNode = node.getParentNode();
                moduleNode.removeChild(node);

                // here I remove the module node from the
                // application node
                final Node applicationNode = moduleNode.getParentNode();
                applicationNode.removeChild(moduleNode);

                removed.add(node);
            }
        }

        applicationNodes.removeAll(removed);
        for (final Node node : removed) {
            final String textContent = node.getTextContent();
            applications.remove(textContent);
        }
    }

    /**
     * Persist the actual state on a given path.
     *
     * @param path
     *            the
     * @throws NullPointerException
     *             if the <code>path</code> argument is null.
     * @throws IOException
     */
    void perist(String path) throws IOException {

        if (path == null) {
            throw new NullPointerException("The path can not be null.");
        }

        final File dest = new File(path);
        final String parentStr = dest.getParent();
        final File parent = new File(parentStr);
        if (!parent.exists()) {
            parent.mkdirs();
        }

        removeApplications();
        final FileOutputStream outputStream = new FileOutputStream(path);
        XMLUtil.persist(outputStream, document);
    }

    void perist() throws IOException {
        perist(destinationFile.getAbsolutePath());
    }

    File getDestinationFile() {
        return destinationFile;
    }

    void setDestinationFile(File destinationFile) {
        this.destinationFile = destinationFile;
    }
}
