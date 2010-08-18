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
package kiwi.api.importexport.exporter;

import java.io.OutputStream;
import java.util.Date;
import java.util.Set;

import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiTriple;

/**
 * Interface specification for exporter components that may be used for exporting content 
 * in various formats from the KiWi system.
 * <p>
 * Exporters are typically implemented as stateless components that register themselves on
 * startup with the ExportService by calling registerExporter(); they usually make
 * use of the kiwiEntityManager for loading entities and the tripleStore component for
 * reading triples.
 * 
 * @author Sebastian Schaffert
 *
 */
public interface Exporter {

	public void initialise();
	
	
	/**
	 * Get the name of this exporter. Used for presentation to the user and for internal 
	 * identification.
	 * 
	 * @return a string uniquely identifying this importer
	 */
	public String getName();
	
	/**
	 * Get a description of this exporter for presentation to the user.
	 * 
	 * @return a string describing this exporter for the user
	 */
	public String getDescription();
	
	
	/**
	 * Get a collection of all mime types accepted by this exporter. Used for automatically 
	 * selecting the appropriate exporter in ExportService.
	 * 
	 * @return a set of strings representing the mime types accepted by this exporter
	 */
	public Set<String> getAcceptTripleTypes();

	/**
	 * Get a collection of all mime types accepted by this exporter. Used for automatically 
	 * selecting the appropriate exporter in ExportService.
	 * 
	 * @return a set of strings representing the mime types accepted by this exporter
	 */
	public Set<String> getAcceptItemTypes();
	
	/**
	 * Returns true when the exporter supports exporting of triples (e.g. RDF/XML, RDF/N3, RXR, ...)
	 * <p>
	 * Called by the registration method of ExportService to register exporters for the right
	 * types.
	 * 
	 * @return true when the exporter supports exporting of triples
	 */
	public boolean isTripleExportSupported();
	
	/**
	 * Returns true when the exporter supports exporting of items.
	 * <p>
	 * Called by the registration method of ExportService to register exporters for the 
	 * right types.
	 * 
	 * @return
	 */
	public boolean isItemExportSupported();
	
	/**
	 * Export a collection of triples in a specific format. The collection is passed as iterable
	 * that can be iterated memory efficiently. Format is a string containing a mime type 
	 * supported by the exporter.
	 * 
	 * <p>
	 * The result will be returned as string, which potentially might require a lot of memory if 
	 * the result is huge. In case of large results, you might also use the exportData method 
	 * that writes to a output stream.
	 * 
	 * @param triples the collection of triples to export
	 * @param format a string containing the mime type that indicates the format; can throw an
	 *        IllegalFormatException when the mime type is not supported by this exporter
	 * @return
	 */
	public String exportTriples(Iterable<KiWiTriple> triples, String format);

	
	/**
	 * Export a collection of triples in a specific format. The collection is passed as iterable
	 * that can be iterated memory efficiently. Format is a string containing a mime type 
	 * supported by the exporter.
	 * 
	 * <p>
	 * The result will be written to the output stream passed as argument.
	 * 
	 * @param triples the collection of triples to export
	 * @param format a string containing the mime type that indicates the format; can throw an
	 *        IllegalFormatException when the mime type is not supported by this exporter
	 * @param out the output stream to write to
	 * @return
	 */
	public void exportTriples(Iterable<KiWiTriple> triples, String format, OutputStream out);

	/**
	 * Export a collection of content items in a specific format. The collection is passed as iterable
	 * that can be iterated memory efficiently. Format is a string containing a mime type 
	 * supported by the exporter.
	 * 
	 * <p>
	 * The result will be returned as string, which potentially might require a lot of memory if 
	 * the result is huge. In case of large results, you might also use the exportData method 
	 * that writes to a output stream.
	 * 
	 * @param items the collection of items to export
	 * @param format a string containing the mime type that indicates the format; can throw an
	 *        IllegalFormatException when the mime type is not supported by this exporter
	 * @return
	 */
	public String exportItems(Iterable<ContentItem> items, String format);

	
	/**
	 * Export a collection of triples in a specific format. The collection is passed as iterable
	 * that can be iterated memory efficiently. Format is a string containing a mime type 
	 * supported by the exporter.
	 * 
	 * <p>
	 * The result will be written to the output stream passed as argument.
	 * 
	 * @param items the collection of items to export
	 * @param format a string containing the mime type that indicates the format; can throw an
	 *        IllegalFormatException when the mime type is not supported by this exporter
	 */
	public void exportItems(Iterable<ContentItem> items, String format, OutputStream out);
	
	
	public void exportUpdateOntology(Iterable<ContentItem> items, String format, OutputStream out, Date d);

}
