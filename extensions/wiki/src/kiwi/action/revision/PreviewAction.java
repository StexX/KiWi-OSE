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

package kiwi.action.revision;

import java.io.Serializable;

import kiwi.action.revision.style.StyleCreator;
import kiwi.api.render.RenderingService;
import kiwi.api.revision.CIVersionService;
import kiwi.api.revision.RevisionBean;
import kiwi.api.revision.RevisionService;
import kiwi.api.revision.UpdateTextContentService.PreviewStyle;
import kiwi.exception.ContentItemDoesNotExistException;
import kiwi.exception.VersionDoesNotExistException;
import kiwi.model.content.ContentItem;
import kiwi.model.revision.MetadataUpdate;
import kiwi.model.revision.TaggingUpdate;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * @author Stephanie Stroka (sstroka@salzburgresearch.at)
 *
 */
@Name("kiwi.action.revision.previewAction")
@Scope(ScopeType.CONVERSATION)
public class PreviewAction implements Serializable {
	
	private static final long serialVersionUID = 5668835011461415359L;

	@Logger
	private static Log log;

    @In(create=true)
	private ContentItem currentContentItem;
	
    private String previewContentHtml;
	
    @In(required=false)
	private RevisionBean selectedRevision;

	private String title;

	private boolean showDeleted;
	
	private String selectedStyle;
	
	@In
	private CIVersionService ciVersionService;
	
	@In
	private RevisionService revisionService;
	
	@In
    private RenderingService renderingPipeline;
	
	/** metadataUpdate for that version **/
	private MetadataUpdate metadataUpdate;
	
	/** taggingUpdate for that version **/
	private TaggingUpdate taggingUpdate;
	
	private int indexRevision;

	public void beginPreview() {
		StyleCreator styleCreator = (StyleCreator) Component.getInstance("kiwi.action.revision.style.styleCreator");
		styleCreator.init();
		styleCreator.generateStyle();
		
		ViewRevisionsAction vra = (ViewRevisionsAction) Component
				.getInstance("kiwi.action.revision.viewRevisionsAction");
		assert vra != null;
		if(selectedRevision == null) {
			selectedRevision = vra.getSelectedRevision();
		}
		if(selectedRevision != null) {
			title = selectedRevision.getTitle();
			
    		try {
    			if(selectedRevision.getPreviewContentHtml() == null) {
	    			previewContentHtml = ciVersionService.createPreview(selectedRevision.getVersion(), 
							PreviewStyle.LAST, showDeleted);

	    			previewContentHtml = renderingPipeline.renderHTML(previewContentHtml, currentContentItem);
    			} else {
    				previewContentHtml = selectedRevision.getPreviewContentHtml();
    			}
    			//TODO: commented
    			metadataUpdate = selectedRevision.getVersion().getMetadataUpdate();
    			taggingUpdate = selectedRevision.getVersion().getTaggingUpdate();
    			log.debug("preview text: #0 ", previewContentHtml);
			} catch (VersionDoesNotExistException e) {
				log.error("Problem with viewing text content update: #0 ", e);
			} catch (ContentItemDoesNotExistException e) {
				log.error("Problem with viewing text content update: #0 ", e);
			}
    		log.info("rendering html content: #0", previewContentHtml);
		}
	}
	
	/**
	 * @return the previewContentHtml
	 */
	public String getPreviewContentHtml() {
		log.info("getPreviewContentHtml: #0", previewContentHtml);
		return previewContentHtml;
	}
	
	/**
	 * @param previewContentHtml the previewContentHtml to set
	 */
	public void setPreviewContentHtml(String previewContentHtml) {
		log.info("setPreviewContentHtml: #0", previewContentHtml);
		this.previewContentHtml = previewContentHtml;
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
	
	public String restore() {
		ViewRevisionsAction viewRevisionAction = (ViewRevisionsAction) 
			Component.getInstance("kiwi.action.revision.viewRevisionsAction");
		selectedRevision = viewRevisionAction.getWikiRevisions()
			.get(viewRevisionAction.getWikiRevisions().size() - indexRevision);
		revisionService.restore(selectedRevision.getVersion().getRevision());
		return "/wiki/home.xhtml";
	}

	/**
	 * @return the showDeleted
	 */
	public boolean isShowDeleted() {
		return showDeleted;
	}

	/**
	 * @param showDeleted the showDeleted to set
	 */
	public void setShowDeleted(boolean showDeleted) {
		ViewRevisionsAction viewRevisionAction = (ViewRevisionsAction) 
			Component.getInstance("kiwi.action.revision.viewRevisionsAction");
		this.showDeleted = showDeleted;
		selectedRevision = viewRevisionAction.getWikiRevisions()
			.get(viewRevisionAction.getWikiRevisions().size() - indexRevision);
		beginPreview();
	}
	
	public String refresh() {
		return "/wiki/preview.xhtml";
	}
	
	/**
	 * @return the selectedStyle
	 */
	public String getSelectedStyle() {
		return selectedStyle;
	}

	/**
	 * @param selectedStyle the selectedStyle to set
	 */
	public void setSelectedStyle(String selectedStyle) {
		this.selectedStyle = selectedStyle;
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
	 * @param indexRevision the indexRevision to set
	 */
	public void setIndexRevision(int indexRevision) {
		ViewRevisionsAction viewRevisionAction = (ViewRevisionsAction) 
			Component.getInstance("kiwi.action.revision.viewRevisionsAction");
		this.indexRevision = indexRevision;
		selectedRevision = viewRevisionAction.getWikiRevisions()
			.get(viewRevisionAction.getWikiRevisions().size() - indexRevision);
		beginPreview();
	}

	/**
	 * @return the indexRevision
	 */
	public int getIndexRevision() {
		return indexRevision;
	}	
}
