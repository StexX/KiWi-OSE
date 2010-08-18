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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import kiwi.api.config.ConfigurationService;
import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.event.KiWiEvents;
import kiwi.api.geo.GeoCodingFailedException;
import kiwi.api.geo.GeoCodingService;
import kiwi.api.geo.Location;
import kiwi.api.importexport.ImportService;
import kiwi.api.importexport.importer.ImporterLocal;
import kiwi.api.importexport.importer.ImporterRemote;
import kiwi.api.reasoning.ReasoningService;
import kiwi.api.tagging.TaggingService;
import kiwi.api.transaction.TransactionService;
import kiwi.api.triplestore.TripleStore;
import kiwi.api.user.CityFacade;
import kiwi.exception.NamespaceResolvingException;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.importexport.ImportLogEntry;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.ontology.SKOSConcept;
import kiwi.model.user.User;
import kiwi.service.importexport.importer.sn.SNImportException;
import kiwi.service.importexport.importer.sn.SNNewsItemFacade;
import kiwi.service.transaction.KiWiSynchronizationImpl;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Nodes;
import nu.xom.ParsingException;
import nu.xom.Text;
import nu.xom.ValidityException;
import nu.xom.xslt.XSLException;
import nu.xom.xslt.XSLTransform;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.log.Log;
import org.jboss.seam.transaction.Transaction;


/**
 * An importer for Salzburger Nachrichten XML format
 * 
 * @author Sebastian Schaffert
 *
 */
@Stateless
@Name("kiwi.service.importer.snxml")
@Scope(ScopeType.STATELESS)
public class SNImporterImpl implements ImporterLocal, ImporterRemote {

	@Logger
	private Log log;

	@In(create = true)
	private ConfigurationService configurationService;
	
	@In(create = true)
	private TripleStore tripleStore;
	
	@In(create = true)
	private KiWiEntityManager kiwiEntityManager;
	
	@In
	private EntityManager entityManager;
	
	@In
	private TransactionService transactionService;
	
	@In(create = true)
	private ContentItemService contentItemService;
	
	@In(create = true)
	private TaggingService taggingService;
	
	@In(value = "kiwi.core.geoCodingService", create = true)
	private GeoCodingService geoCodingService;
	
	private Location salzburg = new Location(47.417, 13.25);
	
	private static String[] mime_types = {
		"application/snxml+xml"
	};
	
	private DateFormat datetimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	private DateFormat datumFormat    = new SimpleDateFormat("dd.MM.yyyy");
	

	
	
	@Observer(KiWiEvents.SEAM_POSTINIT) 
	@BypassInterceptors
	public void initialise() {
		log.info("registering SN News importer ...");
		
		ImportService ies = (ImportService) Component.getInstance("kiwi.core.importService");
		
		ies.registerImporter(this.getName(),"kiwi.service.importer.snxml",this);
	}

	/**
	 * Get a collection of all mime types accepted by this importer. Used for automatically 
	 * selecting the appropriate importer in ImportService.
	 * 
	 * @return a set of strings representing the mime types accepted by this importer
	 */
	@Override
	@BypassInterceptors
	public Set<String> getAcceptTypes() {
		return new HashSet<String>(Arrays.asList(mime_types));
	}

	/**
	 * Get a description of this importer for presentation to the user.
	 * 
	 * @return a string describing this importer for the user
	 */
	@Override
	@BypassInterceptors
	public String getDescription() {
		return "Importer for parsing the Salzburger Nachrichten XML format";
	}

	/**
	 * Get the name of this importer. Used for presentation to the user and for internal 
	 * identification.
	 * 
	 * @return a string uniquely identifying this importer
	 */
	@Override
	@BypassInterceptors
	public String getName() {
		return "SNXML";
	}

	/**
	 * Import data from the URL provided as argument into the KiWi database. The URL must represent
	 * either a single article in SNXML format or a directory containing a set of such articles.
	 * 
	 * @param url the url from which to read the data
	 * @param types the set of types to associate with each generated content item
	 * @param tags the set of content items to use as tags
	 * @param user the user to use as author of all imported data
	 */
	@Override
	public int importData(URL url, String format, Set<KiWiUriResource> types, Set<ContentItem> tags, User user, Collection<ContentItem> outputCIs) {
		try {
			URLConnection con = url.openConnection();
			
			// check whether content is a file or a directory; files have content type application/xml, directories
			// have text/plain, all others we don't import, instead issue a warning to the log
			if(url.getProtocol().equals("file")) {
				File dir = new File(url.getPath());

				if(!Transaction.instance().isActive()) {
					Transaction.instance().begin();
					transactionService.registerSynchronization(
	                		KiWiSynchronizationImpl.getInstance(), 
	                		transactionService.getUserTransaction() );
				}
				
				
				if(dir.exists() && dir.isDirectory()) {
					log.info("importing SNXML files from directory #0",url.getPath());
					
					
					ReasoningService reasoner = (ReasoningService) Component.getInstance("kiwi.core.reasoningService");
					//reasoner.setOnlineReasoningAllowed(false);
					
					
					int count = 0, failed = 0, skipped = 0;
					for(File f : dir.listFiles()) {
						if(f.getName().endsWith(".xml")) {
							
							Query q = entityManager.createNamedQuery("importLog.exists");
							q.setParameter("fileName", f.getName());
							
							
							
							if( q.getResultList().size() == 0) {
							
								boolean success = false;
								String reason = null;
								try {
									success = importFile(getParser().build(f), f.getName(), types, tags, user, outputCIs);
								} catch (SNImportException e) {
									reason = e.getMessage();
								} catch (ValidityException e) {
									reason = "validation error ("+e.getMessage()+")";
									log.error("error while validating SNXML file #0: #1", f.getName(), e.getMessage());
								} catch (ParsingException e) {
									reason = "parsing error ("+e.getMessage()+")";
									log.error("error while parsing SNXML file #0: #1", f.getName(), e.getMessage());
								} catch(FileNotFoundException ex) {
									reason = "file not found";
									log.error("file not found: #0",f.getName());
								}
								
								if(success) {
									count++;
								} else {
									failed++;
								}
								
								
								// mark item as already imported
								entityManager.persist(new ImportLogEntry(f.getName(),SNImporterImpl.class.getCanonicalName(),success, reason));
								
								if(count % 50 == 0) {
									// every 500 imported items, commit the transaction, start a new transaction, 
									Transaction.instance().commit();
									entityManager.clear();
									Transaction.instance().begin();
									transactionService.registerSynchronization(
					                		KiWiSynchronizationImpl.getInstance(), 
					                		transactionService.getUserTransaction() );
								}
							} else {
								skipped++;
								log.debug("skipping already imported #0", f.getName());
							}
						}
					}
//					if(Transaction.instance().isActive()) {
//						Transaction.instance().commit();
//					}
					reasoner.enableReasoning();
				
					if(skipped > 0) {
						log.info("skipped #0 files, as they have already been imported previously",skipped);
					}
					
					return count;
				} else {
					log.info("importing possible SNXML file #0",url.getPath());
					
					// a possible SN News article; import from input stream
					try {
						if(importFile(getParser().build(dir), dir.getName(), types, tags, user, outputCIs)) 
							return 1;
						else
							return 0;
					} catch (ValidityException e) {
						log.error("error while validating SNXML file #0: #1", dir.getName(), e.getMessage());
					} catch (ParsingException e) {
						log.error("error while parsing SNXML file #0: #1", dir.getName(), e.getMessage());
					} catch (SNImportException e) {
						log.warn("SNXML file #0 skipped: #1", dir.getName(),e.getMessage());
					}
					return 0;
				}
				
				
			} else if(con.getContentType() != null && con.getContentType().equals("application/xml")) {
				// a possible SN News article; import from input stream
				return importData(con.getInputStream(),format,types,tags,user,outputCIs);				
			} else {
				log.error("unsupported content type and prototcol for URL #0: #1", url.toString(), con.getContentType());
			}
			
		} catch (IOException e) {
			log.error("error while opening connection to #0 for importing SN News Articles: #1",url.toString(),e.getMessage());
		} catch (NotSupportedException e) {
			log.error("error while starting new transaction (not supported)",e);
		} catch (SystemException e) {
			log.error("error while starting new transaction (system exception)",e);
		} catch (SecurityException e) {
			log.error("error while committing transaction (security exception)",e);
		} catch (IllegalStateException e) {
			log.error("error while committing transaction (illegal state)",e);
		} catch (RollbackException e) {
			log.error("error while committing transaction (rollback)",e);
		} catch (HeuristicMixedException e) {
			log.error("error while committing transaction (heuristic mixed)",e);
		} catch (HeuristicRollbackException e) {
			log.error("error while committing transaction (heuristic rollback)",e);
		}
		
		
		return 0;
	}

	/**
	 * Import data from the input stream provided as argument into the KiWi database. 
	 * 
	 * @param is the input stream from which to read the data
	 * @param filename the name of the imported file, if available; note that this field is used differently from the
	 *        interface, where it specifies the format!
	 * @param types the set of types to associate with each generated content item
	 * @param tags the set of content items to use as tags
	 * @param user the user to use as author of all imported data
	 */
	@Override
	public int importData(InputStream is, String filename, Set<KiWiUriResource> types, Set<ContentItem> tags, User user, Collection<ContentItem> outputCIs) {
		try {
			Document doc = getParser().build(is);
			
			if(importFile(doc, filename, types, tags, user, outputCIs)) {
				return 1;
			} else {
				return 0;
			}
		} catch (ValidityException e) {
			log.error("error parsing XML file #0 from input stream",filename,e);
		} catch (ParsingException e) {
			log.error("error parsing XML file #0 from input stream",filename,e);
		} catch (IOException e) {
			log.error("error parsing XML file #0 from input stream",filename,e);
		} catch (SNImportException e) {
			log.warn("SNXML file #0 skipped: #1", filename,e.getMessage());
		}
		
		return 0;
	}

	/**
	 * Import data from the input stream provided as argument into the KiWi database. 
	 * 
	 * @param reader the reader from which to read the data
	 * @param filename the name of the imported file, if available; note that this field is used differently from the
	 *        interface, where it specifies the format!
	 * @param types the set of types to associate with each generated content item
	 * @param tags the set of content items to use as tags
	 * @param user the user to use as author of all imported data
	 */
	@Override
	public int importData(Reader reader, String filename, Set<KiWiUriResource> types, Set<ContentItem> tags, User user, Collection<ContentItem> outputCIs) {
		try {
			Document doc = getParser().build(reader);
			
			if(importFile(doc, filename, types, tags, user, outputCIs)) {
				return 1;
			} else {
				return 0;
			}
		} catch (ValidityException e) {
			log.error("error parsing XML file #0 from reader",filename,e);
		} catch (ParsingException e) {
			log.error("error parsing XML file #0 from reader",filename,e);
		} catch (IOException e) {
			log.error("error parsing XML file #0 from reader",filename,e);
		} catch (SNImportException e) {
			log.warn("SNXML file #0 skipped: #1", filename,e.getMessage());
		}
		
		return 0;
	}

//	@Transactional
	private boolean importFile(Document doc, String filename, Set<KiWiUriResource> types, Set<ContentItem> tags, User user, Collection<ContentItem> outputCIs) throws SNImportException {

		String uri = configurationService.getBaseUri() + "/sn/"+filename;
		
		// check whether file has already been loaded
		if(contentItemService.getContentItemByUri(uri) != null) {
			log.info("skipping file #0",filename);
			return false;
		}
		log.info("processing file #0",filename);

		
		try {
			String title = null, ressort = null, geo = null, ecid = null;
			Set<String> keywords = new HashSet<String>();
			Date date = null;
			Document content = null;
			Nodes nl;

			// look for ecid
			nl = doc.query("/SNXML/ECID/text()");
			if(nl.size() > 0) {
				ecid = ((Text)nl.get(0)).getValue();
			} else {
				log.debug("file #0 does not contain a ecid identifier",filename);				
			}


			// look for title
			nl = doc.query("/SNXML/ARTIKEL/INHALT/TITEL/text()");
			if(nl.size() > 0) {
				title = ((Text)nl.get(0)).getValue();
				if(title.length() > 200) {
					title = title.substring(0,200);
				}
			} else {
				log.error("file #0 skipped as it does not contain a title",filename);
				throw new SNImportException("no title");
			}

			// look for geo tag
			nl = doc.query("/SNXML/ARTIKEL/GEO/text()");
			if(nl.size() > 0) {
				geo = ((Text)nl.get(0)).getValue();
				if(geo.contains("/") ) {
					String[] s = geo.split("/");
					geo = s[0].toLowerCase().replace('.', ' ').trim();				
				} else {
					geo = geo.toLowerCase().replace('.', ' ').trim();
				}

				// normalize strange patterns where the location name actually occurs twice...
				Pattern p = Pattern.compile("(\\S+)\\s+\\1");
				Matcher m = p.matcher(geo);
				if(m.matches()) {
					geo = m.group(1);
				}
			}

			log.debug("geo-location: #0",geo);
			/*
			if(geo == null) {
				log.info("skipping, no geo tag available");
				return false;
			} else {
				log.debug("geo-location: #0",geo);
			}
			*/

			// look for ressort tag
			nl = doc.query("/SNXML/ARTIKEL/RESSORT/text()");
			if(nl.size() > 0) {
				ressort = ((Text)nl.get(0)).getValue().toLowerCase().trim();
				keywords.add(ressort);
			}

			// look for keyword tags
			nl = doc.query("/SNXML/ARTIKEL/KEYWORD/text()");
			for(int i = 0; i < nl.size(); i++) {
				keywords.add(((Text)nl.get(0)).getValue().toLowerCase().trim());
			}

			// look for datum and timedata tags
			nl = doc.query("/SNXML/DATUM/text()");
			if(nl.size() > 0) {
				try {
					date = datumFormat.parse(((Text)nl.get(0)).getValue());
				} catch (ParseException e) {
					log.warn("error while parsing date: #0",((Text)nl.get(0)).getValue());
				}
			}
			nl = doc.query("/SNXML/ARTIKEL/TIMEDATE/text()");
			if(nl.size() > 0) {
				try {
					date = datetimeFormat.parse(((Text)nl.get(0)).getValue());
				} catch (ParseException e) {
					log.warn("error while parsing date: #0",((Text)nl.get(0)).getValue());
				}
			}

			// now look for content and transform it to HTML
			try {
				// we use the interface as classloader reference, since the stateless bean has a different classloader
				Document stylesheet = getParser().build(SNNewsItemFacade.class.getResourceAsStream("snhtml.xsl"));
				XSLTransform transform = new XSLTransform(stylesheet);
				Nodes output = transform.transform(doc);
				content = XSLTransform.toDocument(output);
			} catch (XSLException e) {
				log.error("error while transforming XML content of file #0: #1",filename,e.getMessage());
			}           

			// if we have come so far, we can create a new content item for the imported data
			ContentItem item = contentItemService.createContentItem("sn/"+filename);
			contentItemService.updateTitle(item, title);
			item.setAuthor(user);
			item.setLanguage(Locale.GERMAN);
			item.addType(tripleStore.createUriResource(Constants.NS_FCP_CORE+"NewsItem"));
			if(date != null) {
				item.setCreated(date);
				item.setModified(date);
			}
			if(content != null) {
				contentItemService.updateTextContentItem(item, content.toXML());
			}

			kiwiEntityManager.persist(item);

			SNNewsItemFacade snitem = kiwiEntityManager.createFacade(item,SNNewsItemFacade.class);

			if(ecid != null) {
				snitem.setECID(ecid);
			}

			// for each of the keywords, do the same
			for(String keyword : keywords) {

				if(!"".equals(keyword)) {
					ContentItem taggingItem = contentItemService.getContentItemByTitle(keyword);
	
					if(taggingItem == null) {
						// create new Content Item of type "tag" if the tag does not yet exist
						taggingItem = contentItemService.createContentItem();
						taggingItem.addType(tripleStore.createUriResource(Constants.NS_FCP_CORE+"Keyword"));
						contentItemService.updateTitle(taggingItem, keyword);
						if(keyword.equals(ressort)) {
							taggingItem.addType(tripleStore.createUriResource(Constants.NS_FCP_CORE+"Ressort"));
							taggingItem.addType(tripleStore.createUriResource(Constants.NS_SKOS+"Concept"));
						}
						kiwiEntityManager.persist(taggingItem);
						log.debug("created new content item for non-existant keyword tag #0",keyword);
					}
	
					taggingService.createTagging(keyword, item, taggingItem, user);
				}

			}

			if(ressort != null) {
				ContentItem snressort   = contentItemService.getContentItemByUri(Constants.NS_FCP_CORE+"Ressort");
				if(snressort != null) {

					ContentItem taggingItem = contentItemService.getContentItemByTitle(ressort);

					if(taggingItem == null) {
						// create new Content Item of type "tag" if the tag does not yet exist
						taggingItem = contentItemService.createContentItem();
						taggingItem.addType(tripleStore.createUriResource(Constants.NS_FCP_CORE+"Keyword"));
						contentItemService.updateTitle(taggingItem, ressort);
						taggingItem.addType(tripleStore.createUriResource(Constants.NS_FCP_CORE+"Ressort"));
						taggingItem.addType(tripleStore.createUriResource(Constants.NS_SKOS+"Concept"));
						try {
							taggingItem.getResource().addOutgoingNode("<"+Constants.NS_SKOS+"broader>", snressort.getResource());
							taggingItem.getResource().addOutgoingNode("<"+Constants.NS_SKOS+"hasTopConcept>", snressort.getResource());
						} catch (NamespaceResolvingException e) {
							e.printStackTrace();
						}

						kiwiEntityManager.persist(taggingItem);
						log.debug("created new content item for non-existant keyword tag #0",ressort);
					}

					snitem.setCategory(kiwiEntityManager.createFacade(snressort, SKOSConcept.class));
					snitem.setSubCategory(kiwiEntityManager.createFacade(taggingItem, SKOSConcept.class));
				}
			}

			// try to geolocate the item if the geo tag is present
			if(geo != null) {
				try {
					Location loc = null;
					
					if(configurationService.getConfiguration("tagit.geocoder","Google").getStringValue().equals("Google")) {
						loc = geoCodingService.getLocationGoogle(geo, salzburg);
					} else if(configurationService.getConfiguration("tagit.geocoder","Google").getStringValue().equals("Geonames")) {
						loc = geoCodingService.getLocationGeonames(geo, salzburg);
					}
					
					if(loc != null) {
						item.addType(tripleStore.createUriResource(Constants.NS_GEO+"Point"));
						snitem.setLongitude(loc.getLongitude());
						snitem.setLatitude(loc.getLatitude());
					}
	
					// tag the content item with the city in the geo field and make it a city
					ContentItem taggingItem = contentItemService.getContentItemByTitle(geo);
	
					if(taggingItem == null) {
						// create new Content Item of type "tag" if the tag does not yet exist
						taggingItem = contentItemService.createContentItem();
						taggingItem.addType(tripleStore.createUriResource(Constants.NS_FCP_CORE+"City"));
						contentItemService.updateTitle(taggingItem, geo);
						kiwiEntityManager.persist(taggingItem);
					}
					if(loc != null) {
						taggingItem.addType(tripleStore.createUriResource(Constants.NS_KIWI_CORE+"City"));
						CityFacade city = kiwiEntityManager.createFacade(taggingItem, CityFacade.class);
						city.setLatitude(loc.getLatitude());
						city.setLongitude(loc.getLongitude());
					}
	
					taggingService.createTagging(geo, item, taggingItem, user);

				} catch(GeoCodingFailedException ex) {
					log.error("geocoding failed for news item #0", filename);
				}
			}
		
			
			// add parameter categories as tags
			if(tags != null) {
				for(ContentItem tag : tags) {
					if(!"".equals(tag.getTitle())) {
						if(!taggingService.hasTag(item, tag.getTitle())) {
							taggingService.createTagging(tag.getTitle(), item, tag, user);
						}
					}
				}
			}
					
			// add parameter types as types
			if(types != null) {
				for(KiWiUriResource type : types) {
					item.addType(type);
				}
			}

			
			//kiwiEntityManager.flush();		
			
			return true;
		} catch(IOException ex) {
			log.error("I/O error reading file #0: #1",filename,ex.getMessage());			
		} catch (ValidityException e) {
			log.error("validation error while parsing file #0: #1",filename,e.getMessage());
		} catch (ParsingException e) {
			log.error("parse error while parsing file #0: #1",filename,e.getMessage());
		}
		return false;
	}

	private static Builder parser = new Builder(false);
	public static Builder getParser() {
		//return parser;
		return new Builder(false);
	}

}
