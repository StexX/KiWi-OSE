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
package kiwi.action.vmt;

/**
*
* @author rsint
*/

import java.io.Serializable;
import java.util.Locale;

import kiwi.api.content.ContentItemService;
import kiwi.exception.NamespaceResolvingException;
import kiwi.model.kbase.KiWiResource;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

@Name("vmtFormAction")
@Scope(ScopeType.CONVERSATION)
public class VMTFormAction implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@In(required = false)
	private KiWiTreeNode actuKiWiTreeNode;
	
	@In(required = true)
	private ConceptInformation conceptInformation;
	
	@In(required = true)
	private String language;
	
	@In
	private ContentItemService contentItemService;
	
	@Logger
	private Log log;
	
	public void saveForm(){
		if(actuKiWiTreeNode.getActualConcept() == null){
			log.info("Concept is null");
		}
		log.info("actualNode: "+actuKiWiTreeNode.getActualConcept().getPreferredLabel());
//		actuKiWiTreeNode.getActualConcept().setPreferredLabel(conceptInformation.getPreferedLabel());		
		KiWiResource kr = actuKiWiTreeNode.getActualConcept().getResource();
		try {
			kr.setProperty("http://www.w3.org/2004/02/skos/core#prefLabel", conceptInformation.getPreferedLabel(), new Locale(language));
			contentItemService.updateTitle(actuKiWiTreeNode.getActualConcept(), conceptInformation.getPreferedLabel());
		} catch (NamespaceResolvingException e) {
			e.printStackTrace();
		}
		
		actuKiWiTreeNode.getActualConcept().setDefinition(conceptInformation.getDefinition());
	}
}
