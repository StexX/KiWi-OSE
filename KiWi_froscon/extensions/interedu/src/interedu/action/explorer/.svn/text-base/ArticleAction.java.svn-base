/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 2008-2009, The KiWi Project (http://www.kiwi-project.eu)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * - Redistributions of source code must retain the above copyright notice, 
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, 
 *   this list of conditions and the following disclaimer in the documentation 
 *   and/or other materials provided with the distribution.
 * - Neither the name of the KiWi Project nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * Contributor(s):
 * 
 * 
 */
package interedu.action.explorer;

import interedu.action.tagging.InterEduTaggingAction;
import interedu.api.comment.StarService;
import interedu.api.dataimport.InterEduArtikelFacade;
import interedu.api.dataimport.InterEduComment;
import interedu.api.recommend.SimpleRecommenderService;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.render.RenderingService;
import kiwi.api.tagging.TagCloudService;
import kiwi.api.tagging.TagRecommendation;
import kiwi.api.tagging.TagRecommendationService;
import kiwi.api.tagging.TaggingService;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.ontology.SKOSConcept;
import kiwi.model.tagging.Tag;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Events;
import org.jboss.seam.log.Log;

@Name("interedu.articleAction")
@Scope(ScopeType.PAGE)
@AutoCreate
//@Transactional
public class ArticleAction implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger
	Log log;
	
    @In
    private RenderingService renderingPipeline;
	
	private String editMode = "none";
    
    @In
    private KiWiEntityManager kiwiEntityManager;
    
    @In(create=true)
    private TagRecommendationService tagRecommendationService;
    
    @In(create=true)
    private TaggingService taggingService;
    
    @In
    EntityManager entityManager;
    
    @In(create = true)
    private ContentItemService contentItemService;
    
    @In(create=true, value="interedu.starService")
    private StarService starService;
    
    @In
    private TripleStore tripleStore;
    
    @In(value="interedu.simpleRecommendationService",create=true)
    private SimpleRecommenderService recService;
    
	@In
	private TagCloudService tagCloudService;
    
    @In(value="interedu.currentArticleSelector")
    private CurrentArticleSelector articleSelector;
    
    @In(value="interedu.InterEduTaggingAction", create=true)
    private InterEduTaggingAction interEduTaggingAction;
	
    @In
    private User currentUser;
    
	private InterEduArtikelFacade article;
	
	public InterEduArtikelFacade getArticle() {
		if( article == null ) {
			article = articleSelector.getCurrentArticle();
		}
		return article;
	}
	
	public String getRenderedDescription() {
		String s = "rendering failed";
		try {
			s = renderingPipeline.renderHTML(article.getDelegate());
		} catch(Exception e) {
			log.error("rendering failed -  rendering Pipeline should be transactional");
		}
		return s;
	}

	public String getEditMode() {
		return editMode;
	}
	
	public void setEditMode(String editMode) {
		this.editMode = editMode;
	}
	
	//**************basic edit mode things
	private String title;
	private String description;
	private String schultyp;
	private String schulstufe;
	private String bildlink;
	private String dokumentArt;
	private String link;
	
	public void startBasicEdit() {
		this.editMode = "basic";
		this.title = article.getTitle();
		this.description =  renderingPipeline.renderEditor(article.getDelegate(), currentUser);
		this.schultyp = article.getSchultyp();
		this.schulstufe = article.getSchulstufen();
		this.bildlink = article.getBildlink();
		this.dokumentArt = article.getDokumentArt();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSchultyp() {
		return schultyp;
	}

	public void setSchultyp(String schultyp) {
		this.schultyp = schultyp;
	}

	public String getSchulstufe() {
		return schulstufe;
	}

	public void setSchulstufe(String schulstufe) {
		this.schulstufe = schulstufe;
	}
	
	public String getBildlink() {
		return bildlink;
	}

	public void setBildlink(String bildlink) {
		this.bildlink = bildlink;
	}

	public void setDokumentArt(String dokumentArt) {
		this.dokumentArt = dokumentArt;
	}

	public String getDokumentArt() {
		return dokumentArt;
	}

	public void saveBasicEdit() {
		contentItemService.updateTitle( article , title);
		contentItemService.updateTextContentItem(article, description);
		article.setSchulstufen(schulstufe);
		if( schultyp != null ) {
			article.setSchultyp(schultyp);
		} else {
			article.setSchultyp("");
		}
		article.setBildlink(bildlink);
		if( dokumentArt != null ) {
			article.setDokumentArt(dokumentArt);
		} else {
			article.setDokumentArt("");
		}
		
		changeStateToEdited();
		
		editMode = "none";
		
	}
	
	//*********** add - delete links
	private String linkUrl;
	private String oldLinkUrl;
	
	public void startAddLink() {
		linkUrl = "http://";
		editMode = "link";
	}
	
	public void startEditLink(String link) {
		linkUrl = link;
		oldLinkUrl = link;
		editMode = "link2";
	}
	
	public void addLink() {
		LinkedList<String> links = article.getLinks();
		if( !linkUrl.equals(null) && !linkUrl.equals("") && !linkUrl.equals("http://") ) {
			links.add(linkUrl);
		}
		article.setLinks(links);
		log.info("added link #0", linkUrl);
		
		changeStateToEdited();
		
		this.editMode = "none";
	}
	
	public void deleteLink() {
		LinkedList<String> links = article.getLinks();
		if( links.contains( oldLinkUrl ) ) {
			links.remove( oldLinkUrl );
			log.info("url #0 removed",oldLinkUrl);
		} else {
			log.info("url not in list and cannot be removed");
		}
		article.setLinks(links);
		
		changeStateToEdited();
		
		this.editMode = "none";
	}
	
	public void saveLink() {
		LinkedList<String> links = article.getLinks();
		if( links.contains( oldLinkUrl ) ) {
			links.remove( oldLinkUrl );
		}
		//add new
		links.add( linkUrl );
		article.setLinks(links);
		log.info("added link #0", linkUrl);

		changeStateToEdited();
		
		this.editMode = "none";
	}
	
	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	
	public List<String> getAllLinks() {
		List<String> l = article.getLinks();
		log.info("article contains #0 links", l.size());
		if( l != null ) {
			return l;
		} else {
			return Collections.EMPTY_LIST;
		}
	}

	//*********** add-delete categories
	private LinkedList<Long> categories;
	
	public void startEditCategories() {
		categories = new LinkedList<Long>();
		for(Long l : article.getCategoryIds()) {
			categories.add(l);
		}
		editMode="cat";
	}
	
	public void saveCategories() {
		article.setCategoryIds(categories);
		//change Fachgebiete if necessary
		log.info("Gegenstand ist #0", article.getHauptGegenstand());
		if( article.getHauptGegenstand().equals("Fachgebiete") && !categories.isEmpty() ) {
			SKOSConcept c = getCategories().get(0);
			String s = "Fachgebiete";
			while(c!=null) {
				if( c.getPreferredLabel().equals("Fachgebiete") ) {
					break;
				} else if( c.getPreferredLabel().equals("InterEduThesaurus") ) {
					break;
				} else {
					s = c.getPreferredLabel();
					c = c.getBroader();
				}
			}
			article.setHauptGegenstand(s);
			log.info("set Gegenstand to #0", s);
		}
		changeStateToEdited();
		
		editMode = "none";
	}
	
	public void addCategory(Long l) {
		categories.add(l);
	}
	
	public void removeCategory(Long l) {
		categories.remove(l);
	}
	
	public List<SKOSConcept> getCategories() {
		LinkedList<SKOSConcept> l = new LinkedList<SKOSConcept>();
		for( Long id : categories ) {
			ContentItem c = entityManager.find(ContentItem.class, id);
			log.info("content item id:#0, title:#1", c.getId(), c.getTitle());
			SKOSConcept cat = kiwiEntityManager.createFacade( c , SKOSConcept.class);
			l.add( cat );
			log.info("prefLabel: #0", cat.getPreferredLabel());
		}
		return l;
	}
	
	//********** add-delete related Articles
	
	private LinkedList<InterEduArtikelFacade> relArts;
	private List<InterEduArtikelFacade> recommendedArts;
	
	public void beginEditRelArts() {
		relArts = new LinkedList<InterEduArtikelFacade>();
		for( InterEduArtikelFacade a : article.getVerwandteArtikel()) {
			relArts.add(a);
		}
		recommendedArts = recService.getRecommenations(article);
		autoc = new LinkedList<String>();
		freeText = "";
		editMode="relArts";
	}
	
	public void saveRelArts() {
		article.setVerwandteArtikel(relArts);
		changeStateToEdited();
		
		editMode = "none";
	}
	
	public List<InterEduArtikelFacade> getAllRelatedArticles() {
		return relArts;
	}
	
	public void addRelatedArticle(InterEduArtikelFacade art) {
		deleteRelatedArticle( art );
		relArts.add(art);
	}
	
	public void deleteRelatedArticle( InterEduArtikelFacade article ) {
		for( InterEduArtikelFacade a : relArts ) {
			if( a.getId() == article.getId() ) {
				relArts.remove(a);
				break;
			}
		}
	}
	
	public void setRelArtFreeText(String freeText) {
		this.freeText = freeText;
	}

	public String getRelArtFreeText() {
		return freeText;
	}

	private String freeText;
	private List<String> autoc;
	
	public List<String> getAutoc() {
		return autoc;
	}
	
	public void autocompleteRelArtFreeText() {
		autoc = recService.getArticlesBeginsWidth(freeText);
	}
	
	public void addRelArtFreeText(String text) {
		ContentItem c = contentItemService.getContentItemByTitle(text);
		if( c != null && c.getResource().hasType(
                tripleStore.createUriResource(Constants.NS_INTEREDU_CORE + "Artikel"))) {
			relArts.add( kiwiEntityManager.createFacade(c, InterEduArtikelFacade.class) );
		} else {
			log.info("text is not an article");
		}
	}
	
	public List<InterEduArtikelFacade> getRecommandations() {
		//simple recommender
		return recommendedArts;
	}

	//********* add tags
	private String tags;
	private List<TagRecommendation> tagRecs;
	
	public void startAddTags() {
		editMode="tags";
		tags = "";
		tagRecs = tagRecommendationService.getRecommendations( article.getDelegate() );
	}
	public void addTags() {
		log.info("add tags: #0", tags);
		interEduTaggingAction.setTagLabel( tags );
		interEduTaggingAction.addTagTo(article);

		tagRecommendationGroups = null;
		
		editMode="none";
		
	}
	
	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}
	
	public void appendTags(String t) {
		this.tags = this.tags + "" + t;
	}
	
	private Map<String,Collection<TagRecommendation>> tagRecommendationGroups;

	public Set<Map.Entry<String, Collection<TagRecommendation>>> getTagRecommendationGroups() {
		if(tagRecommendationGroups == null) {
			tagRecommendationGroups = tagRecommendationService.getGroupedRecommendations(article.getDelegate());
			
			log.info("tag recommendations: #0",tagRecommendationGroups);
			
			for(Tag t : getAllTags()) {
				for(Collection<TagRecommendation> list : tagRecommendationGroups.values()) {
					TagRecommendation dt = null;
					Iterator<TagRecommendation> i = list.iterator();
					while( i.hasNext() ) {
						TagRecommendation tr = i.next();
						if( tr.getLabel().equals(t.getTaggingResource().getTitle()) ) {
							dt = tr;
							break;
						}
					}
					if( dt != null ) {
						list.remove(dt);
					}
				}
			}
		}
		return tagRecommendationGroups.entrySet();
	}
	
	public List<Tag> getAllTags() {
	
		return taggingService.getTags(article.getDelegate());
	}
	
	public String removeTag( Tag t ) {
		interEduTaggingAction.removeTag( t.getTaggingResource(), article );
		
		return "/interedu/article.xhtml";
	}
	
	
	//*********** downloads

	//change state
	private void changeStateToEdited() {
		
		Events.instance().raiseTransactionSuccessEvent("contentUpdated");
		article.setState( InterEduArtikelFacade.IN_USE );
		article.setModified( new Date() );
		kiwiEntityManager.persist( article );
//		kiwiEntityManager.flush();
	}
	
	public void changeStateToCommit() {
		
		Events.instance().raiseTransactionSuccessEvent("contentUpdated");
		article.setState( InterEduArtikelFacade.COMMITTIT );
		article.setModified( new Date() );
		kiwiEntityManager.persist( article );
//		kiwiEntityManager.flush();
	}
	
	//rating
	
	public int getNbOfRatings() {
		return article.getIntereduComments().size();
	}

	public String getRatingStars() {
		int l = 0;
		for( InterEduComment c : article.getIntereduComments() ) {
			l += c.getRating();
		}
		
		int n = getNbOfRatings();
		
		if( n > 0 ) {
			return starService.getStars( Math.round( l/n ) );
		} else {
			return starService.getStars( 0 );
		}
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getLink() {
		if( link == null ) {
			//http://www.eduhi.at/go/loading.php?artikel_id=112911&id=243295
			String kid = getArticle().getKiwiIdentifier();
			String artikel_id = kid.substring(kid.lastIndexOf('/')+1);
			link = "http://www.eduhi.at/go/loading.php?artikel_id="+artikel_id+"&id="+getArticle().getUrlid();
			log.info("kid: #0, link: #1",kid,link);
		}
		return link;
	}
	
}
