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
package interedu.action.vtm;

import java.io.Serializable;

import kiwi.api.ontology.SKOSService;
import kiwi.model.ontology.SKOSConcept;

import interedu.api.configuration.Constants;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.richfaces.model.TreeNodeImpl;

@Name("interedu.treeAction")
@Scope(ScopeType.CONVERSATION)
public class KiWiTreeAction implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//root node of the tree
	private TreeNodeImpl<KiWiTreeNodeData> root;
	
	@In(create=true)
	private SKOSService skosService;
	
	@Logger
	Log log;
	
	/**
	 * if root does not exists yet, a treeNode is created and filled with children that
	 * represent all top concepts.
	 * @return root node
	 */
	public TreeNodeImpl<KiWiTreeNodeData> getRoot() {
		
		if(root == null) {
		
			root = new TreeNodeImpl<KiWiTreeNodeData>();
		
			SKOSConcept concept = skosService.getConcept(Constants.ROOT_CONCEPT_NAME);
			
			if( concept != null ) {
				KiWiTreeNode node = new KiWiTreeNode();
				node.setData(new KiWiTreeNodeData(concept));
				root.addChild(0, node);
			} else {
				log.error("no concept with name #0", Constants.ROOT_CONCEPT_NAME);
			}
			
		}
		
		return root;
	}
}
