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

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.query.sparql.SparqlService;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.annotations.KiWiFacade;
import kiwi.model.annotations.RDFFilter;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiAnonResource;
import kiwi.model.kbase.KiWiEntity;
import kiwi.model.kbase.KiWiQueryLanguage;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiUriResource;

import org.jboss.seam.Component;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;

/**
 * @author Sebastian Schaffert
 *
 */
public class KiWiQuery<C extends KiWiEntity> implements Query {

	private static final Pattern patNumParam = Pattern.compile("(:([a-zA-Z][a-zA-Z0-9]*))"); 
	
	/** maximum batch size for fetching entities by URI from the database. */
	private static final int MAX_RESOURCE_BATCH_SIZE = 30;
	
	private String query;
	private int limit = -1;
	private int offset = 0;
	
	private HashMap<Integer,Object> numParams = new HashMap<Integer,Object>();
	private HashMap<String,Object> nameParams = new HashMap<String,Object>();
	
	private LinkedList<KiWiUriResource> acceptableTypes = new LinkedList<KiWiUriResource>();

	private Query hqlQuery;
	
	private KiWiQueryLanguage qLang;
	
	
	private Class<C> type;
	
	protected KiWiQuery(KiWiEntityManager km, String query, KiWiQueryLanguage qLang, Class<C> type) {
		this.query = query;
		this.qLang = qLang;
		this.type  = type;
		
		if(FacadeUtils.isFacadeAnnotationPresent(type,RDFFilter.class)) {
			String[]      a_type = FacadeUtils.getFacadeAnnotation(type,RDFFilter.class).value();
			TripleStore   ts     = (TripleStore)       Component.getInstance("tripleStore");
			for(String s_type : a_type) {
				KiWiUriResource r_type = ts.createUriResource(s_type);
				acceptableTypes.add(r_type);
			}
		}
		
		// if the query language is HQL, create a delegate
		if(qLang == KiWiQueryLanguage.HQL) {
			EntityManager em     = (EntityManager)     Component.getInstance("entityManager");
			em.joinTransaction();
			hqlQuery             = em.createQuery(query);
		}
	}
	
	public int executeUpdate() {
		throw new UnsupportedOperationException("updating currently not supported");
	}

	/**
	 * Execute the query and return a list of resulting items.
	 * <p>
	 * This method differentiates between RDF queries on the one hand and HQL
	 * queries on the other hand. 
	 * <p>
	 * For RDF queries, it first retrieves a list of
	 * KiWiResources, which it then resolves to the corresponding KiWiEntities 
	 * (usually ContentItems) by executing batch queries to the relational database.
	 * If the type is a facade, the results are in addition facaded behind the
	 * respective type.
	 * <p>
	 * For HQL queries, it is basically just a wrapper around KiWiEntityManager
	 * createFacadeList.
	 * 
	 * @author Sebastian Schaffert
	 * @return the list of results of the query
	 * @see javax.persistence.Query#getResultList()
	 */
	public List getResultList() {
		Log log = Logging.getLog(KiWiQuery.class);
		if(qLang == KiWiQueryLanguage.SPARQL || qLang == KiWiQueryLanguage.SERQL) {
			List result = new LinkedList();
			
			// prepare query
	
			// first, set all parameters by replacing occurrences of ? and :ID 
			// with the unnamed or named parameters from the parameter maps
			Matcher matcher = patNumParam.matcher(query);
			StringBuffer builder = new StringBuffer();
			// ESCA-JAVA0123:
			for(int i = 0; matcher.find(); ) {
				String replacement = matcher.group(1);
				if(numParams.get(i) != null) {
					replacement = convertToSparql(numParams.get(i));
				} else if(nameParams.get(matcher.group(2)) != null) {
					replacement = convertToSparql(nameParams.get(matcher.group(2)));
				}
				matcher.appendReplacement(builder, replacement);
			}
			matcher.appendTail(builder);
			String qString = builder.toString();
			
			KiWiEntityManager km = (KiWiEntityManager) Component.getInstance("kiwiEntityManager");
			TripleStore   ts     = (TripleStore)       Component.getInstance("tripleStore");
			SparqlService ss     = (SparqlService)     Component.getInstance("kiwi.query.sparqlService");
			
			if(type.isInterface()) {
				// if the interface is a KiWiFacade, we execute the query and then create an 
				// invocation handler for each result to create proxy objects
				
				if(type.isAnnotationPresent(KiWiFacade.class)) {

					
					// if the RDFFilter annotation is present, filter out content items that are of the wrong type
					LinkedList<KiWiUriResource> acceptable_types = new LinkedList<KiWiUriResource>();
					if(FacadeUtils.isFacadeAnnotationPresent(type,RDFFilter.class)) {
						String[]        a_type = FacadeUtils.getFacadeAnnotation(type,RDFFilter.class).value();
						for(String s_type : a_type) {
							KiWiUriResource r_type = ts.createUriResource(s_type);
							acceptable_types.add(r_type);
						}
					}
					
					int i = 0;
					// execute query and iterate over results; for each result, look up the 
					// right object in the entity manager, load it, merge it with triple store, 
					// and add it to result list
					
					// as an optimisation, we use a batching procedure that fetches up to
					// MAX_RESOURCE_BATCH_SIZE ContentItems in one step; we collect KiWiResources
					// in a HashSet for this purpose and pass them to the fetchBatch method later
					HashSet<KiWiResource> resourceBatch = new HashSet<KiWiResource>();
					
					for(KiWiResource resource : ss.queryResource(qString, qLang)) {
						if( i >= offset && 
								(limit == -1 || result.size()+resourceBatch.size() < limit)) {
							
							// check whether the resource has an acceptable type for the facade
							boolean accept = acceptable_types.size() == 0; // true for empty filter
							for(KiWiUriResource rdf_type : acceptable_types) {
								if(resource.hasType(rdf_type)) {
									accept = true;
									log.debug("accepting resource #0 because type matches (#1)",resource.getKiwiIdentifier(),rdf_type.getKiwiIdentifier());
									if(log.isDebugEnabled()) {
										for(KiWiResource type : resource.getTypes()) {
											log.debug("type: #0",type.getKiwiIdentifier());
										}
									}
									break;
								}
							}
							if(accept) {
								resourceBatch.add(resource);
							}
						}
						
						if(resourceBatch.size() >= MAX_RESOURCE_BATCH_SIZE) {
							result.addAll(km.createFacadeList(fetchBatch(resourceBatch,ContentItem.class),type,false));
						}
						i++;
						if(limit != -1 && i-offset > limit) {
							break;
						}
					}
					if(resourceBatch.size() > 0) {
						result.addAll(km.createFacadeList(fetchBatch(resourceBatch,ContentItem.class),type,false));
					}
	
				} else {
					throw new IllegalArgumentException("only interfaces defined as KiWiFacade are supported in createQuery");
				}
				
			} else {
				// if the type is not an interface, try to retrieve the appropriate KiWiEntity from
				// the triplestore and database
				
				
				int i = 0;
				// execute query and iterate over results; for each result, look up the 
				// right object in the entity manager, load it, merge it with triple store, 
				// and add it to result list
				
				// as an optimisation, we use a batching procedure that fetches up to
				// MAX_RESOURCE_BATCH_SIZE ContentItems in one step; we collect KiWiResources
				// in a HashSet for this purpose and pass them to the fetchBatch method later
				HashSet<KiWiResource> resourceBatch = new HashSet<KiWiResource>();
				
				for(KiWiResource resource : ss.queryResource(qString, qLang)) {
					if( i >= offset && (limit == -1 || result.size()+resourceBatch.size() < limit)) {
						resourceBatch.add(resource);
					}
					
					if(resourceBatch.size() >= MAX_RESOURCE_BATCH_SIZE) {
						result.addAll(fetchBatch(resourceBatch,type));
					}
					
					i++;
					if(limit != -1 && i-offset > limit) {
						break;
					}
				}
				if(resourceBatch.size() > 0) {
					result.addAll(fetchBatch(resourceBatch,type));
				}
			}
			return result;
		} else {
			for(String param : nameParams.keySet()) {
				hqlQuery.setParameter(param, nameParams.get(param));
			}
			for(Integer param : numParams.keySet()) {
				hqlQuery.setParameter(param, numParams.get(param));
			}
			
			KiWiEntityManager km = (KiWiEntityManager) Component.getInstance("kiwiEntityManager");

			return km.createFacadeList(hqlQuery.getResultList(), type);
		}
	}
	
	/**
	 * Execute a batch fetch to the database to improve speed. Fetches all KiWi entities that are 
	 * associated with one of the resources passed in either uris or anonIds.
	 * @param <D>
	 * @param resourceBatch a set of resources for which to retrieve the KiWi entities
	 * @param type
	 * @return
	 */
	private <D extends KiWiEntity> List<D> fetchBatch(HashSet<KiWiResource> resourceBatch, Class<D> type) {
		// create a query in the entity manager
		EntityManager em     = (EntityManager)     Component.getInstance("entityManager");
		em.joinTransaction();
		
		Query query = em.createNamedQuery("kiwiEntityManager.query"+type.getSimpleName());
    	query.setHint("org.hibernate.cacheable", true);
		

		// initialise two sets that contain the uris of the KiWiUriResources and the anonIds of
		// KiWiAnonResources. These are used as parameters to the query
		HashSet<String> uris    = new HashSet<String>();
		HashSet<String> anonIds = new HashSet<String>();
		
		for(KiWiResource resource : resourceBatch) {
			if(resource.isUriResource()) {
				uris.add(((KiWiUriResource) resource).getUri());
			} else if(resource.isAnonymousResource()) {
				anonIds.add(((KiWiAnonResource) resource).getAnonId());
			}			
		}

		// add dummy elements, because hibernate is to stupid to support "IN ()" with empty
		// arrays
		if(anonIds.isEmpty()) {
			anonIds.add("dummy");
		}
		if(uris.isEmpty()) {
			uris.add("dummy");
		}
		
		query.setParameter("anonIds", anonIds);
		query.setParameter("uris", uris);
		
		// execute query
		List<D> result = query.getResultList();
		
		
		// check whether there was a KiWi entity for each of the provided resource identifiers
		// if no, check which ones are missing and create them manually
		if(result.size() < uris.size() + anonIds.size()) {
			KiWiEntityManager km = (KiWiEntityManager) Component.getInstance("kiwiEntityManager");

			// create a hash set of found resources from the result for easy contains() check
			HashSet<KiWiResource> foundResources = new HashSet<KiWiResource>();
			for(KiWiEntity e : result) {
				foundResources.add(e.getResource());
			}
			
			// check for all resources; if not found, create the corresponding resource and 
			// persist it; then, add it to the result
			for(KiWiResource searched : resourceBatch) {
				if(!foundResources.contains(searched)) {
					try {
						KiWiEntity entity = type.newInstance();
						entity.setResource(searched);
						km.persist(entity);
						result.add((D)entity);
					} catch (InstantiationException e) {
					} catch (IllegalAccessException e) {
					}					
				}
			}
		}
		
		resourceBatch.clear();
		return result;
	}
	

	public Object getSingleResult() throws NoResultException {
		setMaxResults(1);
		List l = getResultList();
		if(l.size() > 0) {
			return l.get(0);
		} else {
			throw new NoResultException("no results for query: "+query);
		}
	}

	public Query setFirstResult(int arg0) {
		this.offset = arg0;
		return this;
	}

	public Query setFlushMode(FlushModeType arg0) {
		return this;
	}

	public Query setHint(String arg0, Object arg1) {
		return this;
	}

	public Query setMaxResults(int arg0) {
		this.limit = arg0;
		return this;
	}

	public Query setParameter(int arg0, Calendar arg1, TemporalType arg2) {
		numParams.put(arg0, arg1);
		return this;
	}

	public Query setParameter(int arg0, Date arg1, TemporalType arg2) {
		numParams.put(arg0, arg1);
		return this;
	}

	public Query setParameter(int arg0, Object arg1) {
		numParams.put(arg0, arg1);
		return this;
	}

	public Query setParameter(String arg0, Calendar arg1, TemporalType arg2) {
		nameParams.put(arg0, arg1);
		return this;
	}

	public Query setParameter(String arg0, Date arg1, TemporalType arg2) {
		nameParams.put(arg0, arg1);
		return this;
	}

	public Query setParameter(String arg0, Object arg1) {
		nameParams.put(arg0, arg1);
		return this;
	}
	
	// TODO: this needs to be extended to support more types
	private String convertToSparql(Object o) {
		if(o instanceof KiWiEntity) {
		    KiWiEntity e = (KiWiEntity)o;
		    return e.getResource().getSeRQLID();
		} else if(o.toString().startsWith("<") || o.toString().contains(":")) {
			return o.toString();
		} else {
			return "\""+o.toString()+"\"";
		}
		
	}

}
