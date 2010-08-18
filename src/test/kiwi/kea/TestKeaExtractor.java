/*
 * File : TestKeaExtractor.java
 * Date : Jun 19, 2009
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
 *
 *
 */
package kiwi.kea;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;

import kiwi.interedu.kea.KeyphrassesExtractor;

import org.testng.annotations.Test;


/**
 * This test demonstrate how the kea keypass extractor works.
 * More precisely this class load a text file and make kea
 * recommendation for the file content text. <br>
 * The recommendation are print on the standard output. <br>
 * Note : the kea needs a lot of resources - that means a
 * OutOfMemoryException may occur. To avoid this you need to
 * increase the extended your maximum Java size and/or the set
 * java thread stack size. On my machine this settings was enough
 * : -Xmx2048m -Xss10m. <br>
 * This test is no unit test and this with purpose, because it
 * requires a special configuration for the jvm.
 * 
 * @author mradules
 * @version 04-pre
 * @since 04-pre
 */
@Test
public final class TestKeaExtractor {

    /**
     * Runs this test from the command line.
     * 
     * @param args the command lien arguments.
     * @throws Exception by any kind of exception.
     */
    public static void main(String[] args) throws Exception {
        new TestKeaExtractor();
    }

    /**
     * Don'tlat anybody to instantiate this class.
     * 
     * @throws Exception by any kind of exception.
     */
    private TestKeaExtractor() throws Exception {
        final KeyphrassesExtractor extractor =
                new KeyphrassesExtractor("model.small.kea");
        final String text = getText();
        final Set<String> keyphrasses = extractor.getKeyphrasses(text);
        System.out.println("keyphrasses ->" + keyphrasses);
    }

    /**
     * Reads the given file in to a string.
     * 
     * @param fileName the file to read.
     * @return the file content.
     * @throws IOException by any IOException.
     */
    private String getText(String fileName) throws IOException {
        final InputStream iStream = getClass().getResourceAsStream(fileName);
        final StringBuilder result = new StringBuilder();
        final InputStreamReader inReader = new InputStreamReader(iStream);
        final BufferedReader bufferedReader = new BufferedReader(inReader);
        for (String readLine = bufferedReader.readLine(); readLine != null; readLine =
                bufferedReader.readLine()) {
            result.append(readLine);
        }

        return result.toString();
    }

    /**
     * Reads the "10005.txt" file in to a string.
     * 
     * @param fileName the file to read.
     * @return the file content.
     * @throws IOException by any IOException.
     */
    private String getText() throws IOException {
        return getText("100005.txt");
    }
}
