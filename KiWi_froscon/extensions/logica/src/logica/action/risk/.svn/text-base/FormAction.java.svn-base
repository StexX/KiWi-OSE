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

package logica.action.risk;

import java.io.Serializable;

import kiwi.api.config.ConfigurationService;
import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.content.ContentItem;
import kiwi.model.user.User;
import logica.api.risk.RiskFacade;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * @author devadmin
 * 
 */

@Scope(ScopeType.CONVERSATION)
@Name("formAction")
public class FormAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@In(required = false)
	@Out(required = false)
	private ContentItem currentContentItem;
	
	@org.jboss.seam.annotations.Logger
	Log log;
	
	private RiskFacade risk;
	
    @In
    private KiWiEntityManager kiwiEntityManager;
	
	@In
	private ContentItemService contentItemService;
	
	@In(create = true)
	private User currentUser;
	
	@In(value = "configurationService")
	private ConfigurationService configurationService;
	
	@In
	private TripleStore tripleStore;
	
    @In(required = false)
    private RiskFacade selectedRisk;


	public RiskFacade getSelectedRisk() {
		return selectedRisk;
	}

	public void setSelectedRisk(RiskFacade selectedRisk) {
		this.selectedRisk = selectedRisk;
	}

	//this method is responsible for loading or creating (if it does not exist) a new content item with the given name. 
	//it associates the html form with the content item
	public String go(String title, String formName) {
		log.info("SelectedRisk : "+selectedRisk);
		currentContentItem = contentItemService.getContentItemByTitle(title);
		
		
		if (currentContentItem != null) {
			risk = kiwiEntityManager.createFacade(currentContentItem, RiskFacade.class);
			risk.setFileReference(formName);
		}
		else
		{
		currentContentItem = tripleStore
					.createUriResource(configurationService
							.getBaseUri()
							+ "/content/"
							+ Long.toHexString(System
									.currentTimeMillis())).getContentItem();
	
		contentItemService.updateTitle(currentContentItem, title);
		currentContentItem.setAuthor(currentUser);

		risk = kiwiEntityManager.createFacade(currentContentItem, RiskFacade.class);
		risk.setFileReference(formName);
		}
	return "/logica/index.xhtml";
	}
}
