/*
 * File : JDBCUtils.java.java
 * Date : May 3, 2010
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


package kiwi.util;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.jboss.seam.util.Naming;


/**
 * It is used to manipulate a given database via JDBC in an ENC
 * compliant environment. <br/>
 *
 * @author mradules
 * @version 0.9
 * @since 0.9
 */
public final class JDBCUtils {

    /**
     * Don't let anyone to instantiate this class.
     */
    private JDBCUtils() {
        // UNIMPLEMENTD
    }

    public static void execute(String sqlStatement) throws NamingException,
            SQLException {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = getDefaultConnection();
            statement = connection.createStatement();
            statement.executeUpdate(sqlStatement);
        } finally {

// if (statement != null) {
// statement.close();
// }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static Connection getDefaultConnection() throws NamingException,
            SQLException {
        final DataSource defaultDataSource = getDefaultDataSource();

        final Connection connection = defaultDataSource.getConnection();
        return connection;
    }

    public static DataSource getDefaultDataSource() throws NamingException {
        final InitialContext context = Naming.getInitialContext();
        final DataSource ds = (DataSource) context.lookup("java:/DefaultDS");
        return ds;
    }
}
