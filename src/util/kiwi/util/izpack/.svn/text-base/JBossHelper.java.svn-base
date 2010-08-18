/*
 * File : JBossHelper.java.java
 * Date : Aug 2, 2010
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


package kiwi.util.izpack;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;


/**
 * @author mradules
 * @version 0.9
 * @since 0.9
 */
final class JBossHelper {
	
    private final static Logger LOG = Logger.getLogger(JBossHelper.class.getName());

    static void cleanJBoss(String jBossDir) {
        final List<String> toRemove = new ArrayList<String>();
        toRemove.add("hibernate3.jar");
        toRemove.add("lf4j-api-1.5.2.jar");
        toRemove.add("slf4j-log4j12-1.5.2.jar");
        toRemove.add("postgresql-8.3dev-602.jdbc3.jar");
        toRemove.add("oscache-2.1.jar");

        final String serverDefaultLibDir = getServerDefaultLibDir(jBossDir);
        LOG.finest("Tries to remove :" + toRemove + " from " + serverDefaultLibDir);
        for (final String resource : toRemove) {
            final String path = serverDefaultLibDir + resource;
            final File toDelete = new File(path);
            if (toDelete.exists()) {
    			final boolean wasDelete = toDelete.delete();
    			final String deleteMsg = wasDelete ? " successfully" : "fails";
    			LOG.info("Delete of " + toDelete + " was " + deleteMsg);
            } else {
            	LOG.warning("The File : " + toDelete 
            			+ " can not be deleted, this file does not exit.");
            }
        }
    }

    static String getServerDefaultLibDir(String jBossDir) {
        final StringBuffer result = new StringBuffer();
        result.append(jBossDir);
        result.append(File.separator);
        result.append("server");
        result.append(File.separator);
        result.append("default");
        result.append(File.separator);
        result.append("lib");
        result.append(File.separator);

        return result.toString();
    }

    static String getDataSourceFilepath(String jBossDir) {
        final String deployDir = getServerDefaultDeployDir(jBossDir);
        final StringBuffer result = new StringBuffer(deployDir);
        result.append("KiWi-dev-ds.xml");

        return result.toString();
    }

    static String getServerDefaultDeployDir(String jBossDir) {
        final StringBuffer result = new StringBuffer();
        result.append(jBossDir);
        result.append(File.separator);
        result.append("server");
        result.append(File.separator);
        result.append("default");
        result.append(File.separator);
        result.append("deploy");
        result.append(File.separator);

        return result.toString();
    }

    static String getPersistenceXMLpath(String jBossDir) {
        final String kiWiDeployPath = getKiWiDeployPath(jBossDir);
        final StringBuffer result = new StringBuffer(kiWiDeployPath);
        result.append("KiWi.jar");
        result.append(File.separator);
        result.append("META-INF");
        result.append(File.separator);
        result.append("persistence.xml");
        return result.toString();
    }

    static String getKiWiDeployPath(String jBossDir) {
        final StringBuffer result = new StringBuffer();
        result.append(jBossDir);
        result.append(File.separator);
        result.append("server");
        result.append(File.separator);
        result.append("default");
        result.append(File.separator);
        result.append("deploy");
        result.append(File.separator);
        result.append("KiWi.ear");
        result.append(File.separator);

        return result.toString();
    }

    static void processPersistence(final File defaulPersitence,
            final String jBossHome, final String kiwiHome, final String dbType)
            throws ParserConfigurationException, SAXException, IOException {

        final InputStream asStream = new FileInputStream(defaulPersitence);

        final Document document = XMLUtil.getDocument(asStream);
        final DatabaseParser parser = new DatabaseParser(document);
        final DatabaseSystem database =
                DatabaseSystem.getDatabaseForName(dbType);
        parser.setActualDatabase(database);

        parser.setWorkDir(kiwiHome);
        parser.setSolrHome(kiwiHome + File.separator + "solr");
        // mihai : I am not sure if teh dir must be the same for
        // semantic vectors and sorl
        parser.setSemanticVectorDir(kiwiHome + File.separator + "solr");
        parser.setTriplestoreDir(kiwiHome + File.separator + "triples");

        final String persistenceXMLpath = getPersistenceXMLpath(jBossHome);
        parser.persist(persistenceXMLpath);
    }

    static void processDS(final File defaulDS, final String jBossHome,
            final String dbType, final String dbName, final String dbHost,
            final String dbPort, final String dbUser, final String dbUserPasswd)
            throws ParserConfigurationException, SAXException, IOException {

        final InputStream dsAsStream = new FileInputStream(defaulDS);

        final Document dsDocument = XMLUtil.getDocument(dsAsStream);
        final JDBCParser jdbcParser = new JDBCParser(dsDocument);
        jdbcParser.setKiWiUser(dbUser);
        jdbcParser.setKiWiPassword(dbUserPasswd);

        final DatabaseSystem database =
                DatabaseSystem.getDatabaseForName(dbType);
        final String driverClassName = database.getDriverClassName();
        jdbcParser.setKiWiDriver(driverClassName);
        final String url =
                DatabaseSystem.getdatabaseURL(database, dbName, dbHost, dbPort);
        jdbcParser.setKiWiConnectionURL(url);
        final String dataSourceFilepath = getDataSourceFilepath(jBossHome);
        jdbcParser.persist(dataSourceFilepath);
    }

}
