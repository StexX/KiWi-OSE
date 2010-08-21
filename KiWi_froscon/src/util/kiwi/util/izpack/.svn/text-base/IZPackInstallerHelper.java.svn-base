/*
 * File : IZPackInstallerHelper.java.java
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
import java.io.IOException;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;


/**
 * This class is bridge between the IZpack and the java/kiwi
 * classes. The IZpach mechanism call the main method from this
 * class with the following arguments :
 * <ol>
 * <li>jBosssHome
 * <li>kiwiHome
 * <li>dbType
 * <li>dbHost
 * <li>dbPort
 * <li>dbName
 * <li>dbUser
 * <li>dbUserPasswd
 * </ol>
 * All arguments are required. The order is required also.
 *
 * @author mradules
 * @version 0.9
 * @since 0.9
 */
public class IZPackInstallerHelper {

    private final static Logger LOG = Logger
            .getLogger(IZPackInstallerHelper.class.getName());

    /**
     * @param args
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws ClassNotFoundException
     * @throws IllegalArgumentException
     */
    public static void main(String[] args) throws IOException,
            ParserConfigurationException, SAXException, ClassNotFoundException {

        if (args == null) {
            throw new IllegalArgumentException("Right arguments required");
        }

        if (args.length != 8) {
            throw new IllegalArgumentException("Right arguments required");
        }

        final String jBossHome =  args[0];
        final String kiwiHome =  args[1];
        final String dbType = args[2];
        final String dbHost = args[3];
        final String dbPort = args[4];
        final String dbName = args[5];
        final String dbUser = args[6];
        final String dbUserPasswd = args[7];

        final StringBuilder msg = new StringBuilder();
        msg.append("Jboss home : ");
        msg.append(jBossHome);
        msg.append("\n");

        msg.append("Kiwi home : ");
        msg.append(kiwiHome);
        msg.append("\n");

        msg.append("Database type : ");
        msg.append(dbType);
        msg.append("\n");

        msg.append("Database host : ");
        msg.append(dbHost);
        msg.append("\n");

        msg.append("Database port : ");
        msg.append(dbPort);
        msg.append("\n");

        msg.append("Database name : ");
        msg.append(dbName);
        msg.append("\n");

        msg.append("Database user : ");
        msg.append(dbUser);
        msg.append("\n");

        msg.append("Database password : ");
        msg.append(dbUserPasswd);
        msg.append("\n");

        LOG.info(msg.toString());
        final DatabaseSystem db = DatabaseSystem.getDatabaseForName(dbType);
        if (db == null) {
            final IllegalArgumentException illegalDBException =
                    new IllegalArgumentException("The database : " + dbType
                            + " is not supported.");
            LOG.warning(illegalDBException.getMessage());
            throw illegalDBException;
        }

        JBossHelper.cleanJBoss(jBossHome);
        
        final File dsFile =
                new File(kiwiHome + File.separator + "KiWi-default-ds.xml");
        JBossHelper.processDS(dsFile, jBossHome, dbType, dbName, dbHost,
                dbPort, dbUser,
                dbUserPasswd);
        dsFile.delete();

        final File persitenceFile =
                new File(kiwiHome + File.separator
                        + "KiWi-default-persistence.xml");
        JBossHelper.processPersistence(persitenceFile, jBossHome, kiwiHome,
                dbType);
        persitenceFile.delete();
    }
}
