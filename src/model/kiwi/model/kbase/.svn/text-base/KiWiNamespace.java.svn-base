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
package kiwi.model.kbase;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

/**
 * The KiWiNamespace is used to store namespaces from RDF 
 * documents and link them with their prefix
 *
 * @author Sebastian Schaffert
 */
@Entity
@org.hibernate.annotations.Entity(mutable=false,optimisticLock=OptimisticLockType.VERSION)
@BatchSize(size=20)
@Immutable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@NamedQueries({
	@NamedQuery(name  = "tripleStore.deleteNamespace",
			query = "update KiWiNamespace ns " +
			        "set ns.deleted = true "+
					"where ns.id = :id"),
	@NamedQuery(name  = "tripleStore.undeleteNamespace",
			query = "update KiWiNamespace ns " +
			        "set ns.deleted = false "+
					"where ns.id = :id"),
	@NamedQuery(name  = "tripleStore.listNamespaces",
		        query = "from KiWiNamespace ns " +
		        		"where ns.deleted = false"),
	@NamedQuery(name  = "tripleStore.namespaceByPrefix",
			    query = "from KiWiNamespace ns where ns.prefix = :prefix " +
        				"and ns.deleted = false"),
	@NamedQuery(name  = "tripleStore.namespaceByUri",
				query = "from KiWiNamespace ns where ns.uri = :uri " +
        				"and ns.deleted = false"),
	@NamedQuery(name  = "tripleStore.namespaceByUriPrefix",
				query = "from KiWiNamespace ns where :uri like (ns.uri || '%') " +
        				"and ns.deleted = false")
})
public class KiWiNamespace {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Long id;
	
//	@Column(length=20,unique=true,nullable=false)
	@Column(length=20,nullable=false)
	@Index(name="idx_ns_prefix")
    private String prefix;
    
//	@Column(unique=true,nullable=false)
	@Column(nullable=false)
	@Index(name="idx_ns_uri")
    private String uri;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modified;
    
    @Version
    private Long version;

	private Boolean deleted;
    
    public KiWiNamespace() {
        this.created = new Date();
    }
    
    public KiWiNamespace(String prefix, String uri) {
    	this.deleted = false;
        this.prefix = prefix;
        this.uri = uri;
        this.created = new Date();
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @param created the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @return the modified
	 */
	public Date getModified() {
		return modified;
	}

	/**
	 * @param modified the modified to set
	 */
	public void setModified(Date modified) {
		this.modified = modified;
	}	

    /**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 * @return the deleted
	 */
	public Boolean getDeleted() {
		return deleted;
	}

	/**
	 * @return the version
	 */
	public Long getVersion() {
		return version;
	}
	
	/**
	 * @param version the version to set
	 */
	public void setVersion(Long version) {
		this.version = version;
	}
}
