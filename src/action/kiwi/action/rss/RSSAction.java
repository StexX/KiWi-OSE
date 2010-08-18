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
package kiwi.action.rss;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import kiwi.api.config.ConfigurationService;
import kiwi.api.render.RenderingService;
import kiwi.api.rss.RSSService;
import kiwi.model.content.ContentItem;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.log.Log;

/**
 * A backing action for feed.xhtml.
 * 
 * @author Sebastian Schaffert
 *
 */
@Name("rssAction")
@Scope(ScopeType.PAGE)
public class RSSAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger
	private Log log;
	
	@RequestParameter
	private String mode;
	
	@RequestParameter
	private String login;
	
	@RequestParameter
	private String searchString;
	
	@RequestParameter
	private String format;
	
	@In
	private RSSService rssService;
	
	@In
	private ConfigurationService configurationService;
	
	@In
	private RenderingService renderingPipeline;
	
	private List<ContentItem> feedEntries;
	
	private String feedTitle;
	
	private String feedSubtitle;
	
	private Date feedUpdated;
	
	private String feedLink;
	
	private String feedFormat;
	
	
	
	public void begin() {
		log.info("generating RSS feed for mode #0, format #1.", mode, format);
		
		feedLink = configurationService.getBaseUri();
		feedTitle = "KiWi system at "+feedLink;
		
		if(mode == null || "MOSTRECENT".equals(mode.toUpperCase())) {
			feedSubtitle = "Most recently updated content items";
			feedEntries = rssService.getRecentChangesFeed();
			if(feedEntries.size() > 0) {
				feedUpdated = feedEntries.get(0).getModified();
			} else {
				feedUpdated = new Date();
			}
		}
		
		
		if("ATOM1".equals(format) || "RSS10".equals(format) || "RSS20".equals(format) || "ATOM03".equals(format) || "ATOM10".equals(format)) {
			feedFormat = format.toUpperCase();
		} else {
			feedFormat = "RSS20";
		}
	}

	public List<ContentItem> getFeedEntries() {
		return feedEntries;
	}

	public void setFeedEntries(List<ContentItem> feedEntries) {
		this.feedEntries = feedEntries;
	}

	public String getFeedTitle() {
		return feedTitle;
	}

	public void setFeedTitle(String feedTitle) {
		this.feedTitle = feedTitle;
	}

	public Date getFeedUpdated() {
		return feedUpdated;
	}

	public void setFeedUpdated(Date feedUpdated) {
		this.feedUpdated = feedUpdated;
	}

	public String getFeedLink() {
		return feedLink;
	}

	public void setFeedLink(String feedLink) {
		this.feedLink = feedLink;
	}

	public String getFeedFormat() {
		return feedFormat;
	}

	public void setFeedFormat(String feedFormat) {
		this.feedFormat = feedFormat;
	}

	public String getFeedSubtitle() {
		return feedSubtitle;
	}

	public void setFeedSubtitle(String feedSubtitle) {
		this.feedSubtitle = feedSubtitle;
	}
	
	public String renderEntry(ContentItem entry) {
		if (entry.getTextContent() != null) {
			return renderingPipeline.renderHTML(entry);
		} else {
			return "<p></p>";
		}
		
	}
	
}
