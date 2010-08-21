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
package kiwi.model.user;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import kiwi.model.Constants;
import kiwi.model.annotations.RDF;
import kiwi.model.annotations.RDFType;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.recommendation.ContactRecommendation;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.validator.NotNull;
import org.jboss.seam.annotations.security.management.RoleName;

/**
 * This entity represents the groups that users may belong to.
 * It is possible to assign roles to groups, which results in 
 * assigning a role to every user who is in this specific group.
 * Groups will be created by a user, who then serves as the owner 
 * of the group (unless it's reassigned by him/her).
 * 
 * @author  Stephanie Stroka 
 * 			(stephanie.stroka@salzburgresearch.at)
 *
 */
@Entity
@org.hibernate.annotations.Entity(optimisticLock=OptimisticLockType.VERSION)
@Table(name = "KiWiGroup")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@RDFType(Constants.NS_KIWI_CORE + "Group")
@NamedQueries({
	@NamedQuery(name="groupService.getAllGroups",
			query="select g from kiwi.model.user.Group g"),
	@NamedQuery(name="groupService.getGroupByName",
			query="select g from kiwi.model.user.Group g " +
					"where g.name = :name"),
	@NamedQuery(name="groupService.getGroupsByOwner",
			query="select g from kiwi.model.user.Group g " +
					"where g.owner = :owner"),
	@NamedQuery(name="groupService.getGroupsByUser",
			query="select g from kiwi.model.user.Group g " +
					"left outer join fetch g.users as u " +
					"where u = :user"),
	@NamedQuery(name="groupService.getAllMyGroups",
			query="select g from kiwi.model.user.Group g " +
					"left outer join fetch g.users as u " +
					"where u = :user or g.owner = :owner"),					
	@NamedQuery(name="groupService.getGroupNamesByRole",
			query="select g.name from kiwi.model.user.Group g " +
					"left outer join g.roles as r " +
					"where r.name = :name")
})
public class Group implements PermissionOwner, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="groupname")
	@RDF(Constants.NS_KIWI_CORE + "hasGroupName")	
	private String name;	

	@OneToOne(fetch=FetchType.LAZY)
    //@RDF(Constants.NS_KIWI_CORE+"owner")
    private User owner;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Long id;

    @Version
    private Long version;    
    
    @RDF(Constants.NS_KIWI_CORE + "hasGroupPhoto")
    @OneToOne
    private ContentItem groupPhoto;

    @NotNull
    @OneToOne(fetch=FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    private KiWiResource resource;
    
    @ManyToMany(cascade = CascadeType.PERSIST)
    private Set<User> users;

    @ManyToMany(cascade = CascadeType.PERSIST, mappedBy = "groups")
    private Set<Role> roles;
    
    /**
     *  Social Recommendations
     */    
    @OneToMany(fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ContactRecommendation> contactRecommendations;    
    
    public Group() {
	}
    
    public Group(String name) {
    	this.name = name;
	}
    
    /**
	 * @return the name
	 */
    @RoleName
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
    
	public String toString(){
		return name;
	}

	public Set<User> getUsers() {
		if(users==null){
			users = new HashSet<User>();
		}
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
	public void setOwner(User owner) {
		this.owner = owner;
	}

	public User getOwner() {
		return owner;
	}

	public ContentItem getGroupPhoto() {
		return groupPhoto;
	}

	public void setGroupPhoto(ContentItem groupPhoto) {
		this.groupPhoto = groupPhoto;
	}

	public void setResource(KiWiResource resource) {
		this.resource = resource;
	}

	public KiWiResource getResource() {
		return resource;
	}
	
    /**
	 * @param roles the roles to set
	 */
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	/**
	 * @return the roles
	 */
	public Set<Role> getRoles() {
		return roles;
	}

	/**
     * Sets the type of the resource
     * @param type
     */
	public void setType(KiWiUriResource type) {
		this.resource.addType(type);
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Group other = (Group) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public Set<ContactRecommendation> getContactRecommendations() {
		return contactRecommendations;
	}

	public void setContactRecommendations(
			Set<ContactRecommendation> contactRecommendations) {
		this.contactRecommendations = contactRecommendations;
	}
}
