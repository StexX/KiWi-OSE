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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import kiwi.api.revision.RevisionBean;
import kiwi.model.content.ContentItem;
import kiwi.model.revision.CIVersion;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;

/**
 * @author Stephanie Stroka (sstroka@salzburgresearch.at)
 *
 */
@Name("kiwi.action.inspector.viewRevisionsAction")
@Scope(ScopeType.CONVERSATION)
@AutoCreate
public class ViewRevisionsAction implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@In(create=true,required=false)
	private ContentItem currentContentItem;
    
    protected RevisionBean selectedRevision;
  
    @DataModel(value="inspectorRevisions")
    private List<RevisionBean> revisions;
    
    @Begin(join=true)
    @Factory("inspectorRevisions")
    public void begin() {
    	if(currentContentItem.getVersions() != null) {
    		EntityManager entityManager = (EntityManager) Component.getInstance("entityManager");
    		entityManager.refresh(currentContentItem);
    		List<CIVersion> vers_tmp = currentContentItem.getVersions();
    		revisions = new ArrayList<RevisionBean>();
    		for(CIVersion v : vers_tmp) {
    			RevisionBean rb = new RevisionBean();
    			rb.setVersion(v);
    			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy 'at' HH:mm");
        		rb.setPrettyDate(sdf.format(v.getRevision().getCreationDate()));
    			rb.setTitle(v.getTitle());
    			rb.setUserlogin(v.getRevision().getUser().getLogin());
    			revisions.add(rb);
    		}
   		Collections.reverse(revisions);
     	} else {
    		revisions = new ArrayList<RevisionBean>();
    	}
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
	public String selectRevision(RevisionBean selectedRevision) {
		this.selectedRevision = selectedRevision;
		return "/inspector/preview.xhtml";
	}

	/**
	 * @return the revisions
	 */
	public List<RevisionBean> getRevisions() {
		return revisions;
	}

	/**
	 * @param revisions the revisions to set
	 */
	public void setRevisions(List<RevisionBean> revisions) {
		this.revisions = revisions;
	}
	
	/**
	 * 
	 * @return
	 */
	@End
	public String end() {
		return "/inspector/revisions.xhtml";
	}
}
