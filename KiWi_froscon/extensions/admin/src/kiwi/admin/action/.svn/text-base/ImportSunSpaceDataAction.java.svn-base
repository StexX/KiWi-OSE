/*
 * File : ImportSunSpaceDataAction.java
 * Date : Apr 7, 2010
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * Copyright 2008 The KiWi Project.
 * All rightsreserved. http://www.kiwi-project.eu
 * The contents of this file are subject to
 * the terms of either the GNU General Public License Version 2 only ("GPL") or
 * the Common Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the License.
 * You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html or nbbuild/licenses/CDDL-GPL-2-CP.
 * See the License for the specific language governing permissions and
 * limitations under the License. When distributing the software, include this
 * License Header Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP. KiWi designates this particular file as
 * subject to the "Classpath" exception as provided by Sun in the GPL Version 2
 * section of the License file that accompanied this code. If applicable, add
 * the following below the License Header, with the fields enclosed by brackets
 * [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]" If you wish your
 * version of this file to be governed by only the CDDL or only the GPL Version
 * 2, indicate your decision by adding "[Contributor] elects to include this
 * software in this distribution under the [CDDL or GPL Version 2] license." If
 * you do not indicate a single choice of license, a recipient has the option to
 * distribute your version of this file under either the CDDL, the GPL Version 2
 * or to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL Version
 * 2 license, then the option applies only if the new code is made subject to
 * such option by the copyright holder. Contributor(s):
 */


package kiwi.admin.action;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

import kiwi.admin.api.sunspace.SunSpaceImportService;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;


/**
 * Collects the user input and triggers the Sun Space import. <br>
 * The usage is simple the action contains only one property, the
 * path for the the import directory. This directory must contain
 * five files named PERSON.xml, LOG.xml, INFORMATION.xml,
 * KEYWORD.xml and INFO_KEYWORDS.xml. <b>otherwise the import
 * will not starts</b>.<br>
 * The name for this component is "importSunSpaceDataAction".
 *
 * @author mradules
 * @version 0.7
 * @since 0.7
 */
@Name("importSunSpaceDataAction")
public class ImportSunSpaceDataAction {

    /**
     * All the log massages goes trough this field.
     */
    @Logger
    private Log log;

    /**
     * Sends messages to the UI.
     */
    @In
    private FacesMessages facesMessages;

    /**
     * The directory which contains the data dump files.
     */
    private String importDir;

    private long logsLimit;

    private boolean allLogs = true;

    private long spacesLimit;

    private boolean allSpaces = true;

    private long tagsLimit;

    private boolean allTags = true;

    private boolean importRuns;

    /**
     * Builds an <code>ImportSunSpaceDataAction</code> instance.
     */
    public ImportSunSpaceDataAction() {
        // UNIMPLEEMNTED
    }

    /**
     * Returns the path for the import directory, the import
     * directory must contains the data dump files.
     *
     * @return the path for the import directory, the import
     *         directory must contains the data dump files.
     */
    public String getImportDir() {
        return importDir;
    }

    /**
     * Registers a new value for the path for the import
     * directory, the import directory must contains the data
     * dump files.
     *
     * @param importDir the new value path, it can not be null.
     */
    public void setImportDir(String importDir) {
        this.importDir = importDir;
    }

    /**
     * Import (parse and persist) a set of given data dump files.
     */
    public void importFiles() {

        if (!new File(importDir).exists()) {
            facesMessages.add("The #0 is not exisits.", importDir);
            return;
        }

        // FIXME : use constant instead of plain strings
        final String personFile = importDir + File.separator + "PERSON.xml";
        if (!new File(personFile).exists()) {
            facesMessages.add("The #0 is not exisits.", personFile);
            return;
        }

        final String logFile = importDir + File.separator + "LOG.xml";
        if (!new File(logFile).exists()) {
            facesMessages.add("The #0 is not exisits.", logFile);
            return;
        }

        final String informationFile =
                importDir + File.separator + "INFORMATION.xml";
        if (!new File(informationFile).exists()) {
            facesMessages.add("The #0 is not exisits.", informationFile);
            return;
        }

        final String keywordFile = importDir + File.separator + "KEYWORD.xml";
        if (!new File(keywordFile).exists()) {
            facesMessages.add("The #0 is not exisits.", keywordFile);
            return;
        }

        final String infoKeywordFile =
                importDir + File.separator + "INFO_KEYWORDS.xml";
        if (!new File(infoKeywordFile).exists()) {
            facesMessages.add("The #0 is not exisits.", infoKeywordFile);
            return;
        }

        final String spaceFile = importDir + File.separator + "SPACE.xml";
        if (!new File(spaceFile).exists()) {
            facesMessages.add("The #0 is not exisits.", spaceFile);
            return;
        }

        final String spaceKeywordFile =
                importDir + File.separator + "SPACE_KEYWORD.xml";
        if (!new File(spaceKeywordFile).exists()) {
            facesMessages.add("The #0 is not exisits.", spaceKeywordFile);
            return;
        }

        final String spaceInformationFile =
                importDir + File.separator + "SPACE_INFORMATION.xml";
        if (!new File(spaceInformationFile).exists()) {
            facesMessages.add("The #0 is not exisits.", spaceInformationFile);
            return;
        }

        final String spacePersonFile =
                importDir + File.separator + "SPACE_PERSON.xml";
        if (!new File(spacePersonFile).exists()) {
            facesMessages.add("The #0 is not exisits.", spacePersonFile);
            return;
        }

        log.debug("Import files : #0, #1, #2 starts.", logFile, personFile,
                informationFile);

        final Map<SunSpaceImportService.FILE, String> files =
                new HashMap<SunSpaceImportService.FILE, String>();
        files.put(SunSpaceImportService.FILE.LOG_FILE, logFile);
        files.put(SunSpaceImportService.FILE.PERSON_FILE, personFile);
        files.put(SunSpaceImportService.FILE.INFORMATION_FILE, informationFile);
        // tags
        files.put(SunSpaceImportService.FILE.KEYWORD_FILE, keywordFile);
        files.put(SunSpaceImportService.FILE.INFO_KEYWORDFILE, infoKeywordFile);
        // communities
        files.put(SunSpaceImportService.FILE.SPACE_FILE, spaceFile);
        files.put(SunSpaceImportService.FILE.SPACE_INFO_FILE, spaceInformationFile);
        files.put(SunSpaceImportService.FILE.SPACE_KEYWORD_FILE, spaceKeywordFile);
        files.put(SunSpaceImportService.FILE.SPACE_PERSON_FILE, spacePersonFile);

        final long logs = allLogs ? -1 : logsLimit;
        final long spaces = allSpaces ? -1 : logsLimit;
        final long tags = allTags ? -1 : tagsLimit;

        facesMessages.add("The import process was scheduled, please wait.");
        Events.instance().raiseAsynchronousEvent("importFiles", files, logs,
                spaces, tags);
        importRuns = true;
    }

    public long getLogsLimit() {
        return logsLimit;
    }

    public void setLogsLimit(long logsLimit) {
        this.logsLimit = logsLimit;
    }

    public boolean isAllLogs() {
        return allLogs;
    }

    public void setAllLogs(boolean allLogs) {
        this.allLogs = allLogs;
    }

    public long getSpacesLimit() {
        return spacesLimit;
    }

    public void setSpacesLimit(long spacesLimit) {
        this.spacesLimit = spacesLimit;
    }

    public boolean isAllSpaces() {
        return allSpaces;
    }

    public void setAllSpaces(boolean allSpaces) {
        this.allSpaces = allSpaces;
    }

    public long getTagsLimit() {
        return tagsLimit;
    }

    public void setTagsLimit(long tagsLimit) {
        this.tagsLimit = tagsLimit;
    }

    public boolean isAllTags() {
        return allTags;
    }

    public void setAllTags(boolean allTags) {
        this.allTags = allTags;
    }

    public boolean isImportRuns() {
        return importRuns;
    }

    public void setImportRuns(boolean importRuns) {
        this.importRuns = importRuns;
    }
}
