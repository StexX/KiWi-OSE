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
package kiwi.service.entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import kiwi.api.entity.KiWiEntityManagerLocal;
import kiwi.api.entity.KiWiEntityManagerRemote;
import kiwi.api.event.KiWiEvents;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.Constants;
import kiwi.model.annotations.KiWiFacade;
import kiwi.model.annotations.RDFFilter;
import kiwi.model.annotations.RDFType;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiEntity;
import kiwi.model.kbase.KiWiQueryLanguage;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.revision.MetadataUpdate;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Events;
import org.jboss.seam.log.Log;


/**
 * The KiWi Entity Manager is a facade that gives unified access to the database
 * (human readable content) and the triple store. It provides most of the
 * methods an "ordinary" EntityManager would provide, but these methods usually
 * accept additional parameters to perform facading.
 * <p>
 * Facading is a new technique that allows to define a new "Java View" providing
 * additional functionality over ContentItems. A facade is defined as a Java
 * interface extending ContentItemI where getter and setter methods may have @RDF
 * annotations pointing to the right RDF properties. When a getter or setter is
 * called, the corresponding RDF property of the ContentItem in the triple store
 * is called.
 *
 * @author Sebastian Schaffert
 *
 */
@Stateless
@Name("kiwiEntityManager")
@Scope(ScopeType.STATELESS)
@AutoCreate
//@Transactional
public class KiWiEntityManagerImpl implements KiWiEntityManagerLocal, KiWiEntityManagerRemote {

    @Logger
    private Log log;

    @In(create = true)
    private EntityManager entityManager;

    @In(create = true)
    private TripleStore tripleStore;

//    @In(create = true)
//    private SolrService solrService;

    /**
     * Create a KiWiFacade of the given type over the given ContentItem. This
     * method creates a proxy object that delegates ContentItem methods to the
     * provided item and all other methods in type to the triple store (by using
     * appropriate @RDF annotations on the methods).
     *
     * @param <C>
     * @param item
     *            the content item to delegate to
     * @param type
     *            the interface providing additional methods with mappings to
     *            the triple store
     * @return
     *
     * @see kiwi.api.entity.KiWiEntityManager#createFacade(kiwi.model.content.ContentItem,
     *      java.lang.Class)
     */
    public <C extends KiWiEntity> C createFacade(ContentItem item, Class<C> type) {
        if(type.isInterface()) {
            // if the interface is a KiWiFacade, we execute the query and then
            // create an invocation handler for each result to create proxy objects

            if(type.isAnnotationPresent(KiWiFacade.class)) {

                KiWiInvocationHandler handler = new KiWiInvocationHandler(item,this.tripleStore);
                C entity =
                    (C) Proxy.newProxyInstance(type.getClassLoader(),
                                               new Class[] { type },
                                               handler);
                return entity;

            } else {
                throw new IllegalArgumentException("interface passed as parameter is not a KiWi Facade");
            }
        } else {
            throw new IllegalArgumentException("interface passed as parameter is not a KiWi Facade ("+ type.getCanonicalName() +")");
        }
    }

    /**
     * Create a list of KiWiFacade objects. The list only returns ContentItems
     * that have at least one of the RDF types listed in the RDFFilter
     * annotation, so it can also act as a filter that ensures that only the
     * relevant ContentItems are selected. Note that the performance of this
     * method is much slower than issuing appropriate selects to triple store or
     * database.
     *
     * Convenience function.
     *
     * @param <C>
     * @param items
     * @param type
     * @param filter perform filtering on the list
     * @see createFacade(ContentItem, Class<C>)
     * @return
     */
    public <C extends KiWiEntity> List<C> createFacadeList(Collection<ContentItem> items, Class<C> type) {
        return createFacadeList(items, type,true);
    }

    /**
     * Create a list of KiWiFacade objects. The list only returns ContentItems
     * that have at least one of the RDF types listed in the RDFFilter
     * annotation, so it can also act as a filter that ensures that only the
     * relevant ContentItems are selected. Note that the performance of this
     * method is much slower than issuing appropriate selects to triple store or
     * database.
     *
     * Convenience function.
     *
     * @param <C>
     * @param items
     * @param type
     * @param filter perform filtering on the list based on the @RDFFilter annotation
     * @see createFacade(ContentItem, Class<C>)
     * @return
     */
    public <C extends KiWiEntity> List<C> createFacadeList(Collection<ContentItem> items, Class<C> type, boolean filter) {
        log.debug("createFacadeList: creating #0 facade over #1 content items",type.getName(),items.size());
        LinkedList<C> result = new LinkedList<C>();
        if(filter) {
            // if the RDFType annotation is present, filter out content items that are of the wrong type
            LinkedList<KiWiUriResource> acceptable_types = new LinkedList<KiWiUriResource>();
            if(FacadeUtils.isFacadeAnnotationPresent(type,RDFFilter.class)) {
                String[]        a_type = FacadeUtils.getFacadeAnnotation(type,RDFFilter.class).value();
                for(String s_type : a_type) {
                    KiWiUriResource r_type = tripleStore.createUriResource(s_type);
                    acceptable_types.add(r_type);
                }
            }

            // add facades for all content items to the result list
            for(ContentItem item : items) {
                boolean accept = acceptable_types.size() == 0; // true for empty filter
                for(KiWiUriResource rdf_type : acceptable_types) {
                    if(item.getResource().hasType(rdf_type)) {
                        accept = true;
                        log.debug("accepting resource #0 because type matches (#1)",item.getResource().getKiwiIdentifier(),rdf_type.getKiwiIdentifier());
                        if(log.isDebugEnabled()) {
                            for(KiWiResource _type : item.getResource().getTypes()) {
                                log.debug("type: #0",_type.getKiwiIdentifier());
                            }
                        }
                        break;
                    }
                }
                if(accept) {
                    result.add(createFacade(item,type));
                }
            }
            log.debug("createFacadeList: filtered #0 content items because they did not match the necessary criteria",items.size()-result.size());
        } else {
            // add facades for all content items to the result list
            for(ContentItem item : items) {
                result.add(createFacade(item,type));
            }
        }
        return result;
    }

    /**
     * JPA compatibility method to create a query to the triple store that can
     * take named arguments similar to JPA/QL or HQL. Returns a
     * javax.persistence.Query object that runs the configured SPARQL query on
     * the triple store and loads the corresponding KiWiEntity object, properly
     * casted to the provided class.
     * <p>
     * If the type represents an interface, this method creates proxy classes
     * using an appropriate invocation handler that maps @RDF annotations on
     * method names to queries on the triple store.
     * <p>
     * If the interface extends ContentItemI, the returned proxy classes also
     * delegate to a ContentItem instance (TODO)
     * <p>
     * TODO: if type parameter passed as argument is a primitive type, we could
     * also run a literal query and return the transformed literal value; would
     * also simplify the TripleStoreInvocationHandler and provide a more generic
     * interface
     *
     * @param query
     *            a string representing the query in the specified query
     *            language (HQL, SPARQL)
     * @param qLang
     *            the query language used by the query string
     * @param type
     *            the return type for the results retrieved by the query
     * @return a JPA Query object representing the query passed as parameter;
     *         results will be instances of the type passed as argument
     *
     */
    public <C extends KiWiEntity> Query createQuery(final String query,final KiWiQueryLanguage qLang, final Class<C> type) {
        log.debug("calling method createQuery (query = #0)",query);

        return new KiWiQuery(this, query, qLang, type);

    }


    public <C extends KiWiEntity> Query createNamedQuery(final String queryName, final KiWiQueryLanguage qLang, final Class<C> type) {
        throw new UnsupportedOperationException("operation not supported");
    }




    /**
     * Find a KiWi entity that has the given type and uri as identifier.
     * @param <C>
     * @param type the KiWiFacade or KiWiEntity to use as return type
     * @param uri the URI of the KiWi entity to search for
     * @return
     */
    public <C extends KiWiEntity> C find(Class<C> type, String uri) {

        if(type.isInterface()) {

            if(type.isAnnotationPresent(KiWiFacade.class)) {
                Query hqlQueryUri = entityManager.createNamedQuery("contentItemService.byUri");
                hqlQueryUri.setParameter("uri", uri);
                hqlQueryUri.setHint("org.hibernate.cacheable", true);


                try {
                    ContentItem item = (ContentItem)hqlQueryUri.getSingleResult();
                    return createFacade(item, type);
                }
                catch (NoResultException ex) {
                    return null;
                }
            } else {
                throw new IllegalArgumentException("provided type is not a KiWi Facade");
            }
        } else if(type.equals(ContentItem.class)){
            Query hqlQueryUri = entityManager.createNamedQuery("contentItemService.byUri");
            hqlQueryUri.setParameter("uri", uri);
            hqlQueryUri.setHint("org.hibernate.cacheable", true);

            try {
                ContentItem item = (ContentItem)hqlQueryUri.getSingleResult();
                return (C)item;
            }
            catch (NoResultException ex) {
                return null;
            }
        } else {
            throw new IllegalArgumentException("the type "+type.getName()+" is not supported by KiWiEntityManager");
        }

    }

    /**
     * Merge the provided entity with this entity manager and return the merged object.
     *
     * @param <C>
     * @param entity
     * @return
     */
    public <C> C merge(C entity) {
//        if (!Identity.instance().hasPermission(entity, "update")) {
//            throw new AuthorizationException("You may not update the entity "+entity.toString());
//        }

        if(FacadeUtils.isKiWiFacade(entity.getClass())) {
            // for KiWi facades, we retrieve the delegated content item and persist it
            ContentItem item = ((KiWiInvocationHandler)Proxy.getInvocationHandler(entity)).getItem();

            tripleStore.merge(item);

            // should be autosaved!
            if(!entityManager.contains(item)) {
                item = entityManager.merge(item);
            }

            Events.instance().raiseEvent(KiWiEvents.ENTITY_PERSISTED, (KiWiEntity)entity);
            // facade getters and setters are persisted immediately (or "cascading"), so there is nothing further to
            // be done
            return (C)item;
        } else {
            // ordinary triples are persisted directly
            if(entity instanceof KiWiEntity) {
                tripleStore.merge((KiWiEntity)entity);
            }

            // should be autosaved!
            if(!entityManager.contains(entity)) {
                entity = entityManager.merge(entity);
            }

            if(entity instanceof KiWiEntity) {
                Events.instance().raiseEvent(KiWiEvents.ENTITY_PERSISTED, (KiWiEntity)entity);
            }
            return entity;
        }
    }

    /**
     * Persist a KiWi Entity in the data repositories. This method distinguishes between direct entities
     * and KiWi Facades and calls the persist methods of the underlying data repositories appropriately.
     *
     * @see kiwi.api.entity.KiWiEntityManager#persist(kiwi.model.kbase.KiWiEntity)
     */
    public <C> void persist(C entity) {
        // permission check on entity
//        if (!Identity.instance().hasPermission(entity, "insert")) {
//            throw new AuthorizationException("You may not persist the entity "+entity.toString());
//        }

        if(FacadeUtils.isKiWiFacade(entity.getClass())) {
            // for KiWi facades, we retrieve the delegated content item and persist it
            ContentItem item = ((KiWiInvocationHandler)Proxy.getInvocationHandler(entity)).getItem();

            if(item.isDeleted()) {
                // removes resource associated with item from triple store
                tripleStore.remove(item);
            } else {
                tripleStore.persist(item);
            }

            // avoid "detached entity" issues: if item already has a database id, call merge(),
            // otherwise call persist()
            if(item.getId() == null || item.getId() <= 0) {
                entityManager.persist(item);
            } else {
                item = entityManager.merge(item);
            }

            //solrService.remove(item);
            //solrService.persist(item);
            Events.instance().raiseEvent(KiWiEvents.ENTITY_PERSISTED, item);

            log.info("persisted content item id #0, title #1", item.getId(), item.getTitle());

            // support @RDFType annotation in facade
            if(entity instanceof KiWiEntity && FacadeUtils.isFacadeAnnotationPresent(entity.getClass(),RDFType.class)) {
                String[]        a_type = FacadeUtils.getFacadeAnnotation(entity.getClass(),RDFType.class).value();
                for(String s_type : a_type) {
                    KiWiUriResource r_type = tripleStore.createUriResource(s_type);
                    ((KiWiEntity)entity).getResource().addType(r_type);
                }
            }

            // facade getters and setters are persisted immediately (or "cascading"), so there is nothing further to
            // be done
        } else {
            // ordinary triples are persisted directly
            if(entity instanceof KiWiEntity) {

                // treat entities correctly if they have the deleted flag set
                if(((KiWiEntity)entity).isDeleted()) {
                    log.debug("entity is marked deleted, removing from triplestore");
                    tripleStore.remove((KiWiEntity)entity);
                } else {
                    tripleStore.persist((KiWiEntity)entity);
                }

                if(((KiWiEntity)entity).getId() == null) {
                    entityManager.persist(entity);
                } else {
                    entity = entityManager.merge(entity);
                }

                //solrService.remove((KiWiEntity)entity);
                //solrService.persist((KiWiEntity)entity);
                Events.instance().raiseEvent(KiWiEvents.ENTITY_PERSISTED, (KiWiEntity)entity);
            } else {

                Method m = null;
                Class clazz = entity.getClass();
                while(clazz != null && m == null) {
                    try {
                        m = clazz.getDeclaredMethod("getId");
                    } catch (SecurityException e1) {
                        log.error("Method getId() could not get retrieved " +
                                "because of security issues");
                        // try to persist the entity anyway
                        m = null;
                    } catch (NoSuchMethodException e1) {
                        // It the method doesn't exist in that way
                        // (Maybe the parameter is not a Long)
                        m = null;
                    }
                    clazz = clazz.getSuperclass();
                }

                // if method m could be retrieved
                if(m != null) {
                    Long id = null;
                    try {
                        // get the id
                        id = (Long) m.invoke(entity);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }

                    // avoid "detached entity" issues: if item already has a database id, call merge(),
                    // otherwise call persist()
                    if(id == null || id <= 0) {
                        entityManager.persist(entity);
                        if(log.isDebugEnabled()) {
                            if(entity instanceof KiWiTriple) {
                                KiWiTriple triple = (KiWiTriple) entity;
                                log.debug("Triple #0 persisted, id is: #1", triple, triple.getId());
                            } else if (entity instanceof MetadataUpdate) {
                                MetadataUpdate update = (MetadataUpdate) entity;
                                log.debug("MetadataUpdate #0 persisted, id is: #1", update, update.getId());
                            }
                        }
                    } else if(!entityManager.contains(entity)){
                        entity = entityManager.merge(entity);
                        if(log.isDebugEnabled()) {
                            if(entity instanceof KiWiTriple) {
                                KiWiTriple triple = (KiWiTriple) entity;
                                log.debug("Triple #0 merged, id is: #1", triple, triple.getId());
                            } else if (entity instanceof MetadataUpdate) {
                                MetadataUpdate update = (MetadataUpdate) entity;
                                log.debug("MetadataUpdate #0 merged, id is: #1", update, update.getId());
                            }
                        }
                    }
                } else {
                    entityManager.persist(entity);
                    if(log.isDebugEnabled()) {
                        if(entity instanceof KiWiTriple) {
                            KiWiTriple triple = (KiWiTriple) entity;
                            log.debug("Triple #0 persisted, id is: #1", triple, triple.getId());
                        } else if (entity instanceof MetadataUpdate) {
                            MetadataUpdate update = (MetadataUpdate) entity;
                            log.debug("MetadataUpdate #0 persisted, id is: #1", update, update.getId());
                        }
                    }
                }
            }
        }

    }

    /**
     * Refresh the state of the provided entity with the data from the data repositories.
     * @param <C>
     */
    public <C> void refresh(C entity) {

        if(FacadeUtils.isKiWiFacade(entity.getClass())) {
            // for KiWi facades, we retrieve the delegated content item and persist it
            ContentItem item = ((KiWiInvocationHandler)Proxy.getInvocationHandler(entity)).getItem();
            entityManager.refresh(item);
            tripleStore.refresh(item,false);
            // facade getters and setters are persisted immediately (or "cascading"), so there is nothing further to
            // be done
        } else {
            // ordinary triples are persisted directly
            entityManager.refresh(entity);
            if(entity instanceof KiWiEntity) {
                tripleStore.refresh((KiWiEntity)entity,false);
            }
        }
    }



    /* (non-Javadoc)
     * @see kiwi.api.entity.KiWiEntityManager#remove(java.lang.Object)
     */
    @Override
    public <C extends KiWiEntity> void remove(C entity) {
//        if (!Identity.instance().hasPermission(entity, "delete")) {
//            throw new AuthorizationException("You may not delete the entity "+entity.toString());
//        }

    	tripleStore.remove(entity);
    	
//    	entity.setDeletedOn(new Date());
//        entity.setDeleted(true);
       
    	entity.setDeleted(true);
    	
    	if(entity instanceof ContentItem){
    		ContentItem ci = (ContentItem) entity;
    		try{
    			
    			ci.setDeleted(true);
    			ci.setDeletedOn(new Date());
    			ci.setModified(new Date());
    			    			
	    		ci.getResource().setProperty(Constants.NS_DC_TERMS+"modified", new Date().toString());
	    		ci.getResource().setProperty(Constants.NS_OWL+"deprecated", "true");
	    		ci.getResource().setProperty(Constants.NS_KIWI_CORE+"deletedOn", new Date().toString());
    		}
    		catch(Exception e){
    			e.printStackTrace();
    		}
//    		ci.setModified(new Date());
//    		ci.setDeprecated(true);
//    		ci.setDeletedOn(new Date());	
    		
    		//persist(ci);
       	}

    		if(!entityManager.contains(entity) && entity.getId() != null) {
    			entity = entityManager.merge(entity);
    		} else {
    			entityManager.persist(entity);
    		}
    	
        log.debug("removed entity of type #0 with id #1",entity.getClass().getName(),entity.getId());

        Events.instance().raiseEvent(KiWiEvents.ENTITY_REMOVED, (KiWiEntity)entity);
    }
    
    @Override
    public <C extends KiWiEntity> void replace(C replacedEntity, C entity) {
//        if (!Identity.instance().hasPermission(entity, "delete")) {
//            throw new AuthorizationException("You may not delete the entity "+entity.toString());
//        }
    	
    	
    	
    	if(entity instanceof ContentItem){
    		ContentItem ciEnt = (ContentItem) entity;
    		ciEnt.setModified(new Date());
    	}
    	
		Collection<KiWiTriple> ck = replacedEntity.getResource().listOutgoing();
		
		try{
			for(KiWiTriple kt : ck){
				entity.getResource().addOutgoingNode(kt.getProperty(), kt.getObject());
			}
		}
		catch(Exception e){
				e.printStackTrace();
		}

    	tripleStore.remove(replacedEntity);
    	
//    	entity.setDeletedOn(new Date());
//        entity.setDeleted(true);
       
    	replacedEntity.setDeleted(true);
    	
    	if(replacedEntity instanceof ContentItem){
    		ContentItem ci = (ContentItem) replacedEntity;
    		try{
    			
    			ci.setDeleted(true);
    			ci.setDeletedOn(new Date());
    			ci.setModified(new Date());
    			    			
	    		ci.getResource().setProperty(Constants.NS_DC_TERMS+"modified", new Date().toString());
	    		ci.getResource().setProperty(Constants.NS_OWL+"deprecated", "true");
	    		ci.getResource().setProperty(Constants.NS_KIWI_CORE+"deletedOn", new Date().toString());
	    		ci.getResource().setProperty(kiwi.model.Constants.NS_DC_TERMS+"isReplacedBy", ((KiWiUriResource) entity.getResource()).getUri());
    		}
    		catch(Exception e){
    			e.printStackTrace();
    		}
    		

    		
    		
    		
//    		ci.setModified(new Date());
//    		ci.setDeprecated(true);
//    		ci.setDeletedOn(new Date());	
    		
    		//persist(ci);
       	}

    		if(!entityManager.contains(replacedEntity) && replacedEntity.getId() != null) {
    			replacedEntity = entityManager.merge(replacedEntity);
    		} else {
    			entityManager.persist(replacedEntity);
    		}
    	
        log.debug("removed entity of type #0 with id #1",replacedEntity.getClass().getName(),replacedEntity.getId());

        Events.instance().raiseEvent(KiWiEvents.ENTITY_REMOVED, (KiWiEntity)replacedEntity);
    }

    /**
     * Flush the underlying data repositories
     * 
     * @deprecated should be taken care of by transaction ...
     */
    @Deprecated
    public void flush() {
        entityManager.flush();
        //tripleStore.flush();
    }
}
