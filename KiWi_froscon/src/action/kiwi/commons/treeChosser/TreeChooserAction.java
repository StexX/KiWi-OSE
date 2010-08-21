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
package kiwi.commons.treeChosser;



import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

import kiwi.api.ontology.SKOSService;
import kiwi.model.ontology.SKOSConcept;

import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.log.Log;
import org.richfaces.component.UITree;
import org.richfaces.component.html.HtmlTree;
import org.richfaces.event.NodeSelectedEvent;


/**
 * @author Rolf Sint
 * @version 0.7
 * @since 0.7
 *
 */
public abstract class TreeChooserAction {
	
	
	//all concepts which are selected and chosen
	private LinkedList<SKOSConcept> chosenConcepts;
	
	private SKOSConcept selectedConcept;
	
	public SKOSConcept selectedTreeConcept;
	
	@In
	private Map<String, String> messages;
	
	@Logger
	private static Log log;
	
	@In(create=true)
	private SKOSService skosService;
	
	private kiwi.commons.treeChosser.TreeNode[] rootTreeNodes;
	
	
	@Begin(join=true)
	public TreeNode[] getTreeRoots() {
		if( rootTreeNodes == null ) {
			HashSet<SKOSConcept> rootNodes = skosService.getConcept(getRoot()).getNarrower();
			rootTreeNodes = new TreeNode[rootNodes.size()];
			
			int i = 0;
			for( SKOSConcept c : rootNodes ) {
				rootTreeNodes[i] = new kiwi.commons.treeChosser.TreeNode(c);
				i++;
			}
		}
		return rootTreeNodes;
	}
	
	public abstract String getRoot();
	
	protected abstract String getSelectToRemoveMsg();
	
	public String removeChosenConcept() {
		if( selectedConcept == null ) {
			return messages.get(getSelectToRemoveMsg());
		}
		if( chosenConcepts.contains(selectedConcept) ) {
			chosenConcepts.remove(selectedConcept);
		}
		return null;
	}
	
	public void setSelectedConcept(SKOSConcept c) {
		log.info("Selected concept #0",c.getPreferredLabel());
		this.selectedConcept = c;
	}
	
	public void setSelectedTreeConcept(SKOSConcept c) {
		log.info("Selected tree-concept #0",c.getPreferredLabel());
		this.selectedTreeConcept = c;
	}
	

	
	public void onNodeSelect(final NodeSelectedEvent event) {
		final UITree theTree = (HtmlTree) event.getComponent();
		if (null == theTree) {
			return;
		}
		final Object rowKey = theTree.getRowKey();
		// this works better
		final Object rowData = theTree.getRowData(rowKey);
		if (rowData instanceof TreeNode) {
			setSelectedTreeConcept(((kiwi.commons.treeChosser.TreeNode) rowData).getConcept());
		}
	}
	
	public LinkedList<SKOSConcept> getChosenConcepts() {
		if( chosenConcepts != null )
			return chosenConcepts;
		else
			return new LinkedList<SKOSConcept>();
	}
	
	public void setChosenConcepts(LinkedList<SKOSConcept> positions) {
		this.chosenConcepts = positions;
	}

	public String addChosenConcept() {
		
		if( chosenConcepts == null ) {
			chosenConcepts = new LinkedList<SKOSConcept>();
		}
		
		if( selectedTreeConcept == null ) {
			return messages.get(getSelectToAddMsg());
		}
		
		if( !selectedTreeConcept.getNarrower().isEmpty() ) {
			return "'"+selectedTreeConcept.getPreferredLabel() +"' "+messages.get(getNotValidMsg());
		}
		
		if( chosenConcepts.contains(selectedTreeConcept) ) {
			
			return "Position '"+ selectedTreeConcept.getPreferredLabel()+ "' " +messages.get(getAlreadySelectedMsg());
		}
		
		chosenConcepts.add(selectedTreeConcept);
		return null;
 	}
	
	public abstract String getNotValidMsg();
	
	public abstract String getSelectToAddMsg();
	
	public abstract String getAlreadySelectedMsg();
	
	public void clear(){
		chosenConcepts = new LinkedList<SKOSConcept>();
	}

}
