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
package kiwi.model.content;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import kiwi.model.kbase.KiWiEntity;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.User;

/**
 * The ContentItem Interface is required to create KiWIFacades. 
 * If provides the common setter and getter methods and is implemented 
 * in the ContentItem class
 * 
 * @author Sebastian Schaffert
 *
 */
public interface ContentItemI extends KiWiEntity, Serializable {

	/**
	 * Get the delegated content item
	 * @return
	 */
	public ContentItem getDelegate();
	
	/**
	 * Return the content object related to the provided language.
	 * @return
	 */
	public TextContent getTextContent();

	/**
	 * Set the content object for this content item and the provided language.
	 * @param content
	 */
	public void setTextContent(TextContent content);

	/**
	 * Return the localised title of this content item. Currently, the title is represented by the
	 * RDF label of the corresponding resource; the method thus just delegates to KiWiResource.getLabel(Locale).
	 * @return
	 */
	

	public String getTitle();

	/**
	 * Set the localised title of this content item. Currently, the title is represented by the
	 * RDF label of the corresponding resource; the method thus just delegates to KiWiResource.setLabel(Locale,String).
	 * @param title
	 */
	public void setTitle(String title);

	/**
	 * Return the locale describing the language this content is represented in.
	 * @return
	 */
	public Locale getLanguage();

	/**
	 * Set the locale describing the language this content is represented in.
	 * @param language
	 */
	public void setLanguage(Locale language);

	/**
	 * Return the resource associated with this content item.
	 * @return
	 */
	public KiWiResource getResource();

	/**
	 * Set the resource associated with this content item
	 * @param resource
	 */
	public void setResource(KiWiResource resource);

	/**
	 * Return an ID for the content object that is unique across the system and can be used to identify the content
	 * object in error messages. A good way would be to use the resource of the content item together with the language of the
	 * object, and possibly the revision id.
	 * 
	 * @return
	 */
	public Long getId();

	/**
	 * Sets an ID for the content object that is unique across the system and can be used to identify the content
	 * object in error messages. A good way would be to use the resource of the content item together with the language of the
	 * object, and possibly the revision id.
	 * @param id
	 */
	public void setId(Long id);


	/**
	 * @return the created
	 */
	public Date getCreated();

	/**
	 * @param created the created to set
	 */
	public void setCreated(Date created);

	/**
	 * @return the modified
	 */
	public Date getModified();

	/**
	 * @param modified the modified to set
	 */
	public void setModified(Date modified);

	/**
	 * @param rating the rating to set
	 */
//	public void setRating(Integer rating);

	/**
	 * @return the rating
	 */
//	public Integer getRating();

	/**
	 * Setting the type of 
	 * @param type
	 */
	public void addType(KiWiUriResource type);

	/**
	 * @return the author
	 */
	public User getAuthor();

	/**
	 * @param author the author to set
	 */
	public void setAuthor(User author);

	/**
	 * @return the mediaContent
	 */
	public MediaContent getMediaContent();

	/**
	 * @param mediaContent the mediaContent to set
	 */
	public void setMediaContent(MediaContent mediaContent);


	/**
	 * @return the tagLabels
	 */
	public Set<String> getTagLabels();

	/**
	 * @param tagLabels the tagLabels to set
	 */
	public void setTagLabels(Set<String> tagLabels);
	
	/**
	 * Return the KiWi identifier associated with the resource of this content item.
	 * Usually just delegates to KiWiResource.getKiWiIdentifier(), but may be overriden e.g.
	 * in the search results to give faster answers.
	 * 
	 * @return
	 */
	public String getKiwiIdentifier();
}