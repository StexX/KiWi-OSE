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
package interedu.action.comment;

import interedu.action.explorer.CurrentArticleSelector;
import interedu.api.dataimport.InterEduArtikelFacade;
import interedu.api.dataimport.InterEduComment;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.UUID;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;

@Scope(ScopeType.CONVERSATION)
@Name("interedu.intereduCommentAction")
//@Transactional
public class IntereduCommentAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@In(value="interedu.currentArticleSelector")
	private CurrentArticleSelector selector;
	
	@In
	private ContentItemService contentItemService;
	
	@In
	private TripleStore tripleStore;
	
	// input current user; might affect the loading of the content item
	@In(create = true)
	private User currentUser;
	    
    @In
    private KiWiEntityManager kiwiEntityManager;
	
	private InterEduArtikelFacade article;
	private String description;
	private String rating;
	
	private boolean active = false;;
	
	@Begin(join=true)
	public void begin() {
		article = selector.getCurrentArticle();
		description = "";
		rating = "3";
		active = true;
	}
	
	@End
	public void save() {
		ContentItem reply = contentItemService.createContentItem("intereducomment/"+UUID.randomUUID().toString());
		KiWiUriResource type = tripleStore.createUriResource(Constants.NS_INTEREDU_CORE + "IntereduComment");
		
		reply.getResource().addType(type);

		reply.setAuthor(currentUser);
		contentItemService.updateTitle(reply, "IntereduComment from " + currentUser.getLogin());
		contentItemService.updateTextContentItem(reply, description);
		
		contentItemService.saveContentItem(reply);
		
	    InterEduComment comment = kiwiEntityManager.createFacade(reply, InterEduComment.class);
		comment.setRating(Integer.valueOf(rating));
	    
	    kiwiEntityManager.persist(comment);
//	    kiwiEntityManager.flush();
	    
		LinkedList<InterEduComment> c = article.getIntereduComments();
		c.add( comment );
		article.setIntereduComments(c);
		
		kiwiEntityManager.persist(article);
//		kiwiEntityManager.flush();
		
		active = false;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	@End
	public void cancel() {
		active = false;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}
	
}
