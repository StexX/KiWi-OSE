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
package kiwi.model.informationextraction;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;

import kiwi.model.kbase.KiWiResource;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

/**
 * 
 * Represents a suggestion for a 
 * tagged fragment, tagged item, link, datatype propery or object property.
 * 
 * This class tries to represent all kinds of annotation suggestion, so not all
 * properties are used for every suggestion. 
 * 
 * TODO: It might be better to model the specific kinds of suggestion as subclasses of some
 * general Suggestion type... 
 *   
 * @author Marek Schmidt
 *
 */
@Entity
@org.hibernate.annotations.Entity(optimisticLock=OptimisticLockType.VERSION)
@NamedQueries({	
	@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.listSuggestions",
			query = "select s " +
					"from kiwi.model.informationextraction.Suggestion s"),
	@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.listSuggestionsByClassifier",
				query = "select s " +
				"from kiwi.model.informationextraction.Suggestion s " +
				"where s.classifier = :classifier"),
	@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.listSuggestionsByClassifierAndSourceTextContent",
				query = "select s " +
				"from kiwi.model.informationextraction.Suggestion s " +
				"where s.classifier = :classifier and " +
				"      s.instance.sourceTextContent.id = :contentid"),
	@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.listSuggestionsByInstanceSourceResource",
				query = "select s " +
				"from kiwi.model.informationextraction.Suggestion s " +
				"where s.instance.id in (select i.id from kiwi.model.informationextraction.InstanceEntity i where i.sourceResource.id = :resourceid)"),
	
	@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.listUnusedSuggestionsByInstanceSourceResource",
				query = "select s " +
				"from kiwi.model.informationextraction.Suggestion s" +
				" where s.instance.id in (select i.id from kiwi.model.informationextraction.InstanceEntity i where i.sourceResource.id = :resourceid)" +
				" and 0=(select count(*) as n from kiwi.model.informationextraction.Example e where e.suggestion.id = s.id)"),
	
	@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.listSuggestionsByInstanceSourceTextContent",
				query = "select s " +
				"from kiwi.model.informationextraction.Suggestion s " +
				"where s.instance.id in (select i.id from kiwi.model.informationextraction.InstanceEntity i where i.sourceTextContent.id = :contentid)"),

	@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.listSuggestionsByClassifierSortedByScore",
				query = "select s " +
						"from kiwi.model.informationextraction.Suggestion s " +
				        "where s.classifier = :classifier and " +
				        "s.instance.sourceResource.contentItem.textContent.id = s.instance.sourceTextContent.id and " +
				        "0=(select count(*) as n from kiwi.model.informationextraction.Example e where e.suggestion.id = s.id) " +
				        "order by s.score desc"),
	@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.listSuggestionsByClassifierResourceSortedByScore",
				query = "select s " +
						"from kiwi.model.informationextraction.Suggestion s " +
					    "where s.classifier.resource = :resource and " +
					    "s.instance.sourceResource.contentItem.textContent.id = s.instance.sourceTextContent.id and " +
				        "0=(select count(*) as n from kiwi.model.informationextraction.Example e where e.suggestion.id = s.id) " +
					    "order by s.score desc"),				
	@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.listSuggestionsByResourceSortedByScore",
				query = "select s " +
						"from kiwi.model.informationextraction.Suggestion s " +
						"where s.instance.sourceResource.id = :resourceid" +
						" and s.instance.sourceResource.contentItem.textContent.id = s.instance.sourceTextContent.id" +
						" and 0=(select count(*) as n from kiwi.model.informationextraction.Example e where e.suggestion.id = s.id)" +
						" order by s.score desc"),
						
	@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.listSuggestionsByInstanceSourceTextContentSortedByScore",
				query = "select s " +
						"from kiwi.model.informationextraction.Suggestion s " +
						"where s.instance.id in (select i.id from kiwi.model.informationextraction.InstanceEntity i where i.sourceTextContent.id = :contentid)" +
						" order by s.score desc"),

	/*@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.deleteSuggestionsByClassifier",
				query = "delete from kiwi.model.informationextraction.Suggestion s " +
						"where s.classifier = :classifier"),
	@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.deleteSuggestionsByInstanceSourceItem",
				query = "delete from kiwi.model.informationextraction.Suggestion s " +
						"where s.instance.id in (select i.id from kiwi.model.informationextraction.InstanceEntity i where i.sourceContentItem.id = :itemid)"),
	*/
	@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.deleteSuggestions",
				query = "delete from kiwi.model.informationextraction.Suggestion s"),
	@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.deleteSuggestionsByClassifier",
				query = "delete from kiwi.model.informationextraction.Suggestion s " +
						"where s.classifier = :classifier")
})
public class Suggestion implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public final static int TAGGED_FRAGMENT = 1;
	public final static int TAG = 2;
	public final static int ENTITY = 3;
	public final static int NESTED_ITEM = 4;
	public final static int DATATYPE = 5;
	public static final int TYPE = 6;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
	
	/**
	 * For those suggestions that comes from a classifier. 
	 */
	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch=FetchType.LAZY)
    @Index(name="ie_suggestion_classifier")
    private ClassifierEntity classifier;
	
	/**
	 * We try to share instances wherever possible (e.g. a Date can be shared by 
	 * both `birthdate' and `death' suggestions... )
	 */
	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch=FetchType.EAGER)
	@Index(name="ie_suggestion_instance")
	private InstanceEntity instance;
	
	@Version
	private Long version;
	
	/**
	 * A score used to order suggestions of the same type during active learning.
	 */
	// @Index(name="ie_suggestion_score")
	private float score;
	
	/**
	 * One of TAGGED_FRAGMENT, TAGGED_ITEM, ...
	 */
	private int kind;
	
	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
	
	public void setKind(int kind) {
		this.kind = kind;
	}
	
	public int getKind() {
		return this.kind;
	}
	
	public ClassifierEntity getClassifier() {
		return classifier;
	}

	public void setClassifier(ClassifierEntity classifier) {
		this.classifier = classifier;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public void setInstance(InstanceEntity instance) {
		this.instance = instance;
	}

	public InstanceEntity getInstance() {
		return instance;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(Long version) {
		this.version = version;
	}

	/**
	 * @return the version
	 */
	public Long getVersion() {
		return version;
	}
	
	/**
	 * A name of the extractlet component that produced this suggestion.
	 * It is stored so that extractlet can be notified when the suggestion is accepted or rejected.
	 * This property is filled by the informationExtraction service, so
	 * extractlets don't have to worry about it...
	 */
	private String extractletName;
	
	/**
	 * A tag label for tag suggestion kinds, or some canonical title for entities, literals, etc...
	 */
	private String label;
	
	/**
	 * A tagging resource for tag suggestions. An entity resource for entity suggestion, ... 
	 * It is a list to support ambiguous suggestion (may let user choose the right one) 
	 */
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="Suggestion_Resource")
	private List<KiWiResource> resources = new LinkedList<KiWiResource>();
	
	/**
	 * Suggested types for an entity suggestion. 
	 */
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="Suggestion_Type")
	private List<KiWiResource> types = new LinkedList<KiWiResource>();

	/**
	 * An entity role. (such as, object property for object property suggestion)
	 */
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="Suggestion_Role")
	private List<KiWiResource> roles = new LinkedList<KiWiResource>();

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	
	public List<KiWiResource> getResources() {
		if (resources == null) {
			resources = new LinkedList<KiWiResource> ();
		}
		return resources;
	}

	public void setResources(List<KiWiResource> resources) {
		this.resources = resources;
	}

	
	public List<KiWiResource> getTypes() {
		if (types == null) {
			types = new LinkedList<KiWiResource> ();
		}
		return types;
	}

	public void setTypes(List<KiWiResource> types) {
		this.types = types;
	}
	
	public List<KiWiResource> getRoles() {
		if (roles == null) {
			roles = new LinkedList<KiWiResource> ();
		}
		return roles;
	}

	public void setRoles(List<KiWiResource> roles) {
		this.roles = roles;
	}
	
	public void setExtractletName(String extractletName) {
		this.extractletName = extractletName;
	}
	
	public String getExtractletName() {
		return this.extractletName;
	}
}
