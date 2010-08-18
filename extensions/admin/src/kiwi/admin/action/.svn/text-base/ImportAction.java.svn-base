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
package kiwi.admin.action;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.importexport.ImportService;
import kiwi.api.ontology.OntologyService;
import kiwi.api.ontology.TemplateService;
import kiwi.api.tagging.TaggingService;
import kiwi.api.transaction.TransactionService;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.importexport.ImportTask;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.User;
import kiwi.service.importexport.KiWiImportException;
import kiwi.service.transaction.KiWiSynchronizationImpl;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.transaction.Transaction;

/**
 * @author Sebastian Schaffert
 * 
 */
@Name("kiwi.admin.importAction")
public class ImportAction {

	@Logger
	private Log log;

	@In("kiwi.core.importService")
	private ImportService importService;

	@In
	private ContentItemService contentItemService;

	@In
	private TripleStore tripleStore;

	@In
	private EntityManager entityManager;

	@In
	private KiWiEntityManager kiwiEntityManager;
	
	@In
	private TransactionService transactionService;

	// for tag autocompletion
	@In
	private TaggingService taggingService;

	// for class autocompletion
	@In
	private OntologyService ontologyService;

	@In
	private FacesMessages facesMessages;

	@In(create = true)
	private User currentUser;

	private String selectedMimeType;

	private String selectedUrl;

	public Set<String> mimeTypes;

	// tags added to the imported content items, separated by comma
	private String tagLabels;

	// types added to the imported content items, separated by comma
	private String typeLabels;

	// whether to apply templates on imported content items.
	private boolean templates;

	// the interval for scheduled tasks in minutes
	private int interval;

	// description for scheduled tasks
	private String description;

	private List<ImportTask> tasks;

	public void manualImport() {
		try {
			URL url = new URL(selectedUrl);

			Set<ContentItem> tags = new HashSet<ContentItem>();
			Set<KiWiUriResource> types = new HashSet<KiWiUriResource>();

			String[] components = tagLabels.split(",");

			for (String component : components) {

				log.info("adding tag #0", component);

				String label = component.trim();

				if (!"".equals(label)) {
					// TODO: query by uri instead of by title
					ContentItem taggingItem = contentItemService
							.getContentItemByTitle(label);

					if (taggingItem == null) {
						// create new Content Item of type "tag" if the tag does
						// not yet exist
						taggingItem = contentItemService
								.createContentItem("content/"
										+ label.toLowerCase().replace(" ", "_")
										+ "/" + UUID.randomUUID().toString());
						taggingItem.addType(tripleStore
								.createUriResource(Constants.NS_KIWI_CORE
										+ "Tag"));
						contentItemService.updateTitle(taggingItem, label);
						kiwiEntityManager.persist(taggingItem);
						log
								.info("created new content item for non-existant tag");
					}
					tags.add(taggingItem);
				}

			}

			components = typeLabels.split(",");
			for (String component : components) {

				log.info("adding type #0", component);

				String label = component.trim();

				if (!"".equals(label)) {
					// TODO: query by uri instead of by title
					ContentItem taggingItem = contentItemService
							.getContentItemByTitle(label);

					if (taggingItem == null) {
						// create new Content Item of type "tag" if the tag does
						// not yet exist
						taggingItem = contentItemService
								.createContentItem("content/"
										+ label.toLowerCase().replace(" ", "_")
										+ "/" + UUID.randomUUID().toString());
						taggingItem.addType(tripleStore
								.createUriResource(Constants.NS_OWL + "Class"));
						contentItemService.updateTitle(taggingItem, label);
						kiwiEntityManager.persist(taggingItem);
						log
								.info("created new content item for non-existant type");
					}
					types.add((KiWiUriResource) taggingItem.getResource());
				}
			}

			int count = 0;
			if (templates) {
				TemplateService templateService = (TemplateService) Component
						.getInstance("templateService");
				Collection<ContentItem> output = new HashSet<ContentItem>();
				try {
					count = importService.importData(url, selectedMimeType,
							types, tags, currentUser, output);
				} catch (KiWiImportException e1) {
					facesMessages.add("Import error: #0 (url: #1)", e1
							.getMessage(), url);
				}

				try {
					// We need to commit the data, so we can query them in
					// template service
					Transaction.instance().commit();
					Transaction.instance().begin();
					transactionService.registerSynchronization
						(KiWiSynchronizationImpl.getInstance(), transactionService.getUserTransaction() );

					for (ContentItem ci : output) {
						templateService.applyTemplate(ci);
					}
				} catch (Exception e) {
					log.info("exception", e);
				}
			} else {
				try {
					count = importService.importData(url, selectedMimeType,
							types, tags, currentUser, null);
				} catch (KiWiImportException e) {
					facesMessages.add("Import error: #0 (url: #1)", e
							.getMessage(), url);
				}
			}
			facesMessages.add("#0 content items have been imported", count);

		} catch (MalformedURLException ex) {
			log.error("malformed URL: #0", selectedUrl);
			facesMessages.add(
					"the entered URL #0 was malformed; no data imported",
					selectedUrl);
		}
	}

	public Set<String> getMimeTypes() {
		if (mimeTypes == null) {
			mimeTypes = importService.getAcceptTypes();
		}
		return mimeTypes;
	}

	public String getSelectedMimeType() {
		return selectedMimeType;
	}

	public void setSelectedMimeType(String selectedMimeType) {
		this.selectedMimeType = selectedMimeType;
	}

	public String getSelectedUrl() {
		return selectedUrl;
	}

	public void setSelectedUrl(String selectedUrl) {
		this.selectedUrl = selectedUrl;
	}

	public String getTagLabels() {
		return tagLabels;
	}

	public void setTagLabels(String tagLabels) {
		this.tagLabels = tagLabels;
	}

	public String getTypeLabels() {
		return typeLabels;
	}

	public void setTypeLabels(String typeLabels) {
		this.typeLabels = typeLabels;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean getTemplates() {
		return templates;
	}

	public void setTemplates(boolean templates) {
		this.templates = templates;
	}

	public List<ImportTask> getTasks() {
		if (tasks == null) {
			Query q = entityManager.createNamedQuery("importTask.listAllTasks");
			tasks = q.getResultList();
		}
		return tasks;
	}

	public void setTasks(List<ImportTask> tasks) {
		this.tasks = tasks;
	}

	public void addTask() {
		if (selectedMimeType == null) {
			facesMessages.add("please select a import format");
			return;
		}
		try {
			URL url = new URL(selectedUrl);

			Set<ContentItem> tags = new HashSet<ContentItem>();
			Set<KiWiUriResource> types = new HashSet<KiWiUriResource>();

			String[] components = tagLabels.split(",");

			for (String component : components) {

				log.info("adding tag #0", component);

				String label = component.trim();

				if (!"".equals(label)) {
					// TODO: query by uri instead of by title
					ContentItem taggingItem = contentItemService
							.getContentItemByTitle(label);

					if (taggingItem == null) {
						// create new Content Item of type "tag" if the tag does
						// not yet exist
						taggingItem = contentItemService
								.createContentItem("content/"
										+ label.toLowerCase().replace(" ", "_")
										+ "/" + UUID.randomUUID().toString());
						taggingItem.addType(tripleStore
								.createUriResource(Constants.NS_KIWI_CORE
										+ "Tag"));
						contentItemService.updateTitle(taggingItem, label);
						kiwiEntityManager.persist(taggingItem);
						log
								.info("created new content item for non-existant tag");
					}
					tags.add(taggingItem);
				}

			}

			components = typeLabels.split(",");
			for (String component : components) {

				log.info("adding type #0", component);

				String label = component.trim();

				if (!"".equals(label)) {

					// TODO: query by uri instead of by title
					ContentItem taggingItem = contentItemService
							.getContentItemByTitle(label);

					if (taggingItem == null) {
						// create new Content Item of type "tag" if the tag does
						// not yet exist
						taggingItem = contentItemService
								.createContentItem("content/"
										+ label.toLowerCase().replace(" ", "_")
										+ "/" + UUID.randomUUID().toString());
						taggingItem.addType(tripleStore
								.createUriResource(Constants.NS_OWL + "Class"));
						contentItemService.updateTitle(taggingItem, label);
						kiwiEntityManager.persist(taggingItem);
						log
								.info("created new content item for non-existant type");
					}
					types.add((KiWiUriResource) taggingItem.getResource());
				}

			}
			importService.scheduleImport(description, new Date(
					interval * 60 * 1000), url, selectedMimeType, types, tags,
					currentUser);

//			entityManager.flush();
			tasks = null;
		} catch (MalformedURLException ex) {
			log.error("malformed URL: #0", selectedUrl);
			facesMessages.add(
					"the entered URL #0 was malformed; no data imported",
					selectedUrl);
		}
	}

	public void removeTask(ImportTask task) {
		if (entityManager.contains(task)) {
			entityManager.remove(task);
		} else {
			task = entityManager.find(ImportTask.class, task.getId());
			entityManager.remove(task);
		}
	}

	public void runAllTasks() {
		log.info("running all import tasks...");
		List<ImportTask> mytasks = getTasks();
		int count = 0;
		for (ImportTask t : mytasks) {
			try {
				log.info("importing task no #0: #1", t.getId(), t
						.getDescription());
				try {
					count += importService.importData(new URL(t.getUrl()), t
							.getFormat(), t.getTypes(), t.getTags(), t
							.getOwner(), null);
				} catch (KiWiImportException e) {
					facesMessages.add("Import error at task #0: #1", t
							.getDescription(), e.getMessage());
				}
			} catch (MalformedURLException ex) {
				log.error("malformed URL: #0", t.getUrl());
				facesMessages.add(
						"the entered URL #0 was malformed; no data imported", t
								.getUrl());
			}
		}
		facesMessages.add("all blogs have been imported; #0 items overall",
				count);
	}

	public <C> List<C> setToList(Set<C> set) {
		return new LinkedList<C>(set);
	}

	public List<String> autocomplete(Object param) {
		return taggingService.autocomplete(param.toString().toLowerCase());
	}

	public List<String> autocompleteClasses(Object param) {
		return ontologyService.autocomplete(param.toString().toLowerCase());

	}

}
