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


import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;


/**
 * Reads a ms word (from a input stream) and transforms its
 * content in to a string.
 * 
 * @author mradules
 * @version 04-pre
 * @since 04-pre
 */
final class MsWordParser implements Parser {

    /**
     * Builds a <code>MsWordParser</code> instance.
     */
    MsWordParser() {
        // UNIMPLMENTED
    }

    /**
     * Transforms a ms document in to a String.
     * 
     * @throws ParserException by any possible failures. This
     *             exception chains the real cause for the
     *             exception, to obtain this use the cause()
     *             method.
     */
    @Override
    public String parse(InputStream inputStream) throws ParserException {
        final WordExtractor we;
        try {
            final POIFSFileSystem fs =
                    new POIFSFileSystem(inputStream);
            final HWPFDocument doc = new HWPFDocument(fs);
            we = new WordExtractor(doc);
        } catch (final IOException e) {
            throw new ParserException(e);
        }
        final String[] paragraphs = we.getParagraphText();

        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < paragraphs.length; i++) {
            paragraphs[i] = paragraphs[i].replaceAll("\\cM?\r?\n", "");
            result.append(paragraphs[i]);
        }

        return result.toString();
    }
}
