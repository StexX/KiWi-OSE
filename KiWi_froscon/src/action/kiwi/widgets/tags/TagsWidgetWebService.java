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
package kiwi.widgets.tags;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import kiwi.api.content.ContentItemService;
import kiwi.api.tagging.TagCloudEntry;
import kiwi.api.tagging.TagCloudService;
import kiwi.api.tagging.TagRecommendation;
import kiwi.api.tagging.TagRecommendationService;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiUriResource;

import org.jboss.resteasy.annotations.providers.jaxb.Wrapped;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * @author Sebastian Schaffert
 *
 */
@Name("kiwi.widgets.tagsWidget")
@Scope(ScopeType.STATELESS)
@Path("/widgets/tags")
public class TagsWidgetWebService {

	@Logger
	private Log log;
	
	@In
	private ContentItemService contentItemService;
	
	@In
	private TagCloudService tagCloudService;

	@In
	private TagRecommendationService tagRecommendationService;
	
	/**
	 * Return a JSON object representing a list of tags of the content item with the URI passed as
	 * argument (TagJSON). The web service is called using 
	 * /KiWi/seam/resource/service/widgets/tags/list?uri=<CONTEXT_URI> by the
	 * activity widget.
	 * 
	 * @param uri the URI of the content item for which to list the tags
	 * @return
	 */
	@GET
	@Path("/list")
	@Produces("application/json")
	@Wrapped(element="activities")
	public List<TagJSON> getTagCloud(@QueryParam("uri") String uri) {
		
		List<TagJSON> tags = new LinkedList<TagJSON>();
		
		if(uri != null) {
			ContentItem ci = contentItemService.getContentItemByUri(uri);
			
			for(TagCloudEntry t : tagCloudService.aggregateTagsByCI(ci)) {
				tags.add(new TagJSON(t.getTagTitle(),t.getCount()));
			}
			
		}
		
		return tags;
	}
	
	
	/**
	 * Return a JSON object representing a list of tag recommendations for the content item with
	 * the URI passed as parameter. (TagJSON). The web service is called using 
	 * /KiWi/seam/resource/service/widgets/tags/recommendations?uri=<CONTEXT_URI> by the
	 * tags widget.
	 * 
	 * @param uri the URI of the content item for which to list the tags
	 * @return
	 */
	@GET
	@Path("/recommendations")
	@Produces("application/json")
	@Wrapped(element="activities")
	public List<TagJSON> getTagRecommendations(@QueryParam("uri") String uri) {
		
		List<TagJSON> tags = new LinkedList<TagJSON>();
		
		if(uri != null) {
			ContentItem ci = contentItemService.getContentItemByUri(uri);
			
			for(TagRecommendation t : tagRecommendationService.getRecommendations(ci)) {
				if(t.getItem() == null) {
					tags.add(new TagJSON(t.getLabel()));
				} else if(t.getItem().getResource().isUriResource()) {
					tags.add(new TagJSON(t.getLabel(),((KiWiUriResource)t.getItem().getResource()).getUri()));
				}
			}
			
		}
		
		return tags;
	}

}
