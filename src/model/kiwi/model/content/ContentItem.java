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
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import kiwi.model.Constants;
import kiwi.model.annotations.RDF;
import kiwi.model.annotations.RDFType;
import kiwi.model.kbase.KiWiEntity;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.perspective.Perspective;
import kiwi.model.revision.CIVersion;
import kiwi.model.user.User;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.Type;
import org.hibernate.validator.Length;
import org.jboss.seam.annotations.security.Restrict;

/**
 * The ContentItem is the main entity of the KiWi project.
 * It is directly connected to a KiWiResource (which is an RDF resource)
 * and contains information about the content it holds (TextContent, MediaContent, Title)
 * and meta-information, e.g. creation and modification date, language, rating, etc.
 *
 *
 * @author Sebastian Schaffert
 */
@Entity
@org.hibernate.annotations.Entity(optimisticLock=OptimisticLockType.VERSION)
//@Indexed(index="ContentItems")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@BatchSize(size = 20)
@RDFType(Constants.NS_KIWI_CORE + "ContentItem")
@NamedQueries({
	@NamedQuery(name="taggingAction.autocomplete",
				query="select ci.title " +
					  "from ContentItem ci left outer join ci.tagLabels as tagLabel " +
					  "where (lower(ci.title) like :n " +
					  "   or lower(tagLabel) like :n) " +
					  "  and ci.deleted = false " +
					  "order by ci.title"),
	@NamedQuery(name="taggingAction.autocompleteClasses",
			query="select ci.title " +
				  "from ContentItem ci, KiWiTriple t_type left outer join ci.tagLabels as tagLabel " +
				  "where (lower(ci.title) like :n " +
				  "   or lower(tagLabel) like :n) " +
		       	  "  and ci.resource.id = t_type.subject.id " +
		       	  "  and t_type.property.uri = '"+Constants.NS_RDF+"type' " +
		       	  "  and t_type.object.uri = '"+Constants.NS_OWL+"Class' " +
				  "  and ci.deleted = false " +
				  "order by ci.title"),
    @NamedQuery(name="contentItemService.byUri",
    		    query="from ContentItem ci " +
					  "join fetch ci.resource " +
			    	  "left outer join fetch ci.textContent " +
			    	  "left outer join fetch ci.mediaContent " +
//					  "left outer join fetch ci.comments " +
//					  "left join fetch ci.tags " +
    		    	  "where ci.resource.uri = :uri and ci.deleted = false"),
    @NamedQuery(name="contentItemService.byUriIncludeDeleted",
    		    query="from ContentItem ci " +
    		    	  "join fetch ci.resource " +
    		       	  "left outer join fetch ci.textContent " +
    		      	  "left outer join fetch ci.mediaContent " +
//    		    					  "left outer join fetch ci.comments " +
//    		    					  "left join fetch ci.tags " +
    		          "where ci.resource.uri = :uri"),
     @NamedQuery(name="contentItemService.byTitle",
    		    query="from ContentItem ci " +
					  "join fetch ci.resource " +
			    	  "left outer join fetch ci.textContent " +
			    	  "left outer join fetch ci.mediaContent " +
//					  "left outer join fetch ci.comments " +
//					  "left join fetch ci.tags " +
    		    	  "where ci.title = :title and ci.deleted = false"),
    @NamedQuery(name="contentItemService.byAnonId",
    		    query="from ContentItem ci " +
					  "join fetch ci.resource " +
			    	  "left outer join fetch ci.textContent " +
			    	  "left outer join fetch ci.mediaContent " +
//					  "left outer join fetch ci.comments " +
//					  "left join fetch ci.tags " +
    		    	  "where ci.resource.anonId = :anonId and ci.deleted = false"),
    @NamedQuery(name="contentItemService.byId",
			    query="from ContentItem ci " +
					  "join fetch ci.resource " +
			    	  "left outer join fetch ci.textContent " +
			    	  "left outer join fetch ci.mediaContent " +
//					  "left outer join fetch ci.comments " +
//					  "left join fetch ci.tags " +
			    	  "where ci.id = :id"),
    @NamedQuery(name="contentItemService.list",
    		    query="from ContentItem ci " +
					  "join fetch ci.resource " +
//					  "left outer join fetch ci.comments " +
    		    	  "where ci.deleted = false"),
    @NamedQuery(name="contentItemService.count",
			    query="select count(ci.id) " +
			    	  "from ContentItem ci " +
			    	  "where ci.deleted = false"),
    @NamedQuery(name="contentItemService.listSorted", // predictable sort order
			    query="from ContentItem ci " +
					  "join fetch ci.resource " +
//					  "left outer join fetch ci.comments " +
			    	  "where ci.deleted = false " +
			    	  "order by ci.id"),
    @NamedQuery(name="commentService.listComments", 
			    query="select comment " +
			    	  "from ContentItem comment, KiWiTriple t " +
					  "join fetch comment.resource " +
			    	  "left outer join fetch comment.textContent " +
			    	  "where comment.deleted = false " +
			    	  "and t.object.id = comment.resource.id " +
			    	  "and t.property.uri = '"+Constants.NS_KIWI_CORE+"hasComment' " +
			    	  "and t.subject.contentItem = :item " +
			    	  "order by comment.modified desc"),
    @NamedQuery(name="contentItemService.listByDate",
			    query="select ci "+
			          "from ContentItem ci " +
					  "join fetch ci.resource " +
//					  "left outer join fetch ci.comments " +
			    	  "where " +
			    	  "  NOT EXISTS (from KiWiTriple t " +
			    	  "              where ci.resource.id = t.subject.id " +
			    	  "                and t.property.uri = '"+Constants.NS_RDF+"type' " +
			    	  "                and t.object.uri in ('"+Constants.NS_KIWI_CORE+"Tagging')) " +
			    	  "  and ci.deleted = false " +
			    	  "order by ci.modified desc"),
	/* retrieve a content item by its type; type is a KiWiResource object passed as named parameter ":type" */
	@NamedQuery(name="contentItemService.byType",
			    query="select ci from ContentItem ci " +
			    	  "inner join fetch ci.resource, KiWiTriple t " +
//					  "left outer join fetch ci.comments " +
			    	  "where ci.resource.id = t.subject.id " +
			    	  "  and t.property.uri = '"+Constants.NS_RDF+"type' " +
			    	  "  and t.object = :type " +
			    	  "  and ci.deleted = false " +
			    	  "order by ci.title"),
	@NamedQuery(name="kiwiEntityManager.ciTitleByUri",
			    query="select ci.title " +
				      "from ContentItem ci " +
			    	  "where ci.resource.uri = :uri and ci.deleted = false"),
	@NamedQuery(name="kiwiEntityManager.ciTitleByAnonId",
			    query="select ci.title " +
				      "from ContentItem ci " +
	  				  "where ci.resource.anonId = :anonId and ci.deleted = false"),
	@NamedQuery(name="ciVersionService.ciVersionCount", /* efficient counting of versions */
			    query="select count(v.id) " +
				      "from ContentItem ci join ci.versions v " +
	  				  "where ci.id = :ci_id"),
	@NamedQuery(name="kiwiGATEService.listContentItems", /* efficient listing of URI and title */
			    query="select ci.title, ci.resource.uri " +
				      "from ContentItem ci " +
	  				  "where ci.resource.class='u' and ci.deleted=false"),
	@NamedQuery(name="kiwiEntityManager.queryContentItem",
				query="from ContentItem o " +
			    	  "inner join fetch o.resource " +
			    	  "left outer join fetch o.textContent " +
			    	  "left outer join fetch o.mediaContent " +
//					  "left outer join fetch o.comments " +
			    	  "where o.resource.anonId IN (:anonIds) " +
					  "   or o.resource.uri    IN (:uris)"),
	@NamedQuery(name="contentItemService.byAuthor",
			    query="from ContentItem ci " +
					  "join fetch ci.resource " +
			    	  "where ci.author.login = :login and ci.deleted = false"),
	@NamedQuery(name="contentItemService.taggedByUser",
			    query="select ci from ContentItem ci " +
					  "join fetch ci.resource, " +
					  "kiwi.model.tagging.Tag tag " +
					  "where tag.taggedResource.id = ci.id " +
					  "and ci.author.login = :login " +
					  "and ci.deleted = false"),
	@NamedQuery(name="contentItemService.currentVersionId",
			    query="select ci.version from ContentItem ci " +
					  "where ci.id = :id "),
	@NamedQuery(name="contentItemService.getAllContentItemsByDate",
				query="select ci " +
					  "from ContentItem ci " +
					  "where (ci.created >= :since or ci.modified >= :since or ci.deletedOn >= :since or ci.mergedOn >= :since) and (ci.created <= :until or ci.modified <= :until or ci.deletedOn <= :until)" +
					  "order by ci.modified"),
	 @NamedQuery(name="contentItemService.getTagLabelsByContentItemAndAuthor",
				query = " select tag.taggingResource.title " +			
					  " from ContentItem ci, kiwi.model.tagging.Tag tag "+
					  " where tag.taggedResource.id = ci.id "+
					  " and ci.author = :author " +
					  " and ci.id =:contentItemId and ci.deleted = false")						  
})
@FilterDefs({
	@FilterDef(name="tagNotDeleted",parameters={ @ParamDef( name="is_deleted", type="java.lang.Boolean") })
})
@Restrict
public class ContentItem implements Serializable, KiWiEntity, ContentItemI, Comparable<ContentItem> {

	private static final long serialVersionUID = 1L;
 	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)    
    @RDF(Constants.NS_KIWI_CORE+"id")
    private Long id;
    
    /** the creation date of the ContentItem **/
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(updatable = false, nullable=false)
    @RDF(Constants.NS_KIWI_CORE+"createdOn")
    private Date created;

    /** the last modification date of the ContentItem **/
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(updatable = true, nullable=false)
    @RDF(Constants.NS_DC_TERMS+"modified")
    private Date modified;
    
    /** the deletionDate date of the ContentItem **/
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(updatable = true, nullable=true)
    @RDF(Constants.NS_KIWI_CORE+"deletedOn")
    private Date deletedOn;
    
    /** the mergeDate of the ContentItem **/
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(updatable = true, nullable=true)
    @RDF(Constants.NS_KIWI_CORE+"mergedOn")
    private Date mergedOn;
    
    
	/** content that is hold by the ContentItem. **/
    @OneToOne(fetch=FetchType.EAGER)  // nullable, thus lazy fetching does not work
    @RDF(Constants.NS_KIWI_CORE+"hasTextContent")
    @Fetch(FetchMode.JOIN)
    @LazyToOne(LazyToOneOption.FALSE)
    @Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
    private TextContent textContent;
            
    /** media content that is hold by ContentItem - different from content, because of indexing. */
    @OneToOne(fetch=FetchType.EAGER) // nullable, thus lazy fetching does not work
    @Fetch(FetchMode.JOIN)
    @LazyToOne(LazyToOneOption.FALSE)
    @Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
    private MediaContent mediaContent;
    
    /** Every KiWiResource has one ContentItem. **/
    //@NotNull
    @OneToOne(mappedBy="contentItem", fetch=FetchType.EAGER, optional=false)
    @Fetch(FetchMode.JOIN)
    @LazyToOne(LazyToOneOption.FALSE)
    @Immutable
    private KiWiResource resource;

    /** The KnowledgeSpace the content item belongs to */
    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    @LazyToOne(LazyToOneOption.FALSE)
    @Immutable
    private KiWiUriResource context;
       
    @Transient
    @RDF(Constants.NS_KIWI_CORE+"rating")
    private Double rating;
    
    /** the title of the contentitem **/
//    @NotNull
    @Index(name="idx_content_item_title")
    @RDF(Constants.NS_KIWI_CORE+"title")
    @Length(max=255)
    private String title;

    /** the language of the content item */
    @Column(name="lang")
    @RDF(Constants.NS_KIWI_CORE+"language")
    private Locale language;
    
    /** the last author of the content item */
    @ManyToOne(fetch=FetchType.LAZY)
    @RDF(Constants.NS_KIWI_CORE+"author")
    private User author;

    /** the tag labels by which this content item is found in tag suggestion */
    @CollectionOfElements(fetch=FetchType.EAGER)
    @RDF(Constants.NS_SKOS+"altLabel")
    private Set<String> tagLabels;
    
    @RDF(Constants.NS_OWL+"deprecated")
    @Transient
    private boolean deprecated;
    
	private boolean deleted;
    
    
	@GenericGenerator(name="gg_perspectives",strategy="hilo")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "contentitems_perspectives",
    		joinColumns = { @JoinColumn(name = "contentitem_id") },
    		inverseJoinColumns = { @JoinColumn(name = "perspective_id") } )
    @CollectionId(
    		columns = @Column(name="contentitems_perspectives_id"),
    		type = @Type(type="long"),
    		generator = "gg_perspectives")
    @BatchSize(size = 100)
    private List<Perspective> perspectives;
    
	/** used for calculating the score of the content item considering the tag weight of its tags*/
    @Transient
    private Long contentItemScore;
    
    @OneToMany(mappedBy="revisedContentItem", fetch=FetchType.LAZY)
    @BatchSize(size=20)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OrderBy("versionId")
    private List<CIVersion> versions;
    
    @OneToMany
    @Column(name="nestedCI")
    private List<ContentItem> nestedContentItems;
    
    @Version
    private Long version;

	public ContentItem() {
		this.perspectives = new LinkedList<Perspective>();
        this.tagLabels = new HashSet<String>();
//        this.comments  = new LinkedList<ContentItem>();
        this.created   = new Date();
        this.modified  = new Date();
        this.language  = Locale.getDefault(); // TODO: default language should be configurable
    }
    
    
    /**
     * Constructor
     * Sets the resource and the contentItem for the resource (which is itself), as well
     * @param resource
     */
    public ContentItem(KiWiResource resource) {
    	this();
        this.resource = resource;
        this.resource.setContentItem(this);
    }
    
    
    public ContentItem getDelegate() {
    	return this;
    }
    
    /**
     * Return the content object related to the provided language.
     * @return
     */
    public TextContent getTextContent() {
        return this.textContent;
    }
    
    /**
     * Set the content object for this content item and the provided language.
     * @param content
     */
    public void setTextContent(TextContent content) {
//   		content.setContentItem(this);
		this.textContent = content;
    }
    
 	/**
     * Return the localised title of this content item. Currently, the title is represented by the
     * RDF label of the corresponding resource; the method thus just delegates to KiWiResource.getLabel(Locale).
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the localised title of this content item. Currently, the title is represented by the
     * RDF label of the corresponding resource; the method thus just delegates to KiWiResource.setLabel(Locale,String).
     * @param title
     */
    public void setTitle(String title) {
    	this.title = title;
    }

    
    /**
     * Return the locale describing the language this content is represented in.
     * @return
     */
    public Locale getLanguage() {
        return language;
    }


    /**
     * Set the locale describing the language this content is represented in.
     * @param language
     */
    public void setLanguage(Locale language) {
        this.language = language;
    }
    
    
    /**
     * Return the resource associated with this content item.
     * @return
     */
    public KiWiResource getResource() {
        return resource;
    }

    /**
     * Set the resource associated with this content item
     * @param resource
     */
    public void setResource(KiWiResource resource) {
        this.resource = resource;
        this.resource.setContentItem(this);
    }

    /**
     * Return the KnowledgeSpace (a KiWiUriResource) this ContentItem belongs to.
     * @return
     */
    public KiWiUriResource getContext() {
        return context;
    }

    /**
     * Set the KnowledgeSpace (a KiWiUriResource) this ContentItem belongs to.
     * @param context
     */
    public void setContext(KiWiUriResource context) {
        this.context = context;
    }

    /**
     * Return an ID for the content object that is unique across the system and can be used to identify the content
     * object in error messages. A good way would be to use the resource of the content item together with the language of the
     * object, and possibly the revision id.
     * 
     * @return
     */
    public Long getId() {
        return id;
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
	 * The average rating of the ContentItem
	 * @param rating the rating to set
	 */
	public void setRating(Double rating) {
		this.rating = rating;
	}

	/**
	 * The average rating of the ContentItem
	 * @return the rating
	 */
	public Double getRating() {
		return rating;
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
	 * Setting the type of 
	 * @param type
	 */
	public void addType(KiWiUriResource type) {
		getResource().addType(type);
	}
	
	public Collection<KiWiResource> getTypes() {
		return getResource().getTypes();
	}

	/**
	 * @return the author
	 */
	public User getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(User author) {
		this.author = author;
	}

	
	/**
	 * @return the mediaContent
	 */
	public MediaContent getMediaContent() {
		return mediaContent;
	}

	/**
	 * @param mediaContent the mediaContent to set
	 */
	public void setMediaContent(MediaContent mediaContent) {
		this.mediaContent = mediaContent;
		//this.mediaContent.setContentItem(this);
	}

	
	
	/**
	 * @return the tagLabels
	 */
	public Set<String> getTagLabels() {
		return tagLabels;
	}

	/**
	 * @param tagLabels the tagLabels to set
	 */
	public void setTagLabels(Set<String> tagLabels) {
		this.tagLabels = tagLabels;
	}

	/**
	 * @return the revisions
	 */
	public List<CIVersion> getVersions() {
		if(versions == null) {
			versions = new LinkedList<CIVersion>();
		}
		return versions;
	}


	/**
	 * @param versions the revisions to set
	 */
	public void setVersions(List<CIVersion> versions) {
		this.versions = versions;
	}

	
	

//	public List<ContentItem> getComments() {
//		return comments;
//	}
//
//
//	public void setComments(List<ContentItem> comments) {
//		this.comments = comments;
//	}


	@Override
    public boolean equals(Object obj) {
       if (obj == null) {
            return false;
        }
        
        if(! (obj instanceof ContentItem)) {
            return false;
        }
        
        final ContentItem other = (ContentItem) obj;
        if (this.getId() != other.getId() && (this.getId() == null || !this.getId().equals(other.getId()))) {
            return false;
        }
        if (this.getTitle() != other.getTitle() && (this.getTitle() == null || !this.getTitle().equals(other.getTitle()))) {
            return false;
        }
        /*// too expensive, and not needed, because database id is unique
        if (this.resource != other.resource && (this.resource == null || !this.resource.equals(other.resource))) {
            return false;
        }
        */
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
        hash = 29 * hash + (this.getResource() != null ? this.getResource().hashCode() : 0);
        return hash;
    }
	
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 * Method used for ranking contentItem by contentScores necessary when
	 * calculating multi factor recommendations
	 */
	public int compareTo(ContentItem contentItem) {
        if (this.contentItemScore == contentItem.getContentItemScore())
            return 0;
        else if (this.contentItemScore < contentItem.getContentItemScore())
            return 1;
        else
            return -1;
    }


	public long getContentItemScore() {
		if (contentItemScore==null) {
			contentItemScore=0l;
		}
		return contentItemScore;
	}


	public void setContentItemScore(long contentItemScore) {
		this.contentItemScore = contentItemScore;
	}


	@Override
	public String getKiwiIdentifier() {
		return resource.getKiwiIdentifier();
	}
	

	/**
	 * @return the nestedContentItems
	 */
	public List<ContentItem> getNestedContentItems() {
		return nestedContentItems;
	}


	/**
	 * @param nestedContentItems the nestedContentItems to set
	 */
	public void setNestedContentItems(List<ContentItem> nestedContentItems) {
		this.nestedContentItems = nestedContentItems;
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
	
 	public List<Perspective> getPerspectives() {
		return perspectives;
	}


	public void setPerspectives(List<Perspective> perspectives) {
		this.perspectives = perspectives;
	}
	
    public Date getDeletedOn() {
		return deletedOn;
	}


	public void setDeletedOn(Date deletedOn) {
		this.deletedOn = deletedOn;
	}


	public Date getMergedOn() {
		return mergedOn;
	}


	public void setMergedOn(Date mergedOn) {
		this.mergedOn = mergedOn;
	}
	
    public boolean isDeprecated() {
		return deprecated;
	}


	public void setDeprecated(boolean deprecated) {
		this.deprecated = deprecated;
	}
}