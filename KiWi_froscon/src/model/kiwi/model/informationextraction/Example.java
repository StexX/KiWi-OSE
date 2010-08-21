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

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

import kiwi.model.user.User;

/**
 * Represents positive and negative examples used in learning models, 
 * especially for tagged fragments.
 * 
 * @author Marek Schmidt
 *
 */
@Entity
@org.hibernate.annotations.Entity(optimisticLock=OptimisticLockType.VERSION)
@Immutable
@NamedQueries({	
	@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.listExamplesByClassifier",
				query = "select e " +
						"from kiwi.model.informationextraction.Example e " +
				        "where e.suggestion.classifier = :classifier"),
	@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.listExamplesByClassifierResource",
				query = "select e " +
						"from kiwi.model.informationextraction.Example e " +
				        "where e.suggestion.classifier.resource = :resource"),
	@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.listPositiveExamplesByClassifierResource",
				query = "select e " +
						"from kiwi.model.informationextraction.Example e " +
				        "where e.suggestion.classifier.resource = :resource " +
				        " and e.type = 1"),
	@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.listNegativeExamplesByClassifierResource",
				query = "select e " +
						"from kiwi.model.informationextraction.Example e " +
				        "where e.suggestion.classifier.resource = :resource " +
				        " and e.type = 0"),
	@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.listExamplesByInstanceSourceResource",
				query = "select e " +
						"from kiwi.model.informationextraction.Example e " +
				        "where e.suggestion.instance.sourceResource.id = :resourceid"),
	@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.listPositiveExamplesByClassifier",
				query = "select e " +
						"from kiwi.model.informationextraction.Example e " +
				        "where e.suggestion.classifier = :classifier " +
				        "  and e.type = 1"),
	@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.listNegativeExamplesByClassifier",
				query = "select e " +
						"from kiwi.model.informationextraction.Example e " +
						"where e.suggestion.classifier = :classifier " +
						"  and e.type = 0"),
	@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.listExamplesByContextHashAndExtractletName",
				query = "select e " +
						"from kiwi.model.informationextraction.Example e " +
				        "where e.suggestion.instance.contextHash = :hash " +
				        "   and e.suggestion.extractletName = :name"),
	@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.deleteExamplesByClassifier",
				query = "delete kiwi.model.informationextraction.Example e " +
						"where e.id in (select x.id from kiwi.model.informationextraction.Example x where x.suggestion.classifier = :classifier)"),
	@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.deleteExamplesByInstanceSourceResource",
				query = "delete from kiwi.model.informationextraction.Example e " +
						"where e.id in (select x.id from kiwi.model.informationextraction.Example x where x.suggestion.instance.sourceResource.id = :resourceid)"),
	@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.deleteExamplesByInstanceSourceTextContent",
				query = "delete from kiwi.model.informationextraction.Example e " +
						"where e.id in (select x.id from kiwi.model.informationextraction.Example x where x.suggestion.instance.sourceTextContent.id = :contentid)"),
	@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.deleteExamples",
				query = "delete kiwi.model.informationextraction.Example e ")
})
public class Example implements Serializable {
	
	public static int POSITIVE = 1;
	public static int NEGATIVE = 0;

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
	
	@Version
	private Long version;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
	
	/**
	 * The user which created this example.
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@Index(name="ie_example_user")
    private User user;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@Index(name="ie_example_suggestion")
	private Suggestion suggestion;
	
	public void setSuggestion(Suggestion suggestion) {
		this.suggestion = suggestion;
	}
	
	public Suggestion getSuggestion() {
		return this.suggestion;
	}
	
/*
	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch=FetchType.LAZY)
    @Index(name="ie_example_classifier")
    private ClassifierEntity classifier;
	
	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch=FetchType.EAGER)
	@Index(name="ie_example_instance")
	private InstanceEntity instance;
	*/
	/*
	@Basic
	@Index(name="ie_example_extractletname")
	private String extractletName;
	
	public String getExtractletName() {
		return extractletName;
	}
	
	public void setExtractletName(String extractletName) {
		this.extractletName = extractletName;
	}*/
	
	// This is a copy of the instance context hash, for efficiency reasons it is also
	// stored directly here.
	/*@Basic
	@Index(name="ie_example_contexthash")
	private Integer contextHash;
	
	public Integer getContextHash() {
		if (contextHash == null) { 
			contextHash = instance.getContextHash();
		}
		return contextHash;
	}
	
	public void setContextHash(Integer contextHash) {
		this.contextHash = contextHash;
	}*/
	
	/**
	 * Type of the example. So far, can be POSITIVE (1) or NEGATIVE (0). 
	 */
	private int type;
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	/*public ClassifierEntity getClassifier() {
		return classifier;
	}

	public void setClassifier(ClassifierEntity classifier) {
		this.classifier = classifier;
	}*/

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	

	/*public void setInstance(InstanceEntity instance) {
		this.instance = instance;
	}

	public InstanceEntity getInstance() {
		return instance;
	}*/

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
}
