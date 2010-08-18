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

package kiwi.service.importexport;

import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ejb.PrePassivate;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.transaction.SystemException;

import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.event.KiWiEvents;
import kiwi.api.importexport.ImportServiceLocal;
import kiwi.api.importexport.ImportServiceRemote;
import kiwi.api.importexport.importer.Importer;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.content.ContentItem;
import kiwi.model.importexport.ImportTask;
import kiwi.model.kbase.KiWiDataFormat;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.User;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.RaiseEvent;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.core.Events;
import org.jboss.seam.log.Log;
import org.jboss.seam.transaction.Transaction;


/**
 * @author Sebastian Schaffert
 *
 */
@Stateless
@Name("kiwi.core.importService")
@Scope(ScopeType.STATELESS)
@AutoCreate
//@Startup
public class ImportServiceImpl implements ImportServiceLocal, ImportServiceRemote {

	@Logger
	private Log log;
	
	/**
	 * Map from importer name to component name
	 */
	@In(value="importService.importers",scope=ScopeType.APPLICATION, required=false)
	@Out(value="importService.importers",scope=ScopeType.APPLICATION)
	private Map<String,String> importers;
	
	
	/**
	 * Map from mime type to importer component name
	 */
	@In(value="importService.mimeImporters",scope=ScopeType.APPLICATION, required=false)
	@Out(value="importService.mimeImporters",scope=ScopeType.APPLICATION)
	private Map<String,String> mimeImporters;
	
	
	// the triple store used by this KiWi system
	@In(value = "tripleStore", create = true)
	private TripleStore tripleStore;

	@In
	private EntityManager entityManager;
	
	@In
	private KiWiEntityManager kiwiEntityManager;

	
//	@Factory(value="importService.importers",scope=ScopeType.APPLICATION,autoCreate=true)
//	public void initImporters() {
//		if(importers == null) {
//			importers = new HashMap<String,String>();
//			mimeImporters = new HashMap<String,String>();		
//		}
//	}
	
	@Observer(KiWiEvents.SEAM_POSTINIT)
	@BypassInterceptors
	public void initialise() {
		log.info("KiWi Import Service starting up ...");
		
		Events.instance().raiseAsynchronousEvent(KiWiEvents.IMPORTSERVICE_INIT);			
		
	}
	
	@PrePassivate
	public void shutdown() {
		log.info("KiWi Import Service shutting down ...");
	}
	
	//@Destroy
	public void destroy() {
		
	}
	
	
	/**
	 * Register the importer provided as argument with the import/export service
	 * 
	 * @param importerName the name of the importer to register
	 * @param componentName the component name of the importer to register
	 */
	@Override
	public void registerImporter(String importerName, String componentName, Importer imp) {
		
		getImporters().put(importerName,componentName);
		for(String mimeType : imp.getAcceptTypes()) {
			log.debug("importer for mime type #0: #1", mimeType,componentName);
			getMimeImporters().put(mimeType,componentName);
		}
	}
		
	

	/**
	 * Schedule an import for regular execution. When the ExportService is running, it
	 * checks at regular intervals whether the task is due and runs it if necessary.
	 * 
	 * @param description a human-readable description of the task
	 * @param interval a Date representing the interval in which to run the task
	 * @param url the URL to retrieve
	 * @param format the format of the content to retrieve
	 * @param types the types to associate with each imported content item
	 * @param tags the tags to associate with each imported content item
	 * @param user the user to set as author for each imported content item
	 */
	public void scheduleImport(String description, Date interval, URL url, String format, Set<KiWiUriResource> types, Set<ContentItem> tags, User user) {
		ImportTask task = new ImportTask(description,url.toString(),format,interval,user);
		task.setTags(tags);
		task.setTypes(types);
		
		entityManager.persist(task);
	}
	
	
	
	
	@Override
	public Set<String> getAcceptTypes() {
		HashSet<String> result = new HashSet<String>();
		
		for(String componentName : importers.values()) {
			Importer imp = (Importer) Component.getInstance(componentName);
			
			result.addAll(imp.getAcceptTypes());
		}
		
		return result;
	}

	@Override
	public String getDescription() {
		return "The Import Service delegates import tasks to registered importers";
	}

	@Override
	public String getName() {
		return "Generic Import Service";
	}

	/**
	 * Import data from the input stream provided as argument into the KiWi database. 
	 * 
	 * @param is the input stream from which to read the data
	 * @param format the mime type of the import format
	 * @param types the set of types to associate with each generated content item
	 * @param tags the set of content items to use as tags
	 * @param user the user to use as author of all imported data
	 * @throws KiWiImportException in case the import fails
	 */
//	@Transactional
	@Override
	public int importData(InputStream is, String format, Set<KiWiUriResource> types, Set<ContentItem> tags, User user, Collection<ContentItem> output) throws KiWiImportException {
		try {
			Transaction.instance().setTransactionTimeout(60000);
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Importer i = getImporterForMimeType(format);
		return i.importData(is, format, types, tags, user, output);
	}

	/**
	 * Import data from the reader provided as argument into the KiWi database.
	 * 
	 * @param reader the reader from which to read the data
	 * @param format the mime type of the import format
	 * @param types the set of types to associate with each generated content item
	 * @param tags the set of content items to use as tags
	 * @param user the user to use as author of all imported data
	 * @throws KiWiImportException in case the import fails 
	 */
//	@Transactional
	@Override
	public int importData(Reader reader, String format, Set<KiWiUriResource> types, Set<ContentItem> tags, User user, Collection<ContentItem> output) throws KiWiImportException {
		Importer i = getImporterForMimeType(format);
		return i.importData(reader, format, types, tags, user, output);
	}

	/**
	 * Import data from the input stream provided as argument into the KiWi database. 
	 * 
	 * @param url the url from which to read the data
	 * @param format the mime type of the import format
	 * @param types the set of types to associate with each generated content item
	 * @param tags the set of content items to use as tags
	 * @param user the user to use as author of all imported data
	 * @throws KiWiImportException In case there is no importer for the given format
	 */
//	@Transactional
	@Override
	public int importData(URL url, String format, Set<KiWiUriResource> types, Set<ContentItem> tags, User user, Collection<ContentItem> output) throws KiWiImportException {
		try {
			Transaction.instance().setTransactionTimeout(60000);
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Importer i = getImporterForMimeType(format);
		return i.importData(url, format, types, tags, user, output);
	}
	
	
	
	
	
	
	/**
	 * Look up the importer registered for the given mime type
	 * @param mime_type the mime type to look for
	 * @return an Importer that is capable of handling the mime type
	 * @throws KiWiImportException in case there cannot be found 
	 * an importer for the given mime-type
	 */
	private Importer getImporterForMimeType(String mime_type) throws KiWiImportException {
		
		
		String componentName = getMimeImporters().get(mime_type);
		if(componentName != null) {
			return (Importer)Component.getInstance(componentName);
		} else {
			log.error("importer for mime type #0 could not be found",mime_type);
			throw new KiWiImportException("importer for mime type "+ mime_type + " could not be found");
		}
	}

	
	/* The following methods are deprecated and will be removed in the future */
	
//	@Transactional
//	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@RaiseEvent("ontologyChanged")
	public void clearTripleStore() {
		tripleStore.clear();
	}
	
//	@Transactional
//	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Deprecated
	public void importData(InputStream is, KiWiDataFormat format) throws KiWiImportException {	
		User currentUser = (User) Component.getInstance("currentUser");
		
		//entityManager.clear();
		//entityManager.setFlushMode(FlushModeType.COMMIT);
		
		try {
			Transaction.instance().setTransactionTimeout(60000);
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// import data into triple store
		if(is != null && format != null) {
			//currentUser = kiwiEntityManager.merge(currentUser);
			currentUser = entityManager.find(User.class, currentUser.getId());

			Importer rdfImporter = (Importer) Component.getInstance("kiwi.service.importer.rdf");
			
			rdfImporter.importData(is, "application/rdf+xml", null, null, currentUser, null);
			
		} else {
			log.error("InputStream or KiWiDataformat object are null. InputStream: #0, KiWiDataFormat: #1", is, format);
			throw new KiWiImportException("InputStream or KiWiDataformat object are null.");
		}
	}
	
	/**
	 * importData via java.io.Reader (e.g. StringReader)
	 * @param re
	 * @param format
	 * @throws KiWiImportException in case the import fails
	 */
//	@Transactional
//	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Deprecated
	public void importData(Reader re, KiWiDataFormat format) throws KiWiImportException {	
		User currentUser = (User) Component.getInstance("currentUser");
		
		//entityManager.clear();
		//entityManager.setFlushMode(FlushModeType.COMMIT);

		try {
			Transaction.instance().setTransactionTimeout(60000);
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("current transaction: #0", Transaction.instance().hashCode());

		// import data into triple store
		if(re != null && format != null) {
			currentUser = kiwiEntityManager.merge(currentUser);

			Importer rdfImporter = (Importer) Component.getInstance("kiwi.service.importer.rdf");
			
			rdfImporter.importData(re, "application/rdf+xml", null, null, currentUser, null);
		} else {
			log.error("InputStream or KiWiDataformat object are null. InputStream: #0, KiWiDataFormat: #1", re, format);
		}
	}

	public Map<String, String> getImporters() {
		if(importers == null) {
			importers = new HashMap<String, String>();
		}
		return importers;
	}

	public Map<String, String> getMimeImporters() {
		if(mimeImporters == null) {
			mimeImporters = new HashMap<String, String>();
		}
		return mimeImporters;
	}


	
	
	
}
