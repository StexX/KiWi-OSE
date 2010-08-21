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
package kiwi.widgets.references;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kiwi.api.event.KiWiEvents;
import kiwi.exception.NamespaceResolvingException;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiTriple;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;

/**
 * An action component for backing the references widget.
 * 
 * @author Sebastian Schaffert
 *
 */
@Name("referencesWidget")
@Scope(ScopeType.CONVERSATION)
@AutoCreate
public class ReferencesWidget implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int limit = 100;
	
	int incomingCount = 0;
	int outgoingCount = 0;
	
	@In
	private ContentItem currentContentItem;
	
	
	private List<ReferencesNode> incoming, outgoing;
	
	public List<ReferencesNode> getIncoming() {
		if(incoming == null) {
			incoming = new ArrayList<ReferencesNode>();
			
			// get incoming triples up to limit
			Collection<KiWiTriple> incomingTriples;
			try {
				incomingTriples = currentContentItem.getResource().listIncoming(null,limit);
			} catch (NamespaceResolvingException e) {
				e.printStackTrace();
				incomingTriples = Collections.emptySet();
			}
			incomingCount = incomingTriples.size();
			
			Map<KiWiResource,List<KiWiTriple>> groupings = calculateGrouping(incomingTriples);
			
			for(KiWiResource prop : groupings.keySet()) {
				// create new node for property
				ReferencesNode node = new ReferencesNode(prop);
				incoming.add(node);
				
				// add all objects reachable by this property to the children of the node
				for(KiWiTriple t : groupings.get(prop)) {
					ReferencesNode child = new ReferencesNode((KiWiResource)t.getSubject());
					if(t.isInferred()) {
						child.setInferred(true);
					}
					child.setTripleId(t.getId());					
					node.getChildren().add(child);
				}
				
			}
		}
		return incoming;
	}
	
	
	public List<ReferencesNode> getOutgoing() {
		if(outgoing == null) {
			outgoing = new ArrayList<ReferencesNode>();
			
			// get outgoing triples up to limit
			Collection<KiWiTriple> outgoingTriples;
			try {
				outgoingTriples = currentContentItem.getResource().listOutgoing(null,limit,false);
			} catch (NamespaceResolvingException e) {
				e.printStackTrace();
				outgoingTriples = Collections.emptySet();
			}
			outgoingCount = outgoingTriples.size();
			
			Map<KiWiResource,List<KiWiTriple>> groupings = calculateGrouping(outgoingTriples);
			
			for(KiWiResource prop : groupings.keySet()) {
				// create new node for property
				ReferencesNode node = new ReferencesNode(prop);
				outgoing.add(node);
				
				// add all objects reachable by this property to the children of the node
				for(KiWiTriple t : groupings.get(prop)) {
					ReferencesNode child = new ReferencesNode((KiWiResource)t.getObject());
					if(t.isInferred()) {
						child.setInferred(true);
					}
					child.setTripleId(t.getId());					
					node.getChildren().add(child);
				}
				
			}
		}
		return outgoing;
	}

	
	/**
	 * A method that groups the collection of triples passed as argument by their property
	 * @param triples
	 * @return
	 */
	private Map<KiWiResource,List<KiWiTriple>> calculateGrouping(Iterable<KiWiTriple> triples) {
		HashMap<KiWiResource,List<KiWiTriple>> grouping = new HashMap<KiWiResource, List<KiWiTriple>>();
		
		for(KiWiTriple t : triples) {
			// for each triple, if the object is a resource, create an entry into the map based on
			// the property by adding the triple to the list of grouped triples; a new list 
			// is created if needed
			if(t.getObject() instanceof KiWiResource) {
				if(grouping.get(t.getProperty()) == null) {
					grouping.put(t.getProperty(),new LinkedList<KiWiTriple>());				
				}
				grouping.get(t.getProperty()).add(t);
			}
		}
		// sort the individual lists
		for(List<KiWiTriple> l : grouping.values()) {
			Collections.sort(l, new Comparator<KiWiTriple>() {
				@Override
				public int compare(KiWiTriple o1, KiWiTriple o2) {
					return KiWiResource.LabelComparator.getInstance().compare((KiWiResource)o1.getObject(), (KiWiResource)o2.getObject());
				}
				
			});
		}
		
		return grouping;
	}
	
	
	
	
	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}


	/**
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}




	/**
	 * @return the incomingCount
	 */
	public int getIncomingCount() {
		return incomingCount;
	}


	/**
	 * @param incomingCount the incomingCount to set
	 */
	public void setIncomingCount(int incomingCount) {
		this.incomingCount = incomingCount;
	}


	/**
	 * @return the outgoingCount
	 */
	public int getOutgoingCount() {
		return outgoingCount;
	}


	/**
	 * @param outgoingCount the outgoingCount to set
	 */
	public void setOutgoingCount(int outgoingCount) {
		this.outgoingCount = outgoingCount;
	}


	// event handlers for clearing the references...
	@Observer(value=KiWiEvents.TRIPLESTORE_UPDATED,create=false)
	public void tripleStoreUpdated() {
		clear();
	}
	
	@Observer(value=KiWiEvents.CONTENT_UPDATED,create=false)
	public void contentUpdated() {
		clear();
	}
	
	public void clear() {
		incoming = null;
		outgoing = null;
	}



	/**
	 * A bean class to represent tree nodes using the rich:recursiveTreeAdaptor.
	 * 
	 * @author Sebastian Schaffert
	 *
	 */
	public static class ReferencesNode {
		
		private KiWiResource data;
		
		private boolean inferred;
		
		private List<ReferencesNode> children;
		
		private Long tripleId;
		
		private ReferencesNode(KiWiResource data) {
			this.data = data;
			this.children = new LinkedList<ReferencesNode>();
			this.inferred = false;
		}

		/**
		 * @return the data
		 */
		public KiWiResource getData() {
			return data;
		}

		/**
		 * @param data the data to set
		 */
		public void setData(KiWiResource data) {
			this.data = data;
		}

		/**
		 * @return the children
		 */
		public List<ReferencesNode> getChildren() {
			return children;
		}

		/**
		 * @param children the children to set
		 */
		public void setChildren(List<ReferencesNode> children) {
			this.children = children;
		}

		public boolean isInferred() {
			return inferred;
		}

		public void setInferred(boolean inferred) {
			this.inferred = inferred;
		}

		public Long getTripleId() {
			return tripleId;
		}

		public void setTripleId(Long tripleId) {
			this.tripleId = tripleId;
		}		
		
	}
}
