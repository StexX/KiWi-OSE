/*
 * File : ImportSunSpaceDataWebService.java.java
 * Date : May 16, 2010
 *
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2008 The KiWi Project. All rights reserved.
 * http://www.kiwi-project.eu
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  KiWi designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 *
 * Contributor(s):
 */


package kiwi.admin.action;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import kiwi.admin.api.sunspace.SunSpaceImportService;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.jboss.seam.log.Log;


/**
 * Web service used to trigger the sun space data import.
 *
 * @author mradules
 * @version 0.9
 * @since 0.9
 */
@Name("kiwi.admin.action.ImportSunSpaceDataWebService")
@Scope(ScopeType.STATELESS)
@Path("/sunspace/importdata")
public class ImportSunSpaceDataWebService {

    @Logger
    private Log log;

    /**
     * Starts the sun space data import in an asynchronous
     * thread.
     *
     * @param path the path which contains the xml sun space data
     *            dump. The path must point to a directory which
     *            contains following files : INFORMATION.xml,
     *            INFO_KEYWORDS.xml, KEYWORD.xml, LOG.xml and
     *            PERSON.xml, otherwise the method has no effect.
     */
    @GET
    @Path("/startImport")
    public void startImport(@QueryParam("localPath") String path) {

        // FIXME : use constant instead of plain strings
        final String personFile = path + File.separator + "PERSON.xml";
        final String logFile = path + File.separator + "LOG.xml";
        final String informationFile =
                path + File.separator + "INFORMATION.xml";
        final String keywordFile = path + File.separator + "KEYWORD.xml";
        final String infoKeywordFile =
                path + File.separator + "INFO_KEYWORDS.xml";

        final String spaceFile = path + File.separator + "SPACE.xml";
        final String spaceKeywordFile =
                path + File.separator + "SPACE_KEYWORD.xml";
        final String spaceInformationFile =
                path + File.separator + "SPACE_INFORMATION.xml";
        final String spacePersonFile =
                path + File.separator + "SPACE_PERSON.xml";

        if (!new File(personFile).exists() || !new File(logFile).exists()
                || !new File(informationFile).exists()
                || !new File(keywordFile).exists()
                || !new File(infoKeywordFile).exists()) {
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
        files.put(SunSpaceImportService.FILE.SPACE_INFO_FILE,
                spaceInformationFile);
        files.put(SunSpaceImportService.FILE.SPACE_KEYWORD_FILE,
                spaceKeywordFile);
        files.put(SunSpaceImportService.FILE.SPACE_PERSON_FILE, spacePersonFile);

    Events.instance().raiseAsynchronousEvent("importFiles", files);
    }
}
