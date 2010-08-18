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
package kiwi.api.tagging;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import kiwi.model.content.ContentItem;
import kiwi.model.tagging.Tag;
import kiwi.model.user.User;

/**
 * @author Sebastian Schaffert
 *
 */
public interface TagCloudService {

	/**
	 * Do a more efficient, database-driven aggregation of the tags of a content item.
	 * Otherwise yields the same results as aggregateTags(Collection<Tag>)
	 * @param item
	 * @return
	 */
	public List<TagCloudEntry> aggregateTagsByCI(ContentItem item);
	public List<TagCloudEntry> aggregateTagsByCI(ContentItem item, boolean includeInferred);
	
	public List<ExtendedTagCloudEntry> aggregateTags(Collection<Tag> tags);

	/**
	 * Return the tag cloud of the tags of the user passed as argument.
	 * 
	 * @param u
	 * @return
	 */
	public List<TagCloudEntry> userTagCloud(User u);
	public List<ExtendedTagCloudEntry> portalTagCloud();
	
	/**
	 * Return a list of users who tagged some ContentItems with the given Tag
	 * @param item
	 * @return
	 */
	public List<Map<String,Object>> listTaggingUsers(String tagLabel);
}
