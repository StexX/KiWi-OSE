

package kiwi.util.izpack;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * Used to manage and manipulate the information stored in the
 * <code>persitence.xml</code> file. <br>
 * From design reasons this class can not be extend.
 * 
 * @author mradules
 */
final class DatabaseParser {

    /**
     * The property node name.
     */
    private static final String PROPERTY_NODE_NAME = "property"; //$NON-NLS-1$

    /**
     * The name attribute (for the property node).
     */
    private static final String NAME_PROPERTY = "name"; //$NON-NLS-1$

    /**
     * The value attribute (for the property node).
     */
    private static final String VALUE_PROPERTY = "value"; //$NON-NLS-1$

    /**
     * The "Hibernate dialect" value attribute's value.
     */
    private static final String HIBERNATE_DIALECT_PROPERTY =
            "hibernate.dialect"; //$NON-NLS-1$

    /**
     * The "KiWi triple store" value attribute's value.
     */
    private static final String KIWI_TRIPLESTORE_DIR_PROPERTY =
            "kiwi.triplestore.dir"; //$NON-NLS-1$

    /**
     * The "KiWi SOLR home" value attribute's value.
     */
    private static final String KIWI_SOLR_HOME_PROPERTY = "kiwi.solr.home"; //$NON-NLS-1$

    /**
     * The "KiWi work directory" value attribute's value.
     */
    private static final String KIWI_WORK_DIR_PROPERTY = "kiwi.work.dir"; //$NON-NLS-1$

    /**
     * The "KiWi semantic vectors directory" value attribute's
     * value.
     */
    private static final String KIWI_SEMVECTOR_DIR_PROPERTY =
            "kiwi.semanticvectors"; //$NON-NLS-1$

    /**
     * The DOM which contains the application.xml.
     */
    private final Document document;

    /**
     * The actual database system.
     */
    private DatabaseSystem database;

    /**
     * The element which contains the KiWi work directory
     * property ("kiwi.work.dir").
     */
    private final Element workDirNode;

    /**
     * The element which contains the KiWi work directory
     * property ("kiwi.solr.home").
     */
    private final Element solrHomeNode;

    /**
     * The element which contains the KiWi work directory
     * property ("kiwi.triplestore.dir").
     */
    private final Element triplestoreDirNode;

    /**
     * The element which contains the KiWi semantic vectors
     * directory property ("kiwi.semanticvectors").
     */
    private final Element semanticVectorsDirNode;

    /**
     * The element which contains the hybernate dialect property
     * ("hibernate.dialect").
     */
    private final Element hibernateDialectNode;

    private File destinationFile;

    /**
     * Used to manage and manipulate the information stored in
     * the <code>persistence.xml</code> file.
     * 
     * @param document the DOM correspond to the application.xml
     *            file, can not be null.
     * @throws NullPointerException if the <code>document</code>
     *             is null.
     */
    DatabaseParser(Document document) {

        if (document == null) {
            throw new NullPointerException("The document can not be null.");
        }

        this.document = document;
        workDirNode = getProperty(KIWI_WORK_DIR_PROPERTY);
        solrHomeNode = getProperty(KIWI_SOLR_HOME_PROPERTY);
        triplestoreDirNode = getProperty(KIWI_TRIPLESTORE_DIR_PROPERTY);
        hibernateDialectNode = getProperty(HIBERNATE_DIALECT_PROPERTY);
        semanticVectorsDirNode = getProperty(KIWI_SEMVECTOR_DIR_PROPERTY);
        final String attribute =
                hibernateDialectNode.getAttribute(VALUE_PROPERTY);
        if (attribute != null) {
            for (final DatabaseSystem databaseSystem : DatabaseSystem.values()) {
                if (attribute.contains(databaseSystem.getName())) {
                    // the dialect name contains the db system
                    // name
                    database = databaseSystem;
                }
            }
        }
    }

    /**
     * Returns the DOM element that has a certain property, the
     * properties is identified after its name. E.G. the method
     * call :
     * 
     * <pre>
     * Element e = getProperty(&quot;kiwi.work.dir&quot;);
     * </pre>
     * 
     * will return the element tat looks like :
     * 
     * <pre>
     * <property name="kiwi.work.dir" value="/tmp/kiwi"/>
     * </pre>
     * 
     * @param propName the property name.
     * @return the property with the given name.
     */
    private Element getProperty(String propName) {
        final NodeList elements =
                document.getElementsByTagName(PROPERTY_NODE_NAME);
        for (int index = 0; index < elements.getLength(); index++) {
            final Element item = (Element) elements.item(index);
            final String name = item.getAttribute(NAME_PROPERTY);
            if (propName.equals(name)) {
                return item;
            }
        }

        return null;
    }

    /**
     * Returns an array which contains the name for all the
     * supported database systems.
     * 
     * @return an array which contains the name for all the
     *         supported database systems.
     */
    DatabaseSystem[] getSupportedDatabase() {
        return DatabaseSystem.values();
    }

    /**
     * Returns the actual database base system.
     * 
     * @return the actual database base system.
     */
    DatabaseSystem getActualDatabase() {
        return database;
    }

    /**
     * Set the actual database system.
     * 
     * @param database the actual database system.
     */
    void setActualDatabase(DatabaseSystem database) {
        this.database = database;
        hibernateDialectNode.setAttribute(VALUE_PROPERTY,
                this.database.getDialect());
    }

    /**
     * Registers a new value for the actual database system. The
     * string must correspond one <code>toString</code> element
     * representation from the <code>DatabaseSystem</code> type
     * safe enum.
     * 
     * @param database the name for the database system.
     * @see DatabaseSystem
     */
    void setActualDatabaseAsString(String database) {
        for (final DatabaseSystem ds : DatabaseSystem.values()) {
            if (database.equals(ds.getName())) {
                this.database = ds;
                hibernateDialectNode.setAttribute(VALUE_PROPERTY,
                        this.database.getDialect());
                break;
            }
        }
    }

    /**
     * Returns the actual value for the KiWi work directory
     * property.
     * 
     * @return the actual value for the KiWi work directory
     *         property.
     */
    String getWorkDir() {
        return workDirNode.getAttribute(VALUE_PROPERTY);
    }

    /**
     * Register a new value for the KiWi work directory property.
     * 
     * @param workDir the new value for the workDir.
     */
    void setWorkDir(String workDir) {
        workDirNode.setAttribute(VALUE_PROPERTY, workDir);
    }

    /**
     * Returns the actual value for the KiWi work directory
     * property.
     * 
     * @return the solrHome the
     */
    String getSolrHome() {
        return solrHomeNode.getAttribute(VALUE_PROPERTY);
    }

    /**
     * @param solrHome the solrHome to set
     */
    void setSolrHome(String solrHome) {
        solrHomeNode.setAttribute(VALUE_PROPERTY, solrHome);
    }

    /**
     * @return the triplestoreDir
     */
    String getTriplestoreDir() {
        return triplestoreDirNode.getAttribute(VALUE_PROPERTY);
    }

    /**
     * @param triplestoreDir the triplestoreDir to set
     */
    void setTriplestoreDir(String triplestoreDir) {
        triplestoreDirNode.setAttribute(VALUE_PROPERTY, triplestoreDir);
    }

    String getSemanticVectorDir() {
        return semanticVectorsDirNode.getAttribute(VALUE_PROPERTY);
    }

    void setSemanticVectorDir(String semanticVectorDir) {
        semanticVectorsDirNode.setAttribute(VALUE_PROPERTY, semanticVectorDir);
    }

    /**
     * Persists the actual state in in an XML file placed on a
     * given path.
     * 
     * @param path the path to the xml file.
     * @throws IOException by any IO related exceptions.
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

        final FileOutputStream outputStream = new FileOutputStream(path);
        XMLUtil.persist(outputStream, document);
    }

    File getDestinationFile() {
        return destinationFile;
    }

    void setDestinationFile(File destinationFile) {
        this.destinationFile = destinationFile;
    }

    void persist() throws IOException {

        if (destinationFile == null) {
            throw new NullPointerException(
                    "The destination file can not eb null.");
        }

        final String parentStr = destinationFile.getParent();
        final File parent = new File(parentStr);
        if (!parent.exists()) {
            parent.mkdirs();
        }

        final FileOutputStream outputStream =
                new FileOutputStream(destinationFile);
        XMLUtil.persist(outputStream, document);
    }
}
