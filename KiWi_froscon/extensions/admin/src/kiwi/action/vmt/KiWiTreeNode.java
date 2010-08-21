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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import kiwi.action.webservice.thesaurusManagement.SKOSConceptUtils;
import kiwi.api.ontology.SKOSService;
import kiwi.model.ontology.SKOSConcept;
import kiwi.service.ontology.ConceptComparator;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("kiwiTreeNode")
@Scope(ScopeType.SESSION)
public class KiWiTreeNode implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private SKOSConcept actualConcept;
	private Locale l;
	private String language;
	private List<KiWiTreeNode> rootNodes;
	private LinkedList<KiWiTreeNode> children;
	private SKOSService skosService;

	public KiWiTreeNode(SKOSConcept actualConcept, SKOSService skosService, String language) {
		this.actualConcept = actualConcept;
		this.skosService = skosService;
		this.language = language;
	}

	public List<KiWiTreeNode> getRootNodes() {
		if (rootNodes == null) {
			rootNodes = new ArrayList<KiWiTreeNode>();
			List<SKOSConcept> topConcepts = skosService.getTopConcepts();
			for (Iterator iterator = topConcepts.iterator(); iterator.hasNext();) {
				SKOSConcept name = (SKOSConcept) iterator.next();
				rootNodes.add(new KiWiTreeNode(name, skosService, language));
			}
		}
		return rootNodes;
	}

	public LinkedList<KiWiTreeNode> getChildren() {
		if (children == null) {
			children = new LinkedList<KiWiTreeNode>();
			HashSet<SKOSConcept> narrowerConcepts = actualConcept.getNarrower();
			LinkedList narrowerConceptList = new LinkedList(narrowerConcepts);
			
			Collections.sort(narrowerConceptList, new ConceptComparator());
			
			for (Iterator iterator = narrowerConceptList.iterator(); iterator
					.hasNext();) {
				SKOSConcept name = (SKOSConcept) iterator.next();
				children.add(new KiWiTreeNode(name, skosService, language));
			}
		} 		
		return children;
	}
	
	public String toString() {
		l = new Locale(language);
		return SKOSConceptUtils.getConceptLabelInLanguage(actualConcept, l);
	}

	public SKOSConcept getActualConcept() {
		return actualConcept;
	}

	public void setActualConcept(SKOSConcept actualConcept) {
		this.actualConcept = actualConcept;
	}
	
	public void delete(KiWiTreeNode node) {			
			children.remove(node);
	}
	
	public void addChild(SKOSConcept sc) {
//		if(sc == null){
//			System.out.println("sc = null");
//		}
//		if(skosService == null){
//			System.out.println("skosService = null");
//		}
//		if(language == null){
//			System.out.println("language = null");
//		}
		if(children == null){
//			System.out.println("children = null");
			children = new LinkedList<KiWiTreeNode>();
		}
		
		children.add(new KiWiTreeNode(sc, skosService, language));
	}
}