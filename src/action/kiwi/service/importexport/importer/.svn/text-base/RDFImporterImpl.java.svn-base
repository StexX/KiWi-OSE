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
package kiwi.service.importexport.importer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import kiwi.api.config.ConfigurationService;
import kiwi.api.content.ContentItemService;
import kiwi.api.event.KiWiEvents;
import kiwi.api.importexport.ImportService;
import kiwi.api.importexport.importer.RDFImporterLocal;
import kiwi.api.importexport.importer.RDFImporterRemote;
import kiwi.api.tagging.TaggingService;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.User;
import kiwi.service.triplestore.TripleStoreUtil;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.RaiseEvent;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.log.Log;
import org.openrdf.model.Literal;
import org.openrdf.model.Namespace;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParseException;
import org.openrdf.sail.memory.MemoryStore;
import org.openrdf.sail.nativerdf.NativeStore;

/**
 * An importer for importing RDF sources in RDF/XML or other RDF formats. Currently uses
 * the Sesame parser for parsing the RDF content. 
 * <p>
 * For each triple found in the imported data, an appropriate KiWi triple is added. For each 
 * resource in the imported data, the importer creates a ContentItem using rdfs:label or 
 * dc:title as title and rdfs:comment or dc:description as content.
 * 
 * @author Sebastian Schaffert
 *
 */
@Stateless
@Name("kiwi.service.importer.rdf")
@Scope(ScopeType.STATELESS)
public class RDFImporterImpl implements RDFImporterLocal, RDFImporterRemote {

	
	@Logger
	private Log log;

	@In(create = true)
	private TripleStore tripleStore;
	
	@In
	private EntityManager entityManager;

	@In(create = true)
	private ConfigurationService configurationService;

	@In(create = true)
	private ContentItemService contentItemService;
	
	@In(create = true)
	private TaggingService taggingService;
	
	private static String[] mime_types = {
		"application/rdf+xml",
		"text/n3",
		"application/turtle",
		"application/x-turtle"
	};

	/**
	 * Get a collection of all mime types accepted by this importer. Used for automatically 
	 * selecting the appropriate importer in ImportService.
	 * 
	 * @return a set of strings representing the mime types accepted by this importer
	 */
	@Override
	public Set<String> getAcceptTypes() {
		return new HashSet<String>(Arrays.asList(mime_types));
	}

	/**
	 * Get a description of this importer for presentation to the user.
	 * 
	 * @return a string describing this importer for the user
	 */
	@Override
	public String getDescription() {
		return "Importer for various RDF formats (RDF/XML, N3, TURTLE); also supports OWL and RDFS files";
	}

	/**
	 * Get the name of this importer. Used for presentation to the user and for internal 
	 * identification.
	 * 
	 * @return a string uniquely identifying this importer
	 */
	@Override
	public String getName() {
		return "RDF";
	}

	/**
	 * Import data from the input stream provided as argument into the KiWi database. 
	 * 
	 * @param url the url from which to read the data
	 * @param types the set of types to associate with each generated content item
	 * @param tags the set of content items to use as tags
	 * @param user the user to use as author of all imported data
	 */
	@Override
	@RaiseEvent("ontologyChanged")
	public int importData(URL url, String format, Set<KiWiUriResource> types, Set<ContentItem> tags, User user, Collection<ContentItem> output) {
		try {
			return importData(url.openStream(), format, types, tags, user, output);
		} catch (IOException ex) {
			log.error("I/O error while importing data from URL #0: #1",url, ex.getMessage());
			return 0;
		}
	}

	/**
	 * Import data from the input stream provided as argument into the KiWi database. 
	 * <p>
     * Import function for formats supported by Sesame; imports the data first into a separate memory
     * repository, and then iterates over all statements, adding them to the current knowledge space.
     * This method also checks for resources that have a rdfs:label, dc:title, or skos:prefLabel and uses
     * it as the title for newly created ContentItems. 
     *
	 * @param is the input stream from which to read the data
	 * @param types the set of types to associate with each generated content item
	 * @param tags the set of content items to use as tags
	 * @param user the user to use as author of all imported data
	 */
	@Override
	@RaiseEvent("ontologyChanged")
	public int importData(InputStream is, String format, Set<KiWiUriResource> types, Set<ContentItem> tags, User user, Collection<ContentItem> output) {
		// TODO: need to figure out format automatically!
		RDFFormat f = getFormat(format);

		String baseUri = configurationService.getBaseUri();

		int count = 0;

		try {
			// File sesameDataDir = new
			// File(configurationService.getWorkDir()+File.separator+Long.toHexString(System.currentTimeMillis()));
			Repository myRepository = new SailRepository(new MemoryStore());

			myRepository.initialize();
			RepositoryConnection myCon = myRepository.getConnection();

			if (is != null) {

				long timer = System.currentTimeMillis();

				myCon.add(is, baseUri, f, (Resource) null); // no context (null)
															// for the moment,
															// clear it all

				log.info("imported data into temporary repository (#0 ms)",
						System.currentTimeMillis() - timer);

				count = importDataSesameInternal(myCon, types, tags, user, output);

			} else {
				log.error("could not load ontology; InputStream was null");
			}
			// myCon.clear();
			// myCon.clearNamespaces();
			myCon.close();
			myRepository.shutDown();
			// sesameDataDir.delete();
		} catch (RepositoryException ex) {
			log.error("error while importing Sesame data:", ex);
		} catch (RDFParseException ex) {
			log.error("parse error while importing Sesame data:", ex);
		} catch (IOException ex) {
			log.error("I/O error while importing Sesame data:", ex);
		}
		return count;

	}

	/**
	 * Import data from the reader provided as argument into the KiWi database.
	 * <p>
     * Import function for formats supported by Sesame; imports the data first into a separate memory
     * repository, and then iterates over all statements, adding them to the current knowledge space.
     * This method also checks for resources that have a rdfs:label, dc:title, or skos:prefLabel and uses
     * it as the title for newly created ContentItems. 
	 * 
	 * @param reader the reader from which to read the data
	 * @param types the set of types to associate with each generated content item
	 * @param tags the set of content items to use as tags
	 * @param user the user to use as author of all imported data
	 */
	@Override
	@RaiseEvent("ontologyChanged")
	public int importData(Reader reader, String format, Set<KiWiUriResource> types, Set<ContentItem> tags, User user, Collection<ContentItem> output) {
		ConfigurationService configurationService = (ConfigurationService) Component.getInstance("configurationService");

		// TODO: need to figure out format automatically!
		RDFFormat f = getFormat(format);

		String baseUri = configurationService.getBaseUri();

		int count = 0;
		
		try {
			File sesameDataDir = new File(configurationService.getWorkDir()+File.separator+Long.toHexString(System.currentTimeMillis()));
			Repository myRepository = new SailRepository(new NativeStore(sesameDataDir));

			myRepository.initialize();
			RepositoryConnection myCon = myRepository.getConnection();


			if(reader != null) {

				long timer = System.currentTimeMillis();

				myCon.add(reader, baseUri, f, (Resource)null);

				log.info("imported data into temporary repository (#0 ms)", System.currentTimeMillis() - timer);

				count = importDataSesameInternal(myCon,types,tags,user, output);

			} else {
				log.error("could not load ontology; InputStream was null");
			}
			myCon.clear();
			myCon.clearNamespaces();
			myCon.close();
			myRepository.shutDown();
			sesameDataDir.delete();
		} catch(RepositoryException ex) {
			log.error("error while importing Sesame data:",ex);
		} catch(RDFParseException ex) {
			log.error("parse error while importing Sesame data:",ex);
		} catch(IOException ex) {
			log.error("I/O error while importing Sesame data:",ex);
		} catch(Exception ex) {
			log.error("unknown exception with message #0; skipping ontology",ex.getMessage());
		}
		return count;
	}

	
	
	
	@Observer(KiWiEvents.SEAM_POSTINIT) 
	@BypassInterceptors
	public void initialise() {
		log.info("registering RDF importer ...");
		
		ImportService ies = (ImportService) Component.getInstance("kiwi.core.importService");
		
		ies.registerImporter(this.getName(),"kiwi.service.importer.rdf",this);
	}	
	
    /**
     * internal generic method for importing from a temporary sesame connection
     * 16.09.2009: Changed from private to public scope (steffi)
     * @param myCon
     * @return
     * @throws RepositoryException
     */
   public int importDataSesame(RepositoryConnection myCon, Set<KiWiUriResource> types, Set<ContentItem> tags, User user, Collection<ContentItem> output) {
		try {
			return importDataSesameInternal(myCon,types,tags,user,output);
		} catch(RepositoryException ex) {
			log.error("error while importing Sesame data (#0); skipping ontology",ex.getMessage());
			return 0;
		} catch(Exception ex) {
			log.error("unknown exception with message #0; skipping ontology",ex.getMessage());
			return 0;
		}
   }
	   
	/**
     * internal generic method for importing from a temporary sesame connection
     * 16.09.2009: Changed from private to public scope (steffi)
     * 07.12.2009: Changed back to private, because it otherwise causes exception intercepting by seam, which is
     *             not desirable
     * @param myCon
     * @return
     * @throws RepositoryException
     */
   private int importDataSesameInternal(RepositoryConnection myCon, Set<KiWiUriResource> types, Set<ContentItem> tags, User user, Collection<ContentItem> output) throws RepositoryException {
    	int itemCount = 0;
    	
    	if(tags == null) {
    		tags = new HashSet<ContentItem>();
    	}
    	if(types == null) {
    		types = new HashSet<KiWiUriResource>();
    	}
    	
        Locale defaultLocale                  = (Locale) Component.getInstance("org.jboss.seam.core.locale");
        long timer = System.currentTimeMillis();
        
		// collect subjects in a HashSet
		HashSet<Resource> subjects = new HashSet<Resource>();

		HashSet<KiWiTriple> triples = new HashSet<KiWiTriple>();
		
		// ESCA-JAVA0123:
		for(RepositoryResult<Statement> r = myCon.getStatements(null, null, null, false) ; r.hasNext(); ) {
			Statement stmt = r.next();
            // TODO: do not store every triple explicitly (performance issues)
			//tripleStore.storeTriple(SesameUtil.transformSesameToKiWi(tripleStore,stmt),false);
			triples.add(TripleStoreUtil.uncheckedSesameToKiWi(stmt));
            subjects.add(stmt.getSubject());
        }
		tripleStore.storeTriplesUnchecked(triples);
        
        // ESCA-JAVA0123:
		for(RepositoryResult<Namespace> ns = myCon.getNamespaces(); ns.hasNext(); ) {
            Namespace ns1 = ns.next();
            
            tripleStore.setNamespace(ns1.getPrefix(),ns1.getName());
        }
		
		log.info("transferred data into KiWi repository (#0 ms)", System.currentTimeMillis() - timer);

        timer = System.currentTimeMillis();

		// for each subject, look for dc:title or rdfs:label and dc:description or rdfs:comment and create a content item
		for(Resource subject : subjects) {
			log.debug("Looking for dc:title, rdfs:label, dc:description or rdfs:comment for the resource #0 ", subject);
			String title = null, description = null, language = null;
			HashSet<String> altLabels = new HashSet<String>();
			
			for(RepositoryResult<Statement> stmts = myCon.getStatements(subject, null, null, false) ; stmts.hasNext(); ) {
				Statement stmt = stmts.next();

				URI type_xml = myCon.getValueFactory().createURI("http://www.w3.org/1999/02/22-rdf-syntax-ns#XMLLiteral");
				
				if(stmt.getPredicate().getNamespace().equals(Constants.NS_RDFS) && 
						stmt.getPredicate().getLocalName().equals("comment")    &&
						description == null) {
					// if predicate is rdfs:comment and description not already set, use literal value as description

					// TODO: how to deal with different languages?
					Literal l = (Literal)stmt.getObject();
					if(type_xml.equals(l.getDatatype())) {
						description = "<div xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:kiwi=\""+Constants.NS_KIWI_HTML+"\" kiwi:type=\"page\">" + l.stringValue()+"</p></div>";
					} else {
						description = "<div xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:kiwi=\""+Constants.NS_KIWI_HTML+"\" kiwi:type=\"page\"><p>"+l.stringValue().replace("&","&amp;").replace("<", "&lt;").replace(">","&gt;")+"</p></div>";
					}
					language = l.getLanguage();
				} else if(stmt.getPredicate().getNamespace().equals(Constants.NS_DC) && 
						  stmt.getPredicate().getLocalName().equals("description")) {
					// if predicate is dc:description, use it in *any* case, even if comment was already there
					Literal l = (Literal)stmt.getObject();
					if(type_xml.equals(l.getDatatype())) {
						description = "<div xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:kiwi=\""+Constants.NS_KIWI_HTML+"\" kiwi:type=\"page\">" + l.stringValue()+"</p></div>";
					} else {
						description = "<div xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:kiwi=\""+Constants.NS_KIWI_HTML+"\" kiwi:type=\"page\"><p>"+l.stringValue().replace("&","&amp;").replace("<", "&lt;").replace(">","&gt;")+"</p></div>";
					}
					language = l.getLanguage();
				} else if(stmt.getPredicate().getNamespace().equals(Constants.NS_SKOS) && 
						  stmt.getPredicate().getLocalName().equals("definition")) {
					// if predicate is skos:definition, use it in *any* case, even if comment was already there
					Literal l = (Literal)stmt.getObject();
					if(type_xml.equals(l.getDatatype())) {
						description = "<div xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:kiwi=\""+Constants.NS_KIWI_HTML+"\" kiwi:type=\"page\">" + l.stringValue()+"</p></div>";
					} else {
						description = "<div xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:kiwi=\""+Constants.NS_KIWI_HTML+"\" kiwi:type=\"page\"><p>"+l.stringValue().replace("&","&amp;").replace("<", "&lt;").replace(">","&gt;")+"</p></div>";
					}
					language = l.getLanguage();
				} else if(stmt.getPredicate().getNamespace().equals(Constants.NS_RDFS) && 
						  stmt.getPredicate().getLocalName().equals("label")           &&
						  title == null) {
					// if predicate is rdfs:label, set title if it is not yet set
					Literal l = (Literal)stmt.getObject();
					title = l.stringValue();
				} else if(stmt.getPredicate().getNamespace().equals(Constants.NS_DC) && 
						  stmt.getPredicate().getLocalName().equals("title")) {
					// if predicate is dc:title, use it in *any* case, even if label was already there
					Literal l = (Literal)stmt.getObject();
					title = l.stringValue();
				} else if(stmt.getPredicate().getNamespace().equals(Constants.NS_SKOS) && 
						  stmt.getPredicate().getLocalName().equals("prefLabel")) {
					// if predicate is skos:prefLabel, use it in *any* case, even if label was already there
					Literal l = (Literal)stmt.getObject();
					title = l.stringValue();
				} else if(stmt.getPredicate().getNamespace().equals(Constants.NS_SKOS) &&
						  stmt.getPredicate().getLocalName().equals("altLabel")) {
					Literal l = (Literal)stmt.getObject();
					altLabels.add(l.stringValue());
				}
			
			}
			if(title != null) {
				KiWiResource r = (KiWiResource)TripleStoreUtil.transformSesameToKiWi(tripleStore,subject);
				ContentItem item = r.getContentItem();
				if(!entityManager.contains(item) && item.getId() != null) {
					item = entityManager.find(ContentItem.class,item.getId());
				}
				
				contentItemService.updateTitle(item, title);
				
				if(description != null) {
					contentItemService.updateTextContentItem(item, description);
					
					if(language != null) {
						item.setLanguage(new Locale(language));
					} else {
						item.setLanguage(defaultLocale);
					}
					
				}
				item.setTagLabels(altLabels);
				
				// if user has been given as parameter, use it
				if(user != null) {
					item.setAuthor(user);
				}				
				
				contentItemService.saveContentItem(item);
				
				log.debug("Creating tags (#0) for ContentItem '#1' ", tags, title);
				// add parameter categories as tags
				for(ContentItem tag : tags) {
					taggingService.createTagging(tag.getTitle(), item, tag, user);
				}
				
				log.debug("Creating types (#0) for ContentItem '#1' ", tags, title);
				// add parameter types as types
				for(KiWiUriResource type : types) {
					item.addType(type);
				}
				
				itemCount ++;		
			
				if (output != null) {
					output.add(item);
				}
			}
			
		}
		
		log.info("created #0 ContentItems for resources with descripive information (#1 ms)",itemCount,System.currentTimeMillis()-timer);
		return itemCount;
    }

    private static final RDFFormat getFormat(String format) {
    	if("application/rdf+xml".equals(format)) {
    		return RDFFormat.RDFXML;
    	} else if("text/n3".equals(format)) {
    		return RDFFormat.N3;
    	} else if("application/turtle".equals(format) || "application/x-turtle".equals(format)) {
    		return RDFFormat.TURTLE;
    	} else {
    		return RDFFormat.RDFXML;
    	}
    }
}
