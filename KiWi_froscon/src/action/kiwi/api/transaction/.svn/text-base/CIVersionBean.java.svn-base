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
package kiwi.api.transaction;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import kiwi.api.revision.CIVersionService;
import kiwi.exception.ContentItemMissingException;
import kiwi.exception.CyclicDependencyException;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.revision.CIVersion;
import kiwi.model.revision.DeletionUpdate;
import kiwi.model.revision.MediaContentUpdate;
import kiwi.model.revision.MetadataUpdate;
import kiwi.model.revision.RenamingUpdate;
import kiwi.model.revision.RuleUpdate;
import kiwi.model.revision.TaggingUpdate;
import kiwi.model.revision.TextContentUpdate;
import kiwi.model.tagging.Tag;

import org.jboss.seam.Component;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;

/**
 * The CIVersionBean holds the data that temporarily has to be stored during a transaction.
 * At the end of the transaction, the data in the CIVersionBean gets combined to one CIVersion,  
 * linked to a Revision object and stored in the database.
 * 
 * @author Stephanie Stroka
 *			(stephanie.stroka@salzburgresearch.at)
 *
 */
public class CIVersionBean implements Comparable<CIVersionBean> {

	private Set<KiWiTriple> 			transactionAddedTriples;
    private Set<KiWiTriple> 			transactionRemovedTriples;
    
    private List<Tag>					transactionAddedTags;
    private List<Tag>					transactionRemovedTags;
    
	private ContentItem 				contentItem;
	
	private CIVersion 					version;
	
	public CIVersionBean() {
		this.transactionAddedTags = new LinkedList<Tag>();
		this.transactionRemovedTags = new LinkedList<Tag>();
		this.transactionAddedTriples = new HashSet<KiWiTriple>();
		this.transactionRemovedTriples = new HashSet<KiWiTriple>();
	}
	
	/**
	 * returns the content item version
	 * @return
	 */
	public CIVersion getCIVersion() {
		return version;
	}
	
	/**
	 * @return the textContentUpdate
	 */
	public TextContentUpdate getTextContentUpdate() {
		if(version != null) {
			return version.getTextContentUpdate();
		} else {
			return null;
		}
	}

	/**
	 * @param textContentUpdate the textContentUpdate to set
	 * @throws ContentItemMissingException 
	 */
	public void setTextContentUpdate(TextContentUpdate textContentUpdate) throws ContentItemMissingException {
		ensureVersion();
		version.setTextContentUpdate(textContentUpdate);
	}

	/**
	 * @return the metadataUpdate
	 */
	public MetadataUpdate getMetadataUpdate() {
		if(version != null) {
			return version.getMetadataUpdate();
		} else {
			return null;
		}
	}

	/**
	 * @param metadataUpdate the metadataUpdate to set
	 * @throws ContentItemMissingException 
	 */
	public void setMetadataUpdate(MetadataUpdate metadataUpdate) throws ContentItemMissingException {
		ensureVersion();
		version.setMetadataUpdate(metadataUpdate);
	}
	
	/**
	 * @return the mediaContentUpdate
	 */
	public MediaContentUpdate getMediaContentUpdate() {
		if(version != null) {
			return version.getMediaContentUpdate();
		} else {
			return null;
		}
	}

	/**
	 * @param mediaContentUpdate the mediaContentUpdate to set
	 * @throws ContentItemMissingException 
	 */
	public void setMediaContentUpdate(MediaContentUpdate mediaContentUpdate) throws ContentItemMissingException {
		ensureVersion();
		version.setMediaContentUpdate(mediaContentUpdate);
	}

	/**
	 * @return the ruleUpdate
	 */
	public RuleUpdate getRuleUpdate() {
		if(version != null) {
			return version.getRuleUpdate();
		} else {
			return null;
		}
	}

	/**
	 * @param ruleUpdate the ruleUpdate to set
	 * @throws ContentItemMissingException 
	 */
	public void setRuleUpdate(RuleUpdate ruleUpdate) throws ContentItemMissingException {
		ensureVersion();
		version.setRuleUpdate(ruleUpdate);
	}

	/**
	 * @return the renamingUpdate
	 */
	public RenamingUpdate getRenamingUpdate() {
		if(version != null) {
			return version.getRenamingUpdate();
		} else {
			return null;
		}
	}

	/**
	 * @param renamingUpdate the renamingUpdate to set
	 * @throws ContentItemMissingException 
	 */
	public void setRenamingUpdate(RenamingUpdate renamingUpdate) throws ContentItemMissingException {
		ensureVersion();
		version.setRenamingUpdate(renamingUpdate);
	}

	/**
	 * @return the deletionUpdate
	 */
	public DeletionUpdate getDeletionUpdate() {
		if(version != null) {
			return version.getDeletionUpdate();
		} else {
			return null;
		}
	}

	/**
	 * @param deletionUpdate the deletionUpdate to set
	 * @throws ContentItemMissingException 
	 */
	public void setDeletionUpdate(DeletionUpdate deletionUpdate) throws ContentItemMissingException {
		ensureVersion();
		version.setDeletionUpdate(deletionUpdate);
	}

	/**
	 * @return the taggingUpdate
	 */
	public TaggingUpdate getTaggingUpdate() {
		if(version != null) {
			return version.getTaggingUpdate();
		} else {
			return null;
		}
	}

	/**
	 * @param taggingUpdate the taggingUpdate to set
	 * @throws ContentItemMissingException 
	 */
	public void setTaggingUpdate(TaggingUpdate taggingUpdate) throws ContentItemMissingException {
		ensureVersion();
		version.setTaggingUpdate(taggingUpdate);
	}

	/**
	 * @return the transactionAddedTags
	 */
	public List<Tag> getTransactionAddedTags() {
		return transactionAddedTags;
	}

	/**
	 * @param transactionAddedTags the transactionAddedTags to set
	 */
	public void setTransactionAddedTags(List<Tag> transactionAddedTags) {
		this.transactionAddedTags = transactionAddedTags;
	}

	/**
	 * @return the transactionRemovedTags
	 */
	public List<Tag> getTransactionRemovedTags() {
		return transactionRemovedTags;
	}

	/**
	 * @param transactionRemovedTags the transactionRemovedTags to set
	 */
	public void setTransactionRemovedTags(List<Tag> transactionRemovedTags) {
		this.transactionRemovedTags = transactionRemovedTags;
	}
	
	/**
	 * @return the transactionAddedTriples
	 */
	public Set<KiWiTriple> getTransactionAddedTriples() {
		return Collections.synchronizedSet(transactionAddedTriples);
	}
	
	/**
	 * add a triple to the list of triples that 
	 * have been added during the transaction
	 * @param triple
	 * @return
	 */
	public boolean addTransactionAddedTriples(KiWiTriple triple) {
		return transactionAddedTriples.add(triple);
	}
	
	/**
	 * remove a triple from the list of triples that 
	 * have been added during the transaction
	 * @param triple
	 * @return
	 */
	public boolean removeTransactionAddedTriple(KiWiTriple triple) {
		return transactionAddedTriples.remove(triple);
	}

	/**
	 * @return the transactionRemovedTriples
	 */
	public Set<KiWiTriple> getTransactionRemovedTriples() {
		return Collections.synchronizedSet(transactionRemovedTriples);
	}
	
	/**
	 * add a triple to the list of triples that 
	 * have been removed during the transaction
	 * @param triple
	 * @return
	 */
	public boolean addTransactionRemovedTriples(KiWiTriple triple) {
		transactionRemovedTriples.add(triple);
		return true;
	}
	
	/**
	 * remove a triple from the list of triples that 
	 * have been removed during the transaction
	 * @param triple
	 * @return
	 */
	public boolean removeTransactionRemovedTriple(KiWiTriple triple) {
		return transactionRemovedTriples.remove(triple);
	}
	
	/**
	 * @return the contentItem
	 */
	public ContentItem getContentItem() {
		return contentItem;
	}

	/**
	 * @param contentItem the contentItem to set
	 */
	public void setContentItem(ContentItem contentItem) {
		this.contentItem = contentItem;
	}

	@Override
	public int compareTo(CIVersionBean o) {
		ContentItem c1 = this.contentItem;
		ContentItem c2 = o.contentItem;
		boolean c2Inc1 = c1.getNestedContentItems().contains(c2);
		boolean c1Inc2 = c2.getNestedContentItems().contains(c1);
		if( c2Inc1 && c1Inc2 ) {
			try {
				throw new CyclicDependencyException("Cyclic dependencies between contentitems are not allowed");
			} catch (CyclicDependencyException e) {
				e.printStackTrace();
			}
			return 0;
		} else if(c2Inc1) {
			return 1;
		} else if (c1Inc2) {
			return -1;
		} else {
			return 0;
		}
	}
	
	
	private void ensureVersion() throws ContentItemMissingException {
		if(version == null) {
			CIVersionService ciVersionService = 
				(CIVersionService) Component.getInstance("ciVersionService");
			if(contentItem == null) {
				Log log = Logging.getLog(this.getClass());
				log.error("CIVersionBean is missing a content item");
				throw new ContentItemMissingException("CIVersionBean misses a content item");
			}
			version = ciVersionService.createCIVersion(contentItem);
		}
	}
}
