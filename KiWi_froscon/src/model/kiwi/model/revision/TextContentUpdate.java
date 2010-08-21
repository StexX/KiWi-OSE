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

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import kiwi.model.content.TextContent;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;


/**
 * Saves the changes of modified TextContent. 
 * The TextContentUpdate is a mix of incremental and discrete updates. 
 * The TextContent is stored as a whole, which enables a faster 
 * view and restore functionality. The actual changes 
 * (which is the result of a diff between two TextContents on a word basis)
 * is stored in the TextContentUpdateDiffs. 
 * 
 * @author Stephanie Stroka 
 * 			(sstroka@salzburgresearch.at)
 *
 */
@Entity
@Immutable
@BatchSize(size = 20)
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class TextContentUpdate extends ContentUpdate {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Long id;
	
	/**
	 * the generated serialization id
	 */
	private static final long serialVersionUID = 4829510330615701158L;
	
	/**
	 * the styled previewText, contains style spans
	 */
	@Transient
	private String previewText;
	
	/**
	 * The textcontent which caused the update
	 */
	@OneToOne(fetch=FetchType.EAGER)
	private TextContent textContent;
	
	/**
	 * a list of diffs (words and word-positions) that can regenerate 
	 * the previous text
	 */ 
	@OneToMany(cascade=CascadeType.PERSIST)
	@BatchSize(size=20)
	private List<TextContentUpdateDiff> changes;

//	@Version
//	private Long version;

	/**
	 * default constructor
	 */
	public TextContentUpdate() {
		super();
	}
	
	/**
	 * @return the previewText
	 */
	public String getPreviewText() {
		return previewText;
	}

	/**
	 * @param previewText the previewText to set
	 */
	public void setPreviewText(String previewText) {
		this.previewText = previewText;
	}

	/**
	 * @return the textContent
	 */
	public TextContent getTextContent() {
		return textContent;
	}

	/**
	 * @param textContent the textContent to set
	 */
	public void setTextContent(TextContent textContent) {
		this.textContent = textContent;
	}

	/**
	 * @return the changes
	 */
	public List<TextContentUpdateDiff> getChanges() {
		return changes;
	}

	/**
	 * @param changes the changes to set
	 */
	public void setChanges(List<TextContentUpdateDiff> changes) {
		this.changes = changes;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

//    /**
//	 * @return the version
//	 */
//	public Long getVersion() {
//		return version;
//	}
//	
//	/**
//	 * @param version the version to set
//	 */
//	public void setVersion(Long version) {
//		this.version = version;
//	}
}
