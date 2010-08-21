/*
 * File : StoredProcedureLoader.java.java Date : Apr 1, 2010 DO NOT ALTER OR
 * REMOVE COPYRIGHT NOTICES OR THIS HEADER. Copyright 2008 The KiWi Project. All
 * rights reserved. http://www.kiwi-project.eu The contents of this file are
 * subject to the terms of either the GNU General Public License Version 2 only
 * ("GPL") or the Common Development and Distribution License("CDDL")
 * (collectively, the "License"). You may not use this file except in compliance
 * with the License. You can obtain a copy of the License at
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


package kiwi.service.equity;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;


/**
 * Used to load the stored procedure in to the underling
 * database.<br>
 * Your database must be able to handle stored procedures, if you
 * use postgresql you can use the tutorial from :
 * http://www.faqs.org/docs/ppbook/c19610.htm
 * 
 * @author mradules
 * @version 0.7
 * @since 0.7
 */
@Name("storedProcedureLoader")
@Scope(ScopeType.APPLICATION)
@AutoCreate
public class StoredProcedureLoader {

    @Logger
    private static Log log;

    /**
     * The driver class name for the underlying database.
     */
    private String driver;

    /**
     * The URL for the underlying database.
     */
    private String url;

    /**
     * The user for the underlying database.
     */
    private String user;

    /**
     * The password for the underlying database.
     */
    private String passwd;

    private StoringProcedureProcessor procedureProcessor;

    /**
     * Grabs the database information from the datasource file.
     * This method initializes the
     * 
     * @throws IOException
     */
    @Create
    public void init() throws IOException {
        final String earPath = System.getProperty("ear.path");
        final String kiwiPath =
                earPath.substring(0, earPath.indexOf("KiWi.ear"));
        final String kiwiDSPath = kiwiPath + "KiWi-dev-ds.xml";
        final List<String> lines = FileUtils.readLines(new File(kiwiDSPath));

        // mihai : I know this is not the nicest way to process
// xml files but it
        // the shortest :).
        for (final String line : lines) {
            final String trimLine = line.trim();

            if (trimLine.startsWith("<connection-url>")) {
                final int prefixLength = "<connection-url>".length();
                url =
                        trimLine.substring(prefixLength, trimLine.length()
                                - prefixLength - 1);
                continue;
            }

            if (trimLine.startsWith("<driver-class>")) {
                final int prefixLength = "<driver-class>".length();
                driver =
                        trimLine.substring(prefixLength, trimLine.length()
                                - prefixLength - 1);
                continue;

            }

            if (trimLine.startsWith("<user-name>")) {
                final int prefixLength = "<user-name>".length();
                user =
                        trimLine.substring(prefixLength, trimLine.length()
                                - prefixLength - 1);
                continue;

            }

            if (trimLine.startsWith("<password>")) {
                final int prefixLength = "<password>".length();
                passwd =
                        trimLine.substring(prefixLength, trimLine.length()
                                - prefixLength - 1);
                continue;
            }
        }

        if (url == null || driver == null || passwd == null || user == null) {
            throw new IllegalArgumentException(
                    "Some JDBS paramter are missing "
                            + "or they are malformed, check if your datasource file exits "
                            + "and/or it contains the right information.");
        }

        if (driver.contains("postgresql")) {
            procedureProcessor =
                    new PostgresqlProcedureProcessor(driver, url, user, passwd);
        } else if (driver.contains("h2")) {
            procedureProcessor =
                    new H2ProcedureProcessor(driver, url, user, passwd);
        } else if (driver.contains("mysql")) {

        } else if (driver.contains("oracle")) {

        } else {
            // FIXME : throw new Illegal...Excepition();
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
        resourceNames.add("calculate_equity_start.sql");
        resourceNames.add("select_equity_times.sql");
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
                log.warn("Stored procedure #0 can not be processed.",
                        resourceName);
                log.warn(e.getMessage(), e);
            }
        }
        return result;
    }

    /**
     * Registers all the available stored procedures, if a
     * resource can not be loaded from any reason(e.g. SQL syntax
     * failure) then the resource will be skipped.
     * 
     * @throws ClassNotFoundException if the database driver can
     *             not be loaded, the database driver information
     *             is collected in the post-construct phase (see
     *             the {@link #init()} method for more
     *             information).
     */
    public void processStoredProcedures() throws ClassNotFoundException {

        final List<String> procedureNames = getProcedureNames();
        final List<String> procedures = getProcedures(procedureNames);

        for (final String procedure : procedures) {
            try {
                // JDBCTool.execute(procedure, driver, url, user,
// passwd);
                procedureProcessor.process(procedure);
            } catch (final Exception e) {
                log.debug("Stored procedure #0[#1] can not be processed.");
                log.warn(e.getMessage(), e);
            }
        }
        log.debug("Stored procedures #0 are succesfull processed.", procedures);
    }
}
