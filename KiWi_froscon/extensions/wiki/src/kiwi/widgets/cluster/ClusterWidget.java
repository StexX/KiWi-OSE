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
package kiwi.widgets.cluster;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import kiwi.api.cluster.ClusterService;
import kiwi.model.cluster.TagCluster;
import kiwi.model.content.ContentItem;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;

/**
 * An action component for backing the spectral cluster widget.
 * 
 * @author Fred Durao
 *
 */
@Name("clusterWidget")
@Scope(ScopeType.CONVERSATION)
@AutoCreate
public class ClusterWidget implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static int MAX_TITLE_LENGTH = 25;	

	@In(create=true)
	private ClusterService clusterService;	

	private List<ClusterNode> clusterNode;

	public List<ClusterNode> getClusterNode() {
		if(clusterNode == null) {
			clusterNode = new ArrayList<ClusterNode>();

			List<TagCluster> tagClusters = clusterService.listTagCluster();
			
			for (TagCluster tagCluster: tagClusters) {
					// create new node for property
					ClusterNode node = new ClusterNode(tagCluster.getCluster());
					clusterNode.add(node);
					
					// add all objects reachable by this property to the children of the node
					for(ContentItem t : tagCluster.getContentItems()) {
						ClusterNode child = new ClusterNode(t);
						node.getChildren().add(child);
					}
			}			
		}
		return clusterNode;
	}

	public void clear() {
		clusterNode = null;
	}
	
	/**
	 * Compute the tag x doc cluster
	 */
	public void calculateSpectralCluster(){
		String message = "Tag cluster is calculated. See the results at any WiKi page.";
		try {
			clusterService.calculateSpectralCluster();	
		} catch (Exception e) {
			message = "Spectral Cluster could not be computed due to "+e.getMessage();
		}
		FacesMessages.instance().add(message);		

	}
	
	/**
	 * @param s
	 * @return
	 */
	public String ellipseString(String s) {
		if(s.length() > MAX_TITLE_LENGTH) {
			return s.substring(0, MAX_TITLE_LENGTH) + "...";
		} else {
			return s;
		}
	}

	/**
	 * A bean class to represent tree nodes using the rich:recursiveTreeAdaptor.
	 * 
	 * @author Fred Durao
	 *
	 */
	public static class ClusterNode {
		
		private ContentItem data;
		
		private String tagKeys;		

		private List<ClusterNode> children;

		private ClusterNode(ContentItem data) {
			this.data = data;
			this.children = new LinkedList<ClusterNode>();
		}
		
		private ClusterNode(String tagKeys) {
			this.tagKeys = tagKeys;
			this.children = new LinkedList<ClusterNode>();
		}	
		
		/**
		 * @return the children
		 */
		public List<ClusterNode> getChildren() {
			return children;
		}

		/**
		 * @param children the children to set
		 */
		public void setChildren(List<ClusterNode> children) {
			this.children = children;
		}

		public ContentItem getData() {
			return data;
		}


		public void setData(ContentItem data) {
			this.data = data;
		}


		public String getTagKeys() {
			return tagKeys;
		}


		public void setTagKeys(String tagKeys) {
			this.tagKeys = tagKeys;
		}		
		
	}
}
