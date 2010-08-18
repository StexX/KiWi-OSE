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
package kiwi.model.content;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.OptimisticLockType;

import kiwi.model.revision.Revision;

/**
 * Content is an abstract superclass for all kind of content,
 * e.g. TextContent and MediaContent
 *
 *
 * @author Sebastian Schaffert
 */
@AccessType("property")
@MappedSuperclass
public abstract class Content implements Serializable {

	private static final long serialVersionUID = 8076173648970074477L;


	private Long id;
	
    
    private Date created;
    
    private Long version;
    
//    @ManyToOne(fetch=FetchType.LAZY)    
//    private User author;

	private Set<Revision> revision;
    
    // default constructor for persistence
    public Content() {
        this.created = new Date();
    }
        
    
    /**
     * Get the creation time of this content as a Java Date object.
     */  
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreated() {
        return created;
    }

    /**
     * Set the creation time of this content as a Java Date object.
     * @param creationTime
     */
    public void setCreated(Date creationTime) {
        this.created = creationTime;
    }

//    /**
//     * Return the author of this content; usually a single user, but could also be a group.
//     * @return
//     */
//    public User getAuthor() {
//        return author;
//    }
//
//    /**
//     * Set the author of this content; usually a single user, but could also be a group.
//     * @param author
//     */
//    public Content setAuthor(User author) {
//        this.author = author;
//        return this;
//    }
//    
//    /**
//     * Returns the content item that includes this content
//     * @return
//     */
//    public abstract ContentItem getContentItem();
//
//    /**
//     * sets the content item that includes this content
//     * @param contentItem
//     */
//    public abstract void setContentItem(ContentItem contentItem);
    

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Content other = (Content) obj;
        if (this.getId() != other.getId() && (this.getId() == null || !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
        return hash;
    }


	

	/**
     * Sets an ID for the content object that is unique across the system and can be used to identify the content
     * object in error messages. A good way would be to use the resource of the content item together with the language of the
     * object, and possibly the revision id.
     * @param id
     */
	public void setId(Long id) {
		this.id = id;
	}

	/**
     * Return an ID for the content object that is unique across the system and can be used to identify the content
     * object in error messages. A good way would be to use the resource of the content item together with the language of the
     * object, and possibly the revision id.
     * 
     * @return
     */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return id;
	}	

	/**
	 * @return the revision
	 */
	@OneToMany
	public Set<Revision> getRevision() {
		return revision;
	}

	/**
	 * @param revision the revision to set
	 */
	public void setRevision(Set<Revision> revision) {
		this.revision = revision;
	}

    /**
	 * @return the version
	 */
    @Version
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
