

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


@Name("kiwiApplicationSetupAction")
@Scope(ScopeType.APPLICATION)
@AutoCreate
public class KiWiApplicationSetupAction {

    private static final String DEFAULT_APPLICATION_FILE_NAME =
            "default.application.xml";

    private static ApplicationsParser applicationsParser;

    private String kiwiDirPath;

    private List<String> selectedItems;

    private boolean isDefault;

    @Create
    public void init() {
        final String earPath = System.getProperty("earPath");
        kiwiDirPath = earPath + File.separator + "KiWi.ear";
        final String applicationPath =
                kiwiDirPath + File.separator + "META-INF" + File.separator
                        + "application.xml";

        try {
            final Document document = getInputStream(applicationPath);
            applicationsParser = new ApplicationsParser(document);
            selectedItems = applicationsParser.getApplicationNames();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private Document getInputStream(final String applicationPath)
            throws IOException, ParserConfigurationException, SAXException {

        final File applicationFile = new File(applicationPath);
        final boolean fileExists = applicationFile.exists();

        isDefault = !fileExists;

        final ClassLoader classLoader = getClass().getClassLoader();
        // if the KiWi is not installed the the default
        // application is loaded.
        final InputStream input =
                fileExists ? new BufferedInputStream(new FileInputStream(
                        applicationFile)) : classLoader
                        .getResourceAsStream(DEFAULT_APPLICATION_FILE_NAME);

        final Document document = XMLUtil.getDocument(input);
        return document;
    }

    /**
     * @return the applications
     */
    public List<String> getApplications() {

        if (applicationsParser == null) {
            return new ArrayList<String>();
        }

        return applicationsParser.getApplicationNames();
    }

    public void setApplications(final List<String> application) {
        // UNIMPLEMENTED
    }

    public void setSelectedItems(List<String> selItems) {

        if (applicationsParser == null) {
            return;
        }

        applicationsParser.setSelectedApplication(selItems);
    }

    public List<String> getSelectedItems() {

        if (applicationsParser == null) {
            return new ArrayList<String>();
        }

        return selectedItems;
    }

    public void storeChages() {
        if (applicationsParser == null) {
            return;
        }

        if (isDefault) {
            return;
        }

        final String applicationPath =
                kiwiDirPath + File.separator + "META-INF" + File.separator
                        + "application.xml";

        try {
            applicationsParser.perist(applicationPath);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void restoreDefault() {
        // I must think over the default values.
    }

    @Observer(KiWiSetupConstants.INSTALL_DONE)
    public void installDone() {
        isDefault = false;
        storeChages();
    }
}
