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

package kiwi.wiki.action.ie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.faces.model.SelectItem;

import kiwi.api.comment.CommentService;
import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.fragment.FragmentFacade;
import kiwi.api.fragment.FragmentService;
import kiwi.api.informationextraction.InformationExtractionService;
import kiwi.api.ontology.OntologyService;
import kiwi.api.render.RenderingService;
import kiwi.api.tagging.TaggingService;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.content.TextContent;
import kiwi.model.content.TextFragment;
import kiwi.model.informationextraction.Context;
import kiwi.model.informationextraction.Suggestion;
import kiwi.model.kbase.KiWiLiteral;
import kiwi.model.kbase.KiWiNamespace;
import kiwi.model.kbase.KiWiNode;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.ontology.KiWiClass;
import kiwi.model.ontology.KiWiProperty;
import kiwi.model.tagging.Tag;
import kiwi.model.user.User;
import kiwi.service.render.RenderingServiceImpl;
import kiwi.service.render.renderlet.XOMRenderlet;
import kiwi.service.render.savelet.RdfaSavelet;
import kiwi.util.KiWiXomUtils;
import kiwi.util.KiWiXomUtils.NodePos;
import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.Node;
import nu.xom.Nodes;
import nu.xom.ParentNode;
import nu.xom.Text;
import nu.xom.XPathContext;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Synchronized;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.json.JSONException;
import org.json.JSONStringer;

/**
 * Structuring information without editing content.
 * 
 * For annotating by fragments, RDFa, nested content items and supported by Information Extraction.
 * 
 * What should work:
 *   creating fragments, tagging fragments (does not currently work)
 *   creating nested content items, tagging nested content items.
 *   information extraction suggestions (just displaying them, accepting/rejecting is not yet implemented)
 * TODO:
 *   Links
 *   Iterated includes
 *   accepting/rejecting suggestions
 *   choosing from alternative suggestions (disambiguating entities and types)
 *   unify terminology (roles/predicates/properties...)
 *   
 *   refactor the annotation types collections. those that need one needs to be made into a special class with their own real "resource" with a type
 * 
 * @author Marek Schmidt 
 * 
 */
@Name("ie.informationExtractionAction")
@Scope(ScopeType.CONVERSATION) 
//@Transactional
@Synchronized
public class InformationExtractionAction implements Serializable {
	
	/**
	 * A resource, or a hypothetical resource, or just partially defined resource...
]	 *
	 */
	public static class ResourceUI {
		
		public ResourceUI (String kiwiid, String label) {
			this.kiwiid = kiwiid;
			this.label = label;
		}
		
		private List<ResourceUI> types = new LinkedList<ResourceUI>();
		
		private String kiwiid;
		
		private String label;
				
		public String getLabel() {
			return label;
		}
		
		public void setLabel(String label) {
			this.label = label;
		}
		
		public String getKiwiid() {
			return kiwiid;
		}
		
		public void setKiwiid(String kiwiid) {
			this.kiwiid = kiwiid;
		}
		
		public String toString() {
			return "(" + this.kiwiid + " " + this.label + ")";
		}
		
		public boolean equals(Object other) {
			if (other instanceof ResourceUI) {
				ResourceUI otherResource = (ResourceUI)other;
				
				if (this.kiwiid != null) {
					return this.kiwiid.equals(otherResource.kiwiid);
				}
				else {
					if (otherResource.kiwiid == null) {
						return this.label.equals(otherResource.label);
					}
				}
			}
			return false;
		}
		
		public List<ResourceUI> getTypes() {
			return types;
		}
	}

	public static class PropertyUI {
		private ResourceUI predicate;
		private boolean hidden = false;
		private boolean inferred = false;
		
		public ResourceUI getPredicate() {
			return predicate;
		}
		
		public void setPredicate(ResourceUI predicate) {
			this.predicate = predicate;
		}

		public void setHidden(boolean hidden) {
			this.hidden = hidden;
		}

		public boolean isHidden() {
			return hidden;
		}

		public void setInferred(boolean inferred) {
			this.inferred = inferred;
		}

		public boolean isInferred() {
			return inferred;
		}
	}
	
	/**
	 * Object property as part of a nested item annotation (one that is not in the content, but just in the metadata.
	 */
	public static class ObjectPropertyUI extends PropertyUI {
		
		private ResourceUI object;
		
		public void setObject(ResourceUI object) {
			this.object = object;
		}
		
		public ResourceUI getObject() {
			return object;
		}
	}
	
	/**
	 * TODO: still no datatypes, just String literals... 
	 */
	public static class DatatypePropertyUI extends PropertyUI {
		private String literal;
		
		public void setLiteral(String literal) {
			this.literal = literal;
		}
		public String getLiteral() {
			return literal;
		}	
	}
	
	public static class CommentUI extends ResourceUI {

		private String html;
		private User author;
		public CommentUI(String kiwiid, String label) {
			super(kiwiid, label);
		}

		public String getHtml() {
			return html;
		}
		
		public void setHtml(String html) {
			this.html = html;
		}
		
		public void setAuthor (User author) {
			this.author = author;
		}
		
		public User getAuthor() {
			return this.author;
		}
	}
	
	public static class AnnotationUI {
		private String id;
		private int kind;
		
		private String literal;
		
		// nested item tags, or fragment tags
		private List<ResourceUI> tags = new LinkedList<ResourceUI> (); 
		
		// properties, (predicates of the triples linking content item and the resource)
		private List<ResourceUI> predicates = new LinkedList<ResourceUI> ();
		
		// types of the resource. 
		private List<ResourceUI> types = new LinkedList<ResourceUI> ();
		
		// resources (multiple resources for ambiguous annotations
		private List<ResourceUI> resources = new LinkedList<ResourceUI> ();
		//private ResourceUI resource;
		
		private List<CommentUI> comments = new LinkedList<CommentUI> ();
		
		private List<ObjectPropertyUI> objectProperties = new LinkedList<ObjectPropertyUI>();
		
		private List<DatatypePropertyUI> datatypeProperties = new LinkedList<DatatypePropertyUI>();
		
		// Annotation created in this session.
		private boolean _new;
		
		// Annotation deleted.
		private boolean deleted;
		
		private int start;
		private int end;
		
		// Annotation is just a suggestion
		// private boolean suggestion;
		
		final static int FRAGMENT_KIND = 1;
		final static int NESTED_KIND = 2;
		final static int RDF_DATATYPE_KIND = 3;
		final static int RDF_OBJECT_KIND = 4;
		final static int EMPTY = 0;
		
		final static int VALIDITY_VALID = 0;
		final static int VALIDITY_INCOMPLETE = 1;
		final static int VALIDITY_INCONSISTENT = 2;
		
		public AnnotationUI(int kind, String id, boolean _new) {
			this.kind = kind;
			this.id = id;
			this._new = _new;
		}
		
		public void setId (String id) {
			this.id = id;
		}
		
		public String getId () {
			return this.id;
		}
		
		public int getKind() {
			return kind;
		}
		
		public void setKind(int kind) {
			this.kind = kind;
		}
		
		public List<ResourceUI> getTags() {
			return tags;
		}
		
		public List<ResourceUI> getPredicates() {
			return predicates;
		}
		
		public List<ResourceUI> getTypes() {
			return types;
		}
		
		public List<ResourceUI> getResources() {
			return resources;
		}
		
		public List<DatatypePropertyUI> getDatatypeProperties() {
			return datatypeProperties;
		}
		
		public List<DatatypePropertyUI> getVisibleDatatypeProperties() {
			List<DatatypePropertyUI> ret = new LinkedList<DatatypePropertyUI> ();
			for (DatatypePropertyUI ui : datatypeProperties) {
				if (ui.isHidden()) {
					continue;
				}
				
				ret.add (ui);
			}
			return ret;
		}
		
		public List<ObjectPropertyUI> getObjectProperties() {
			return objectProperties;
		}
		
		public List<CommentUI> getComments() {
			return comments;
		}
		
		public void setNew (boolean isNew) {
			this._new = isNew;
		}
		
		public boolean isNew () {
			return this._new;
		}
		
		public void setDeleted (boolean isDeleted) {
			this.deleted = isDeleted;
		}
		
		public boolean isDeleted () {
			return this.deleted;
		}

		public int getStart() {
			return start;
		}

		public void setStart(int begin) {
			this.start = begin;
		}

		public int getEnd() {
			return end;
		}

		public void setEnd(int end) {
			this.end = end;
		}

		public String getLiteral() {
			return this.literal;
		}
		
		public void setLiteral(String literal) {
			this.literal = literal;
		}
		
		/**
		 * A "title" to be displayed for the annotation.
		 * 
		 * It is used for nested items to display the type label of the nested item 
		 * 
		 * @return
		 * @throws JSONException 
		 */
		public String getTitleJson() throws JSONException {
			if (getKind() == NESTED_KIND) {
				
				StringBuilder sb = new StringBuilder();
				for (ResourceUI ui: getTypes()) {
										
					if (sb.length() > 0) {
						sb.append (", ");
					}
					
					sb.append(ui.getLabel());
				}
				
				return new JSONStringer().array().value(sb.toString()).endArray().toString();
			}
			return new JSONStringer().array().endArray().toString();
		}
		
		/**
		 * Checks the validity of this suggestion. The valid suggestion is consistent with ontology and is complete and unambiguous.
		 * @return
		 */
		public int getValidity() {
			if (getKind() == EMPTY) {
				return VALIDITY_INCOMPLETE;
			}
			else if (getKind() == RDF_OBJECT_KIND) {
				if (getResources().size() != 1) {
					return VALIDITY_INCOMPLETE;
				}
				if (getPredicates().size() == 0) {
					return VALIDITY_INCOMPLETE;
				}
				
				return VALIDITY_VALID;
			}
			
			return VALIDITY_VALID;
		}

		public List<ResourceUI> getVisibleObjectPropertyPredicates() {
			List<ResourceUI> ret = new LinkedList<ResourceUI>();
			
			for (ObjectPropertyUI property : getObjectProperties()) {
				if (property.isHidden()) {
					continue;
				}
				
				if (!ret.contains(property.getPredicate())) {
					ret.add(property.getPredicate());
				}
			}
			
			return ret;
		}
		
		public List<ResourceUI> getDatatypePropertyPredicates() {
			List<ResourceUI> ret = new LinkedList<ResourceUI>();
			
			for (DatatypePropertyUI property : getDatatypeProperties()) {
				if (!ret.contains(property.getPredicate())) {
					ret.add(property.getPredicate());
				}
			}
			
			return ret;
		}
		
		public List<ObjectPropertyUI> getVisibleObjectProperties(ResourceUI predicate) {
			List<ObjectPropertyUI> ret = new LinkedList<ObjectPropertyUI> ();
			
			for (ObjectPropertyUI property : getObjectProperties()) {
				if (property.isHidden()) {
					continue;
				}
				if (predicate.equals(property.getPredicate())) {
					ret.add(property);
				}
			}
			
			return ret;
		}
		
		public List<DatatypePropertyUI> getDatatypeProperties(ResourceUI predicate) {
			List<DatatypePropertyUI> ret = new LinkedList<DatatypePropertyUI> ();
			
			for (DatatypePropertyUI property : getDatatypeProperties()) {
				if (predicate.equals(property.getPredicate())) {
					ret.add(property);
				}
			}
			
			return ret;
		}
		
		
		
		
		
		
		/*public ResourceUI getResource() {
			return resource;
		}
		
		public void setResource(ResourceUI resource) {
			this.resource = resource;
		}*/
	}
	
	public static class SuggestionUI {
		private Suggestion suggestion;
		private int kind;
		
		private String label;
		
		private int start;
		private int end;
		
		private float score = 1.0f;
		
		private List<ResourceUI> resources = new LinkedList<ResourceUI> ();
		private List<ResourceUI> types = new LinkedList<ResourceUI> ();
		private List<ResourceUI> predicates = new LinkedList<ResourceUI> ();
		
		public SuggestionUI(Suggestion suggestion, int kind, int start, int end) {
			this.suggestion = suggestion;
			this.kind = kind;
			
			this.start = start;
			this.end = end;
		}
		
		public Suggestion getSuggestion() {
			return this.suggestion;
		}
		
		public void setSuggestion(Suggestion suggestion) {
			this.suggestion = suggestion;
		}

		public int getKind() {
			return kind;
		}

		public void setKind(int kind) {
			this.kind = kind;
		}

		public int getStart() {
			return start;
		}

		public void setStart(int begin) {
			this.start = begin;
		}

		public int getEnd() {
			return end;
		}

		public void setEnd(int end) {
			this.end = end;
		}
		
		public String getLabel() {
			return this.label;
		}
		
		public void setLabel(String label) {
			this.label = label;
		}
		
		public List<ResourceUI> getResources() {
			return resources;
		}
		
		public List<ResourceUI> getTypes() {
			return types;
		}
		
		public List<ResourceUI> getPredicates() {
			return predicates;
		}
		
		public void setScore(float score) {
			this.score = score;
		}
		
		public float getScore() {
			return this.score;
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3559415160348323325L;

	@Logger
	private static Log log;

	@In
	FacesMessages facesMessages;

	@In(create=true)
	private ContentItem currentContentItem;

	@In(create=true)
	private User currentUser;

	@In
	private TripleStore tripleStore;

	private String currentContentHtml;
	
	@In
	private TaggingService taggingService;
	
	private Document currentDocument;
	
	@In
	private FragmentService fragmentService;
	
	@In
	private ContentItemService contentItemService;
	
	@In
	private KiWiEntityManager kiwiEntityManager;
	
	@In(create=true)
	private OntologyService ontologyService;
	
	@In(value="kiwi.informationextraction.informationExtractionService")
	private InformationExtractionService informationExtractionService;

	@In(create=true)
	private CommentService commentService;
	
	@In(create=true)
	private RenderingService renderingPipeline;
	
	private Map<String, AnnotationUI> annotations;
	
	private Set<SuggestionUI> suggestions;
	private Collection<SuggestionUI> currentSuggestions;
	
	private int maxid = 0;
	
	private int startCoord = 0;
	private int endCoord = 0;
	
	private String tagLabel;
	private String literal;
	private String predicate;
	private String type;
	private String resourceTitle;
	
	private AnnotationUI selectedAnnotation = null;
	
	private boolean displaySuggestions = true;
	
	public List<AnnotationUI> getAnnotations() {
		return new LinkedList<AnnotationUI> (annotations.values());
	}
	
	public void setSelectedAnnotationId(String selectedAnnotationId) {
		log.info("setSelectedAnnotationId #0", selectedAnnotationId);
		selectedAnnotation = annotations.get(selectedAnnotationId);
		
		log.info("selected: #0", selectedAnnotation == null ? null : selectedAnnotation.getId());
	}
	
	public AnnotationUI getSelectedAnnotation() {
		return selectedAnnotation;
	}
	
	public void setTagLabel(String tagLabel) {
		this.tagLabel = tagLabel;
	}
	
	public String getTagLabel() {
		return this.tagLabel;
	}
	
	public String getPredicate() {
		return predicate;
	}
	
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public boolean isContent() {
		return currentDocument != null;
	}
	
	public String getSelectedFragmentId() {
		
		if (selectedAnnotation == null) {
			return null;
		}
		
		if (selectedAnnotation.getKind() != AnnotationUI.NESTED_KIND) {
			return selectedAnnotation.getId();
		}
		
		return null;
	}
	
	public String getSelectedNestedId() {
		
		if (selectedAnnotation == null) {
			return null;
		}
		
		if (selectedAnnotation.getKind() == AnnotationUI.NESTED_KIND) {
			return selectedAnnotation.getId();
		}
		
		return null;
	}
	
	public void setSelectedFragmentId(String fragmentId) {
		
	}
	
	public void setSelectedNestedId(String nestedId) {
		
	}
	
	public void setStartCoord (String startCoord) {
		log.info("start coord: #0", startCoord);
		this.startCoord = Integer.parseInt(startCoord);
	}
	
	public String getStartCoord() {
		return Integer.toString(this.startCoord);
	}
	
	public void setEndCoord (String endCoord) {
		log.info("end coord: #0", endCoord);
		this.endCoord = Integer.parseInt(endCoord);
	}
	
	public String getEndCoord () {
		return Integer.toString(this.endCoord);
	}

	/**
	 * inserts a bookmark at specified positions
	 */
	private static Document insertFragment(Document content, int startCoord, int endCoord, String id) {
		
		// TODO: why copy?
		Document doc = (Document) content.copy();

		Element e_begin = new Element("kiwi:bookmarkstart", Constants.NS_KIWI_HTML);
        e_begin.addAttribute(new Attribute("id", id)); //"uri::annotation:" + id));
        
		KiWiXomUtils.insertNodeAtPos(doc, 0, startCoord, e_begin);
		
		Element e_end = new Element("kiwi:bookmarkend", Constants.NS_KIWI_HTML);
        e_end.addAttribute(new Attribute("id", id)); //"uri::annotation:" + id));
        
        KiWiXomUtils.insertNodeAtPos(doc, 0, endCoord, e_end);
		
		return doc;
	} 
	
	/**
	 * Move the positions of the annotations and suggestions after nested item insertion. The nested item insertion
	 * inserts a new block element at the specified position, so it introduces a new one position whitespace in the 
	 * text space.
	 * @param originalStart
	 * @param originalEnd
	 */
	private void moveAnnotationAndSuggestionOffsetsAfterNestedInsert(int originalStart, int originalEnd) {
		
		for (AnnotationUI annotation : annotations.values()) {
			if (annotation.getStart() > originalStart) {
				annotation.setStart(annotation.getStart() + 1);
			}
			if (annotation.getStart() > originalEnd) {
				annotation.setStart(annotation.getStart() + 1);
			}
			
			if (annotation.getEnd() > originalStart) {
				annotation.setEnd(annotation.getEnd() + 1);
			}
			if (annotation.getEnd() > originalEnd) {
				annotation.setEnd(annotation.getEnd() + 1);
			}
		}
		
		for (SuggestionUI suggestion : suggestions) {
			if (suggestion.getStart() > originalStart) {
				suggestion.setStart(suggestion.getStart() + 1);
			}
			if (suggestion.getStart() > originalEnd) {
				suggestion.setStart(suggestion.getStart() + 1);
			}
			
			if (suggestion.getEnd() > originalStart) {
				suggestion.setEnd(suggestion.getEnd() + 1);
			}
			if (suggestion.getEnd() > originalEnd) {
				suggestion.setEnd(suggestion.getEnd() + 1);
			}
		}
	}
	
	/**
	 * Inserts a virtual nested content item around specified positions. 
	 * It tries to find the best match without breaking the document structure. 
	 */
	private Document insertNested(Document content, int startCoord, int endCoord, String id) {
		
		// TODO: why copy?
		Document doc = (Document) content.copy();
		
		String t = KiWiXomUtils.xom2plain(content);
		String inside = t.substring(startCoord, endCoord);
		
		// log.info("#0:#1 <#2>", startCoord, endCoord, inside);
		
		NodePos startNode = KiWiXomUtils.getNode(doc, 0, startCoord);
		NodePos endNode = KiWiXomUtils.getNode(doc, 0, endCoord);
		
		if (startNode != null && endNode != null) {
			ParentNode startNodeParent;
			ParentNode endNodeParent;
			
			if (startNode.getNode() instanceof ParentNode) {
				startNodeParent = (ParentNode)startNode.getNode();
			}
			else {
				startNodeParent = startNode.getNode().getParent();
			}
			
			if (endNode.getNode() instanceof ParentNode) {
				endNodeParent = (ParentNode)endNode.getNode();
			}
			else {
				endNodeParent = endNode.getNode().getParent();
			}
			
			
			List<ParentNode> ns1 = new ArrayList<ParentNode> (KiWiXomUtils.getParents (startNodeParent));
			List<ParentNode> ns2 = new ArrayList<ParentNode> (KiWiXomUtils.getParents (endNodeParent));
			
			for (int i1 = 0; i1 < ns1.size(); ++i1) {
				for (int i2 = 0; i2 < ns2.size(); ++i2) {
					if (ns1.get(i1).equals(ns2.get(i2))) {
						
						ParentNode parent = ns1.get(i1);
						
						Node cutStart = null;
						Node cutEnd = null;
						
						Element nested = new Element("div", Constants.NS_XHTML);
						nested.addAttribute(new Attribute("kiwi:component", Constants.NS_KIWI_HTML, id));
						
						if (i1 != 0 && i2 != 0) {
							cutStart = ns1.get(i1 - 1);
							cutEnd = ns2.get(i2 - 1);
							
							int cutStartIndex = parent.indexOf(cutStart);
							int cutEndIndex = parent.indexOf(cutEnd);
							
							parent.insertChild(nested, cutStartIndex);
							
							for (int i = 0; i <= cutEndIndex - cutStartIndex; ++i) {
								Node child = parent.removeChild(cutStartIndex + 1);
								nested.appendChild(child);
							}
						}
						else {
							// just wrap all the children...
							
							// TODO: do it properly, p is not the important thing, find nearest element that can contain div instead...						
							// if the parent is a paragraph, wrap the paragraph with the div.
							if (parent instanceof Element) {
								Element parentElement = (Element) parent;
								
								if ("p".equalsIgnoreCase(parentElement.getLocalName())) {
									parentElement.getParent().replaceChild(parentElement, nested);
									nested.appendChild(parentElement);
									
									moveAnnotationAndSuggestionOffsetsAfterNestedInsert(startCoord, endCoord);
									
									return doc;
								}
							}
							
							parent.insertChild(nested, 0);
							while(parent.getChildCount() > 1) {
								Node child = parent.removeChild(1);
								nested.appendChild(child);
							}
						}
						
						// TODO: move the annotations accordingly.
						
						
						return doc;
					}
				}
			}
		}
		
		// didn't find the coordinates in the document... 
		return doc;
	}
	
	/**
	 * Deletes a nested content item div from a XOM
	 */
	private static Document deleteNested(Document content, String id) {
		log.debug("deleting nested #0", id);
		
		Document ret = (Document)content.copy();
		
		XPathContext namespaces = new XPathContext();
        namespaces.addNamespace("kiwi", Constants.NS_KIWI_HTML);
        namespaces.addNamespace("html", Constants.NS_XHTML);
	
		Nodes nodes = ret.query("//html:div[@kiwi:component=\"" + id + "\"]", namespaces);
		for (int i = 0; i < nodes.size(); ++i) {
			Element div = (Element)nodes.get(i);
			
			ParentNode parent = div.getParent();
			int indexOfDiv = parent.indexOf(div);
			while(div.getChildCount() > 0) {
				Node child = div.removeChild(div.getChildCount() - 1);
				parent.insertChild(child, indexOfDiv);
			}
			
			parent.removeChild(div);
		}
		
		if (nodes.size() == 0) {
			log.info("no nested item with id '#0' found!", id);
		}
		
		return ret;
	}
	
	/**
	 * deletes a bookmarkstart and bookmarkend elements from a XOM
	 */
	private static Document deleteFragment(Document content, String id) {
		
		log.debug("deleting fragment #0", id);
		
		Document ret = (Document)content.copy();
		
		XPathContext namespaces = new XPathContext();
        namespaces.addNamespace("kiwi", Constants.NS_KIWI_HTML);
        namespaces.addNamespace("html", Constants.NS_XHTML);
	
		Nodes nodes;
		
		nodes = ret.query("//kiwi:bookmarkstart[@id=\"" + id + "\"]", namespaces);
		for (int i = 0; i < nodes.size(); ++i) {
			Node node = nodes.get(i);
			node.getParent().removeChild(node);
		}

		if (nodes.size() == 0) {
			log.info("no bookmarkstart node with id '#0' found", id);
		}
		
		nodes = ret.query("//kiwi:bookmarkend[@id=\"" + id + "\"]", namespaces);
		for (int i = 0; i < nodes.size(); ++i) {
			Node node = nodes.get(i);
			node.getParent().removeChild(node);
		}
		
		if (nodes.size() == 0) {
			log.info("no bookmarkend node with id '#0' found!", id);
		}
		
		return ret;
	}
	
	public Collection<SuggestionUI> getCurrentSuggestions () {
		if (currentSuggestions == null) {
			currentSuggestions = new LinkedList<SuggestionUI> ();
			
			//log.info("filtering current suggestions #0:#1", startCoord, endCoord);
			
			for (SuggestionUI suggestion : suggestions) {
				
				// interval intersection non-empty, I hope I am doing it right, didn't really try to think about it... 
				if ( (this.startCoord <= suggestion.getStart() && suggestion.getStart() <= this.endCoord) ||
					 (this.startCoord <= suggestion.getEnd() && suggestion.getEnd() <= this.endCoord) ||
					 (suggestion.getStart() <= this.startCoord && this.startCoord <= suggestion.getEnd()) ||
					 (suggestion.getStart() <= this.endCoord && this.endCoord <= suggestion.getEnd())) {
					
					log.info("suggestion #0 #1 #2 #3:#4", 
							suggestion.getKind(), 
							suggestion.getSuggestion().getId(), 
							suggestion.getLabel(), 
							suggestion.getStart(), 
							suggestion.getEnd());
					
					currentSuggestions.add(suggestion);
				}	
			}
		}
		
		return currentSuggestions;
	}
	
	/**
	 * Displayed are current suggestion that should be displayed. Depending on the currently selected annotation
	 * and other state information.
	 * @return
	 */
	public List<SuggestionUI> getDisplayedSuggestions() {
		// for now, only display suggestions if selected empty annotation or none.
		List<SuggestionUI> ret = new LinkedList<SuggestionUI> ();
		if (selectedAnnotation == null || selectedAnnotation.getKind() == AnnotationUI.EMPTY || selectedAnnotation.getKind() == AnnotationUI.NESTED_KIND) {
			for (SuggestionUI suggestion : getCurrentSuggestions()) {
				if (! (suggestion.getKind() == Suggestion.ENTITY || suggestion.getKind() == Suggestion.NESTED_ITEM || suggestion.getKind() == Suggestion.TYPE)) {
					continue;
				}
				
				if (suggestion.getKind() == Suggestion.TYPE) {
					if (!(selectedAnnotation == null || selectedAnnotation.getKind() == AnnotationUI.NESTED_KIND)) {
						continue;
					}
						
				}
			
				ret.add (suggestion);
			}
		}
		
		return ret;
	}
	
	public List<ResourceUI> getTypeSuggestions() {
		List<ResourceUI> ret = new LinkedList<ResourceUI> ();
		if (selectedAnnotation != null) {
			for (SuggestionUI s : getCurrentSuggestions()) {
				for (ResourceUI type : s.getTypes()) {
					if (!selectedAnnotation.getTypes().contains(type) && !ret.contains(type)) {
						ret.add(type);
					}
				}
			}
		}
		
		return ret;
	}
	
	public List<ResourceUI> getResourceSuggestions() {
		List<ResourceUI> ret = new LinkedList<ResourceUI> ();
		if (selectedAnnotation != null) {
			for (SuggestionUI s : getCurrentSuggestions()) {
				for (ResourceUI r : s.getResources()) {
					if (!selectedAnnotation.getResources().contains(r) && !ret.contains(r)) {
						ret.add(r);
					}
				}
			}
		}
		
		return ret;
	}
	
	public List<ResourceUI> getPredicateSuggestions() {
		List<ResourceUI> ret = new LinkedList<ResourceUI> ();
		if (selectedAnnotation != null) {
			for (SuggestionUI s : getCurrentSuggestions()) {
				for (ResourceUI r : s.getPredicates()) {
					if (!selectedAnnotation.getPredicates().contains(r) && !ret.contains(r)) {
						ret.add(r);
					}
				}
			}
			
			// TODO: also add "ontological" suggestions
			for (KiWiProperty property : getPossibleObjectProperties ()) {
				ret.add(kiwiResource2resourceUi(property.getResource()));
			}
		}
		
		return ret;
	}
	
	/**
	 * Render text offset to each node. Makes the client side easier to locate the coordinates, 
	 * and there is no worry about other rendering steps introducing whitespace or something which may damage the coordinates. 
	 */
	private Document offsetsRenderlet (Document xom) {
		
		KiWiXomUtils.NodePosIterator iter = new KiWiXomUtils.NodePosIterator(xom.getRootElement());
		while (iter.hasNext()) {
			NodePos np = iter.next();
			
			if (np.getNode() instanceof Element) {
				Element elem = (Element)np.getNode();
				elem.addAttribute(new Attribute("pos", Integer.toString(np.getPos())));
			}
		}
		
		return xom;
	}
	
	/**
	 * Compute annotation begin and end offsets.
	 */
	private void fillAnnotationPosition (Document xom, AnnotationUI annotation) {
		if (annotation.getKind() == AnnotationUI.FRAGMENT_KIND) {
			XPathContext namespaces = new XPathContext();
	        namespaces.addNamespace("kiwi", Constants.NS_KIWI_HTML);
	        namespaces.addNamespace("html", Constants.NS_XHTML);
					
			Nodes startNodes = xom.query("//kiwi:bookmarkstart[@id=\"" + annotation.getId() + "\"]", namespaces);
			Nodes endNodes = xom.query("//kiwi:bookmarkend[@id=\"" + annotation.getId() + "\"]", namespaces);
			
			if (startNodes.size() == 1 && endNodes.size() == 1) {
				Node startNode = startNodes.get(0);
				Node endNode = endNodes.get(0);
				KiWiXomUtils.NodePosIterator iter = new KiWiXomUtils.NodePosIterator(xom.getRootElement());
				while (iter.hasNext()) {
					NodePos np = iter.next();
					
					if (np.getNode() instanceof Element) {
						if (np.getNode().equals(startNode)) {
							annotation.setStart(np.getPos());
						}
						
						if (np.getNode().equals(endNode)) {
							annotation.setEnd(np.getPos());
						}
					}
				}
			}
		}
		else if (annotation.getKind() == AnnotationUI.NESTED_KIND) {
			XPathContext namespaces = new XPathContext();
	        namespaces.addNamespace("kiwi", Constants.NS_KIWI_HTML);
	        namespaces.addNamespace("html", Constants.NS_XHTML);
		
			Nodes nodes = xom.query("//html:div[@kiwi:component=\"" + annotation.getId() + "\"]", namespaces);
			// TODO: can one component be included multiple times? 
			if (nodes.size() == 1) {
				Node startNode = nodes.get(0);
				
				// the end coordinate is defined by the coordinate of the last text node + its length... 
				Node endNode = KiWiXomUtils.getLastTextNode(startNode);
				
				KiWiXomUtils.NodePosIterator iter = new KiWiXomUtils.NodePosIterator(xom.getRootElement());
				while (iter.hasNext()) {
					NodePos np = iter.next();
					
					if (np.getNode() instanceof Element) {
						if (np.getNode().equals(startNode)) {
							annotation.setStart(np.getPos());
						}
					}
					
					if (np.getNode() instanceof Text) {
						if (np.getNode().equals(endNode)) {
							// TODO: +/- one ? 
							annotation.setEnd(np.getPos() + ((Text)np.getNode()).getValue().length());
						}
					}
				}
			}
			else if (nodes.size() == 0 && annotation.getId().equals(currentContentItem.getKiwiIdentifier())) {
				// the current top-level content item is not a component per se...
				
				Node startNode = xom.getRootElement();
				
				// the end coordinate is defined by the coordinate of the last text node + its length... 
				Node endNode = KiWiXomUtils.getLastTextNode(startNode);
				
				KiWiXomUtils.NodePosIterator iter = new KiWiXomUtils.NodePosIterator(xom.getRootElement());
				while (iter.hasNext()) {
					NodePos np = iter.next();
					
					if (np.getNode() instanceof Element) {
						if (np.getNode().equals(startNode)) {
							annotation.setStart(np.getPos());
						}
					}
					
					if (np.getNode() instanceof Text) {
						if (np.getNode().equals(endNode)) {
							// TODO: +/- one ? 
							annotation.setEnd(np.getPos() + ((Text)np.getNode()).getValue().length());
						}
					}
				}
			}
		}
	}
		
	/**
	 * @return the currentContentHtml
	 */
	public synchronized String getCurrentContentHtml() {
		
		log.debug("getCurrentContentHtml");
		
		if(currentContentHtml == null) {
			
			if (currentDocument == null) {
				currentContentHtml = "";
			}
			else {
				// render the document
				// TODO: create a method in renderingService for this pipeline...
				
				// log.info("rendering: #0", currentDocument.toXML());
				
				Document xom = (Document)currentDocument.copy();
				
				// remove the suggestion fragments if suggestions are disabled.
				if (!displaySuggestions) {
					for (AnnotationUI annotation : annotations.values()) {
						if (annotation.getKind() == AnnotationUI.EMPTY) {
							xom = deleteFragment(xom, annotation.getId());
						}
					}
				}
				
				// render offsets (hints for the client about text coordinates of every element)
				xom = offsetsRenderlet(xom);
				
				// render the HTML spans with fragment ids, so displaying fragments is easy on the client side...
				XOMRenderlet htmlFragmentRenderlet = (XOMRenderlet)Component.getInstance("kiwi.service.render.renderlet.HtmlFragmentRenderlet");
				xom = htmlFragmentRenderlet.apply(currentContentItem.getResource(), xom);
				
				currentContentHtml = "<div xmlns:kiwi=\"" + 
						Constants.NS_KIWI_HTML + "\" kiwi:component=\"" + 
						currentContentItem.getKiwiIdentifier() +
						"\">" + RenderingServiceImpl.extractPage(xom) +
						"</div>";
			}
		}
		return currentContentHtml;
	}

	/**
	 * @param currentContentHtml
	 *            the currentContentHtml to set
	 */
	public void setCurrentContentHtml(String currentContentHtml) {
		this.currentContentHtml = currentContentHtml;
	}
	
	public String getLiteral() {
		if (this.selectedAnnotation != null) {
			literal = this.selectedAnnotation.getLiteral();
		}
		else {
			literal = "";
		}
		
		return literal;
	}
	
	public void setLiteral(String literal) {
		this.literal = literal;
	}
	
	public void setLiteral() {
		if (this.selectedAnnotation != null) {
			String oldLiteral = this.selectedAnnotation.getLiteral();
			this.selectedAnnotation.setLiteral(literal);
			
			if (!literal.equals(oldLiteral)) {
				// replace the literal in the text.
				
				Text node = new Text(literal);
				List<Node> list = new ArrayList<Node> ();
				list.add(node);
				
				replaceFragmentContentWithNodes(currentDocument, list, this.selectedAnnotation.getId());
				
				currentContentHtml = null;
			}
		}
	}
	
	/**
	 * Choosing existing resource, try to find an existing resource, or select null if not found.
	 */
	public void selectResource() {
		if (selectedAnnotation != null) {
			ContentItem ci = contentItemService.getContentItemByTitle(resourceTitle);
			if (ci != null) {
				ResourceUI resourceUi = new ResourceUI(ci.getKiwiIdentifier(), ci.getTitle());
				setResource(resourceUi);
			}
			else {
				// Empty string to unset the resource.
				if ("".equals(resourceTitle)) {
					setResource(null);
				}
				else {
					//facesMessages.add("No content item with title #0 has been found. You might want to create one.", resourceTitle);
					createResource();
				}
			}
		}
	}
	
	public void selectResource(ResourceUI resource) {
		if (selectedAnnotation != null) {
			setResource(resource);
		}
	}
	
	/**
	 * Creating a new resource.
	 */
	public void createResource() {
		if (selectedAnnotation != null) {
			ResourceUI resourceUi = new ResourceUI(null, resourceTitle);
			
			// also, add types to the resource that are contained in the annotation
			for (ResourceUI type : selectedAnnotation.getTypes()) {
				resourceUi.getTypes().add(type);
				
				// log.info("adding type #0", type.getKiwiid());
			}
			
			setResource(resourceUi);
		}
	}
	
	public void clearResource() {
		if (selectedAnnotation != null) {
			setResource(null);
			resourceTitle = "";
		}
	}
	
	public String getResourceTitle() {
		if (selectedAnnotation != null) {
			resourceTitle = selectedAnnotation.getLiteral();
		}
		return resourceTitle;
	}
	
	public void setResourceTitle(String resourceTitle) {
		this.resourceTitle = resourceTitle;
	}
	
	public List<ResourceUI> getResources() {
		if (selectedAnnotation != null) {
			return selectedAnnotation.getResources();
		}
		
		return new LinkedList<ResourceUI> ();
	}
	
	/*
	public ResourceUI getResource() {
		if (selectedAnnotation != null) {
			return selectedAnnotation.getResource();
		}
		
		return null;
	}*/
	
	public void setResource(ResourceUI resource) {
		
		if (selectedAnnotation != null) {
			ResourceUI oldResource = null;
			if (selectedAnnotation.getResources().size() == 1) {
				oldResource = selectedAnnotation.getResources().get(0);
			}
			
			selectedAnnotation.getResources().clear();
			if (resource != null) {
				selectedAnnotation.getResources().add(resource);
			}
			
			// is we are unsetting a resource, don't touch the content
			if (resource == null) {
				return;
			}
			
			// otherwise, replace the title also in the text.
			if (oldResource == null || !oldResource.getLabel().equals(resource.getLabel())) {
				// replace the literal... the literal is stored to represent the text value in the XOM even if 
				// the user unsets the resource itself... 
				selectedAnnotation.setLiteral(resource.getLabel());
				Text node = new Text(selectedAnnotation.getLiteral());
				List<Node> list = new ArrayList<Node> ();
				list.add(node);
				
				replaceFragmentContentWithNodes(currentDocument, list, this.selectedAnnotation.getId());
				
				currentContentHtml = null;
			}
		}
	}
	
	
	/**
	 * Creates an annotation (fragment)
	 */
	public synchronized void addFragment() {
		log.debug("add annotation");
		
		if (startCoord == endCoord && selectedAnnotation != null) {
			startCoord = selectedAnnotation.getStart();
			endCoord = selectedAnnotation.getEnd();
		}
		
		int id = ++maxid;
		AnnotationUI annotation = new AnnotationUI(AnnotationUI.FRAGMENT_KIND,  "uri::annotation:" + id, true);
		annotations.put(annotation.getId(), annotation);
		
		selectedAnnotation = annotation;
		
		currentDocument = insertFragment (currentDocument, startCoord, endCoord, annotation.getId());
		
		fillAnnotationPosition (currentDocument, selectedAnnotation);
		
		log.info("computed positions: #0 #1, original: #2 #3", selectedAnnotation.getStart(), selectedAnnotation.getEnd(), startCoord, endCoord);
		
		// cause the rerendering... 
		currentContentHtml = null;
		currentSuggestions = null;
	}
	
	public synchronized void addRdfaDatatype() {
		log.debug("add rdfa datatype");
		
		if (startCoord == endCoord && selectedAnnotation != null) {
			startCoord = selectedAnnotation.getStart();
			endCoord = selectedAnnotation.getEnd();
		}
		
		int id = ++maxid;
		AnnotationUI annotation = new AnnotationUI(AnnotationUI.RDF_DATATYPE_KIND,  "uri::annotation:" + id, true);
		annotations.put(annotation.getId(), annotation);
		
		selectedAnnotation = annotation;
		
		currentDocument = insertFragment (currentDocument, startCoord, endCoord, annotation.getId());
		
		fillAnnotationPosition (currentDocument, selectedAnnotation);
		
		String content = getFragmentContent (currentDocument, annotation.getId());
		annotation.setLiteral(content);
		
		log.info("computed positions: #0 #1, original: #2 #3 content: #4", 
				selectedAnnotation.getStart(), 
				selectedAnnotation.getEnd(), 
				startCoord, 
				endCoord,
				content);
		
		// cause the rerendering... 
		currentContentHtml = null;
		currentSuggestions = null;
	}
	
	public synchronized void addRdfaObject() {
		log.debug("add rdfa datatype");
		
		if (startCoord == endCoord && selectedAnnotation != null) {
			startCoord = selectedAnnotation.getStart();
			endCoord = selectedAnnotation.getEnd();
		}
		
		int id = ++maxid;
		AnnotationUI annotation = new AnnotationUI(AnnotationUI.RDF_OBJECT_KIND,  "uri::annotation:" + id, true);
		annotations.put(annotation.getId(), annotation);
		
		selectedAnnotation = annotation;
		
		currentDocument = insertFragment (currentDocument, startCoord, endCoord, annotation.getId());
		
		fillAnnotationPosition (currentDocument, selectedAnnotation);
		
		String content = getFragmentContent (currentDocument, annotation.getId());
		annotation.setLiteral(content);
		
		resourceTitle = content;
		
		// if the content matches some title, set it directly
		ContentItem ci = contentItemService.getContentItemByTitle(content);
		if (ci != null) {
			ResourceUI resource = new ResourceUI(ci.getKiwiIdentifier(), ci.getTitle());
			//annotation.setResource(resource);
			annotation.getResources().clear();
			annotation.getResources().add(resource);
		}
		
		log.info("computed positions: #0 #1, original: #2 #3 content: #4", 
				selectedAnnotation.getStart(), 
				selectedAnnotation.getEnd(), 
				startCoord, 
				endCoord,
				content);
		
		// cause the rerendering... 
		currentContentHtml = null;
		currentSuggestions = null;
	}
	
	/**
	 * Creates an nested content item. 
	 */
	public synchronized void addNested() {
		log.debug("add nested");
		int id = ++maxid;
		
		if (startCoord == endCoord && selectedAnnotation != null) {
			startCoord = selectedAnnotation.getStart();
			endCoord = selectedAnnotation.getEnd();
		}
		
		AnnotationUI annotation = new AnnotationUI(AnnotationUI.NESTED_KIND, "uri::annotation:" + id, true);
		
		annotations.put(annotation.getId(), annotation);
		
		selectedAnnotation = annotation;
		
		currentDocument = insertNested (currentDocument, startCoord, endCoord, annotation.getId());
		
		fillAnnotationPosition (currentDocument, selectedAnnotation);
		
		log.info("computed positions: #0 #1, original: #2 #3", selectedAnnotation.getStart(), selectedAnnotation.getEnd(), startCoord, endCoord);
		
		// cause the rerendering... 
		currentContentHtml = null;
		currentSuggestions = null;
	}
	

	
	public synchronized void deleteAnnotation() {
		log.debug("delete");
		
		if (selectedAnnotation != null) {
			
			if (selectedAnnotation.getId().equals(currentContentItem.getKiwiIdentifier())) {
				log.info("tried to delete the whole content item #0.", selectedAnnotation.getId());
				return;
			}
			
			// we can immediately delete annotations that we had created here...
			if (selectedAnnotation.isNew()) {
				annotations.remove(selectedAnnotation.getId());
			}
			else {
				// all the others can be really deleted after calling save.
				// so just note that it is deleted.
				selectedAnnotation.setDeleted(true);
			}
			
			// ...but we can delete any annotation from the working XOM document in either case.
			if (selectedAnnotation.getKind() == AnnotationUI.NESTED_KIND) {
				currentDocument = deleteNested (currentDocument, selectedAnnotation.getId());
			}
			else {
				currentDocument = deleteFragment (currentDocument, selectedAnnotation.getId());
			}
			
			selectedAnnotation = null;

			// cause the rerendering... 
			currentContentHtml = null;
		}
	}
	
	/**
	 * Tries to match an existing item with the label, or a CURIE, or something...
	 * @param label
	 * @return
	 */
	private String label2kiwiid(String label) {
		// Try to parse it as a kind of a CURIE (foo:Bar) , if it fails fall back to a title search. 
		int colon = label.indexOf(':');
		if (colon > 0) {
			String prefix = label.substring(0, colon);
			String suffix = label.substring(colon + 1);
			
			KiWiNamespace ns = tripleStore.getNamespace(prefix);
			if (ns != null) {
				String uri = ns.getUri() + suffix;
			
				log.info("curie #1:#2 = #3", label, prefix, suffix, uri);
			
				ContentItem curieCi = contentItemService.getContentItemByUri(uri);
				if (curieCi != null) {
					return curieCi.getResource().getKiwiIdentifier();
				}
			}
			else {
				log.info("no namespace for prefix #0", prefix);
			}
		}
		
		ContentItem ci = contentItemService.getContentItemByTitle(label);
		if (ci != null) {
			return ci.getResource().getKiwiIdentifier();
		}
		
		return null;
	}
	
	private String kiwiid2label(String kiwiid) {

		ContentItem ci = contentItemService.getContentItemByKiwiId(kiwiid);
		if (ci != null) {
			KiWiUriResource uriResource = (KiWiUriResource)ci.getResource();
			String prefix = uriResource.getNamespacePrefix();
			
			if (prefix != null) {
				return prefix + ":" + uriResource.getLocalName();
			}
			
			return ci.getTitle();
		}
		
		return null;
	}
	
	public void addTag(String label) {
		if (selectedAnnotation != null) {
			ResourceUI resource = new ResourceUI(label2kiwiid(label), label);
			selectedAnnotation.getTags().add(resource);
		}
	}
	
	public void addTag() {
		String[] components = tagLabel.split(",");
		
		for(String component : components) {			
			String label = component.trim();
			addTag(label);
		}
	}
	
	public void removeTag(ResourceUI tag) {
		log.info("removeTag #0", tag);
		if (selectedAnnotation != null) {
			selectedAnnotation.getTags().remove(tag);
		}
	}
	
	public void addPredicate(String label) {
		if (selectedAnnotation != null) {
			String kiwiid = label2kiwiid(label);
			if (kiwiid == null) {
				facesMessages.add("Predicate #0 was not found. Sorry.", label);
				return;
			}
			
			ResourceUI resource = new ResourceUI(kiwiid, label);
			selectedAnnotation.getPredicates().add(resource);
		}
	}
	
	public void addPredicate() {
		String[] components = predicate.split(",");
		
		for(String component : components) {			
			String label = component.trim();
			addPredicate(label);
		}
	}
	
	public void removePredicate(ResourceUI predicate) {
		if (selectedAnnotation != null) {
			selectedAnnotation.getPredicates().remove(predicate);
		}
	}
	
	public void addType(String label) {
		if (selectedAnnotation != null) {
			String kiwiid = label2kiwiid(label);
			if (kiwiid == null) {
				// disable creating types here for now...
				
				facesMessages.add("Type #0 was not found. Sorry.", label);
				return;
			}
			
			ResourceUI resource = new ResourceUI(kiwiid, label);
			selectedAnnotation.getTypes().add(resource);
		}
	}
	
	public void addType() {
		String[] components = type.split(",");
		
		for(String component : components) {			
			String label = component.trim();
			addType(label);
		}
	}
	
	public void removeType(ResourceUI type) {
		if (selectedAnnotation != null) {
			selectedAnnotation.getTypes().remove(type);
		}
	}
	
	public void selectAnnotation() {
		log.info("selectAnnotation. selected: #0", selectedAnnotation == null ? null : selectedAnnotation.getId());
		
		if (selectedAnnotation != null) {
			this.startCoord = selectedAnnotation.getStart();
			this.endCoord = selectedAnnotation.getEnd();
			
			tagLabel = "";
			literal = "";
			predicate = "";
			type = "";
			resourceTitle = "";
			
			selectedComponentRelation = null;
		}
		
		currentSuggestions = null;
	}
	
	public void selectInterval() {
		log.info("selectInterval. #0 #1", this.startCoord, this.endCoord);
		
		selectedAnnotation = null;
		
		currentSuggestions = null;
	}

	// If the content is edited, reload the content, rerun information extraction.
	// This still breaks, not sure why... is the 
	/*@Observer({KiWiEvents.ACTIVITY_EDITCONTENTITEM})
//	@Transactional(TransactionPropagationType.SUPPORTS)
	public void listenMetadataUpdate(User user, ContentItem item) throws SystemException {
		
		if (!Transaction.instance().isActive()) {
			log.info("MetadataUpdate: transaction not active!");
			return;
		}
		
		log.info("metadata or content update #0", item.getTitle());
		
		if (currentContentItem == null) return;
		
		log.info("currentContentItem: #0", currentContentItem.getTitle());
		
		if (item.getKiwiIdentifier().equals(currentContentItem.getKiwiIdentifier())) {
			refresh();
		}
	}*/
	
	/*private ResourceUI resource2resourceUi(KiWiResource resource) {
		// TODO: this is inefficient, refactor kiwii2label to accept content items as arguments? 
		ResourceUI ret = new ResourceUI(resource.getKiwiIdentifier(), kiwiid2label(resource.getKiwiIdentifier()));
		return ret;
	}*/
	
	/**
	 * This method shall be called *after* the nested item content is inserted, to go
	 * and add all of the annotations and insert other nested items recursively 
	 */
	private int insertComponentRecursive (Document xom, Element componentRoot, ContentItem component, int offset) {
		
		Collection<NodePos> components = new LinkedList<NodePos> ();
		
		Map<Node, NodePos> positions = new HashMap<Node, NodePos> ();
		
		KiWiXomUtils.NodePosIterator iterator = new KiWiXomUtils.NodePosIterator(componentRoot, offset);
		KiWiXomUtils.NodePos np = null;
		while(iterator.hasNext()) {
			np = iterator.next();
			
			positions.put(np.getNode(), np);
			
			if (np.getNode() instanceof Element) {
				Element e = (Element)np.getNode();
				
				// let's not try to add the same component.. 
				if (e.equals(componentRoot)) {
					continue;
				}
				
				String nextComponent = e.getAttributeValue("component", Constants.NS_KIWI_HTML);
				if (nextComponent != null) {
					components.add(np);
				}
			}
		}
		
		int componentLength = 0;
		if (np != null) {
			// get the text length of the whole compoennt... should be last position + its length... 
			
			int endOffset = np.getPos();
			
			if (np.getNode() instanceof Text) {
				Text t = (Text)np.getNode();
				endOffset += t.getValue().length();
			}
			
			componentLength = endOffset - offset;
		}
		
		log.info("offset: #0, componentLength: #1", offset, componentLength);
		
		// now, move all existing annotations and suggestions after offset by componentLength
		// (because we have inserted a content inside...)
		for (AnnotationUI annotation : annotations.values()) {
			if (annotation.getStart() > offset) {
				annotation.setStart(annotation.getStart() + componentLength);
			}
			if (annotation.getEnd() > offset) {
				annotation.setEnd(annotation.getEnd() + componentLength);
			}
		}
		
		// same for suggestions
		for (SuggestionUI suggestion : suggestions) {
			if (suggestion.getStart() > offset) {
				suggestion.setStart(suggestion.getStart() + componentLength);
			}
			if (suggestion.getEnd() > offset) {
				suggestion.setEnd(suggestion.getEnd() + componentLength);
			}
		}
		
		// now insert new annotations and suggestions...
		
		// insert the annotation about this nested item
		AnnotationUI annotation = new AnnotationUI(AnnotationUI.NESTED_KIND, component.getKiwiIdentifier(), false);
		readTaggings (component, annotation);
		annotations.put(annotation.getId(), annotation);
		fillAnnotationPosition (xom, annotation);
		
		// get component relation to its parent, if any
		String relValue = componentRoot.getAttributeValue("rel");
		if (relValue != null) {
			String[] relKiwiids = relValue.trim().split(" ");
			for (String relKiwiid : relKiwiids) {
				annotation.getPredicates().add(kiwiid2resourceUi(relKiwiid));
			}
		}
		
		log.info("filled: #0:#1, guessed: #2:#3", annotation.getStart(), annotation.getEnd(), offset, offset + componentLength);
		
		// insert the annotations from the fragments...
		Collection<FragmentFacade> ffs = fragmentService.getContentItemFragments(component, FragmentFacade.class);
		for (FragmentFacade ff : ffs) {
			annotation = new AnnotationUI(AnnotationUI.FRAGMENT_KIND, ff.getKiwiIdentifier(), false);
			readTaggings (ff.getDelegate(), annotation);
			annotations.put(annotation.getId(), annotation);
			fillAnnotationPosition (xom, annotation);
		}
		
		// insert RDFa datatype properties
		// TODO: create a utility class for working with RDFa
		Nodes nodes = componentRoot.query("descendant-or-self::node()[@property]");

		for(int i = 0; i < nodes.size(); ++i) {
			Element elem = (Element) nodes.get(i);
			
			Attribute propertyAttribute = elem.getAttribute("property");
			
			annotation = new AnnotationUI(AnnotationUI.RDF_DATATYPE_KIND, "uri::annotation:" + (++maxid), false);
			
			if (propertyAttribute != null) {
				String property = propertyAttribute.getValue();
				String[] propertyKiwiids = property.trim().split(" ");
				
				for (String propertyKiwiid : propertyKiwiids) {
					String label = kiwiid2label (propertyKiwiid);
					if (label != null) {
						annotation.getPredicates().add(new ResourceUI(propertyKiwiid, label));
					}
				}
			}
			
			annotation.setLiteral(KiWiXomUtils.xom2plain(elem));
			
			// get the coordinates.
			int start = positions.get(elem).getPos();
			int end = start;
			Node lastText = KiWiXomUtils.getLastTextNode(elem);
			if (lastText != null) {
				end = positions.get(lastText).getPos() + ((Text)lastText).getValue().length();
			}
			
			annotation.setStart(start);
			annotation.setEnd(end);
			
			annotations.put(annotation.getId(), annotation);
			
			// Fragmentize, which means, create start and end bookmarks. I know RDFa is not a fragment
			// we just treat them as such in this Action, to make handling of all annotation more general.
			Context ctx = new Context();
			ctx.setIsFragment(true);
			ctx.setInBegin(start);
			ctx.setInEnd(end);
			ctx.setContextBegin(start);
			ctx.setContextEnd(end);
			
			ctx.fragmentize(xom, annotation.getId(), 0);
			
			// delete the attribute in the XOM, we will recreate it while saving
			if (propertyAttribute != null) {
				elem.removeAttribute(propertyAttribute);
			}
			
			//delete the node whole node actually...
			// TODO: is it safe? (or is it safe only if it is not a block node, so the text coords do not change?)
			ParentNode pn = elem.getParent();
			int pasteTo = pn.indexOf(elem);
			while (elem.getChildCount() > 0) {
				Node child = elem.getChild(elem.getChildCount() - 1);
				child.detach();
				pn.insertChild(child, pasteTo);
			}
			
			pn.removeChild(elem);
		}
		
		// TODO: this block shares some common code with the previous one, refactor.
		nodes = componentRoot.query("descendant-or-self::node()[@resource] | " +
				"descendant-or-self::node()[@typeof] | " +
				"descendant-or-self::node()[@rel]");
		for(int i = 0; i < nodes.size(); ++i) {
			Element elem = (Element) nodes.get(i);
			
			// typeof and rel can also be on nested items, which are identified by "about"
			if (elem.getAttribute("about") != null) continue;
			// now, actually nested items in the internal format use kiwi:component
			if (elem.getAttribute("component", Constants.NS_KIWI_HTML) != null) continue;
			
			Attribute resourceAttribute = elem.getAttribute("resource");
			Attribute typeofAttribute = elem.getAttribute("typeof");
			Attribute relAttribute = elem.getAttribute("rel");
			
			annotation = new AnnotationUI(AnnotationUI.RDF_OBJECT_KIND, "uri::annotation:" + (++maxid), false);
			
			// We treat both property and rel attributes as a "predicate", since we don't support both
			// property and rel on one element, let's suppose rel has a priority (it should never be possible 
			// to create both at one element by any tool in KiWi anyway... ) 
			if (relAttribute != null) {
				String rel = relAttribute.getValue();
				String[] relKiwiids = rel.trim().split(" ");
				
				for (String relKiwiid : relKiwiids) {
					/*String label = kiwiid2label (relKiwiid);
					if (label != null) {
						annotation.getPredicates().add(new ResourceUI(relKiwiid, label));
					}*/
					
					annotation.getPredicates().add(kiwiid2resourceUi(relKiwiid));
				}
			}
			
			annotation.setLiteral(KiWiXomUtils.xom2plain(elem));
			
			if (resourceAttribute != null) {
				String resourceStr = resourceAttribute.getValue();
				String[] resourceKiwiids = resourceStr.trim().split(" ");
				
				for (String resourceKiwiid : resourceKiwiids) {
					/*String label = kiwiid2label (resourceKiwiid);
					if (label != null) {
						annotation.getResources().add(new ResourceUI(resourceKiwiid, label));
					}*/
					
					annotation.getResources().add(kiwiid2resourceUi(resourceKiwiid));
				}
			}
			
			if (typeofAttribute != null) {
				String typeofStr = typeofAttribute.getValue();
				String[] typeKiwiids = typeofStr.trim().split(" ");
				
				for (String typeofKiwiid : typeKiwiids) {
					/*
					String label = kiwiid2label (typeofKiwiid);
					if (label != null) {
						annotation.getTypes().add(new ResourceUI(typeofKiwiid, label));
					}*/
					
					annotation.getTypes().add(kiwiid2resourceUi(typeofKiwiid));
				}
			}
			
			// get the coordinates.
			int start = positions.get(elem).getPos();
			int end = start;
			Node lastText = KiWiXomUtils.getLastTextNode(elem);
			if (lastText != null) {
				end = positions.get(lastText).getPos() + ((Text)lastText).getValue().length();
			}
			
			annotation.setStart(start);
			annotation.setEnd(end);
			
			annotations.put(annotation.getId(), annotation);
			
			log.info("annotation #0, start #1 end #2 label #3", annotation.getId(), start, end, annotation.getLiteral());
			
			// Fragmentize, which means, create start and end bookmarks. I know RDFa is not a fragment
			// we just treat them as such in this Action, to make handling of all annotation more general.
			Context ctx = new Context();
			ctx.setIsFragment(true);
			ctx.setInBegin(start);
			ctx.setInEnd(end);
			ctx.setContextBegin(start);
			ctx.setContextEnd(end);
			
			ctx.fragmentize(xom, annotation.getId(), 0);
			
			// delete the attribute in the XOM, we will recreate it while saving
			if (resourceAttribute != null) {
				elem.removeAttribute(resourceAttribute);
			}
			if (typeofAttribute != null) {
				elem.removeAttribute(typeofAttribute);
			}
			if (relAttribute != null) {
				elem.removeAttribute(relAttribute);
			}
			
			//delete the node whole node actually...
			// TODO: is it safe? (or is it safe only if it is not a block node, so the text coords do not change?)
			ParentNode pn = elem.getParent();
			int pasteTo = pn.indexOf(elem);
			while (elem.getChildCount() > 0) {
				Node child = elem.getChild(elem.getChildCount() - 1);
				child.detach();
				pn.insertChild(child, pasteTo);
			}
			
			pn.removeChild(elem);
		}
		
		// insert the suggestions...
		for(Suggestion suggestion : informationExtractionService.getSuggestionsByTextContent(component.getTextContent())) {
			log.info("iterate suggestions: #0 #1 #2 #3", suggestion.getId(), suggestion.getExtractletName(), suggestion.getKind(), suggestion.getScore());
			if (suggestion.getInstance() == null) {
				continue;
			}
			
			if (suggestion.getInstance().getContext() == null) {
				continue;
			}
			
			Context ctx = suggestion.getInstance().getContext();
			
			int start;
			int end;
			
			if (!ctx.getIsFragment()) {
				start = offset;
				end = offset + componentLength;
			}
			else {
				start = offset + ctx.getInBegin();
				end = offset + ctx.getInEnd();
			}
			
			// TODO: Suggestions share one Instance... create only one Annotation per Instance... 
			SuggestionUI suggestionUi = new SuggestionUI(suggestion, suggestion.getKind(), start, end);
			suggestionUi.setLabel(suggestion.getLabel());
			suggestionUi.setScore(suggestion.getScore());
			
			// convert all the resources in the original Suggestion to SuggestionUI, so we can then easily transfer to annotation...
			for (KiWiResource resource : suggestion.getResources()) {
				suggestionUi.getResources().add(kiwiResource2resourceUi (resource));
			}
			for (KiWiResource type : suggestion.getTypes()) {
				suggestionUi.getTypes().add(kiwiResource2resourceUi (type));
			}
			for (KiWiResource role : suggestion.getRoles()) {
				suggestionUi.getPredicates().add(kiwiResource2resourceUi (role));
			}
			suggestions.add(suggestionUi);

			// We don't create suggestion annotation for just tags...
			if (ctx.getIsFragment() && (suggestion.getKind() == Suggestion.ENTITY
					|| suggestion.getKind() == Suggestion.NESTED_ITEM
					|| suggestion.getKind() == Suggestion.TAGGED_FRAGMENT)) {
				annotation = null;
				// TODO:check if the suggestion adds anything, if so, create a
				// suggestion, but no annotation
				for (AnnotationUI existingAnnotation : annotations.values()) {
					if (start == existingAnnotation.getStart()
							&& end == existingAnnotation.getEnd()) {
						// continue iterate_suggestions;

						annotation = existingAnnotation;
						break;
					}
				}

				if (annotation == null) {
					int id = ++maxid;
					annotation = new AnnotationUI(AnnotationUI.EMPTY,
							"uri::annotation:" + id, true);
					annotation.setStart(start);
					annotation.setEnd(end);

					annotations.put(annotation.getId(), annotation);

					ctx.fragmentize(xom, annotation.getId(), offset);

					// fillAnnotationPosition (currentDocument, annotation);
				}
			}
		}
		
		// recurse...
		// TODO: list of components in the tree, so we can block infinite recursion here...
		int nextOffset = 0;
		for (NodePos nextComponentNodePos : components) {
			Element e = (Element)nextComponentNodePos.getNode();
			String nextComponentId = e.getAttributeValue("component", Constants.NS_KIWI_HTML);
			
			if (nextComponentId != null) {
				ContentItem nextComponent = contentItemService.getContentItemByKiwiId(nextComponentId);
				
				TextContent componentTc = nextComponent.getTextContent();
				if (componentTc == null) {
					log.info("textContent for #0 is null!", nextComponentId);
					continue;
				}
				
				Document componentDoc = componentTc.copyXmlDocument();
				if (componentDoc == null) {
					log.info("textContent for #0 has empty Document!", nextComponentId);
					continue;
				}
				
				// log.info("adding content #0: #1", nextComponentId, componentDoc.toXML());
				/* Add the document contents to our document, not including the root div element */
				Element nextComponentRoot = componentDoc.getRootElement();
				while(nextComponentRoot.getChildCount() > 0) {
					Node child = nextComponentRoot.getChild(0);
					child.detach();
					e.appendChild(child);
					
					log.info("appending child: #0", child.toXML());
				}
			
				// recurse, not entirely sure why position - 1, I guess so that this div is not counted twice...
				// insertComponentRecursive (xom, e, nextComponent, nextComponentNodePos.getPos() - 1);
				int nextComponentLength = insertComponentRecursive (xom, e, nextComponent, nextComponentNodePos.getPos() + nextOffset - 1);
				nextOffset += nextComponentLength;
			}
		}
		
		return componentLength + nextOffset;
	}
	
	/**
	 * Extracts the suggestions and refreshes the display to show them.
	 */
	public void suggest() {
		informationExtractionService.extractInformation(currentContentItem);
		begin();
	}
	
	@Create
	@Begin(join=true)
	public void begin() {
		
		log.debug("create");
		
		currentDocument = null;
		annotations = new HashMap<String, AnnotationUI>();
		suggestions = new HashSet<SuggestionUI>();
		currentSuggestions = null;
		currentContentHtml = null;
		
		if (currentContentItem != null && currentContentItem.getTextContent() != null) {		
			currentDocument = currentContentItem.getTextContent().copyXmlDocument();
		
			insertComponentRecursive (currentDocument, currentDocument.getRootElement(), currentContentItem, 0);
		
			// log.info("currentDocument: #0", currentDocument.toXML());
		}
	}
	
	/**
	 * Fill a annotation with tags, types and comments from a content item.
	 * TODO: rename to readMetadata
	 */
	private void readTaggings (ContentItem ci, AnnotationUI annotation) {
		List<Tag> tags = taggingService.getTags(ci);
		for (Tag tag : tags) {
			annotation.getTags().add(new ResourceUI(tag.getTaggingResource().getKiwiIdentifier(), tag.getTaggingResource().getTitle()));
		}
		
		for (KiWiResource type : ci.getTypes()) {
			annotation.getTypes().add(new ResourceUI(type.getKiwiIdentifier(), kiwiid2label(type.getKiwiIdentifier())));
		}
		
		for (ContentItem comment : commentService.listComments(ci)) {
			CommentUI commentUi = new CommentUI(comment.getKiwiIdentifier(), comment.getTitle());
			commentUi.setHtml(renderingPipeline.renderHTML(comment));
			commentUi.setAuthor(comment.getAuthor());
			annotation.getComments().add(commentUi);
		}
		
		for (KiWiTriple triple : ci.getResource().listOutgoing()) {
						
			if (triple.getObject().isUriResource() || triple.getObject().isAnonymousResource()) {
				ObjectPropertyUI property = new ObjectPropertyUI ();
				property.setPredicate(kiwiResource2resourceUi(triple.getProperty()));
				property.setObject(kiwiResource2resourceUi((KiWiResource)triple.getObject())); 
				
				if (triple.isContentBasedOutgoing() || triple.isInferred()) {
					property.setInferred(true);
				}
				
				// we don't expose internal properties... TODO: don't hide all these ontologies, just the properties that should not be seen
				String propertyUri = triple.getProperty().getUri();
				if (propertyUri.startsWith(Constants.NS_KIWI_CORE) || 
						propertyUri.startsWith(Constants.NS_RDFS) || 
						propertyUri.startsWith(Constants.NS_DC_TERMS) ||
						propertyUri.startsWith(Constants.NS_OWL) ||
						propertyUri.startsWith(Constants.NS_RDF)) {
					property.setHidden(true);
				}
				
				annotation.getObjectProperties().add(property);
			}
			else if (triple.getObject().isLiteral()) {
				DatatypePropertyUI property = new DatatypePropertyUI();
				
				property.setPredicate(new ResourceUI(triple.getProperty().getKiwiIdentifier(), triple.getProperty().getLabel()));
				property.setLiteral(((KiWiLiteral)triple.getObject()).getContent());
				
				if (triple.isContentBasedOutgoing() || triple.isInferred()) {
					property.setInferred(true);
				}
				
				// we don't expose internal properties... TODO: don't hide all these ontologies, just the properties that should not be seen
				String propertyUri = triple.getProperty().getUri();
				if (propertyUri.startsWith(Constants.NS_KIWI_CORE) || 
						propertyUri.startsWith(Constants.NS_RDFS) || 
						propertyUri.startsWith(Constants.NS_DC_TERMS) ||
						propertyUri.startsWith(Constants.NS_OWL)  ||
						propertyUri.startsWith(Constants.NS_RDF)) {
					property.setHidden(true);
				}
				
				annotation.getDatatypeProperties().add(property);
			}
		}
	}
	
	private KiWiResource resourceUi2kiwiResource(ResourceUI ui) {
		
		if (ui.getKiwiid() != null) {
			return contentItemService.getContentItemByKiwiId(ui.getKiwiid()).getResource();
		}
		else {
			ContentItem ci;
			ci = contentItemService.createContentItem("content/"+ui.getLabel().toLowerCase().replace(" ","_")+"/"+UUID.randomUUID().toString());
			// predicateItem.addType(tripleStore.createUriResource(Constants.NS_KIWI_CORE+"Tag"));
			contentItemService.updateTitle(ci, ui.getLabel());
			//kiwiEntityManager.persist(predicateItem);
			contentItemService.saveContentItem(ci);
			
			return ci.getResource();
		}
	}
	
	private ResourceUI kiwiid2resourceUi (String kiwiid) {
		ResourceUI ret = null;
		
		ContentItem item = contentItemService.getContentItemByKiwiId(kiwiid);
		if (item != null) {
			return kiwiResource2resourceUi(item.getResource());
		}
		else {
			log.error("No resource with kiwiId #0 found", kiwiid);
			return new ResourceUI(kiwiid, kiwiid);
		}
	}
	
	private ResourceUI kiwiResource2resourceUi(KiWiResource resource) {
		ResourceUI ret = new ResourceUI(resource.getKiwiIdentifier(), resource.getLabel());
		
		//we add types, but not recursively. Types are not that interesting... 
		for (KiWiResource typeResource : resource.getTypes()) {
			ResourceUI typeUi = new ResourceUI(typeResource.getKiwiIdentifier(), typeResource.getLabel());
			
			ret.getTypes().add(typeUi);
		}
		
		return ret;
	}
	
	private void updateTaggings (AnnotationUI annotation, ContentItem ci) {
		List<Tag> currentTags = taggingService.getTags(ci);
		Collection<KiWiResource> currentTypes = ci.getTypes();
		
		// delete those that have been deleted...
		for (Tag tag : currentTags) {
			String tagKiwiid = tag.getTaggingResource().getKiwiIdentifier();
			
			boolean containsTag = false;
			for (ResourceUI annotationTag : annotation.getTags()) {
				if (annotationTag.getKiwiid().equals(tagKiwiid)) {
					containsTag = true;
					break;
				}
			}
			
			if (!containsTag) {
				taggingService.removeTagging(tag);
			}
		}
		
		for (KiWiResource type : currentTypes) {
			String typeKiwiid = type.getKiwiIdentifier();
			
			boolean containsType = false;
			for (ResourceUI annotationType : annotation.getTypes()) {
				if (annotationType.getKiwiid().equals(typeKiwiid)) {
					containsType = true;
					break;
				}
			}
			
			if (!containsType) {
				ci.getResource().removeType((KiWiUriResource)type);
			}
		}
		
		// add those that have been added
		labelIter: for (ResourceUI tag : annotation.getTags()) {
			for (Tag currentTag : currentTags) {
				if (tag.getKiwiid().equals(currentTag.getTaggingResource().getKiwiIdentifier())) {
					continue labelIter;
				}
			}
			
			// create the actual tagging, (code copied from TaggingAction)
			// TODO: cleanup
			ContentItem taggingItem = contentItemService.getContentItemByKiwiId(tag.getKiwiid());
			if(taggingItem == null) {
				// create new Content Item of type "tag" if the tag does not yet exist
				taggingItem = contentItemService.createContentItem("content/"+tag.getLabel().toLowerCase().replace(" ","_")+"/"+UUID.randomUUID().toString());
				taggingItem.addType(tripleStore.createUriResource(Constants.NS_KIWI_CORE+"Tag"));
				contentItemService.updateTitle(taggingItem, tag.getLabel());
				kiwiEntityManager.persist(taggingItem);
				log.info("created new content item for non-existant tag");
			}

			taggingService.createTagging(tag.getLabel(), ci, taggingItem, currentUser);
		}
		
		labelIter2: for (ResourceUI type : annotation.getTypes()) {
			for (KiWiResource currentType : currentTypes) {
				if (type.getKiwiid().equals(currentType.getKiwiIdentifier())) {
					continue labelIter2;
				}
			}
			
			// create the actual tagging, (code copied from TaggingAction)
			// TODO: cleanup
			ContentItem newType = contentItemService.getContentItemByKiwiId(type.getKiwiid());
			if(newType != null) {
				// we don't try to create non-existing types.
				// TODO: should we?
				
				ci.addType((KiWiUriResource)newType.getResource());
			}
		}
		
		// delete the deleted comments
		commentIter: for (ContentItem comment : commentService.listComments(ci)) {
			for (CommentUI commentUi : annotation.getComments()) {
				if (commentUi.getKiwiid() == null || commentUi.getKiwiid().equals(comment.getResource().getKiwiIdentifier())) {
					continue commentIter;
				}
			}
			
			// remove the comment
			contentItemService.removeContentItem(comment);
		}
		
		// add new comments
		for (CommentUI commentUi : annotation.getComments()) {
			if (commentUi.getKiwiid() == null) {
				// add new comment
				ContentItem newComment = commentService.createReply(ci, currentUser, commentUi.getLabel(), commentUi.getHtml());
				commentUi.setKiwiid(newComment.getResource().getKiwiIdentifier());
			}
		}
		
		Collection<KiWiTriple> outgoingTriples = ci.getResource().listOutgoing();
		
		// Datatype and object properties
		// delete the removed ones.
	deleteTriples:
		for (KiWiTriple triple : outgoingTriples) {
			KiWiUriResource predicate = triple.getProperty();
			KiWiNode object = triple.getObject();
			if (object.isLiteral()) {
				KiWiLiteral literal = (KiWiLiteral)object;
				
				for (DatatypePropertyUI ui : annotation.getDatatypeProperties()) {
					if (predicate.getKiwiIdentifier().equals(ui.getPredicate().getKiwiid()) && literal.getContent().equals(ui.getLiteral())) {
						continue deleteTriples;
					}
				}
			}
			else if (object.isUriResource() || object.isAnonymousResource()) {
				KiWiResource objectResource = (KiWiResource)object;
				
				for (ObjectPropertyUI ui : annotation.getObjectProperties()) {
					if (predicate.getKiwiIdentifier().equals(ui.getPredicate().getKiwiid()) && objectResource.getKiwiIdentifier().equals(ui.getObject().getKiwiid())) {
						continue deleteTriples;
					}
				}
			}
			
			// we delete the triple
			tripleStore.removeTriple(triple);
		}
		
		// we now add the new triples, datatype properties...
	addDatatypeProperties:	
		for (DatatypePropertyUI ui : annotation.getDatatypeProperties()) {
			for (KiWiTriple triple : outgoingTriples) {
				KiWiResource predicate = triple.getProperty();
				if (triple.getObject().isLiteral()) {
					KiWiLiteral literal = (KiWiLiteral)triple.getObject();
					if (predicate.getKiwiIdentifier().equals(ui.getPredicate().getKiwiid()) && literal.getContent().equals(ui.getLiteral())) {
						continue addDatatypeProperties;
					}
				}
			}
			
			// we add the triple
			KiWiLiteral literal = tripleStore.createLiteral(ui.getLiteral());
			KiWiUriResource predicate = (KiWiUriResource)resourceUi2kiwiResource(ui.getPredicate());
			tripleStore.createTriple(ci.getResource(), predicate, literal);
		}
		
	addObjectProperties:
		for (ObjectPropertyUI ui : annotation.getObjectProperties()) {
			for (KiWiTriple triple : outgoingTriples) {
				KiWiResource predicate = triple.getProperty();
				if (triple.getObject().isAnonymousResource() || triple.getObject().isUriResource()) {
					KiWiResource objectResource = (KiWiResource)triple.getObject();
					if (predicate.getKiwiIdentifier().equals(ui.getPredicate().getKiwiid()) && objectResource.getKiwiIdentifier().equals(ui.getObject().getKiwiid())) {
						continue addObjectProperties;
					}
				}
			}
			
			// we add the triple
			KiWiResource object = resourceUi2kiwiResource(ui.getObject());
			KiWiUriResource predicate = (KiWiUriResource)resourceUi2kiwiResource(ui.getPredicate());
			tripleStore.createTriple(ci.getResource(), predicate, object);
		}
	}
	
	/**
	 * We need to do a similar operation as the FragmentSavelet do... but we can't use it, since the fragments are not persited yet 
	 * (fragment savelet would not find them through fragmentService... so this is a modified copy of a fragmentsavelet...
	 */
	private TextContent fragmentSavelet(KiWiResource context, TextContent content, Map<String, FragmentFacade> kiwiid2ff) {		
		for (String fragmentId : content.getFragmentIds()) {
			
			FragmentFacade ff = kiwiid2ff.get(fragmentId);
			if (ff == null) {
				log.info("ff is null for fragmentId #0", fragmentId);
				continue;
			}
			
			TextFragment fragment = content.getFragment(((KiWiUriResource)ff.getResource()));
			
			ContentItem _item = ff.getDelegate();
			
			contentItemService.updateTextContentItem(_item, fragment.getFragmentTree());

			// update last author
			_item.setAuthor(currentUser);

			// update modification
			_item.setModified(new Date());

			contentItemService.saveContentItem(_item);
		}
		
		return content;
	}
	
	private TextContent componentSavelet(KiWiResource context, TextContent content, Map<String, ContentItem> kiwiid2ci) {
		Set<KiWiResource> components = new HashSet<KiWiResource> ();
		
		/* Update the content of the nested content items (components) */
		componentSaveletRecursive(content.getXmlDocument().getRootElement(), context, components, kiwiid2ci);
		
		/* Update the list of all nested content items of this "context" content item */
		List<ContentItem> nestedContentItems = new LinkedList<ContentItem> ();
		for (KiWiResource component: components) {
			nestedContentItems.add(component.getContentItem());
		}
		context.getContentItem().setNestedContentItems(nestedContentItems);
		return content;
	}
	
	private TextContent rdfaSavelet(KiWiResource context, TextContent content, final Map<String, ContentItem> kiwiid2ci) {
		RdfaSavelet savelet = new RdfaSavelet(new RdfaSavelet.ContentItemProvider() {
			@Override
			public ContentItem get(String kiwiid) {
				ContentItem ret = kiwiid2ci.get(kiwiid);
				if (ret == null) {
					ret = contentItemService.getContentItemByKiwiId(kiwiid);
				}
				
				return ret;
			}
		}, tripleStore, log);
		
		log.info("rdfaSavelet: #0", content.getXmlString());
		
		return savelet.apply(context, content);
	}
	
	private boolean isKiWiRootTag (Node n) {
		if (!(n instanceof Element)) return false;
		Element e = (Element)n;
		return "div".equals(e.getLocalName()) && "page".equals(e.getAttributeValue("type", Constants.NS_KIWI_HTML));
	}
	
	private void componentSaveletRecursive(Element e, KiWiResource context, Set<KiWiResource> components, Map<String, ContentItem> kiwiid2ci) {
		/* The context for all the nodes below, the context does not change by default */
		KiWiResource nextContext = context;
		Set<KiWiResource> nextComponents = components;
		
		String componentKiwiId = e.getAttributeValue("component", Constants.NS_KIWI_HTML);
		ContentItem componentCi = null; 
		
		if (componentKiwiId != null) {
			componentCi = kiwiid2ci.get(componentKiwiId);
			if (componentCi == null) {
				log.info("component #0 not found", componentKiwiId);
				return;
			}
			
			/* We are at the "about div" element, below as is the content of this component */
			nextContext = componentCi.getResource();
			
			/* Store all the components of this new component. */
			nextComponents = new HashSet<KiWiResource> ();
		}
		
		Elements children = e.getChildElements();
		for (int i = 0; i < children.size(); ++i) {
			componentSaveletRecursive (children.get(i), nextContext, nextComponents, kiwiid2ci);
		}
		
		if (componentKiwiId != null) {
			
			/* update the list of components of the component "above" */
			components.add(componentCi.getResource());
			
			/* and update the nested content items property of _this_ content item. */
			List<ContentItem> nestedContentItems = new LinkedList<ContentItem> ();
			for (KiWiResource component: nextComponents) {
				nestedContentItems.add(component.getContentItem());
			}
			componentCi.setNestedContentItems(nestedContentItems);
			
			/* TODO: We should delete the components which are not standalone content items and not contained anywhere anymore
			 * (there is no such thing as "non-standalone" content item yet) */
			
			/* We have transformed all the content below, so now store the component content. */
			Document componentDoc = null;
			
			if (e.getChildCount() == 1 && isKiWiRootTag(e.getChild(0))) {
				Element root = children.get(0);
				
				root.detach();
				componentDoc = new Document(root);		
			}
			else {
				Element root = new Element("div", Constants.NS_XHTML);
				root.addAttribute(new Attribute("kiwi:type", Constants.NS_KIWI_HTML, "page"));
				while (e.getChildCount() > 0) {
					Node n = e.getChild(0);
					n.detach();
					root.appendChild(n);
				}
				
				componentDoc = new Document(root);
			}
			
			if (componentCi.getTextContent() == null) {
				log.debug("previous textcontent is null");
			}
			else {
				log.debug("previous textcontent: #0", componentCi.getTextContent().getXmlString());
			}
			
			//log.info("storing component #0: #1", componentKiwiId, componentDoc.toXML());
			
			contentItemService.updateTextContentItem(componentCi, componentDoc);

			// update last author
			componentCi.setAuthor(currentUser);

			// update modification
			componentCi.setModified(new Date());

			contentItemService.saveContentItem(componentCi);

			//log.info("stored textcontent: #0", componentCi.getTextContent().getXmlString());
		}
	}
	
	private String getFragmentContent (Document xom, String fragmentId) {
		XPathContext namespaces = new XPathContext();
        namespaces.addNamespace("kiwi", Constants.NS_KIWI_HTML);
        namespaces.addNamespace("html", Constants.NS_XHTML);
	
		Nodes nodes;
		
		Node bookmarkstart = null;
		Node bookmarkend = null;
		
		nodes = xom.query("//kiwi:bookmarkstart[@id=\"" + fragmentId + "\"]", namespaces);
		if (nodes.size() == 0) {
			log.info("no bookmarkstart node with id '#0' found", fragmentId);
			return null;
		}
		bookmarkstart = nodes.get(0);
		
		nodes = xom.query("//kiwi:bookmarkend[@id=\"" + fragmentId + "\"]", namespaces);
		if (nodes.size() == 0) {
			log.info("no bookmarkend node with id '#0' found!", fragmentId);
			return null;
		}
		bookmarkend = nodes.get(0);
		
		KiWiXomUtils.NodePosIterator iter = new KiWiXomUtils.NodePosIterator(xom.getRootElement());
		
		StringBuilder sb = new StringBuilder();
		boolean appending = false;
		while (iter.hasNext()) {
			NodePos np = iter.next();
			
			if (np.getNode() instanceof Element) {
				if (np.getNode().equals(bookmarkstart)) {
					appending = true;
					continue;
				}
				
				if (np.getNode().equals(bookmarkend)) {
					appending = false;
					continue;
				}
			}
			
			if (appending) {
				if (np.getPrefix() != null) {
					sb.append(np.getPrefix());
				}
				
				if (np.getNode() instanceof Text) {
					Text text = (Text)np.getNode();
					sb.append(text.getValue());
				}
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * Replaces a fragment with an element, `flatten' the DOM in between, but keep all fragment tags inside...
	 * TODO: this version does not keep the fragments inside. 
	 * @param xom
	 * @param e
	 * @param fragmentId
	 */
	private void replaceFragmentContentWithNodes (Document xom, List<Node> insertNodes, String fragmentId) {
		XPathContext namespaces = new XPathContext();
        namespaces.addNamespace("kiwi", Constants.NS_KIWI_HTML);
        namespaces.addNamespace("html", Constants.NS_XHTML);
	
		Nodes nodes;
		
		Node bookmarkstart = null;
		Node bookmarkend = null;
		
		nodes = xom.query("//kiwi:bookmarkstart[@id=\"" + fragmentId + "\"]", namespaces);
		if (nodes.size() == 0) {
			log.info("no bookmarkstart node with id '#0' found", fragmentId);
			return;
		}
		bookmarkstart = nodes.get(0);
		
		nodes = xom.query("//kiwi:bookmarkend[@id=\"" + fragmentId + "\"]", namespaces);
		if (nodes.size() == 0) {
			log.info("no bookmarkend node with id '#0' found!", fragmentId);
			return;
		}
		bookmarkend = nodes.get(0);
		
		int originalStart = 0;
		int originalEnd = 0;
		int finalStart = 0;
		int finalEnd = 0;
		
		//Node parent = KiWiXomUtils.getLeastCommonParent(bookmarkstart, bookmarkend);
		
		//KiWiXomUtils.get
		// Now, iterate through the content, get the text content out of there, to be replaced with something else
		// TODO: this solution is not optimal and probably does not work on some cases, do it better (empty elements may remain)
		KiWiXomUtils.NodePosIterator iter = new KiWiXomUtils.NodePosIterator(xom.getRootElement());
		
		boolean moving = false;
		
		List<Node> textNodes = new LinkedList<Node> ();
		
		while (iter.hasNext()) {
			NodePos np = iter.next();
			
			if (np.getNode() instanceof Element) {
				if (np.getNode().equals(bookmarkstart)) {
					originalStart = np.getPos();
					moving = true;
					continue;
				}
				
				if (np.getNode().equals(bookmarkend)) {
					originalEnd = np.getPos();
					moving = false;
					continue;
				}
			}
			
			if (moving) {
				if (np.getNode() instanceof Text) {
					textNodes.add(np.getNode());
				}
			}
		}
		
		// replace the bookmark element by the element to place
		ParentNode pnode = bookmarkstart.getParent();
		//pnode.replaceChild(bookmarkstart, e);
		//bookmarkend.detach();
		
		int index = pnode.indexOf(bookmarkstart);
		for (int i = 0; i < insertNodes.size(); ++i) {
			// appending after the bookmarkstart
			pnode.insertChild(insertNodes.get(i), index + i + 1);
		}
		
		// remove the old text nodes...
		for (Node node : textNodes) {
			node.detach();
		}
		
		// now, we need to again recompute the positions of the fragments, so we can appropriately move the offsets of annotations and suggestions
		iter = new KiWiXomUtils.NodePosIterator(xom.getRootElement());
		while (iter.hasNext()) {
			NodePos np = iter.next();
			
			if (np.getNode() instanceof Element) {
				if (np.getNode().equals(bookmarkstart)) {
					finalStart = np.getPos();
				}
				
				if (np.getNode().equals(bookmarkend)) {
					finalEnd = np.getPos();
				}
			}
		}
		
		// now, we can move the annotations and suggestions...
		
		//TODO: what about fragments at the same position? (we can't tell if they are after, or before... ), needs to be handled somehow...
		// Maybe it would be better to just reset all the fragment annotations in the nodepos iteration above... 
		for (AnnotationUI annotation : annotations.values()) {
			if (annotation.getStart() > originalStart) {
				annotation.setStart(annotation.getStart() - (originalEnd - originalStart) + (finalEnd - finalStart));
			}
			if (annotation.getEnd() > originalStart) {
				annotation.setEnd(annotation.getEnd() - (originalEnd - originalStart) + (finalEnd - finalStart));
			}
		}
		
		for (SuggestionUI suggestion : suggestions) {
			if (suggestion.getStart() > originalStart) {
				suggestion.setStart(suggestion.getStart() - (originalEnd - originalStart) + (finalEnd - finalStart));
			}
			if (suggestion.getEnd() > originalStart) {
				suggestion.setEnd(suggestion.getEnd() - (originalEnd - originalStart) + (finalEnd - finalStart));
			}
		}
	}
		
	// @End
//	@Transactional
	public void save() {
		log.debug("save");
		
		Map<String, FragmentFacade> kiwiid2ff = new HashMap<String, FragmentFacade> ();
		Map<String, ContentItem> kiwiid2ci = new HashMap<String, ContentItem> ();
    			
		// We need to convert Annotations to actual entities, tags to actual tags and rename the temporary identifiers...
		
		for (AnnotationUI annotation : annotations.values()) {
			if (annotation.getKind() == AnnotationUI.FRAGMENT_KIND) {
				
				if (!annotation.isNew() && annotation.isDeleted()) {
					// a fragment has been deleted...
					FragmentFacade ff = fragmentService.getContentItemFragment(currentContentItem, annotation.getId(), FragmentFacade.class);
					if (ff != null) {
						fragmentService.removeFragment(ff);
					}
				}
				else if (!annotation.isNew() && !annotation.isDeleted()) {
					// an existing fragment... we need to update the taggings.
					FragmentFacade ff = fragmentService.getContentItemFragment(currentContentItem, annotation.getId(), FragmentFacade.class);
					updateTaggings (annotation, ff.getDelegate());
					
					kiwiid2ff.put(ff.getKiwiIdentifier(), ff);
				}
				else if (annotation.isNew() && !annotation.isDeleted()) {
					FragmentFacade ff = fragmentService.createFragment(currentContentItem, FragmentFacade.class);
					
					kiwiid2ff.put(ff.getKiwiIdentifier(), ff);
					
					// Rename the id of the new annotation to the id of the ff...
					XPathContext namespaces = new XPathContext();
			        namespaces.addNamespace("kiwi", Constants.NS_KIWI_HTML);
				
					Nodes nodes;					
					nodes = currentDocument.query("//kiwi:bookmarkstart[@id=\"" + annotation.getId() + "\"]", namespaces);
					for (int i = 0; i < nodes.size(); ++i) {
						Element node = (Element)nodes.get(i);
						node.addAttribute(new Attribute("id", ff.getKiwiIdentifier()));
					}
					
					nodes = currentDocument.query("//kiwi:bookmarkend[@id=\"" + annotation.getId() + "\"]", namespaces);
					for (int i = 0; i < nodes.size(); ++i) {
						Element node = (Element)nodes.get(i);
						node.addAttribute(new Attribute("id", ff.getKiwiIdentifier()));
					}
					
					updateTaggings (annotation, ff.getDelegate());
					
					fragmentService.saveFragment(ff);
				}
				else if (annotation.isNew() && annotation.isDeleted()) {
					// this actually should never happen... 
				}
			}
			else if (annotation.getKind() == AnnotationUI.NESTED_KIND) {
				if (!annotation.isNew() && annotation.isDeleted()) {
					// a nested content item has been deleted
					// TODO: there is currently no mechanism to delete nested content items... (we can't tell if this content item should
					// continue to exist by itself... 
				}
				else if (!annotation.isNew() && !annotation.isDeleted()) {
					// an existing nested item... we need to update the taggings.
					ContentItem ci = contentItemService.getContentItemByKiwiId(annotation.getId());
					updateTaggings (annotation, ci);
					
					kiwiid2ci.put(ci.getKiwiIdentifier(), ci);
				}
				else if (annotation.isNew() && !annotation.isDeleted()) {
					ContentItem nested = contentItemService.createContentItem();
					
					kiwiid2ci.put(nested.getKiwiIdentifier(), nested);
					
					// Rename the id of the new annotation to the id of the nested...
					XPathContext namespaces = new XPathContext();
			        namespaces.addNamespace("kiwi", Constants.NS_KIWI_HTML);
			        namespaces.addNamespace("html", Constants.NS_XHTML);
				
					Nodes nodes;
					
					nodes = currentDocument.query("//html:div[@kiwi:component=\"" + annotation.getId() + "\"]", namespaces);
					for (int i = 0; i < nodes.size(); ++i) {
						Element node = (Element)nodes.get(i);
						node.addAttribute(new Attribute("kiwi:component", Constants.NS_KIWI_HTML, nested.getKiwiIdentifier()));
						
						// node.addAttribute(new Attribute("about", Constants.NS_XHTML, nested.getKiwiIdentifier()));
					}
					
					updateTaggings (annotation, nested);
					
					contentItemService.saveContentItem(nested);
				}
				else if (annotation.isNew() && annotation.isDeleted()) {
					// this actually should never happen... 
				}
				
				
				if (!annotation.isDeleted()) {
					// update the XOM we add the rel and typeof attributes to the component
					
					XPathContext namespaces = new XPathContext();
			        namespaces.addNamespace("kiwi", Constants.NS_KIWI_HTML);
			        namespaces.addNamespace("html", Constants.NS_XHTML);
			        
			        StringBuilder sb = new StringBuilder();
					for (ResourceUI predicate : annotation.getPredicates()) {
						if (sb.length() > 0) sb.append(' ');
						
						sb.append(predicate.getKiwiid());
					}
					String rel = sb.toString();
					
					sb = new StringBuilder();
					for (ResourceUI type : annotation.getTypes()) {
						if (sb.length() > 0) sb.append(' ');
						
						sb.append(type.getKiwiid());
					}
					String typeof = sb.toString();
				
					Nodes nodes;
					
					nodes = currentDocument.query("//html:div[@kiwi:component=\"" + annotation.getId() + "\"]", namespaces);
					for (int i = 0; i < nodes.size(); ++i) {
						Element node = (Element)nodes.get(i);
						// node.addAttribute(new Attribute("kiwi:component", Constants.NS_KIWI_HTML, nested.getKiwiIdentifier()));
						node.addAttribute(new Attribute("rel", rel));
						node.addAttribute(new Attribute("typeof", typeof));
						// node.addAttribute(new Attribute("about", Constants.NS_XHTML, nested.getKiwiIdentifier()));
					}					
				}
			}
			else if (annotation.getKind() == AnnotationUI.RDF_DATATYPE_KIND) {
				// we can't create RDFa elements everywhere a fragment can be, so we may need to break the structure.
				// Let's convert all the nodes in between to plain text (but keep the bookmarks)
				
				StringBuilder sb = new StringBuilder();
				for (ResourceUI predicate : annotation.getPredicates()) {
					if (sb.length() > 0) sb.append(' ');
						
					String kiwiid = predicate.getKiwiid();
					if (kiwiid == null) {
						String label = predicate.getLabel();
						ContentItem predicateItem;
						predicateItem = contentItemService.createContentItem("content/"+label.toLowerCase().replace(" ","_")+"/"+UUID.randomUUID().toString());
						// predicateItem.addType(tripleStore.createUriResource(Constants.NS_KIWI_CORE+"Tag"));
						contentItemService.updateTitle(predicateItem, label);
						kiwiEntityManager.persist(predicateItem);

						kiwiid = predicateItem.getKiwiIdentifier();
					}
					sb.append(kiwiid);
				}
					
				Element elem = new Element("span", Constants.NS_XHTML);
				elem.addAttribute(new Attribute("property", sb.toString()));
				
				// the text content of the node shall be the literal value. (unless we implement the proper RDFa value attribute)
				elem.appendChild(annotation.getLiteral());
				List<Node> elemList = new ArrayList<Node> ();
				elemList.add(elem);
					
				replaceFragmentContentWithNodes (currentDocument, elemList, annotation.getId());
					
				// the previous method just replaces the content, we want to also remove the fragment bookmarks
				currentDocument = deleteFragment (currentDocument, annotation.getId());
					
				// TODO: update RDF metadata
			}
			else if (annotation.getKind() == AnnotationUI.RDF_OBJECT_KIND) {
				StringBuilder rel = new StringBuilder();
				StringBuilder types = new StringBuilder();
				
				for (ResourceUI predicate : annotation.getPredicates()) {
					if (rel.length() > 0) rel.append(' ');
						
					String kiwiid = predicate.getKiwiid();
					if (kiwiid == null) {
						String label = predicate.getLabel();
						ContentItem predicateItem;
						predicateItem = contentItemService.createContentItem("content/"+label.toLowerCase().replace(" ","_")+"/"+UUID.randomUUID().toString());
						
						// predicateItem.addType(tripleStore.createUriResource(Constants.NS_KIWI_CORE+"Tag"));
						contentItemService.updateTitle(predicateItem, label);
						//kiwiEntityManager.persist(predicateItem);
						contentItemService.saveContentItem(predicateItem);
						kiwiid = predicateItem.getKiwiIdentifier();
					}
					rel.append(kiwiid);
				}
				
				// TODO: creating types like that is a bit weird... nowhere else in kiwi can we create types like that...
				List<ContentItem> allTypes = new LinkedList<ContentItem> ();
				for (ResourceUI type : annotation.getTypes()) {
					if (types.length() > 0) types.append(' ');
						
					String kiwiid = type.getKiwiid();
					if (kiwiid == null) {
						String label = type.getLabel();
						ContentItem typeItem;
						typeItem = contentItemService.createContentItem();
						// a `type' means `an OWL class' in high societies... TODO: maybe we could just let the reasoner figure it out?
						typeItem.addType(tripleStore.createUriResource(Constants.NS_OWL+"Class"));
						contentItemService.updateTitle(typeItem, label);
						//kiwiEntityManager.persist(typeItem);
						contentItemService.saveContentItem(typeItem);
						allTypes.add(typeItem);

						kiwiid = typeItem.getKiwiIdentifier();
					}
					else {
						ContentItem typeItem = contentItemService.getContentItemByKiwiId(kiwiid);
						if (typeItem != null ){
							allTypes.add(typeItem);
						}
					}
					types.append(kiwiid);
				}
					
				Element elem = new Element("span", Constants.NS_XHTML);
				elem.addAttribute(new Attribute("rel", rel.toString()));
				
				// TODO: does the RDFa spec actually allow the typeof here? (we want to store typeof 
				// for storing an incomplete annotation, maybe we can get rid of it if resource is available...
				// NOTE: currently, the RDFa plugin in editor requires the typeof attribute...  
				elem.addAttribute(new Attribute("typeof", types.toString()));
				
				// now, set or create the resource, also set the type for the resource if we are creating it...
				// TODO: should we modify the types if we are not creating it?
				// We store ambiguous links as a list of kiwiids... 
				
				StringBuilder resourceBuilder = new StringBuilder();
				for (ResourceUI resource : annotation.getResources()) {
					String kiwiid = resource.getKiwiid();
					if (kiwiid == null) {
						ContentItem resourceItem;
						resourceItem = contentItemService.createContentItem();
						contentItemService.updateTitle(resourceItem, resource.getLabel());
						annotation.setLiteral(resource.getLabel());
						for (ContentItem type : allTypes) {
							resourceItem.addType((KiWiUriResource)type.getResource());
						}
						
						kiwiid = resourceItem.getKiwiIdentifier();
						contentItemService.saveContentItem(resourceItem);
					}
					
					if (resourceBuilder.length() > 0) {
						resourceBuilder.append(' ');
					}
					
					resourceBuilder.append(kiwiid);
				}
				if (resourceBuilder.length() > 0) {
					elem.addAttribute(new Attribute("resource", resourceBuilder.toString()));
				}
				/*if (annotation.getResource() != null) {
					ResourceUI resource = annotation.getResource();
					String kiwiid = resource.getKiwiid();
					if (kiwiid == null) {
						ContentItem resourceItem;
						resourceItem = contentItemService.createContentItem();
						contentItemService.updateTitle(resourceItem, resource.getLabel());
						annotation.setLiteral(resource.getLabel());
						for (ContentItem type : allTypes) {
							resourceItem.addType((KiWiUriResource)type.getResource());
						}
						
						kiwiid = resourceItem.getKiwiIdentifier();
						contentItemService.saveContentItem(resourceItem);
					}
					
					elem.addAttribute(new Attribute("resource", kiwiid));
				}*/
				
				// the text content of the node shall be the resource title. it is stored in "literal" property, to allow also 
				// non-specified resources to be stored... in those cases the literal should contain the previous annotated content.
				elem.appendChild(annotation.getLiteral());
				List<Node> elemList = new ArrayList<Node> ();
				elemList.add(elem);
				replaceFragmentContentWithNodes (currentDocument, elemList, annotation.getId());
					
				// remove the temporary fragment bookmarks... RDFa is not a fragment 
				currentDocument = deleteFragment (currentDocument, annotation.getId());
			}
			else if (annotation.getKind() == AnnotationUI.EMPTY) {
				// empty annotation is a kind of empty fragment. just delete...
				currentDocument = deleteFragment (currentDocument, annotation.getId());
			}
		}
		
		// Now, run the current XOM through the FragmentSavelet and ComponentSavelet, so all the nested content item text contents get updated...
		
		// We don't actually run savelets... the code is copied from the savelets and modified to use the kiwiid2ff|ci maps
		// those fragments/content items are not yet persisted, so savelets can't find them using contentItemService... 
		
		// We need to run savelets, but we don't have the proper format for saveletes (we have internal format, not
		// the editor-rendered format. 
		
		// First, generate the `about' attribute from the kiwi:component
		XPathContext namespaces = new XPathContext();
        namespaces.addNamespace("kiwi", Constants.NS_KIWI_HTML);
        namespaces.addNamespace("html", Constants.NS_XHTML);
	
		Nodes nodes;
		
		nodes = currentDocument.query("//html:div[@kiwi:component]", namespaces);
		for (int i = 0; i < nodes.size(); ++i) {
			Element node = (Element)nodes.get(i);
			
			String kiwiid = node.getAttributeValue("component", Constants.NS_KIWI_HTML);
			// node.addAttribute(new Attribute("kiwi:component", Constants.NS_KIWI_HTML, nested.getKiwiIdentifier()));
			node.addAttribute(new Attribute("about", kiwiid));
		}
		// that's it, run the savelets..
		
		// a virtual TextContent, we don't persist that one directly... 
		TextContent textContent = new TextContent();
		textContent.setXmlDocument(currentDocument);
		
		textContent = fragmentSavelet(currentContentItem.getResource(), textContent, kiwiid2ff);
		textContent = rdfaSavelet(currentContentItem.getResource(), textContent, kiwiid2ci);
		textContent = componentSavelet(currentContentItem.getResource(), textContent, kiwiid2ci);
		
		contentItemService.updateTextContentItem(currentContentItem, textContent.getXmlDocument());
		
		contentItemService.saveContentItem(currentContentItem);
		
		// TODO: metadata also changed for all the nested items...
		//Events.instance().raiseTransactionSuccessEvent(KiWiEvents.METADATA_UPDATED, currentContentItem);
	}
	
	
	public void acceptResourceSuggestion(/*SuggestionUI suggestion,*/ ResourceUI resource) {
		// suggestion.getResources().remove(resource);
		
		if (selectedAnnotation != null) {
			// TODO: this is silly, refactor the Suggestion entity so the tags and resources are distinct.
			if (selectedAnnotation.getKind() == AnnotationUI.RDF_OBJECT_KIND) {
				// object predicates can have only one resource
				this.setResource(resource);
			}
			else if (selectedAnnotation.getKind() == AnnotationUI.FRAGMENT_KIND || selectedAnnotation.getKind() == AnnotationUI.NESTED_KIND) {
				selectedAnnotation.getTags().add(resource);
			}
		}
	}
	
	/*
	public void rejectResourceSuggestion(SuggestionUI suggestion, ResourceUI resource) {
		suggestion.getResources().remove(resource);
	}*/
	
	public void acceptTypeSuggestion(/*SuggestionUI suggestion,*/ ResourceUI resource) {
		//suggestion.getTypes().remove(resource);
		
		if (selectedAnnotation != null) {
			selectedAnnotation.getTypes().add(resource);
		}
	}
	
	/*public void rejectTypeSuggestion(SuggestionUI suggestion, ResourceUI resource) {
		suggestion.getTypes().remove(resource);
	}*/
	
	public void acceptPredicateSuggestion(/*SuggestionUI suggestion,*/ ResourceUI resource) {
		//suggestion.getPredicates().remove(resource);
		
		if (selectedAnnotation != null) {
			selectedAnnotation.getPredicates().add(resource);
		}
	}
	/*
	public void rejectPredicateSuggestion(SuggestionUI suggestion, ResourceUI resource) {
		suggestion.getPredicates().remove(resource);
	}*/
	
	public void acceptSuggestion(SuggestionUI suggestion) {
		suggestions.remove(suggestion);
		currentSuggestions = null;
		
		informationExtractionService.acceptSuggestion(suggestion.getSuggestion(), currentUser);
		
		if (selectedAnnotation != null) {
			// TODO: implement all cases
			
			// if we have selected an empty annotation, accepting an entity suggestion creates a link.
			if (selectedAnnotation.getKind() == AnnotationUI.EMPTY && suggestion.getKind() == Suggestion.ENTITY) {
				// modify the annotation kind and fill all the resources.
				
				selectedAnnotation.setKind(AnnotationUI.RDF_OBJECT_KIND);
				
				// Set the literal as the text content, unless overwritten by setting a specific resource.
				String content = getFragmentContent (currentDocument, selectedAnnotation.getId());
				selectedAnnotation.setLiteral(content);
				
				if (suggestion.getResources().size() == 1) {
					// if there is exactly one resource suggestion, set that.
					this.setResource(suggestion.getResources().get(0));
				}
				else {
					// We store all resource suggestions -- ambiguous links
					for (ResourceUI resource : suggestion.getResources()) {
						selectedAnnotation.getResources().add(resource);
					}
				}
				
				for (ResourceUI predicate : suggestion.getPredicates()) {
					selectedAnnotation.getPredicates().add(predicate);
				}
				for (ResourceUI type : suggestion.getTypes()) {
					selectedAnnotation.getTypes().add(type);
				}
			}
			else if (selectedAnnotation.getKind() == AnnotationUI.EMPTY && suggestion.getKind() == Suggestion.NESTED_ITEM) {
				
				selectedAnnotation.setKind(AnnotationUI.NESTED_KIND);
				
				// we first delete the fragments for the old annotation. while converted into a nested item, the bookmarks
				// must not exist anymore ... (otherwise the annotation would still be there visible after the nested item
				// was created.
				currentDocument = deleteFragment (currentDocument, selectedAnnotation.getId());
				currentDocument = insertNested (currentDocument, selectedAnnotation.getStart(), selectedAnnotation.getEnd(), selectedAnnotation.getId());
				
				fillAnnotationPosition (currentDocument, selectedAnnotation);
				
				for (ResourceUI type : suggestion.getTypes()) {
					selectedAnnotation.getTypes().add(type);
				}
				
				log.info("computed positions: #0 #1, original: #2 #3", selectedAnnotation.getStart(), selectedAnnotation.getEnd(), startCoord, endCoord);
				
				// cause the rerendering... 
				currentContentHtml = null;
				currentSuggestions = null;
			}
			else if (selectedAnnotation.getKind() == AnnotationUI.NESTED_KIND && suggestion.getKind() == Suggestion.TYPE) {
				selectedAnnotation.getTypes().addAll(suggestion.getTypes());
			}
		}
		else {
			if (suggestion.getKind() == Suggestion.TYPE) {
				// if no annotation is selected, a type suggestion means adding the type to the current content item.
				AnnotationUI currentContentItemAnnotationUi = annotations.get(currentContentItem.getKiwiIdentifier());
				if (currentContentItemAnnotationUi != null) {
					currentContentItemAnnotationUi.getTypes().addAll(suggestion.getTypes());
				}
			}
		}
	}
	
	public void rejectSuggestion(SuggestionUI suggestion) {
		suggestions.remove(suggestion);
		currentSuggestions = null;
		
		informationExtractionService.rejectSuggestion(suggestion.getSuggestion(), currentUser);
	}
	
	// Comments
	private String commentTitle;
	private String commentHtml;
	
	public void setCommentTitle (String title) {
		this.commentTitle = title;
	}
	
	public String getCommentTitle () {
		return commentTitle;
	}
	
	public void setCommentHtml (String html) {
		this.commentHtml = html;
	}
	
	public String getCommentHtml () {
		return commentHtml;
	}
	
	public void addComment() {
		
		if (selectedAnnotation != null) {
			
			CommentUI comment = new CommentUI (null, commentTitle);
			comment.setAuthor(currentUser);
			comment.setHtml(commentHtml);
			selectedAnnotation.getComments().add(comment);
			
			commentTitle = "";
			commentHtml = "";
		}
	}
	
	public void updateDatatypeProperty(DatatypePropertyUI property) {
		// do nothing
	}
	
	public void removeDatatypeProperty(DatatypePropertyUI property) {
		if (selectedAnnotation != null) {
			selectedAnnotation.getDatatypeProperties().remove(property);
		}
	}
	
	public void removeObjectProperty(ObjectPropertyUI property) {
		if (selectedAnnotation != null) {
			selectedAnnotation.getObjectProperties().remove(property);
		}
	}
	
	private transient Map<String, KiWiProperty> properties = null;
	private Map<String, KiWiProperty> getPropertyMap() {
		if (properties == null) {
			properties = new HashMap<String, KiWiProperty> ();
			for(KiWiProperty property : ontologyService.listProperties()) {
				// TODO: this filter out too general ontologies, remove it after demo
				if (!property.getResource().isUriResource()) continue;
				
				String propertyUri = ((KiWiUriResource)property.getResource()).getUri();
				if (propertyUri.startsWith(Constants.NS_SIOC) || propertyUri.startsWith(Constants.NS_KIWI_CORE) || propertyUri.startsWith(Constants.NS_SKOS)) {
					continue;
				}
				
				properties.put(property.getKiwiIdentifier(), property);
			}
		}
		
		return properties;
	}
	
	public List<KiWiProperty> getProperties() {
		List<KiWiProperty> ret = new LinkedList<KiWiProperty> ();
		if (selectedAnnotation != null) {
			
			for (KiWiProperty property : getPropertyMap().values()) {	
				ret.add(property);
			}
		}
		
		Collections.sort(ret, new Comparator<KiWiProperty> () {
			@Override
			public int compare(KiWiProperty o1, KiWiProperty o2) {
				
				if (o1.getResource().getNamespacePrefix() == null && o2.getResource().getNamespacePrefix() == null) {
					return 0;
				}
				if (o1.getResource().getNamespacePrefix() == null) {
					return -1;
				}
				if (o2.getResource().getNamespacePrefix() == null) {
					return 1;
				}
				
				if (o1.getResource().getNamespacePrefix().equals(o2.getResource().getNamespacePrefix())) {
					return o1.getResource().getLabel().compareTo(o2.getResource().getLabel());
				}
				
				return o1.getResource().getNamespacePrefix().compareTo(o2.getResource().getNamespacePrefix());
			}	
		});
		
		return ret;
	}
	
	public void setSelectedPropertyId(String selectedPropertyId) {
		this.selectedProperty = getPropertyMap().get(selectedPropertyId);
	}

	public String getSelectedPropertyId() {
		if (selectedProperty != null) {
			return selectedProperty.getKiwiIdentifier();
		}
		
		return null;
	}
	
	public KiWiProperty getSelectedProperty() {
		return selectedProperty;
	}
	
	public boolean isSelectedPropertyDatatype() {
		if (selectedProperty != null) {
			return selectedProperty.getResource().hasType(tripleStore.createUriResource(Constants.NS_OWL + "DatatypeProperty"));
		}
		
		return false;
	}

	private KiWiProperty selectedProperty = null;
	
	private List<ResourceUI> objectPropertySuggestions = new LinkedList<ResourceUI>();
	
	public List<ResourceUI> getObjectPropertySuggestions () {
		return objectPropertySuggestions;
	}
	
	public void selectProperty() {
		// TODO: prepare suggestions...
		
		objectPropertySuggestions = new LinkedList<ResourceUI>();
		// TODO: go through all annotations and suggestions and retrieve a list of relevant resources.
		
		if (selectedProperty != null) {
			
			Set<String> rangeKiwiids = new HashSet<String>();
			for (KiWiClass klass : selectedProperty.getRange()) {
				rangeKiwiids.add(klass.getKiwiIdentifier());
				
				// log.info("adding to rangekiwiids: #0", klass.getKiwiIdentifier());
			}
			
			for (SuggestionUI s : suggestions) {
			resources:
				for (ResourceUI r : s.getResources()) {
					for (ResourceUI type : r.getTypes()) {
						if (rangeKiwiids.contains(type.getKiwiid())) {
							if (!objectPropertySuggestions.contains(r)) {
								objectPropertySuggestions.add(r);
							}
							
							continue resources;
						}
					}
				}
			}
			
			for (AnnotationUI a : annotations.values()) {
			resources:
				for (ResourceUI r : a.getResources()) {
					for (ResourceUI type : r.getTypes()) {
						if (rangeKiwiids.contains(type.getKiwiid())) {
							if (!objectPropertySuggestions.contains(r)) {
								objectPropertySuggestions.add(r);
							}
								
							continue resources;
						}
					}
				}
			}
		}
	}
	
	private String propertyLiteral;
	
	public void setPropertyLiteral(String propertyLiteral) {
		this.propertyLiteral = propertyLiteral;
	}
	
	public String getPropertyLiteral() {
		return propertyLiteral;
	}
	
	public void createDatatypeProperty() {
		if (selectedAnnotation != null && selectedProperty != null) {
			DatatypePropertyUI property = new DatatypePropertyUI();
			property.setLiteral(propertyLiteral);
			property.setPredicate(new ResourceUI(selectedProperty.getKiwiIdentifier(), selectedProperty.getResource().getLabel()));
			
			selectedAnnotation.getDatatypeProperties().add(property);
		}
	}
	
	public void setPropertyObjectTitle(String propertyObjectTitle) {
		this.propertyObjectTitle = propertyObjectTitle;
	}

	public String getPropertyObjectTitle() {
		return propertyObjectTitle;
	}

	private String propertyObjectTitle;
	
	private void createObjectProperty(KiWiProperty property, ResourceUI object) {
		if (selectedAnnotation != null) {
			ObjectPropertyUI ui = new ObjectPropertyUI();
			ui.setPredicate(new ResourceUI(property.getKiwiIdentifier(), property.getResource().getLabel()));
			ui.setObject(object);
			
			selectedAnnotation.getObjectProperties().add(ui);
		}
	}
	
	public void selectPropertyObject() {
		if (selectedAnnotation != null) {
			ContentItem ci = contentItemService.getContentItemByTitle(propertyObjectTitle);
			if (ci != null) {
				ResourceUI resourceUi = new ResourceUI(ci.getKiwiIdentifier(), ci.getTitle());
				createObjectProperty(selectedProperty, resourceUi);
			}
			else {
				// Empty string to unset the resource.
				facesMessages.add("No content item with title #0 has been found. You might want to create one.", propertyObjectTitle);
			}
		}
	}
	
	public void createPropertyObject() {
		if (selectedAnnotation != null) {
			ResourceUI resourceUi = new ResourceUI(null, propertyObjectTitle);
			createObjectProperty(selectedProperty, resourceUi);
		}
	}
	
	public void acceptObjectPropertySuggestion(ResourceUI s) {
		
		if (selectedAnnotation != null) {
			createObjectProperty(selectedProperty, s);
		}
		
		objectPropertySuggestions.remove(s);
	}
	
	public void rejectObjectPropertySuggestion(ResourceUI s) {
		objectPropertySuggestions.remove(s);
	}
	
	/*public List<DatatypePropertyUI> getCurrentDatatypeProperties() {
		if (selectedAnnotation != null) {
			return selectedAnnotation.getDatatypeProperties();
		}
		
		return Collections.EMPTY_LIST;
	}*/
	
	
	// Component relations
	
	private List<KiWiProperty> listApplicableObjectProperties(ResourceUI resource) {
		return listApplicableObjectProperties(resource.getTypes());
	}
	
	private List<KiWiProperty> listApplicableProperties (Collection<ResourceUI> subjectTypes) {
List<KiWiProperty> ret = new LinkedList<KiWiProperty>();
		
		Set<String> subjectTypeKiwiids = new HashSet<String>();
		for (ResourceUI type : subjectTypes) {
			subjectTypeKiwiids.add(type.getKiwiid());
		}
		
		// TODO: this does not fully respect OWL semantics, AFAIUOS (as far as i understand OWL semantics), IANAO... 
	properties:	
		for (KiWiProperty property : getProperties()) {
			for (KiWiClass cls : property.getDomain()) {
				if (!subjectTypeKiwiids.contains(cls.getKiwiIdentifier())) {
					continue properties;
				}
			}
				
			ret.add(property);
		}
		
		return ret;
	}
	
	public List<KiWiProperty> getApplicableProperties() {
		return listApplicableProperties(selectedAnnotation.getTypes());
	}
	
	private List<KiWiProperty> listApplicableObjectProperties(Collection<ResourceUI> subjectTypes, Collection<ResourceUI> objectTypes) {
		List<KiWiProperty> ret = new LinkedList<KiWiProperty>();
		
		Set<String> subjectTypeKiwiids = new HashSet<String>();
		for (ResourceUI type : subjectTypes) {
			subjectTypeKiwiids.add(type.getKiwiid());
		}
		
		Set<String> objectTypeKiwiids = new HashSet<String>();
		for (ResourceUI type : objectTypes) {
			objectTypeKiwiids.add(type.getKiwiid());
		}
		
		// TODO: this does not fully respect OWL semantics, AFAIUOS (as far as i understand OWL semantics), IANAO... 
		KiWiUriResource owlObjectProperty = tripleStore.createUriResource(Constants.NS_OWL + "ObjectProperty");
	properties:	
		for (KiWiProperty property : getProperties()) {
			if (property.getResource().hasType(owlObjectProperty)) {
				for (KiWiClass cls : property.getDomain()) {
					if (!subjectTypeKiwiids.contains(cls.getKiwiIdentifier())) {
						continue properties;
					}
				}
								
				for (KiWiClass cls : property.getRange()) {
					if (!objectTypeKiwiids.contains(cls.getKiwiIdentifier())) {
						continue properties;
					}
				}
				
				ret.add(property);
			}
		}
		
		return ret;
	}
	
	private List<KiWiProperty> listApplicableObjectProperties(Collection<ResourceUI> subjectTypes) {
		List<KiWiProperty> ret = new LinkedList<KiWiProperty>();
		
		Set<String> subjectTypeKiwiids = new HashSet<String>();
		for (ResourceUI type : subjectTypes) {
			subjectTypeKiwiids.add(type.getKiwiid());
		}
		
		// TODO: this does not fully respect OWL semantics, AFAIUOS (as far as i understand OWL semantics), IANAO... 
		KiWiUriResource owlObjectProperty = tripleStore.createUriResource(Constants.NS_OWL + "ObjectProperty");
	properties:	
		for (KiWiProperty property : getProperties()) {
			if (property.getResource().hasType(owlObjectProperty)) {
				for (KiWiClass cls : property.getDomain()) {
					if (!subjectTypeKiwiids.contains(cls.getKiwiIdentifier())) {
						continue properties;
					}
				}
				
				ret.add(property);
			}
		}
		
		return ret;
	}
	
	List<SelectItem> possibleComponentRelations;
	
	/**
	 * Returns a nested item annotation that represents the nested item that the node is a node of directly. 
	 * If the node is some nested item div, it returns its parent nested item.
	 * @param n
	 * @return
	 */
	private AnnotationUI getNearestNestedItemAnnotation(Node n) {
		
		ParentNode pn = n.getParent();
		
		while (pn != null) {
			if (pn instanceof Element) {
				Element e = (Element)pn;
				
				String componentId = e.getAttributeValue("component", Constants.NS_KIWI_HTML);
				if (componentId != null) {
					// a component annotation is identified by its kiwiid stored in the kiwi:component attribute
					return annotations.get(componentId);
				}
			}
			
			pn = pn.getParent();
		}
		
		// the root annotation
		return annotations.get(currentContentItem.getKiwiIdentifier());
		//String nextComponentId = e.getAttributeValue("component", Constants.NS_KIWI_HTML);
	}
	
	public AnnotationUI getParentNestedItemAnnotation(AnnotationUI nestedAnnotation) {
		// currentDocument.
		assert (nestedAnnotation.getKind() == AnnotationUI.NESTED_KIND);
		
		// the root has no parent
		if (currentContentItem.getKiwiIdentifier().equals(nestedAnnotation.getId())) {
			return null;
		}
		
		XPathContext namespaces = new XPathContext();
        namespaces.addNamespace("kiwi", Constants.NS_KIWI_HTML);
        namespaces.addNamespace("html", Constants.NS_XHTML);
	
		Nodes nodes;
		
		log.info("getParentNestedItemAnnotation searcching for #0 in #1", nestedAnnotation.getId(), currentDocument.toXML());
		
		nodes = currentDocument.query("//html:div[@kiwi:component=\"" + nestedAnnotation.getId() + "\"]", namespaces);
		// TODO: there is nothing fundamentally agains supporting embedding a nesting of the same CI more than once... 
		if (nodes.size() == 1) {
			return getNearestNestedItemAnnotation(nodes.get(0));
		}
		else {
			log.info("found #0 occurences, wanted 1", nodes.size());
		}
		
		return null;
	}
	
	public List<KiWiProperty> getPossibleObjectProperties() {
		List<KiWiProperty> ret = new LinkedList<KiWiProperty>();
		if (selectedAnnotation != null) {
			
			NodePos np = KiWiXomUtils.getNode(currentDocument, 0, selectedAnnotation.getStart());
			if (np != null) {
				AnnotationUI parentNestedItem = getNearestNestedItemAnnotation (np.getNode());
				if (parentNestedItem != null) {
					for (KiWiProperty property : listApplicableObjectProperties(parentNestedItem.getTypes(), selectedAnnotation.getTypes())) {
						ret.add(property);
					}
				}
			}
		}
		
		return ret;
	}
	
	/**
	 * This method should list options for selecting a relation and type of a nested item. current annotation has to be of a nested kind.
	 * It does look for the type of the parent nested item to determine what types are possible wrt. ontology.
	 * @return
	 */
	public List<KiWiProperty> getPossibleComponentRelations() {
		
		List<KiWiProperty> ret = new LinkedList<KiWiProperty>();
		if (selectedAnnotation != null) {
			AnnotationUI parentNestedItem = getParentNestedItemAnnotation(selectedAnnotation);
			if (parentNestedItem != null) {
				for (KiWiProperty property : listApplicableObjectProperties(parentNestedItem.getTypes())) {
					ret.add(property);
				}
			}
			else {
				log.info("did not find parent nested item!");
			}
		}
		
		return ret;
		
		/*ContentItem context = getCurrentContext();
		List<SelectItem> ret = new LinkedList<SelectItem> ();
		for (KiWiProperty property : listApplicableProperties(context.getResource())) {
			// support subset of OWL semantics. Only one range type is supported.
			// let's just not specify type for any other case... 
			if (property.getRange().size() == 1) {
				KiWiClass range = property.getRange().iterator().next();
				SelectItem si = new SelectItem(property.getResource().getKiwiIdentifier() + " " + range.getResource().getKiwiIdentifier(), 
						property.getTitle() + " (" + property.getResource().getNamespacePrefix() + ")" +
						": " + 
						range.getTitle() + " (" + range.getResource().getNamespacePrefix() + ")");
				ret.add(si);
			}
			else {
				SelectItem si = new SelectItem(property.getResource().getKiwiIdentifier() + " ", 
						property.getTitle() + " (" + property.getResource().getNamespacePrefix() + ")");
				ret.add(si);
			}
		}
		
		possibleComponentRelations = ret;
		
		return possibleComponentRelations;*/
	}
	
	private KiWiProperty selectedComponentRelation;
	
	public String getSelectedComponentRelationId() {
		/*selectedComponentRelation 
		// selectedComponentRelation = null;
		if (selectedAnnotation != null) {
			// only if we have a unique predicate...
			if (selectedAnnotation.getPredicates().size() == 1) {
				selectedComponentRelation = getPropertyMap().get(selectedAnnotation.getPredicates().get(0).getKiwiid());
			}
		}*/
		
		if (selectedComponentRelation == null) {
			if (selectedAnnotation.getPredicates().size() == 1) {
				selectedComponentRelation = getPropertyMap().get(selectedAnnotation.getPredicates().get(0).getKiwiid());
			}
		}
		
		if (selectedComponentRelation != null) {
			return selectedComponentRelation.getKiwiIdentifier();
		}
		return null;
	}
	
	public void setSelectedComponentRelationId(String id) {
		selectedComponentRelation = getPropertyMap().get(id);
	}
	
	public void selectComponentRelation() {
		if (selectedAnnotation != null) {
			selectedAnnotation.getPredicates().clear();
			
			if (selectedComponentRelation != null) {
				selectedAnnotation.getPredicates().add(kiwiResource2resourceUi(selectedComponentRelation.getResource()));
			}
		}
	}
}
