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
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.OneToOne;
import javax.persistence.Query;
import javax.persistence.Transient;

import kiwi.api.query.sparql.SparqlService;
import kiwi.api.triplestore.TripleStore;
import kiwi.exception.NamespaceResolvingException;
import kiwi.exception.NonUniqueRelationException;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.jboss.seam.Component;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;

/**
 * KiWiResources correspond to RDF resources. A KiWi resource is either an anonymous 
 * resource or a URIResource. A KiWiResource represents an RDF resource. However, there
 * is no 1:1 correspondance, as there may be several KiWiResources in different
 * KnowledgeSpaces that represent the same RDF resource.
 * <p>
 * Each KiWiResource has a 1:1 correspondance to a content item. Each KiWiResource is 
 * also directly related with a Sesame 2 resource in the knowledge base backend. 
 * The kiwi.sesame package contains appropriate adaptors for transforming Sesame 2 
 * resources into KiWiResources.
 * <p>
 * 
 * Each KiWiResoure furthermore participates in exactly one knowledge space, where the RDF 
 * resource may have different identifiers for the users. This knowledge space defines the 
 * getContext().of the node. For this reason, a Sesame 2 Value may correspond to several 
 * KiWiNodes in different knowledge spaces.
 *
 * @author Sebastian Schaffert
 */
@Entity
@DiscriminatorValue("r")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@BatchSize(size = 100)
@NamedQueries({
	@NamedQuery(name  = "tripleStore.resourceByUri", 
			    query = "from KiWiUriResource r " +
			    		"left join fetch r.contentItem " +
			    		"where r.uri = :uri"),
    @NamedQuery(name  = "tripleStore.resourceByAnonId",
    	        query = "from KiWiAnonResource r " +
    	        		"left join fetch r.contentItem " +
    	        		"where r.anonId = :anonId")
})
@Restrict
public abstract class KiWiResource extends KiWiNode implements Serializable {
    
	private static final long serialVersionUID = 1L;

    @OneToOne(fetch=FetchType.EAGER)
    @Cascade({CascadeType.PERSIST})
    @Index(name="contentitemid_index")
    @Fetch(FetchMode.JOIN)
    @LazyToOne(LazyToOneOption.FALSE)
    protected ContentItem contentItem;
    
    /**
     * Cache for literal property values.
     */
	@Transient
	private HashMap<String,KiWiLiteral> properties;
	
	/**
	 * Cache for properties that are undefined. If property is undefined in rdf, store "TRUE".
	 */
	@Transient
	private HashMap<String,Boolean> undef_properties;
	
	
	/*
	 * Indicate whether a prefetch run for properties has already been executed.
	 */
	@Transient
	private boolean prefetched = false;
	
	/**
	 * Create a new empty KiWiResource; called by subclasses
	 */
    protected KiWiResource() {
        super();
        properties = new HashMap<String,KiWiLiteral>();
        undef_properties = new HashMap<String, Boolean>();
    }
     
    
    /**
     * Get the content item associated with this KiWiResource in the knowledge space "getContext()..
     * CHANGED because of persistence conflicts - contentItem couldn't 
     * be persisted because there didn't exist any at this time
     * @return
     */
    public ContentItem getContentItem() {
        return contentItem;
    }

    /**
     * Set the content item associated with this KiWiResource.
     * CHANGED because of persistence conflicts - contentItem couldn't 
     * be persisted because there didn't exist any at this time
     * @param contentItem
     */
    public void setContentItem(ContentItem contentItem) {
        this.contentItem = contentItem;
    }
    
    /**
     * Return the knowledge space that is the getContext().of this node.
     * @return
     */
    protected static final TripleStore getContext() {
    	return (TripleStore) Component.getInstance("tripleStore");
    	
    }

 

    /**
     * Generic method to query for literal values related to this resource with the property
     * identified by "propLabel" (SeRQL/SPARQL short or long notation). Returns only literal
     * values for which no language has been assigned.
     * 
     * @param propLabel
     * @return
     * @throws NonUniqueRelationException 
     */
    public String getProperty(String propLabel) throws NonUniqueRelationException {
        return getProperty(propLabel,null);              
    }

    /**
     * Generic method to query for literal values related to this resource with the property
     * identified by "propLabel" (SeRQL/SPARQL short or long notation) and the given locale.
     * 
     * @param propLabel label of the property; either RDF short form (e.g. "foaf:mbox") or long form (e.g. <http://xmlns.com/foaf/0.1/mbox>)
     * @param loc
     * @return
     * @throws NonUniqueRelationException 
     */
    public String getProperty(String propLabel, Locale loc)  {  	
    	KiWiLiteral l;
		try {
			l = getLiteral(propLabel,loc);
			if(l == null) {
				return null; 
			} else {
				return l.getContent();
			}
		} catch (NamespaceResolvingException e) {
			e.printStackTrace();
		}
		return null;
    }
    
    
    /**
     * Generic method to query for literal values related to this resource with the property
     * identified by "propLabel" (SeRQL/SPARQL short or long notation) and the given locale.
     * 
     * @param propLabel label of the property; either RDF short form (e.g. "foaf:mbox") or long form (e.g. <http://xmlns.com/foaf/0.1/mbox>)
     * @return
     * @throws NamespaceResolvingException 
     */
    public Iterable<String> getProperties(String propLabel) throws NamespaceResolvingException {
    	return getProperties(propLabel,null);
    }

	/**
	 * Generic method to query for literal values related to this resource with the property
	 * identified by "propLabel" (SeRQL/SPARQL short or long notation) and the given locale.
	 * 
	 * @param propLabel label of the property; either RDF short form (e.g. "foaf:mbox") or long 
	 *        form (e.g. <http://xmlns.com/foaf/0.1/mbox>)
	 * @param loc
	 * @return
	 * @throws NamespaceResolvingException 
	 */
	public Iterable<String> getProperties(String propLabel, Locale loc) throws NamespaceResolvingException {
		List<KiWiLiteral> props = listLiterals(propLabel, loc);
		List<String> results = new ArrayList<String>(props.size());
		
		for(KiWiLiteral l : props) {
			results.add(l.getContent());
		}
		
		return results;
	}

    /**
     * Generic method to set the literal value of a property of this resource to the provided
     * value without setting a language.
     * 
     * @param propLabel  the SeRQL or SPARQL short or long notation for the property
     * @param propValue  the String value of this property
     * @throws NamespaceResolvingException 
     */
    public void setProperty(String propLabel, String propValue) throws NamespaceResolvingException {
        setProperty(propLabel,propValue,null);
    }

    /**
     * Generic method to set the literal value of a property of this resource to the provided
     * value in the provided language.
     * 
     * @param propLabel  the SeRQL or SPARQL short or long notation for the property
     * @param propValue  the String value of this property
     * @param loc        the Locale representing the language of this property
     * @throws NamespaceResolvingException 
     */
    public <T> void setProperty(String propLabel, T propValue, Locale loc) throws NamespaceResolvingException {

    	String key = propLabel + (loc == null?"":"@"+loc.getLanguage());

    	if(propValue != null) {
	    	// quick check whether the cached value is the same as the passed value; in this case we
	    	// do nothing because no update is needed
	    	if(properties.get(key) != null && 
	    	   properties.get(key).getContent().equals(propValue)) {
	    		return;
	    	}
	    	
	    	// remove previous property setting
	    	removeProperty(propLabel,loc);

	    	TripleStore ts = getContext();
	    	
	    	String prop_uri = _resolvePropLabel(propLabel);
	    	
	        // then set the new property value
	        //KiWiUriResource propResource = ts.createUriResource(prop_uri);
	        KiWiLiteral value = ts.createLiteral(propValue, loc, getContext().getXSDType(propValue.getClass()));
	        ts.createTriple(this, prop_uri, value);
	
	        // and update properties cache
	        properties.put(key, value);        
	        undef_properties.put(key, Boolean.FALSE);
    	} else {
    		removeProperty(propLabel,loc);
    		
    	}
    }
    
    public boolean removeProperty(String propLabel) throws NamespaceResolvingException {
    	return removeProperty(propLabel,null);
    }
    
    /**
     * Remove a property from the KiWiResource.
     * 
     * @param propLabel the property label in SeRQL syntax to remove
     * @param loc the locale of the property to remove
     * @return true if the property existed and was removed
     * @throws NamespaceResolvingException 
     */
    public boolean removeProperty(String propLabel, Locale loc) throws NamespaceResolvingException {
    	
    	
    	String uri = _resolvePropLabel(propLabel);

		EntityManager em = (EntityManager) Component.getInstance("entityManager");
   		
		// check whether property exists by simply counting (should be much more efficient than retrieving) 
		long count = 0;
		
		Query q1 = em.createNamedQuery("tripleStore.countLiteralTripleBySubjectProperty"+(loc != null?"Locale":""));
		q1.setParameter("subject", this);
		q1.setParameter("property_uri",uri);
		if(loc != null) {
			q1.setParameter("locale",loc);
		}
		try {
			count = (Long) q1.getSingleResult();
		} catch(NoResultException ex) {

		} catch(NonUniqueResultException ex) {

		}
		
    	// look up triple that corresponds to property
    	KiWiTriple triple = null;
    	
		// when there is such a triple, retrieve it and then add it to the list of triples to be removed
		if(count > 0) {
			Query q2 = em.createNamedQuery("tripleStore.literalTripleBySubjectProperty"+(loc != null?"Locale":""));
			q2.setHint("org.hibernate.cacheable", true);
			q2.setParameter("subject", this);
			q2.setParameter("property_uri",uri);
			q2.setMaxResults(1);
			if(loc != null) {
				q2.setParameter("locale",loc);
			}
	
			try {
				triple = (KiWiTriple) q2.getSingleResult();
			} catch(NoResultException ex) {
	
			} catch(NonUniqueResultException ex) {
	
			}
		}
    	
    	// if triple exists, call TripleStore.remove on it
		if(triple != null) {
			getContext().removeTriple(triple);
			
	   		// invalidate property cache
        	String key = propLabel + (loc == null?"":"@"+loc.getLanguage());
        	properties.remove(key);
        	undef_properties.put(key, Boolean.TRUE);
        	
        	return true;
		} else {
			return false;
		}
    }

        
    /* incoming and outgoing edges (KiWiTriple) */
    
    /**
     * List all outgoing edges from this resource to other resources. Shortcut for listOutgoing(null).
     * 
     * @return all outgoing edges from this resource 
     */
    public Collection<KiWiTriple> listOutgoing() {
    	try {
			return listOutgoing(null);
		} catch (NamespaceResolvingException e) {
			e.printStackTrace();
			return Collections.emptySet();
		}
    }
    
    public Collection<KiWiTriple> listOutgoingIncludeDeleted() {
    	try {
			return listOutgoing(null, -1, true);
		} catch (NamespaceResolvingException e) {
			e.printStackTrace();
			return Collections.emptySet();
		}
    }    

    /**
     * List all outgoing edges from this resource to other resources, using the property label passed
     * as argument. 
     * 
     * @param propLabel
     * @return
     * @throws NamespaceResolvingException 
     */
    public Collection<KiWiTriple> listOutgoing(String propLabel) throws NamespaceResolvingException {
		return listOutgoing(propLabel, -1, false);
    }
    
    
    /**
     * List outgoing edges from this resource to other resources, using the property label passed
     * as argument. If limit is bigger than 0, then a maximum of limit triples will be returned.
     * Otherwise, all triples will be returned.
     * <p>
     * The parameter propLabel is in the form of a SeRQL or SPARQL id. It can take one of the following
     * values:
     * <ul>
     * <li>a URI enclosed in &lt; &gt, e.g. &lt;http://www.example.com/myProp&gt;</li>
     * <li>a uri prefix, followed by a colon and the property name, e.g. ex:myProp</li>
     * <li>the value "null", in which case all outgoing edges are listed regardless of their label
     * (wildcard)</li>
     * </ul>
     * The result will be an iterable that allows to iterate over KiWiTriples.
     * 
     * @param propLabel the label of the property to be queried, or null for wildcard
     * @param limit the maximum number of triples to retrieve
     * @return an iterable over the KiWiTriples that are outgoing edges of this resource
     * @throws NamespaceResolvingException 
     */
    public Collection<KiWiTriple> listOutgoing(String propLabel, int limit, boolean includeDeleted) throws NamespaceResolvingException {
    	Log log = Logging.getLog(KiWiResource.class);
    	if(this.getId() == null) {
    		log.warn("PLEASE AVOID: calling listOutgoing() on Sesame triplestore, since resource is not yet in database");
    		
	        throw new UnsupportedOperationException("listOutgoing() no longer supports non-persistent resources");
    	} else {
    		// resource is already a persisted entity
    		// perform entityManager query ...
    		
	        EntityManager em = (EntityManager) Component.getInstance("entityManager");
	        //TripleStore   ts = (TripleStore)   Component.getInstance("tripleStore");
	        
	        Query q;
	        if(includeDeleted == false)
	        {
	        	q = em.createNamedQuery("tripleStore.tripleByS"+(propLabel != null?"P2":""));
	        }
	        else
	        {
	        	q = em.createNamedQuery("tripleStore.tripleByS"+(propLabel != null?"P2":"") + "includeDeleted");
	        }
	        
			q.setHint("org.hibernate.cacheable", true);
	    	q.setParameter("subject", this);
	    	if(propLabel != null) {
		        String uri = _resolvePropLabel(propLabel);
		    	log.debug("querying property with uri #0", uri);
		    	//KiWiUriResource property = ts.createUriResource(uri);
	    		q.setParameter("property_uri",uri);
	    	}
	    	if(limit > 0) {
	    		q.setMaxResults(limit);
	    	}
	    	
	    	return (List<KiWiTriple>) q.getResultList();
    	}
 	}
    

    
    
    

	/**
	 * List the objects that are related to this resource through a certain property
	 * @return
	 * @throws NamespaceResolvingException 
	 */
	public Iterable<KiWiNode> listOutgoingNodes(String propLabel) throws NamespaceResolvingException {
		Collection<KiWiTriple> out = listOutgoing(propLabel);
		List<KiWiNode> result = new ArrayList<KiWiNode>(out.size());
		for(KiWiTriple t : out) {
			result.add(t.getObject());
		}
		return result;
	}

    // CHECK
    public <C extends KiWiNode> void setOutgoingNode(String propLabel, C target) throws NamespaceResolvingException {
    	TripleStore ts = getContext();

    	// remove all existing triples
    	for( KiWiTriple t : listOutgoing(propLabel)) {
    		ts.removeTriple(t);
    	}
    	KiWiUriResource propResource = ts.createUriResourceBySPARQLId(propLabel);
    	ts.createTriple(this, propResource, target);
    }
    
    


    public void addOutgoingNode(String propLabel, KiWiResource target) throws NamespaceResolvingException {
    	TripleStore ts = getContext();
    	
    	String property_uri = _resolvePropLabel(propLabel);
        ts.createTriple(this, property_uri, target);
    	
     	
//    	KiWiUriResource propResource = ts.createUriResourceBySPARQLId(propLabel);
//    	ts.createTriple(this, propResource, target);      
    }
    
    public void addOutgoingNode(KiWiUriResource prop, KiWiNode target) throws NamespaceResolvingException {
    	TripleStore ts = getContext();
    	ts.createTriple(this, prop, target);
    }
    
    
    
    public void removeOutgoingNode(String propLabel, KiWiResource target) throws NamespaceResolvingException {
    	TripleStore ts = getContext();

    	String property_uri = _resolvePropLabel(propLabel);
        ts.removeTriple(this, property_uri, target);
    	
 
    	
//    	KiWiUriResource propResource = ts.createUriResourceBySPARQLId(propLabel);
//    	ts.removeTriple(this, propResource, target);
    }
    
    
    
    /**
     * List all incoming edges from other resources to this resource
     * @return
     * @throws NamespaceResolvingException 
     */
    public Collection<KiWiTriple> listIncoming() {
    	try {
			return listIncoming(null);
		} catch (NamespaceResolvingException e) {
			e.printStackTrace();
			return Collections.emptySet();
		}
    }
    
    /**
     * List incoming edges from other resources to this resource, using the property label passed
     * as argument.
     * @throws NamespaceResolvingException 
     */
    public Collection<KiWiTriple> listIncoming(String propLabel) throws NamespaceResolvingException {
    	return listIncoming(propLabel, -1);
    }
    
    /**
     * List incoming edges from other resources to this resource, using the property label passed
     * as argument. If limit is bigger than 0, then a maximum of limit triples will be returned.
     * Otherwise, all triples will be returned.
     * <p>
     * The parameter propLabel is in the form of a SeRQL or SPARQL id. It can take one of the following
     * values:
     * <ul>
     * <li>a URI enclosed in &lt; &gt, e.g. &lt;http://www.example.com/myProp&gt;</li>
     * <li>a uri prefix, followed by a colon and the property name, e.g. ex:myProp</li>
     * <li>the value "null", in which case all outgoing edges are listed regardless of their label
     * (wildcard)</li>
     * </ul>
     * The result will be an iterable that allows to iterate over KiWiTriples.
     * 
     * @param propLabel the label of the property to be queried, or null for wildcard
     * @param limit the maximum number of triples to retrieve
     * @return an iterable over the KiWiTriples that are incoming edges of this resource
     * @throws NamespaceResolvingException 
     */
     public Collection<KiWiTriple> listIncoming(String propLabel, int limit) throws NamespaceResolvingException {
     	Log log = Logging.getLog(KiWiResource.class);
    	if(this.getId() == null) {
    		log.warn("PLEASE AVOID: calling listIncoming() on Sesame triplestore, since resource is not yet in database");
	        
    		throw new UnsupportedOperationException("listIncoming() no longer supports non-persistent resources");
    	} else {
    		// resource is already a persisted entity
    		// perform entityManager query ...
    		
	        EntityManager em = (EntityManager) Component.getInstance("entityManager");
	        //TripleStore   ts = (TripleStore)   Component.getInstance("tripleStore");
	
	    	Query q = em.createNamedQuery("tripleStore.tripleBy"+(propLabel != null?"PO2":"O"));
			q.setHint("org.hibernate.cacheable", true);
	    	q.setParameter("object", this);
	    	if(propLabel != null) {
		        String uri = _resolvePropLabel(propLabel);
		    	log.debug("querying property with uri #0", uri);
		    	//KiWiUriResource property = ts.createUriResource(uri);
	    		q.setParameter("property_uri",uri);	    		
	    	}
	    	if(limit > 0) {
	    		q.setMaxResults(limit);
	    	}
	    	
    		return (List<KiWiTriple>) q.getResultList();
    	}
     }

	/**
	 * Return a list of nodes that are the sources for edges with propLabel that have this resource 
	 * as endpoint. This is mostly a convenience method that wraps listIncoming(propLabel).
	 * 
	 * @param propLabel the label that all edges listed must have, or null for wildcard
	 * @return a list of resources that are sources of edges that have this resource as endpoint
	 * @throws NamespaceResolvingException 
	 */
	public Iterable<KiWiResource> listIncomingNodes(String propLabel) throws NamespaceResolvingException {
		Collection<KiWiTriple> in = listIncoming(propLabel);
		List<KiWiResource> result = new ArrayList<KiWiResource>(in.size());
		for(KiWiTriple t : in) {
			result.add(t.getSubject());
		}
		return result;
	}

    public void addIncomingNode(String propLabel, KiWiResource source) {
    	TripleStore ts = getContext();
    	
    	KiWiUriResource propResource = ts.createUriResourceBySPARQLId(propLabel);
    	ts.createTriple(source, propResource, this);      
    }
    
    
    /* convenience wrappers around common RDF properties */

    /**
     * Return the label of this resource in the language provided as parameter 
     * 
     * If no label is available for the given language, returns the identifier.
     * 
     * @return
     * @throws NonUniqueRelationException 
     */
    public String getLabel()  {
    	return getLabel(null);
    }

    
    /**
     * Return the label of this resource in the language provided as parameter 
     * within the getContext().knowledge space of this KiWiResource.
     * 
     * If no label is available for the given language, returns the identifier.
     * 
     * @param loc
     * @return
     * @throws NonUniqueRelationException 
     */
    public String getLabel(Locale loc) {
    	String label = null;
		label = getProperty("<"+Constants.NS_RDFS+"label>",loc);
  	
		// no label available, take content item title
		if(label == null) {
			label = getContentItem().getTitle();
		}
		
		// still no label available, try to get last part from uri
		if(label == null && this.isUriResource()) {
			String uri = ((KiWiUriResource)this).getUri();
			if(uri.lastIndexOf("#") > 0) {
				label = uri.substring(uri.lastIndexOf("#")+1);
			} else {
				label = uri.substring(uri.lastIndexOf("/")+1);
			}
		} else if(label == null && this.isAnonymousResource()){
			label = ((KiWiAnonResource)this).getAnonId();
		}
    	return label;
    }
    
    /**
     * Set the rdfs:label of this KiWiResource in the configured getContext().TripleStore 
     * for the given Locale.
     * 
     * 
     * @param loc
     * @param label
     * @throws NamespaceResolvingException 
     */
    public void setLabel(Locale loc, String label) {
        try {
			setProperty("<"+Constants.NS_RDFS+"label>", label, loc);
		} catch (NamespaceResolvingException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Return the identifier of this resource in its getContext().TripleStore for
     * the Locale passed as parameter. The identifier is the name used e.g. in links or
     * in short URIs. It thus needs to be unique within a knowledge space and
     * the language.
     * 
     * @param loc
     * @return
     */
    public String getIdentifier(Locale loc) {
        // TODO
        return "";
    }
    
    /**
     * 
     * @param loc
     * @param identifier
     */
    public void setIdentifier(Locale loc, String identifier) {
        // TODO
    }
    
    /**
     * Return the rdfs:comment for this resource for the given locale in the getContext().
     * knowledge space.
     * @param loc
     * @throws NonUniqueRelationException 
     */
    public String getComment(Locale loc) throws NonUniqueRelationException {
        return getProperty("<"+Constants.NS_RDFS+"comment>",loc);
    }
    
    /**
     * Set the rdfs:comment for this resource
     * @param loc
     * @param comment
     */
    public void setComment(Locale loc, String comment) {
        try {
			setProperty("<"+Constants.NS_RDFS+"comment>", comment, loc);
		} catch (NamespaceResolvingException e) {
			e.printStackTrace();
		}
    }
    

    /**
     * Return the list of types as KiWiResources that are associated with this resource using the
     * rdf:type RDF property.
     * 
     * @return an iterable of KiWiResource instances that represent the RDF types of this resource
     */
    public Collection<KiWiResource> getTypes() {
    	EntityManager em = (EntityManager) Component.getInstance("entityManager");
    	
    	Query q = em.createNamedQuery("tripleStore.getTypes");
    	q.setParameter("subject", this);
		q.setHint("org.hibernate.cacheable", true);
		return q.getResultList();
    }
    
    /**
     * Add a new type to the list of RDF types of this KiWiResource.
     * 
     * @param type the type to add 
     */
    public void addType(KiWiUriResource type) {
    	try {
			addOutgoingNode("<"+Constants.NS_RDF+"type>", type);
		} catch (NamespaceResolvingException e) {
			e.printStackTrace();
		}
    }
 
    /**
     * Remove one of the RDF types of this KiWiResource
     * @param type a URI resource representing the type of this KiWiResource
     */
    public void removeType(KiWiUriResource type) {
    	try {
			removeOutgoingNode("<"+Constants.NS_RDF+"type>", type);
		} catch (NamespaceResolvingException e) {
			e.printStackTrace();
		}
    }
    
    public boolean hasType(String typeUri) {
    	TripleStore ts = getContext();
    	return hasType(ts.createUriResource(typeUri));
    }
    
    /**
     * Check whether this KiWiResource has a certain RDF type
     * @param type the resource representing the type to check for
     * @return true if the type is in the list of RDF types of this resource, false otherwise
     */
    public boolean hasType(KiWiUriResource type) {
    	EntityManager em = (EntityManager) Component.getInstance("entityManager");
    	
    	Query q = em.createNamedQuery("tripleStore.hasType");
    	q.setParameter("type", type);
    	q.setParameter("subject", this);
    	q.setMaxResults(1);
		q.setHint("org.hibernate.cacheable", true);

		return q.getResultList().size() > 0;
    }
    
    /**
     * Return the SeRQL identifier of this resource; this is
     * - <URI> for URI resources and
     * - _:ID  for anonymous resources
     * Needs to be implemented by subclasses.
     * @return
     */
    public abstract String getSeRQLID();
    
    
    public abstract String getKiwiIdentifier();


	
	/**
     * Generic method to query for literal values related to this resource with the property
     * identified by "propLabel" (SeRQL/SPARQL short or long notation) and the given locale.
     * 
     * @param propLabel label of the property; either RDF short form (e.g. "foaf:mbox") or long form (e.g. <http://xmlns.com/foaf/0.1/mbox>)
     * @param loc
     * @return
	 * @throws NamespaceResolvingException 
     */
    private KiWiLiteral getLiteral(String propLabel, Locale loc) throws NamespaceResolvingException {
    	Log log = Logging.getLog(KiWiResource.class);
    	
    	if(!prefetched) {
    		prefetchProperties();
    	}
    	
    	String key = propLabel + (loc == null?"":"@"+loc.getLanguage());
    	
    	if(undef_properties.get(key) != null && undef_properties.get(key)) {
    		return null;
    	}
    	
    	KiWiLiteral result = properties.get(key);
    	
    	// prefetchProperties retrieves all properties anyways, so it should be safe enough to not do this:
    	if(result == null) {
    		// perform entityManager query ...
    		log.debug("resource is stored in database, performing entity manager query");

    		EntityManager em = (EntityManager) Component.getInstance("entityManager");
    		//TripleStore   ts = (TripleStore)   Component.getInstance("tripleStore");

    		String uri = _resolvePropLabel(propLabel);

    		log.debug("#0: querying property with uri #1", this, uri);

    		//KiWiUriResource property = ts.createUriResource(uri);

    		Query q = em.createNamedQuery("tripleStore.literal2BySubjectProperty"+(loc != null?"Locale":""));
    		q.setHint("org.hibernate.cacheable", true);
    		q.setParameter("subject", this);
    		q.setParameter("property_uri",uri);
    		q.setMaxResults(1);
    		if(loc != null) {
    			q.setParameter("locale",loc);
    		}

    		try {
    			result = (KiWiLiteral) q.getSingleResult();
    		} catch(NoResultException ex) {
    			// valid, no result
    			undef_properties.put(key, Boolean.TRUE);
    			return null;
    		} catch(NonUniqueResultException ex) {
    			// invalid state of database
    			log.error("non-unique result while querying resource #0 with property #1",this.getKiwiIdentifier(),uri);
    			result = (KiWiLiteral) q.getResultList().get(0);
    		}
	    	properties.put(key, result);
    	}
    	return result;
    }

    
	/**
     * Generic method to query for literal values related to this resource with the property
     * identified by "propLabel" (SeRQL/SPARQL short or long notation) and the given locale.
     * 
     * @param propLabel label of the property; either RDF short form (e.g. "foaf:mbox") or long form (e.g. <http://xmlns.com/foaf/0.1/mbox>)
     * @param loc
     * @return
	 * @throws NamespaceResolvingException 
     */
    private List<KiWiLiteral> listLiterals(String propLabel, Locale loc) throws NamespaceResolvingException {
    	// TODO: should be implemented as an RDF set or sequence!
    	Log log = Logging.getLog(KiWiResource.class);
    	
    	LinkedList<KiWiLiteral> result = new LinkedList<KiWiLiteral>();
    	
    	if(this.getId() == null || this.getId() <= 0) {
    		log.warn("PLEASE AVOID: calling listLiterals() on Sesame triplestore, since resource is not yet in database");
	    	String myLabel = propLabel;
	    	
	    	// allow slightly incorrect passing of URI without <> brackets ...
	    	if(!myLabel.startsWith("<") && myLabel.contains("://")) {
	    		myLabel = "<" + myLabel + ">";
	    	}
	    	
	        String query = "SELECT label(L) FROM {"+getSeRQLID()+"} "+myLabel+" {L} WHERE isLiteral(L)"+_resolveSeRQLLang(loc);
	        
			SparqlService ss     = (SparqlService)     Component.getInstance("kiwi.query.sparqlService");

	        
	        for(KiWiLiteral l : ss.queryLiteral(query, KiWiQueryLanguage.SERQL)) {
	        	result.add(l);
	        }
	        
    	} else {
    		// perform entityManager query ...
    		
	        EntityManager em = (EntityManager) Component.getInstance("entityManager");
	        //TripleStore   ts = (TripleStore)   Component.getInstance("tripleStore");
	
	    	String uri = _resolvePropLabel(propLabel);
	    	
	    	log.debug("querying property with uri #0", uri);
	    	
	    	//KiWiUriResource property = ts.createUriResource(uri);
	    	
	    	Query q = em.createNamedQuery("tripleStore.literal2BySubjectProperty"+(loc != null?"Locale":""));
			q.setHint("org.hibernate.cacheable", true);
	    	q.setParameter("subject", this);
	    	q.setParameter("property_uri",uri);
	    	if(loc != null) {
	    		q.setParameter("locale",loc);
	    	}
	    	
    		result.addAll( (List<KiWiLiteral>) q.getResultList() );
    	}
     	return result;
    }
    
	
    private static final String _resolvePropLabel(String propLabel) throws NamespaceResolvingException {
    	Log log = Logging.getLog(KiWiResource.class);
    	String uri = propLabel;
    	
    	// find out which kind of propLabel we got passed
    	if(uri.startsWith("<") && uri.endsWith(">")) {
    		// uri is a real uri enclosed in < >
    		uri = uri.substring(1,uri.length()-1);
    	} else if(!uri.contains("://") && uri.contains(":")) {
    		// uri is a SeQRQL/SPARQL identifier with abbreviated namespace, we need to lookup the namespace...
    		String[] components = uri.split(":");
    		if(components.length == 2) {
    			String ns_prefix = components[0];
    			String ns_local  = components[1];
    			
    	        TripleStore   ts = (TripleStore)   Component.getInstance("tripleStore");
    			KiWiNamespace ns = ts.getNamespace(ns_prefix);
    			if(ns == null) {
    				throw new NamespaceResolvingException("The namespace with prefix " + ns_prefix + 
    						"could not be resolved. Have you imported the kiwi core ontology?");
    			}
    			uri = ns.getUri() + ns_local;
    		} else {
    			log.error("could not properly split property identifier #0, as it contained more than one ':'",uri);
    		}
    	}
    	return uri;
    }
    
    /**
     * Helper method. Return an appropriate query string for querying the language provided as parameter in SeRQL query strings.
     * @param loc
     * @return
     */
    protected static final String _resolveSeRQLLang(Locale loc) {
        if(loc == null) {
            return "";
        } else {
            return " AND lang(L) LIKE \""+loc.getLanguage()+"*\"";
        }
    }
    
    protected void prefetchProperties() {
    	Log log = Logging.getLog(KiWiResource.class);

    	EntityManager em = (EntityManager) Component.getInstance("entityManager");
        TripleStore   ts = (TripleStore)   Component.getInstance("tripleStore");

        //em.setFlushMode(FlushModeType.COMMIT); // avoid flushing transaction for query
        
        log.debug("prefetching resource properties for #0", this);
    	
    	Query q = em.createNamedQuery("tripleStore.prefetchTripleByS");
		q.setHint("org.hibernate.cacheable", true);
    	q.setParameter("subject", this);
    	
		List<Object[]> result = q.getResultList();
		for(Object[] tuple : result) {
			KiWiUriResource property = (KiWiUriResource)tuple[0];
			KiWiLiteral lit          = (KiWiLiteral)tuple[1];
		
			String key = "<"+property.getUri()+">" + (lit.getLanguage() == null?"":"@"+lit.getLanguage().getLanguage());
	
			properties.put(key, lit);

			log.debug("prefetched #0 = #1", key, lit);
		}
    	
    	prefetched = true;
    	
    }
 
    /**
     * A comparator that allows to compare KiWiResources by label. Useful for sorting.
     * 
     * @author Sebastian Schaffert
     *
     */
    public static class LabelComparator implements Comparator<KiWiResource> {
		@Override
		public int compare(KiWiResource o1, KiWiResource o2) {
			if(o1.getLabel() != null && o2.getLabel() != null) {
				return Collator.getInstance().compare(o1.getLabel(), o2.getLabel());
			} else {
				return 0;
			}
		}
    
		private static LabelComparator _c;
		
		public static LabelComparator getInstance() {
			if(_c == null) {
				_c = new LabelComparator();
			}
			return _c;
		}
    }
    
    public abstract String getNamespacePrefix();
    
}
