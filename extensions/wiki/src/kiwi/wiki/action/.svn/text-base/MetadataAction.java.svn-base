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

import java.io.Serializable;
import java.text.Collator;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import kiwi.api.event.KiWiEvents;
import kiwi.api.ontology.OntologyService;
import kiwi.api.triplestore.TripleStore;
import kiwi.exception.NamespaceResolvingException;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiLiteral;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.ontology.KiWiProperty;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

@AutoCreate
@Name("metadataAction")
@Scope(ScopeType.CONVERSATION)
//@Transactional
public class MetadataAction implements Serializable {
	
	@DataModel
	protected List<UIMetadata> metadataSet;
	
	@In(create=true)
	ContentItem currentContentItem;
	
	@In
	Locale locale;
	
	@Logger
	static Log log;

	@In
	TripleStore tripleStore;
	
	@In
	FacesMessages facesMessages;
	
	@DataModelSelection(value="metadataSet")
//	@Out(required=false)
	protected UIMetadata selectedMetadata;
	
	@DataModel("possibleProperties")
	List<KiWiProperty> possibleProperties;

	private KiWiProperty selectedproperty;
	
	/**
	 * initializes the metadataSet variable from the tripleStore.
	 */
	@Begin(join=true)
	public void loadMetadata(){
		log.info("init metadataSet...");
		LinkedList<UIMetadata> res = new LinkedList<UIMetadata>();
		
		List<String> disabledProperties = Arrays.asList(new String[] {
				Constants.NS_KIWI_CORE+"hasTextContent",
				Constants.NS_KIWI_CORE+"id"
		});
		
		for(KiWiTriple triple : currentContentItem.getResource().listOutgoing()) {
			if(triple.getObject() instanceof KiWiLiteral && 
			   !disabledProperties.contains(triple.getProperty().getUri())){
				res.add(new UIMetadata(triple));
			}
		}
		
		Collections.sort(res, new Comparator<UIMetadata>() {

			@Override
			public int compare(UIMetadata o1, UIMetadata o2) {
				return Collator.getInstance().compare(o1.getName(), o2.getName());
			}
			
		});
		
		metadataSet = res;
	}
	
	/**
	 * generates the list of properties that are applicable for the 
	 * given currentContentItem (subject) and annotationTarget (object),
	 * for UI dropdown. That means, properties between the classes of the 
	 * resources are listed. Existing resources are left out.
	 */
	public void listPossibleProperties() {
		ContentItem subject = currentContentItem;
		OntologyService ontologyService = (OntologyService) Component.getInstance("ontologyService");
		if(subject.getResource()==null){
			log.error("currentContentItem.getResource returnd null!");
			return;
		}
		
		possibleProperties = ontologyService.listApplicableDataTypeProperties(subject.getResource());
		log.info("listPossibleProperties: #0 properties found", possibleProperties.size());
	}

	public void removeProperty(UIMetadata md){
		// log.info("remove property '#0'", selectedMetadata.get)
		// TODO implement
		log.info("removing metadata #0", md.getName());
		tripleStore.removeTriple(md.getTriple());
		facesMessages.add("Property #0 removed.", md.getName());
	}
	
	public void createProperty(){
		// TODO implement
		KiWiResource subject = currentContentItem.getResource();
		KiWiUriResource property;
		if(selectedproperty == null){
			facesMessages.add("You have to select a property to add first.");
			return;
		}
		if(selectedproperty.getResource().isUriResource()){
			property = (KiWiUriResource)selectedproperty.getResource();
		}else{
			facesMessages.add("A problem occurred adding the property #0.",
					selectedproperty.getTitle());
			return;
		}
		KiWiLiteral object = tripleStore.createLiteral("");
		tripleStore.createTriple(subject, property, object);
		facesMessages.add("Property #0 created.", selectedproperty.getTitle());
	}
	
	
	public static final class UIMetadata {
		
		private String name, value, language, type;
		private KiWiTriple triple;
		private KiWiLiteral lit;
		
		public UIMetadata(KiWiTriple triple){
			this.setTriple(triple);
			lit = (KiWiLiteral)triple.getObject();
			this.extractTriple();
		}
		
		private void extractTriple(){
			log.info("loading a property #0", triple);
			this.name=triple.getProperty().getLabel();
			this.value=this.getValueFromTriple();
			this.language=this.getLanguageFromTriple();
		}
		
		private String getValueFromTriple(){
			return lit.getContent();
		}
		private String getLanguageFromTriple(){
			String res="";
			//set language
			if(lit.getLanguage()==null){
				res=("any");
			} else {
				res=(lit.getLanguage().getLanguage());
			}
			return res;
		}

		private String getTypeFromTriple(){
			String res="";
			if(lit.getType()==null){
				log.error("Literal #0 doesn't have a type (is null!)", lit.getContent());
			}
			res=lit.getType().getLabel();
			return res;
		}

		private boolean changed(){
			boolean res = 
					!this.name.equals(triple.getProperty().getLabel()) ||
					!this.value.equals(this.getValueFromTriple()) ||
					!this.type.equals(this.getTypeFromTriple()) ||
					!this.language.equals(this.getLanguageFromTriple());
			if(log.isDebugEnabled()) {
				if(this.name!=triple.getProperty().getLabel())
					log.debug("name changed: #0 -> #1", triple.getProperty().getLabel(), this.name);
				if(this.value!=this.getValueFromTriple())
					log.debug("value changed: #0 -> #1", this.getValueFromTriple(), this.value);
				if(this.type!=this.getTypeFromTriple())
					log.debug("type changed: #1 -> #0", this.type, this.getTypeFromTriple());
				if(this.language!=this.getLanguageFromTriple())
					log.debug("language changed #0 -> #1", this.getLanguageFromTriple(), this.language);
			}
			return res;
		}
		
		protected void save(){
			if(this.changed()){
				log.info("saving property #0", this.name);
				if("any".equals(getLanguage())){
					try {
						triple.getSubject().setProperty("<"+triple.getProperty().getUri()+">", getValue());
					} catch (NamespaceResolvingException e) {
						e.printStackTrace();
					}
				} else {
					try {
						triple.getSubject().setProperty("<"+triple.getProperty().getUri()+">", 
								getValue(),
								new Locale(this.getLanguage()));
					} catch (NamespaceResolvingException e) {
						e.printStackTrace();
					}
				}
				
				// necessary because triple has changed when using setProperty to update ...
				//((MetadataAction)Component.getInstance("metadataAction")).loadMetadata();
			
				
				Events.instance().raiseEvent(KiWiEvents.METADATA_UPDATED, triple.getSubject().getContentItem());
			} else {
				log.debug("not saving property #0 as it has not changed", this.name);
			}
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			log.info("setting value to #0, was #1", value, this.value);
			if(!value.equals(this.value)) {
				this.value = value;
				this.save();
			}
		}

		public String getLanguage() {
			return language;
		}

		public void setLanguage(String language) {
			if(!language.equals(this.language)) {
				this.language = language;
				this.save();
			}
		}

		public KiWiTriple getTriple() {
			return triple;
		}

		public void setTriple(KiWiTriple triple) {
			this.triple = triple;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			if(!type.equals(this.type)) {
				this.type = type;
				this.save();
			}
		}
	}

	public List<UIMetadata> getMetadataSet() {
		return metadataSet;
	}

	public void setMetadataSet(List<UIMetadata> metadataSet) {
		this.metadataSet = metadataSet;
	}

	public UIMetadata getSelectedMetadata() {
		return selectedMetadata;
	}

	public void setSelectedMetadata(UIMetadata selectedMetadata) {
		this.selectedMetadata = selectedMetadata;
	}

	public ContentItem getCurrentContentItem() {
		return currentContentItem;
	}

	public KiWiProperty getSelectedproperty() {
		return selectedproperty;
	}

	public void setSelectedproperty(KiWiProperty selectedproperty) {
		this.selectedproperty = selectedproperty;
	}

	public List<KiWiProperty> getPossibleProperties() {
		listPossibleProperties();
		return possibleProperties;
	}

	public void setPossibleProperties(List<KiWiProperty> possibleProperties) {
		this.possibleProperties = possibleProperties;
	}
}
