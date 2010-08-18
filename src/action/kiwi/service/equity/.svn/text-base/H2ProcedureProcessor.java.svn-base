/*
 * File : PostgresqlProcedureProcessor.java.java Date : Apr 13, 2010 DO NOT
 * ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER. Copyright 2008 The KiWi
 * Project. All rights reserved. http://www.kiwi-project.eu The contents of this
 * file are subject to the terms of either the GNU General Public License
 * Version 2 only ("GPL") or the Common Development and Distribution
 * License("CDDL") (collectively, the "License"). You may not use this file
 * except in compliance with the License. You can obtain a copy of the License
 * at http://www.netbeans.org/cddl-gplv2.html or nbbuild/licenses/CDDL-GPL-2-CP.
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


import kiwi.util.JDBCTool;

/**
 * Used to load stored procedure for the Hyperdrive database. <br>
 * The JDBC parameters required to for the connection with the database must
 * follow the Hyperdrive standards, in this way the :
 * <ul>
 * <li>diver can be : org.h2.Driver
 * <li>URL can be : jdbc:h2:tcp://localhost/~/kiwi
 * <li>user : sa
 * <li>password : ""(empty string)
 * </ul>
 * <br>
 * This class is not designed to be extends.
 * 
 * @author mradules
 * @version 0.9
 * @since 0.9
 */
final class H2ProcedureProcessor implements StoringProcedureProcessor {

    /**
     * String that represents the ALIAS h2 SQL statement.
     */
    private static final String ALIAS = "ALIAS";

    /**
     * The driver class name for the underlying database.
     */
    private final String driver;

    /**
     * The URL for the underlying database.
     */
    private final String url;

    /**
     * The user for the underlying database.
     */
    private final String user;

    /**
     * The password for the underlying database.
     */
    private final String passwd;

    /**
     * Builds a <code>H2ProcedureProcessor</code> for a given list of arguments.
     * See the class comments for details about the possible arguments values.
     * 
     * @param driver the database driver class full name (package and class
     *            name), it can not be null or empty string..
     * @param url the URL for the database, must be h2 conform.
     * @param user the database user, it can not be null or empty string..
     * @param passwd the passwords for the given user, it can not be null.
     * @throws NullPointerException if any argument is null.
     * @throws IllegalArgumentException if any argument is an empty string.
     */
    H2ProcedureProcessor(String driver, String url, String user, String passwd) {

        if (url == null || driver == null || passwd == null || user == null) {
            throw new NullPointerException(
                    "Some postgresql stored procedure's paramter are null.");
        }

        if (url.isEmpty() || driver.isEmpty() || user.isEmpty()) {
            throw new NullPointerException(
                    "Some postgresql stored procedure's paramter are empty strings.");
        }

        this.driver = driver;
        this.url = url;
        this.user = user;
        this.passwd = passwd;
    }

    /**
     * Register the given stored procedure to the database specified in the
     * constructor.
     * 
     * @param procedure to store, it can not be null or empty string.
     * @throws StoringProcedureProcessorException by any kind of database
     *             related exception. Most of the times it wraps the real cause
     *             for the exception, this cause can be obtained by using the
     *             <code>getCause()</code> method.
     * @throws NullPointerException if the <code>procedure</code> argument is
     *             null.
     * @throws IllegalArgumentException if the <code>procedure</code> argument
     *             is an empty string.
     */
    @Override
    public void process(String procedure)
            throws StoringProcedureProcessorException {
        final String procedureName = getProcedureName(procedure);
        removeProcedure(procedureName);

        try {
            JDBCTool.execute(procedure, driver, url, user, passwd);
        } catch (final Exception e) {
            // It is not recommend to catch all exceptions but in this case I
            // rethrow an exception.
            throw new StoringProcedureProcessorException(e);
        }
    }

    /**
     * Extracts the stored procedure name for a given stored procedure.
     * 
     * @param procedure the stored procedure, it must be valid one.
     * @return the name for the given stored procedure.
     * @throws IllegalArgumentException if the <code>procedure</code> is not a
     *             valid H2 stored procedure.
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
     * Removes an existing stored procedure from the underlying database, the
     * procedure is identified after its name.
     * 
     * @param procedure the name for the stored procedure to remove.
     */
    private void removeProcedure(String procedure) {
        try {
            final String dropAlias = "DROP ALIAS " + procedure.trim();
            JDBCTool.execute(dropAlias, driver, url, user, passwd);
        } catch (final Exception e) {
            // I don't care if I can not remove a procedure.
            // can be that the procedure already exists
        }
    }
}
