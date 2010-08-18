/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2008 The KiWi Project. All rights reserved.
 * http://www.kiwi-project.eu
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  KiWi designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 * 
 * Contributor(s):
 * 
 * 
 */

package kiwi.wiki.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;

import kiwi.api.ontology.OntologyService;
import kiwi.api.render.RenderingService;
import kiwi.api.triplestore.TripleStore;
import kiwi.exception.KiWiException;
import kiwi.exception.NamespaceResolvingException;
import kiwi.exception.NonUniqueRelationException;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiAnonResource;
import kiwi.model.kbase.KiWiNode;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.ontology.KiWiClass;
import kiwi.model.ontology.KiWiProperty;
import kiwi.model.user.User;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

/**
 * An action that is concerned with all aspects of handling the current content
 * item.
 * 
 * @author Szaby Gruenwald
 * 
 */
@Name("annotateAction")
@Scope(ScopeType.CONVERSATION) 
//@Transactional
public class AnnotateAction implements Serializable {

	private static final long serialVersionUID = -6274469444435208061L;

	@Logger
	private static Log log;

	@In
	FacesMessages facesMessages;

	@In(create=true)
	private ContentItem currentContentItem;

	@In(create=true)
	private User currentUser;

	// the entity manager used by this KiWi system
	@In
	private EntityManager entityManager;

	@In
	private TripleStore tripleStore;

	/**
	 * Inject the ontology service for retrieving list of classes.
	 */
	@In
	private OntologyService ontologyService;
	
	private String currentContentHtml;
	
	@In
	private RenderingService renderingPipeline;

	
	@DataModel("classes")
	List<KiWiClass> classes;
	
	@DataModel("possibleProperties")
	List<KiWiProperty> possibleProperties;
	
	@DataModel("existingProperties")
	List<KiWiProperty> existingProperties;
	
	// user selected content type to add to currentContentItem
	private KiWiClass selectedType;
	private ContentItem annotationTarget;
	private KiWiProperty selectedproperty;
	
	@Begin(join=true)
	public void begin() {
	}
	
	
	/**
	 * generates the list of owl:Class and rdfs:class elements for UI dropdown.
	 */
	public void listClasses(){
		if(classes == null) {
			long start = System.currentTimeMillis();
			classes = new LinkedList<KiWiClass>();
			// TODO when ontologyService.listClasses() will deliver anonymous 
			// nodes, we'll have to deal here with how to display them... 
			// (see KIWI-159 in Jira)
			for(KiWiClass cls : ontologyService.listClasses()) {
				if(cls.getTitle() != null) {
					classes.add(cls);
				}
				else{
					log.info("not added Type, (anonymous?) #0", cls.toString());
					if(cls instanceof KiWiAnonResource){
						log.info("anon resource #0 has anonId #1", cls.toString(), ((KiWiAnonResource) cls).getAnonId());
					}
				}
			}
			log.info("listClasses: #0 classes found (#1ms)", classes.size(), System.currentTimeMillis()-start);
		}
	}
	
	/**
	 * when the ontology changes remove the cached classes list
	 */
	@Observer("ontologyChanged")
	public void flushClasses(){
		if(classes!=null){
			log.info("flush classes");
			classes=null;
		}
	}
	
	/**
	 * generates the list of properties that are applicable for the 
	 * given currentContentItem (subject) and annotationTarget (object),
	 * for UI dropdown. That means, properties between the classes of the 
	 * resources are listed. Existing resources are left out.
	 */
	public void listPossibleProperties(){
		ContentItem subject = currentContentItem;
		ContentItem object = this.annotationTarget;
		
		log.info("listPossibleProperties ...");
		
		// This can happen when the UI loads the first time, no link is selected yet.
		if(object==null){
			log.info("listPossibleProperties: no listing, object is null.");
			return;
		}
		
		OntologyService ontologyService = (OntologyService) Component.getInstance("ontologyService");
		if(subject.getResource()==null){
			log.error("currentContentItem.getResource returnd null!");
		}
		if(object.getResource()==null){
			log.error("annotationTarget.getResource returnd null!");
		}
		if(subject.getResource()==null || object.getResource()==null){
			log.info("listPossibleProperties: no listing, subject.getResource or object.getresource is null.");
			return;
		}
		
		possibleProperties = ontologyService.listApplicableProperties(subject.getResource(), object.getResource());
		possibleProperties.removeAll(ontologyService.listExistingProperties(subject.getResource(), object.getResource()));
		
		log.info("listPossibleProperties: #0 properties found", possibleProperties.size());
	}
	
	/**
	 * generates the list of existing properties in triples with
	 * currentContentItem (subject) and annotationTarget (object),
	 * for UI dropdown. That means, properties between the two resources
	 * are listed.
	 * @throws Exception 
	 */
	public void listExistingProperties() throws Exception{
		ContentItem subject = currentContentItem;
		ContentItem object = this.annotationTarget;

		if(subject==null || object== null){
			existingProperties=new ArrayList<KiWiProperty>();
			
			log.debug("listExistingProperties: subject or object are null");
		} else {
			existingProperties = ontologyService.listExistingProperties(subject.getResource(), object.getResource());
			Iterator<KiWiProperty> it = existingProperties.iterator();
			while(it.hasNext()){
				KiWiProperty prop = it.next();
				log.info("property #0 is #1", prop.getResource().getKiwiIdentifier(), prop.getTextContent());
			}
			log.debug("listExistingProperties: #0 properties found", existingProperties.size());
		}
	}
	
	/**
	 * 
	 * @return the ContentTypes of currentContentItem for UI repeat.
	 * @throws NonUniqueRelationException 
	 * @throws KiWiException 
	 */ 
	public List<PageType> getTypes() throws NonUniqueRelationException, KiWiException{
		Collection<KiWiTriple> typeTriples;
		try {
			typeTriples = currentContentItem.getResource().listOutgoing("rdf:type");
		} catch (NamespaceResolvingException e) {
			e.printStackTrace();
			typeTriples = Collections.emptySet();
		}
		List<PageType> types = new ArrayList<PageType>();
		Iterator<KiWiTriple> it=typeTriples.iterator();
		while(it.hasNext()){
			KiWiTriple triple =it.next();
			KiWiResource type = (KiWiResource) triple.getObject();
			if (type.getLabel(null)!=null && type.getLabel(null)!=""){
				PageType pageType = new PageType(type);
				pageType.setInferred(triple.isInferred());
				pageType.setTripleId(triple.getId());
				types.add(pageType);
			}
			if(type.getContentItem()==null)
				log.debug("KiWiResource #0 has a ContentItem null. ", type.getKiwiIdentifier());
		}
		return types;
	}
	
	/**
	 * simplify the kiwiId for showing the samespace prefix on the ui.
	 * @param kiwiId
	 * @return
	 */
	public String getPrefixByKiwiId(String kiwiId){
		String uri;
		if(kiwiId.startsWith("uri::")){
			uri=kiwiId.substring(5);
			return tripleStore.getNamespacePrefix(uri);
		}else{
			return "anon";
		}
	}
	
	/**
	 * User clicked Add type button
	 */
	public void associateContentType() {
		KiWiResource subject = this.currentContentItem.getResource();
		subject.addType((KiWiUriResource)this.selectedType.getResource());
		
		facesMessages.add("Type #0 added",this.selectedType.getResource());

		Events.instance().raiseTransactionCompletionEvent("activity.annotate",currentUser,currentContentItem);
	}
	
	/**
	 * Removes the rdf:type triple for the type. 
	 * @throws NonUniqueRelationException 
	 */
	public void removeType(KiWiResource type) throws NonUniqueRelationException {
		KiWiResource subject = 
			currentContentItem.getResource();
		KiWiUriResource property = 
			tripleStore.createUriResource(Constants.NS_RDF+"type");
		KiWiNode object = type;

		if(subject==null || property==null || object== null){
			log.debug("removeType: not possible to remove triple for S:#0 P:#1 O:#2", 
					subject, property, object);
			return;
		}
		log.info("removeType: triple: #0 #1 #2", subject, property, object);
		tripleStore.removeTriple(subject, property, object);

		facesMessages.add("Type #0 removed",type);

		Events.instance().raiseTransactionCompletionEvent("activity.annotate",currentUser,currentContentItem);
	}

	/**
	 * user selected a property and clicked the Add button 
	 */
	public void setProperty(){
		KiWiResource subject = 
			currentContentItem.getResource();
		KiWiUriResource property=null;
		if(selectedproperty!=null)
			property=(KiWiUriResource)selectedproperty.getResource();
		KiWiNode object = 
			annotationTarget.getResource();

		if(subject==null || property==null || object== null){
			log.debug("setProperty: not possible to set triple for S:#0 P:#1 O:#2", 
					subject, property, object);
			return;
		}

		log.info("setProperty: triple: #0 #1 #2", subject, property, object);
		tripleStore.createTriple(subject, property, object);		

		facesMessages.add("property #0 added to link",this.selectedproperty.getResource());
		
		Events.instance().raiseTransactionCompletionEvent("activity.annotate",currentUser,currentContentItem);
	}

	/**
	 * user selected a property and clicked the Remove button 
	 * @throws NonUniqueRelationException 
	 */
	public void removeProperty() throws NonUniqueRelationException{
		KiWiResource subject = 
			currentContentItem.getResource();
		KiWiUriResource property=null;
		if(selectedproperty!=null)
			property=(KiWiUriResource)selectedproperty.getResource();
		KiWiNode object = 
			annotationTarget.getResource();
		
		if(subject==null || property==null || object== null){
			log.debug("removeProperty: not possible to remove triple for S:#0 P:#1 O:#2", 
					subject, property, object);
			return;
		}	
		
		log.info("setProperty: triple: #0 #1 #2", subject, property, object);
		tripleStore.removeTriple(subject, property, object);		

		facesMessages.add("property #0 removed from link",this.selectedproperty.getResource());
		
		Events.instance().raiseTransactionCompletionEvent("activity.annotate",currentUser,currentContentItem);
	}

	/**
	 * @return the selectedType
	 */
    @BypassInterceptors
	public KiWiClass getSelectedType() {
		return selectedType;
	}

	/**
	 * @param selectedType the selectedType to set
	 */
    @BypassInterceptors
	public void setSelectedType(KiWiClass selectedType) {
		this.selectedType = selectedType;
	}

	/**
	 * @return the list of owl and rdfs classes for selection on the UI
	 * to add as rdf:type to a ContentItem.
	 */
	public Collection<KiWiClass> getClasses() {
		listClasses();
		return classes;
	}

	/**
	 * @param linkToAnnotateUri the linkToAnnotateUri to set
	 */
	public void setAnnotationTargetId(String annotationTargetId) {
		Long linkToAnnotateId = Long.valueOf(annotationTargetId);
		annotationTarget = entityManager.find(ContentItem.class,linkToAnnotateId);
	}

	/**
	 * @return the selectedproperty
	 */
    @BypassInterceptors
	public KiWiProperty getSelectedproperty() {
		return selectedproperty;
	}

	/**
	 * @param selectedproperty the selectedproperty to set
	 */
    @BypassInterceptors
	public void setSelectedproperty(KiWiProperty selectedproperty) {
		this.selectedproperty = selectedproperty;
	}

	/**
	 * @return the possibleProperties
	 */
	public List<KiWiProperty> getPossibleProperties() {
		listPossibleProperties();
		return possibleProperties;
	}

	/**
	 * @return the annotationTarget
	 */
    @BypassInterceptors
	public ContentItem getAnnotationTarget() {
		return annotationTarget;
	}

	/**
	 * @return the existingProperties
	 * @throws NonUniqueRelationException 
	 */
	public List<KiWiProperty> getExistingProperties() throws NonUniqueRelationException {
		try {
			listExistingProperties();
		} catch (Exception e) {
			log.error("Problems occured while listing up possible properties: #0", e);
		}
		return existingProperties;
	}

	/**
	 * @param existingProperties the existingProperties to set
	 */
	public void setExistingProperties(List<KiWiProperty> existingProperties) {
		this.existingProperties = existingProperties;
	}

	public void setPossibleProperties(List<KiWiProperty> possibleProperties) {
		this.possibleProperties = possibleProperties;
	}
	
	/**
	 * @return the currentContentHtml
	 */
	public String getCurrentContentHtml() {
		if(currentContentHtml == null) {
			log.debug(currentContentItem.getTitle());
			
			if (currentContentItem.getTextContent() != null) {
				currentContentHtml = renderingPipeline.renderAnnotation(currentContentItem);
				log.info("rendering html content: #0", currentContentHtml);
			} else {
				currentContentHtml = "<p>Please add initial content</p>";
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
	
	public static class PageType {
		private KiWiResource res;
		private boolean inferred;
		private Long tripleId;
		
		/**
		 * @return the inferred
		 */
		public boolean isInferred() {
			return inferred;
		}
		/**
		 * @param inferred the inferred to set
		 */
		public void setInferred(boolean inferred) {
			this.inferred = inferred;
		}
		public PageType(KiWiResource res) {
			super();
			this.res = res;
			this.inferred = false;
		}
		/**
		 * @return the res
		 */
		public KiWiResource getRes() {
			return res;
		}
		/**
		 * @param res the res to set
		 */
		public void setRes(KiWiResource res) {
			this.res = res;
		}
		public Long getTripleId() {
			return tripleId;
		}
		public void setTripleId(Long id) {
			this.tripleId = id;
		}
		
	}

}
