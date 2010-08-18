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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

import kiwi.model.content.ContentItem;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Immutable;
import org.hibernate.validator.NotNull;

/**
 * The CIVersion represents a ContentItem version, which 
 * means that it contains the information about the state of a ContentItem, which is either 
 * the current or a past state.
 * A CIVersion is created during every beforeCompletion state of the transaction. 
 * It is linked to a Revision object and differs from it because it contains all updates that
 * can be associated with a single ContentItem. The Revision object on the other hand may contain 
 * more than one CIVersion (especially in the case of nested ContentItem updates).
 * 
 * @author Stephanie Stroka
 *			(stephanie.stroka@salzburgresearch.at)
 *
 */
@Entity
@Immutable
@NamedQueries({
	@NamedQuery(name="version.lastTextContent",
			query="select civ.textContentUpdate.textContent " +
				  "from kiwi.model.revision.CIVersion civ " +
				  "where civ.revisedContentItem = :ci and " +
				  "civ.textContentUpdate is not null and " +
				  "civ.versionId <= :vid"),
	@NamedQuery(name="version.lastContentItemTitle",
			query="select civ.renamingUpdate.title " +
			  "from kiwi.model.revision.CIVersion civ " +
			  "where civ.revisedContentItem = :ci and " +
			  "civ.renamingUpdate is not null and " +
			  "civ.versionId < :vid"),
	@NamedQuery(name="version.lastDeletionFlag",
			query="select civ.deletionUpdate.deleted " +
			  "from kiwi.model.revision.CIVersion civ " +
			  "where civ.revisedContentItem = :ci and " +
			  "civ.deletionUpdate is not null and " +
			  "civ.versionId <= :vid"),
	@NamedQuery(name="version.lastMediaContent",
			query="select civ.mediaContentUpdate.currentMediaContent " +
			  "from kiwi.model.revision.CIVersion civ " +
			  "where civ.revisedContentItem = :ci and " +
			  "civ.textContentUpdate is not null and " +
			  "civ.versionId <= :vid")
})
@BatchSize(size = 20)
public class CIVersion implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6172743983508044935L;

	/**
	 * the id is generated to serve as a primary key 
	 * for this table in the relational model
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Long id;
	
	private String title;
	
	@OneToOne
	private TextContentUpdate textContentUpdate;
	
	@OneToOne
	private MediaContentUpdate mediaContentUpdate;
	
	@OneToOne
	private TaggingUpdate taggingUpdate;
	
	@OneToOne
	private RuleUpdate ruleUpdate;
	
	@OneToOne
	private DeletionUpdate deletionUpdate;
	
	@OneToOne
	private RenamingUpdate renamingUpdate;
	
	@OneToOne
	private MetadataUpdate metadataUpdate;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private ContentItem revisedContentItem;
	
	/** 
	 * the colour of this revision, shown in the preview view.
	 * by default it's black
	 */
	@NotNull
	@Column(length=7,nullable=false)
	private String colour = new String("#000000");
	
	@NotNull
	private Long versionId;
	
	@ManyToOne
	private Revision revision;
	
	private boolean origin;
	
	/**
	 * default constructor
	 */
	public CIVersion() {
	}
	
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}


	/**
	 * @return the revisedContentItem_Id
	 */
	public ContentItem getRevisedContentItem() {
		return revisedContentItem;
	}

	/**
	 * @param revisedContentItem_Id the revisedContentItem_Id to set
	 */
	public void setRevisedContentItem(ContentItem revisedContentItem) {
		this.revisedContentItem = revisedContentItem;
	}

	/**
	 * @return the colour
	 */
	public String getColour() {
		return colour;
	}

	/**
	 * @param colour the colour to set
	 */
	public void setColour(String colour) {
		this.colour = colour;
	}

	/**
	 * @return the textContentUpdate
	 */
	public TextContentUpdate getTextContentUpdate() {
		return textContentUpdate;
	}


	/**
	 * @param textContentUpdate the textContentUpdate to set
	 */
	public void setTextContentUpdate(TextContentUpdate textContentUpdate) {
		this.textContentUpdate = textContentUpdate;
	}


	/**
	 * @return the mediaContentUpdate
	 */
	public MediaContentUpdate getMediaContentUpdate() {
		return mediaContentUpdate;
	}


	/**
	 * @param mediaContentUpdate the mediaContentUpdate to set
	 */
	public void setMediaContentUpdate(MediaContentUpdate mediaContentUpdate) {
		this.mediaContentUpdate = mediaContentUpdate;
	}

	/**
	 * @return the taggingUpdate
	 */
	public TaggingUpdate getTaggingUpdate() {
		return taggingUpdate;
	}


	/**
	 * @param taggingUpdate the taggingUpdate to set
	 */
	public void setTaggingUpdate(TaggingUpdate taggingUpdate) {
		this.taggingUpdate = taggingUpdate;
	}


	/**
	 * @return the ruleUpdate
	 */
	public RuleUpdate getRuleUpdate() {
		return ruleUpdate;
	}


	/**
	 * @param ruleUpdate the ruleUpdate to set
	 */
	public void setRuleUpdate(RuleUpdate ruleUpdate) {
		this.ruleUpdate = ruleUpdate;
	}


	/**
	 * @return the deletionUpdate
	 */
	public DeletionUpdate getDeletionUpdate() {
		return deletionUpdate;
	}


	/**
	 * @param deletionUpdate the deletionUpdate to set
	 */
	public void setDeletionUpdate(DeletionUpdate deletionUpdate) {
		this.deletionUpdate = deletionUpdate;
	}


	/**
	 * @return the renamingUpdate
	 */
	public RenamingUpdate getRenamingUpdate() {
		return renamingUpdate;
	}


	/**
	 * @param renamingUpdate the renamingUpdate to set
	 */
	public void setRenamingUpdate(RenamingUpdate renamingUpdate) {
		this.renamingUpdate = renamingUpdate;
	}
	
	/**
	 * @return the metadataUpdate
	 */
	public MetadataUpdate getMetadataUpdate() {
		return metadataUpdate;
	}


	/**
	 * @param metadataUpdate the metadataUpdate to set
	 */
	public void setMetadataUpdate(MetadataUpdate metadataUpdate) {
		this.metadataUpdate = metadataUpdate;
	}


	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}


	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the versionId
	 */
	public Long getVersionId() {
		return versionId;
	}

	/**
	 * @param versionId the versionId to set
	 */
	public void setVersionId(Long versionId) {
		this.versionId = versionId;
	}

	/**
	 * @return the revision
	 */
	public Revision getRevision() {
		return revision;
	}

	/**
	 * @param revision the revision to set
	 */
	public void setRevision(Revision revision) {
		this.revision = revision;
	}

	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(boolean origin) {
		this.origin = origin;
	}

	/**
	 * @return the origin
	 */
	public boolean isOrigin() {
		return origin;
	}
}
