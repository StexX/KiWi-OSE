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

package kiwi.wiki.action;

import static kiwi.model.kbase.KiWiQueryLanguage.SPARQL;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.persistence.Query;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.ontology.OntologyService;
import kiwi.api.triplestore.TripleStore;
import kiwi.exception.NamespaceResolvingException;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.ontology.KiWiClass;
import kiwi.model.ontology.KiWiProperty;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Synchronized;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.log.Log;

/**
 * @author Marek Schmidt
 * 
 * TODO: refactor to share code between link and component, which are mostly the same thing (internally even more so) 
 *
 */
@Name("kiwirdfaAction")
@Scope(ScopeType.CONVERSATION)
@Synchronized
public class KiwirdfaAction implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger
	Log log;
	
	@In
	private ContentItemService contentItemService;
	
	@In(create=true)
	private ContentItem currentContentItem;
	
	@In
	private OntologyService ontologyService;

	@In
	private KiWiEntityManager kiwiEntityManager;
	
	@In
	private TripleStore tripleStore;
	
	private ContentItem currentContext;
		
	private KiWiProperty property;
	
	@DataModel("possibleProperties")
	List<KiWiProperty> possibleProperties;
	
	
	private KiWiProperty componentRelation;
	private KiWiClass componentType;
	private ContentItem componentContentItem;
	
	private KiWiProperty selectedComponentRelation;
	private KiWiClass selectedComponentType;
	
	private KiWiProperty linkRelation;
	private KiWiClass linkType;
	private ContentItem linkContentItem;
	private String linkTitle;
	
	private KiWiProperty selectedLinkRelation;
	private KiWiClass selectedLinkType;
	private String selectedLinkTitle;

	private KiWiProperty iterationRelation;
	private KiWiProperty selectedIterationRelation;

	private KiWiProperty iterationItemRelation;
	private KiWiClass iterationItemType;
	private ContentItem iterationItemContentItem;
	private String iterationItemTitle;
	
	private KiWiClass selectedIterationItemType;
	
	public ContentItem getCurrentContext () {
		if (currentContext == null) {
			currentContext = currentContentItem;
		}
		return currentContext;
	}
	
	public void setCurrentContext (ContentItem currentContext) {
		this.currentContext = currentContext;
		
		log.info("set current context: #0", this.currentContext);
	}
	
	public void setCurrentContextKiwiId (String kiwiId) {
		log.info("setCurrentContextKiwiId: \"#0\"", kiwiId);
		if ("".equals(kiwiId) || kiwiId == null) {
			this.setCurrentContext(currentContentItem);
		}
		else {
			this.setCurrentContext(contentItemService.getContentItemByKiwiId(kiwiId));
		}
	}
	
	public KiWiProperty getProperty () {
		return property;
	}
	
	public String getPropertyKiwiId() {
		if (property == null) {
			log.info("getPropertyKiwiId property is null!");
			return null;
		}
		
		return property.getKiwiIdentifier();
	}
	
	public void setPropertyKiwiId(String kiwiId) {
		if (kiwiId == null || "".equals(kiwiId)) {
			property = null;
		}
		else {
			ContentItem propertyCi = contentItemService.getContentItemByKiwiId(kiwiId);
			property = kiwiEntityManager.createFacade(propertyCi, KiWiProperty.class);
		}
	}
	
	/**
	 * Sets the currently edited context and the property (from the client)
	 * @param js
	 */
	public void setPropertyJS (String js) {
		String[] split = js.split(" ", 2);
		setCurrentContextKiwiId(split[0]);
		setPropertyKiwiId(split[1]);
	}
	
	public void setProperty(KiWiProperty property) {
		this.property = property;
	}
	
	/**
	 * Creates or updates the component based on selected values in the kiwirdfaAbout form.
	 */
	public void createOrUpdateComponent() {
		
		log.info("create about");
		
		/* No distinction between creating and updating, just create a new one if none is created yet... */
		if (componentContentItem == null) {
			componentContentItem = contentItemService.createContentItem();
		}
		
		/* Update the types based on the selection user has made. We need to delete the old type. */
		/* We can update the relation in the savelet, but we need to update the type now (so proper properties of the component are immediately available)*/
		if (!selectedComponentType.equals(componentType)) {
			
			if (componentType != null) {
				try {
					for (KiWiTriple triple : componentContentItem.getResource().listOutgoing("rdf:type")) {
						if (triple.isContentBasedOutgoing() != null && triple.isContentBasedOutgoing()) {
							tripleStore.removeTriple(triple);
						}
					}
				} catch (NamespaceResolvingException e) {
					e.printStackTrace();
				}
			}
			
			componentType = selectedComponentType;
			KiWiTriple t = tripleStore.createTriple(componentContentItem.getResource(), Constants.NS_RDF + "type", componentType.getResource());
			t.setContentBasedOutgoing(true);
//			tripleStore.storeTriple(t);
//			componentContentItem.getResource().addType((KiWiUriResource)componentType.getResource());
		}
		
		/* Update the relation, we need to delete the old relation if the user selected a different one. */
//		if (!selectedComponentRelation.equals(componentRelation)) {
//			
//			if (componentRelation != null) {
//				
//				/* TODO: there has to be a better way to query for triples by subject, property and object... */
//				for (KiWiTriple triple : getCurrentContext().getResource().listOutgoing()) {
//					if (triple.getProperty().equals(componentRelation.getResource()) && triple.getObject().equals(componentContentItem.getResource())) {
//						if (triple.isContentBasedOutgoing()) {
//							tripleStore.removeTriple(triple);
//						}
//					}
//				}
//			}
//			
//			componentRelation = selectedComponentRelation;
//			
//			KiWiTriple t = tripleStore.createTriple(getCurrentContext().getResource(), ((KiWiUriResource)componentRelation.getResource()), componentContentItem.getResource());
//			t.setContentBasedOutgoing(true);
//			tripleStore.storeTriple(t, false);
//			
//			/*getCurrentContext().getResource().addOutgoingNode(
//						((KiWiUriResource)componentRelation.getResource()).getUri(),
//						componentContentItem.getResource());*/
//		}
		
		contentItemService.saveContentItem(componentContentItem);
	}
	
	private List<KiWiProperty> listPossibleProperties(ContentItem ci) {
		return ontologyService.listApplicableDataTypeProperties(ci.getResource());
		// return ontologyService.listDatatypeProperties();
	}
	
	public List<KiWiProperty> getPossibleProperties() {	
		ContentItem context = getCurrentContext();
		possibleProperties =  listPossibleProperties(context);
		
		log.info("list of possible properties for #0: #1", context, possibleProperties);
		
		return possibleProperties;
	}
	
	/* TODO: move to OntologyService */ 
	private List<KiWiProperty> listApplicableProperties(KiWiResource subject) {
		String sparqlStr = "SELECT ?S WHERE { " +
		"{ ?S <" + Constants.NS_RDF + "type> <" + Constants.NS_OWL + "ObjectProperty> . " +
		"  ?S <" + Constants.NS_RDFS + "domain> ?D . " +
		"  "+ subject.getSeRQLID()+" <" + Constants.NS_RDF + "type> ?D . " +
		"} } ";
		log.info("listApplicableProperties sparql: ' #0'", sparqlStr);
		Query query = kiwiEntityManager.createQuery(
				sparqlStr, 
				SPARQL, KiWiProperty.class);

		return query.getResultList();
	}
	
	List<SelectItem> possibleComponentRelations;
	
	public List<SelectItem> getPossibleComponentRelations() {
		ContentItem context = getCurrentContext();
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
		
		return possibleComponentRelations;
	}
	
	public String getSelectedComponentRelation() {
		StringBuilder sb = new StringBuilder();
		sb.append(selectedComponentRelation == null ? "" : selectedComponentRelation.getKiwiIdentifier());
		sb.append(" ");
		sb.append(selectedComponentType == null ? "" : selectedComponentType.getKiwiIdentifier());
		return sb.toString();
	}
	
	public void setSelectedComponentRelation(String componentRelation) {
		log.info("set selected component relation: \"#0\"", componentRelation);
		
		if (componentRelation == null) {
			selectedComponentRelation = null;
			selectedComponentType = null;
		}
		else {
			String[] split = componentRelation.split(" ", 2);
			if ("".equals(split[0])) {
				selectedComponentRelation = null;
			}
			else {
				selectedComponentRelation = kiwiEntityManager.createFacade(
					contentItemService.getContentItemByKiwiId(split[0]),
					KiWiProperty.class);
			}
			
			if ("".equals(split[1])) {
				selectedComponentType = null;
			}
			else {
				selectedComponentType = kiwiEntityManager.createFacade(
						contentItemService.getContentItemByKiwiId(split[1]),
						KiWiClass.class);
			}
			
		}	
	}
	
	/**
	 * Creates or updates the link based on selected values in the kiwirdfaLink form.
	 */
	public void createOrUpdateLink() {
		
		log.info("create link");

		/* If we have the link...  */
		if (linkContentItem != null) {
			
			/* Update the title */
			selectedLinkTitle = linkContentItem.getTitle();
			
			/* Update the relation, we need to delete the old relation if the user selected a different one. */
//			if (!selectedLinkRelation.equals(linkRelation)) {
//				if (linkRelation != null) {
//					getCurrentContext().getResource().removeOutgoingNode(
//						((KiWiUriResource)linkRelation.getResource()).getUri(),
//						linkContentItem.getResource());
//				}
//			
//				linkRelation = selectedLinkRelation;
//				getCurrentContext().getResource().addOutgoingNode(
//						((KiWiUriResource)linkRelation.getResource()).getUri(),
//						linkContentItem.getResource());
//			}
		}
		else {
			/* We don't have the link, or the link does not link to anywhere... (so this link works only as a template */
			selectedLinkTitle = "";
		}
	}
	
	/**
	 * Updates the link values based on selected link in the kiwirdfaLinkSelect form. 
	 * 
	 * The kiwirdfaLink form is made for creating and updating the "template" (relation and type), while this kiwirdfaLinkSelect 
	 * form for updating the link target.
	 */ 
	public void updateLinkSelect () {
		log.info("update link select");
		if (!linkTitle.equals(selectedLinkTitle) || (linkContentItem == null && linkTitle != null && !linkTitle.equals(""))) {
			
			linkContentItem = contentItemService.getContentItemByTitle(linkTitle);
			if (linkContentItem != null) {
				log.info("found content item #0" + linkContentItem.getTitle());
				selectedLinkTitle = linkContentItem.getTitle();
			}
			else {
				log.info("not found content item with a title #0" + linkTitle);
				selectedLinkTitle = "";
			}
		}
		else {
		}
	}
	
	List<SelectItem> possibleLinkRelations;
	
	public List<SelectItem> getPossibleLinkRelations() {
		ContentItem context = getCurrentContext();
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
		
		possibleLinkRelations = ret;
		
		return possibleLinkRelations;
	}
	
	public String getSelectedLinkRelation() {
		StringBuilder sb = new StringBuilder();
		sb.append(selectedLinkRelation == null ? "" : selectedLinkRelation.getKiwiIdentifier());
		sb.append(" ");
		sb.append(selectedLinkType == null ? "" : selectedLinkType.getKiwiIdentifier());
		return sb.toString();
	}
	
	/**
	 * A selected link relation in a human-readable form
	 * @return
	 */
	public String getSelectedLinkRelationTitle() {
		StringBuilder sb = new StringBuilder();
		sb.append(selectedLinkRelation == null ? "" : selectedLinkRelation.getTitle());
		sb.append(" ");
		sb.append(selectedLinkType == null ? "" : selectedLinkType.getTitle());
		return sb.toString();
	}
	
	public void setSelectedLinkRelation(String linkRelation) {
		log.info("set selected link relation: \"#0\"", linkRelation);
		
		if (linkRelation == null) {
			selectedLinkRelation = null;
			selectedLinkType = null;
		}
		else {
			String[] split = linkRelation.split(" ", 2);
			if ("".equals(split[0])) {
				selectedLinkRelation = null;
			}
			else {
				selectedLinkRelation = kiwiEntityManager.createFacade(
					contentItemService.getContentItemByKiwiId(split[0]),
					KiWiProperty.class);
			}
			
			if ("".equals(split[1])) {
				selectedLinkType = null;
			}
			else {
				selectedLinkType = kiwiEntityManager.createFacade(
					contentItemService.getContentItemByKiwiId(split[1]),
					KiWiClass.class);
			}
		}	
	}
	
	/**
	 * Returns the current component information for javascript
	 * @return "componentKiwiId relationKiwiId classKiwiId" 
	 */
	public String getComponentJS () {
		StringBuilder sb = new StringBuilder();
		sb.append(componentContentItem == null ? "" : componentContentItem.getKiwiIdentifier());
		sb.append(" ");
		sb.append(selectedComponentRelation == null ? "" : selectedComponentRelation.getKiwiIdentifier());
		sb.append(" ");
		sb.append(selectedComponentType == null ? "" : selectedComponentType.getKiwiIdentifier());
		
		log.info("getComponentJS returing \"#0\"", sb.toString());
		
		return sb.toString();
	}
	
	/**
	 * Returns the current link information for javascript
	 * @return "linkKiwiId relationKiwiId classKiwiId" 
	 */
	public String getLinkJS () {
		StringBuilder sb = new StringBuilder();
		sb.append(linkContentItem == null ? "" : linkContentItem.getKiwiIdentifier());
		sb.append(" ");
		sb.append(selectedLinkRelation == null ? "" : selectedLinkRelation.getKiwiIdentifier());
		sb.append(" ");
		sb.append(selectedLinkType == null ? "" : selectedLinkType.getKiwiIdentifier());
		sb.append(" ");
		sb.append(selectedLinkTitle == null ? "" : selectedLinkTitle);
		
		log.info("getLinkJS returing \"#0\"", sb.toString());
		
		return sb.toString();
	}
	
	/**
	 * Sets the currently edited component values. Updates the selected values to those new component values.
	 * @param componentJS "contextKiwiId componentKiwiId relationKiwiId classKiwiId"
	 */
	public void setComponentJS (String componentJS) {
		
		log.info("setCompomentJS \"#0\"", componentJS);
		
		String[] split = componentJS.split(" ", 4);
		if (split.length != 4) {
			log.info("setCompomentJS wrong format");
			return;
		}
		
		setCurrentContextKiwiId(split[0]);
		
		if ("".equals(split[1])) {
			componentContentItem = null;
		}
		else {
			componentContentItem = contentItemService.getContentItemByKiwiId(split[1]);
		}
		
		if ("".equals(split[2])) {
			componentRelation = null;
		}
		else {
			componentRelation = kiwiEntityManager.createFacade(
					contentItemService.getContentItemByKiwiId(split[2]),
					KiWiProperty.class);
		}
		
		if ("".equals(split[3])) {
			componentType = null;
		}
		else {
			componentType = kiwiEntityManager.createFacade(
					contentItemService.getContentItemByKiwiId(split[3]),
					KiWiClass.class);
		}
		
		selectedComponentRelation = componentRelation;
		selectedComponentType = componentType;
	}
	
	/**
	 * Sets the currently edited link values. Updates the selected values to those new link values.
	 * @param linkJS "contextKiwiId linkKiwiId relationKiwiId classKiwiId title"
	 */
	public void setLinkJS (String linkJS) {
		
		log.info("setLinkJS \"#0\"", linkJS);
		
		String[] split = linkJS.split(" ", 5);
		if (split.length != 5) {
			log.info("setLinkJS wrong format");
			return;
		}
		
		setCurrentContextKiwiId(split[0]);
		
		if ("".equals(split[1])) {
			linkContentItem = null;
		}
		else {
			linkContentItem = contentItemService.getContentItemByKiwiId(split[1]);
		}
		
		if ("".equals(split[2])) {
			linkRelation = null;
		}
		else {
			ContentItem ci = contentItemService.getContentItemByKiwiId(split[2]);
			if (ci != null) {
				linkRelation = kiwiEntityManager.createFacade(ci, KiWiProperty.class);
			}
			else {
				linkRelation = null;
			}
		}
		
		if ("".equals(split[3])) {
			linkType = null;
		}
		else {
			ContentItem ci = contentItemService.getContentItemByKiwiId(split[3]);
			if (ci != null) {
				linkType = kiwiEntityManager.createFacade(ci, KiWiClass.class);
			}
			else {
				linkType = null;
			}
		}
		
		linkTitle = split[4];
		
		selectedLinkRelation = linkRelation;
		selectedLinkType = linkType;
		selectedLinkTitle = linkTitle;
	}
	
	public String getLinkTitle () {
		return linkTitle;
	}
	
	public void setLinkTitle (String linkTitle) {
		this.linkTitle = linkTitle;
	}

	public void setIterationJS(String iterationJS) {
		log.info("setIterationJS \"#0\"", iterationJS);
		
		String[] split = iterationJS.split(" ", 2);
		if (split.length != 2) {
			log.info("setIterationJS wrong format");
			return;
		}
		
		setCurrentContextKiwiId(split[0]);
		
		if ("".equals(split[1])) {
			iterationRelation = null;
		}
		else {
			ContentItem ci = contentItemService.getContentItemByKiwiId(split[1]);
			if (ci != null) {
				iterationRelation = kiwiEntityManager.createFacade(ci, KiWiProperty.class);
			}
			else {
				iterationRelation = null;
			}
		}
		
		selectedIterationRelation = iterationRelation;
	}

	public String getIterationJS() {
		StringBuilder sb = new StringBuilder();
		sb.append(iterationRelation == null ? "" : iterationRelation.getKiwiIdentifier());
		
		log.info("getIterationJS returing \"#0\"", sb.toString());
		
		return sb.toString();
	}

	public void createOrUpdateIteration() {
		log.info("create iteration");

		iterationRelation = selectedIterationRelation;
	}

	public String getSelectedIterationRelation() {
		StringBuilder sb = new StringBuilder();
		sb.append(selectedIterationRelation == null ? "" : selectedIterationRelation.getKiwiIdentifier());
		return sb.toString();
	}
	
	public void setSelectedIterationRelation(String relation) {
		log.info("set selected iteration relation: \"#0\"", relation);
		
		if (relation == null) {
			selectedIterationRelation = null;
		}
		else {
			selectedIterationRelation = kiwiEntityManager.createFacade(
					contentItemService.getContentItemByKiwiId(relation),
					KiWiProperty.class);
		}	
	}
	

	private List<SelectItem> possibleIterationRelations;

	public List<SelectItem> getPossibleIterationRelations() {
		ContentItem context = getCurrentContext();
		List<SelectItem> ret = new LinkedList<SelectItem> ();
		for (KiWiProperty property : listApplicableProperties(context.getResource())) {
			SelectItem si = new SelectItem(property.getResource().getKiwiIdentifier(), 
				property.getTitle() + " (" + property.getResource().getNamespacePrefix() + ")");

			ret.add (si);
		}
		
		possibleIterationRelations = ret;
		
		return possibleIterationRelations;
	}


	public void setIterationItemJS(String iterationItemJS) {
		log.info("setIterationItemJS \"#0\"", iterationItemJS);
		
		String[] split = iterationItemJS.split(" ", 5);
		if (split.length != 5) {
			log.info("setIterationItemJS wrong format");
			return;
		}
		
		setCurrentContextKiwiId(split[0]);
	
		if ("".equals(split[1])) {
			iterationItemContentItem = null;
		}
		else {
			iterationItemContentItem = contentItemService.getContentItemByKiwiId(split[1]);
		}
		
		if ("".equals(split[2])) {
			iterationItemRelation = null;
		}
		else {
			iterationItemRelation = kiwiEntityManager.createFacade(
					contentItemService.getContentItemByKiwiId(split[2]),
					KiWiProperty.class);
		}
		
		if ("".equals(split[3])) {
			iterationItemType = null;
		}
		else {
			iterationItemType = kiwiEntityManager.createFacade(
					contentItemService.getContentItemByKiwiId(split[3]),
					KiWiClass.class);
		}

		iterationItemTitle = split[4];
		
		//selectedIterationItemRelation = iterationItemRelation;
		selectedIterationItemType = iterationItemType;
	}

	public String getIterationItemJS() {
		StringBuilder sb = new StringBuilder();
		sb.append(iterationItemContentItem == null ? "" : iterationItemContentItem.getKiwiIdentifier());
		sb.append(" ");
		sb.append(iterationItemRelation == null ? "" : iterationItemRelation.getKiwiIdentifier());
		sb.append(" ");
		sb.append(iterationItemType == null ? "" : iterationItemType.getKiwiIdentifier());
		sb.append(" ");
		sb.append(iterationItemTitle);
		log.info("getIterationItemJS returing \"#0\"", sb.toString());
		
		return sb.toString();

	}

	public void createOrUpdateIterationItem() {
		log.info("create iteration item");

		if (iterationItemContentItem == null) {
			iterationItemContentItem = contentItemService.createContentItem();
		}
		
		if (!selectedIterationItemType.equals(iterationItemType)) {
			if (iterationItemType != null) {
				try {
					for (KiWiTriple triple : iterationItemContentItem.getResource().listOutgoing("rdf:type")) {
						if (triple.isContentBasedOutgoing() != null && triple.isContentBasedOutgoing()) {
							tripleStore.removeTriple(triple);
						}
					}
				} catch (NamespaceResolvingException e) {
					e.printStackTrace();
				}
			}
			
			iterationItemType = selectedIterationItemType;
			KiWiTriple t = tripleStore.createTriple(iterationItemContentItem.getResource(), Constants.NS_RDF + "type", iterationItemType.getResource());
			t.setContentBasedOutgoing(true);
		}
		
		contentItemService.saveContentItem(iterationItemContentItem);
	}

	public String getSelectedIterationItemType() {
		StringBuilder sb = new StringBuilder();
		sb.append(selectedIterationItemType == null ? "" : selectedIterationItemType.getKiwiIdentifier());
		return sb.toString();
	}
	
	public void setSelectedIterationItemType(String type) {
		log.info("set selected iteration item type: \"#0\"", type);
		
		if (type == null) {
			selectedIterationItemType = null;
		}
		else {
			selectedIterationItemType = kiwiEntityManager.createFacade(
					contentItemService.getContentItemByKiwiId(type),
					KiWiClass.class);
		}	
	}
	

	private List<SelectItem> possibleIterationItemTypes;

	public List<SelectItem> getPossibleIterationItemTypes() {
		ContentItem context = getCurrentContext();
		List<SelectItem> ret = new LinkedList<SelectItem> ();

		if (iterationItemRelation == null) {
			log.info("getPossibleIterationItemTypes: no predicate to select range from!");
			return ret;
		}

		for (KiWiClass range : iterationItemRelation.getRange()) {
			SelectItem si = new SelectItem(range.getResource().getKiwiIdentifier(), 
				range.getTitle() + " (" + range.getResource().getNamespacePrefix() + ")");
			ret.add(si);
		}
		
		possibleIterationItemTypes = ret;
		
		return possibleIterationItemTypes;
	}
	
	/** 
	 * This actions can potentially modify the property, like converting the URI or something... 
	 */
	public void create () {
		log.info("create");
	}
	
	public void cancel () {
		log.info("cancel");
	}
	
	public void delete() {
		log.info("delete");
	}
	
	public void cancelComponent() {
		log.info("cancelComponent");
	}
	
	public void deleteComponent() {
		log.info("deleteComponent");
	}
	
	public void cancelLink() {
		log.info("cancelLink");
	}
	
	public void deleteLink() {
		log.info("deleteLink");
	}
	
	public void cancelLinkSelect() {
		log.info("cancel link select");
	}

	public void cancelIteration() {
		log.info("cancel iteration");
	}

	public void deleteIteration() {
		log.info("delete iteration");
	}

	public void cancelIterationItem() {
		log.info("cancel iteration item");
	}

	public void deleteIterationItem() {
		log.info("delete iteration item");
	}
}
