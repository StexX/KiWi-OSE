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


package interedu.service.recommend;


import interedu.api.recommend.KeaRecomandationException;
import interedu.api.recommend.KeaRecommendService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import kiwi.api.ontology.SKOSService;
import kiwi.api.tagging.TagRecommendation;
import kiwi.interedu.kea.KeyphrassesExtractor;
import kiwi.model.content.ContentItem;
import kiwi.model.ontology.SKOSConcept;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.log.Log;


/**
 * Provides recommendation for a given text.
 * 
 * @author mradules
 * @version 03-pre
 * @since 03-pre
 */
@Name("keaRecommendService")
@Scope(ScopeType.STATELESS)
@AutoCreate
public class KeaRecommendServiceImpl implements KeaRecommendService {

    @Logger
    private Log log;

    @In(create = true)
    private SKOSService skosService;

    /**
     * The instance used to extract the key phases.
     */
    private KeyphrassesExtractor extractor;

    public KeaRecommendServiceImpl() {
    }

    @Create
    public void initExtractor() throws KeaRecomandationException {
        try {
            extractor = new KeyphrassesExtractor("model.small.kea");
        } catch (final Exception e) {
            final KeaRecomandationException exception =
                    new KeaRecomandationException(e);
            log.error(exception.getMessage(), e);
            throw exception;
        }

    }

    /**
     * Provides recommendation for a given text.
     * 
     * @param text
     * @return the recommendation for the given text.
     */
    @Override
    public List<TagRecommendation> getRecommedFor(String text)
            throws KeaRecomandationException {

        // start a nested conversation for tagging
        // Conversation.instance().beginNested();

        try {
            final Set<String> keyphrasses = extractor.getKeyphrasses(text);
            final List<TagRecommendation> result =
                    new ArrayList<TagRecommendation>();
            for (String keyphras : keyphrasses) {
                final TagRecommendation tagRecommendation =
                        new TagRecommendation(keyphras);
                final SKOSConcept concept = skosService.getConcept(keyphras);
                final boolean isConcept = concept != null;
                tagRecommendation.setConcept(isConcept);

                result.add(tagRecommendation);
            }

            return result;
        } catch (final Exception e) {
            final KeaRecomandationException exception =
                    new KeaRecomandationException(e);
            log.error(exception.getMessage(), e);
            throw exception;
        } finally {
            // Conversation.instance().endBeforeRedirect();
        }



    }

    @Override
    public List<TagRecommendation> getRecommedFor(ContentItem item)
            throws KeaRecomandationException {

        final String text = item.getTextContent().getHtmlContent();
        final List<TagRecommendation> result = getRecommedFor(text);
        return result;
    }

}
