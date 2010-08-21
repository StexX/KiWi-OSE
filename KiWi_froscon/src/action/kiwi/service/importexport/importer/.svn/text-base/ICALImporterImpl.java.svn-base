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
 * sschaffe
 * 
 */
package kiwi.service.importexport.importer;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.log.Log;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.event.KiWiEvents;
import kiwi.api.importexport.ImportService;
import kiwi.api.importexport.importer.ImporterLocal;
import kiwi.api.importexport.importer.ImporterRemote;
import kiwi.api.tagging.TaggingService;
import kiwi.api.triplestore.TripleStore;
import kiwi.api.user.UserService;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.User;
import kiwi.service.importexport.KiWiImportException;

/**
 * ICALImporter - a KiWi importer for loading files in iCal format. For each calendar entry, a
 * new content item is created according to the iCal ontology.
 *
 * @author Sebastian Schaffert
 *
 */
@Stateless
@Name("kiwi.service.importer.ical")
@Scope(ScopeType.STATELESS)
public class ICALImporterImpl implements ImporterLocal, ImporterRemote {

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
	private UserService userService;
	
	@In(create = true)
	private TaggingService taggingService;

	private static String[] mime_types = {
		"text/calendar"
	};
	
	
	@Observer(KiWiEvents.SEAM_POSTINIT) 
	@BypassInterceptors
	public void initialise() {
		log.info("registering iCal importer ...");
		
		ImportService ies = (ImportService) Component.getInstance("kiwi.core.importService");
		
		ies.registerImporter(this.getName(),"kiwi.service.importer.ical",this);
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
		return "Importer for parsing the iCal calender format";
	}

	/**
	 * Get the name of this importer. Used for presentation to the user and for internal 
	 * identification.
	 * 
	 * @return a string uniquely identifying this importer
	 */
	@Override
	public String getName() {
		return "iCal";
	}

	/* (non-Javadoc)
	 * @see kiwi.api.importexport.importer.Importer#importData(java.net.URL, java.lang.String, java.util.Set, java.util.Set, kiwi.model.user.User, java.util.Collection)
	 */
	@Override
	public int importData(URL url, String format, Set<KiWiUriResource> types, Set<ContentItem> tags, User user, Collection<ContentItem> output) throws KiWiImportException {
		try {
			return importData(url.openStream(), format, types, tags, user, output);
		} catch (IOException ex) {
			log.error("I/O error while importing data from URL #0: #1",url, ex.getMessage());
			throw new KiWiImportException("I/O error while importing data from URL "+url, ex);
		}
	}

	/* (non-Javadoc)
	 * @see kiwi.api.importexport.importer.Importer#importData(java.io.InputStream, java.lang.String, java.util.Set, java.util.Set, kiwi.model.user.User, java.util.Collection)
	 */
	@Override
	public int importData(InputStream is, String format, Set<KiWiUriResource> types, Set<ContentItem> tags, User user, Collection<ContentItem> output) throws KiWiImportException {
		CalendarBuilder builder = new CalendarBuilder();
		
		try {
			return importData(builder.build(is),types,tags,user,output);
		} catch (IOException e) {
			log.error("I/O error while trying to import calendar data from input stream",e);
			throw new KiWiImportException("I/O error while trying to import calendar data from input stream",e);
		} catch (ParserException e) {
			log.error("parse error while trying to import calendar data from input stream",e);
			throw new KiWiImportException("parse error while trying to import calendar data from input stream",e);
		}
	}

	/* (non-Javadoc)
	 * @see kiwi.api.importexport.importer.Importer#importData(java.io.Reader, java.lang.String, java.util.Set, java.util.Set, kiwi.model.user.User, java.util.Collection)
	 */
	@Override
	public int importData(Reader reader, String format, Set<KiWiUriResource> types, Set<ContentItem> tags, User user, Collection<ContentItem> output) throws KiWiImportException {
		CalendarBuilder builder = new CalendarBuilder();
		
		try {
			return importData(builder.build(reader),types,tags,user,output);
		} catch (IOException e) {
			log.error("I/O error while trying to import calendar data from reader",e);
			throw new KiWiImportException("I/O error while trying to import calendar data from reader",e);
		} catch (ParserException e) {
			log.error("parse error while trying to import calendar data from reader",e);
			throw new KiWiImportException("parse error while trying to import calendar data from reader",e);
		}
	}

	
	private int importData(Calendar cal, Set<KiWiUriResource> types, Set<ContentItem> tags, User user, Collection<ContentItem> output) {
		
		for(Object c : cal.getComponents(net.fortuna.ical4j.model.Component.VEVENT)) {
			
			VEvent event = (VEvent)c;
			
			
		}
		
		return 0;
	}

}
