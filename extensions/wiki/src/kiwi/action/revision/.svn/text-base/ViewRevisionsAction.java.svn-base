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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;

import kiwi.api.revision.RevisionBean;
import kiwi.model.content.ContentItem;
import kiwi.model.revision.CIVersion;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;

/**
 * @author 	Stephanie Stroka 
 * 			(sstroka@salzburgresearch.at)
 *
 */
@Name("kiwi.action.revision.viewRevisionsAction")
@Scope(ScopeType.CONVERSATION)
public class ViewRevisionsAction implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@In(create=true,required=false)
	private ContentItem currentContentItem;
    
//    private String updateType;

    @Out(required=false,scope=ScopeType.CONVERSATION)
    private RevisionBean selectedRevision;
    
    private boolean revisionSelected = false;
  
    @DataModel
    private List<RevisionBean> wikiRevisions;
    
    private int countRevisions;
    
    private Integer selectedRevIndex;
    
    @In
    private EntityManager entityManager;
    
    private boolean onlyShowContentUpdates = true;
    
    @Begin(join=true)
    @Factory(value="wikiRevisions")
    public void begin() {
    	entityManager.refresh(currentContentItem);
    	if(currentContentItem.getVersions() != null) {
    		List<CIVersion> rev_tmp = currentContentItem.getVersions();
    		wikiRevisions = new ArrayList<RevisionBean>();
    		for(int i=rev_tmp.size()-1; i>=0; i--) {
    			CIVersion v = rev_tmp.get(i);
    			RevisionBean rb = new RevisionBean();
    			rb.setVersion(v);
    			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy 'at' HH:mm");
        		rb.setPrettyDate(sdf.format(v.getRevision().getCreationDate()));
    			rb.setTitle(v.getTitle());
    			rb.setUserlogin(v.getRevision().getUser().getLogin());
    			wikiRevisions.add(rb);
    		}
     	} else {
     		wikiRevisions = new ArrayList<RevisionBean>();
    	}
    	setCountRevisions(wikiRevisions.size());
	}
    
    public String selectRevision(RevisionBean rev) {
    	PreviewAction previewAction = (PreviewAction) 
    		Component.getInstance("kiwi.action.revision.previewAction");
    	selectedRevision = rev;
    	previewAction.setIndexRevision(wikiRevisions.size() - wikiRevisions.indexOf(rev));
    	return "/wiki/preview.xhtml";
    }

	/**
	 * @return the revisionSelected
	 */
	public boolean isRevisionSelected() {
		return revisionSelected;
	}

	/**
	 * @return the selectedRevision
	 */
	public RevisionBean getSelectedRevision() {
		return selectedRevision;
	}

	/**
	 * @param revisionSelected the revisionSelected to set
	 */
	public void setRevisionSelected(boolean revisionSelected) {
		this.revisionSelected = revisionSelected;
	}

	/**
	 * @return the revisions
	 */
	public List<RevisionBean> getWikiRevisions() {
		return wikiRevisions;
	}

	/**
	 * @param wikiRevisions the wikiRevisions to set
	 */
	public void setWikiRevisions(List<RevisionBean> wikiRevisions) {
		this.wikiRevisions = wikiRevisions;
	}

	public String compare() {
		ComparePreviewAction comparePreviewAction = (ComparePreviewAction) 
			Component.getInstance("kiwi.action.revision.comparePreviewAction");
		List<RevisionBean> selectedRevisions = new LinkedList<RevisionBean>();
		for(RevisionBean rev : wikiRevisions) {
			if(rev.isChecked() == true) {
				selectedRevisions.add(0,rev);
			}
		}
		if(selectedRevisions.size() != 2) {
			return "/wiki/history.xhtml";
		} else {
			Iterator<RevisionBean> it = selectedRevisions.iterator();
			// the first it.next() should always be the 'younger' version
			comparePreviewAction.compare(it.next(), it.next());
			return "/wiki/compare_revisions.xhtml";
		}
	}
	/**
	 * 
	 * @return
	 */
	@End
	public String end() {
		return "/wiki/history.xhtml";
	}

	/**
	 * @param countRevisions the countRevisions to set
	 */
	public void setCountRevisions(int countRevisions) {
		this.countRevisions = countRevisions;
	}

	/**
	 * @return the countRevisions
	 */
	public int getCountRevisions() {
		return countRevisions;
	}

	/**
	 * @param selectedRevIndex the selectedRevIndex to set
	 */
	public void setSelectedRevIndex(Integer selectedRevIndex) {
		this.selectedRevIndex = selectedRevIndex;
	}

	/**
	 * @return the selectedRevIndex
	 */
	public Integer getSelectedRevIndex() {
		return selectedRevIndex;
	}

	/**
	 * @return the onlyShowTextContentUpdates
	 */
	public boolean isOnlyShowContentUpdates() {
		return onlyShowContentUpdates;
	}

	/**
	 * @param onlyShowTextContentUpdates the onlyShowTextContentUpdates to set
	 */
	public void setOnlyShowContentUpdates(boolean onlyShowContentUpdates) {
		this.onlyShowContentUpdates = onlyShowContentUpdates;
	}
	
	public boolean showRevision(RevisionBean rev) {
		if(rev == null || rev.getVersion() == null) {
			return false;
		}
		if(!onlyShowContentUpdates) {
			return true;
		} else if(rev.getVersion().getTextContentUpdate() != null || rev.getVersion().getMediaContentUpdate() != null){
			return true;
		} else {
			return false;
		}
	}
}
