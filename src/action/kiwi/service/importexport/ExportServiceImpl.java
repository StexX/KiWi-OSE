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

import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ejb.PrePassivate;

import kiwi.api.event.KiWiEvents;
import kiwi.api.importexport.ExportServiceLocal;
import kiwi.api.importexport.ExportServiceRemote;
import kiwi.api.importexport.exporter.Exporter;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiTriple;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.core.Events;
import org.jboss.seam.log.Log;

/**
 * The export service integrates all available Export components and offers a unified interface
 * for exporting. It implements the Exporter interface and can thus be used like any other exporter.
 * <p>
 * Exporter components need to register to this service in their initialise() methods. See one of
 * the existing exporters as example.
 * 
 * @author Sebastian Schaffert
 *
 */
//@Stateless
@Name("kiwi.core.exportService")
@Scope(ScopeType.APPLICATION)
@AutoCreate
public class ExportServiceImpl implements ExportServiceLocal, ExportServiceRemote {

	@Logger
	private Log log;
	
	/**
	 * Map from exporter name to component name for exporters capable of handling collections of
	 * items.
	 */
	@In(value="exportService.itemExporters", scope=ScopeType.APPLICATION, required=false)
	@Out(value="exportService.itemExporters", scope=ScopeType.APPLICATION)
	private Map<String,String> itemExporters;
	
	/**
	 * Map from exporter name to component name for exporters capable of handling collections of
	 * triples.
	 */
	@In(value="exportService.tripleExporters", scope=ScopeType.APPLICATION, required=false)
	@Out(value="exportService.tripleExporters", scope=ScopeType.APPLICATION)
	private Map<String,String> tripleExporters;

	
	/**
	 * Map from mime type to exporter component name capable of exporting triples
	 */
	@In(value="exportService.mimeTripleExporters", scope=ScopeType.APPLICATION, required=false)
	@Out(value="exportService.mimeTripleExporters", scope=ScopeType.APPLICATION)
	private Map<String,String> mimeTripleExporters;
	
	/**
	 * Map from mime type to exporter component name capable of exporting items
	 */
	@In(value="exportService.mimeItemExporters", scope=ScopeType.APPLICATION, required=false)
	@Out(value="exportService.mimeItemExporters", scope=ScopeType.APPLICATION)
	private Map<String,String> mimeItemExporters;

	
	@PrePassivate
	public void shutdown() {
		log.info("KiWi Export Service shutting down ...");
	}
	
	public void destroy() {
		
	}

	@Observer(KiWiEvents.CONFIGURATIONSERVICE_INIT)
	public void initialise() {
		log.info("KiWi Export Service starting up ...");
		
		if(tripleExporters == null || itemExporters == null) {
			tripleExporters = new HashMap<String, String>();
			itemExporters   = new HashMap<String, String>();
	
			
			mimeTripleExporters = new HashMap<String, String>();
			mimeItemExporters   = new HashMap<String, String>();
		}
		
		Events.instance().raiseAsynchronousEvent(KiWiEvents.EXPORTSERVICE_INIT);			

	}

	/**
	 * Register the exporter provided as argument with the import/export service
	 * 
	 * @param exporterName the name of the exporter to register
	 * @param componentName the component name of the exporter to register
	 * @param exp the instance of the exporter component to register (for performance reasons, 
	 *        avoid unnecessary JNDI lookups)
	 */
	@Override
	public void registerExporter(String exporterName, String componentName, Exporter exp) {
		log.info("registering exporter #0 ...",exporterName);
		if(exp.isItemExportSupported()) {
			itemExporters.put(exporterName, componentName);
			for(String mimeType : exp.getAcceptItemTypes()) {
				mimeItemExporters.put(mimeType,componentName);
			}
		}
		
		if(exp.isTripleExportSupported()) {
			tripleExporters.put(exporterName, componentName);
			for(String mimeType : exp.getAcceptTripleTypes()) {
				mimeTripleExporters.put(mimeType,componentName);
			}
		}
	}

	@Override
	public void exportUpdateOntology(Iterable<ContentItem> items, String format, OutputStream out, Date since) {
		log.info("exporting update ontology");
		Exporter e = getItemExporterForMimeType(format);
		if(e != null) {
			e.exportUpdateOntology(items, format, out, since);  
		} else {
			throw new IllegalArgumentException("mime type "+format+" not supported for export");
		}
	}
	

	/**
	 * Export the items passed as argument by writing them into a string that is returned as return
	 * value. The method selects the appropriate exporter for the given mime type and throws an
	 * IllegalArgumentException when the format is not supported.
	 * 
	 * @see kiwi.api.importexport.exporter.Exporter#exportItems(java.lang.Iterable, java.lang.String)
	 */
	@Override
	public String exportItems(Iterable<ContentItem> items, String format) {
		
		Exporter e = getItemExporterForMimeType(format);
		if(e != null) {
			return e.exportItems(items, format);
		} else {
			throw new IllegalArgumentException("mime type "+format+" not supported for export");
		}
	}

	/**
	 * Export the items passed as argument by writing them into a string that is returned as return
	 * value. The method selects the appropriate exporter for the given mime type and throws an
	 * IllegalArgumentException when the format is not supported.
	 * 
	 * @see kiwi.api.importexport.exporter.Exporter#exportItems(java.lang.Iterable, java.lang.String, java.io.OutputStream)
	 */
	@Override
	public void exportItems(Iterable<ContentItem> items, String format, OutputStream out) {
		Exporter e = getItemExporterForMimeType(format);
		if(e != null) {
			e.exportItems(items, format, out);
		} else {
			throw new IllegalArgumentException("mime type "+format+" not supported for export");
		}
	}

	/**
	 * Export the triples passed as argument by writing them into a string that is returned as return
	 * value. The method selects the appropriate exporter for the given mime type and throws an
	 * IllegalArgumentException when the format is not supported.
	 * 
	 * @see kiwi.api.importexport.exporter.Exporter#exportTriples(java.lang.Iterable, java.lang.String)
	 */
	@Override
	public String exportTriples(Iterable<KiWiTriple> triples, String format) {
		Exporter e = getTripleExporterForMimeType(format);
		if(e != null) {
			return e.exportTriples(triples, format);
		} else {
			throw new IllegalArgumentException("mime type "+format+" not supported for export");
		}
	}

	/**
	 * Export the triples passed as argument by writing them into a string that is returned as return
	 * value. The method selects the appropriate exporter for the given mime type and throws an
	 * IllegalArgumentException when the format is not supported.
	 * 
	 * @see kiwi.api.importexport.exporter.Exporter#exportTriples(java.lang.Iterable, java.lang.String, java.io.OutputStream)
	 */
	@Override
	public void exportTriples(Iterable<KiWiTriple> triples, String format, OutputStream out) {
		Exporter e = getTripleExporterForMimeType(format);
		if(e != null) {
			e.exportTriples(triples, format, out);
		} else {
			throw new IllegalArgumentException("mime type "+format+" not supported for export");
		}

	}

	/**
	 * Return the triple export mime types supported by this service. Aggregates the types of all
	 * registered exporters and returns them as a Set.
	 * 
	 * @see kiwi.api.importexport.exporter.Exporter#getAcceptTypes()
	 */
	@Override
	@BypassInterceptors
	public Set<String> getAcceptTripleTypes() {
		HashSet<String> result = new HashSet<String>();
		
		for(String componentName : tripleExporters.values()) {
			Exporter exp = (Exporter) Component.getInstance(componentName);
			
			result.addAll(exp.getAcceptTripleTypes());
		}
		
		return result;
	}

	/**
	 * Return the item export mime types supported by this service. Aggregates the types of all
	 * registered exporters and returns them as a Set.
	 * 
	 * @see kiwi.api.importexport.exporter.Exporter#getAcceptTypes()
	 */
	@Override
	@BypassInterceptors
	public Set<String> getAcceptItemTypes() {
		HashSet<String> result = new HashSet<String>();
		
		for(String componentName : tripleExporters.values()) {
			Exporter exp = (Exporter) Component.getInstance(componentName);
			
			result.addAll(exp.getAcceptItemTypes());
		}
		
		return result;
	}
	
	
	/* (non-Javadoc)
	 * @see kiwi.api.importexport.exporter.Exporter#getDescription()
	 */
	@Override
	public String getDescription() {
		return "The Export Service delegates export tasks to registered exporters";
	}

	/* (non-Javadoc)
	 * @see kiwi.api.importexport.exporter.Exporter#getName()
	 */
	@Override
	public String getName() {
		return "Generic Export Service";
	}

	/* (non-Javadoc)
	 * @see kiwi.api.importexport.exporter.Exporter#isItemExportSupported()
	 */
	@Override
	public boolean isItemExportSupported() {
		return true;
	}

	/* (non-Javadoc)
	 * @see kiwi.api.importexport.exporter.Exporter#isTripleExportSupported()
	 */
	@Override
	public boolean isTripleExportSupported() {
		return true;
	}

	
	/**
	 * Look up the triple exporter registered for the given mime type
	 * @param mime_type the mime type to look for
	 * @return an Exporter that is capable of handling the mime type
	 */
	private Exporter getTripleExporterForMimeType(String mime_type) {
		
		String componentName = mimeTripleExporters.get(mime_type);
		if(componentName != null) {
			return (Exporter)Component.getInstance(componentName);
		} else {
			log.error("exporter for mime type #0 could not be found",mime_type);
			return null;
		}
	}

	/**
	 * Look up the triple exporter registered for the given mime type
	 * @param mime_type the mime type to look for
	 * @return an Exporter that is capable of handling the mime type
	 */
	private Exporter getItemExporterForMimeType(String mime_type) {
		String componentName = mimeItemExporters.get(mime_type);
		if(componentName != null) {
			return (Exporter)Component.getInstance(componentName);
		} else {
			log.error("exporter for mime type #0 could not be found",mime_type);
			return null;
		}
	}

}
