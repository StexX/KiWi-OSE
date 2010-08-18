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

package kiwi.model.tagging;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import kiwi.model.Constants;
import kiwi.model.annotations.RDF;
import kiwi.model.annotations.RDFType;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiEntity;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.user.User;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.validator.NotNull;

/**
 * This entity represents a Tag. 
 * It contains information about the user, who created it, the resource that has been tagged 
 * and the resource that is used to tag. This means that the tag serves as a annotated link 
 * between two ContentItems. 
 *
 * @author Sebastian Schaffert
 */
@Entity
@org.hibernate.annotations.Entity(optimisticLock=OptimisticLockType.VERSION)
@RDFType(Constants.NS_KIWI_CORE + "Tagging")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@BatchSize(size = 20)
@NamedQueries({
	// get a tag by user and by content item id
	@NamedQuery(name="taggingAction.getTagByIdAuthor",
			query="select t " +
				  "from kiwi.model.tagging.Tag t " +
				  "where t.taggingResource.id = :id " +
				  "  and t.taggedBy.login = :login " +
				  "  and t.deleted = false"),
	@NamedQuery(name="tagCloudService.getTagCloudByAuthor",
			query="select t.taggingResource.id, t.taggingResource.title, count(t) as count, min(t.creationTime) as firstTagging, max(t.creationTime) as lastTagging " +
				  "from kiwi.model.tagging.Tag t " +
				  "where t.taggedBy.login = :login " +
				  "  and t.deleted = false " +
				  "group by t.taggingResource.id, t.taggingResource.title " +
				  "order by t.taggingResource.title"),
	@NamedQuery(name="tagCloudService.getFullTagCloud",
			query="select t.taggingResource.id, count(t) as count, min(t.creationTime) as firstTagging, max(t.creationTime) as lastTagging " +
				  "from kiwi.model.tagging.Tag t " +
				  "where t.deleted = false " +
				  "group by t.taggingResource.id, t.taggingResource.title " +
				  "order by t.taggingResource.title"),
	@NamedQuery(name="taggingService.listTagsByLabel",
			query="select t " +
				  "from kiwi.model.tagging.Tag t " +
				  "where t.taggingResource.title = :label " +
				  "  and t.deleted = false"),
	@NamedQuery(name="taggingAction.getTagByLabelItem",
			query="select t " +
				  "from kiwi.model.tagging.Tag t " +
				  "where t.taggingResource.title = :label " +
				  "  and t.taggedResource.id = :itemid " +
				  "  and t.deleted = false"),
	@NamedQuery(name="tagCloudService.aggregateTagsByContentItem",
			query="select t.taggingResource.title, " +
					"     count(t.taggingResource) as count, " +
					"     max(t.creationTime) as lastTaggingDate " +
				  "from kiwi.model.tagging.Tag t " +
				  "where t.taggedResource = :ci " +
				  "  and t.deleted = false " +
				  "group by t.taggingResource.title " +
				  "order by t.taggingResource.title"),
	@NamedQuery(name="taggingService.listTagsByContentItem",
			query="select t " +
				  "from kiwi.model.tagging.Tag t " +
				  "where t.taggedResource = :ci " +
				  "  and t.deleted = false "),
	@NamedQuery(name="taggingService.listTagsByUser",
			query="select t " +
				  "from kiwi.model.tagging.Tag t " +
				  "where t.taggedBy = :cu " +
				  "  and t.deleted = false "),
//	@NamedQuery(name="taggingService.listTagsByUser",
//			query="select t " +
//				  "from kiwi.model.tagging.Tag t " +
//				  "where t.taggingResource.title = :tagLabel " +
//				  "  and t.taggingResource.deleted = false " +
//				  "  and t.deleted = false "),
	@NamedQuery(name="kiwiEntityManager.queryTag",
				query="from ContentItem o " +
			    	  "inner join fetch o.resource " +
			    	  "where o.resource.anonId IN (:anonIds) " +
					  "   or o.resource.uri    IN (:uris)"),

	@NamedQuery(name="recommendationService.personalRecommendations",
				query="select ci "
					+ "from   kiwi.model.tagging.Tag t2, " +
							" kiwi.model.tagging.Tag t1, " +
							" ContentItem ci  "
					+ "where " +
						  "     t2.taggedResource = ci " +
						  " AND t2.taggingResource.title = t1.taggingResource.title " +
						  " AND t1.taggedBy.login = :cu " +
						  " AND t1.taggingResource.deleted = false " +
						  " AND t2.taggedBy.login <> :cu " +
						  " and  NOT EXISTS (from KiWiTriple t " +
				    	  "              where ci.resource.id = t.subject.id " +
				    	  "                and t.property.uri = '"+Constants.NS_RDF+"type' " +
				    	  "                and t.object.uri in ('"+Constants.NS_KIWI_SPECIAL+"KiWiGroup')) " +
				    	  " and ci.author.login <> :cu " +
				    	  " and ci.deleted = false"),
					  
	// such as, all fragments tagged with this content item. 
	@NamedQuery(name="ie.activeLearningFragmentsAction.listTagsByTaggingItemAndTaggedResourceType",
			query=	"select tag " +
					"from kiwi.model.tagging.Tag tag, kiwi.model.kbase.KiWiTriple triple " +
					"where tag.taggingResource.id = :id " +
					"  and tag.deleted = false" +
					"  and triple.property.uri = '" + Constants.NS_RDF + "type' " +
					"  and triple.subject.id = tag.taggedResource.resource.id " +
					"  and triple.object.id = :typeid " +
					"  and triple.deleted = false"),
					
	@NamedQuery(name="kiwi.informationextraction.informationExtractionService.listTaggedItemsByTaggingItem",
			query=	"select tag.taggedResource " +
					"from kiwi.model.tagging.Tag tag " +
					"where tag.taggingResource.id = :id " +
					"  and tag.deleted = false"),
	@NamedQuery(name="tagCloudService.getUsersUsingTag",
			query="select t.taggedBy.login, count(t), min(t.creationTime) as firstTagging, max(t.creationTime) as lastTagging " +
				  "from kiwi.model.tagging.Tag t " +
				  "where t.taggingResource.title = :tag " +
				  "  and t.deleted = false " + 
				  " group by t.taggedBy.login")

})
public class Tag implements KiWiEntity, Serializable {

	private static final long serialVersionUID = 3914028705399725342L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Long id;
    
    /** the user who has created this tag */
    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch=FetchType.LAZY)
    @RDF(Constants.NS_HGTAGS+"taggedBy")
    @Index(name="tag_taggedby")
    @Immutable
    private User taggedBy;

    /** the creation time */
    @Temporal(TemporalType.TIMESTAMP)
    @RDF(Constants.NS_HGTAGS+"taggedOn")
    private Date creationTime;
    
    /** the deletionDate date of the ContentItem **/
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(updatable = true, nullable=true)
    @RDF(Constants.NS_KIWI_CORE+"deletedOn")
    private Date deletedOn;
    
    /** A content item that represents the meaning of the tag */
    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch=FetchType.LAZY)
    @RDF(Constants.NS_HGTAGS+"associatedTag")
    @Index(name="tag_taggingresource")
    @Immutable
    private ContentItem taggingResource;
    
    /** the content items that use this tag */
    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch=FetchType.LAZY)
    @RDF(Constants.NS_HGTAGS+"taggedResource")
    @Index(name="tag_taggedresource")
    @Immutable
    private ContentItem taggedResource;
    

    @NotNull
    @OneToOne(fetch=FetchType.EAGER, cascade = {CascadeType.PERSIST})
    @Immutable
    private KiWiResource resource;

    @NotNull
    private boolean deleted;
    
    //@RDF(Constants.NS_HGTAGS+"hasPurpose")
    private String purpose;    
    
    @Version
    private Long version;
    
    public Tag() {
        this.creationTime = new Date();
    }

    public Tag( ContentItem taggedResource, User taggedBy, ContentItem taggingResource ) {
    	this.taggedResource = taggedResource;
    	this.creationTime = new Date();
        this.taggedBy = taggedBy;
        this.taggingResource = taggingResource;
        this.deleted = false;
    }

    /**
     * returns the time of creation
     * @return
     */
    public Date getCreationTime() {
        return creationTime;
    }

    /**
     * sets the time of creation
     * @param creationTime
     */
    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

 
    /**
     * sets the creator of the tag
     * @return
     */
    public User getTaggedBy() {
        return taggedBy;
    }
    
    /**
	 * @param taggedBy the taggedBy to set
	 */
	public void setTaggedBy(User taggedBy) {
		this.taggedBy = taggedBy;
	}

    /**
     * Returns a Set of ContentItems that use this Tag. This method is 
     * important to classify ContentItems. (All ContentItems in this Set
     * share the same tag) 
	 * @return the taggedResources
	 */
	public ContentItem getTaggedResource() {
		return taggedResource;
	}

	/**
	 * @param taggedResources the taggedResources to set
	 */
	public void setTaggedResource(ContentItem taggedResource) {
		this.taggedResource = taggedResource;
	}
    
	/**
	 * Sets the meaning of the tag which is represented by a ContentItem.
	 * This method is used to link the tag to a ContentItem that holds the
	 * main information about the tag in its content
	 * @param meaning
	 */
    public void setTaggingResource( ContentItem meaning ) {
    	this.taggingResource = meaning;
    }
    
    /**
     * Returns the main ContentItem that explains the tag
     * @return
     */
    public ContentItem getTaggingResource( ) {
    	return this.taggingResource;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tag other = (Tag) obj;
        if (getId() != other.getId() && (this.getId() == null || !this.getId().equals(other.getId()))) {
            return false;
        }
        if (getResource() != other.getResource() && (getResource() == null || !getResource().equals(other.getResource()))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
        hash = 59 * hash + (this.getResource() != null ? this.getResource().hashCode() : 0);
        return hash;
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
	 * @return
	 */
	public String getPurpose() {
		if (purpose==null) {
			purpose = new String("relates_to");
		}
		return purpose;
	}

	/**
	 * @param purpose
	 */
	public void setPurpose(String purpose) {
		if (purpose==null) {
			purpose = new String("relates_to");
		}
		this.purpose = purpose;
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

	public Date getDeletedOn() {
		return deletedOn;
	}

	public void setDeletedOn(Date deletedOn) {
		this.deletedOn = deletedOn;
	}
	
	public String toString() {
		String title = taggingResource == null ? "no title" : taggingResource.getTitle();
		String user = taggedBy == null ? "no user" : taggedBy.toString();
		String item = taggedResource == null ? "no item" : taggedResource.getTitle();
		return "Tag("+title+","+user+","+item+")";
	}

	/**
	 * The label of the tagging resource.
	 * @return
	 * @author szabyg
	 */
	public String getLabel() {
		return taggingResource.getTitle();
	}

	/**
	 * Is the taggingResource controlled vocabulary (SKOS Concept)? 
	 * @return
	 * @author szabyg
	 */
	public boolean isControlled() {
		return getResource().hasType(Constants.NS_SKOS + "Concept");
	}	
}
