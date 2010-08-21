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
package kiwi.service.importexport.exporter;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.ejb.Stateless;

import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.event.KiWiEvents;
import kiwi.api.importexport.ExportService;
import kiwi.api.importexport.exporter.ExporterLocal;
import kiwi.api.importexport.exporter.ExporterRemote;
import kiwi.api.tagging.TaggingService;
import kiwi.api.triplestore.TripleStore;
import kiwi.exception.NamespaceResolvingException;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.tagging.Tag;
import kiwi.service.triplestore.TripleStoreUtil;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.log.Log;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.n3.N3Writer;
import org.openrdf.rio.rdfxml.util.RDFXMLPrettyWriter;
import org.openrdf.rio.trig.TriGWriter;
import org.openrdf.rio.trix.TriXWriter;
import org.openrdf.rio.turtle.TurtleWriter;
import org.openrdf.sail.memory.MemoryStore;

/**
 * An exporter that allows to export items and triples in various RDF formats. It makes use of
 * Sesame to provide the RDF writing functionality.
 * 
 * @author Sebastian Schaffert
 *
 */
/**
 * @author Rolf Sint
 * @version 0.7
 * @since 0.7
 *
 */
@Stateless
@Name("kiwi.service.exporter.rdf")
@Scope(ScopeType.STATELESS)
public class RDFExporterImpl implements ExporterLocal, ExporterRemote {

	@Logger
	private Log log;

	@In(create = true)
	private TripleStore tripleStore;
	
	@In
	private KiWiEntityManager kiwiEntityManager;
	
	@In
	private TaggingService taggingService;

	private static String[] mime_types = {
		"application/rdf+xml",
		"text/n3",
		"application/turtle",
		"application/x-turtle",
		"application/trix",
		"application/x-trig"
	};

	@Observer(KiWiEvents.EXPORTSERVICE_INIT) 
	@BypassInterceptors
	public void initialise() {
		log.info("registering RDF exporter ...");
		
		ExportService ies = (ExportService) Component.getInstance("kiwi.core.exportService");
		
		ies.registerExporter(this.getName(),"kiwi.service.exporter.rdf",this);
	}
	
	/* (non-Javadoc)
	 * @see kiwi.api.importexport.exporter.Exporter#exportItems(java.lang.Iterable, java.lang.String)
	 */
	@Override
	public String exportItems(Iterable<ContentItem> items, String format) {
		try {
			Repository rep = exportItemsCommon(items);
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			
			exportRepositoryOutputStream(rep, format, out);
			rep.shutDown();
			
			return new String(out.toByteArray(), "utf-8");
		} catch(RepositoryException ex) {
			log.error("error while accessing temporary RDF repository", ex);
		} catch (UnsupportedEncodingException e) {
			log.error("error while converting byte array to string; wrong encoding?");
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see kiwi.api.importexport.exporter.Exporter#exportItems(java.lang.Iterable, java.lang.String, java.io.OutputStream)
	 */
	@Override
	public void exportItems(Iterable<ContentItem> items, String format, OutputStream out) {
		try {
			Repository rep = exportItemsCommon(items);
			exportRepositoryOutputStream(rep, format, out);
			rep.shutDown();
		} catch(RepositoryException ex) {
			log.error("error while accessing temporary RDF repository", ex);
		}

	}
	
	@Override
	public void exportUpdateOntology(Iterable<ContentItem> items, String format, OutputStream out, Date since) {
		log.info("writing update stream ...");
		try {
			Repository rep = exportAsUpdateOntology(items, since);
			exportRepositoryOutputStream(rep, format, out);
			rep.shutDown();
		} catch(RepositoryException ex) {
			log.error("error while accessing temporary RDF repository", ex);
		}

	}

	/* (non-Javadoc)
	 * @see kiwi.api.importexport.exporter.Exporter#exportTriples(java.lang.Iterable, java.lang.String)
	 */
	@Override
	public String exportTriples(Iterable<KiWiTriple> triples, String format) {
		try {
			Repository rep = exportTriplesCommon(triples);
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			
			exportRepositoryOutputStream(rep, format, out);
			rep.shutDown();
			
			return new String(out.toByteArray(), "utf-8");
		} catch(RepositoryException ex) {
			log.error("error while accessing temporary RDF repository", ex);
		} catch (UnsupportedEncodingException e) {
			log.error("error while converting byte array to string; wrong encoding?");
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see kiwi.api.importexport.exporter.Exporter#exportTriples(java.lang.Iterable, java.lang.String, java.io.OutputStream)
	 */
	@Override
	public void exportTriples(Iterable<KiWiTriple> triples, String format, OutputStream out) {
		try {
			Repository rep = exportTriplesCommon(triples);
			exportRepositoryOutputStream(rep, format, out);
			rep.shutDown();
		} catch(RepositoryException ex) {
			log.error("error while accessing temporary RDF repository", ex);
		}

	}

	
	/**
	 * Return a collection of mime types (as string) that represent the mime types supported by
	 * this exporter for exporting content items.
	 * 
	 * @see kiwi.api.importexport.exporter.Exporter#getAcceptItemTypes()
	 */
	@Override
	@BypassInterceptors
	public Set<String> getAcceptItemTypes() {
		return new HashSet<String>(Arrays.asList(mime_types));
	}

	/**
	 * Return a collection of mime types (as string) that represent the mime types supported by
	 * this exporter for exporting triples.
	 * 
	 * @see kiwi.api.importexport.exporter.Exporter#getAcceptTripleTypes()
	 */
	@Override
	@BypassInterceptors
	public Set<String> getAcceptTripleTypes() {
		return new HashSet<String>(Arrays.asList(mime_types));
	}

	/* (non-Javadoc)
	 * @see kiwi.api.importexport.exporter.Exporter#getDescription()
	 */
	@Override
	@BypassInterceptors
	public String getDescription() {
		return "An Exporter supporting to export data into various RDF formats like RDF/XML and N3";
	}

	/* (non-Javadoc)
	 * @see kiwi.api.importexport.exporter.Exporter#getName()
	 */
	@Override
	@BypassInterceptors
	public String getName() {
		return "RDF Exporter";
	}

	/* (non-Javadoc)
	 * @see kiwi.api.importexport.exporter.Exporter#isItemExportSupported()
	 */
	@Override
	@BypassInterceptors
	public boolean isItemExportSupported() {
		return true;
	}

	/* (non-Javadoc)
	 * @see kiwi.api.importexport.exporter.Exporter#isTripleExportSupported()
	 */
	@Override
	@BypassInterceptors
	public boolean isTripleExportSupported() {
		return true;
	}

	/**
	 * Create a in-memory Sesame repository containing the triples passed as argument.
	 * 
	 * @param triples
	 * @return
	 */
	private final Repository exportTriplesCommon(Iterable<KiWiTriple> triples) throws RepositoryException {
		Repository myRepository = new SailRepository(new MemoryStore());

		myRepository.initialize();
		RepositoryConnection myCon = myRepository.getConnection();

		ValueFactory f = myRepository.getValueFactory();

		for(KiWiTriple t : triples) {
			myCon.add(TripleStoreUtil.transformKiWiToSesame(f, t));
		}

		myCon.close();

		return myRepository;
	}

	
	
	/**
	 * Export Triples for Update Ontology
	 * 
	 * @param items
	 * @param since
	 * @return
	 * @throws RepositoryException
	 */
	private Repository exportAsUpdateOntology(Iterable<ContentItem> items, Date since) throws RepositoryException{
		Repository myRepository = new SailRepository(new MemoryStore());

		myRepository.initialize();
		RepositoryConnection myCon = myRepository.getConnection();

		ValueFactory f = myRepository.getValueFactory();
		
		log.info(f == null?"f = null":"ok");
		
		for(ContentItem item : items) {
			

		    
			if(item.getModified() != null && item.getModified().after(since)){
			    	
				addSpeciProperties(item);
				
				filter(item, myCon, f,"creator",Constants.NS_DC_TERMS, Appearence.First);
				filter(item, myCon, f,"createdOn",Constants.NS_KIWI_CORE, Appearence.First);
				filter(item, myCon, f,"created",Constants.NS_DC_TERMS,Appearence.Latest);
				filter(item, myCon, f,"title",Constants.NS_KIWI_CORE,Appearence.First);
				filter(item, myCon, f,"modified",Constants.NS_DC_TERMS,Appearence.Latest);
				filter(item, myCon, f,"isReplacedBy",Constants.NS_DC_TERMS, Appearence.Latest);
				filter(item, myCon, f, "type", Constants.NS_RDF, Appearence.DUPLICATES_ALLOWED);
			}
			
		
			if(item.getDeletedOn() != null && item.getDeletedOn().after(since)){
				filter(item, myCon, f,"deprecated",Constants.NS_OWL,Appearence.Latest);
				filter(item, myCon, f,"deletedOn",Constants.NS_KIWI_CORE, Appearence.Latest);
			}
		}

		myCon.close();
		return myRepository;
	
	}

	/**
	 * @param item
	 */
	private void addSpeciProperties(ContentItem item) {
	    try {
		log.info(item == null?"item = null":"item ok");
		log.info(item.getAuthor() == null?"author = null":"author ok");
				
		if(item.getAuthor() != null)
		    item.getResource().setProperty(Constants.NS_DC_TERMS+"creator", ((KiWiUriResource)item.getAuthor().getResource()).getUri());
	        item.getResource().setProperty(Constants.NS_DC_TERMS+"created", item.getCreated().toString());
	        kiwiEntityManager.persist(item);
	    } catch (NamespaceResolvingException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	}
	
	/**
	 * iterates over all triples and adds only these triples which contain the value given in the value String
	 * if one triple appears more than one time, return only the latest created element
	 * 
	 * @param item
	 * @param myCon
	 * @param f
	 * @param value
	 * @param latest
	 * @throws RepositoryException
	 */
	private void filter(ContentItem item, RepositoryConnection myCon, ValueFactory f, String value, String namespace, Appearence app) throws RepositoryException{
		KiWiTriple triple2store = null;
		for(KiWiTriple t : item.getResource().listOutgoingIncludeDeleted()) {
		    	if(t != null && t.getProperty() != null && t.getProperty().getLabel() != null){		
		    	
		    	    if(t.getProperty().getLabel().contains(value) && t.getProperty().getUri().contains(namespace)){
				if(triple2store == null){
					triple2store = t;
				}
				if(app == Appearence.Latest)
				{
					if(t.getCreated().after(triple2store.getCreated()))
						triple2store = t;
				}
				else if(app == Appearence.First)
				{
					if(t.getCreated().before(triple2store.getCreated()))
						triple2store = t;
				}
				else if(app == Appearence.DUPLICATES_ALLOWED){
				    myCon.add(TripleStoreUtil.transformKiWiToSesame(f, t));
				}
			}
		    	}    
		}
		if(triple2store != null)
			myCon.add(TripleStoreUtil.transformKiWiToSesame(f, triple2store));
	}
	
	/**
	 * Create a in-memory Sesame repository containing the triples concerning the items passed as 
	 * argument.
	 * <p>
	 * The collection of triples is constructed as follows:
	 * <ul>
	 * <li>for each item, all outgoing triples are included</li>
	 * <li>for each item, the triples of all taggings that have this item as "taggedItem" are included</li>
	 * </ul>
	 * 
	 * @param triples
	 * @return
	 */
	private final Repository exportItemsCommon(Iterable<ContentItem> items) throws RepositoryException {
		Repository myRepository = new SailRepository(new MemoryStore());

		myRepository.initialize();
		RepositoryConnection myCon = myRepository.getConnection();

		ValueFactory f = myRepository.getValueFactory();

		for(ContentItem item : items) {
		
			for(KiWiTriple t : item.getResource().listOutgoing()) {
				myCon.add(TripleStoreUtil.transformKiWiToSesame(f, t));
			}
			
			for(Tag t : taggingService.getTags(item)) {
				for(KiWiTriple triple : t.getResource().listOutgoing()) {
					myCon.add(TripleStoreUtil.transformKiWiToSesame(f, triple));
				}
				
			}
		}

		myCon.close();

		return myRepository;
	}


	private final void exportRepositoryOutputStream(Repository repository, String format, OutputStream out) {
		
        try {
        	RDFHandler handler;
        	if("application/rdf+xml".equalsIgnoreCase(format)) {
        		handler = new RDFXMLPrettyWriter(out);
        	} else if("text/n3".equalsIgnoreCase(format)) {
        		handler = new N3Writer(out);
        	} else if("application/turtle".equalsIgnoreCase(format) || "application/x-turtle".equalsIgnoreCase(format)) {
           		handler = new TurtleWriter(out);       	
        	} else if("application/trix".equalsIgnoreCase(format)) {
        		handler = new TriXWriter(out);
        	} else if("application/x-trig".equalsIgnoreCase(format)) {
        		handler = new TriGWriter(out);
        	} else {
        		throw new IllegalArgumentException("format "+format+" not supported by "+getName());
        	}
        	
        	RepositoryConnection myCon = repository.getConnection();
        	myCon.export(handler);
        	myCon.close();
        } catch (RDFHandlerException ex) {
        	log.error("error while exporting data to RDF/XML", ex);
        } catch (RepositoryException ex) {
            log.error("error while exporting data to RDF/XML", ex);
        }

	}
}

/**
 * 
 * @author Rolf Sint
 * @version 0.7
 * @since 0.7
 *
 * uses to identify the Appearence of a property
 * first ... shows only the first appearence
 * latest ... shows the latest appearence of a property
 * duplicates_allowed shows all properties
 * 
 */
enum Appearence{
    Latest, First, DUPLICATES_ALLOWED
}
