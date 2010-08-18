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
package kiwi.action.inspector;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;

import kiwi.api.render.RenderingService;
import kiwi.api.revision.CIVersionService;
import kiwi.api.revision.RevisionBean;
import kiwi.api.revision.RevisionService;
import kiwi.api.revision.UpdateTextContentService.PreviewStyle;
import kiwi.exception.ContentItemDoesNotExistException;
import kiwi.exception.VersionDoesNotExistException;
import kiwi.model.content.ContentItem;
import kiwi.model.revision.CIVersion;
import kiwi.model.revision.DeletionUpdate;
import kiwi.model.revision.MetadataUpdate;
import kiwi.model.revision.RenamingUpdate;
import kiwi.model.revision.Revision;
import kiwi.model.revision.TaggingUpdate;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * Inspector class to debug one revision
 * @author Stephanie Stroka (stephanie.stroka@salzburgresearch.at)
 *
 */
@Name("kiwi.action.inspector.previewAction")
@Scope(ScopeType.PAGE)
@AutoCreate
public class PreviewAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Logger **/
	@Logger
	private Log log;
	
	/** the contentitem that we debug **/
	@In(create = true)
	private ContentItem currentContentItem;
	
	/** the revisionService that prepares the previews **/
	@In
	private RevisionService revisionService;
	
	/** the revisionService that prepares the previews **/
	@In
	private CIVersionService ciVersionService;
	
	/** rendering pipeline renders the text **/
	@In
    private RenderingService renderingPipeline;
	
	private RevisionBean selectedRevision;
	
	/** shows deleted text, tags, triples **/
	private Boolean showDeleted = true;
	
	private String title;
	
	private String author;
	
	/** the textual preview for the revision **/
	private String previewContentHtml;
	
	/** metadataUpdate for that version **/
	private MetadataUpdate metadataUpdate;
	
	/** taggingUpdate for that version **/
	private TaggingUpdate taggingUpdate;
	
	/** renamingUpdate for that revision **/
	private RenamingUpdate renamingUpdate;
	
	/** deletionUpdates for that revision **/
	private DeletionUpdate deletionUpdate;
	
	private List<ContentItem> affectedContentItems;
	
	/**
	 * Called by event from the InspectRevisionAction.
	 * Prepares the updates for previewing
	 * @return
	 */
	@Create
	public void begin() {
		ViewRevisionsAction ira = (ViewRevisionsAction) Component
				.getInstance("kiwi.action.inspector.viewRevisionsAction");
		assert ira != null;
		if(selectedRevision == null) {
			selectedRevision = ira.getSelectedRevision();
		}
		if(selectedRevision != null) {
			EntityManager entityManager = (EntityManager) Component.getInstance("entityManager");
			title = selectedRevision.getTitle();
			setAuthor(selectedRevision.getVersion().getRevision().getUser().getLogin());
    		
			String preText;
			if(selectedRevision.getVersion().getTextContentUpdate() != null) {
    			if(selectedRevision.getPreviewContentHtml() == null) {
					try {
						preText = ciVersionService.createPreview(selectedRevision.getVersion(), 
								PreviewStyle.LAST, showDeleted);
						previewContentHtml = renderingPipeline.renderHTML(preText, currentContentItem);
						selectedRevision.setPreviewContentHtml(previewContentHtml);
					} catch (VersionDoesNotExistException e) {
						e.printStackTrace();
					} catch (ContentItemDoesNotExistException e) {
						e.printStackTrace();
					}
    			} else {
    				previewContentHtml = selectedRevision.getPreviewContentHtml();
    			}
			}
			log.debug("preview text: #0 ", previewContentHtml);
			
			affectedContentItems = new LinkedList<ContentItem>();
			Revision rev = selectedRevision.getVersion().getRevision();
			entityManager.refresh(rev);
			for(CIVersion civ : rev.getContentItemVersions()) {
				affectedContentItems.add(civ.getRevisedContentItem());
			}
			// commented
			metadataUpdate = selectedRevision.getVersion().getMetadataUpdate();
			taggingUpdate = selectedRevision.getVersion().getTaggingUpdate();
    		renamingUpdate = selectedRevision.getVersion().getRenamingUpdate();
    		deletionUpdate = selectedRevision.getVersion().getDeletionUpdate();
		}
	}

	/**
	 * @return the previewContentHtml
	 */
	public String getPreviewContentHtml() {
		return previewContentHtml;
	}

	/**
	 * @param previewContentHtml the previewContentHtml to set
	 */
	public void setPreviewContentHtml(String previewContentHtml) {
		this.previewContentHtml = previewContentHtml;
	}

	/**
	 * @return the revisionService
	 */
	public RevisionService getRevisionService() {
		return revisionService;
	}

	/**
	 * @param revisionService the revisionService to set
	 */
	public void setRevisionService(RevisionService revisionService) {
		this.revisionService = revisionService;
	}

	/**
	 * @return the renderingPipeline
	 */
	public RenderingService getRenderingPipeline() {
		return renderingPipeline;
	}

	/**
	 * @param renderingPipeline the renderingPipeline to set
	 */
	public void setRenderingPipeline(RenderingService renderingPipeline) {
		this.renderingPipeline = renderingPipeline;
	}

	/**
	 * @return the selectedRevision
	 */
	public RevisionBean getSelectedRevision() {
		return selectedRevision;
	}

	/**
	 * @param selectedRevision the selectedRevision to set
	 */
	public void setSelectedRevision(RevisionBean selectedRevision) {
		this.selectedRevision = selectedRevision;
	}

	/**
	 * @return the showDeleted
	 */
	public Boolean getShowDeleted() {
		return showDeleted;
	}

	/**
	 * @param showDeleted the showDeleted to set
	 */
	public void setShowDeleted(Boolean showDeleted) {
		this.showDeleted = showDeleted;
	}

	/**
	 * @param metadataUpdate the metadataUpdate to set
	 */
	public void setMetadataUpdate(MetadataUpdate metadataUpdate) {
		this.metadataUpdate = metadataUpdate;
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
	 * @return the renamingUpdate
	 */
	public RenamingUpdate getRenamingUpdate() {
		return renamingUpdate;
	}

	/**
	 * @param renamingUpdate the renamingUpdate to set
	 */
	public void setRenamingUpdates(RenamingUpdate renamingUpdate) {
		this.renamingUpdate = renamingUpdate;
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
	public void setDeletionUpdates(DeletionUpdate deletionUpdate) {
		this.deletionUpdate = deletionUpdate;
	}

	/**
	 * @return the affectedContentItems
	 */
	public List<ContentItem> getAffectedContentItems() {
		return affectedContentItems;
	}

	/**
	 * @param affectedContentItems the affectedContentItems to set
	 */
	public void setAffectedContentItems(List<ContentItem> affectedContentItems) {
		this.affectedContentItems = affectedContentItems;
	}

	/**
	 * @return the metadataUpdate
	 */
	public MetadataUpdate getMetadataUpdate() {
		return metadataUpdate;
	}

	/**
	 * @param renamingUpdate the renamingUpdate to set
	 */
	public void setRenamingUpdate(RenamingUpdate renamingUpdate) {
		this.renamingUpdate = renamingUpdate;
	}

	/**
	 * @param deletionUpdate the deletionUpdate to set
	 */
	public void setDeletionUpdate(DeletionUpdate deletionUpdate) {
		this.deletionUpdate = deletionUpdate;
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
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}
}
