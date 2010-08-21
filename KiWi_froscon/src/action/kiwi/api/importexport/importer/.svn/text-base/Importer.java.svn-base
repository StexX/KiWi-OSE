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
package kiwi.api.importexport.importer;

import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.Collection;
import java.util.Set;

import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.User;
import kiwi.service.importexport.KiWiImportException;


/**
 * Interface specification for importer components that may be used for importing content 
 * in various formats into the KiWi system.
 * <p>
 * Importers are typically implemented as stateless components that register themselves on
 * startup with the ImportService by calling registerImporter(); they usually make
 * use of the kiwiEntityManager for persisting entities and the tripleStore component for
 * adding/updating triples.
 * 
 * @author Sebastian Schaffert
 *
 */
public interface Importer {

	
	public void initialise();
	
	
	/**
	 * Get the name of this importer. Used for presentation to the user and for internal 
	 * identification.
	 * 
	 * @return a string uniquely identifying this importer
	 */
	public String getName();
	
	/**
	 * Get a description of this importer for presentation to the user.
	 * 
	 * @return a string describing this importer for the user
	 */
	public String getDescription();
	
	
	/**
	 * Get a collection of all mime types accepted by this importer. Used for automatically 
	 * selecting the appropriate importer in ImportService.
	 * 
	 * @return a set of strings representing the mime types accepted by this importer
	 */
	public Set<String> getAcceptTypes();
	
	/**
	 * Import data from the input stream provided as argument into the KiWi database. 
	 * 
	 * @param url the url from which to read the data
	 * @param format the mime type of the import format
	 * @param types the set of types to associate with each generated content item
	 * @param tags the set of content items to use as tags
	 * @param user the user to use as author of all imported data
	 * @param output a collection to be filled with new content item created while importing data. It can be null.
	 * 
	 * @return the number of Content Items imported
	 * @throws KiWiImportException in case the import fails
	 */
	public int importData(URL url, String format, Set<KiWiUriResource> types, Set<ContentItem> tags, User user, Collection<ContentItem> output) throws KiWiImportException;
	

	/**
	 * Import data from the input stream provided as argument into the KiWi database. 
	 * 
	 * @param is the input stream from which to read the data
	 * @param format the mime type of the import format
	 * @param types the set of types to associate with each generated content item
	 * @param tags the set of content items to use as tags
	 * @param user the user to use as author of all imported data
	 * @param output a collection to be filled with new content item created while importing data. It can be null.
	 * 
	 * @return the number of Content Items imported
	 * @throws KiWiImportException in case the import cannot execute
	 */
	public int importData(InputStream is, String format, Set<KiWiUriResource> types, Set<ContentItem> tags, User user, Collection<ContentItem> output) throws KiWiImportException;
	
	/**
	 * Import data from the reader provided as argument into the KiWi database.
	 * 
	 * @param reader the reader from which to read the data
	 * @param format the mime type of the import format
	 * @param types the set of types to associate with each generated content item
	 * @param tags the set of content items to use as tags
	 * @param user the user to use as author of all imported data
	 * @param output a collection to be filled with new content item created while importing data. It can be null.
	 * 
	 * @return the number of Content Items imported
	 * @throws KiWiImportException in case the import fails
	 */
	public int importData(Reader reader, String format, Set<KiWiUriResource> types, Set<ContentItem> tags, User user, Collection<ContentItem> output) throws KiWiImportException;
	
}
