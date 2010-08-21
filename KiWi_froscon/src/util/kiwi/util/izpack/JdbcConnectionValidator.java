/*
 * File : JDBCConnectionDataValidator.java.java
 * Date : Aug 13, 2010
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


import java.util.Properties;

import com.izforge.izpack.installer.AutomatedInstallData;
import com.izforge.izpack.installer.DataValidator;


/**
 * Used to to prove if the JDBC connection with a given database
 * can be done.
 * 
 * @author mihai
 * @version 0.9
 * @since 0.9
 */
public final class JdbcConnectionValidator implements DataValidator {

    private final String TYPE = "kiwi.database.type";

    private final String HOST = "kiwi.database.host";

    private final String PORT = "kiwi.database.port";

    private final String DB_NAME = "kiwi.database.name";

    private final String DB_USER = "kiwi.database.name";

    private final String DB_USER_PASSWORD = "kiwi.database.name";

    private final StringBuilder errorMessage;

    /**
     * 
     */
    public JdbcConnectionValidator() {
        errorMessage = new StringBuilder();
    }

    /*
     * (non-Javadoc)
     * @see
     * com.izforge.izpack.installer.DataValidator#getDefaultAnswer
     * ()
     */
    @Override
    public boolean getDefaultAnswer() {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.izforge.izpack.installer.DataValidator#getErrorMessageId
     * ()
     */
    @Override
    public String getErrorMessageId() {
        return errorMessage.toString();
    }

    /*
     * (non-Javadoc)
     * @see
     * com.izforge.izpack.installer.DataValidator#getWarningMessageId
     * ()
     */
    @Override
    public String getWarningMessageId() {
        return "";
    }

    /*
     * (non-Javadoc)
     * @see
     * com.izforge.izpack.installer.DataValidator#validateData
     * (com.izforge.izpack.installer.AutomatedInstallData)
     */
    @Override
    public Status validateData(AutomatedInstallData data) {
        final Properties variables = data.getVariables();
        final String dbType = variables.getProperty(TYPE);
        final String dbPort = variables.getProperty(PORT);
        final String dbHost = variables.getProperty(HOST);
        final String dbName = variables.getProperty(DB_NAME);
        final String user = variables.getProperty(DB_USER);
        final String password = variables.getProperty(DB_USER_PASSWORD);

        final DatabaseSystem databaseSystem =
                DatabaseSystem.getDatabaseForName(dbType);

        if (databaseSystem == null) {
            return Status.ERROR;
        }

        final String driver = databaseSystem.getDriverClassName();
        final String url =
                databaseSystem.getdatabaseURL(databaseSystem, dbName, dbHost,
                        dbPort);
        buildErrorMessage(url);
        final boolean canConnectTo =
                JDBCConnectionTester.canConnectTo(url, driver, user, password);

        final Status result = canConnectTo ? Status.OK : Status.ERROR;
        return result;
    }

    private void buildErrorMessage(String url) {
        if (!errorMessage.toString().isEmpty()) {
            errorMessage.delete(0, errorMessage.length() - 1);
        }

        errorMessage.append("The Database ");
        errorMessage.append(url);
        errorMessage.append(" can not be connected.");
    }
}
