/**
 *
 */


package kiwi.wsetup;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;


/**
 * @author mradules
 */
@Name("generalSetupAction")
@Scope(ScopeType.APPLICATION)
@AutoCreate
public class GeneralSetupAction {

    private static final String KIWI_DEFAULT_DS_FILE = "KiWi-dev-ds.xml";

    private static final String KIWI_SERVICE_FILE_NAME =
            "KiWi-destinations-service.xml";

    private String serverPath;

    private String earPath;

    private String kiwiDirPath;

    private boolean isKiwiInstalled;

    @Create
    public void init() {
        earPath = System.getProperty("earPath");
        kiwiDirPath = earPath + File.separator + "KiWi.ear";
        final File kiwiDir = new File(kiwiDirPath);
        isKiwiInstalled = kiwiDir.exists();
    }

    /**
     * @return the serverPath
     */
    public String getServerPath() {
        return serverPath;
    }

    /**
     * @param serverPath the serverPath to set
     */
    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
    }

    /**
     * @return the earPath
     */
    public String getEarPath() {
        return earPath;
    }

    /**
     * @param earPath the earPath to set
     */
    public void setEarPath(String earPath) {
        this.earPath = earPath;
    }

    /**
     * @return the kiwiDirPath
     */
    public String getKiwiDirPath() {
        return kiwiDirPath;
    }

    /**
     * @param kiwiDirPath the kiwiDirPath to set
     */
    public void setKiwiDirPath(String kiwiDirPath) {
        this.kiwiDirPath = kiwiDirPath;
    }

    /**
     * @return the isKiwiInstalled
     */
    public boolean isKiwiInstalled() {
        return isKiwiInstalled;
    }

    /**
     * @return the isKiwiInstalled
     */
    public boolean getIsKiwiInstalled() {
        return isKiwiInstalled;
    }

    /**
     * @param isKiwiInstalled the isKiwiInstalled to set
     */
    public void setKiwiInstalled(boolean isKiwiInstalled) {
        this.isKiwiInstalled = isKiwiInstalled;
    }

    /**
     * Install the KiWi ear in the directory where the
     * <code>kiwiDirPath</code> points. If the KiWi is already
     * install (and the method <code>isKiwiInstalled</code>
     * returns true) then this method has no effect.
     * 
     * @throws IOException by any IO related errors.
     * @see #kiwiDirPath
     * @see #isKiwiInstalled()
     */
    public void install() throws IOException {

        final File kiwiInstallDir = new File(kiwiDirPath);
        kiwiInstallDir.exists();
        if (!isKiwiInstalled) {
            kiwiInstallDir.mkdirs();
        } else {
            return;
        }

        final ClassLoader classLoader = getClass().getClassLoader();
        final InputStream kiwiJar = classLoader.getResourceAsStream("kiwi.ear");

        JarUtil.unjar(kiwiJar, kiwiDirPath);

        // the KiWi-destinations-service.xml contains MBean
        // configuration for the reasoner.
        final String servicesFilePath = earPath + KIWI_SERVICE_FILE_NAME;
        final InputStream servicesInput =
                classLoader.getResourceAsStream(KIWI_SERVICE_FILE_NAME);
        JarUtil.copy(servicesInput, servicesFilePath);

        final String defaultDSFilePath = earPath + KIWI_DEFAULT_DS_FILE;
        // The hyperdrive is the default database - copy the DS
        final InputStream defaultDSInput =
                classLoader.getResourceAsStream("HYPERDRIVE.ds.xml");
        JarUtil.copy(defaultDSInput, defaultDSFilePath);

        // The hyperdrive is the default database - copy the DS
        final String defaultPersFilePath =
                kiwiDirPath + File.separator + "KiWi.jar" + File.separator
                        + "META-INF" + File.separator + "persistence.xml";
        final InputStream defaultPersXMLInput =
                classLoader.getResourceAsStream("HYPERDRIVE.persisntece.xml");
        JarUtil.copy(defaultPersXMLInput, defaultPersFilePath);

        // anouce every body about the succesfull install.
        Events.instance().raiseEvent(KiWiSetupConstants.INSTALL_DONE);
    }
}
