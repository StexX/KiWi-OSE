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
package kiwi.model.revision;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import kiwi.model.user.User;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

/**
 * This class represents the revision entity, which connects transactional
 * updates with [0..n] ContentItem-Versions as well as global content or 
 * metadata updates.
 * 
 * @author  Stephanie Stroka
 * 			(stephanie.stroka@salzburgresearch.at)
 *
 */
@Entity
@Immutable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@NamedQueries({
	@NamedQuery(name="revisionService.getAllRevisionByCI",
			query="select r " +
				  "from Revision r left outer join r.contentItemVersions as v " +
				  "where v.revisedContentItem = :ci " +
				  "order by r.creationDate"),
	@NamedQuery(name="revisionService.getAllRevisionByDate",
			query="select r " +
				  "from Revision r " +
				  "where r.creationDate >= :since and r.creationDate <= :until " +
				  "order by r.creationDate"),
	@NamedQuery(name="revisionService.getRevisionByCIandDate",
			query="select r " +
				  "from Revision r left outer join r.contentItemVersions as v " +
				  "where v.revisedContentItem = :ci and " +
				  "r.creationDate >= :since and r.creationDate <= :until " +
				  "order by r.creationDate")
})
public class Revision implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5665251477774627571L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Long id;
	
	@OneToMany(mappedBy="revision")
	private Set<CIVersion> contentItemVersions;
	
	@OneToOne
	private ConfigurationUpdate configurationUpdate;
	
	/**
	 * the timestamp when the revision was created
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	
	/**
	 * The user who created this revision.
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	private User user;
	
	public Revision() {
		this.creationDate = new Date();
	}
	
	/**
	 * @return the contentItemVersions
	 */
	public Set<CIVersion> getContentItemVersions() {
		return contentItemVersions;
	}

	/**
	 * @param contentItemVersions the contentItemVersions to set
	 */
	public void setContentItemVersions(Set<CIVersion> contentItemVersions) {
		this.contentItemVersions = contentItemVersions;
	}
	
	/**
	 * @param configurationUpdate the configurationUpdate to set
	 */
	public void setConfigurationUpdate(ConfigurationUpdate configurationUpdate) {
		this.configurationUpdate = configurationUpdate;
	}

	/**
	 * @return the configurationUpdate
	 */
	public ConfigurationUpdate getConfigurationUpdate() {
		return configurationUpdate;
	}

	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
}
