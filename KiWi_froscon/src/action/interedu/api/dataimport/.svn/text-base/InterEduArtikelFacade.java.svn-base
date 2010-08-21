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


package interedu.api.dataimport;

import java.util.LinkedList;

import kiwi.model.Constants;
import kiwi.model.annotations.KiWiFacade;
import kiwi.model.annotations.RDF;
import kiwi.model.annotations.RDFType;
import kiwi.model.content.ContentItem;
import kiwi.model.content.ContentItemI;


/**
 * TODO: does that really need to be half-english/half-german?!
 * 
 * @author Rolf Sint
 */
@KiWiFacade
@RDFType(Constants.NS_INTEREDU_CORE + "Artikel")
public interface InterEduArtikelFacade extends ContentItemI {

    // //TODO
    // public InterEduApertureFacade getAperture();
    //
    // public void setAperture(InterEduApertureFacade aperture);
    //

    @RDF(Constants.NS_INTEREDU_CORE + "autor")
    public String getArtikelAutor();

    public void setArtikelAutor(String autor);

    @RDF(Constants.NS_INTEREDU_CORE + "urlId")
    public int getUrlid();

    public void setUrlid(int urlid);

    @RDF(Constants.NS_INTEREDU_CORE + "erscheinungsDatum")
    public String getErscheindatum();

    public void setErscheindatum(String erscheindatum);

    @RDF(Constants.NS_INTEREDU_CORE + "verfallsDatum")
    public String getVerfallsdatum();

    public void setVerfallsdatum(String verfallsdatum);

    @RDF(Constants.NS_INTEREDU_CORE + "dokumentArtId")
    public int getDokumentartid();

    public void setDokumentartid(int dokumentartid);

    @RDF(Constants.NS_INTEREDU_CORE + "bildlink")
    public String getBildlink();

    public void setBildlink(String bildlink);

    @RDF(Constants.NS_INTEREDU_CORE + "schulStufen")
    public String getSchulstufen();

    public void setSchulstufen(String schulstufen);

    @RDF(Constants.NS_INTEREDU_CORE + "schultyp")
    public String getSchultyp();

    public void setSchultyp(String schultyp);

    @RDF(Constants.NS_INTEREDU_CORE + "gegenstand")
    public String getGegenstand();

    public void setGegenstand(String gegenstand);

    @RDF(Constants.NS_INTEREDU_CORE + "quelle")
    public String getQuelle();

    public void setQuelle(String quelle);

    @RDF(Constants.NS_INTEREDU_CORE + "hauptGegenstand")
    public String getHauptGegenstand();
    
    public String setHauptGegenstand(String hauptGegenstand);
    
    @RDF(Constants.NS_INTEREDU_CORE + "dokumentArt")
    public String getDokumentArt();
    
    public void setDokumentArt(String dokumentArt);
    
    
    /**
     * Gets the state for this article. There are only three
     * allowed states : NEW, IN_USE, COMMIT.
     * 
     * @return the state for this article.
     * @see #NEW
     * @see #IN_USE
     * @see #COMMITTIT
     */
    @RDF(Constants.NS_INTEREDU_CORE + "state")
    int getState();

    /**
     * Sets the state for this article. There are only three
     * allowed states : NEW, IN_USE, COMMIT.
     * 
     * @return the state for this article.
     * @see #NEW
     * @see #IN_USE
     * @see #COMMITTIT
     */
    void setState(int state);

    @RDF(Constants.NS_INTEREDU_CORE + "categoryid")
    LinkedList<Long> getCategoryIds();

    void setCategoryIds(LinkedList<Long> categoryIds);
    
    @RDF(Constants.NS_INTEREDU_CORE + "links")
    LinkedList<String> getLinks();

    void setLinks(LinkedList<String> links);
    
    @RDF(Constants.NS_INTEREDU_CORE + "documents")
    LinkedList<ContentItem> getDocuments();
    
    void setDocuments( LinkedList<ContentItem> documents );
    
    @RDF(Constants.NS_INTEREDU_CORE + "relArticle")
    LinkedList<InterEduArtikelFacade> getVerwandteArtikel();
    
    void setVerwandteArtikel( LinkedList<InterEduArtikelFacade> items );
    
    @RDF(Constants.NS_INTEREDU_CORE + "comments")
    LinkedList<InterEduComment> getIntereduComments();
    
    void setIntereduComments( LinkedList<InterEduComment> interEduComments );
    
    /**
     * In this state the articles was just imported - no user
     * interaction.
     */
    int NEW = 0;

    /**
     * in this state the article status was changes but it was
     * not commit/released.
     */
    int IN_USE = 1;

    /**
     * The article is released.
     */
    int COMMITTIT = 2;

}
