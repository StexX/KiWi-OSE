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
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;
import javax.persistence.Version;

import kiwi.model.content.ContentItem;
import kiwi.model.content.TextContent;
import kiwi.model.kbase.KiWiResource;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;
import org.jboss.seam.Component;

/**
 * Represents a possible instance of some fragment suggestion or training example for
 * those models based on classification of discrete instances. 
 * 
 * @author Marek Schmidt
 *
 */
@Entity
@org.hibernate.annotations.Entity(optimisticLock=OptimisticLockType.VERSION)
@Immutable
@NamedQueries({	
		@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.deleteInstances",
					query = "delete kiwi.model.informationextraction.InstanceEntity i"),
		@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.deleteInstancesBySourceResource",
					query = "delete from kiwi.model.informationextraction.InstanceEntity i " +
							"where i.sourceResource.id = :resourceid"),
		@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.deleteUnusedInstancesBySourceResource",
					query = "delete from kiwi.model.informationextraction.InstanceEntity i " +
							" where i.sourceResource.id = :resourceid" +
							" and 0=(select count(*) as n from kiwi.model.informationextraction.Suggestion s where s.instance.id = i.id)"),
		@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.deleteInstancesBySourceTextContent",
					query = "delete from kiwi.model.informationextraction.InstanceEntity i " +
							"where i.sourceTextContent.id = :contentid"),
		@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.listInstancesBySourceResourceAndInstanceType",
					query = "select i from kiwi.model.informationextraction.InstanceEntity i " +
							"where i.sourceResource.id = :resourceid " +
							"  and i.instanceType = :type"),
		@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.listInstancesBySourceResourceAndExtractletName",
					query = "select i from kiwi.model.informationextraction.InstanceEntity i " +
							"where i.sourceResource.id = :resourceid " +
							"  and i.extractletName = :name"),
		@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.listInstancesBySourceTextContentAndExtractletName",
					query = "select i from kiwi.model.informationextraction.InstanceEntity i " +
							"where i.sourceTextContent.id = :contentid " +
							"  and i.extractletName = :name"),
		@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.listInstancesByInstanceType",
					query = "select i from kiwi.model.informationextraction.InstanceEntity i " +
						    "where  i.instanceType = :type"),
		@NamedQuery(name  = "kiwi.informationextraction.informationExtractionService.listInstancesByExtractletName",
					query = "select i from kiwi.model.informationextraction.InstanceEntity i " +
						    "where  i.extractletName = :name")
})
public class InstanceEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

	@Deprecated
	@Lob
	private cc.mallet.types.Instance malletInstance;
	
	@Transient
	private Collection<String> features;
	
	public Collection<String> getFeatures() {
		return features;
	}
	
	public void setFeatures(Collection<String> features) {
		this.features = features;
	}
	
	@Embedded
	private Context context;
	
	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch=FetchType.LAZY)
	@Index(name="ie_instanceentity_sourceresource")
    private KiWiResource sourceResource;
	
	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch=FetchType.LAZY)
	@Index(name="ie_instanceentity_sourcetextcontent")
	private TextContent sourceTextContent;
	
	/**
	 * Type identifier of the instance, each type has different features, etc...  
	 */
	@Deprecated
	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch=FetchType.LAZY)
	private InstanceExtractorEntity instanceType;
	
	@Basic
	@Index(name="ie_instanceentity_extractletname")
	private String extractletName;
	
	public String getExtractletName() {
		return extractletName;
	}
	
	public void setExtractletName(String extractletName) {
		this.extractletName = extractletName;
	}
	
	@Version
	private Long version;
	
	@Basic
	@Index(name="ie_instanceentity_contexthash")
	private Integer contextHash;
	
	public Integer getContextHash() {
		if (contextHash == null) {
			contextHash = context.hashCode();
		}
		
		return contextHash;
	}
	
	public void setContextHash(Integer contextHash) {
		this.contextHash = contextHash;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Deprecated
	public cc.mallet.types.Instance getMalletInstance() {
		return malletInstance;
	}

	@Deprecated
	public void setMalletInstance(cc.mallet.types.Instance malletInstance) {
		this.malletInstance = malletInstance;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
		this.contextHash = context.hashCode();
	}
	
	/**
	 * Whatever the extractlet wants (such as, canonical representation of a literal value for datatype extractlets...)
	 */
	private String value;
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}

	public KiWiResource getSourceResource() {
		return sourceResource;
	}

	public void setSourceResource(KiWiResource sourceResource) {
		this.sourceResource = sourceResource;
	}

	public TextContent getSourceTextContent() {
		return sourceTextContent;
	}

	public void setSourceTextContent(TextContent sourceTextContent) {
		this.sourceTextContent = sourceTextContent;
	}

	@Deprecated
	public void setInstanceType(InstanceExtractorEntity type) {
		this.instanceType = type;
	}

	@Deprecated
	public InstanceExtractorEntity getInstanceType() {
		return instanceType;
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
}
