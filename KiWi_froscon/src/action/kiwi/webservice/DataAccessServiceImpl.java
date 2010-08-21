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
package kiwi.webservice;

import java.io.ByteArrayInputStream;
import java.util.HashSet;
import java.util.Set;

import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import kiwi.api.content.ContentItemService;
import kiwi.api.importexport.ImportService;
import kiwi.api.tagging.TaggingService;
import kiwi.api.triplestore.TripleStore;
import kiwi.api.user.PasswordGeneratorService;
import kiwi.api.user.UserService;
import kiwi.api.webservice.DataAccessServiceLocal;
import kiwi.api.webservice.DataAccessServiceRemote;
import kiwi.exception.UserExistsException;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.User;
import kiwi.service.importexport.KiWiImportException;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.jboss.seam.security.Identity;

/**
 * This is the WebService access point for data inserts and updates.
 * 
 * @author Karsten Jahn
 */
@WebService(name = "DataAccessService", serviceName = "DataAccessService")
@Name("webservice.dataAccess")
@Scope(ScopeType.STATELESS)
@Stateless
@SOAPBinding(style = Style.RPC)
public class DataAccessServiceImpl implements DataAccessServiceRemote, DataAccessServiceLocal {

	/** Instead of injection, we ask for the components inside the methods. */
	private Log log;
	private ImportService importService;

	public DataAccessServiceImpl() {
	}

	/**
	 * Searches for or creates the Import User.
	 * 
	 * @return The Import User.
	 */
	private User getImportUser() {
		UserService userService = (UserService) Component
				.getInstance("userService");
		User importUser = userService.getUserByLogin("import");

		PasswordGeneratorService passwordGenerator = (PasswordGeneratorService) Component.getInstance("passwordGenerator");
		
		if (importUser == null) {
			try {
				Identity.setSecurityEnabled(false);
				importUser = userService.createUser("import",passwordGenerator.generatePassword());
			} catch (UserExistsException e) {
				log.error("This should seriously never happen, since we could not find the user before... " + e.getMessage());
			} finally {
				Identity.setSecurityEnabled(true);
			}
		}
		return importUser;
	}

	/**
	 * The publishing method takes a rdf+xml formatted string and converts it
	 * into triples, which are imported into KiWi. Additionally a new CI is
	 * created, based on the given Template.
	 * 
	 * @param rdfdata
	 * @param mimetype
	 * @param templateURI
	 *            The URI of the template in KiWi.
	 * @param originURI
	 *            The origin URI of the resource.
	 * @return The URL of the created CI or <code>null</code> if operation was
	 *         not successful.
	 * @see kiwi.api.webservice.DataAccessService#publishRDF(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	public String publishRDF(final String rdfdata, final String mimetype, String originURI,
			String templateURI) {

		log = Logging.getLog(this.getClass());
		log.info("Importing data..." 
					+ "\nMimeType: " + mimetype 
					+ "\nOrigin URL: " + originURI 
					+ "\nTemplate URL: " + templateURI 
					+ "\nRDF-Data: " + rdfdata);

		// Some checks first
		if (mimetype == null || !mimetype.equals("application/rdf+xml")) {
			log.error("FAILED IMPORT: You have to specify the mime type of your input-stream.");
			return null;
		}
		if (rdfdata == null) {
			log.error("FAILED IMPORT: Your rdf data may not be null.");
			return null;
		}
		if (templateURI == null) {
			log.error("FAILED IMPORT: Your templateURI may not be null.");
			return null;
		}
		if (!mimetype.equals("application/rdf+xml")) {
			log.error("FAILED IMPORT: The publishing of data in another format than rdf/xml is not yet implemented");
			return null;
		}

		// now the processing...
		ContentItemService contentItemService = (ContentItemService) Component
				.getInstance("contentItemService");

		/* get the template content item */
		ContentItem template = contentItemService
				.getContentItemByUri(templateURI);
		if (template == null) {
			log.error("FAILED IMPORT: The ContentItem " + templateURI
					+ " does not exist");
			return null;
		}
		if (template.getTextContent() == null) {
			log.error("FAILED IMPORT: The ContentItem " + templateURI
					+ " does not contain any textual content.");
			return null;
		}

		/* get the content item for the RDF property 'fromTemplate' */
		ContentItem fromTemplate = contentItemService
				.getContentItemByUri(Constants.NS_KIWI_CORE + "FromTemplate");

		if (fromTemplate == null) {
			log.error("FAILED IMPORT: The ContentItem "
							+ Constants.NS_KIWI_CORE
							+ "FromTemplate does not exist. The reason might be that the kiwi-core ontology has not been loaded. Please contact a KiWi administrator about this problem.");
			return null;
		}

		/* add the fromTemplate property to the type array */
		final Set<KiWiUriResource> types = new HashSet<KiWiUriResource>();
		if (template.getResource().isUriResource()) {
			types.add((KiWiUriResource) template.getResource());
		} else {
			log.error("FAILED IMPORT: For whatever reason, the ContentItem "
						+ templateURI + " does not have a UriResource");
			return null;
		}
		if (fromTemplate.getResource().isUriResource()) {
			types.add((KiWiUriResource) fromTemplate.getResource());
		} else {
			log.error("FAILED IMPORT: For whatever reason, the type "
						+ Constants.NS_KIWI_CORE
						+ "FromTemplate does not have a UriResource");
			return null;
		}
		
		/* create tags from types */
		Set<ContentItem> tags = new HashSet<ContentItem>();
		try {
			for (KiWiUriResource type :types) {
				if (!type.getLabel().endsWith("lass") &&
					!(type.getLabel().substring(0, 3).matches("[0-9]+") && type.getLabel().substring(type.getLabel().length()-3, type.getLabel().length()).matches("[0-9]+"))) {
						tags.add(contentItemService.getContentItemByTitle(type.getLabel()));
				}
			}
		} catch (Exception e) {
			log.error("FAILED IMPORT: Couldn't create Tags from Types due to the error: " + e);
			return null;
		} 

		/* import the RDF data into KiWi */
		importService = (ImportService) Component.getInstance("kiwi.core.importService");
		
		try {
			importService.importData(new ByteArrayInputStream(rdfdata.getBytes()), mimetype, types, null, getImportUser(), null);
		} catch (KiWiImportException e1) {
			log.error("FAILED IMPORT: Preconditions seem fine, import just failed. " + e1);
			e1.printStackTrace();
		}
		
		/* get the template's instance ContentItem and add the data */
		ContentItem templateInstance = contentItemService.getContentItemByUri(originURI);
		contentItemService.updateTitle(templateInstance, 
				originURI.substring(originURI.lastIndexOf("/")+1).toUpperCase());
		contentItemService.updateTextContentItem(templateInstance, template.getTextContent().getXmlDocument());

		// adding user
		templateInstance.setAuthor(getImportUser()); 
		
		// adding types and tags
		TaggingService taggingService = (TaggingService) Component.getInstance("taggingService");
		for (KiWiUriResource type :types) {
			if (!type.getLabel().endsWith("lass") // From "Class", being a helper type
					&& !(type.getLabel().equalsIgnoreCase("Template"))	// the created CI will not be a template
					&& !(type.getLabel().substring(0, 1).matches("[0-9]+") && type.getLabel().substring(type.getLabel().length()-1, type.getLabel().length()).matches("[0-9]+"))) {	// for invisible types like "46382be1273"
				templateInstance.addType(type);
				ContentItem taggingItem = contentItemService.createContentItem();
				taggingItem.setTitle(type.getLabel());
				taggingService.createTagging(type.getLabel(), templateInstance, taggingItem, getImportUser());
			}
		}

		TripleStore tripleStore = (TripleStore) Component.getInstance("tripleStore");
		tripleStore.createTriple(
						template.getResource(),
						tripleStore.createUriResource("http://www.kiwi-project.eu/kiwi/core/hasTemplateInstance"),
						templateInstance.getResource());
		tripleStore.createTriple(
						templateInstance.getResource(),
						tripleStore.createUriResource("http://www.kiwi-project.eu/kiwi/core/instancesTemplate"),
						template.getResource());

		log.info("Data Import succesfull: " + templateInstance.getKiwiIdentifier());
		return templateInstance.getKiwiIdentifier();
	}
}