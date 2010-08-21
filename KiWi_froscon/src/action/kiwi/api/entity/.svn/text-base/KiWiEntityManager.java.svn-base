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
package kiwi.api.entity;

import java.util.Collection;
import java.util.List;

import javax.persistence.Query;

import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiEntity;
import kiwi.model.kbase.KiWiQueryLanguage;

/**
 * The KiWiEntityManager is a facade and delegator to the RDB entityManager and the Sesame 
 * tripleStore. It provides the most common functionalities for managing KiWi entities and
 * facades.
 * <p>
 * Although KiWiEntityManager does not have the same signature as an ordinary JPA EntityManager,
 * many methods closely resemble the EntityManager counterparts but with some extensions specific
 * to KiWi.
 * <p>
 * Particularly, the KiWiEntityManager has full support for KiWiFacades and KiWiEntities. Most
 * query methods accept a type as additional argument which may either be a facade or a 
 * KiWiEntity. Results are then returned as instances of this type rather than of the generic
 * ContentItem.
 * 
 * @author Sebastian Schaffert
 *
 */
public interface KiWiEntityManager {

	/**
	 * Persist the provided entity in the data repositories.
	 * 
	 * @param entity the entity to persist
	 * @param <C>    the type of the entity to persist
	 */
	public <C> void persist(C entity);
	
	/**
	 * Merge the provided entity with this entity manager and return the merged object.
	 * 
	 * @param <C>     the type of the entity to merge
	 * @param entity  the entity to merge with this KiWiEntityManager
	 * @return        the merged entity
	 */
	public <C> C merge(C entity);

	/**
	 * Refresh the state of the provided entity with the data from the data repositories.
	 * 
	 * @param <C>     the type of the entity to refresh
	 * @param entity  the entity to refresh
	 */
	public <C> void refresh(C entity);
	
	/**
	 * Remove the provided entity from the databases.
	 * 
	 * @param <C> the type of the entity to remove
	 * @param entity the entity to remove
	 */
	public <C extends KiWiEntity> void remove(C entity);
	
	public <C extends KiWiEntity> void replace(C entity, C replaceEntity);
	
	/**
	 * JPA compatibility method to create a query to the triple store that can take named
	 * arguments similar to JPA/QL or HQL. Returns a javax.persistence.Query object that runs
	 * the configured SPARQL query on the triple store and loads the corresponding KiWiEntity 
	 * object, properly casted to the provided class.
	 * <p>
	 * If the type represents an interface, this method creates proxy classes using an 
	 * appropriate invocation handler that maps @RDF annotations on method names to queries 
	 * on the triple store.
	 * <p>
	 * If the interface extends ContentItemI, the returned proxy classes also delegate to a
	 * ContentItem instance (TODO)
	 * <p>
	 * TODO: if type parameter passed as argument is a primitive type, we could also run a 
	 * literal query and return the transformed literal value; would also simplify the 
	 * KiWiInvocationHandler and provide a more generic interface
	 * 
	 * @param query
	 * @param lang
	 * @param type
	 * @param <C>
	 * @return
	 */
	public <C extends KiWiEntity> Query createQuery(String query, KiWiQueryLanguage lang, Class<C> type);


	/**
	 * Create a KiWiFacade of the given type over the given ContentItem. This
	 * method creates a proxy object that delegates ContentItem methods to the
	 * provided item and all other methods in type to the triple store (by using
	 * appropriate @RDF annotations on the methods).
	 * 
	 * @param item
	 *            the content item to delegate to
	 * @param type
	 *            the interface providing additional methods with mappings to
	 *            the triple store
	 * @param <C>
	 * 			  the parametrized type of the type parameter
	 * @return an instance of type masking the ContentItem item
	 */
	public <C extends KiWiEntity> C createFacade(ContentItem item, Class<C> type);
	
	
	/**
	 * Create a list of KiWiFacade objects from the collection of ContentItems
	 * provided as argument. Filters out ContentItems that are not matching the
	 * \@RDFFilter annotation of the KiWiFacade.
	 * 
	 * @param <C>
	 *            the parametrized type of the type parameter
	 * @param items
	 *            the collection of content items to facade
	 * @param type
	 *            the interface providing additional methods with mappings to
	 *            the triple store
	 * @see createFacade(ContentItem, Class<C>)
	 * @return a list of instances of type, masking the elements in the
	 *         collection items
	 */
	public <C extends KiWiEntity> List<C> createFacadeList(Collection<ContentItem> items, Class<C> type);
	
	/**
	 * Create a list of KiWiFacade objects from the collection of ContentItems
	 * provided as argument. Optionally filters out ContentItems that are not 
	 * matching the \@RDFFilter annotation of the KiWiFacade.
	 * 
	 * @param <C>
	 *            the parametrized type of the type parameter
	 * @param items
	 *            the collection of content items to facade
	 * @param type
	 *            the interface providing additional methods with mappings to
	 *            the triple store
	 * @param filter
	 * 			  perform filtering on the list based on the \@RDFFilter annotation
	 * @see createFacade(ContentItem, Class<C>)
	 * @return a list of instances of type, masking the elements in the
	 *         collection items
	 */
	public <C extends KiWiEntity> List<C> createFacadeList(Collection<ContentItem> items, Class<C> type, boolean filter);

	/**
	 * Find a KiWi entity that has the given type and uri as identifier.
	 * 
	 * @param type
	 *            the KiWiFacade or KiWiEntity to use as return type
	 * @param uri
	 *            the URI of the KiWi entity to search for
	 * @param <C>
	 *            the parametrized type of the type parameter
	 * @return the KiWiEntity with the given uri
	 */
	public <C extends KiWiEntity> C find(Class<C> type, String uri);
	
	
	/**
	 * Flush the underlying data repositories
	 */
	public void flush();
}
