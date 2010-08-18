

package kiwi.wsetup;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


@Scope(ScopeType.APPLICATION)
@Name("kiwiDatabseSetupAction")
@AutoCreate
public class KiWiDatabseSetupAction {

    private static final String DEFAULT_DATASOURCE_FILE_NAME = "default.ds.xml";

    private static final String DEFAULT_PERSISTENCE_FILE_NAME =
            "default.persisntece.xml";

    /**
     * Used to manage
     */
    private DatabaseParser databaseParser;

    /**
     * The path to the persitence.xml file.
     */
    private String persistenceCfgFile;

    private boolean databseSysyemChanged;

    private boolean isPresistenceDefault;

    private boolean isDataSourceceDefault;

    /**
     * Used to manage
     */
    private JDBCParser jdbcParser;

    /**
     * The path to the persitence.xml file.
     */
    private String jdbcCfgFile;

    @Create
    public void init() {
        try {
            init(null);
        } catch (final Exception e) {
            // TODO use your own exception here
            throw new RuntimeException(e);
        }
    }

    private void init(DatabaseSystem database)
            throws ParserConfigurationException, SAXException, IOException {

        final String earPath = System.getProperty("earPath");
        final String kiwiDirPath = earPath + File.separator + "KiWi.ear";
        persistenceCfgFile =
                kiwiDirPath + File.separator + "KiWi.jar" + File.separator
                        + "META-INF" + File.separator + "persistence.xml";

        databaseParser = buildDatabaseParser(persistenceCfgFile);
        if (database != null && databaseParser != null) {
            databaseParser.setActualDatabase(database);
        }

        jdbcCfgFile = earPath + File.separator + "KiWi-dev-ds.xml";
        new File(jdbcCfgFile).exists();
        jdbcParser = buildJDBCParser(jdbcCfgFile);
    }

    private JDBCParser buildJDBCParser(String pathName)
            throws ParserConfigurationException, SAXException, IOException {

        final File file = new File(pathName);
        final boolean fileExists = file.exists();
        isDataSourceceDefault = !fileExists;
        final ClassLoader classLoader = getClass().getClassLoader();
        // if the KiWi is not installed the the default
        // application is loaded.
        final InputStream input =
                fileExists ? new BufferedInputStream(new FileInputStream(
                        pathName)) : classLoader
                        .getResourceAsStream(DEFAULT_DATASOURCE_FILE_NAME);

        final Document document = XMLUtil.getDocument(input);
        final JDBCParser result = new JDBCParser(document);

        return result;
    }

    private DatabaseParser buildDatabaseParser(String pathName)
            throws ParserConfigurationException, SAXException, IOException {

        final File file = new File(pathName);
        final boolean fileExists = file.exists();
        isPresistenceDefault = !fileExists;
        final ClassLoader classLoader = getClass().getClassLoader();
        // if the KiWi is not installed the the default
        // application is loaded.
        final InputStream input =
                fileExists ? new BufferedInputStream(new FileInputStream(
                        pathName)) : classLoader
                        .getResourceAsStream(DEFAULT_PERSISTENCE_FILE_NAME);

        final Document document = XMLUtil.getDocument(input);
        final DatabaseParser result = new DatabaseParser(document);
        return result;
    }

    public void setDatabaseSystem(String databaseName) {
        if (!databaseParser.getActualDatabase().getName().equals(databaseName)) {
            databseSysyemChanged = true;
        }
        databaseParser.setActualDatabaseAsString(databaseName);

        // FIXME : this logic must be moved on parser
        final DatabaseSystem actDB = databaseParser.getActualDatabase();

        final String kiWiConnectionURL = jdbcParser.getKiWiConnectionURL();
        final String newKiWiURL =
                DatabaseSystem.getdatabaseURL(actDB, kiWiConnectionURL);
        jdbcParser.setKiWiConnectionURL(newKiWiURL);

        final String ceqConnectionURL = jdbcParser.getCEQConnectionURL();
        final String newCEQURL =
                DatabaseSystem.getdatabaseURL(actDB, ceqConnectionURL);
        jdbcParser.setCEQConnectionURL(newCEQURL);
    }

    public String getDatabaseSystem() {

        if (databaseParser == null) {
            return "Not Installed";
        }

        final DatabaseSystem actualDatabase =
                databaseParser.getActualDatabase();
        return actualDatabase.getName();
    }

    public List<String> getDatabaseSystems() {
        final List<String> result = new ArrayList<String>();
        for (final DatabaseSystem databaseSystem : DatabaseSystem.values()) {
            result.add(databaseSystem.getName());
        }

        return result;
    }

    /**
     * @return the workDir
     */
    public String getWorkDir() {

        if (databaseParser == null) {
            return "Not Installed";
        }

        return databaseParser.getWorkDir();
    }

    /**
     * @param workDir the workDir to set
     */
    public void setWorkDir(String workDir) {

        if (databaseParser == null) {
            // just to avoid any possible problems
            return;
        }

        databaseParser.setWorkDir(workDir);
        createDirs(workDir);
    }

    /**
     * @return the solrHome
     */
    public String getSolrHome() {

        if (databaseParser == null) {
            return "Not Installed";
        }

        return databaseParser.getSolrHome();
    }

    /**
     * @param solrHome the solrHome to set
     */
    public void setSolrHome(String solrHome) {

        if (databaseParser == null) {
            // just to avoid any possible problems
            return;
        }

        databaseParser.setSolrHome(solrHome);
        createDirs(solrHome);
    }

    /**
     * @return the triplestoreDir
     */
    public String getTriplestoreDir() {

        if (databaseParser == null) {
            return "Not Installed";
        }

        return databaseParser.getTriplestoreDir();
    }

    public void setTriplestoreDir(String dir) {

        if (databaseParser == null) {
            // just to avoid any possible problems
            return;
        }

        databaseParser.setTriplestoreDir(dir);

        createDirs(dir);
    }

    private void createDirs(String dir) {
        final File file = new File(dir);
        final boolean exists = file.exists();
        if (!exists) {
            file.mkdirs();
        }
    }

    public void storeChages() throws IOException {

        if (isDataSourceceDefault && isPresistenceDefault) {
            return;
        }

        final DatabaseSystem actualDatabase =
                databaseParser.getActualDatabase();

        // FIXME : here is a bug I can not change in the same
        // time the db type and the b param :)
        if (databseSysyemChanged && !isPresistenceDefault) {
            final InputStream persXMLInput =
                    getClass().getClassLoader().getResourceAsStream(
                            actualDatabase + ".persisntece.xml");
            JarUtil.copy(persXMLInput, persistenceCfgFile);

            final InputStream dsXMLInput =
                    getClass().getClassLoader().getResourceAsStream(
                            actualDatabase + ".ds.xml");
            JarUtil.copy(dsXMLInput, jdbcCfgFile);

            try {
                init(actualDatabase);
            } catch (final Exception e) {
                // TODO use your own exception here
                throw new RuntimeException(e);
            }
        }

        if (!isPresistenceDefault) {
            databaseParser.persist(persistenceCfgFile);
        }

        if (!isDataSourceceDefault) {
            jdbcParser.persist(jdbcCfgFile);
        }

    }

    public String getKiWiURL() {

        if (databaseParser == null) {
            return "Not Installed";
        }

        return jdbcParser.getKiWiConnectionURL();
    }

    public String getCEQURL() {

        if (databaseParser == null) {
            return "Not Installed";
        }

        return jdbcParser.getCEQConnectionURL();
    }

    public String getKiWiUser() {

        if (databaseParser == null) {
            return "Not Installed";
        }

        return jdbcParser.getKiWiUser();
    }

    public void setKiWiUser(String user) {
        jdbcParser.setKiWiUser(user);
    }

    public String getKiWiPassword() {

        if (databaseParser == null) {
            return "Not Installed";
        }

        return jdbcParser.getKiWiPassword();
    }

    public void setKiWiPassword(String passwd) {
        jdbcParser.setKiWiPassword(passwd);
    }

    public String getCEQUser() {

        if (databaseParser == null) {
            return "Not Installed";
        }

        return jdbcParser.getCEQUser();
    }

    public void setCEQUser(String user) {
        jdbcParser.setCEQUser(user);
    }

    public String getCEQPassword() {

        if (databaseParser == null) {
            return "Not Installed";
        }

        return jdbcParser.getCEQPassword();
    }

    public void setCEQPassword(String passwd) {
        jdbcParser.setCEQPassword(passwd);
    }

    public String getKiWiConnectionURL() {

        if (databaseParser == null) {
            return "Not Installed";
        }

        return jdbcParser.getKiWiConnectionURL();
    }

    public boolean getCanConnectToDatabase() {
        if (jdbcParser == null) {
            // if the jdbcParser is null then KiWi is not
            // installed - and the configuration files are not
            // existing.
            return false;
        }

        final String url = jdbcParser.getKiWiConnectionURL();
        final String driver = jdbcParser.getKiWiDriver();
        final String user = jdbcParser.getCEQUser();
        final String password = jdbcParser.getKiWiPassword();

        final boolean canConnectTo =
                JDBCUtil.canConnectTo(url, driver, user, password);
        return canConnectTo;
    }

    public String getKiWiDatabaseName() {

        if (jdbcParser == null) {
            return "Not Installed";
        }

        final String url = jdbcParser.getKiWiConnectionURL();
        final String driver = jdbcParser.getKiWiDriver();
        final String result = getDatabaseName(url, driver);

        return result;
    }

    private String getDatabaseName(final String url, final String driver) {
        final DatabaseSystem databaseForDriver =
                DatabaseSystem.getDatabaseForDriver(driver);
        final String result;
        switch (databaseForDriver) {
            case POSTGRESS:
                result = url.substring(url.lastIndexOf(":") + 1);
                break;
            case MYSQL:
                // fall through
            case HYPERDRIVE:
                result = url.substring(url.lastIndexOf("/") + 1);
                break;

            default:
                result = null;
                break;
        }
        return result;
    }

    public void setKiWiDatabaseName(String databaseName) {
        final DatabaseSystem actualDatabase =
                databaseParser.getActualDatabase();
        final String url =
                DatabaseSystem.getdatabaseURL(actualDatabase, databaseName);
        jdbcParser.setKiWiConnectionURL(url);
    }

    public String getCEQDatabaseName() {
        final String url = jdbcParser.getCEQConnectionURL();
        final String driver = jdbcParser.getCEQDriver();

        final String result = getDatabaseName(url, driver);

        return result;

    }

    public void setCEQDatabaseName(String databaseName) {
        final DatabaseSystem actualDatabase =
                databaseParser.getActualDatabase();
        final String url =
                DatabaseSystem.getdatabaseURL(actualDatabase, databaseName);
        jdbcParser.setCEQConnectionURL(url);
    }

    @Observer(KiWiSetupConstants.INSTALL_DONE)
    public void installDone() {
        isDataSourceceDefault = false;
        isPresistenceDefault = false;
        try {
            storeChages();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
