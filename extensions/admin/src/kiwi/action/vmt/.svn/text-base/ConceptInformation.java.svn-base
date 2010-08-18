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

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import kiwi.util.KiWiCollections;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 * 
 * @author Rolf Sint
 *			(rolf.sint@salzburgresearch.at)
 *
 */
@AutoCreate
@Name("conceptInformation")
@Scope(ScopeType.CONVERSATION)
public class ConceptInformation implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String preferedLabel;
	
	private String definition;
	
	private String author;
	
	private List<String> altLabels;
	
	@org.jboss.seam.annotations.Logger
	private org.jboss.seam.log.Log log;
	
	@In(required = false)
	private KiWiTreeNode actuKiWiTreeNode;
	
	public void setAltLabels(List<String> altLabels) {
		this.altLabels = altLabels;
	}
	
	public List<String> getAltLabels() {
		List<String> altLabels = new LinkedList<String>();
		if(actuKiWiTreeNode == null){
			return altLabels;
		}
		altLabels =  KiWiCollections.toList(actuKiWiTreeNode.getActualConcept().getAlternativeLabels());		
		for (String altLabel : altLabels) {
			log.debug("method: getAltLabels. altLabels: " + altLabel );
		}
		return altLabels;
	}
	
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public void setPreferedLabel(String preferedLabel) {
		this.preferedLabel = preferedLabel;
	}

	public String getPreferedLabel() {
		return preferedLabel;
	}	
}
