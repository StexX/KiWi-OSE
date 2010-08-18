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
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import kiwi.model.Constants;
import kiwi.model.annotations.RDF;
import kiwi.model.annotations.RDFType;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiEntity;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.personalization.UserPreference;
import kiwi.model.recommendation.ContactRecommendation;
import kiwi.model.tagging.Tag;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.validator.Email;
import org.hibernate.validator.NotNull;
import org.jboss.seam.annotations.security.management.UserEnabled;
import org.jboss.seam.annotations.security.management.UserFirstName;
import org.jboss.seam.annotations.security.management.UserLastName;
import org.jboss.seam.annotations.security.management.UserPassword;
import org.jboss.seam.annotations.security.management.UserPrincipal;
import org.jboss.seam.annotations.security.management.UserRoles;

/**
 * This entity represents the User that is registered at the KiWi Platform.
 * He has a login name, a first and a last name, an email address, a creation date,
 * which is the date of his/her registration at the KiWi platform, a picture, a passwordHash and a
 * transient password, which is only stored temporarily.
 * He may have roles and be part of one or more than one groups and he also may have friends, 
 * which are other users, who are registered at the KiWi platform.
 * 
 * We are using JBoss Seams IdentityManagement for creating users, for login/logout actions 
 * and for permission handling.
 *
 *
 * @author Stephanie Stroka
 */
@Entity
@org.hibernate.annotations.Entity(optimisticLock=OptimisticLockType.VERSION)
@Table(name = "KiWiUser",uniqueConstraints = @UniqueConstraint(columnNames = "login"))
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@RDFType(Constants.NS_KIWI_CORE + "User")
@NamedQueries({
	@NamedQuery(name="currentUserFactory.getUserByLogin",
				query="from User u " +
//			    	  "inner join fetch u.resource " +
			    	  "left outer join fetch u.friends " +
			    	  "where u.login = :login "),
	@NamedQuery(name="userService.getUserByName",
				query="from User u " +
			    	  "inner join fetch u.resource " +
			    	  "left outer join fetch u.friends " +
			    	  "where u.firstName = :fn and u.lastName = :ln"),
	@NamedQuery(name="userService.getUserByFacebookAcc",
				query="from User u " +
			    	  "inner join fetch u.resource " +
			    	  "left outer join fetch u.friends " +
			    	  "where u.fbId = :fbId"),
    @NamedQuery(name="userService.getUserByWebId",
                query="from User u " +
                	  "inner join fetch u.resource " +
			    	  "left outer join fetch u.friends " +
			    	  "where u.webId = :webId"),
    @NamedQuery(name="userService.getUserByUri",
				query="from User u " +
			    	  "inner join fetch u.resource " +
			    	  "left outer join fetch u.friends " +
			    	  "where u.resource.uri = :uri"),
	@NamedQuery(name="userService.getUserByEmail",
				query="from User u " +
					  "inner join fetch u.resource " +
					  "left outer join fetch u.friends " +
	  				  "where u.email = :email"),
	@NamedQuery(name="currentUserFactory.getUserById",
		        query="from User u " +
        	      "where u.id = :id "),	  				  
	@NamedQuery(name="kiwiEntityManager.queryUser",
				query="from User o " +
			    	  "inner join fetch o.resource " +
			    	  "left outer join fetch o.friends " +
			    	  "where o.resource.anonId IN (:anonIds) " +
					  "   or o.resource.uri    IN (:uris)")			
})
public class User implements PermissionOwner, KiWiEntity, Serializable {
 
	private static final long serialVersionUID = 1L;

	@Index(name="user_login_idx")
    @Column(unique=true, nullable=false)
    @RDF(Constants.NS_FOAF + "nick")
    @NotNull
    private String login;
	
	private String passwordHash;
	
	@Transient
	private String password;
	
	/* TODO: think about erasing the key. 
	 * It should not be stored, not even in the Session */
	@Transient
	private PrivateKey privateKey;
	
	@Transient
	private PublicKey publicKey;
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch=FetchType.LAZY)
//    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @UserRoles
    private Set<Role> roles;
	
	@UserEnabled
    private boolean enabled;
    
    @RDF(Constants.NS_FOAF + "firstName")
    @UserFirstName
    private String firstName;
    
    @RDF(value = {Constants.NS_FOAF + "name", 
    		Constants.NS_FOAF + "family_name", 
    		Constants.NS_FOAF + "familyName", 
    		Constants.NS_FOAF + "surname", 
    		Constants.NS_FOAF + "lastName"})
    @UserLastName
    private String lastName;
    
    @Email
    @RDF(Constants.NS_FOAF + "mbox")
    private String email;

    /** the creation date of the User **/
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(updatable = false, nullable=false)
    private Date created;
    
    /** the deletionDate date of the ContentItem **/
//  no need to store it in DB, since it will be created when the user is deleted and
//  deleted, when the user is deleted, too, and therefore will always be null
    @Transient
    @RDF(Constants.NS_KIWI_CORE+"deletedOn")
    private Date deletedOn;


    /** the content items this User has authored **/
    // we do not cascade due to performance issues ...
    @OneToMany(fetch = FetchType.LAZY, mappedBy="author")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size=20)
    private Set<ContentItem> authoredContent;
    
    /** the content items currently watched by the user */
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "UserWatchedContent",
	        joinColumns = @JoinColumn(name="user_id"),
	        inverseJoinColumns = @JoinColumn(name="contentitem_id")
	)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ContentItem> watchedContent;
    
    /** the tags this user has created */
    @OneToMany(fetch = FetchType.LAZY, mappedBy="taggedBy")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size=100)
	@Filter(name = "tagNotDeleted", condition = "deleted = :is_deleted")
    private Set<Tag> tags;

    @RDF(Constants.NS_FOAF + "knows")
    @ManyToMany(fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size=20)
    private Set<User> friends;
    
    @RDF(Constants.NS_KIWI_CORE + "hasProfilePhoto")
    @OneToOne
    private ContentItem profilePhoto;

/*    @NotNull UPDATE: the NotNull annotation cannot be set anymore, 
 * 				because the very first user that will be created cannot 
 * 				get a KiWiresource. (The workflow would be: 
 * 				(1) Create user, 
 * 				(2) create URIResource before user gets persisted, 
 * 				(3) get into KiWiSynchronizationImpl, because the creation of a resource 
 * 					triggers the registration of the KiWiSyncronizationImpl,
 * 				(4) The KiWiSynchronizationImpl tries to create another user, because the 
 * 					Revision object needs it.)
 */
    @OneToOne(fetch=FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    private KiWiResource resource;
    
    /** the content items this User says is similar **/
    @OneToMany(fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size=20)
    @Column(name="SIMILAR_ITEMS")
    private Set<ContentItem> similarItems; 
    
    /**
     *  Social Recommendations
     */    
    @OneToMany(fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ContactRecommendation> contactRecommendations;    

    @ManyToMany(fetch=FetchType.LAZY, mappedBy = "users")
    private List<Group> groups;
    
    @Column(unique=true)
    private String fbId;
    
    @Column(unique=true)
    private String webId;
    
    @NotNull    
    private boolean deleted = false;

    @Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Long id;
    
    // optimistic locking; improve performance of merge etc.
    @Version
    private Long version;
    
    @OneToOne(fetch=FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    private UserPreference userPreference;  
    
    public User() {
        created = new Date();
        enabled = true;
        tags    = new HashSet<Tag>();
		watchedContent = new HashSet<ContentItem>();
	}

    public User(String login) {
    	this();
    	this.login = login;
    }
    
    /**
     * returns the time of creation
     * @return
     */
    public Date getCreated() {
        return created;
    }

    /**
     * sets the time of creation
     * @param created
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * @return login
     */
    @UserPrincipal
    public String getLogin() {
        return login;
    }
    
    /**
     * Sets the login of the user
     * @param login
     */
    public void setLogin(String login) {
        this.login = login;
    }


    /**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param passwordHash the passwordHash to set
	 */
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	/**
	 * @return the passwordHash
	 */
	@UserPassword(hash = "MD5")
	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	/**
     * @return firstName
     */
    @UserFirstName
    public String getFirstName() {
        return firstName;
    }
    
    /**
     * Sets the firstName
     * @param firstName
     */

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return lastname
     */
    @UserLastName
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the lastname
     * @param lastname
     */
    public void setLastName(String lastname) {
        this.lastName = lastname;
    }

    /**
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the type of the resource
     * @param type
     */
	public void setType(KiWiUriResource type) {
		this.resource.addType(type);
	}

    /**
     * Return the content objects this User has authored
     * @return
     */
    public Set<ContentItem> getAuthoredContent() {
        return authoredContent;
    }

    /**
     * Set the content objects this User has authored (used by JPA)
     * @param authoredContent
     */
    public void setAuthoredContent(Set<ContentItem> authoredContent) {
        this.authoredContent = authoredContent;
    }

    
    
    /**
     * Get the set of content items on the user's watchlist.
     * @return
     */
    public Set<ContentItem> getWatchedContent() {
		return watchedContent;
	}

    /**
     * Set the set of content items on the user's watchlist.
     * @param watchedContent
     */
	public void setWatchedContent(Set<ContentItem> watchedContent) {
		this.watchedContent = watchedContent;
	}

	/**
     * Return the tags created by this User
     * @return
     */
    public Set<Tag> getTags() {
        return tags;
    }

    /**
     * Set the tags created by this User
     * @param tags
     */
    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }
    

	/**
	 * @return friends
	 */
	public Set<User> getFriends() {
		if(friends == null) {
			friends = new HashSet<User>();
		}
		return friends;
    }

	/**
	 * Sets the friends
	 * @param friends
	 */
	public void setFriends( Set<User> friends ) {
		this.friends = friends;
	}

    /* (non-Javadoc)
	 * @see kiwi.model.kbase.KiWiEntity#getResource()
	 */
	public KiWiResource getResource() {
		return resource;
	}

	/* (non-Javadoc)
	 * @see kiwi.model.kbase.KiWiEntity#setResource(kiwi.model.kbase.KiWiSpecialResource)
	 */
	public void setResource(KiWiResource resource) {
		this.resource = resource;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		User other = (User) obj;
		if (getLogin() == null) {
			if (other.getLogin() != null)
				return false;
		} else if (!getLogin().equals(other.getLogin()))
			return false;
		return true;
	}

	/**
	 * @return the deleted
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 * Return the content item object associated with this user.
	 */
	public ContentItem getContentItem() {
		assert(resource != null);
		
		return resource.getContentItem();
	}

	/**
	 * Return the profile photo content item object associated with this user.
	 */
	public ContentItem getProfilePhoto() {
		return profilePhoto;
	}
	
	public void setProfilePhoto(ContentItem profilePhoto) {
		this.profilePhoto = profilePhoto;
	}

	/**
	 * @param fbId the fbId to set
	 */
	public void setFbId(String fbId) {
		this.fbId = fbId;
	}

	/**
	 * @return the fbId
	 */
	public String getFbId() {
		return fbId;
	}

	/**
	 * @param webId the webId to set
	 */
	public void setWebId(String webId) {
		this.webId = webId;
	}

	/**
	 * @return the webId
	 */
	public String getWebId() {
		return webId;
	}

	public Long getVersion() {
		return version;
	}    

    /**
     * @return group
     */
    public List<Group> getGroups() {
        return groups;
    }

    /**
     * Sets the group
     * @param group
     */
    public void setGroups(List<Group> group) {
        this.groups = group;
    }
    
    /**
	 * @return the role
	 */
	public Set<Role> getRoles() {
		return roles;
	}

	/**
	 * @param role the role to set
	 */
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	/**
     * @param contentItem
     */
    public void addSimilarItem(ContentItem contentItem) {
    	Set<ContentItem> simItems = new HashSet<ContentItem>();
    	simItems.addAll(getSimilarItems());
    	simItems.add(contentItem);
    	setSimilarItems(simItems);
    }

	/**
	 * @return
	 */
	public Set<ContentItem> getSimilarItems() {
		if (similarItems==null) {
			similarItems = new HashSet<ContentItem>();
		}
		return similarItems;
	}

	/**
	 * @param similarItems
	 */
	public void setSimilarItems(Set<ContentItem> similarItems) {
		this.similarItems = similarItems;
	}
	
    /**
     * @return
     */
    public Set<ContactRecommendation> getContactRecommendations() {
		return contactRecommendations;
	}

	/**
	 * @param contactRecommendations
	 */
	public void setContactRecommendations(
			Set<ContactRecommendation> contactRecommendations) {
		this.contactRecommendations = contactRecommendations;
	}
	
	/**
	 *  Add a contact recommendation
	 * @param contactRecommendation
	 */
	public void addContactRecommendation(ContactRecommendation contactRecommendation) {
		if (contactRecommendations==null) {
			contactRecommendations = new HashSet<ContactRecommendation>();
		}
		getContactRecommendations().add(contactRecommendation);
	}	
	
	/**
	 * Remove a contact recommendation
	 * @param contactRecommendation
	 */
	public void removeContactRecommendation(ContactRecommendation contactRecommendation) {
		if (contactRecommendations.contains(contactRecommendation)) {
			contactRecommendations.remove(contactRecommendation);
		}
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(Long version) {
		this.version = version;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Date getDeletedOn() {
		return deletedOn;
	}

	public void setDeletedOn(Date deletedOn) {
		this.deletedOn = deletedOn;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}
	
	public String toString() {
		return "User:"+login;
	}	
	
	public UserPreference getUserPreference() {
		return userPreference;
	}

	public void setUserPreference(UserPreference userPreference) {
		this.userPreference = userPreference;
	}	
}
