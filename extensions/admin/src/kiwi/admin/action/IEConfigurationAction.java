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

package kiwi.admin.action;

import java.io.Serializable;
import java.text.Collator;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import kiwi.api.config.ConfigurationService;
import kiwi.api.content.ContentItemService;
import kiwi.api.informationextraction.InformationExtractionService;
import kiwi.api.informationextraction.LabelService;
import kiwi.api.ontology.OntologyService;
import kiwi.api.ontology.SKOSService;
import kiwi.api.triplestore.TripleStore;
import kiwi.config.Configuration;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.informationextraction.ClassifierEntity;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.ontology.KiWiClass;
import kiwi.model.ontology.SKOSConcept;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;

/**
 * 
 * Action for changing Information Extraction configuration
 * 
 * such as path to local GATE installation. 
 * 
 * @author Marek Schmidt
 *
 */
@Name("kiwi.admin.ieConfigurationAction")
@Scope(ScopeType.PAGE)
public class IEConfigurationAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger
	private Log log;
	
	@In
	private ConfigurationService configurationService;
	
	@In(value="kiwi.informationextraction.informationExtractionService")
	private InformationExtractionService informationExtractionService;
	
	@In
	private EntityManager entityManager;
	
	@In
	private TripleStore tripleStore;
	
	@In(create=true)
	private OntologyService ontologyService;
	
	@In(create=true)
	private SKOSService skosService;
	
	@In(create=true, value="kiwi.informationextraction.labelService")
	LabelService labelService;
	
	private List<KiWiUriResource> entityTypes; 
	
	private String gatePath;
	
	private String classifierTag;
	private String extractletName;
	// private InstanceExtractorEntity classifierType;
	private boolean enabled;
	private boolean online;
	
	public boolean getEnabled() {
		return configurationService.getConfiguration("kiwi.informationextraction.informationExtractionService.enabled").getBooleanValue();
	}
	
	public boolean getOnline() {
		return configurationService.getConfiguration("kiwi.informationextraction.informationExtractionService.online").getBooleanValue();
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public void setOnline(boolean online) {
		this.online = online;
	}
	
	public String getGatePath () {
		gatePath = configurationService.getConfiguration("gate.home").getStringValue();
		return gatePath;
	}
	
	public void setGatePath (String gatePath) {
		this.gatePath = gatePath;
	}
	
	public String getClassifierTag() {
		return classifierTag;
	}

	public void setClassifierTag(String classifierTag) {
		this.classifierTag = classifierTag;
	}
	
	public String getExtractletName() {
		return extractletName;
	}
	
	public void setExtractletName(String extractletName) {
		this.extractletName = extractletName;
	}
	
	public Collection<String> getExtractletNames() {
		return informationExtractionService.getExtractletNames();
	}

	/*
	public InstanceExtractorEntity getClassifierType() {
		return classifierType;
	}

	public void setClassifierType(InstanceExtractorEntity classifierType) {
		this.classifierType = classifierType;
	}*/
	
	/*public Collection<InstanceExtractorEntity> getClassifierTypes() {
		InformationExtractionService ie = (InformationExtractionService)Component.getInstance("kiwi.informationextraction.informationExtractionService");
		return ie.getInstanceExtractors();
	}*/
	
//	@Transactional
	public String submit() {
		Configuration pathConf = configurationService.getConfiguration("gate.home"); 
		pathConf.setStringValue(gatePath);
		configurationService.setConfiguration(pathConf);
		
		Configuration enabledConf = configurationService.getConfiguration("kiwi.informationextraction.informationExtractionService.enabled"); 
		enabledConf.setBooleanValue(enabled);
		configurationService.setConfiguration(enabledConf);
		
		Configuration onlineConf = configurationService.getConfiguration("kiwi.informationextraction.informationExtractionService.online"); 
		onlineConf.setBooleanValue(online);
		configurationService.setConfiguration(onlineConf);
		
		log.info("IE configuration: changed GATE Path to #0, enabled to #1, online to #2", gatePath, enabled, online);
		
		informationExtractionService.setEnabled(enabled);
		informationExtractionService.setOnline(online);
		
		return "success";
	}
	
	public void initInstances() {
		informationExtractionService.initInstances();
	}
	
	public Collection<ClassifierEntity> getClassifiers() {
		return informationExtractionService.getClassifiers();
	}
	
	public void deleteClassifier(ClassifierEntity cls) {
		informationExtractionService.deleteClassifier(cls);
	}
	
	public void createClassifier() {		
		ContentItemService contentItemService = (ContentItemService)Component.getInstance("contentItemService");
		TripleStore tripleStore = (TripleStore)Component.getInstance("tripleStore");
		ContentItem tag = contentItemService.getContentItemByTitle(classifierTag);
		
		KiWiUriResource tagType = tripleStore.createUriResource(Constants.NS_KIWI_CORE+"Tag");
		
		if (tag == null || !tag.getResource().hasType(tagType)) {
			log.info("tag with name #0 does not exist or is not a tag", classifierTag);
			return;
		}
		
		informationExtractionService.createClassifier(tag.getResource(), extractletName);
	}
	
	public List<KiWiUriResource> getEntityTypes() {
		if(entityTypes == null) {
			entityTypes = new LinkedList<KiWiUriResource>();
			for(String entity_type : configurationService.getConfiguration("kiwi.informationextraction.entityTypes").getListValue()) {
				entityTypes.add(tripleStore.createUriResource(entity_type));
			}
		}
		return entityTypes;
	}

	public void setEntityTypes(List<KiWiUriResource> entityTypes) {
		this.entityTypes = entityTypes;
	}
	
	public void saveEntityTypes() {
		LinkedList<String> results = new LinkedList<String>();
		for(KiWiUriResource r : entityTypes) {
			results.add(r.getUri());
		}
		
		Configuration conf = configurationService.getConfiguration("kiwi.informationextraction.entityTypes");
		
		conf.setListValue(results);
		
		configurationService.setConfiguration(conf);
	}
	
	public void removeEntityType(KiWiUriResource entityType) {
		this.entityTypes.remove(entityType);
	}
	
	List<KiWiClass> availableEntityTypes = null;
	
	public List<KiWiClass> getAvailableEntityTypes() {
		if(availableEntityTypes == null) {
			availableEntityTypes = new LinkedList<KiWiClass>();
			
			availableEntityTypes.addAll(ontologyService.listClasses());
			
			Collections.sort(availableEntityTypes, new Comparator<KiWiClass>() {
				@Override
				public int compare(KiWiClass o1, KiWiClass o2) {
					return Collator.getInstance().compare(o1.getResource().getLabel(), o2.getResource().getLabel());
				}
				
			});
		}
		return availableEntityTypes;
	}
	
	KiWiClass selectedEntityType = null;
	public KiWiClass getSelectedEntityType() {
		return selectedEntityType;
	}
	
	public void setSelectedEntityType(KiWiClass entityType) {
		this.selectedEntityType = entityType;
	}
	
	public void addEntityType() {
		if(this.selectedEntityType != null && !entityTypes.contains(this.selectedEntityType.getResource())) {
			entityTypes.add((KiWiUriResource)this.selectedEntityType.getResource());
		}
	}
	
	public void init() {
		this.informationExtractionService.init();
	}	

	public static class TaxonomyConceptUI {
		private KiWiUriResource topLevelConcept;
		private KiWiUriResource concept;
		private int level;
		
		public TaxonomyConceptUI () {
			
		}
		
		public TaxonomyConceptUI (KiWiUriResource topLevelConcept, KiWiUriResource concept, int level) {
			this.topLevelConcept = topLevelConcept;
			this.concept = concept;
			this.level = level;
		}
		
		public void setTopLevelConcept(KiWiUriResource topLevelConcept) {
			this.topLevelConcept = topLevelConcept;
		}
		public KiWiUriResource getTopLevelConcept() {
			return topLevelConcept;
		}
		public void setConcept(KiWiUriResource concept) {
			this.concept = concept;
		}
		public KiWiUriResource getConcept() {
			return concept;
		}
		public void setLevel(int level) {
			this.level = level;
		}
		public int getLevel() {
			return level;
		}
		
		public String toString() {
			return concept.getUri() + " " + topLevelConcept.getUri() + " " + level;
		}
	}
	
	private List<TaxonomyConceptUI> taxonomyConcepts;

	public List<TaxonomyConceptUI> getTaxonomyConcepts() {
		if (taxonomyConcepts == null) {
			taxonomyConcepts = new LinkedList<TaxonomyConceptUI> ();
			for(String taxonomyConcept : configurationService.getConfiguration("kiwi.informationextraction.taxonomyConcepts").getListValue()) {
				String[] split = taxonomyConcept.split(" ");
				
				TaxonomyConceptUI ui = new TaxonomyConceptUI();
				ui.setConcept(tripleStore.createUriResource(split[0]));
				ui.setTopLevelConcept(tripleStore.createUriResource(split[1]));
				ui.setLevel(Integer.parseInt(split[2]));
				
				taxonomyConcepts.add(ui);
			}
		}
		return taxonomyConcepts;
	}
	
	public void saveTaxonomyConcepts() {
		LinkedList<String> results = new LinkedList<String>();
		for(TaxonomyConceptUI r : taxonomyConcepts) {
			results.add(r.getConcept() + " " + r.getTopLevelConcept() + " " + r.getLevel());
		}
		
		Configuration conf = configurationService.getConfiguration("kiwi.informationextraction.taxonomyConcepts");
		
		conf.setListValue(results);
		
		configurationService.setConfiguration(conf);
	}
	
	public void clearTaxonomyConcepts() {
		this.taxonomyConcepts = new LinkedList<TaxonomyConceptUI>();
	}
	
	private SKOSConcept topLevelConcept;
	private int conceptLevel;
	
	public List<SKOSConcept> getTopLevelConcepts() {
		return skosService.getTopConcepts();
	}

	public void setTopLevelConcept(SKOSConcept topLevelConcept) {
		this.topLevelConcept = topLevelConcept;
	}

	public SKOSConcept getTopLevelConcept() {
		return topLevelConcept;
	}
	
	public void addTopLevelConcept() {
		if (topLevelConcept != null) {
			Set<SKOSConcept> set = new HashSet<SKOSConcept>();
			Set<SKOSConcept> nextset = new HashSet<SKOSConcept>();
			set.add(topLevelConcept);
			
			for (int i = 0; i < conceptLevel; ++i) {
				for (SKOSConcept c : set) {
					nextset.addAll(c.getNarrower());
				}
				
				set = nextset;
				nextset = new HashSet<SKOSConcept>();
			}
			
			for (SKOSConcept c : set) {
				taxonomyConcepts.add(new TaxonomyConceptUI((KiWiUriResource)topLevelConcept.getResource(), (KiWiUriResource)c.getResource(), conceptLevel));
			}
		}
	}

	public void setConceptLevel(int conceptLevel) {
		this.conceptLevel = conceptLevel;
	}

	public int getConceptLevel() {
		return conceptLevel;
	}
	
	public void initLabels() {
		labelService.initLabels();
	}
	
	public long getLabelCount() {
		Query q = entityManager.createNamedQuery("kiwi.informationextraction.labelService.countLabels");
		return ((Number)q.getSingleResult()).longValue();
	}
}
