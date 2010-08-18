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
package tagit2.action.tagCloud;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kiwi.api.tagging.ExtendedTagCloudEntry;
import kiwi.api.tagging.TagCloudService;
import kiwi.api.tagging.TagRecommendation;
import kiwi.api.tagging.TagRecommendationService;
import kiwi.api.tagging.TaggingService;
import kiwi.model.content.ContentItem;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * A backing bean for the tag list displayed in wiki pages. Provides caching to avoid constant 
 * recalculation of tag cloud.
 * 
 * @author Sebastian Schaffert
 *
 */
@Name("tagit2.tagCloudAction")
@Scope(ScopeType.CONVERSATION)
public class TagCloudAction implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger
	private Log log;
	
	@In(create=true)
	private ContentItem currentContentItem;
	
	@In
	private TagCloudService tagCloudService;
	
	@In
	private TaggingService taggingService;
	
	@In
	private TagRecommendationService tagRecommendationService;
	
	private List<ExtendedTagCloudEntry> tagCloud;

	private List<TagRecommendation> tagRecommendations;
	
	private Map<String, Collection<TagRecommendation> > tagRecommendationGroups;
	
	/**
	 * @return the tagCloud
	 */
	public List<ExtendedTagCloudEntry> getTagCloud() {
		if(tagCloud == null) {
			tagCloud = tagCloudService.aggregateTags(taggingService.getTags(currentContentItem));
		}
		
		return tagCloud;
	}
	
	public Collection<TagRecommendation> getTagRecommendations() {
		if(tagRecommendations == null) {
			tagRecommendations = tagRecommendationService.getRecommendations(currentContentItem);
			
			log.debug("tag recommendations: #0",tagRecommendations);
			
			for(ExtendedTagCloudEntry e : getTagCloud()) {
				Iterator<TagRecommendation> it = tagRecommendations.iterator();
				while(it.hasNext()) {
					if(it.next().getLabel().equals(e.getTag().getTitle())) {
						it.remove();
					}
				}
			}
		}
		return tagRecommendations;
	}
	
	public Set<Map.Entry<String, Collection<TagRecommendation>>> getTagRecommendationGroups() {
		if(tagRecommendationGroups == null) {
			tagRecommendationGroups = tagRecommendationService.getGroupedRecommendations(currentContentItem);
			
			log.info("tag recommendations: #0",tagRecommendationGroups);
			
			for(ExtendedTagCloudEntry e : getTagCloud()) {
				for(Collection<TagRecommendation> list : tagRecommendationGroups.values()) {
					Iterator<TagRecommendation> it = list.iterator();
					while(it.hasNext()) {
						if(it.next().getLabel().equals(e.getTag().getTitle())) {
							it.remove();
						}
					}
				}
			}
		}
		return tagRecommendationGroups.entrySet();
	}
	
	/**
	 * When a tag update occurs, clear tagCloud cache
	 */
	@Observer(value="tagUpdate", create=false)
	public void tagUpdate() {
		clear();
	}
		
	@Observer(value="contentUpdated",create=false)
	public void contentUpdated() {
		clear();
	}
	
	@Observer(value="contentItemChanged",create=false)
	public void contentItemChanged() {
		clear();
	}

	public void clear() {
		tagCloud = null;
		tagRecommendations = null;
		tagRecommendationGroups = null;
	}
}
