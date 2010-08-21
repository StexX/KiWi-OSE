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

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import kiwi.model.tagging.Tag;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;

/**
 * The TaggingUpdate contains added and removed tags. Similar to the MetadataUpdate, 
 * TaggingUpdate stored the changes made to a ContentItem incrementally- 
 * 
 * @author Stephanie Stroka
 *			(stephanie.stroka@salzburgresearch.at)
 *
 */
@Entity
@Immutable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@BatchSize(size = 20)
public class TaggingUpdate implements KiWiUpdate, Serializable  {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Long id;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1519864637587237474L;

	@GenericGenerator(name="gg1",strategy="hilo")
	@ManyToMany
	@JoinTable(name = "addedTag_TaggingUpdate",
    		joinColumns = { @JoinColumn(name = "taggingUpdate_id") },
    		inverseJoinColumns = { @JoinColumn(name = "addedTag_id") } )
    @CollectionId(
    		columns = @Column(name="addedTag_TU_id"),
    		type = @Type(type="long"),
    		generator = "gg1")
	private Collection<Tag> addedTags;
	
	@GenericGenerator(name="gg2",strategy="hilo")
	@ManyToMany
	@JoinTable(name = "removedTag_TaggingUpdate",
    		joinColumns = { @JoinColumn(name = "taggingUpdate_id") },
    		inverseJoinColumns = { @JoinColumn(name = "removedTag_id") } )
    @CollectionId(
    		columns = @Column(name="removedTag_TU_id"),
    		type = @Type(type="long"),
    		generator = "gg2")
	private Collection<Tag> removedTags;
	
	public TaggingUpdate() {
	}

	/**
	 * @return the addedTags
	 */
	public Collection<Tag> getAddedTags() {
		return addedTags;
	}

	/**
	 * @param addedTags the addedTags to set
	 */
	public void setAddedTags(Collection<Tag> addedTags) {
		this.addedTags = addedTags;
	}

	/**
	 * @return the removedTags
	 */
	public Collection<Tag> getRemovedTags() {
		return removedTags;
	}

	/**
	 * @param removedTags the removedTags to set
	 */
	public void setRemovedTags(Collection<Tag> removedTags) {
		this.removedTags = removedTags;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}
}
