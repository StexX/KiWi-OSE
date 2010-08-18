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

import gate.Document;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;
import javax.persistence.Version;

import kiwi.api.informationextraction.InstanceExtractor;
import kiwi.model.content.ContentItem;
import kiwi.model.user.User;

import org.hibernate.annotations.OptimisticLockType;

import cc.mallet.types.Alphabet;

@Entity
@org.hibernate.annotations.Entity(optimisticLock=OptimisticLockType.VERSION)
@NamedQueries({	
	@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.deleteInstanceExtractors",
			query = "delete kiwi.model.informationextraction.InstanceExtractorEntity ie"),
	@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.getInstanceExtractorByType",
				query = "select ie from kiwi.model.informationextraction.InstanceExtractorEntity ie " +
						" where ie.type = :type"),
	@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.getInstanceExtractors",
				query = "select ie from kiwi.model.informationextraction.InstanceExtractorEntity ie ")
	})
@Deprecated
public class InstanceExtractorEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
	
	private String type;
	
	@Transient
	private InstanceExtractor extractor;
	
	/**
	 * Human-readable label
	 */
	private String title;
	
	@Basic(fetch=FetchType.LAZY)
	private Alphabet malletAlphabet;
	
	@Version
	private Long version;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Alphabet getMalletAlphabet() {
		return malletAlphabet;
	}

	public void setMalletAlphabet(Alphabet malletAlphabet) {
		this.malletAlphabet = malletAlphabet;
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
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	private InstanceExtractor getExtractor() {
		if (extractor == null) {
			try {
				Class<?> cls = Class.forName(type);
				extractor = (InstanceExtractor) cls.newInstance();
				extractor.init(this);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
			
		return extractor;
	}
	
	public Collection<InstanceEntity> extractInstances(ContentItem item, Document gateDoc) {
		return getExtractor().extractInstances(this, item, gateDoc);
	}
	
	public void realizeInstance(ClassifierEntity classifier, User user, InstanceEntity instance) {
		getExtractor().realizeInstance(this, classifier, user, instance);
	}
}
