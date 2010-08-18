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
package kiwi.api.comment;

import java.util.List;

import kiwi.model.content.ContentItem;
import kiwi.model.user.User;

/**
 * Provides the functionality for commenting to other content items.
 * 
 * The current implementation is rather straightforward and could be solved
 * without using a service. In the future it should be extended to support argumentation
 * ontologies and similar.
 * 
 * @author Sebastian Schaffert
 *
 */
public interface CommentService {

	/**
	 * Create a reply to the content item "post" and return it for further editing. The created
	 * reply is automatically added to the comments-List of post and persisted in the database. 
	 * 
	 * @param post the content item to reply to
	 * @return a new content item which is already persisted and added to the comments of post
	 */
	public ContentItem createReply(ContentItem post, User user, String title, String content);
	
	/**
	 * List all the comments associated with a content item using the kiwi:hasComment relation.
	 * <p>
	 * For performance reasons, this method is implemented in a service and not as part of the 
	 * content item.
	 * 
	 * @param item the item for which to list the comments
	 * @return a list of comments for the passed content item, ordered by date (newest first)
	 */
	public List<ContentItem> listComments(ContentItem item);
}
