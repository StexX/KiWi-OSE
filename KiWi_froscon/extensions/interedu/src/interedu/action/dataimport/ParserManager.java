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

package interedu.action.dataimport;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Provides access to various parsers, each parser it used to
 * parse a certain kind of input.
 * 
 * @author mradules
 * @version 04-pre
 * @since 04-pre
 */
final class ParserManager {

    /**
     * The only <code>ParserManager</code> instance.
     */
    private static final ParserManager MANAGER = new ParserManager();

    /**
     * Holds all the parsers.
     */
    private final Map<String, Parser> parsers;

    /**
     * Don't let anybody to instantiate this class.
     */
    private ParserManager() {
        parsers = new HashMap<String, Parser>();
        parsers.put("doc", new MsWordParser());
        parsers.put("txt", new TextFileParser());
    }

    /**
     * Returns a <code>ParserManager</code>, this method returns
     * always the same instance.
     * 
     * @return a <code>ParserManager</code> instance..
     */
    static ParserManager getInstance() {
        return MANAGER;
    }

    /**
     * Returns true of false if this manager can parse the given
     * file.
     * 
     * @param file the file which parse possibility is to be
     *            tested.
     * @return
     */
    boolean canParse(String file) {
        final int lastIndexOf = file.lastIndexOf(".");
        if (lastIndexOf == -1) {
            throw new IllegalArgumentException("Mime type is not supported");
        }

        final String extension = file.substring(lastIndexOf + 1);


        final boolean result = parsers.containsKey(extension);
        return result;
    }

    /**
     * Parse the given file. If the given file is not supported
     * then this method throws a
     * <code>IllegalArgumentException</code>.
     * 
     * @param file the file to process.
     * @return the parsed file.
     * @throws IOException by may kind of io problem.
     */
    String parse(File file) throws IOException {
        return parse(file.toString());
    }

    String parse(String file) throws IOException {

        final int lastIndexOf = file.lastIndexOf(".");
        if (lastIndexOf == -1) {
            throw new IllegalArgumentException("Mime type is not supported");
        }

        final String extension = file.substring(lastIndexOf + 1);
        if (!parsers.containsKey(extension)) {
            throw new IllegalArgumentException("Mime type [" + extension
                    + "] is not supported");
        }

        final FileInputStream fileInputStream = new FileInputStream(file);
        final Parser parser = parsers.get(extension);
        final String result = parser.parse(fileInputStream);
        return result;
    }


}
