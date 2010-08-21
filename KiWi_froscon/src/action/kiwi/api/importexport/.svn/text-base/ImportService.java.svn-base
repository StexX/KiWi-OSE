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
package kiwi.api.importexport;

import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.Date;
import java.util.Set;

import kiwi.api.importexport.importer.Importer;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiDataFormat;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.User;
import kiwi.service.importexport.KiWiImportException;

/**
 * A service for importing and exporting different kinds of data into the KiWi system.
 * 
 * Should at least support:
 * - a native KiWi format
 * - RDF in XML format
 * 
 * @author Sebastian Schaffert
 *
 */
public interface ImportService extends Importer {

	public void initialise();
	
	
	public void shutdown();
	
	public void destroy();
	
	/**
	 * Register the importer provided as argument with the import/export service
	 * 
	 * @param importerName the name of the importer to register
	 * @param componentName the component name of the importer to register
	 * @param imp the instance of the importer component to register (for performance reasons, 
	 *        avoid unnecessary JNDI lookups)
	 */
	public void registerImporter(String importerName, String componentName, Importer imp);
	
	
	/**
	 * Schedule an import for regular execution. When the ImportService is running, it
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
	public void scheduleImport(String description, Date interval, URL url, String format, Set<KiWiUriResource> types, Set<ContentItem> tags, User user);

	
	
	
	public void clearTripleStore();
	
	
	@Deprecated
	public void importData(InputStream is, KiWiDataFormat format) throws KiWiImportException;

	/**
	 * importData via java.io.Reader (e.g. StringReader)
	 * @param re
	 * @param format
	 */
	@Deprecated
	public void importData(Reader re, KiWiDataFormat format) throws KiWiImportException;
	
	
}
