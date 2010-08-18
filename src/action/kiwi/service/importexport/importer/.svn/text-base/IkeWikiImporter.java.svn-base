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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.xml.transform.TransformerException;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.event.KiWiEvents;
import kiwi.api.importexport.ImportService;
import kiwi.api.importexport.importer.ImporterLocal;
import kiwi.api.importexport.importer.ImporterRemote;
import kiwi.api.multimedia.MultimediaService;
import kiwi.api.tagging.TaggingService;
import kiwi.api.triplestore.TripleStore;
import kiwi.api.user.UserService;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiNamespace;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.User;
import kiwi.service.importexport.importer.ikewiki.RXRReader;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.Nodes;
import nu.xom.ParsingException;
import nu.xom.Text;
import nu.xom.ValidityException;
import nu.xom.XPathContext;
import nu.xom.xslt.XSLException;
import nu.xom.xslt.XSLTransform;

import org.bouncycastle.util.encoders.Base64;
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

/**
 * Importer that supports importing data from old IkeWiki installations using IkeWiki's WIF
 * format.
 * 
 * @author Sebastian Schaffert
 *
 */
@Stateless
@Name("kiwi.service.importer.ikewiki")
@Scope(ScopeType.STATELESS)
public class IkeWikiImporter implements ImporterLocal, ImporterRemote {

	@Logger
	private Log log;

	@In(create = true)
	private TripleStore tripleStore;
	
	@In(create = true)
	private EntityManager entityManager;

	@In(create = true)
	private KiWiEntityManager kiwiEntityManager;
	
	@In(create = true)
	private ContentItemService contentItemService;
	
	@In(create = true)
	private MultimediaService multimediaService;
	
	@In(create = true)
	private UserService userService;
	
	@In(create = true)
	private TaggingService taggingService;
	
	private static String[] mime_types = {
		"application/ikewiki+xml"
	};
	
	
	@Observer(KiWiEvents.SEAM_POSTINIT) 
	@BypassInterceptors
	public void initialise() {
		log.info("registering IkeWiki importer ...");
		
		ImportService ies = (ImportService) Component.getInstance("kiwi.core.importService");
		
		ies.registerImporter(this.getName(),"kiwi.service.importer.ikewiki",this);
	}
	
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
		return "Importer for importing data from old IkeWiki installations";
	}

	/**
	 * Get the name of this importer. Used for presentation to the user and for internal 
	 * identification.
	 * 
	 * @return a string uniquely identifying this importer
	 */
	@Override
	public String getName() {
		return "IkeWiki";
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
		Builder parser = new Builder();
		try {
			return importData(parser.build(is), types, tags, user, output);
		} catch (ValidityException e) {
			log.error("the IkeWiki WIF document was not valid",e);
		} catch (ParsingException e) {
			log.error("the IkeWiki WIF document could not be parsed",e);
		} catch (IOException e) {
			log.error("I/O error while reading IkeWiki WIF document",e);
		}
		return 0;
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
		Builder parser = new Builder();
		try {
			return importData(parser.build(reader), types, tags, user, output);
		} catch (ValidityException e) {
			log.error("the IkeWiki WIF document was not valid",e);
		} catch (ParsingException e) {
			log.error("the IkeWiki WIF document could not be parsed",e);
		} catch (IOException e) {
			log.error("I/O error while reading IkeWiki WIF document",e);
		}
		return 0;
	}


 	private int importData(Document doc, Set<KiWiUriResource> types, Set<ContentItem> tags, User user, Collection<ContentItem> outputCIs) {

		if(types == null) {
			types = new HashSet<KiWiUriResource>();
		}
		
		if(tags == null) {
			tags = new HashSet<ContentItem>();
		}
		
		Builder parser = new Builder();
		
		
 		XPathContext xPathContext = new XPathContext();
 		
 		String NS_IKEWIKI_EXT = "http://ikewiki.srfg.at/syntax/1.0/ext";
 		
		xPathContext.addNamespace("wif", "http://ikewiki.srfg.at/syntax/1.0/core");
		xPathContext.addNamespace("iw", NS_IKEWIKI_EXT);
		xPathContext.addNamespace("rxr", "http://ilrt.org/discovery/2004/03/rxr/");
 		
		
 		try {
 			System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
 			
 			Document stylesheet = parser.build(this.getClass().getResourceAsStream("ikewiki/transform-html.xsl"));
 			XSLTransform transform = new XSLTransform(stylesheet); 
 			
			// add all namespaces from the imported knowledge base
			// if a namespace URI already exists, it is updated to the prefix of the imported declaration
			Nodes nl = doc.query("//iw:namespace",xPathContext);
			
			for(int i=0; i<nl.size(); i++) {
				nu.xom.Element e = (nu.xom.Element)nl.get(i);
				
				tripleStore.setNamespace(e.getAttributeValue("prefix"), e.getAttributeValue("uri"));
			}
			
			// add all pages from the imported knowledge base
			Nodes pages = doc.query("//wif:page",xPathContext);
			for(int i=0; i<pages.size(); i++) {
				nu.xom.Element e = (nu.xom.Element)pages.get(i);
				
				KiWiUriResource r = null;
				
				// check whether URI exists
				Nodes uris = e.query("*[local-name() = 'uri' and namespace-uri() = '"+NS_IKEWIKI_EXT+"']/text()", xPathContext);
				if(uris.size() > 0) {
					Text text = (Text)uris.get(0);

					r = tripleStore.createUriResource(text.getValue());
				} else {
					// check whether QTitle exists
					Nodes qtitles = e.query("*[local-name() = 'qtitle' and namespace-uri() = '"+NS_IKEWIKI_EXT+"']/text()",xPathContext);
					if(qtitles.size() > 0) {
						Text text = (Text)qtitles.get(0);
						
						
						
						r = tripleStore.createUriResource(getUriForQTitle(text.getValue()));
					}
				}
				if(r != null) {
					//if(hasImportPermission(user, r)) { // TODO: permissions are not yet setup when importing!
						log.info("importing page #0",r);
						
						// determine whether this page comes with multimedia data and decode it if yes
						boolean ismm = false; byte[] bytes = null; String mime_type = "";

						Elements datanodes = e.getChildElements(NS_IKEWIKI_EXT, "data");
						if(datanodes.size() == 1) {
							nu.xom.Element datanode = datanodes.get(0);

							mime_type = datanode.getAttributeValue("mime-type");

							String datacontent = "";
							for(int j=0; j<datanode.getChildCount(); j++) {
								if(datanode.getChild(j) instanceof Text) {
									Text txt = (Text)datanode.getChild(j);
									datacontent += txt.getValue();
								}
							}

							if(!datacontent.equals("")) {
								bytes = Base64.decode(datacontent);
							}

							// remove data node, we don't want it to be stored in the database
							e.removeChild(datanode);

							ismm = true;
						}

						Document d = new Document((Element)e.copy());

						
						
						
						ContentItem ci = contentItemService.createExternContentItem(r.getUri());
						
						// set title; label overrides if given
						Nodes titles = d.query("//wif:title/text()",xPathContext);
						if(titles.size() > 0) {
							contentItemService.updateTitle(ci, ((Text)titles.get(0)).getValue());
						}
						Nodes labels = d.query("//iw:label/text()",xPathContext);
						if(labels.size() > 0) {
							contentItemService.updateTitle(ci, ((Text)labels.get(0)).getValue());
						}
						
						// set content
						Nodes output = transform.transform(d);
						Document result = XSLTransform.toDocument(output);
						contentItemService.updateTextContentItem(ci, result.toXML());
						
						// set user to current user (we don't use IkeWiki's user information).
						ci.setAuthor(user);
						
						
						if(ismm) {
							File tmpfile = File.createTempFile("ikewiki-multimedia", "tmp");
							OutputStream tmpout = new BufferedOutputStream(new FileOutputStream(tmpfile));
							tmpout.write(bytes);
							tmpout.close();
							
					    	String type = multimediaService.getMimeType("", bytes);

					    	contentItemService.updateMediaContentItem(ci, bytes, type, UUID.randomUUID().toString());
					    	
					    	/*
					    	MediaContent mediaContent = new MediaContent(ci);
					    	mediaContent.setData(bytes);
					    	mediaContent.setMimeType(type);
					    	mediaContent.setFileName(UUID.randomUUID().toString());
							
					    	ci.setMediaContent(mediaContent);
					    	*/
						}
						
						contentItemService.saveContentItem(ci);

						// add parameter categories as tags
						for(ContentItem tag : tags) {
							taggingService.createTagging(tag.getTitle(), ci, tag, user);
						}
						
						// add parameter types as types
						for(KiWiUriResource type : types) {
							ci.addType(type);
						}
						
						// add kiwi:FeedPost type
						ci.addType(tripleStore.createUriResource("http://ikewiki.srfg.at/base/Page"));

//						entityManager.flush();
						log.info("imported content item '#0' with URI '#1'", ci.getTitle(), ci.getResource());
				} else {
					log.warn("page with empty URI ignored");
				}
			}
			
			// add all statements from the RXR graph
			RXRReader rxrReader = new RXRReader(doc);
			for (KiWiTriple triple : rxrReader) {
				tripleStore.storeTriple(triple);
			}
			
			log.info("finished import");
			
			return pages.size();
		} catch(IOException ex) {
			log.error("error while parsing XML exchange format",ex);
		} catch(XSLException ex) {
			log.error("error while transforming IkeWiki WIF to HTML",ex);
			log.error("cause was: ",ex.getCause());
			log.error("location: #0",((TransformerException)ex.getCause()).getMessageAndLocation());
		} catch(ValidityException ex) {
			log.error("XSL stylesheet is not valid",ex);
		} catch(ParsingException ex) {
			log.error("error while parsing XSL stylesheet",ex);
		} catch(Throwable ex) {
			ex.printStackTrace();
		}
		return 0;
			
    }

 	
	/**
	 * resolve a qualified title and return the corresponding unique URI.
	 * @param qtitle
	 * @return
	 * @throws DBException
	 */
	public String getUriForQTitle(String qtitle) {
		String article_ns, article_title;
		String[] components = qtitle.replace(" ","_").split(":",2);
		if(components.length == 2) {
			article_ns    = components[0];
			article_title = components[1];				
		} else if(components.length == 1){
			article_ns    = "";
			article_title = components[0];
		} else {
			article_ns    = "";
			article_title = "";
		}
		KiWiNamespace ns = tripleStore.getNamespace(article_ns);
		if(ns == null) {
			ns = new KiWiNamespace(article_ns,"");
			log.warn("namespace "+article_ns+" not found in database (for resource "+article_title+")");
		}
		
		return ns.getUri()+article_title;
	}


}
