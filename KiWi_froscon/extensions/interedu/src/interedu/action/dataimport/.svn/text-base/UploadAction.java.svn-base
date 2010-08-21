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


import interedu.api.configuration.Constants;
import interedu.api.dataimport.InterEduImportService;
import interedu.api.recommend.KeaRecomandationException;
import interedu.api.recommend.KeaRecommendService;
import interedu.service.dataimport.Artikel;
import interedu.service.dataimport.Kategorie;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import kiwi.api.ontology.SKOSService;
import kiwi.api.tagging.TagRecommendation;
import kiwi.model.ontology.SKOSConcept;
import kiwi.model.user.User;

import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;


/**
 * Handle the user gestured done with the inter edu upload UI.
 * Using this ui the user can upload extern data and import it
 * like inter edu article.
 * 
 * @author mradules
 * @version 04-pre
 * @since 04-pre
 */
@Name("interEdu.uploadAction")
public class UploadAction {

    /**
     * Used to format the date using the pattern : dd.MM.yyyy.
     */
    final private SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat("dd.MM.yyyy");

    /**
     * All the log messages are delegate to this member.
     */
    @Logger
    private static Log log;

    /**
     * Used to display messages to the UI.
     */
    @In
    private FacesMessages facesMessages;

    /**
     * Used to import interedu articles.
     */
    @In(create = true, value = "interedu.intereduImportService")
    private InterEduImportService interEduImportService;

    /**
     * used to manage and manipulate skos related issues.
     */
    @In(create = true)
    private SKOSService skosService;

    /**
     * The current user.
     */
    @In(create = true)
    private User currentUser;

    @In(create = true)
    private KeaRecommendService keaRecommendService;

    /**
     * The title for the uploaded article.
     */
    private String title;

    /**
     * The location from where the article content will be
     * loaded.
     */
    private String location;

    /**
     * The available SKOS categories.
     */
    private Set<String> categories;

    /**
     * The selected category, this will be the category for the
     * uploaded article.
     */
    private String[] selectedCategories;

    /**
     * All the recommended tags.
     */
    private Set<String> recommendedTags;

    /**
     * All the selected tags - the user select the tags.
     */
    private List<String> selectedTags;

    private Set<SKOSConcept> skosCategories;

    private String expireDate;

    private String difficulty;

    private String subject;

    private String informationSource;

    private String content;

    public UploadAction() {
    }

    @Create
    public void init() {
        final SKOSConcept concept =
                skosService.getConcept(Constants.ROOT_CONCEPT_NAME);
        skosCategories = concept.getNarrower();
        categories = new HashSet<String>();
        recommendedTags = new HashSet<String>();
        for (final SKOSConcept category : skosCategories) {
            final String title = category.getPreferredLabel();
            categories.add(title);
        }

        recommendedTags = new HashSet<String>();
        recommendedTags.add("default");

    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location == null ? "" : location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getExpireDate() {
        return expireDate == null ? "" : expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getDifficulty() {
        return difficulty == null ? "" : difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getSubject() {
        return subject == null ? "" : subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getInformationSource() {
        return informationSource == null ? "" : informationSource;
    }

    public void setInformationSource(String informationSource) {
        this.informationSource = informationSource;
    }

    public List<String> getSelectedTags() {
        return selectedTags;
    }

    public void setSelectedTags(List<String> selectedTags) {
        this.selectedTags = selectedTags;
    }

    public void load() throws IOException, KeaRecomandationException {

        if (title == null) {
            facesMessages.add("The title can not be null");
            log.warn("The title text field can not be empty.");
            return;
        }

        if (subject == null) {
            facesMessages.add("The subject can not be null");
            log.warn("The subject text field can not be empty.");
            return;
        }

        if (difficulty == null) {
            facesMessages.add("The difficulty can not be null");
            log.warn("The difficulty text field can not be empty.");
            return;
        }

        if (informationSource == null) {
            facesMessages.add("The informationSource can not be null");
            log.warn("The Information Source text field can not be empty.");
            return;
        }

        if (location == null) {
            facesMessages.add("The location can not be null");
            log.warn("The location can not be null");
            return;
        }

        final File loc = new File(location);
        if (!loc.exists() || !loc.canRead()) {
            final FileNotFoundException notFoundException =
                    new FileNotFoundException("The File : " + location
                            + " does not exist or can not be read.");
            log.warn(notFoundException.getMessage(), notFoundException);
        }

        final StringBuilder contentBuff = new StringBuilder();
        final ParserManager paserManager = ParserManager.getInstance();
        if (loc.isDirectory()) {
            final String[] files = loc.list();
            for (final String file : files) {
                final String transform = paserManager.parse(file);
                contentBuff.append(transform);
            }
        } else {
            final String transform = paserManager.parse(location);
            contentBuff.append(transform);
        }

        content = contentBuff.toString();

        buildRecommendTags(content);
    }

    private List<Kategorie> getCategories(Set<SKOSConcept> concepts) {
        final List<Kategorie> result = new ArrayList<Kategorie>();

        for (final SKOSConcept concept : concepts) {
            final Kategorie kategorie = new Kategorie();

            final Long id = concept.getId();
            kategorie.setId(id.intValue());

            final String title = concept.getTitle();
            kategorie.setName(title);

            result.add(kategorie);
        }

        return result;
    }

    public void commit() {
        final Artikel article = new Artikel();
        article.setArtikelTitel(title);
        article.setArtikelAutor(currentUser.getLogin());
        article.setQuelle(informationSource);
        article.setErscheindatum(DATE_FORMAT.format(new Date()));
        article.setVerfallsdatum(expireDate);
        article.setSchulstufen(difficulty);
        article.setGegenstand(subject);
        article.setInhalt(content);

        article.setArtikelAutor(currentUser.getLogin());

        final List<Kategorie> kats = getCategories(skosCategories);
        interEduImportService.importArticle(article, kats, recommendedTags);
    }

    private void buildRecommendTags(String text)
            throws KeaRecomandationException {

        recommendedTags.clear();
        final List<TagRecommendation> keaTags =
                keaRecommendService.getRecommedFor(text);
        if (keaTags.isEmpty()) {
            facesMessages.add("No recommendations for the given document.");
        }

        for (final TagRecommendation recTag : keaTags) {
            recommendedTags.add(recTag.getLabel());
        }

    }

    public Set<String> getCategories() {
        return categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    public Set<String> getRecommendedTags() {
        return recommendedTags;
    }

    public void setRecommendedTags(Set<String> recommendedTags) {
        this.recommendedTags = recommendedTags;
    }

    public String[] getSelectedCategories() {
        return selectedCategories;
    }

    public void setSelectedCategories(String[] selectedCategories) {
        this.selectedCategories = selectedCategories;
    }
}
