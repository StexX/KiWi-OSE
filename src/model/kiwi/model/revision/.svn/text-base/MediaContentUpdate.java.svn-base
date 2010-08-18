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
package kiwi.model.revision;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import kiwi.model.content.MediaContent;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

/**
 * MediaContentUpdate links to the old and to the current MediaContent 
 * 
 * @author 	Stephanie Stroka
 * 			(stephanie.stroka@salzburgresearch.at)
 *
 */
@Entity
@Immutable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class MediaContentUpdate extends ContentUpdate {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2765057972367669569L;
	
	@OneToOne
	private MediaContent currentMediaContent;
	
	@OneToOne
	private MediaContent previousMediaContent;
	
	/**
	 * @return the currentMediaContent
	 */
	public MediaContent getCurrentMediaContent() {
		return currentMediaContent;
	}

	/**
	 * @param currentMediaContent the currentMediaContent to set
	 */
	public void setCurrentMediaContent(MediaContent currentMediaContent) {
		this.currentMediaContent = currentMediaContent;
	}

	/**
	 * @param previousMediaContent the previousMediaContent to set
	 */
	public void setPreviousMediaContent(MediaContent previousMediaContent) {
		this.previousMediaContent = previousMediaContent;
	}

	/**
	 * @return the previousMediaContent
	 */
	public MediaContent getPreviousMediaContent() {
		return previousMediaContent;
	}
}
