/*
 * File : JDBCTool.java.java Date : Apr 1, 2010 DO NOT ALTER OR REMOVE COPYRIGHT
 * NOTICES OR THIS HEADER. Copyright 2008 The KiWi Project. All rights reserved.
 * http://www.kiwi-project.eu The contents of this file are subject to the terms
 * of either the GNU General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the "License").
 * You may not use this file except in compliance with the License. You can
 * obtain a copy of the License at http://www.netbeans.org/cddl-gplv2.html or
 * nbbuild/licenses/CDDL-GPL-2-CP. See the License for the specific language
 * governing permissions and limitations under the License. When distributing
 * the software, include this License Header Notice in each file and include the
 * License file at nbbuild/licenses/CDDL-GPL-2-CP. KiWi designates this
 * particular file as subject to the "Classpath" exception as provided by Sun in
 * the GPL Version 2 section of the License file that accompanied this code. If
 * applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
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


package kiwi.util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * It is used to manipulate a given database via JDBC. <br/>
 * Its main purpose is to execute pure SQL statements on a given
 * database, this statements can be more than SQL queries(e.g.
 * create, destroy table, add storing procedure).<br/>
 * <b>Note : </b> this class uses the data base in a row mode,
 * uninspired usage can lead on very unpleasant side effects -
 * use this class only if there is really no other alternative.
 * 
 * @author mradules
 * @version 0.7
 * @since 0.7
 */
public final class JDBCTool {

    /**
     * Don't let anybody to instantiate this class.
     */
    private JDBCTool() {
        // UNIMPLEMENTD
    }

    /**
     * Obtains the SQL connection to the given database for a
     * list of arguments.
     * 
     * @param driver the taxi driver
     *            ("Are you talking with me ?").
     * @param url the database URL, it can not be null.
     * @param userid the user for the database, it can not be
     *            null..
     * @param password the password for the given user, it can
     *            not be null.
     * @return the SQL connection for the given parameters.
     * @throws ClassNotFoundException if the database driver can
     *             not be located in the classpath.
     * @throws SQLException by any SQL related errors.
     */
    private static Connection getConnection(String driver, String url,
            String userid, String password) throws ClassNotFoundException,
            SQLException {

        if (driver == null || url == null || userid == null || password == null) {
            throw new NullPointerException(
                    "The Connection argument(s) can no be null.");
        }

        Class.forName(driver);
        final Connection connection =
                DriverManager.getConnection(url, userid, password);
        return connection;
    }

    /**
     * Executes the given SQL statement, which may be an INSERT,
     * UPDATE, or DELETE statement or an SQL statement that
     * returns nothing, such as an SQL DDL statement.
     * 
     * @param sqlStatement the SQL statement to execute, it can
     *            not be null.
     * @param driver database driver like string.
     * @param url the database URL, it can not be null.
     * @param userid the user for the database, it can not be
     *            null..
     * @param password the password for the given user, it can
     *            not be null.
     * @throws ClassNotFoundException if the database driver can
     *             not be located in the classpath.
     * @throws SQLException by any SQL related errors.
     */
    public static void execute(String sqlStatement, String driver, String url,
            String userid, String password) throws ClassNotFoundException,
            SQLException {

        if (sqlStatement == null || sqlStatement.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "The sqlStatement must be a valid one.");
        }

        final Connection connection =
                getConnection(driver, url, userid, password);

        final Statement statement = connection.createStatement();

        statement.executeUpdate(sqlStatement);

        statement.close();
        connection.close();
    }
}
