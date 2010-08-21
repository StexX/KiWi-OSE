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
package kiwi.service.comment;

import java.util.List;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import kiwi.api.comment.CommentServiceLocal;
import kiwi.api.comment.CommentServiceRemote;
import kiwi.api.content.ContentItemService;
import kiwi.api.event.KiWiEvents;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Events;

/**
 * @author Sebastian Schaffert
 *
 */

@Stateless
@Name("commentService")
@Scope(ScopeType.STATELESS)
@AutoCreate
public class CommentServiceImpl implements CommentServiceLocal,	CommentServiceRemote {
	
	@In
	private ContentItemService contentItemService;
	
	@In
	private EntityManager entityManager;
	
	@In
	private TripleStore tripleStore;
	
	/**
	 * Create a reply to the content item "post" and return it for further editing. The created
	 * reply is automatically added to the comments-List of post and persisted in the database. 
	 * 
	 * @param post the content item to reply to
	 * @return a new content item which is already persisted and added to the comments of post
	 */
	@Override
//	@Transactional
	public ContentItem createReply(ContentItem post, User user, String title, String content) {
		ContentItem reply = contentItemService.createContentItem("comment/"+UUID.randomUUID().toString());
		KiWiUriResource type = tripleStore.createUriResource(Constants.NS_KIWI_CORE+"Comment");
		
		reply.getResource().addType(type);
		

		reply.setAuthor(user);
		contentItemService.updateTitle(reply, title);
		contentItemService.updateTextContentItem(reply, content);
		
		contentItemService.saveContentItem(reply);
		
		// TODO: now we need to add an explicit relation between the post and the reply using
		// the kiwi:hasComment property
		KiWiUriResource p_hasComment = tripleStore.createUriResource(Constants.NS_KIWI_CORE + "hasComment");
		tripleStore.createTriple(post.getResource(), p_hasComment, reply.getResource());
		
		//post.getComments().add(reply);
		
		Events.instance().raiseEvent(KiWiEvents.ACTIVITY_ADDCOMMENT, user, post, reply);
		
		return reply;
	}

	
	
	@Override
	public List<ContentItem> listComments(ContentItem item) {
		Query q = entityManager.createNamedQuery("commentService.listComments");
		q.setHint("org.hibernate.cacheable", true);
		q.setParameter("item", item);

		return q.getResultList();
	}

	
}
