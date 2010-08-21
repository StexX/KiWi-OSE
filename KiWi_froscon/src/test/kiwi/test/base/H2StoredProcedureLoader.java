/*
 * File : SoredProcedureLoader.java.java
 * Date : May 4, 2010
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


package kiwi.test.base;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import kiwi.util.JDBCUtils;

import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;


/**
 * Used to load the H2 stored procedures required for the equity
 * computing. <br>
 * This class can not be instantiated or extended. <br>
 * The usage is simple the only one default accessible method
 * <code>loadH2Soredprocedures</code> load all the requird stored
 * procedures and register them in the underlying database
 * system. This has tow main pre-conditions :
 * <ol>
 * <li>the underlying database system must be H2.
 * <li>all the stored procedures must be H2 specific and they
 * must be placed in the classpath.
 * </ol>
 *
 * @author mradules
 * @version 0.9
 * @since 0.9
 */
final class H2StoredProcedureLoader {

    /**
     * String that represents the ALIAS h2 SQL statement.
     */
    private static final String ALIAS = "ALIAS";

    /**
     * The only one instance for this class.
     */
    private static final H2StoredProcedureLoader LOADER =
            new H2StoredProcedureLoader();

    private static final Log LOG = Logging
            .getLog(H2StoredProcedureLoader.class);

    /**
     * Don't let anybody to instantiate this class.
     */
    private H2StoredProcedureLoader() {
        // UNIMPLEMENTED
    }

    /**
     * Loads and register in the underlying database all the
     * stored procedures required for the Community Equity
     * computing.
     */
    static void loadH2SoredProcedures() {
        LOADER.loadSoredProcedures();
    }

    /**
     * Loads and register in the underlying database all the
     * stored procedures required for the Community Equity
     * computing.
     */
    private void loadSoredProcedures() {
        final List<String> procedureNames = getProcedureNames();
        final List<String> procedures = getProcedures(procedureNames);
        final Log log = Logging.getLog(this.getClass());
        for (final String procedure : procedures) {
            try {
                process(procedure);
            } catch (final Exception e) {
                // It is not recomandable to catch all exception
                // but in this case I want to be sure that all
                // procedures are loaded,
                // even if by some an exception may occurs
                // when I load one the other are still load.
                // I log it anyway.
                e.printStackTrace();
                log.debug("Stored procedure #0 can not be processed.",
                        procedure);
                log.warn(e.getMessage(), e);
            }
        }
    }

    /**
     * Returns a list which contains the storing procedure names.
     * Please note the result is a List - so the order has a
     * role.
     *
     * @return a list which contains all stored procedure name.
     */
    private List<String> getProcedureNames() {
        final List<String> resourceNames = new ArrayList<String>();

        resourceNames.add("addtypeAging.sql");
        resourceNames.add("annotateAging.sql");
        resourceNames.add("commentAging.sql");
        resourceNames.add("createAging.sql");
        resourceNames.add("deleteAging.sql");
        resourceNames.add("editAging.sql");
        resourceNames.add("shareAging.sql");
        resourceNames.add("tweetAging.sql");
        resourceNames.add("visitAging.sql");
        resourceNames.add("generalAging.sql");
        resourceNames.add("calculate_equityfor.sql");
        resourceNames.add("calculate_equityfornow.sql");
        return resourceNames;
    }

    /**
     * Loads using the classloader the given resource and returns
     * its content like a string.
     *
     * @param procName the resource name.
     * @return the content for the given resource (as a String).
     * @throws IOException by any IO related errors.
     */
    private String getProcedureContent(String procName) throws IOException {
        final ClassLoader classLoader = this.getClass().getClassLoader();
        final InputStream in = classLoader.getResourceAsStream(procName);
        final InputStreamReader inReader = new InputStreamReader(in);
        final BufferedReader reader = new BufferedReader(inReader);
        String readLine = reader.readLine();
        final StringBuffer result = new StringBuffer();
        while (readLine != null) {
            result.append(readLine);
            readLine = reader.readLine();
        }

        return result.toString();
    }

    /**
     * Loads (using the classloader) the content for all the
     * specified resources, if a resource can not be loaded from
     * any reason then the resource will be skipped.
     *
     * @param resourceNames the name for all the resources to be
     *            loaded.
     * @return the content (like string) for all the given
     *         resources.
     */
    private List<String> getProcedures(List<String> resourceNames) {

        final List<String> result = new ArrayList<String>();
        for (final String resourceName : resourceNames) {
            try {
                final String procedureContent =
                        getProcedureContent(resourceName);
                result.add(procedureContent);
            } catch (final IOException e) {
                LOG.warn("Stored procedure #0 can not be processed.",
                        resourceName);
                LOG.warn(e.getMessage(), e);
            }
        }
        return result;
    }

    /**
     * Register the given stored procedure to the database
     * specified in the constructor.
     *
     * @param procedure to store, it can not be null or empty
     *            string.
     * @throws SQLException by any kind of database related
     *             exception. Most of the times it wraps the real
     *             cause for the exception, this cause can be
     *             obtained by using the <code>getCause()</code>
     *             method.
     * @throws NamingException if the default data source can not
     *             be located in the ENC,
     * @throws NullPointerException if the <code>procedure</code>
     *             argument is null.
     * @throws IllegalArgumentException if the
     *             <code>procedure</code> argument is an empty
     *             string.
     */
    private void process(final String procedure) throws NamingException,
            SQLException {
        final String procedureName = getProcedureName(procedure);
        LOG.debug("Try to process : #0 ", procedureName);
        removeProcedure(procedureName);
        JDBCUtils.execute(procedure);
        LOG.debug("Procedure #0 was process", procedureName);
    }

    /**
     * Extracts the stored procedure name for a given stored
     * procedure.
     *
     * @param procedure the stored procedure, it must be valid
     *            one.
     * @return the name for the given stored procedure.
     * @throws IllegalArgumentException if the
     *             <code>procedure</code> is not a valid H2
     *             stored procedure.
     */
    private String getProcedureName(String procedure) {
        final int aliasIndexOf = procedure.indexOf(ALIAS);
        if (aliasIndexOf < 0) {
            throw new IllegalArgumentException("The [ " + procedure
                    + "] is not a valid h2 storing procedure.");
        }

        final int startIndex = aliasIndexOf + ALIAS.length();
        final int endIndex = procedure.indexOf(" ", startIndex + 1);
        final String result = procedure.substring(startIndex, endIndex);
        return result;
    }

    /**
     * Removes an existing stored procedure from the underlying
     * database, the procedure is identified after its name.
     *
     * @param procedure the name for the stored procedure to
     *            remove.
     */
    private void removeProcedure(String procedure) {
        try {
            final String dropAlias = "DROP ALIAS " + procedure.trim();
            JDBCUtils.execute(dropAlias);
        } catch (final Exception e) {
            // I don't care if I can not remove a procedure.
            // can be that the procedure already exists, hihihi.
        }
    }
}
