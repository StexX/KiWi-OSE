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
 * sschaffe
 * 
 */
package kiwi.action.ui;

import java.util.LinkedList;
import java.util.List;

import kiwi.api.content.ContentItemService;
import kiwi.api.perspectives.PerspectiveService;
import kiwi.model.content.ContentItem;
import kiwi.model.perspective.Perspective;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

/**
 * The PerspectiveAction is a core view backing bean that keeps track of the perspective that has been
 * chosen for the currently active content item.
 *
 * @author Sebastian Schaffert
 *
 */
@Name("kiwi.ui.perspectiveAction")
@Scope(ScopeType.CONVERSATION)
@AutoCreate
public class PerspectiveAction {

	@Logger
    private Log log;
	
    @In 
    private FacesMessages facesMessages;

    @In
	private PerspectiveService perspectiveService;
	
	@In
	private ContentItem currentContentItem;
	
	private Perspective currentPerspective;
	
	private List<String> selectedPerspectives;
	
	private List<String> availablePerspectives;
	
    @Create
	public void begin() {
    	if(currentContentItem.getPerspectives().size() == 0) {
    		perspectiveService.attachPerspective(currentContentItem, perspectiveService.getDefaultPerspective());
    		currentPerspective = perspectiveService.getDefaultPerspective();
    	} else {
    		currentPerspective = currentContentItem.getPerspectives().get(0);
    	}
    	selectedPerspectives = new LinkedList<String>();
    	for(Perspective p : currentContentItem.getPerspectives()) {
    		selectedPerspectives.add(p.getName());
    	}
    	log.info("PerspectiveAction initalised; current content item has #0 perspectives.",currentContentItem.getPerspectives().size());
    }

	/**
	 * @return the currentPerspective
	 */
	public Perspective getCurrentPerspective() {
		return currentPerspective;
	}

	/**
	 * @param currentPerspective the currentPerspective to set
	 */
	public void setCurrentPerspective(Perspective currentPerspective) {
		log.info("switching to perspective #0",currentPerspective.getName());
		this.currentPerspective = currentPerspective;
	}
    

	/**
	 * Return the complete JSF path of the current perspective's view template.
	 * @return
	 */
    public String getViewTemplate() {
    	return "/perspectives/"+currentPerspective.getViewTemplate();
    }
    
    /**
     * Return the complete JSF path of the current perspective's edit template.
     * @return
     */
    public String getEditTemplate() {
    	return "/perspectives/"+currentPerspective.getEditTemplate();    	
    }
    
    
    public List<Perspective> listPerspectives() {
    	return currentContentItem.getPerspectives();
    }

	/**
	 * @return the selectedPerspectives
	 */
	public List<String> getSelectedPerspectives() {
		return selectedPerspectives;
	}

	/**
	 * @param selectedPerspectives the selectedPerspectives to set
	 */
	public void setSelectedPerspectives(List<String> selectedPerspectives) {
		if(selectedPerspectives.size() > 0) {
			this.selectedPerspectives = selectedPerspectives;
			
		} else {
			facesMessages.add("please select at least one perspective!");
		}

	}
	
	public List<String> getAvailablePerspectives() {
		if(availablePerspectives == null) {
			availablePerspectives = new LinkedList<String>();
			for(Perspective p : perspectiveService.listPerspectives()) {
				if(!selectedPerspectives.contains(p.getName())) {
					availablePerspectives.add(p.getName());
				}
			}
		}
		return availablePerspectives;
	}
	
	
    
    /**
	 * @param allPerspectives the allPerspectives to set
	 */
	public void setAvailablePerspectives(List<String> allPerspectives) {
		this.availablePerspectives = allPerspectives;
	}

	public void saveSelectedPerspectives() {
		List<Perspective> result = new LinkedList<Perspective>();
    	for(String p : selectedPerspectives) {
    		result.add(perspectiveService.getPerspective(p));
    	}
		log.info("adding #0 perspectives",result.size());
    	
		currentContentItem.setPerspectives(result);
   	
    	ContentItemService contentItemService = (ContentItemService) Component.getInstance("contentItemService");
    	contentItemService.saveContentItem(currentContentItem);
    }
}
