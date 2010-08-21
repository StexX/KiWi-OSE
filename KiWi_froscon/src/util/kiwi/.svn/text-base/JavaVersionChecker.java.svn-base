/*
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
 *
 *
 */


package kiwi;


import javax.swing.JOptionPane;


/**
 * Use to check if the actual java version correspond with a
 * given java version.
 * 
 * @author Mihai
 * @version 00.01-1
 * @since 00.01-1
 */
public final class JavaVersionChecker {

    private static final String WRONG_JAVA_TITLE = "wrong java";
    private static final String WRONG_JAVA_MESSAGE =
            "The KiWi system requres java 1.6\nPlease refer the documentation(howToRun.txt) for more details.\n";

    /**
     * Starts this class from the command line.
     *
     * @param args command line parameter. The array must contain
     *            only one string - the java version (e.g. 1.6)
     */
    public static void main(String[] args) {

        if (args.length != 1) {
            showWrongJava();
            System.out.println("false");
            return;
        }

        final String versionPref = args[0];
        final String version = System.getProperty("java.version");

        if (version == null) {
            showWrongJava();
            System.out.println("false");
            return;
        }

        if (version.startsWith(versionPref)) {
            System.out.println("true");
            return;
        }

        showWrongJava();
        System.out.println("false");
    }

    private static void showWrongJava() {
        JOptionPane.showMessageDialog(null, WRONG_JAVA_MESSAGE,
                WRONG_JAVA_TITLE,
                JOptionPane.ERROR_MESSAGE);
    }
}
