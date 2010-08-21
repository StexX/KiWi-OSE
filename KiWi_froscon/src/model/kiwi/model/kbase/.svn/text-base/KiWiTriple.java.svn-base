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

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import kiwi.model.Constants;
import kiwi.model.user.User;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.validator.NotNull;

/**
 * KiWiExtendedTriples are one of the core concepts of the KiWi system. They
 * correspond to triples in RDF, but extend them with additional information
 * that is useful and/or needed in the KiWi system, for example author information,
 * version information, justifications (in case of inferred triples), etc.
 * 
 * Like a KiWiResource, each triple is associated with a single TripleStore.
 * All triples together essentially make up the TripleStore. As with KiWiResources,
 * this means that there is no 1:1 correspondence between RDF triples and KiWi triples.
 * An RDF triple may be represented by several KiWi triples in different KnowledgeSpaces.
 *
 * @author Sebastian Schaffert
 */
@Entity
@org.hibernate.annotations.Entity(mutable=false,optimisticLock=OptimisticLockType.VERSION)
@Immutable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@BatchSize(size = 100)
@Table(
	name = "TRIPLES"
//	uniqueConstraints = {@UniqueConstraint(columnNames={"subject_id", "property_id", "object_id", "context_id"})}
)
@org.hibernate.annotations.Table(
	appliesTo = "TRIPLES",
	indexes = { 
		@Index(name = "idx_triples_spoc", columnNames = {"subject_id","property_id","object_id","context_id"}),
		@Index(name = "idx_triples_spo", columnNames = {"subject_id","property_id","object_id"}),
		@Index(name = "idx_triples_sp", columnNames = {"subject_id","property_id"}),
		//@Index(name = "idx_triples_so", columnNames = {"subject_id","object_id"}),
		@Index(name = "idx_triples_po", columnNames = {"property_id","object_id"})
	}
)
@NamedQueries({	
	@NamedQuery(name  = "programProcessor.unmarkTriplesMarkedForRound",
				query = "update KiWiTriple t " +
				        "set t.reasoningRound = 0 "+
						"where t.reasoningRound = :round"),
	@NamedQuery(name  = "programProcessor.markTriplesForRoundById",
			query = "update KiWiTriple t " +
			        "set t.reasoningRound = :round "+
					"where t.id IN (:tripleIds)"),
	@NamedQuery(name  = "tripleStore.deleteTriple",
			query = "update KiWiTriple t " +
			        "set t.deleted = true "+
					"where t.id = :id"),
	@NamedQuery(name  = "tripleStore.deleteTripleIfNotBase",
			query = "update KiWiTriple t " +
			        "set t.deleted = true "+
					"where t.id = :id and t.base = false"),
	@NamedQuery(name  = "tripleStore.deleteTriples",
				query = "update KiWiTriple t " +
				        "set t.deleted = true "+
						"where t.id in (:ids)"),
	@NamedQuery(name  = "tripleStore.deleteTriplesIfNotBase",
			query = "update KiWiTriple t " +
			        "set t.deleted = true "+
					"where t.id in (:ids) and t.base = false"),
	@NamedQuery(name  = "tripleStore.undeleteTriple",
				query = "update KiWiTriple t " +
				        "set t.deleted = false "+
						"where t.id = :id"),
	@NamedQuery(name  = "tripleStore.undeleteTriples",
				query = "update KiWiTriple t " +
				        "set t.deleted = false "+
						"where t.id in (:ids)"),
	@NamedQuery(name  = "tripleStore.allTriples",
				query = "from KiWiTriple t " +
						"where t.deleted = false"),
	@NamedQuery(name  = "tripleStore.inferredTriples",
				query = "from KiWiTriple t " +
						"where t.deleted = false" +
						" and t.inferred = true"),
	@NamedQuery(name  = "tripleStore.inferredTriplesCount",
				query = "select count (t) from KiWiTriple t " +
						"where t.deleted = false" +
						" and t.inferred = true"),
	@NamedQuery(name  = "tripleStore.baseTriples",
				query = "from KiWiTriple t " +
						"where t.deleted = false" +
						" and t.inferred = false"),
	@NamedQuery(name  = "tripleStore.baseTriplesCount",
				query = "select count(t) from KiWiTriple t " +
						"where t.deleted = false" +
						" and t.inferred = false"),
	@NamedQuery(name  = "tripleStore.markedTriples",
				query = "from KiWiTriple t " +
						"where t.deleted = false" +
						" and t.marked = true"),
	@NamedQuery(name  = "tripleStore.tripleByS",
				query = "from KiWiTriple t " +
					    "join fetch t.property "+
					    "join fetch t.object "+
						"where t.subject = :subject" +
						"  and t.deleted = false"),
	@NamedQuery(name  = "tripleStore.tripleBySincludeDeleted",
				query = "from KiWiTriple t " +
						"join fetch t.property "+
						"join fetch t.object "+
						"where t.subject = :subject"),
	@NamedQuery(name  = "tripleStore.tripleByP",
			query = "from KiWiTriple t " +
				    "join fetch t.subject "+
				    "join fetch t.object "+
					"where t.property = :property" +
					"  and t.deleted = false"),
	@NamedQuery(name  = "tripleStore.tripleByPu",
			query = "from KiWiTriple t " +
				    "join fetch t.subject "+
				    "join fetch t.object "+
					"where t.property.uri = :property_uri" +
					"  and t.deleted = false"),
	@NamedQuery(name  = "tripleStore.tripleByPuOu",
			query = "from KiWiTriple t " +
				    "join fetch t.subject "+
				    "join fetch t.object "+
					"where t.property.uri = :property_uri" +
					" and t.object.uri = :object_uri" +
					"  and t.deleted = false"),
	@NamedQuery(name  = "tripleStore.prefetchTripleByS",
			    query = "select t.property, l "+
				        "from KiWiTriple t,  KiWiLiteral l " +
				    	"where t.subject = :subject" +
				    	"  and t.object.id = l.id"+
				    	"  and t.deleted = false"),
	@NamedQuery(name  = "tripleStore.tripleByO",
				query = "from KiWiTriple t " +
						"where t.object = :object" +
						"  and t.deleted = false"),
	@NamedQuery(name  = "tripleStore.tripleBySP",
				query = "from KiWiTriple t " +
						"where t.subject = :subject" +
						"  and t.property = :property" +
						"  and t.deleted = false"),
	@NamedQuery(name  = "tripleStore.tripleBySP2",
				query = "from KiWiTriple t " +
			    		"join fetch t.object "+
					    "join fetch t.property "+
						"where t.subject = :subject" +
						"  and t.property.uri = :property_uri" +
						"  and t.deleted = false"),
	@NamedQuery(name  = "tripleStore.tripleBySP2includeDeleted",
				query = "from KiWiTriple t " +
			    		"join fetch t.object "+
			    		"join fetch t.property "+
			    		"where t.subject = :subject" +
						"  and t.property.uri = :property_uri"),
	@NamedQuery(name  = "tripleStore.tripleByPO",
				query = "from KiWiTriple t " +
						"where t.object = :object" +
						"  and t.property = :property" +
						"  and t.deleted = false"),
	@NamedQuery(name  = "tripleStore.tripleByPO2",
				query = "from KiWiTriple t " +
						"where t.object = :object" +
						"  and t.property.uri = :property_uri" +
						"  and t.deleted = false"),
	@NamedQuery(name  = "tripleStore.tripleBySPO",
				query = "from KiWiTriple t " +
//				        "left join fetch t.subject left join fetch t.property left join fetch t.object " +
						"where t.subject = :subject" +
						"  and t.property = :property" +
						"  and t.object = :object" +
						"  and t.deleted = false"),
	@NamedQuery(name  = "tripleStore.tripleIdBySPO",
			query = "select t.id from KiWiTriple t " +
					"where t.subject = :subject" +
					"  and t.property = :property" +
					"  and t.object = :object" +
					"  and t.deleted = false"),
	@NamedQuery(name  = "tripleStore.tripleBySPuO",
				query = "from KiWiTriple t " +
						"where t.subject = :subject" +
						"  and t.property.uri = :property_uri" +
						"  and t.object = :object" +
						"  and t.deleted = false"),
	@NamedQuery(name  = "tripleStore.tripleBySPOC",
				query = "from KiWiTriple t " +
						"where t.subject = :subject" +
						"  and t.property = :property" +
						"  and t.object = :object" +
						"  and t.context = :context" +
						"  and t.deleted = false"),
	@NamedQuery(name  = "tripleStore.literalBySubjectProperty",
			    query = "select l " +
			    		"from KiWiLiteral l," +
			    		"     KiWiTriple  t " +
			    		"where t.object = l" +
			    		"  and t.property = :property" +
			    		"  and t.subject   = :subject" +
						"  and t.deleted = false"),
	@NamedQuery(name  = "tripleStore.literalBySubjectPropertyLocale",
			    query = "select l " +
			    		"from KiWiLiteral l," +
			    		"     KiWiTriple  t " +
			    		"where t.object = l" +
			    		"  and t.property = :property" +
			    		"  and t.subject   = :subject" +
			    		"  and l.language  = :locale" +
						"  and t.deleted = false"),
	// find the literal for a certain combination of subject and property, 
	// and - optionally - locale
	@NamedQuery(name  = "tripleStore.literal2BySubjectProperty",
			    query = "select l " +
			    		"from KiWiLiteral l," +
			    		"     KiWiTriple  t " +
			    		"where t.object = l" +
			    		"  and t.property.uri = :property_uri" +
			    		"  and t.subject   = :subject" +
						"  and t.deleted = false"),
	@NamedQuery(name  = "tripleStore.literal2BySubjectPropertyLocale",
			    query = "select l " +
			    		"from KiWiLiteral l," +
			    		"     KiWiTriple  t " +
			    		"where t.object = l" +
			    		"  and t.property.uri = :property_uri" +
			    		"  and t.subject   = :subject" +
			    		"  and l.language  = :locale" +
						"  and t.deleted = false"),
	// find triples with a certain subject and property and a literal as object
	// similar to previous two queries but retrieves complete triple instead of only literal
	@NamedQuery(name  = "tripleStore.literalTripleBySubjectProperty",
			    query = "select t " +
			    		"from KiWiLiteral l," +
			    		"     KiWiTriple  t " +
			    		"where t.object = l" +
			    		"  and t.property.uri = :property_uri" +
			    		"  and t.subject   = :subject" +
						"  and t.deleted = false"),
	@NamedQuery(name  = "tripleStore.literalTripleBySubjectPropertyLocale",
			    query = "select t " +
			    		"from KiWiLiteral l," +
			    		"     KiWiTriple  t " +
			    		"where t.object = l" +
			    		"  and t.property.uri = :property_uri" +
			    		"  and t.subject   = :subject" +
			    		"  and l.language  = :locale" +
						"  and t.deleted = false"),
	// counting should be more efficient than retrieval (less characters to read) and in many
	// cases the count will be 0, so we use the following two queries to decide whether it is
	// necessary to retrieve the triple at all
	@NamedQuery(name  = "tripleStore.countLiteralTripleBySubjectProperty",
			    query = "select count(t.id) " +
			    		"from KiWiLiteral l," +
			    		"     KiWiTriple  t " +
			    		"where t.object = l" +
			    		"  and t.property.uri = :property_uri" +
			    		"  and t.subject   = :subject" +
						"  and t.deleted = false"),
	@NamedQuery(name  = "tripleStore.countLiteralTripleBySubjectPropertyLocale",
			    query = "select count(t.id) " +
			    		"from KiWiLiteral l," +
			    		"     KiWiTriple  t " +
			    		"where t.object = l" +
			    		"  and t.property.uri = :property_uri" +
			    		"  and t.subject   = :subject" +
			    		"  and l.language  = :locale" +
						"  and t.deleted = false"),
	@NamedQuery(name  = "tripleStore.tripleBySPO+Deleted",
				query = "from KiWiTriple t " +
						"where t.subject = :subject" +
						"  and t.property = :property" +
						"  and t.object = :object"),
	@NamedQuery(name  = "tripleStore.tripleBySPOC+Deleted",
				query = "from KiWiTriple t " +
						"where t.subject = :subject" +
						"  and t.property = :property" +
						"  and t.object = :object" +
						"  and t.context = :context"),
	@NamedQuery(name  = "tripleStore.subjectByProperty",
				query = "select t.subject.contentItem " +
					    "from KiWiTriple t " +
						"where t.property.uri = :prop_uri" +
						"  and t.deleted = false"),
	@NamedQuery(name  = "tripleStore.objectByProperty",
				query = "select t.object.contentItem " +
					    "from KiWiTriple t " +
						"where t.property.uri = :prop_uri" +
						"  and t.deleted = false"),
	@NamedQuery(name  = "tripleStore.objectBySubjectProperty",
				query = "select t.object.contentItem " +
					    "from KiWiTriple t " +
						"where t.property.uri = :prop_uri" +
						"  and t.subject = :subj" +
						"  and t.deleted = false"),
	@NamedQuery(name  = "tripleStore.hasType",
				query = "select t.id " +
					    "from KiWiTriple t " +
						"where t.property.uri = '" + Constants.NS_RDF + "type' " +
						"  and t.object = :type " +
						"  and t.subject = :subject " +
						"  and t.deleted = false"),
	@NamedQuery(name  = "tripleStore.getTypes",
				query = "select t.object " +
					    "from KiWiTriple t " +
						"where t.property.uri = '" + Constants.NS_RDF + "type' " +
						"  and t.subject = :subject " +
						"  and (t.object.class = kiwi.model.kbase.KiWiUriResource or t.object.class = kiwi.model.kbase.KiWiAnonResource) " +
						"  and t.deleted = false"),
	@NamedQuery(name  = "tripleStore.subjectByType",
				query = "select t.subject.contentItem " +
					    "from KiWiTriple t " +
						"where t.property.uri = '" + Constants.NS_RDF + "type' " +
						"  and t.object = :type " +
						"  and t.deleted = false"),
	@NamedQuery(name  = "tripleStore.subjectByTypeUri",
				query = "select t.subject.contentItem " +
					    "from KiWiTriple t " +
						"where t.property.uri = '" + Constants.NS_RDF + "type' " +
						"  and t.object.uri = :type_uri " +
						"  and t.deleted = false"),

						// TODO When using an ontology that makes use of blank nodes, 
						// the list of types does not show anything. (see KIWI-159 in Jira)
	@NamedQuery(name  = "ontologyService.listClasses",
				query = "select t.subject.contentItem " +
					    "from KiWiTriple t " +
						"where t.property.uri = '" + Constants.NS_RDF + "type' " +
						"  and (  t.object.uri = '" + Constants.NS_RDFS + "Class'  " +
						"      or t.object.uri = '" + Constants.NS_OWL + "Class' ) " +
						"  and t.deleted = false " +
						"  and t.subject.contentItem.title is not null " +
						" order by t.subject.contentItem.title asc"),
						
	@NamedQuery(name="tagCloudService.aggregateInferredTagsByContentItem",
			query="select t3.object, " +
					"     count(t.subject) " +
				  "from KiWiTriple t, KiWiTriple t2, KiWiTriple t3 " +
				  "where t.object = :uri " +
				  "  and t.property = 'http://www.holygoat.co.uk/owl/redwood/0.1/tags/taggedResource' " +
				  "  and t.subject = t2.subject " +
				  "  and t2.property = 'http://www.holygoat.co.uk/owl/redwood/0.1/tags/associatedTag' " +
				  "  and t2.object = t3.subject " +
				  "  and t3.property = 'http://www.kiwi-project.eu/kiwi/core/title' " +
				  "  and t.deleted = false and t2.deleted = false and t3.deleted = false " +
				  "  and t.inferred = true and t2.inferred = true and t3.inferred = true " +
				  "group by t3.object " +
				  "order by t3.object ")
})
public class KiWiTriple  implements Serializable {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -8726615974625660845L;
	
	@Id
	@NotNull
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Long id;
	

	@NotNull
	@Immutable
	@ManyToOne(fetch = FetchType.EAGER, optional=false)	
	@Index(name="idx_triple_subject")
	@Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
    private KiWiResource    subject;
    
	@NotNull
	@Immutable
	@ManyToOne(fetch = FetchType.EAGER, optional=false)
	@Index(name="idx_triple_property")
	@Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
    private KiWiUriResource property;
	
	@NotNull
	@Immutable
	@ManyToOne(fetch = FetchType.EAGER, optional=false)
	@Index(name="idx_triple_object")
	@Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
    private KiWiNode        object;
    
	@Immutable
	@ManyToOne(fetch = FetchType.LAZY, optional=true)
	@Index(name="idx_triple_context")
    private KiWiUriResource context;
    
	@Immutable
	@ManyToOne(fetch = FetchType.LAZY, optional=true)
    private User  author;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
    
	private Boolean deleted;
	
	private Boolean inferred;

	private Boolean base;
	
	private Boolean marked;

	@Transient
	private Boolean newTriple;
	
	@Index(name="idx_reasoning_round")
	private Integer reasoningRound;
	
	/**
	 *  Coming from a subject's text content (such as RDFa, or links)
	 *  All contentBased triples should be deleted and recreated by savelets.
	 *  This is needed for consistency, savelets otherwise cannot know which 
	 *  triples should be deleted when RDFa changes in the editor...
	 *  
	 *  TODO: contentBasedIncoming, if needed (coming from object's text content... such as "rev" attributes in RDFa)
	 *  (maybe instead of a boolean, have this mechanism even more general and store a 
	 *  kiwiid of the responsible content item?)
	 */
	private Boolean contentBasedOutgoing;
	
	@Version
	private Long version;
	
	
	public KiWiTriple() {
		this.created = new Date();
		this.deleted = false;
		this.inferred = false;
		this.base = true;
		this.newTriple = true;
		this.contentBasedOutgoing = false;
	}
	

	public KiWiTriple(KiWiResource subject, KiWiUriResource property, KiWiNode object) {
		this(subject,property,object,false);
	}

	public KiWiTriple(KiWiResource subject, KiWiUriResource property, KiWiNode object, boolean _inferred) {
		this(subject,property,object,null, _inferred);
	}
	
	public KiWiTriple(KiWiResource subject, KiWiUriResource property, KiWiNode object, KiWiUriResource context) {
		this(subject,property,object,context, false);
	}
	
	public KiWiTriple(KiWiResource subject, KiWiUriResource property, KiWiNode object, KiWiUriResource context, boolean _inferred) {
		this();
		this.subject = subject;
		this.property = property;
		this.object   = object;
		this.context = context;
		this.inferred = _inferred;
	}

   /**
     * Get the object of this extended triple.
     * @return
     */
    public KiWiNode getObject() {
        return object;
    }

    /**
     * Set the object of this extended triple to the given KiWiNode (either a resource or a literal)
     * @param object
     */
    public void setObject(KiWiNode object) {
        this.object = object;
    }

    /**
     * Get the property of this extended triple. Always a KiWiUriResource.
     * @return
     */
    public KiWiUriResource getProperty() {
        return property;
    }

    /**
     * Set the property of this extended triple. Always needs to be a KiWiUriResource
     * @param property
     */
    public void setProperty(KiWiUriResource property) {
        this.property = property;
    }

    /**
     * Get the subject of this extended triple. Always a resource.
     * @return
     */
    public KiWiResource getSubject() {
        return subject;
    }

    /**
     * Set the subject of this extended triple to the provided KiWiResource
     * @param subject
     */
    public void setSubject(KiWiResource subject) {
        this.subject = subject;
    }

    /**
     * Get the unique triple identifier of this extended triple. Returns a KiWiUriResource identifying this triple.
     * @return
     */
    public KiWiUriResource getContext() {
        return context;
    }

    /**
     * Set the unique triple identifier of this extended triple to the provided KiWiUriResource. The caller needs
     * to ensure that the tripleId is unique over the KiWi system; otherwise, the system might not function correctly.
     * @param context
     */
    public void setContext(KiWiUriResource context) {
        this.context = context;
    }

    
    /**
     * Return the author of this extended triple.
     * 
     * Internally, this is determined using the tripleId of the extended triple and looking it up in the 
     * database.
     * 
     * @return
     */
    public User getAuthor() {
        return author;
    }
    
    /**
     * Set the author of this extended triple.
     * 
     * Changes will be persisted as part of the database using the tripleId as unique identifier.
     * 
     * @param author
     */
    public void setAuthor(User author) {
        this.author = author;
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
	public Boolean isDeleted() {
		return deleted;
	}


	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 * @return the inferred
	 */
	public Boolean isInferred() {
		return inferred;
	}


	/**
	 * @param inferred the inferred to set
	 */
	public void setInferred(Boolean inferred) {
		this.inferred = inferred;
	}
	
	/**
	 * @return the base
	 */
	public Boolean isBase() {
		return base;
	}


	/**
	 * @param base the base to set
	 */
	public void setBase(Boolean base) {
		this.base = base;
	}
	
	

	/** Used by reasoning to implement semi-naive fw-chaining.
	 * 
	 * 
	 * @return the mark
	 */
	public Boolean getMarked() {
		return marked;
	}


	/** Used by reasoning to implement semi-naive fw-chaining.
	 * 
	 * 
	 * @param marked
	 */
	public void setMarked(Boolean marked) {
		this.marked = marked;
	}


	/**
	 * TODO: please comment why that method is necessary
	 * 
	 */
	public boolean isNew() {
		return newTriple;
	}
	
	/**
	 * TODO: please comment why that method is necessary
	 * @param isNew
	 */
	public void setNew(boolean isNew) {
		newTriple = isNew;
	}
	
	public Boolean isContentBasedOutgoing() {
		return contentBasedOutgoing;
	}
	
	public void setContentBasedOutgoing(Boolean contentBasedOutgoing) {
		this.contentBasedOutgoing = contentBasedOutgoing;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((context == null) ? 0 : context.hashCode());
//		result = prime * result + ((deleted == null) ? 0 : deleted.hashCode());
		result = prime * result + ((object == null) ? 0 : object.hashCode());
		result = prime * result + ((property == null) ? 0 : property.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
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
		if (! (obj instanceof KiWiTriple) )
			return false;
		KiWiTriple other = (KiWiTriple) obj;
		if (context == null) {
			if (other.context != null)
				return false;
		} else if (!context.equals(other.context))
			return false;
//		if (deleted != other.deleted)
//			return false;
		if (object == null) {
			if (other.object != null)
				return false;
		} else if (!object.equals(other.object))
			return false;
		if (property == null) {
			if (other.property != null)
				return false;
		} else if (!property.equals(other.property))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		return true;
	}
	
	public String toRDFXML() {
		return "<"+subject.toString()+"> <" + property.toString() + "> <" + object.toString() + ">";
	}


    @Override
    public String toString() {
    	if(context != null) {
    		return "{"+subject.toString()+" "+property.toString()+" "+object.toString()+"}@"+context.toString();
    	} else {
    		return "{"+subject.toString()+" "+property.toString()+" "+object.toString()+"}@GLOBAL";   		
    	}
    }
    
    /**
     * Return a unique key to be used in caches and similar.
     * 
     * @return
     */
    public String getKey() {
    	return toString();
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


	public Integer getReasoningRound() {
		return reasoningRound;
	}


	public void setReasoningRound(Integer reasoningRound) {
		this.reasoningRound = reasoningRound;
	}
	
	@PostLoad
	public void postLoad() {
		this.newTriple = false;
	}
}
