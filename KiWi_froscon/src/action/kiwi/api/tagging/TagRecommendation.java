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

import kiwi.model.content.ContentItem;

/**
 * Recommendation for a tag. Contains the tag label and information whether the tag
 * <ul>
 * <li>is already an existing content item of type skos:Concept and thus may be used to associate
 *     the tagged item with a thesaurus; such tags should be rendered green in the UI</li>
 * <li>is already an existing content item of type kiwi:Tag and thus has already been used as
 *     free tag that is not yet associated with a thesaurus; should be rendered orange</li>
 * <li>is neither an existing tag nor a concept - adding it to a content item will create a 
 *     completly new tag; should be rendered red</li>
 * </ul>
 * 
 * @author Sebastian Schaffert
 *
 */
public class TagRecommendation {

	private String label;
	
	private boolean concept;
	
	private boolean existingTag;

	private float rating;
	
	private ContentItem item;
	
	
	public TagRecommendation(String label) {
		super();
		this.label = label;
	}

	
	public TagRecommendation(String label, boolean concept, boolean existingTag, float rating) {
		super();
		this.label = label;
		this.concept = concept;
		this.existingTag = existingTag;
		this.rating = rating;
	}


	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isConcept() {
		return concept;
	}

	public void setConcept(boolean concept) {
		this.concept = concept;
	}

	public boolean isExistingTag() {
		return existingTag;
	}

	public void setExistingTag(boolean existingTag) {
		this.existingTag = existingTag;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public float getRating() {
		return rating;
	}


	public ContentItem getItem() {
		return item;
	}


	public void setItem(ContentItem item) {
		this.item = item;
	}
	
	
}
