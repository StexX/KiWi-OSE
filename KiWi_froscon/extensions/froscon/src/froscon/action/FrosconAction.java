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


package froscon.action;



import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.tagging.TaggingService;
import kiwi.model.content.ContentItem;
import kiwi.model.tagging.Tag;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import froscon.model.FrosconBugFacade;
import froscon.model.FrosconProjectFacade;


@Name("frosconAction")
@Scope(ScopeType.PAGE)
public class FrosconAction implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    /**
     * All the log messages will go through this member.
     */
    @Logger
    private Log log;

    private String projectName;
    
    private String stage;
    
    private List<String> allStages;
    
    private List<BugBackingBean> bugs;
    
    private List<String> allStates;
    
    @In
    private KiWiEntityManager kiwiEntityManager;
    
    @In
    private ContentItemService contentItemService;
    
    @In
    private TaggingService taggingService;
    
    @In
    private User currentUser;
    
    @Create
    public void init() {
    	log.debug("Init Froscon Action");
    	allStages = new LinkedList<String>();
    	allStages.add("Planning");
    	allStages.add("Alpha");
    	allStages.add("Beta");
    	allStages.add("Stable");
    	allStages.add("Inactive");
    	
    	allStates = new LinkedList<String>();
		allStates.add("Fixed");
		allStates.add("Won't fix");
		allStates.add("Cannot reproduce");
		allStates.add("Duplicate");
    	bugs = new LinkedList<BugBackingBean>();
    }
	
    public void addABug() {
    	BugBackingBean bug = new BugBackingBean();
    	bugs.add(bug);
    }
	
	public List<String> getAllStages() {
		return allStages;
	}

	public void setAllStages(List<String> allStages) {
		this.allStages = allStages;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}
	
	public List<BugBackingBean> getBugs() {
		return bugs;
	}

	public void setBugs(List<BugBackingBean> bugs) {
		this.bugs = bugs;
	}

	public List<String> getAllStates() {
		return allStates;
	}

	public void setAllStates(List<String> allStates) {
		this.allStates = allStates;
	}

	public void store() {
		final ContentItem projectCI = contentItemService.createContentItem();
		projectCI.setTitle(projectName);
		FrosconProjectFacade projectFacade = kiwiEntityManager.
			createFacade(projectCI, FrosconProjectFacade.class);
		
		HashSet<ContentItem> projectBugs = new HashSet<ContentItem>();
		for(BugBackingBean b : bugs) {
			final ContentItem bugCI = contentItemService.createContentItem();
			bugCI.setTitle(b.getTitle());
			
			FrosconBugFacade bugFacade = kiwiEntityManager.
				createFacade(bugCI, FrosconBugFacade.class);

			contentItemService.updateTextContentItem(bugFacade.getDelegate(), 
					b.getDescription());
			bugFacade.setStatus(b.getStatus());
			
			kiwiEntityManager.persist(bugFacade);
			
			projectBugs.add(bugFacade.getDelegate());
		}
		
		projectFacade.setStage(stage);
		projectFacade.setBugs(projectBugs);
		
		ContentItem bugIssueCI = contentItemService.getContentItemByTitle("Bug Issue");
		if(bugIssueCI == null) {
			bugIssueCI = contentItemService.createContentItem();
			bugIssueCI.setTitle("Bug Issue");
		}
		
		contentItemService.updateTextContentItem(projectFacade.getDelegate(), 
				"Status " + stage);
		
		taggingService.createTagging("Bug Issue", projectFacade.getDelegate(), 
				bugIssueCI, currentUser);
		
		kiwiEntityManager.persist(projectFacade);
    }

}
