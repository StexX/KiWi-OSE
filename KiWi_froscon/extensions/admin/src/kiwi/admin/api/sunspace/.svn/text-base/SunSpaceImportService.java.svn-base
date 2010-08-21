/*
 * File : EquityLogImportService.java
 * Date : Apr 8, 2010
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


package kiwi.admin.api.sunspace;


import java.util.Map;


/**
 * Defined the Sun Space import service, this service is used to
 * import activities (and other related information) from an
 * external source. the file format or any other file related
 * details are not defined here (are defined in the
 * implementation).
 *
 * @author mradules
 * @version 0.7
 * @since 0.7
 */
public interface SunSpaceImportService {

    /**
     * All the allowed key values for the import files map.
     */
    enum FILE {
        LOG_FILE, PERSON_FILE, INFORMATION_FILE, KEYWORD_FILE, INFO_KEYWORDFILE, SPACE_FILE, SPACE_INFO_FILE, SPACE_KEYWORD_FILE, SPACE_PERSON_FILE;
    }

    /**
     * FIXME : to many parameters.<br>
     * Parses and stores the information from the given XML
     * files.
     *
     * @param logFile the path to the file contains sun space Log
     *            related information. The sun space log is
     *            equivalent with the kiwi activity stream.
     * @param personFile the path to the file contains sun space
     *            person related information.
     * @param informationFile the path to the file contains sun
     *            space information items, an information item is
     *            equivalent with the content item in kiwi.
     * @param keywordFile the path to the file contains sun space
     *            keyword information, the keyword are equivalent
     *            with tags in kiwi.
     * @param infoKeywordFile the path to the file contains the
     *            relation between a person an information and a
     *            keyword.
     * @throws ImportException by any kind of problems.
     */
    void importFiles(Map<FILE, String> files, long logs, long spaces, long tags)
            throws ImportException;
// void importFiles(String logFile, String personFile, String
// informationFile,
// String keywordFile, String infoKeywordFile, String
// sunSpaceFile,
// String sunSpaceInfoFile, String sunSpaceKeywordFile,
// String sunSpacePersonFile)
// throws ImportException;
}
