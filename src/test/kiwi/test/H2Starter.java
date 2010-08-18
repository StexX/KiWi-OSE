/*
 * Filename     : H2Starter.java
 * Version      : 00.01-1
 * Date         : Feb 20, 2009
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


package kiwi.test;


import java.sql.SQLException;

import org.h2.tools.Console;
import org.h2.tools.Server;


/**
 * This class only starts the H2 server in a separate thread.<br>
 * The usage is simple - instantiate the class and use the
 * <code>stop</code>.<br>
 * This class can be used (for test purposes) to start/stop the
 * H2 server programmatically so in this case you don't need to
 * start the server in a separate (e.g. from the command line).
 *
 * @author mradules
 * @version 00.01-1
 * @since 00.01-1
 */
public final class H2Starter {

    /**
     * The thread used to start the h2 thread.
     */
    private final Thread h2Thread;

    /**
     * The h2 web server.
     */
    private Server server;

    /**
     * Used to starts from the command line the H2 server.
     *
     * @param args the command line arguments - no arguments are
     *            required.
     * @throws SQLException by any SQL related exception.
     */
    public static void main(String[] args) throws SQLException {
        new H2Starter();
        System.out.println("H2 web server is running.");
    }

    /**
     * Builds a <code>H2Starter</code> instance.
     */
    public H2Starter() {
        h2Thread = new Thread(new H2Runner(), "H2Thread");
        h2Thread.start();
    }

    /**
     * Stops the server.
     */
    public synchronized void stop() {
        server.stop();
        server = null;
    }

    /**
     * The threads target responsibble for the server start.
     */
    private final class H2Runner implements Runnable {

        /**
         * Don't let anyone to instantiate this class outside of
         * the enclosing class.
         */
        private H2Runner() {
            // UNIMPLEMETND.
        }

        @Override
        public void run() {

            final String[] args = new String[0];
            try {
                Console.main(args);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
