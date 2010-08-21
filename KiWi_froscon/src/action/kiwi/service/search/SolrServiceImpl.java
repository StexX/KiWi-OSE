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
package kiwi.service.search;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.transaction.SystemException;

import kiwi.api.config.ConfigurationService;
import kiwi.api.content.ContentItemService;
import kiwi.api.equity.EquityService;
import kiwi.api.event.KiWiEvents;
import kiwi.api.fragment.FragmentFacade;
import kiwi.api.fragment.FragmentService;
import kiwi.api.search.KiWiSearchCriteria;
import kiwi.api.search.KiWiSearchResults;
import kiwi.api.search.KiWiSearchResults.KiWiFacet;
import kiwi.api.search.KiWiSearchResults.SearchResult;
import kiwi.api.search.SolrServiceLocal;
import kiwi.api.search.SolrServiceRemote;
import kiwi.api.system.StatusService;
import kiwi.api.tagging.TaggingService;
import kiwi.api.triplestore.TripleStore;
import kiwi.api.user.UserService;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiEntity;
import kiwi.model.kbase.KiWiLiteral;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.revision.CIVersion;
import kiwi.model.status.SystemStatus;
import kiwi.model.tagging.Tag;
import kiwi.model.user.User;
import kiwi.service.config.ConfigurationServiceImpl;
import kiwi.util.MD5;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.impl.StreamingUpdateSolrServer;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.SolrCore;
import org.hibernate.proxy.HibernateProxy;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.transaction.Transaction;

/**
 * Implementation of the Solr search service. The methods persist and remove can be called either
 * directly or by raising the events kiwiEntityPersisted and kiwiEntityRemoved.
 *
 * @author Sebastian Schaffert
 *
 */
@Name("solrService")
@Scope(ScopeType.SESSION) // session scoped, so each user has its own search component
@AutoCreate
public class SolrServiceImpl implements
		SolrServiceLocal, SolrServiceRemote, Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Logger
	private Log log;

	@In
	EquityService equityService;

	@In
	private EntityManager entityManager;

	@In
	private FragmentService fragmentService;

	@In
	private ConfigurationService configurationService;

	@In
	private TaggingService taggingService;

	private SolrServer server;

	private static EmbeddedSolrServer testServer = null;

	/* keep track of number of persisted documents; each 1000 documents, we call server.optimize() */
	private int doc_counter = 0;

	private Set<KiWiEntity> entityQueue;

	@Create
	public void begin() {
		log.info("Initialising new SOLR search service...");
		this.entityQueue = new HashSet<KiWiEntity>();

		// initialise server
		//getSolrServer();
	}

	/**
	 * When a new entity is persisted, first add it to the queue to enable batch processing; when
	 * the transaction completes, we'll run the queue asynchronously.
	 * @param entity
	 */
	@Override
    @BypassInterceptors
	@Observer(value=KiWiEvents.ENTITY_PERSISTED,create=true)
//	@Transactional(TransactionPropagationType.MANDATORY)
	public void enqueue(KiWiEntity entity) {
		entityQueue.add(entity);
	}


	@Override
    @Observer(value=KiWiEvents.TRANSACTION_SUCCESS,create=false)
//	@Asynchronous
	public void runQueue() {
		log.info("running indexing queue after transaction end (size=#0)",entityQueue.size());
		if(entityQueue.size() > 0) {
			persistAll(entityQueue);
			entityQueue.clear();
		}
	}


	@Observer(value=KiWiEvents.TRANSACTION_ABORT,create=false)
	public void abortQueue() {
		log.info("transaction aborted, not indexing entities...");
		entityQueue.clear();
	}


	/* (non-Javadoc)
	 * @see kiwi.api.search.SolrService#persist(kiwi.model.kbase.KiWiEntity)
	 */
	@Override
//	@Transactional
//	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void persist(KiWiEntity entity) {
		final SolrServer server = getSolrServer();

		doc_counter += 1;

		try {
			server.add(createDocument(entity));
			server.commit();

			checkOptimize();
		} catch(final IOException ex) {
			log.warn("document could not be indexed: an I/O Exception occurred",ex);
		} catch (final SolrServerException e) {
			log.warn("document could not be indexed: a SOLR Exception occurred (server not available?)",e);
		} catch(final Exception ex) {
			log.warn("document could not be indexed: a runtime Exception occurred (server sending invalid response?)",ex);
		}
	}

	/**
	 * Create a SOLR input document from a KiWi entity
	 * @param entity
	 * @return
	 */
	private SolrInputDocument createDocument(KiWiEntity entity) {

		if(entity.getId() != null && !entityManager.contains(entity)) {
			log.debug(">>>>>>>>>>>>>>>> solrService.createDocument() entity must be merged ");
			//entity = entityManager.merge(entity);
			if(entity instanceof ContentItem) {
				entity = entityManager.find(ContentItem.class,entity.getId());
			} else if(entity instanceof User) {
				entity = entityManager.find(User.class,entity.getId());
			} else if(entity instanceof Tag){
				entity = entityManager.find(Tag.class,entity.getId());
			} else {
				log.warn("unknown KiWi entity type: #0",entity.getClass().getName());
			}
		}

		// do I really need to merge here? The entity is just queried, not updated in any way

		final SolrInputDocument doc = new SolrInputDocument();
//		if(entity == null) {
//			log.error("!!!!!!!!! ENTITY IS NULL");
//		} else if(entity.getResource() == null) {
//			log.error("!!!!!!!!! ENTITY.GETRESOURCE() IS NULL");
//		} else if(entity.getResource().getContentItem() == null) {
//			log.error("!!!!!!!!! ENTITY.GETRESOURCE().GETCONTENTEITEM() IS NULL");
//		} else if(entity.getResource().getContentItem().getId() == null) {
//			log.error("!!!!!!!!! ENTITY.GETRESOURCE().GETCONTENTEITEM().GETID() IS NULL");
//		}
		doc.addField("id", entity.getResource().getContentItem().getId());

		if(entity.getResource().isUriResource()) {
			KiWiUriResource r;
			// workaround for hibernate lazy proxies that are not instances of KiWiUriResource
			if (entity.getResource() instanceof HibernateProxy) {
				r = (KiWiUriResource)((HibernateProxy)entity.getResource()).getHibernateLazyInitializer().getImplementation();
			} else {
				r = (KiWiUriResource)entity.getResource();
			}


			doc.addField("uri", r.getUri());
		}

		doc.addField("kiwiid", entity.getResource().getKiwiIdentifier());

		// for Lucene Semantic Vectors (require a "path" field as ID):
		doc.addField("path", entity.getResource().getKiwiIdentifier());

		// RDF properties: for each literal property, the URI is used as key, converted to MD5 sum;
		// we store for each property the following fields:
		// - l_KEY: the literal content of the property as tokenized text
		// - i_key: the integer value of the property (if parsable), sortable and queriable
		// - d_key: the double value of the property (if parsable), sorable and queriable
		// for each URI property, the URI is used as key, converted to MD5 sum; we store for each
		// property the following fields:
		// - u_key: the URI of the Uri resource
		for(final KiWiTriple t : entity.getResource().listOutgoing()) {
			if(t.getObject() instanceof KiWiLiteral) {
				final KiWiLiteral l = (KiWiLiteral) t.getObject();
				
				final String key = MD5.md5sum(t.getProperty().getUri());
				log.debug(t.getProperty().getUri());
				log.debug(key);
				doc.addField("l_"+key, l.getContent());
				if(l.getDoubleContent() != null) {
					doc.addField("d_"+key, l.getDoubleContent());
				}
				if(l.getIntContent() != null) {
					doc.addField("i_"+key, l.getIntContent());
				}
			} else if(t.getObject() instanceof KiWiResource) {
				
				final KiWiResource u = (KiWiResource) t.getObject();

				final String key = MD5.md5sum(t.getProperty().getUri());
				log.debug("u1"+t.getProperty().getUri());
				log.debug("u2"+key);
				log.debug("u3"+u.getKiwiIdentifier());
				log.debug("--> u_"+key + " " + u.getKiwiIdentifier());
				doc.addField("u_"+key, u.getKiwiIdentifier());
			}
		}

		final ContentItem item = entity.getResource().getContentItem();

		doc.addField("title",item.getTitle(),10);
		doc.addField("title_id",item.getTitle(),10);

		if(item.getTextContent() != null) {
            doc.addField("content",item.getTextContent().getPlainString());
        }

		// types: we store all types using their kiwi identifier
		for(final KiWiResource type : item.getTypes()) {
			doc.addField("type",type.getKiwiIdentifier());
		}

		if(item.getAuthor() != null) {
			doc.addField("author",item.getAuthor().getFirstName()+" "+item.getAuthor().getLastName());
			doc.addField("author_login",item.getAuthor().getLogin());
		}

		final Set<String> authorLogins = new HashSet<String>();
		final Set<String> authorNames = new HashSet<String>();

		for (final CIVersion version : item.getVersions()) {
			final User user = version.getRevision().getUser();

			if (user != null) {
				authorLogins.add(user.getLogin());
				authorNames.add(user.getFirstName()+" "+user.getLastName());
			}
		}

		for (final String author : authorNames) {
			doc.addField("authors", author);
		}

		for (final String login : authorLogins) {
			doc.addField("author_logins", login);
		}

		doc.addField("modified",item.getModified());
		doc.addField("created",item.getCreated());
		doc.addField("edits", item.getVersions().size());


		// tags: we eliminate duplicates and take the tag title (TODO: we could use kiwi identifier
		// instead, would be more reliable/shift resolving of identifier to other place...)

		final Set<String> tagNames = new HashSet<String>();
		final Set<String> purposes = new HashSet<String>();
		final Set<String> tagAuthorLogins = new HashSet<String>();
		final Set<String> tagAuthorNames = new HashSet<String>();
		final Set<String> tagURIs = new HashSet<String>();
		final Set<Date> tagCreationDates = new HashSet<Date>();

		for(final Tag t : taggingService.getTags(item)) {
			try {
				tagNames.add(t.getTaggingResource().getTitle());
				purposes.add(t.getPurpose());
				tagCreationDates.add(t.getCreationTime());
				tagURIs.add(t.getResource().getKiwiIdentifier());

				tagAuthorLogins.add(t.getTaggedBy().getLogin());
				tagAuthorNames.add(t.getTaggedBy().getFirstName()+" "+t.getTaggedBy().getLastName());
			} catch(final Exception ex) {
				log.error("while indexing tag: #0; exception #1", t.getTaggingResource().getTitle(), ex.getMessage());
				log.error(ex);
			}
		}

		for(final String tag : tagNames) {
			doc.addField("tag",tag);
		}

		for(final String purpose : purposes) {
			doc.addField("purpose",purpose);
		}

		for (final String author : tagNames) {
			doc.addField("tag_names", author);
		}

		for (final String uri : tagURIs) {
			doc.addField("tag_uris", uri);
		}

		for (final String author : tagAuthorNames) {
			doc.addField("tag_authors", author);
		}

		for (final String login : tagAuthorLogins) {
			doc.addField("tag_author_logins", login);
		}

		for (final Date date : tagCreationDates) {
			doc.addField("tag_created", date);
		}


		//fragments

		if (item.getTextContent() != null) {
			final Set<String> fragmentURIs = new HashSet<String>();
			final Set<String> fragmentAuthorLogins = new HashSet<String>();
			final Set<String> fragmentAuthorNames = new HashSet<String>();
			final Set<Date> fragmentCreationDates = new HashSet<Date>();

			final Set<String> fragmentTagNames = new HashSet<String>();
			final Set<String> fragmentTagURIs = new HashSet<String>();
			final Set<String> fragmentTagAuthorLogins = new HashSet<String>();
			final Set<String> fragmentTagAuthorNames = new HashSet<String>();
			final Set<Date> fragmentTagCreationDates = new HashSet<Date>();

			for (final String fragmentId : item.getTextContent().getFragmentIds()) {
				final FragmentFacade ff = fragmentService.getContentItemFragment(item, fragmentId, FragmentFacade.class);

				if (ff == null) {
					continue;
				}

				final ContentItem delegate = ff.getDelegate();

				for(final Tag t : taggingService.getTags(delegate)) {
					fragmentTagNames.add(t.getTaggingResource().getTitle());
					fragmentTagURIs.add(t.getResource().getKiwiIdentifier());
					fragmentTagCreationDates.add(t.getCreationTime());
					fragmentTagAuthorLogins.add(t.getTaggedBy().getLogin());
					fragmentTagAuthorNames.add(t.getTaggedBy().getFirstName()+" "+t.getTaggedBy().getLastName());
				}


				fragmentURIs.add(ff.getKiwiIdentifier());
				fragmentCreationDates.add(ff.getCreated());

				for (final CIVersion version : delegate.getVersions()) {
					final User user = version.getRevision().getUser();

					if (user != null) {
						fragmentAuthorLogins.add(user.getLogin());
						fragmentAuthorNames.add(user.getFirstName()+" "+user.getLastName());
					}
				}
			}

			for (final String author : fragmentAuthorNames) {
				doc.addField("fragment_authors", author);
			}

			for (final String login : fragmentAuthorLogins) {
				doc.addField("fragment_author_logins", login);
			}

			for (final String uri : fragmentURIs) {
				doc.addField("fragment_uris", uri);
			}

			for (final Date date : fragmentCreationDates) {
				doc.addField("fragment_created", date);
			}

			for (final String author : fragmentTagNames) {
				doc.addField("fragment_tag_names", author);
			}

			for (final String uri : fragmentTagURIs) {
				doc.addField("fragment_tag_uris", uri);
			}


			for (final String author : fragmentTagAuthorNames) {
				doc.addField("fragment_tag_authors", author);
			}

			for (final String login : fragmentTagAuthorLogins) {
				doc.addField("fragment_tag_author_logins", login);
			}

			for (final Date date : fragmentTagCreationDates) {
				doc.addField("fragment_tag_created", date);
			}
		}

		// semantic vectors: we want the title and the content in one field with filtered stoplist;
		// we add the title twice to "boost" the importance of words occurring in the title
		String semVector = item.getTitle() + " " + item.getTitle();
		if(item.getTextContent() != null) {
			semVector += " " + item.getTextContent().getPlainString();
		}
		doc.addField("semvector",semVector);


		// the mihai : during import the phase the stored procedures are
		// not installed that's why here you will get an (RT)Exception.
//		final double contentItemEquity = ConfigurationServiceImpl.setupInProgress
//								   ? 0
//								   : equityService.getContentItemEquity(item);
		
		//FIXME
		final double contentItemEquity = 0;
		//add ceq
		doc.addField("ceq", contentItemEquity);

		return doc;
	}

	/* (non-Javadoc)
	 * @see kiwi.api.search.SolrService#remove(kiwi.model.kbase.KiWiEntity)
	 */
	@Override
	@Observer(value=KiWiEvents.ENTITY_REMOVED,create=true)
//	@Transactional
//	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void remove(KiWiEntity entity) {
		try {
			getSolrServer().deleteById(entity.getId().toString());

			doc_counter += 1;

			checkOptimize();
		} catch(final IOException ex) {
			log.warn("document could not be indexed: an I/O Exception occurred",ex);
		} catch (final SolrServerException e) {
			log.warn("document could not be indexed: a SOLR Exception occurred (server not available?)",e);
		} catch(final Exception ex) {
			log.warn("document could not be indexed: a runtime Exception occurred (server sending invalid response?)",ex);
		}
	}




	@Override
//	@Transactional
//	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void persistAll(Collection<? extends KiWiEntity> entities) {
		final Set<SolrInputDocument> docs = new HashSet<SolrInputDocument>();

		// add items to the SOLR server, in batches of 100
		for(final KiWiEntity item : entities) {
			docs.add(createDocument(item));

			if(docs.size() >= 100) {
				doc_counter += docs.size();

				final SolrServer server = getSolrServer();

				try {
					server.add(docs);
					server.commit();
				} catch(final IOException ex) {
					log.warn("document could not be indexed: an I/O Exception occurred",ex);
				} catch (final SolrServerException e) {
					log.warn("document could not be indexed: a SOLR Exception occurred (server not available?)",e);
				} catch(final Exception ex) {
					log.warn("document could not be indexed: a runtime Exception occurred (server sending invalid response?)",ex);
				}
				docs.clear();
			}
		}

		if(docs.size() > 0) {
			final SolrServer server = getSolrServer();

			doc_counter += docs.size();

			try {
				server.add(docs);
				server.commit();

				checkOptimize();
			} catch(final IOException ex) {
				log.warn("document could not be indexed: an I/O Exception occurred",ex);
			} catch (final SolrServerException e) {
				log.warn("document could not be indexed: a SOLR Exception occurred (server not available?)",e);
			} catch(final Exception ex) {
				log.warn("document could not be indexed: a runtime Exception occurred (server sending invalid response?)",ex);
			}
		}
	}

	private void checkOptimize() {
		if(doc_counter > 1000) {
			try {
				getSolrServer().optimize();
			} catch (final SolrServerException e) {
				log.error("could not optimize search index: a SOLR Exception occurred (server not available?)",e);
			} catch (final IOException e) {
				log.error("could not optimize search index: an I/O Exception occurred",e);
			}
			doc_counter = 0;
		}

	}


	@Override
//	@Transactional
//	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void removeAll(Collection<? extends KiWiEntity> entities) {
		// TODO Auto-generated method stub
		for(final KiWiEntity e : entities) {
			remove(e);
		}
	}

	@Override
	public KiWiSearchResults search(KiWiSearchCriteria criteria) {
		final SolrQuery query = buildSolrQuery(criteria);


		query.setFields("id","title", "title_id", "kiwiid", "tag", "score", "ceq");
		query.setHighlight(true);
		query.setHighlightFragsize(200);
		query.setHighlightSimplePre("<span class=\"highlight\">");
		query.setHighlightSimplePost("</span>");

		if( criteria.getSortField() != null && criteria.getSortOrder() != null ) {
            query.setSortField(criteria.getSortField(),criteria.getSortOrder());
        }

		// 5. run the query and return the results as SOLR response object, and convert them to
		//    a KiWiSearchResults
		try {
			final QueryResponse rsp = getSolrServer().query(query);

			log.debug(rsp);
			log.debug(rsp.getHighlighting());

			final SolrDocumentList docs = rsp.getResults();

			final KiWiSearchResults result = new KiWiSearchResults();


			// 5.1 set basic result properties
			result.setResultCount(docs.getNumFound());

			// 5.2 convert list of Solr documents to list of contentitem results
			for(final SolrDocument doc : docs) {
				try {
					final Long id = Long.parseLong(doc.getFieldValue("id").toString());

					log.debug("looking up content item with id #0", id);

					final ContentItem item = entityManager.find(ContentItem.class, id);

					final SearchResult r = new SearchResult(item,doc,(Float)doc.getFieldValue("score"),(Double)doc.getFieldValue("ceq"));


					if(rsp.getHighlighting().get(id.toString()) != null) {
						final List<String> highlightSnippets = rsp.getHighlighting().get(id.toString()).get("content");
						if(highlightSnippets != null && highlightSnippets.size() > 0) {
							r.setHighlightPreview(highlightSnippets.get(0));
						}
					}

					result.getResults().add(r);

				} catch(final NumberFormatException ex) {
					log.error("id field was not a valid integer value: #0",doc.getFieldValue("id").toString());
				}
			}

			// 5.3 set facet results

			// author facet (full name -> user)
			final UserService us = (UserService)Component.getInstance("userService");
			final FacetField field_a = rsp.getFacetField("author_login");
			if(field_a != null && field_a.getValues() != null) {
				for(final FacetField.Count c : field_a.getValues()) {
					if(c.getName() == null){
						log.debug("name = null");
					}
					log.debug(c.getName());
					final User u = us.getUserByLogin(c.getName());
					if(u == null){
						log.debug("user = null");
					}
					log.debug(u.getLogin());

					result.getPersonFacets().add(new KiWiFacet<User>(u,c.getCount(),"+author_login:\""+u.getLogin()+"\""));
				}
			}

			// tag facet (title -> content item)
			final ContentItemService cs = (ContentItemService)Component.getInstance("contentItemService");
			final FacetField field_t = rsp.getFacetField("tag");
			if(field_t != null && field_t.getValues() != null) {
				for(final FacetField.Count c : field_t.getValues()) {
					final ContentItem ci = cs.getContentItemByTitle(c.getName());
					result.getTagFacets().add(new KiWiFacet<ContentItem>(ci,c.getCount(),"+tag:\""+c.getName()+"\""));
				}
			}

			final FacetField field_pp = rsp.getFacetField("purpose");
			if(field_pp != null && field_pp.getValues() != null) {
				for(final FacetField.Count c : field_pp.getValues()) {
					result.getPurposeFacets().add(new KiWiFacet<String>(c.getName(),c.getCount(),"+purpose:\""+c.getName().trim()+"\""));
				}
			}

			// type facet (kiwi id -> kiwi resource)
			final FacetField field_tp = rsp.getFacetField("type");
			if(field_tp != null && field_tp.getValues() != null) {
				for(final FacetField.Count c : field_tp.getValues()) {
					final ContentItem ci = cs.getContentItemByKiwiId(c.getName());
					result.getTypeFacets().add(new KiWiFacet<KiWiResource>(ci.getResource(),c.getCount(),"+type:\""+c.getName()+"\""));
				}
			}

			// rdf literal facets
			final TripleStore ts = (TripleStore)Component.getInstance("tripleStore");
			for(final String prop_uri : criteria.getRdfLiteralFacets()) {
				log.debug("rdf literal facet #0, md5=#1",prop_uri,MD5.md5sum(prop_uri));
				final FacetField field_p = rsp.getFacetField("l_"+MD5.md5sum(prop_uri));
				if(field_p != null && field_p.getValues() != null) {
					final Set<KiWiFacet<String>> s = new HashSet<KiWiFacet<String>>();
					for(final FacetField.Count c : field_p.getValues()) {
						s.add(new KiWiFacet<String>(c.getName(),c.getCount(),"+l_"+MD5.md5sum(prop_uri)+":\""+c.getName()+"\""));
					}

					result.getPropertyFacets().put(ts.createUriResource(prop_uri),s);
				}
			}

			for(final String prop_uri : criteria.getRdfObjectFacets()) {
				log.debug("rdf object facet #0, md5=#1",prop_uri,MD5.md5sum(prop_uri));
				final FacetField field_p = rsp.getFacetField("u_"+MD5.md5sum(prop_uri));
				if(field_p != null && field_p.getValues() != null) {
					final Set<KiWiFacet<KiWiResource>> s = new HashSet<KiWiFacet<KiWiResource>>();
					for(final FacetField.Count c : field_p.getValues()) {
						final String name = c.getName();
						KiWiResource r = null;
						if(name.startsWith("uri::")) {
							r = ts.createUriResource(name.substring(5));
						} else if(name.startsWith("bnode::")){
							r = ts.createAnonResource(name.substring(7));
						}
						if(r != null) {
							s.add(new KiWiFacet<KiWiResource>(r,c.getCount(),"+u_"+MD5.md5sum(prop_uri)+":\""+r.getKiwiIdentifier()+"\""));
						}
					}

					result.getObjectPropertyFacets().put(ts.createUriResource(prop_uri),s);
				}
			}

			return result;
		} catch (final SolrServerException e) {
			log.error("error while running SOLR search");
			final FacesMessages m = (FacesMessages) Component.getInstance("facesMessages");
			m.add("error while running SOLR search: #0", e.getMessage());
			return new KiWiSearchResults();
		} catch(final Exception ex) {
			final FacesMessages m = (FacesMessages) Component.getInstance("facesMessages");
			m.add("error while running SOLR search: #0", ex.getMessage());
			ex.printStackTrace();
			return new KiWiSearchResults();
		}

	}

	/**
	 * Run a direct SOLR query by calling query() on the server with a SolrQuery as argument.
	 * Returns the QueryResponse returned by the server.
	 * <p>
	 * This method may be used if there is need for specialised search that requires direct SOLR
	 * access.
	 *
	 * @param query
	 * @return
	 */
	public QueryResponse search(SolrQuery query) {
		try {
			return getSolrServer().query(query);
		} catch (final SolrServerException e) {
			log.error("error while running SOLR search",e);
			final FacesMessages m = (FacesMessages) Component.getInstance("facesMessages");
			m.add("error while running SOLR search: #0", e.getMessage());
			return null;
		} catch(final Exception ex) {
			final FacesMessages m = (FacesMessages) Component.getInstance("facesMessages");
			m.add("error while running SOLR search: #0", ex.getMessage());
			ex.printStackTrace();
			return null;
		}
	}

	private synchronized SolrServer getSolrServer(){
	    //the instance can be reused
		if(server == null) {
			try {
				// TODO: should use rather a configured URI from configurationService
				//server = new CommonsHttpSolrServer(configurationService.getConfiguration("kiwi.solr.server", configurationService.getServerUri()+"/SOLR").getStringValue());
				server = new StreamingUpdateSolrServer(configurationService.getConfiguration("kiwi.solr.server", configurationService.getServerUri()+"/SOLR").getStringValue(),100,4);
				server.ping();
				return server;
			} catch(final IOException ex) {
				// io error while communicating with SOLR server
				log.error("no SOLR HTTP server available; reverting to embedded mode");
				server = getTestServer();
			} catch (final SolrServerException e) {
				// communication problem with SOLR server
				log.error("error communicating with SOLR HTTP server; reverting to embedded mode");
				server = getTestServer();
			} catch (final SolrException e) {
				// no SOLR server available at URI, but host running
				log.error("error communicating with SOLR HTTP server; reverting to embedded mode");
				server = getTestServer();
			}
		}
		return server;
	}

	// embedded solr server needs to be static
	private static EmbeddedSolrServer getTestServer() {
		if(testServer == null) {
			final CoreContainer container = new CoreContainer();
			try {
				final SolrCore core = SolrCore.getSolrCore();
			    container.register("core1", core, false);
			    testServer = new EmbeddedSolrServer(container, "core1");
			} catch (final Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return testServer;
	}

	@Override
//	@Transactional
//	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void optimize() {
		try {
			getSolrServer().optimize();
		} catch(final IOException ex) {
			log.warn("could not optimize index: an I/O Exception occurred",ex);
		} catch (final SolrServerException e) {
			log.warn("could not optimize index: a SOLR Exception occurred (server not available?)",e);
		} catch(final Exception ex) {
			log.warn("could not optimize index: a runtime Exception occurred (server sending invalid response?)",ex);
		}

	}

	/**
	 * Build a SolrQuery from KiWiSearchCriteria. Creates an appropriate Solr search string from
	 * the criteria's fields. The SolrQuery is returned for further modification (setting additional
	 * facets, setting which fields to retrieve, ...).
	 *
	 * @param criteria
	 * @return
	 */
	@Override
	public SolrQuery buildSolrQuery(KiWiSearchCriteria criteria) {
		final SolrQuery query = new SolrQuery();

		// 1. build the query string from the search criteria
		final Map<String,Integer> defaultFields = new HashMap<String,Integer>();
		defaultFields.put("title",1);
		defaultFields.put("+l_" + MD5.md5sum(getUriByNsName("foaf","nick")),1);
		defaultFields.put("+l_" + MD5.md5sum(getUriByNsName("foaf","firstName")),1);
		defaultFields.put("+l_" + MD5.md5sum(getUriByNsName("foaf","name")),1);
		defaultFields.put("+l_" + MD5.md5sum(getUriByNsName("foaf","family_name")),1);
		defaultFields.put("+l_" + MD5.md5sum(getUriByNsName("foaf","familyName")),1);
		defaultFields.put("+l_" + MD5.md5sum(getUriByNsName("foaf","surname")),1);
		defaultFields.put("+l_" + MD5.md5sum(getUriByNsName("foaf","lastName")),1);
		defaultFields.put("tag",8);
		defaultFields.put("purpose",1);
		defaultFields.put("content",1);
		defaultFields.put("author", 2);
		defaultFields.put("author_login", 2);


		for(final String field : criteria.getKeywordSearchFields().keySet()) {
			defaultFields.put(field, criteria.getKeywordSearchFields().get(field));
		}

		// keyword search: title weighs 4 times as much as content
		final StringBuilder qString = new StringBuilder();
		log.debug(qString.toString());
		if(criteria.getSolrSearchString() != null && !criteria.getSolrSearchString().equals("")) {
			qString.append(criteria.getSolrSearchString());
			qString.append(" ");
		}

		if(criteria.getKeywords() != null && !criteria.getKeywords().equals("")) {
			qString.append("(");

			// parse search string into its components

			final Set<String> keywords = new HashSet<String>();
			// 1.1. quotes get precedence; even components are outside quotes and will be splitted
			//    by space, odd components will be kept as is
			final String[] components = criteria.getKeywords().split("\"");
			for(int i=0; i<components.length; i++) {
				if(i % 2 == 0) {
					for(final String s : components[i].split("\\s")) {
						keywords.add(s);
					}
				} else {
					keywords.add("\""+components[i]+"\"");
				}
			}

			// 1.2. for each of the default search fields and each keyword, add to the query string
			//      an appropriate search query
			for(final Iterator<String> it1 = defaultFields.keySet().iterator(); it1.hasNext(); ) {
				final String field = it1.next();

				final Integer weight = defaultFields.get(field);
				for(final Iterator<String> it2 = keywords.iterator(); it2.hasNext(); ) {
					final String keyword = it2.next();

					qString.append(field);
					qString.append(":");
					qString.append(keyword);

					if(weight != 1) {
						qString.append("^");
						qString.append(weight);
					}

					if(it2.hasNext()) {
						qString.append(" OR ");
					}
				}
				if(it1.hasNext()) {
					qString.append(" OR ");
				}
			}

			qString.append(")");
		}


		// tag search (title of tagging item)
		for(final String tag : criteria.getTags()) {
			qString.append("+tag:\"");
			qString.append(tag);
			qString.append("\" ");
		}

		// purpose search (purpose of tagging item)
		if(criteria.getPurposes() != null && !criteria.getPurposes().isEmpty()) {
		for(final String purpose : criteria.getPurposes()) {
			qString.append("+purpose:\"");
			qString.append(purpose);
			qString.append("\" ");
		}
		}

		// author search (login name)
		if(criteria.getPerson() != null && !criteria.getPerson().equals("")) {
			qString.append("+author_login:\"");
			qString.append(criteria.getPerson());
			qString.append("\" ");
		}

		// type search (kiwi identifiers)
		for(final String type_id : criteria.getTypes()) {
			qString.append("+type:\"");
			qString.append(type_id);
			qString.append("\" ");
		}

		// rdf literal properties
		for(final String prop_uri : criteria.getRdfLiteralProperties().keySet()) {
			log.debug("buildSolrQuery: rdf literal property url: #0", prop_uri);
			final Set<String> queries = criteria.getRdfLiteralProperties().get(prop_uri);
			qString.append("+l_");
			qString.append(MD5.md5sum(prop_uri));
			qString.append(":(");
			for(final Iterator<String> it = queries.iterator(); it.hasNext(); ) {
				qString.append(it.next());
				if(it.hasNext()) {
					qString.append(" ");
				}
			}
			qString.append(") ");
		}

		// rdf double properties
		for(final String prop_uri : criteria.getRdfDoubleProperties().keySet()) {
			final Set<String> queries = criteria.getRdfDoubleProperties().get(prop_uri);
			qString.append("+d_");
			qString.append(MD5.md5sum(prop_uri));
			qString.append(":(");
			for(final String q : queries) {
				qString.append(q);
				qString.append(" ");
			}
			qString.append(") ");
		}

		// rdf integer properties
		for(final String prop_uri : criteria.getRdfIntegerProperties().keySet()) {
			final Set<String> queries = criteria.getRdfIntegerProperties().get(prop_uri);
			qString.append("+i_");
			qString.append(MD5.md5sum(prop_uri));
			qString.append(":(");
			for(final String q : queries) {
				qString.append(q);
				qString.append(" ");
			}
			qString.append(") ");
		}
		
		log.debug(qString.toString());
		// rdf object properties
		for(final String prop_uri : criteria.getRdfObjectProperties().keySet()) {
			final Set<String> queries = criteria.getRdfObjectProperties().get(prop_uri);
			qString.append("+u_");
			qString.append(MD5.md5sum(prop_uri));
			qString.append(":(");
						
			for (Iterator iterator = queries.iterator(); iterator.hasNext();) {
				String q = (String) iterator.next();
				qString.append("\"uri::"+q+"\"");
				if(iterator.hasNext() == true){
					qString.append(" ");
				}
			}

			qString.append(") ");
		}
		
		log.debug(qString.toString());
		
		log.debug("SOLR query string: #0",qString.toString());
		
		log.debug(qString.toString());
		query.setQuery(qString.toString());

		// 2. date range search
		// TODO (need to transform dates into proper format)

		// 3. build the facets from the search criteria
		query.setFacet(true);
		query.addFacetField("tag","purpose","author_login","type");

		for(final String prop_uri : criteria.getRdfLiteralFacets()) {
			query.addFacetField("l_"+MD5.md5sum(prop_uri));
		}
		for(final String prop_uri : criteria.getRdfObjectFacets()) {
			query.addFacetField("u_"+MD5.md5sum(prop_uri));
		}
		query.setFacetLimit(10);
		query.setFacetMinCount(1);

		// 4. set offset and limit
		query.setStart(criteria.getOffset());
		if(criteria.getLimit() > 0) {
			query.setRows(criteria.getLimit());
		}

		// 5. set sort field
		if( criteria.getSortField() != null && criteria.getSortOrder() != null ) {
            query.setSortField(criteria.getSortField(),criteria.getSortOrder());
        }
		return query;
	}

	private String getUriByNsName(String ns, String name){
		final TripleStore tripleStore = (TripleStore) Component.getInstance("tripleStore");
		final KiWiUriResource prop = tripleStore.createUriResourceByNamespaceTitle(ns, name);
		return prop.getUri();
	}

	private final static Pattern pat_searchSpecial = Pattern.compile("(tag|purpose|person|type|(?:[a-zA-Z][a-zA-Z0-9]*:[a-zA-Z][a-zA-Z0-9]*)):\\s*(\"([^\"\\\\]|\\\\.)*\"|[^\\s]*)", Pattern.DOTALL);

	/**
	 * Parse the query string by identifying words that are prefixed with a certain namespace.
	 * Currently, the following namespaces are identified in a search string:
	 * <ul>
	 * <li>tag:XYZ restrict search to articles that are tagged with a content item that has title
	 *    XYZ</li>
	 * <li>person:"Hans Mustermann" restrict search to articles that are related to the person
	 *    called "Hans Mustermann", where related can mean "author of" or "person description" or
	 *    "name appears in full text" or "there is a triple associating the article with this
	 *    person" (at the moment, only "author of" is supported).</li>
	 * </ul>
	 */
	@Override
	public KiWiSearchCriteria parseSearchString(String searchString) {
		log.debug(searchString);
		final TripleStore tripleStore = (TripleStore) Component.getInstance("tripleStore");


		final KiWiSearchCriteria criteria = new KiWiSearchCriteria();

		String searchKeywords      = null;
		final List<String> searchTags    = new LinkedList<String>();
		final List<String> searchPurposes    = new LinkedList<String>();
		final List<String> searchPersons = new LinkedList<String>();
		final List<String> searchTypes   = new LinkedList<String>();
		final Map<String,Set<String>> searchProps = new HashMap<String,Set<String>>();
		final Map<String,Set<String>> intProps = new HashMap<String,Set<String>>();
		final Map<String,Set<String>> doubleProps = new HashMap<String,Set<String>>();
		final Map<String,Set<String>> objProps = new HashMap<String,Set<String>>();

		// search all occurrences of tags and persons and add them to the appropriate sets
		final Matcher m = pat_searchSpecial.matcher(searchString);
		final StringBuffer buf = new StringBuffer();
		while(m.find()) {
			final String kind = m.group(1);
			String query = m.group(2);
			if(query.startsWith("\"") && query.endsWith("\"")) {
				query = query.substring(1, query.length()-1);
			}

			if(query.contains("&")) {
				query = query.replace("&", "\\&");
			}

			if("tag".equals(kind)) {
				searchTags.add(query);
			} else if("purpose".equals(kind)) {
				searchPurposes.add(query);
			} else if("person".equals(kind)) {
				searchPersons.add(query);
			} else if("type".equals(kind)) {
				// normalize query: if it is a kiwiid, keep, if it is a title, resolve
				if(!(query.startsWith("uri::") || query.startsWith("bnode::")) && query.contains(":")) {
					final String[] components = query.split(":");
					final KiWiUriResource res = tripleStore.createUriResourceByNamespaceTitle(components[0], components[1]);
					query = res.getKiwiIdentifier();
				}
				searchTypes.add(query);
			} else if(kind.contains(":")) {
				log.debug("found rdf property search label #0", kind);
				final String[] components = kind.split(":");
				final KiWiUriResource prop = tripleStore.createUriResourceByNamespaceTitle(components[0], components[1]);
				log.debug(prop.getUri());
				if(query.startsWith("uri::") || query.startsWith("bnode::")) {
					if(objProps.get(prop.getUri()) == null) {
						objProps.put(prop.getUri(), new HashSet<String>());
					}
					objProps.get(prop.getUri()).add(query);
				} else {
					if(searchProps.get(prop.getUri()) == null) {
						searchProps.put(prop.getUri(), new HashSet<String>());
					}
					searchProps.get(prop.getUri()).add(query);
				}
			}
			m.appendReplacement(buf, "");
		}
		m.appendTail(buf);
		searchKeywords = buf.toString().trim();


		if(searchKeywords != null) {
			criteria.setKeywords(searchKeywords);
		}
		if(searchTags != null) {
			criteria.getTags().addAll(searchTags);
		}
		if(searchPurposes != null) {
			criteria.getPurposes().addAll(searchPurposes);
		}
		if(searchTypes != null) {
			criteria.getTypes().addAll(searchTypes);
		}
		if(searchPersons != null && searchPersons.size() > 0) {
			// TODO: need support for querying all persons!
			criteria.setPerson(searchPersons.get(0));
		}

		if(searchProps != null && searchProps.size() > 0) {
			criteria.setRdfLiteralProperties(searchProps);
		}

		if(objProps != null && objProps.size() > 0) {
			criteria.setRdfObjectProperties(objProps);
		}

		criteria.setRdfLiteralFacets(new HashSet<String>(configurationService.getConfiguration("kiwi.solr.facets").getListValue()));

		return criteria;
	}

	/**
	 * Rebuild the SOLR index using the information in the database. This method tries to rebuild the
	 * index incrementally in batches of 100 items.
	 */
	@Override
	@Asynchronous
//	@Transactional
	public void rebuildIndex() {

		final StatusService statusService = (StatusService) Component.getInstance("kiwi.core.statusService");


		final SystemStatus status = new SystemStatus("search indexer");
		statusService.addSystemStatus(status);
		status.setMessage("rebuilding search index");

		try {
			try {
				Transaction.instance().setTransactionTimeout(60000);
			} catch (final SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			final javax.persistence.Query qc = entityManager.createNamedQuery("contentItemService.count");
			final Long max = (Long)qc.getSingleResult();

			final javax.persistence.Query q = entityManager.createNamedQuery("contentItemService.listSorted");

			int offset = 0;
			final int step   = 100;
			int count  = 0;
			do {
				status.setProgress(100*offset/max.intValue());

				q.setFirstResult(offset);
				q.setMaxResults(step);

				final List<ContentItem> results = q.getResultList();
				persistAll(results);

				count = results.size();

				offset += count;
			} while(count > 0);

			log.info("finished rebuilding search index from #0 content items", offset);

			optimize();
		} finally {
			statusService.removeSystemStatus(status);
		}
	}

	/**
	 * Refresh CEQ values for all contentItems contained in the search index
	 */
	@Override
	@Asynchronous
//	@Transactional
	public void updateCEQValues() {

		final StatusService statusService = (StatusService) Component.getInstance("kiwi.core.statusService");


		final SystemStatus status = new SystemStatus("search indexer");
		statusService.addSystemStatus(status);
		status.setMessage("rebuilding ceq values");

		try {
			//get a collection of solr documents
			final SolrServer server = getSolrServer();
			//docs with refreshed ceqs
			final Collection<SolrInputDocument> docs = new LinkedList<SolrInputDocument>();

			final int rows = 100;
			int start = 0;

			int resultSize = 0;

			do {
				//query
				final SolrQuery q = new SolrQuery("*:*");
				q.setRows(rows);
				q.setStart(start);
				final QueryResponse rsp = server.query(q);
				resultSize = rsp.getResults().size();

				for(final SolrDocument doc : rsp.getResults()) {

					try {
						//get id value
						final Long id = Long.parseLong(doc.getFieldValue("id").toString());

						//delete old ceq value
						doc.removeFields("ceq");
						//set ceq value
                        final double ceq = getEquity(id);
//                        final double ceq = 0;
						doc.addField("ceq", ceq);
					} catch(final Exception e) {
						e.printStackTrace();
					} finally {
						docs.add( ClientUtils.toSolrInputDocument(doc) );
					}
				}
				start += rows;

			} while(resultSize == rows);

			log.debug("#0 elements for update", docs.size());
			//update documents
			server.add(docs);
			server.commit();
		} catch(final Exception e) {
			e.printStackTrace();
		} finally {
			optimize();
			statusService.removeSystemStatus(status);
			log.debug("refreshed ceq values");
		}
	}

    /**
     * Returns the equity values for a content item with a given
     * id. This method return 0 if is call during the setup
     * process.
     * 
     * @param id the id for the content item.
     * @return the equity values for a content item with a given
     *         id.
     */
    private double getEquity(long id) {
        final boolean setupInProgress =
                ConfigurationServiceImpl.setupInProgress;
        return setupInProgress ? 0 : equityService.getContentItemEquity(id);
    }
}
