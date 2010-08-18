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
package kiwi.service.triplestore;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.Id;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import kiwi.api.config.ConfigurationService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.event.KiWiEvents;
import kiwi.api.perspectives.PerspectiveService;
import kiwi.api.role.RoleService;
import kiwi.api.security.PermissionService;
import kiwi.api.transaction.TransactionService;
import kiwi.api.triplestore.TripleStoreLocal;
import kiwi.api.triplestore.TripleStoreRemote;
import kiwi.exception.NamespaceResolvingException;
import kiwi.exception.NonUniqueRelationException;
import kiwi.exception.PermissionException;
import kiwi.model.Constants;
import kiwi.model.annotations.RDF;
import kiwi.model.annotations.RDFType;
import kiwi.model.content.ContentItem;
import kiwi.model.content.MediaContent;
import kiwi.model.content.TextContent;
import kiwi.model.kbase.KiWiAnonResource;
import kiwi.model.kbase.KiWiDataFormat;
import kiwi.model.kbase.KiWiEntity;
import kiwi.model.kbase.KiWiLiteral;
import kiwi.model.kbase.KiWiNamespace;
import kiwi.model.kbase.KiWiNode;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.Role;
import kiwi.model.user.User;
import kiwi.service.entity.FacadeUtils;
import kiwi.util.KiWiFormatUtils;
import kiwi.util.MD5;
import nu.xom.Document;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.cache.CacheProvider;
import org.jboss.seam.core.Events;
import org.jboss.seam.log.Log;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.rdfxml.util.RDFXMLPrettyWriter;

// ESCA-JAVA0136:
/**
 * The KiWi TripleStore is an component for managing the RDF metadata associated with
 * KiWi Entities. It provides an abstraction layer for storing and retrieving RDF triples,
 * and provides persist and refesh methods that extract / set Java properties of
 * KiWi Entities to / from RDF.
 * <p>
 * The TripleStore stores triples primarily in the relational database inside the table
 * TRIPLES, together with additional triple maintenance data that is used inside the 
 * KiWi system for working with the triple. Secondarily, the triple data is currently
 * stored in the Sesame triple store for efficient SPARQL querying.
 * <p>
 * In the future, we aim to rewrite SPARQL queries to OQL to improve querying performance.
 * Reasoning will be realised using the rule-based reasoner developed in WP2. Reasoning
 * can be efficiently implemented on top of the database because of reason maintenance.
 * <p>
 * Additionally, future versions of the triple store will support multiple knowledge spaces
 * that the user can enable/disable and select for working.
 * <p>
 * Events:
 * <ul>
 *   <li>tripleStoreUpdated - when there are new triples in the triple store or when triples 
 *       have been deleted</li>
 * </ul>
 * 
 * @see KiWiTriple
 * @author Sebastian Schaffert
 *
 */
@Stateless
@Name("tripleStore")
@Scope(ScopeType.STATELESS) 
@AutoCreate
public class TripleStoreImpl implements TripleStoreLocal, TripleStoreRemote {
	
	// cache name for namespace prefixes for passed resource uris
	public static String CACHE_NS_BY_RESOURCE = "NS-RESOURCE";
	
	// cache name for namespaces by prefix
	public static String CACHE_NS_BY_PREFIX   = "NS-PREFIX";
	

	/**
	 * Get the seam logger for issuing logging statements.
	 */
	@Logger
	private static Log log;
	
	/**
	 * The Sesame 2 repository. We are using a native repository for efficient storage.
	 */
// commented out to improve performance, see getRepository()
//	@In(value="tripleStore.repository", create = true)
//    private Repository repository;
    
    /**
     * The connection of this TripleStore to the Sesame 2 repository.
     */
	// commented out to improve performance, see getConnection()
//	@In(value="tripleStore.repositoryConnection", create=true)
//    private RepositoryConnection con;
    
    
    /**
 	 * The Java EE entity manager. Used for persisting and querying triples and nodes in the
 	 * relational database so that queries can make efficient use of OQL.
 	 */
 	@In
    private EntityManager entityManager;
 	
 	/**
 	 * The transaction service is used for managing transaction-related information.
 	 */
 	@In
 	private TransactionService transactionService;
  	
 	/**
 	 * The current user. Used for storing user information in revisions.
 	 */
 	@In(required = false)
 	private User currentUser;
 	
 	
 	/**
 	 * The cache provider defined in Seam. Used extensively for caching triples and nodes in order to 
 	 * avoid unnecessary database access.
 	 */
 	@In
 	private CacheProvider cacheProvider;
 	
 	/**
 	 * When calling createTriple, first check whether the triple already exists. Since this can have
 	 * a considerable performance penalty, it is possible to switch this to false for large imports
 	 * where the one can be sure that the triple does not yet exist.
 	 */
	@In(value="tripleStore.noTripleExistsCheck", required = false)
	@Out(value="tripleStore.noTripleExistsCheck", scope=ScopeType.SESSION, required = false)
 	private Boolean noTripleExistsCheck;
 	
    /**
     * Create a new KiWi triple store.
     */
    public TripleStoreImpl() {
		log.info("KiWi Triple Store starting up ...");
		noTripleExistsCheck = false;
    }

    
    @BypassInterceptors
    public RepositoryConnection getConnection() {
     	return (RepositoryConnection) Component.getInstance("tripleStore.repositoryConnection",true);
    }
    

    
    public synchronized long size() {
        try {
        	return getConnection().size();
        } catch(RepositoryException ex) {
            log.error("error while determining repository size:",ex);
            return 0;
        }
    }

    /**
     * List all triples contained in this Knowledge Space.
     * @return
     */
    public Iterable<KiWiTriple> listTriples(boolean inferred) {
    	Query q = inferred ? entityManager.createNamedQuery("tripleStore.allTriples") : entityManager.createNamedQuery("tripleStore.baseTriples");
    	return q.getResultList();
     }
    
    /**
     * List only inferred triples contained in this Knowledge Space.
     * @return
     */
    public Iterable<KiWiTriple> listInferredTriples() {
    	Query q = entityManager.createNamedQuery("tripleStore.inferredTriples");
    	log.info("Running a query for inferred TRIPLES!", 0);
    	return q.getResultList();
     }
    
    /**
     * Check whether the triple formed by the parameters exists.
     * @param subject
     * @param property
     * @param object
     * @return
     */
    public boolean hasTriple(KiWiResource subject, KiWiUriResource property, KiWiNode object) {
    	String key = TripleStoreUtil.createCacheKey(subject, property, object, null);    
    	
    	KiWiTriple result = getCachedTriple(key);
    	if(result == null) {
    		Query q = entityManager.createNamedQuery("tripleStore.tripleBySPO");
			q.setHint("org.hibernate.cacheable", true);
    		q.setParameter("subject",subject);
    		q.setParameter("property", property);
    		q.setParameter("object", object);
    		
    		q.setMaxResults(1);
    		
    		try {
    			q.getSingleResult();
    			return true;
    		} catch(NoResultException ex) {
    			return false;
    		}
    	} else {
    		return true;
    	}
    }
    
    
    /**
     * Create or retrieve a KiWi triple in this knowledge space using the provided subject, predicate, and 
     * object. 
     * 
     * If a triple with the same subject/property/object already exists in the knowledge base,
     * it is returned. 
     * 
     * @param subject
     * @param property
     * @param object
     * @return the newly constructed triple
     */
    public synchronized KiWiTriple createTriple(KiWiResource subject, KiWiUriResource property, KiWiNode object) {
    	
    	log.debug("creating triple: subject=#0, property=#1, object=#2 ",subject,property,object);
    	
		String key =  TripleStoreUtil.createCacheKey(subject, property, object, null);   		
		
		KiWiTriple result = getCachedTriple(key);
		if(result == null || result.isDeleted()) {
			if(isCheckTripleExists() && subject.getId() != null && property.getId() != null && object.getId() != null) {
    			// if check is turned on, do a more expensive check whether a triple already exists ...
 
				Query q = entityManager.createNamedQuery("tripleStore.tripleBySPO");
    			q.setHint("org.hibernate.cacheable", true);
    			q.setParameter("subject", subject);
    			q.setParameter("property", property);
    			q.setParameter("object", object);
    			q.setMaxResults(1);

    			try {
    				result = (KiWiTriple) q.getSingleResult();
    				result.setNew(false);
    			} catch(NoResultException ex) {
    				result = new KiWiTriple(subject, property, object);
    				result.setAuthor(currentUser);
    				result.setNew(true);
    				Events.instance().raiseEvent(KiWiEvents.TRIPLE_CREATED, result);
    			} catch(NonUniqueResultException ex) {
    				log.error("non-unique result");
    			}
           		putCachedTriple(key, result);
			} else {
				// if check is turned off, insert the triple without checking and return it

				result = new KiWiTriple(subject, property, object);
				result.setAuthor(currentUser);
				result.setNew(true);

				putCachedTriple(key, result);
				
				Events.instance().raiseEvent(KiWiEvents.TRIPLE_CREATED, result);
			}
			// ensure that triple is added to "addedTriples" in transaction
			storeTriple(result);
		} else {
			log.debug("triple already cached");
			result.setNew(false);
		}
		
		if(result == null) {
			log.error("result of createTriple is null - this is a bug in Hibernate and should never happen!");			
		}

		
		return result;
    }
 
//    public synchronized KiWiTriple createInferredTriple(KiWiResource subject, KiWiUriResource property, KiWiNode object) {
//    	
//    	log.debug("creating INFERRED triple: subject=#0, property=#1, object=#2 ",subject,property,object);
//    	
//		String key =  TripleStoreUtil.createCacheKey(subject, property, object, null);   		
//		
//		KiWiTriple result = getCachedTriple(key);
//		if(result == null || result.isDeleted()) {
//			if(isCheckTripleExists()) {
//    			// if check is turned on, do a more expensive check whether a triple already exists ...
//    			//entityManager.setFlushMode(FlushModeType.COMMIT);
//    			Query q = entityManager.createNamedQuery("tripleStore.tripleBySPO");
//    			q.setHint("org.hibernate.cacheable", true);
//    			q.setParameter("subject", subject);
//    			q.setParameter("property", property);
//    			q.setParameter("object", object);
//    			q.setMaxResults(1);
//
//    			try {
//    				result = (KiWiTriple) q.getSingleResult();
//    				if (result != null)
//    					result.setNew(false);
//    			} catch(NoResultException ex) {
//    				result = new KiWiTriple(subject, property, object, true);
//    				result.setAuthor(currentUser);
//    				result.setNew(true);
//
//    				Events.instance().raiseEvent(KiWiEvents.TRIPLE_CREATED, result);
//    			}
//    			// steffi: the following is already done in storeTriple()->saveTriple()
//    			// sebastian: this may be true, but this call also caches *loading*!
//           		putCachedTriple(key, result);
//			} else {
//				// if check is turned off, insert the triple without checking and return it
//				// may result in constraint violations when committing
//				//entityManager.setFlushMode(FlushModeType.COMMIT);
//				result = new KiWiTriple(subject, property, object, true);
//				result.setAuthor(currentUser);
//				// steffi: the following is already done in storeTriple()->saveTriple()
//				// cacheProvider.put("triples", key, result);
//				putCachedTriple(key, result);
//				
//				Events.instance().raiseEvent(KiWiEvents.TRIPLE_CREATED, result);
//			}
//		} else {
//			log.debug("triple already cached");
//			result.setNew(false);
//		}
//    	// ensure that triple is added to "addedTriples" in transaction
//		// if it's not new then it also may be a base triple which shouldn't be stored again /added in transaction
//		if (result.isNew())
//			storeTriple(result);
//		
//		
//		return result;
//    }
    
    /**
     * Create or retrieve a KiWi triple in this knowledge space using the provided subject, predicate, and 
     * object. 
     * 
     * If a triple with the same subject/property/object already exists in the knowledge base,
     * it is returned. 
     * 
     * @param subject
     * @param property
     * @param object
     * @return the newly constructed triple
     */
    public synchronized KiWiTriple createTriple(KiWiResource subject, KiWiUriResource property, KiWiNode object, KiWiUriResource context) {
    	
    	String key = TripleStoreUtil.createCacheKey(subject, property, object, context);
    	
    	KiWiTriple result = getCachedTriple(key);
    	if(result == null || result.isDeleted()) {
    		if(isCheckTripleExists()  && subject.getId() != null && property.getId() != null && object.getId() != null ) {
	    		//entityManager.setFlushMode(FlushModeType.COMMIT);
	    		Query q = entityManager.createNamedQuery("tripleStore.tripleBySPOC");
	    		q.setHint("org.hibernate.cacheable", true);
	    		q.setParameter("subject", subject);
	    		q.setParameter("property", property);
	    		q.setParameter("object", object);
	    		q.setParameter("context", context);
	    		q.setMaxResults(1);

	    		try {
	    			result = (KiWiTriple) q.getSingleResult();
    				if (result != null) {
    					result.setNew(false);
    				}
	    		} catch(NoResultException ex) {
	    			result = new KiWiTriple(subject, property, object,context);
	    			result.setAuthor(currentUser);
    				result.setNew(true);

    				Events.instance().raiseEvent(KiWiEvents.TRIPLE_CREATED, result);
	    		}

    			// steffi: the following is already done in storeTriple()->saveTriple()
    			// sebastian: this may be true, but this call also caches *loading*!
	    		// cacheProvider.put("triples", key, result);
	    		putCachedTriple(key, result);
    		} else {
    			result = new KiWiTriple(subject, property, object,context);
    			result.setAuthor(currentUser);
     			// steffi: the following is already done in storeTriple()->saveTriple()
    			// cacheProvider.put("triples", key, result);
    			putCachedTriple(key, result);

    			Events.instance().raiseEvent(KiWiEvents.TRIPLE_CREATED, result);
   		}   
    	} else  {
			log.debug("triple already cached");
			result.setNew(false);
		}
    	// ensure that triple is added to "addedTriples" in transaction
		storeTriple(result);
    	return result;
    }
    
    /**
     * Convenience method that allows passing a property by string; also avoids unnecessary JNDI
     * lookups, because the property triple does not need to be created separately.
     * 
     * @param subject a KiWiResource instance representing the subject of the triple to be added
     * @param property_uri a String containing the URI of the property to be added
     * @param object a KiWiNode instance representing the object of the triple
     * @return the KiWiTriple instance representing the relation between subject, property, and object
     */
    public KiWiTriple createTriple(KiWiResource subject, String property_uri, KiWiNode object) {
    	KiWiUriResource property = createUriResource(property_uri);
    	return createTriple(subject,property,object);
    }
    
    
    private KiWiResource createResourceInternal(KiWiResource resource, String title) {
		ContentItem ci = new ContentItem(resource);
		if(title != null) {
			ci.setTitle(title);
		}
		
		KiWiResource result = (KiWiResource) saveNode(resource);
		
		// add default perspective to content item
		PerspectiveService perspectiveService = (PerspectiveService) Component.getInstance("perspectiveService");
		perspectiveService.attachPerspective(ci,perspectiveService.getDefaultPerspective());
		
		
		// grant permissions to the author
		try {
			PermissionService permissionService = (PermissionService) Component.getInstance("permissionService");
			RoleService       roleService       = (RoleService) Component.getInstance("roleService");

			
			
			if(currentUser != null) {
				permissionService.grantPermission(currentUser, ci, "read");
				permissionService.grantPermission(currentUser, ci, "write");
				permissionService.grantPermission(currentUser, ci, "editPermissions");
				permissionService.grantPermission(currentUser, ci, "comment");
				permissionService.grantPermission(currentUser, ci, "annotate");
				permissionService.grantPermission(currentUser, ci, "tag");
				permissionService.grantPermission(currentUser, ci, "delete");
			}
			
			Role read = null;
			if((read = roleService.getRoleByName("read")) == null) {
				read = roleService.createRole("read");
			}
			Role write = null;
			if((write = roleService.getRoleByName("write")) == null) {
				write = roleService.createRole("write");
			}
			if(read != null)
				permissionService.grantPermission(read, ci, "read");
			if(write != null)
				permissionService.grantPermission(write, ci, "write");
			
		} catch (PermissionException e) {
			e.printStackTrace();
		}
		
		return result;
    }
    
    
    /**
     * Create a new KiWiUriResource in this knowledge space using the provided uri. Note that resources are not
     * stored unless they are part of a triple.
     * @param uri
     * @return
     */
    public synchronized KiWiUriResource createUriResource(String uri) {
    	KiWiUriResource result = null; 
    	
    	if(cacheProvider == null) {
    		cacheProvider = (CacheProvider) Component.getInstance("cacheProvider");
    	}
    	try {
    		result = (KiWiUriResource) cacheProvider.get("nodes_uri", uri );
    	} catch(IllegalStateException ex) {
    		log.error("error while retrieving resource from cache ...");
    	}
    	if(result == null) {
    		//entityManager.setFlushMode(FlushModeType.COMMIT);
	    	Query q = entityManager.createNamedQuery("tripleStore.resourceByUri");
	    	q.setParameter("uri", uri);
			q.setHint("org.hibernate.cacheable", true);
	    	q.setMaxResults(1);   	
	    	
	    	try {
	    		result = (KiWiUriResource) q.getSingleResult();
	    	} catch(NoResultException ex) {
	    		String label = null;
				if(uri.lastIndexOf("#") > 0) {
					label = uri.substring(uri.lastIndexOf("#")+1);
				} else {
					label = uri.substring(uri.lastIndexOf("/")+1);
				}
				result = (KiWiUriResource) createResourceInternal(new KiWiUriResource(uri), label);
	    		
	    	}
	    	cacheProvider.put("nodes_uri", uri,result);
	        return (KiWiUriResource)result;       
    	} else {
    		return result;
    	}
    }
	
    
    /**
     * Create a KiWiUriResource in this knowledge space by parsing the provided identifier in SPARQL/SeRQL short or long form.
     * - long form: &l;URI&gt; enclosed in pointy braces
     * - short form: ns-prefix : local name, separated by colon
     * @param id
     * @return
     */
    public KiWiUriResource createUriResourceBySPARQLId(String id) {
        if( id.startsWith("<") ) {
            // uris are enclosed in <> in SPARQL and SeRQL
            return createUriResource(id.substring(1,id.length()-1));
        } else if( id.contains("://") ) {
        	// improperly formated URI resource
        	return createUriResource(id);
        } else {
            // short names are separated by namespace : local name
            String[] components = id.split(":");
            return createUriResourceByNamespaceTitle(components[0],components[1]);
        }
    }

    
    /**
     * Create a new KiWiUriResource in this knowledge space using the provided namespace prefix and
     * local name. Note that resources are not stored unless they are part of a triple.
     * @param nsprefix the configured namespace prefix, e.g. "rdf"
     * @param name the local name of the resource, e.g. "type"
     * @return
     */
    public synchronized KiWiUriResource createUriResourceByNamespaceTitle(String nsprefix, String name) {
    	return createUriResource(getNamespaceUri(nsprefix)+name);
    }
	
    
    
    /**
     * Create a new KiWiAnonResource using a new unique identifier.  Note that resources are not stored unless 
     * they are part of a triple.
     * 
     * @return
     */
    public KiWiAnonResource createAnonResource() {
    	return createAnonResource(Long.toHexString(System.currentTimeMillis())+Integer.toHexString((int) (Math.random()*1000)) );
    }
    
    /**
     * Create a new KiWiAnonResource using the provided identifier. If the id is already used, will return the
     * object associated with this id. Note that resources are not stored unless they are part of a triple.
     * 
     * @return
     */
    public synchronized KiWiAnonResource createAnonResource(String id) {
    	KiWiAnonResource result = (KiWiAnonResource) cacheProvider.get("nodes_anon", id );
    	if(result == null) {
	    	Query q = entityManager.createNamedQuery("tripleStore.resourceByAnonId");
	    	q.setParameter("anonId", id);
			q.setHint("org.hibernate.cacheable", true);
	    	q.setMaxResults(1);
	    	   	
	    	try {
	    		result = (KiWiAnonResource) q.getSingleResult();
	    	} catch(NoResultException ex) {
		    	result = (KiWiAnonResource) createResourceInternal(new KiWiAnonResource(id),id);
	    	}
	    	cacheProvider.put("nodes_anon", id, result);
	        return result;       
    	} else {
    		//return entityManager.merge(result);
    		return result;
    	}
    	
    	
    }    
    
    /**
     * Create a new KiWiLiteral of the provided type.
     * @param <T>
     * @param value
     * @param language
     * @param type
     * @return
     */
    public synchronized <T> KiWiLiteral createLiteral(T value, Locale language, KiWiResource type) {
    	if(value != null) {
    		KiWiLiteral result = (KiWiLiteral) cacheProvider.get("nodes_literal", value.hashCode() + "|" + type.hashCode() + "|" + language );
 
    		if(result == null) {
	    		String content_s; 
	    		content_s = value.toString();

	    		Query q;
	    		if(language != null) {
	    			q = entityManager.createNamedQuery("tripleStore.literalByValueLanguageType");
	    			q.setParameter("lang", language);
	    		} else {
	    			q = entityManager.createNamedQuery("tripleStore.literalByValueType");
	    		}
	    		// TODO: I am not sure whether it is safe to use the MD5 value here, this could lead to 
	    		//       problems in exceptional cases ...
	    		q.setParameter("md5", MD5.md5sum(content_s));
	    		//q.setParameter("value", content_s);
	    		q.setParameter("type", type);
	    		q.setHint("org.hibernate.cacheable", true);
	    		q.setMaxResults(1);

	    		try {
	    			result = (KiWiLiteral) q.getSingleResult();
	    		} catch(NoResultException ex) {
	    			if(value.getClass().equals(Date.class)) {
	    				result = new KiWiLiteral(KiWiFormatUtils.ISO8601FORMAT.format(value), language, type);
	    			} else if(Integer.class.equals(value.getClass()) || int.class.equals(value.getClass())) {
	    				result = new KiWiLiteral((Integer)value, language, type);
	    			} else if(Double.class.equals(value.getClass()) || double.class.equals(value.getClass())) {
	    				result = new KiWiLiteral((Double)value, language, type);
	    			} else {
	    				result = new KiWiLiteral(value.toString(), language, type);
	    			}
	    			result = (KiWiLiteral)saveNode(result);
	    		}
	    			
	    		
	    		
		    	cacheProvider.put("nodes_literal", value.hashCode() + "|" + type.hashCode() + "|" + language, result );
    		}

    		
    		
	    	return result;
    	} else { 
    		return null;
    	}
    }
    

    /**
     * Create a new KiWiLiteral. The type is inferred by Java reflection.
     * @param <T>
     * @param value
     * @return
     */
    public <T> KiWiLiteral createLiteral(T value) {
    	return createLiteral(value, null, getXSDType(value.getClass()));
    }


    /**
     * Create or retrieve the default knowledge space of this system. The default knowledge space has a URI that is
     * constructed from the base URI of this KiWi installation, followed by the postfix /kspace/default, e.g.
     * http://localhost/KiWi/kspace/default
     *
     * @return
     */
    @Override
    public KiWiUriResource createDefaultKnowledgeSpace() {
        ConfigurationService cs = (ConfigurationService) Component.getInstance("configurationService");

        return createKnowledgeSpace(cs.getBaseUri() + "/kspace/default");

    }

    /**
     * Create or retrieve the knowledge space with the given uri and return it.
     *
     * @param uri the URI of the knowledge space
     * @return
     */
    @Override
    public KiWiUriResource createKnowledgeSpace(String uri) {
        KiWiUriResource result = createUriResource(uri);

        KiWiUriResource type = createUriResource(Constants.NS_KIWI_CORE + "KnowledgeSpace");
        result.addType(type);

        return result;
    }

    private final <C extends KiWiNode> C createNode(C node) {
    	if(node == null) {
    		return null;
    	} else if(node.isUriResource()) {
			return (C)createUriResource(((KiWiUriResource)node).getUri());
		} else if(node.isAnonymousResource()) {
			return (C)createAnonResource(((KiWiAnonResource)node).getAnonId());
		} else if(node.isLiteral()) {
			
			KiWiLiteral l = (KiWiLiteral)node;
			KiWiResource type;
			
			if(l.getType().isUriResource()) {
				type = createUriResource(((KiWiUriResource)l.getType()).getUri());
			} else {
				type = createAnonResource(((KiWiAnonResource)l.getType()).getAnonId());
			}
			
			return (C)createLiteral(l.getContent(), l.getLanguage(), type);
			
//			if(((KiWiLiteral) node).getType() != null) {
//				((KiWiLiteral)node).setType(createNode(((KiWiLiteral) node).getType()));
//			}
//			saveNode(node);
//			return node;
		} else {
			return null;
		}
   }
    
	/**
	 * Create the triples and associated nodes passed as collection. All triples and nodes in the collection
	 * will be checked for existence using the createTriple, createUriResource, ... methods, so the triples
	 * passed as argument need not origin from this TripleStore instance.
	 * <p>
	 * The purpose of this method is to allow for efficient batch insertions of data.
	 * 
	 * @param triples
	 * 
	 * @deprecated this method is deprecated, because it flushes and clears the EntityManager when 
	 * the transaction has not ended. EntityManagers, which have been injected in other services, have to merge
	 * the flushed objects, which then leads to problems in optimistic lockings with versions.
	 * 
	 * Possible solutions:
	 * 1) Stopping the transactions when the count has been reached and starting another transaction might help.
	 * 2) Clearing the entiymanager could cause the problem, but I guess it's there for a (memory-regarding) purpose
	 */
    @Override
    @Deprecated
	public void storeTriplesUnchecked(Collection<KiWiTriple> triples) {
		
    	try {
    		HashMap<KiWiNode, KiWiNode> nodeCache = new HashMap<KiWiNode, KiWiNode>();
	    	
	    	for(KiWiTriple triple : triples) {
	    		KiWiResource    _s = nodeCache.get(triple.getSubject()) != null ? 
	    					(KiWiResource)nodeCache.get(triple.getSubject()) : 
	    					createNode(triple.getSubject());
	    		KiWiUriResource _p = nodeCache.get(triple.getProperty()) != null ?
	    					(KiWiUriResource)nodeCache.get(triple.getProperty()) :
	    					createNode(triple.getProperty());
	    		KiWiNode        _o = nodeCache.get(triple.getObject()) != null ?
	    					nodeCache.get(triple.getObject()) :
	    					createNode(triple.getObject());
	    		KiWiUriResource _c = createNode(triple.getContext());
	    		
	    		nodeCache.put(triple.getSubject(), _s);
	    		nodeCache.put(triple.getProperty(), _p);
	    		nodeCache.put(triple.getObject(), _o);
	    		
	    		if(_c != null) {
	    			createTriple(_s, _p, _o, _c);
	    		} else {
	    			createTriple(_s, _p, _o);    			
	    		}
	    		
	    	}
//			entityManager.flush();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("SecurityException: #0 ", e.getMessage());
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("IllegalStateException: #0 ", e.getMessage());
		}
		
	}
    
//    /**
//	 * Create the triples and associated nodes passed as collection. All triples and nodes in the collection
//	 * will be checked for existence using the createTriple, createUriResource, ... methods, so the triples
//	 * passed as argument need not origin from this TripleStore instance.
//	 * <p>
//	 * The purpose of this method is to allow for efficient batch insertions of data.
//	 * 
//	 * @param triples
//	 */
//    @Override
//	public void storeTriplesUnchecked(Collection<KiWiTriple> triples) {
//		
//    	HashMap<KiWiNode, KiWiNode> nodeCache = new HashMap<KiWiNode, KiWiNode>();
//    	
//    	long count = 0;
//    	
//    	for(KiWiTriple triple : triples) {
//    		KiWiResource    _s = nodeCache.get(triple.getSubject()) != null ? 
//    					(KiWiResource)nodeCache.get(triple.getSubject()) : 
//    					createNode(triple.getSubject());
//    		KiWiUriResource _p = nodeCache.get(triple.getProperty()) != null ?
//    					(KiWiUriResource)nodeCache.get(triple.getProperty()) :
//    					createNode(triple.getProperty());
//    		KiWiNode        _o = nodeCache.get(triple.getObject()) != null ?
//    					nodeCache.get(triple.getObject()) :
//    					createNode(triple.getObject());
//    		KiWiUriResource _c = createNode(triple.getContext());
//    		
//    		nodeCache.put(triple.getSubject(), _s);
//    		nodeCache.put(triple.getProperty(), _p);
//    		nodeCache.put(triple.getObject(), _o);
//    		
//    		if(_c != null) {
//    			createTriple(_s, _p, _o, _c);
//    		} else {
//    			createTriple(_s, _p, _o);    			
//    		}
//    		
//    		count ++;
//    		if(count % 50 == 0) {
//    			entityManager.flush();
//    			entityManager.clear();
//    		}
//    	}
//		entityManager.flush();
//		entityManager.clear();
//		
//	}

	/**
     * Add or update a triple to/in the triple store. This method only adds the triple to the
     * current transaction data; the triple will be persisted to the database and repository
     * on transaction commit. 
     * 
     * @param triple
     */
	public void storeTriple(KiWiTriple triple) {
		log.debug("storing triple #0 ", triple);
		transactionService.getTransactionCIVersionData(triple.getSubject().getContentItem()).addTransactionAddedTriples(triple);
    }
    
    private KiWiNode saveNode(KiWiNode node) {
    	if(entityManager == null) {
    		entityManager = (EntityManager) Component.getInstance("entityManager");
    	}
		entityManager.setFlushMode(FlushModeType.COMMIT);
		
    	if(node.getId() != null) {
    		//KiWiNode merged = em.merge(node);
    		// update caches
    		if(node.isUriResource()) {
    			cacheProvider.put("nodes_uri", node.getCacheKey(), node);
    		} else if(node.isAnonymousResource()) {
    			cacheProvider.put("nodes_anon", node.getCacheKey(), node);
    		}
    		//return merged;
    		return node;
    	} else {
    		entityManager.persist(node);
//    		if(node instanceof KiWiResource) {
//    			em.persist(((KiWiResource)node).getContentItem());
//    		}
    		return node;
    	}
    	
    }
    
    /**
     * Really remove a triple from the repository; called by transaction manager
     * @param triple
     */
    public synchronized void removeTriple(KiWiTriple triple) {
    	// remove from cache
    	removeCachedTriple(TripleStoreUtil.createCacheKey(triple));
     	
    	// add triple to current transaction data
    	transactionService.getTransactionCIVersionData(triple.getSubject().getContentItem()).addTransactionRemovedTriples(triple);
    }

    
    /**
     * Remove a triple from the repository with the given subject, property, and object
     * @param subject
     * @param property
     * @param object
     */
    public void removeTriple(KiWiResource subject, KiWiUriResource property, KiWiNode object)  {
    	// 
		String key =  TripleStoreUtil.createCacheKey(subject, property, object, null);   		
		
		KiWiTriple result = getCachedTriple(key);
		if(result == null) {
			// if check is turned on, do a more expensive check whether a triple already exists ...
			//entityManager.setFlushMode(FlushModeType.COMMIT);
			Query q = entityManager.createNamedQuery("tripleStore.tripleBySPO");
			q.setHint("org.hibernate.cacheable", true);
			q.setParameter("subject", subject);
			q.setParameter("property", property);
			q.setParameter("object", object);
			q.setMaxResults(1);

			try {
				removeTriple((KiWiTriple) q.getSingleResult());
			} catch(NoResultException ex) {

			}
		} else {    	
			removeTriple(result);
		}
    }
    
    /**
     * Convenience method that allows passing the property uri as string rather than as resource.
     * Useful in situations where it is not necessary to first create a URI resource for a property.
     * 
     * @param subject the KiWiResource that is the subject of the triple
     * @param property_uri a String containing the URI of the property to be removed
     * @param object the KiWiNode representing the object of the triple
     */
    public void removeTriple(KiWiResource subject, String property_uri, KiWiNode object) {
		String key =  createCacheKey(subject, property_uri, object, null);   		
		
		KiWiTriple result = getCachedTriple(key);
		if(result == null) {
			//entityManager.setFlushMode(FlushModeType.COMMIT);
			Query q = entityManager.createNamedQuery("tripleStore.tripleBySPuO");
			q.setHint("org.hibernate.cacheable", true);
			q.setParameter("subject", subject);
			q.setParameter("property_uri", property_uri);
			q.setParameter("object", object);
			q.setMaxResults(1);

			try {
				removeTriple((KiWiTriple) q.getSingleResult());
			} catch(NoResultException ex) {

			}
		} else {    	
			removeTriple(result);
		}
    	
    }
    
 	/** Loads all triples of the form (subject, property, ?).
 	 * 
 	 */
	@SuppressWarnings("unchecked")
	public List<KiWiTriple> getTriplesBySP(KiWiResource subject, KiWiUriResource property) {
 		Query q = entityManager.createNamedQuery("tripleStore.tripleBySP");
 		q.setParameter("subject", subject);
 		q.setParameter("property", property);
 		return q.getResultList();
 	}        
    
 	/** Loads all triples of the form (?, property, ?).
 	 * 
 	 */
	@SuppressWarnings("unchecked")
	public List<KiWiTriple> getTriplesByP(KiWiUriResource property) {
 		Query q = entityManager.createNamedQuery("tripleStore.tripleByP");
 		q.setParameter("property", property);
 		return q.getResultList();
 	}        
    
 	/** Loads all triples of the form (?, property, ?).
 	 * 
 	 */
	@SuppressWarnings("unchecked")
	public List<KiWiTriple> getTriplesByP(String propertyUri) {
 		Query q = entityManager.createNamedQuery("tripleStore.tripleByPu");
 		q.setParameter("property_uri", propertyUri);
 		return q.getResultList();
 	}        
    
 	/** Loads all triples of the form (?, property, ?).
 	 * 
 	 */
	@SuppressWarnings("unchecked")
	public List<KiWiTriple> getTriplesByPO(String propertyUri, String objectUri) {
 		Query q = entityManager.createNamedQuery("tripleStore.tripleByPuOu");
 		q.setParameter("property_uri", propertyUri);
 		q.setParameter("object_uri", objectUri);
 		return q.getResultList();
 	}        

	/* (non-Javadoc)
	 * @see kiwi.api.kspace.TripleStore#exportData(java.io.OutputStream, kiwi.model.kbase.KiWiResource, kiwi.model.kbase.KiWiDataFormat)
	 */
    @Deprecated
	public void exportData(OutputStream out, KiWiResource subject, KiWiDataFormat format) {
        switch (format) {
        case RDFXML:
            exportDataToRDFXML(out,subject);
            break;
        default:
        	log.error("format #0 not supported",format);
        	break;
    }		
	}

    @Deprecated
	public void exportData(OutputStream out, KiWiDataFormat format) {
        switch (format) {
            case RDFXML:
                exportDataToRDFXML(out);
                break;
            default:
            	log.error("format #0 not supported",format);
            	break;
        }
    }
    
    
    
    @Deprecated
    protected synchronized void exportDataToRDFXML(OutputStream out) {

        try {
        	getConnection().export(new RDFXMLPrettyWriter(out));
        } catch (RDFHandlerException ex) {
        	log.error("error while exporting data to RDF/XML", ex);
        } catch (RepositoryException ex) {
            log.error("error while exporting data to RDF/XML", ex);
        }

    }

    @Deprecated
    protected synchronized void exportDataToRDFXML(OutputStream out,KiWiResource subject) {

        try {
        	getConnection().exportStatements((Resource)TripleStoreUtil.transformKiWiToSesame(this,subject),null,null,true,new RDFXMLPrettyWriter(out));
        } catch (RDFHandlerException ex) {
        	log.error("error while exporting data to RDF/XML", ex);
        } catch (RepositoryException ex) {
            log.error("error while exporting data to RDF/XML", ex);
        }

    }
    

	/**
	 * Clear all caches that are currently active; used e.g. in case of a rollback to avoid 
	 * detached triples that are not really persisted.
	 */
	public void clearCaches() {
		synchronized(cacheProvider) {
			cacheProvider.clear();
		}
	}
	
	public synchronized void clear() {
		// the following procedure is very inefficient; for performance reasons, we should rather 
		// do the following on transaction commit
		// (1) select all triple ids of non-deleted triples and store them in the metadataupdate
		//     removed relations (maybe there is a HQL query that can be used like a nested SQL?)
		// (2) run a update query "update KiWiTriple t set t.deleted = true where t.deleted = false"
		// (3) update the cacheProvider by removing all nodes in /triples and /namespaces
		
        Iterable<KiWiTriple> triples = listTriples(true);
        Iterable<KiWiNamespace> namespaces = listNamespaces();
        for(KiWiTriple t : triples) {
        	removeTriple(t);
        }
        for(KiWiNamespace n : namespaces) {
        	removeNamespace(n);
        }
        
        try {
            getConnection().clear();
            getConnection().clearNamespaces();
            //cacheProvider.clear(); // should be covered by removeTriple and removeNamespace
            getConnection().commit();
        } catch(RepositoryException ex) {
            log.error("error while removing triple from knowledge base:", ex);
        }
        
        // old (changed by Steffi)
//      entityManager.createQuery("delete from KiWiTriple t").executeUpdate();
//      entityManager.createQuery("delete from KiWiNamespace ns").executeUpdate();
//      entityManager.flush();
        // new:
    }
    
    public synchronized String getNamespaceUri(String prefix) {
        try {
            return getConnection().getNamespace(prefix);
        } catch(RepositoryException ex) {
            log.error("error while retrieving namespace with prefix "+prefix,ex);
            throw new UnsupportedOperationException("error while retrieving namespace with prefix "+prefix,ex);
        }
    }
        
     
    public synchronized KiWiNamespace getNamespace(String prefix) {
    	KiWiNamespace ns = (KiWiNamespace)cacheProvider.get(CACHE_NS_BY_PREFIX,prefix);
    	
    	if(ns == null) {
	    	Query q = entityManager.createNamedQuery("tripleStore.namespaceByPrefix");
	    	q.setParameter("prefix", prefix);
			q.setHint("org.hibernate.cacheable", true);
			q.setMaxResults(1);
	    	
	    	try {
	    		ns = (KiWiNamespace) q.getSingleResult();
	    		cacheProvider.put(CACHE_NS_BY_PREFIX, ns);
	    		return ns;
	    	} catch(NoResultException ex) {
	    		log.debug("namespace with prefix '#0' could not be found.",prefix);
	    		return null;
	    	}
    	} else {
    		return ns;
    	}
    }
  
    /**
     * Return the KiWi namespace that matches for the uri passed as argument. It is sufficient 
     * that the uri of the namespace is a prefix for the given uri for this method to
     * return the matching namespace.
     * 
     * @param uri the uri of which the namespace is a prefix
     * @return the namespace matching the passed uri
     */
    public KiWiNamespace getNamespaceByUri(String uri) {
    	Query q = entityManager.createNamedQuery("tripleStore.namespaceByUriPrefix");
    	q.setParameter("uri", uri);
		q.setHint("org.hibernate.cacheable", true);
    	
    	q.setMaxResults(1);
    	
    	try {
    		return (KiWiNamespace) q.getSingleResult();
    	} catch(NoResultException ex) {
    		log.debug("namespace for uri '#0' could not be found.",uri);
    		return null;
    	}
    }
    
    public synchronized void setNamespace(KiWiNamespace ns) {
    	cacheProvider.put(CACHE_NS_BY_PREFIX, ns.getPrefix(), ns);
    	
    	transactionService.getCurrentTransactionData().addTransactionAddedNamespaces(ns);
    }
  
    public void setNamespace(String prefix, String uri)  {
    	KiWiNamespace ns = getNamespaceByUri(uri);
    	if(ns != null) {
    		/* I'm wondering whether this works, because ns is immutable */
    		ns.setPrefix(prefix);
    	} else {
    		// check whether prefix already exists; if yes, attach a sequential number to it
    		KiWiNamespace _ns = getNamespace(prefix);
    		String myprefix = prefix;
    		int count = 1;
    		while(_ns != null) {
    			myprefix = prefix + count++;
    			_ns = getNamespace(myprefix);
    		}
    		
    		ns = new KiWiNamespace(myprefix,uri);
    	}
    	setNamespace(ns);
    }
        
    public void removeNamespace(String prefix) {
    	KiWiNamespace ns = null;
    	if((ns = getNamespace(prefix)) != null) {
    		removeNamespace(ns);
    	} else {
    		log.debug("KiwiNamespace with prefix #0 is already deleted", prefix);
    	}
    }
    
    public synchronized void removeNamespace(KiWiNamespace ns) {
    	log.debug("removing namespace with id #0, prefix '#1' and uri #2",ns.getId(),ns.getPrefix(),ns.getUri());
 	
    	/* (changed by steffi) To be able to attach every update action to a ContentItem, 
     	we need to forbear to keep track of changes in namespaces (they can be regenerated, anyways) */
    	transactionService.getCurrentTransactionData().addTransactionRemovedNamespaces(ns);
    	
    	
    	//cacheProvider.clear();
    }
    
    /**
     * Return the namespace prefix of the passed uri. In order to match, it is sufficient that the
     * uri stored in the database is a prefix of the uri passed as argument.
     * 
     * @param uri the uri to return a namespace for
     * @return the namespace prefix (e.g. "rdf") of the uri
     */
    @Override
    public String getNamespacePrefix(String uri) {
    	String prefix = (String)cacheProvider.get(CACHE_NS_BY_RESOURCE, uri);
    	
    	if(prefix == null) {
	    	Query q = entityManager.createNamedQuery("tripleStore.namespaceByUriPrefix");
	    	q.setParameter("uri", uri);
			q.setHint("org.hibernate.cacheable", true);
	    	q.setMaxResults(1);
	    	
	    	try {
	    		prefix = ((KiWiNamespace) q.getSingleResult()).getPrefix();
	    		cacheProvider.put(CACHE_NS_BY_RESOURCE, uri, prefix);
	    	} catch(NoResultException ex) {
	    		log.debug("namespace which is a prefix for uri '#0' could not be found.",uri);
	    		prefix = null;
	    	}
    	}
    	return prefix;
    }
    
    
    /**
     * List all namespaces defined in the triple store.
     * 
     * @return a list of all namespaces defined in the triple store.
     */
    @SuppressWarnings("unchecked")
	public Iterable<KiWiNamespace> listNamespaces() {
    	Query q = entityManager.createNamedQuery("tripleStore.listNamespaces");
		q.setHint("org.hibernate.cacheable", true);
		return (Iterable<KiWiNamespace>) q.getResultList();
    }
    
    /**
     * Return the appropriate XSD type for RDF literals for the provided Java class.
     * @param javaClass
     * @return
     */
    public KiWiResource getXSDType(Class javaClass) {
        if(String.class.isAssignableFrom(javaClass)) {
            return createUriResource(Constants.NS_XSD+"string");
        } else if(Integer.class.isAssignableFrom(javaClass)) {
            return createUriResource(Constants.NS_XSD+"integer");            
        } else if(Long.class.isAssignableFrom(javaClass)) {
            return createUriResource(Constants.NS_XSD+"long");            
        } else if(Double.class.isAssignableFrom(javaClass)) {
            return createUriResource(Constants.NS_XSD+"double");            
        } else if(Float.class.isAssignableFrom(javaClass)) {
            return createUriResource(Constants.NS_XSD+"float");            
        } else if(Date.class.isAssignableFrom(javaClass)) {
            return createUriResource(Constants.NS_XSD+"date");            
        } else if(Boolean.class.isAssignableFrom(javaClass)) {
            return createUriResource(Constants.NS_XSD+"boolean");            
        } else {
            return createUriResource(Constants.NS_XSD+"string");
        }
    }	
    
    
    
    
    
    
    /**
	 * This method checks the annotated fields of a class for 'transient' (not
	 * persisted by JPA) and 'RDF' or just for 'RDF' if the class is not
	 * annotated with 'entity'. If there exists such a field the setter method
	 * of the object will be called with the data hold in the tripleStore. This
	 * allows the mapping of data hold in the tripleStore to POJOs.
	 * 
	 * It is important to know that KiWiEntities that have to be loaded to the
	 * object (e.g. Set<User> friends) will be called by the 'getUserByUri()'
	 * method. That will result in a complete-search and that is not very
	 * practicle. (Think about a KiWiPlatform with 1000 registered users where
	 * every user will load detailed information about the friends and the
	 * friends of the friends and the friends of the friends of the friends ...)
	 * That`s why we add a level of detail, let`s say '1' for <load only details
	 * for YOUR friends> and '2' for <load the details of your friends and the
	 * friends of them>, etc..
	 * 
	 * @param <C>
	 * @param entity
	 */
	public <C extends KiWiEntity> void refresh(C entity, boolean override) {
		if (override) {
			for (Field f : entity.getClass().getDeclaredFields()) {
				f.setAccessible(true);
				if (f.isAnnotationPresent(RDF.class)) {
					setFields(entity, f, true);
				}
			}
		}
	}

    
	/* (non-Javadoc)
	 * @see kiwi.ejb.TripleStore#remove(kiwi.model.kbase.KiWiEntity)
	 */
	public <C extends KiWiEntity> void remove(C entity) {
		KiWiResource r = entity.getResource();
		if(r != null) {
			Iterable<KiWiTriple> it_in = r.listIncoming();
			Iterable<KiWiTriple> it_out = r.listOutgoing();
			for (KiWiTriple tr : it_in) {
				removeTriple(tr);
			}
			for (KiWiTriple tr : it_out) {
				removeTriple(tr);
			}
		} else {
			log.warn("while removing: KiWi entity had no associated resource");
		}
		
	}

	/**
	 * TODO: think about field-types like 'Content' which might be needed in the
	 * future
	 * 
	 * setting fields of KiWiEntities by getting the Resource or Literal of the
	 * corresponding RDF property (the value of
	 * 
	 * @RDF) and invoking the setter method of the entity
	 * 
	 * IMPORTANT: methods that return KiWiEntitied (e.g. getUser...,
	 * getContentItem...) should be implemented for ALL KiWiEntities in
	 * KiWiEntityManager in the following form: User getUserByUri( String uri),
	 * ContentItem getContentItemByUri( String uri), Content getContentByUri(
	 * String uri) ...
	 * 
	 * @param entity
	 * @param f
	 */
	private void setFields(KiWiEntity entity, Field f, boolean baseTypesOnly) {
		log.debug("calling method setFields (field: #0)",f.getName());
		try {
			f.setAccessible(true);
			
			Class<?> returnType   = f.getType();
			Type typeOfGeneric    = f.getGenericType();
			String[] rdf_properties   = f.getAnnotation(RDF.class).value();
			
			if(!baseTypesOnly || FacadeUtils.isBaseType(returnType)) {
			
				Object result = FacadeUtils.transform(returnType, typeOfGeneric, entity, rdf_properties[0], false, (KiWiEntityManager)Component.getInstance("kiwiEntityManager"));
				
				f.set(entity,result);
			}
		} catch (NonUniqueRelationException e) {
			log.error("non-unique relation",e);
		} catch(IllegalAccessException e) {
			log.error("illegal access:",e);
		} catch (InstantiationException e) {
			log.error("instantiation exception:",e);
		} 
		
	}


	/**
	 * stores RDF relevant data in the triplestore technique: - this method is
	 * always called when an object is persisted (or when it's explicitly
	 * called) - if the object is an instance of KiWiEntity and has fields that
	 * are annotated with
	 * 
	 * @RDF(...), the Field value is stored in the triplestore - this works for
	 *            fields of the type: String, Date, Locale, classes that
	 *            implement KiWiEntity, Document and classes that implement
	 *            Collection - when an object is inherited, the superclass is
	 *            also searched for
	 * @RDF annotations
	 * @author sstroka
	 * @param <C>
	 * @param entity
	 * @return
	 */
	public <C extends KiWiEntity> C persist(C entity) {
		return persist(entity, false);
	}
	
	public <C extends KiWiEntity> C persist(C entity, boolean persistNow) {
		if(persistNow) {
			try {
				searchFields(entity, entity.getClass().getDeclaredFields(), entity.getClass());
				log.debug("entity #0 persisted in triple store",entity.getResource().getKiwiIdentifier());
			} catch (SecurityException e) {
				log.error("SecurityException while persisting entity in TripleStore", e);
			} catch (IllegalArgumentException e) {
				log.error("IllegalArgumentException while persisting entity in TripleStore",e);
			} catch (IllegalAccessException e) {
				log.error("IllegalAccessException while persisting entity in TripleStore", e);
			}
		} else {
			transactionService.getCurrentTransactionData().getEntities().add(entity);
		}
		return entity;
	}

	
	public <C extends KiWiEntity> C merge(C entity) {
		return persist(entity);
	}
	
	/**
	 * searches the field for
	 * 
	 * @RDF annotations
	 * 
	 * declaring class is important to find our in which class/superclass we are
	 * 
	 * @author sstroka
	 * @param entity
	 * @param fa
	 * @param declaringClass
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private KiWiEntity searchFields(KiWiEntity entity, Field[] fa, Class<?> declaringClass) throws IllegalAccessException {
		
		KiWiResource subject = entity.getResource();
		
		// test whether the entity has been newly created; in this case some of the expensive checks are not
		// necessary, e.g. checking whether type already exists or deleting existing triples
		boolean isNew = subject.getId() == null;
		
		// check for RDFType annotations and add types if necessary
		if(FacadeUtils.isFacadeAnnotationPresent(entity.getClass(),RDFType.class)) {
			String[]        a_type = FacadeUtils.getFacadeAnnotation(entity.getClass(),RDFType.class).value();
			for(String s_type : a_type) {
				KiWiUriResource t_type = createUriResource(s_type);
				if(isNew || !subject.hasType(t_type)) {
					subject.addType(t_type);
				}
			}			
		}
		
		// check not necessary, because the KiWiResource methods always remove the triple before adding
		setCheckTripleExists(false);
		for (Field f : fa) {
			log.debug("processing field #0, type #1, RDF annotation: #2", f.getName(),f.getType().toString(),f.isAnnotationPresent(RDF.class));
			if (f.isAnnotationPresent(RDF.class)) {
				// field is annotated with @RDF
				f.setAccessible(true);
				String[] properties = f.getAnnotation(RDF.class).value();
				
				try {
				if (f.get(entity) != null) {
					// first, remove all existing triples with the property
					// TODO: only remove if the content hasn't changed
					for(String property : properties) {
				    	try {
				    		if(!isNew) {
								for( KiWiTriple t : subject.listOutgoing("<"+property+">")) {
									removeTriple(t);
								}
				    		}
						} catch (NamespaceResolvingException e) {
							e.printStackTrace();
						}
						if (FacadeUtils.isKiWiEntity(f.getType())) {
							KiWiEntity ke = (KiWiEntity) f.get(entity);
							//subject.setOutgoingNode("<" + property + ">", ke.getResource());
							
							createTriple(subject, property,  ke.getResource());
							
							log.debug("setting resource property #0 to value #1", property, ke.getResource().getKiwiIdentifier());
						} else if (f.get(entity) instanceof TextContent) {
							TextContent c = (TextContent) f.get(entity);
							
							KiWiLiteral value = createLiteral(c.getXmlString(), null, getXSDType(String.class));
							createTriple(subject, property, value);
							
							//subject.setProperty("<" + property + ">", c.getXmlString());
							
							log.debug("setting resource property #0 to value #1", property, c.getXmlString());
						} else if (f.get(entity) instanceof MediaContent) {
							MediaContent c = (MediaContent) f.get(entity);
							// TODO: entity.getResource().setProperty("<" + property + ">","linktodata");
							// log.info("setting resource property #0 to value #1", f
							//		.getAnnotation(RDF.class).value(), "linktodata");
	//					} else if (f.get(entity) instanceof ExternContent) {
	//						ExternContent c = (ExternContent) f.get(entity);
	//						subject.addOutgoingNode("<" + property + ">", createUriResource(c.getExternResource().toString()));
	//
	//						log.debug("setting resource property #0 to value #1", property, c.getExternResource().toString());
						} else if (f.getType().equals(Date.class)) {
							Date d = (Date) f.get(entity);
							
							KiWiLiteral value = createLiteral(KiWiFormatUtils.ISO8601FORMAT.format(d), null, getXSDType(Date.class));
							createTriple(subject, property, value);
	
							log.debug("setting property #0 to value #1", property, KiWiFormatUtils.ISO8601FORMAT.format(d));
							//subject.setProperty("<" + property + ">", KiWiFormatUtils.ISO8601FORMAT.format(d));
						} else if (FacadeUtils.isBaseType(f.getType())) {
							KiWiLiteral value = createLiteral( f.get(entity).toString(), null, getXSDType(f.getType()));
							createTriple(subject, property, value);
							
							//subject.setProperty("<" + property + ">", f.get(entity).toString());
							
							log.debug("setting property #0 to value #1", property, f.get(entity).toString());
						} else if (f.getType().equals(Document.class)) {
							Document doc = (Document) f.get(entity);
							String x = doc.toXML();
							
							KiWiLiteral value = createLiteral( x, null, getXSDType(String.class));
							createTriple(subject, property, value);
							
							//entity.getResource().setProperty("<" + property + ">",x);
	
							log.debug("setting property #0 to value #1", property, x);
						} else if (f.getType().equals(Locale.class)) {
							Locale loc = (Locale) f.get(entity);
							String l = loc.getLanguage();
							
							KiWiLiteral value = createLiteral( l, null, getXSDType(String.class));
							createTriple(subject, property, value);
	
							//subject.setProperty("<" + property + ">",l);
	
							log.debug("setting property #0 to value #1", property, l);
						} else if (FacadeUtils.isCollection(f.getType())) {
							Collection<?> obj = (Collection<?>) f.get(entity);
							Iterator<?> it = obj.iterator();
	
							// add all from the collection
							while (it.hasNext()) {
								Object o = it.next();
								if (o instanceof KiWiEntity) {
									KiWiEntity ke = (KiWiEntity) o;
									//subject.setOutgoingNode("<" + property + ">", ke.getResource());
									
									createTriple(subject, property, ke.getResource());
									
									log.debug("adding to property #0: value #1", property, ke.getResource().toString());
								} else if (o instanceof ContentItem) {
									ContentItem ci = (ContentItem) o;
									
									createTriple(subject, property, ci.getResource());
									
									//subject.addOutgoingNode("<" + property + ">", ci.getResource());
									log.debug("setting resource property #0 to value #1",property, ci.getResource());
								} else if (o instanceof String) {
									KiWiLiteral value = createLiteral( (String) o, null, getXSDType(String.class));
									createTriple(subject, property, value);
									
									//subject.setProperty("<"	+ property + ">", (String) o);
									log.debug("adding to property #0: value #1", property, o.toString());
								} else if (o instanceof Locale) {
									Locale loc = (Locale) o;
									String l = loc.getLanguage();
									KiWiLiteral value = createLiteral( l, null, getXSDType(String.class));
									createTriple(subject, property, value);
									//subject.setProperty("<"	+ property + ">", l);
									log.debug("adding to property #0: value #1", property, l);
								} else if (o instanceof Date) {
									Date d = (Date) o;
									
									KiWiLiteral value = createLiteral(KiWiFormatUtils.ISO8601FORMAT.format(d), null, getXSDType(Date.class));
									createTriple(subject, property, value);
									
									//subject.setProperty("<" + property + ">",KiWiFormatUtils.ISO8601FORMAT.format(d));
									log.debug("adding to property #0: value #1", property, KiWiFormatUtils.ISO8601FORMAT.format(d));
								}
							}
						}
					}
				}
				} catch(NullPointerException ex) {
					log.error("error while processing field #0 with annotation #1",f.getName(),f.getAnnotation(RDF.class).value());
					throw ex;
				}
			} else if (f.isAnnotationPresent(Id.class)) {
				f.setAccessible(true);
				if(f.get(entity) != null) {
					KiWiLiteral value = createLiteral(new String(((Long) f.get(entity)).toString()), null, getXSDType(Long.class));
					createTriple(subject, Constants.NS_KIWI_CORE + "id", value);
					//subject.setProperty("<" + Constants.NS_KIWI_CORE + "id>", new String(((Long) f.get(entity)).toString()));
				}
			}
		}
		/*
		 * example: if TextContent is a subclass of Content and both implement
		 * KiWiEntity, Content is also searched for RDF annotations
		 */
		if (declaringClass != null) {
			if (FacadeUtils.isKiWiEntity(declaringClass)) {
				searchFields(entity, entity.getClass().getSuperclass()
						.getDeclaredFields(), declaringClass.getSuperclass());
			}
		}
		setCheckTripleExists(true);
		return entity;
	}
	
	
	

    private final String createCacheKey(KiWiResource subject, String property_uri, KiWiNode object, KiWiResource context) {
    	if(context != null) {
    		return "{"+subject.toString()+" "+property_uri+" "+object.toString()+"}@"+context.toString();   
    	} else {
    		return "{"+subject.toString()+" "+property_uri+" "+object.toString()+"}@GLOBAL";
    	}
    }
    
    
	/**
	 * @return the !noTripleExistsCheck
	 */
	public boolean isCheckTripleExists() {
		if(noTripleExistsCheck == null) {
			noTripleExistsCheck = false;
		}
		return !noTripleExistsCheck;
	}

	/**
	 * @param !noTripleExistsCheck the !noTripleExistsCheck to set
	 */
	public void setCheckTripleExists(boolean checkTripleExists) {
		this.noTripleExistsCheck = !checkTripleExists;
	}
    
    
	private KiWiTriple getCachedTriple(String key) {
		synchronized(cacheProvider) {
			return (KiWiTriple)cacheProvider.get("triples",key);
		}
	}
	
	private void putCachedTriple(String key, KiWiTriple triple) {
		synchronized(cacheProvider) {
			cacheProvider.put("triples",key,triple);
		}
	}
	
	private void removeCachedTriple(String key) {
		synchronized(cacheProvider) {
			cacheProvider.remove("triples", key);
		}
	}
	
	public KiWiNode getCachedNode(String key) {
		synchronized(cacheProvider) {
			return (KiWiNode)cacheProvider.get("nodes",key);
		}		
	}
	
	public void putCachedNode(String key, KiWiNode value) {
		synchronized(cacheProvider) {
			cacheProvider.put("nodes",key,value);
		}		
	}

	public void removeCachedNode(String key) {
		synchronized(cacheProvider) {
			cacheProvider.remove("nodes", key);
		}		
	}
	
	@BypassInterceptors
	public Repository getRepository() {
		return (Repository)Component.getInstance("tripleStore.repository",true);
	}
	
	public void reIndex() {
		log.info("reindexing triples from database");
		long start = System.currentTimeMillis();
    	try {
			getConnection().clear();
			getConnection().clearNamespaces();
			
	    	List<Statement> stmts = new LinkedList<Statement>();
	    	for(KiWiTriple triple : listTriples(false)) {
	    		stmts.add(TripleStoreUtil.transformKiWiToSesame(this,triple));
	    	}
	    	getConnection().add(stmts);
	    	
	    	for(KiWiNamespace ns : listNamespaces()) {
	    		getConnection().setNamespace(ns.getPrefix(),ns.getUri());
	    	}
	    	
	    	
    		getConnection().commit();
    		log.info("reindexing finished after #0ms",System.currentTimeMillis()-start);
    	} catch(RepositoryException ex) {
    		log.error("error while storing triple in knowledge base:", ex);
    	}
    	
	}

}
