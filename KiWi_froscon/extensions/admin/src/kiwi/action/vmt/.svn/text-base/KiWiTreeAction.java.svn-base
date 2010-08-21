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
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.ontology.SKOSService;
import kiwi.api.triplestore.TripleStore;
import kiwi.exception.NamespaceResolvingException;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.ontology.SKOSConcept;
import kiwi.model.user.User;

import org.ajax4jsf.context.AjaxContext;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.richfaces.component.UITree;
import org.richfaces.component.UITreeNode;
import org.richfaces.component.html.HtmlTree;
import org.richfaces.event.NodeSelectedEvent;
import org.richfaces.model.TreeRowKey;

import bsh.org.objectweb.asm.Constants;

/**
 * 
 * @author Rolf Sint
 *
 */
@AutoCreate
@Name("kiwiTreeAction")
@Scope(ScopeType.CONVERSATION)
public class KiWiTreeAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@In(create = true)
	private SKOSService skosService;

	@Out(required = false)
	private String language;

	@In
	private ContentItemService contentItemService;

	@In(required = false)
	private ConceptInformation conceptInformation;

	private String preferredLabel;

	private UITree destTree;

	private String selectedAltLabel;

	@In(required = false)
	@Out(required = false, scope = ScopeType.EVENT)
	private List<KiWiTreeNode> srcRoots;

	@org.jboss.seam.annotations.Logger
	private org.jboss.seam.log.Log log;

	@Out(required = false)
	private KiWiTreeNode actuKiWiTreeNode;
	
	private KiWiTreeNode broaderKiWiTreeNode;

	@In
	private FreeTagMapper freeTagMapper;

	@In
	private KiWiEntityManager kiwiEntityManager;

	@In(create = true)
	private User currentUser;

	private org.richfaces.event.DropEvent ev;

	private String altLabel;

	@In(required = false)
	private RelationBean relationBean;
	
	@In
	private TripleStore tripleStore;

	// ------------------------------------

	public String getAltLabel() {
		return altLabel;
	}

	public void setAltLabel(String altLabel) {
		this.altLabel = altLabel;
	}

	public ConceptInformation getConceptInformation() {
		return conceptInformation;
	}

	public void setConceptInformation(ConceptInformation conceptInformation) {
		this.conceptInformation = conceptInformation;
	}

	public synchronized List<KiWiTreeNode> getSourceRoots() {

		if (srcRoots == null) {

			if (language == null) {
				language = "en";
			}

			KiWiTreeNode c = new KiWiTreeNode(null, skosService, language);
			srcRoots = c.getRootNodes();
		}

		return srcRoots;
	}

	public void onSelect(NodeSelectedEvent event) {
		log.debug("item selected ...");
		initCurrentTreeNode(event);

		conceptInformation.setPreferedLabel(actuKiWiTreeNode.toString());
		User user = actuKiWiTreeNode.getActualConcept().getAuthor();
		
		if ((user != null) && (user.getLogin()) != null) {
			conceptInformation.setAuthor(user.getLogin());
		}
		String definition = actuKiWiTreeNode.getActualConcept().getDefinition();
		if (definition != null) {
			conceptInformation.setDefinition(actuKiWiTreeNode.getActualConcept().getDefinition());
		}
	}

	// called by onSelect(NodeSelectedEvent event), regards to the selected
	// concept. sets the global variables actuKiWiTreeNode and
	// broaderKiWiTreeNode
	private void initCurrentTreeNode(NodeSelectedEvent event) {
		destTree = (HtmlTree) event.getComponent();
		TreeRowKey x = (TreeRowKey) destTree.getRowKey();

		actuKiWiTreeNode = (KiWiTreeNode) destTree.getRowData(x);
		TreeRowKey y = x.getParentKey();

		try {
			broaderKiWiTreeNode = (KiWiTreeNode) destTree.getRowData(y);
			log.debug("SelectedConcept: "
					+ actuKiWiTreeNode.getActualConcept().getPreferredLabel()
					+ "BroaderConcept"
					+ broaderKiWiTreeNode.getActualConcept()
							.getPreferredLabel());
		} catch (IllegalStateException e) {
			log.warn("No broader element available, this is most likely because this element is a root element");
		}

		log.debug("SelectedConcept: "+ actuKiWiTreeNode.getActualConcept().getPreferredLabel());
	}

	// is called when a concept or a tag is dropped into the thesaurus tree
	public void dropListener(org.richfaces.event.DropEvent ev) {
		log.debug("Event set");

		// init derstTree
		UITreeNode destNode = (UITreeNode) ev.getSource();
		destTree = destNode.getUITree();

		TreeRowKey dropKey = (TreeRowKey) ev.getDropValue();

		// init actual node
		actuKiWiTreeNode = (KiWiTreeNode) destTree.getRowData(dropKey);


		this.ev = ev;
	}
	
	// called e.g. from enterpreflabel.xhtml
	public void addNewConcept() {
		log.debug("Adding new Concept ... called from button menu");
		// Create the new content item
		ContentItem ci = contentItemService.createContentItem();
		contentItemService.updateTitle(ci, preferredLabel);
		addConcept(ci);
	}
	// called from enterRelationship.xhtml
	public void addDragConcept() {
		refreshTree(destTree);
		// in the case the dragvalue is a content item from the type
		// tag (contentItem) and not a dragged value from the tree
		// (KiWiTreeNode).
		// this method is accessed when you drag a "free" tag into the
		// thesaurus
		if (ev.getDragValue() instanceof ContentItem) {

			ContentItem gcontentItemTag = (ContentItem) ev.getDragValue();
			KiWiUriResource kuri = tripleStore.createUriResource(kiwi.model.Constants.NS_KIWI_CORE+"FreeTag");			
			gcontentItemTag.getResource().removeType(kuri);
			
			
			addConcept(gcontentItemTag);
			// for changes of concepts within the thesaurus tree
		} else {
			TreeRowKey dragKey = (TreeRowKey) ev.getDragValue();
			KiWiTreeNode dragNode = (KiWiTreeNode) destTree.getRowData(dragKey);
			TreeRowKey parentKey = dragKey.getParentKey();

			// no parent node exists
			if (parentKey.toString().equals("")) {
				FacesMessages.instance().add("Operation not allowed");
			} else if (dragNode == actuKiWiTreeNode) {
				log.info("Target Item is Destination Item");
				FacesMessages.instance().add("Target Item is Destination Item");
				// dragged element is a root element
			} else {
				
				if(relationBean.getSelectedRelationship() == SkosRelTypes.NARROWER){
								
					//parentNode from the dragged concept
					KiWiTreeNode parentNode = (KiWiTreeNode) destTree.getRowData(parentKey);
					
					log.info("Dragnode" + dragNode);
					log.info("Dropnode" + actuKiWiTreeNode);

					SKOSConcept dropConcept = actuKiWiTreeNode.getActualConcept();
					SKOSConcept dragConcept = dragNode.getActualConcept();
					
					setNarrower(dropConcept, dragConcept);
					removeConcept(parentNode.getActualConcept(),dragConcept);
					
					//make the same add and delete in tree
					parentNode.delete(dragNode);
					actuKiWiTreeNode.addChild(dragConcept);
					
					kiwiEntityManager.persist(parentNode.getActualConcept());
	
					FacesMessages.instance().add("Moved "+ dragNode.getActualConcept().getPreferredLabel()+ " to "+ actuKiWiTreeNode.getActualConcept().getPreferredLabel());
				}
				else if(relationBean.getSelectedRelationship() == SkosRelTypes.MERGE){
					
					KiWiTreeNode parentNode = (KiWiTreeNode) destTree.getRowData(parentKey);
					
					SKOSConcept dropConcept = actuKiWiTreeNode.getActualConcept();
					SKOSConcept dragConcept = dragNode.getActualConcept();

//					Collection<KiWiTriple> ck = dragConcept.getResource().listOutgoing();

//					try{
//						for(KiWiTriple kt : ck){
//							dropConcept.getResource().addOutgoingNode(kt.getProperty(), kt.getObject());
//						}
//					}
//					catch(Exception e){
//							e.printStackTrace();
//					}
					
					
					parentNode.delete(dragNode);
					
					removeConcept(parentNode.getActualConcept(),dragConcept);
					

					//Date actDate = new Date();
					//dragConcept.setModified(actDate);
					//dropConcept.setModified(actDate);
						//dragConcept.getDelegate().getResource().setProperty(kiwi.model.Constants.NS_DC_TERMS+"isReplacedBy", ((KiWiUriResource) dropConcept.getResource()).getUri());
						
//						dropConcept.getDelegate().getResource().setProperty(kiwi.model.Constants.NS_DC_TERMS+"modified", actDate.toString());
//						dragConcept.getDelegate().getResource().setProperty(kiwi.model.Constants.NS_DC_TERMS+"modified", actDate.toString());
						

					
					kiwiEntityManager.replace(dragConcept.getDelegate(), dropConcept.getDelegate());
					
					kiwiEntityManager.persist(parentNode.getActualConcept());
					kiwiEntityManager.persist(dropConcept);

					
					FacesMessages.instance().add("Concept "+dragConcept.getTitle()+" merged with "+dropConcept.getTitle());
					
					refreshTree(destTree);
				}
			}
			log.debug("Drag and Drop proceeded!");
		}
	}

	private void refreshTree(UITree uit) {
	    if(uit != null){
		AjaxContext ac = AjaxContext.getCurrentInstance();
		ac.addComponentToAjaxRender(uit);
	    }
	}

	public void addAltLabel() {
		log.info("adding alternative label ...");
		HashSet<String> altLabels = actuKiWiTreeNode.getActualConcept().getAlternativeLabels();
		altLabels.add(altLabel.trim());
		actuKiWiTreeNode.getActualConcept().setAlternativeLabels(altLabels);

		kiwiEntityManager.persist(actuKiWiTreeNode.getActualConcept());
//		kiwiEntityManager.flush();
	}

	private void addConcept(ContentItem newContentItem) {
		log.debug(newContentItem.getTitle());

		kiwiEntityManager.persist(newContentItem);

		// get the language
		Locale lang = new Locale(relationBean.getSelectedLanguage());

		if (lang == null)
			throw new LanguageNotDefinedExeption();

		switch (relationBean.getSelectedRelationship()) {
		case NARROWER: {
			// convert the free tag into a SKOS Concept
			SKOSConcept newSkosConcept = kiwiEntityManager.createFacade(
					newContentItem, SKOSConcept.class);
			contentItemService.updateTitle(newSkosConcept, newContentItem.getTitle());
			KiWiResource kr = newSkosConcept.getResource();

			if (lang != null) {
				try {
					kr.setProperty("http://www.w3.org/2004/02/skos/core#prefLabel",
							newContentItem.getTitle(), lang);
				} catch (NamespaceResolvingException e) {
					e.printStackTrace();
				}
			} else {
				newSkosConcept.setPreferredLabel(newContentItem.getTitle());
			}
			newSkosConcept = initSkosConcept(newSkosConcept);

			setNarrower(actuKiWiTreeNode.getActualConcept(), newSkosConcept);
						

			// remove the dragged Node from the list
			freeTagMapper.removeContentItem(newContentItem);

			// add the dropped node
			actuKiWiTreeNode.addChild(newSkosConcept);

			break;
		}
		case ALTERNATIVE_LABEL: {
			HashSet<String> altLabels = actuKiWiTreeNode.getActualConcept().getAlternativeLabels();
			altLabels.add(newContentItem.getTitle());
			actuKiWiTreeNode.getActualConcept().setAlternativeLabels(altLabels);
			// remove the dragged Node from the list
			freeTagMapper.removeContentItem(newContentItem);
			// TODO: alternative labels can also have a language
			break;
		}
		case PREFLABEL: {
			KiWiResource kr = actuKiWiTreeNode.getActualConcept().getResource();
			String g = kr.getProperty(
					"http://www.w3.org/2004/02/skos/core#prefLabel", lang);
			if (g == null) {
				try {
					kr.setProperty("http://www.w3.org/2004/02/skos/core#prefLabel",
							newContentItem.getTitle(), lang);
				} catch (NamespaceResolvingException e) {
					e.printStackTrace();
				}
				freeTagMapper.removeContentItem(newContentItem);
			} else {
				FacesMessages.instance().add(
						"Label in language " + lang + " already exists");
			}
			break;
		}
		}
	}
	
	//remove narrower from parent, does not remove the whole concept, kiwientitymanger.remove must be called explicitly
	private void removeConcept(SKOSConcept parentConcept, SKOSConcept childConcept){
		HashSet hs2 = parentConcept.getNarrower();
		hs2.remove(childConcept);
		parentConcept.setNarrower(hs2);
	}
	
	
	private void setNarrower(SKOSConcept parentConcept, SKOSConcept childConcept){	
		
		// set broader
		childConcept.setBroader(actuKiWiTreeNode.getActualConcept());
		
		// set narrower
		HashSet hs1 = parentConcept.getNarrower();
		hs1.add(childConcept);
		parentConcept.setNarrower(hs1);
		
		// persist
		kiwiEntityManager.persist(parentConcept);
		kiwiEntityManager.persist(childConcept);
	}

	public void deleteAltLabel() {
		log.info("deleting alternative label ...");
		HashSet<String> altLabels = actuKiWiTreeNode.getActualConcept().getAlternativeLabels();
		altLabels.remove(selectedAltLabel.trim());
		actuKiWiTreeNode.getActualConcept().setAlternativeLabels(altLabels);

		kiwiEntityManager.persist(actuKiWiTreeNode.getActualConcept());
//		kiwiEntityManager.flush();
	}

	public void deleteSelectedConcept() {

		refreshTree(destTree);
		
		if(broaderKiWiTreeNode != null && broaderKiWiTreeNode.getActualConcept() != null && broaderKiWiTreeNode.getActualConcept().getNarrower() != null){
		
        		HashSet<SKOSConcept> sl = broaderKiWiTreeNode.getActualConcept()
        				.getNarrower();
        		sl.remove(actuKiWiTreeNode.getActualConcept());
        		broaderKiWiTreeNode.getActualConcept().setNarrower(sl);
        
        		broaderKiWiTreeNode.delete(actuKiWiTreeNode);
        		
        		kiwiEntityManager.remove(actuKiWiTreeNode.getActualConcept().getDelegate());
        		
        		FacesMessages.instance().add(
        				"Concept " + actuKiWiTreeNode + " deleted !");
		}
		else{
		    FacesMessages.instance().add("Error initializing Concept, please try again ...");
		}
	}

	// sets the default values
	private SKOSConcept initSkosConcept(SKOSConcept skosConcept) {
		skosConcept.setAuthor(currentUser);
		skosConcept.setDefinition("-");
		return skosConcept;
	}

	public void defineRelated() {
		log.info("Define as related ...");
	}

	public KiWiTreeNode getActuKiWiTreeNode() {
		return actuKiWiTreeNode;
	}

	public void setActuKiWiTreeNode(KiWiTreeNode actuKiWiTreeNode) {
		this.actuKiWiTreeNode = actuKiWiTreeNode;
	}

	public String getPreferredLabel() {
		return preferredLabel;
	}

	public void setPreferredLabel(String preferredLabel) {
		this.preferredLabel = preferredLabel;
	}

	public String getLanguage() {
		return language;
	}

	public String getSelectedAltLabel() {
		return selectedAltLabel;
	}

	public void setSelectedAltLabel(String selectedAltLabel) {
		this.selectedAltLabel = selectedAltLabel;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public List<String> getLabels() {
		List<String> labs = new LinkedList<String>();
		labs.add("en");
		labs.add("de");
		labs.add("dk");
		labs.add("fr");
		labs.add("ja");
		labs.add("jw");
		labs.add("ko");
		return labs;
	}
}
