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
package kiwi.wiki.action;

import java.io.Serializable;

import javax.faces.context.FacesContext;

import kiwi.api.comment.CommentService;
import kiwi.api.content.ContentItemService;
import kiwi.api.render.RenderingService;
import kiwi.model.content.ContentItem;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.richfaces.component.UITogglePanel;

/**
 * This action component provides backing functionality for commenting in the Wiki application.
 * 
 * @author Sebastian Schaffert
 *
 */
@Name("kiwi.wiki.CommentAction")
@Scope(ScopeType.PAGE)
public class CommentAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5795011626948803167L;

	@Logger
	private Log log;
	
	/**
	 * The currently active content item.
	 */
	@In(create=true)
	private ContentItem currentContentItem;
	
	@In(create=true)
	private User currentUser;
	
	@In(create=true)
	private CommentService commentService;
	
	@In(create=true)
	private ContentItemService contentItemService;
	
	@In(create=true)
	private RenderingService renderingPipeline;
	
	// doesn't work yet...
	private UITogglePanel newCommentTogglePanel;
	
	String title;
	
	String comment;

	public String getTitle() {
		if(title == null || title.equals("")) {
			title = "Re: " + currentContentItem.getTitle();
		}
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
	public void addComment() {
		log.info("creating new comment: #0", title);
		
		commentService.createReply(currentContentItem, currentUser, title, comment);
		
		contentItemService.saveContentItem(currentContentItem);
		
		title = "";
		comment = "";
		
		// close togglepanel...
		newCommentTogglePanel=(UITogglePanel) 
			FacesContext.getCurrentInstance().getViewRoot().findComponent("formWikiContent:commentPanel:newcomment");
		if(newCommentTogglePanel!=null)
			newCommentTogglePanel.setValue("closed");
	}
	
	public String getCommentHtml(ContentItem comment) {
		if(comment != null) {
			log.info("rendering content for comment #0: #1", comment.getTitle(), comment.getTextContent());
			return renderingPipeline.renderHTML(comment);
		} else {
			log.error("the comment that was passed to getCommentHtml is null");
			return "";
		}
	}

	public UITogglePanel getNewCommentTogglePanel() {
		return newCommentTogglePanel;
	}

	public void setNewCommentTogglePanel(UITogglePanel newCommentTogglePanel) {
		this.newCommentTogglePanel = newCommentTogglePanel;
	}

}
