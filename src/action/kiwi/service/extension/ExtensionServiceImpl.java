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
package kiwi.service.extension;


import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.ejb.PrePassivate;
import javax.ejb.Stateless;

import kiwi.api.config.ConfigurationService;
import kiwi.api.event.KiWiEvents;
import kiwi.api.extension.ExtensionServiceLocal;
import kiwi.api.extension.ExtensionServiceRemote;
import kiwi.api.extension.KiWiAction;
import kiwi.api.extension.KiWiApplication;
import kiwi.api.extension.KiWiService;
import kiwi.api.extension.KiWiWidget;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.jboss.seam.log.Log;

/**
 * @author Sebastian Schaffert
 *
 */
@Stateless
@Name("extensionService")
@Scope(ScopeType.STATELESS)
@AutoCreate
@Install(dependencies="configurationService")
public class ExtensionServiceImpl implements ExtensionServiceLocal, ExtensionServiceRemote {

	/** The Seam system logger for logging messages. */
	@Logger
	private Log log;
	
	
	/** The collection of applications. The key is the identifier of the application. */
	@In(value="extensionService.applications", scope=ScopeType.APPLICATION, required=false)
	@Out(value="extensionService.applications", scope=ScopeType.APPLICATION, required=false)
	private Map<String,KiWiApplication> applications;
	
	/** The collection of services. The key is the identifier of the service. */
	@In(value="extensionService.services", scope=ScopeType.APPLICATION, required=false)
	@Out(value="extensionService.services", scope=ScopeType.APPLICATION, required=false)
	private Map<String,KiWiService> services;
	
	/** The collection of actions. The key is the identifier of the action. */
	@In(value="extensionService.actions", scope=ScopeType.APPLICATION, required=false)
	@Out(value="extensionService.actions", scope=ScopeType.APPLICATION, required=false)
	private Map<String,KiWiAction> actions;
	
	/** The collection of widgets. The key is the identifier of the widget. */
	@In(value="extensionService.widgets", scope=ScopeType.APPLICATION, required=false)
	@Out(value="extensionService.widgets", scope=ScopeType.APPLICATION, required=false)
	private Map<String,KiWiWidget> widgets;
	
	@Observer("org.jboss.seam.postInitialization")
	public synchronized void initialise() {
		
		if(applications == null) {
			log.info("KiWi Extension Service starting up ...");

			applications = new HashMap<String,KiWiApplication>();
			services     = new HashMap<String, KiWiService>();
			actions      = new HashMap<String, KiWiAction>();
			widgets      = new HashMap<String, KiWiWidget>();
			
			Events.instance().raiseAsynchronousEvent(KiWiEvents.EXTENSIONSERVICE_INIT);			
		}

	}
	
	@PrePassivate
	public void shutdown() {
		log.info("KiWi Extension Service shutting down ...");
	}
	
	//@Destroy
	public void destroy() {
		
	}
	
	/**
	 * Register a KiWi action with the core system.
	 * 
	 * @param act an instance of KiWiAction serving as descriptor of the action to be registered
	 */
	@Override
	public void registerAction(KiWiAction act) {
		initialise();
		
		log.info("registering action #0",act.getIdentifier());
		
		actions.put(act.getIdentifier(),act);
	}

	/**
	 * Register a KiWi application with the core system.
	 * 
	 * @param app an instance of KiWiApplication serving as descriptor of the application to be
	 *            registered.
	 */
	@Override
	public void registerApplication(KiWiApplication app) {
		initialise();
		
		log.info("registering application '#0' (#1)",app.getName(), app.getIdentifier());

		// sets the default order
		app.getOrder();
		
		applications.put(app.getIdentifier(), app);
	}

	/**
	 * Register a KiWi service with the core system.
	 * 
	 * @param srv an instance of KiWiService serving as descriptor of the service to be registered.
	 */
	@Override
	public void registerService(KiWiService srv) {
		initialise();
		
		log.info("registering service #0",srv.getIdentifier());
		
		services.put(srv.getIdentifier(),srv);
	}

	/**
	 * Register a KiWi widget with the core system.
	 * 
	 * @param wdt an instance of KiWiWidget serving as descriptor of the widget to be registered.
	 */
	@Override
	public void registerWidget(KiWiWidget wdt) {
		initialise();
		
		log.info("registering widget #0",wdt.getIdentifier());

		widgets.put(wdt.getIdentifier(), wdt);
	}


	/**
	 * Return a collection of all actions. May use the context to select only the relevant actions
	 * based on user permissions and current content item.
	 * 
	 * @return a collection of KiWiAction descriptors
	 */
	@Override
	public Collection<KiWiAction> getActions() {
		return actions.values();
	}


	/**
	 * Return a collection of all applications (e.g. for displaying in top navigation bar).
	 * May use the context to restrict the list based on user permissions.
	 * 
	 * @return a collection of KiWiApplication descriptors
	 */
	@Override
	public Collection<KiWiApplication> getApplications() {
		LinkedList<KiWiApplication> myApplications = new LinkedList<KiWiApplication>();
		myApplications.addAll(applications.values());
		
		Collections.sort(myApplications, new Comparator<KiWiApplication>() {

			@Override
			public int compare(KiWiApplication o1, KiWiApplication o2) {
				ConfigurationService cs = (ConfigurationService) Component.getInstance("configurationService");
				int i1 = o1.getOrder();
				int i2 = o2.getOrder();
				return i1-i2;
			}
			
		});
		
		log.debug("returning #0 applications", myApplications.size());
		
		return myApplications;
	}


	/**
	 * Return a collection of all services. May use the context to restrict the list based on
	 * user permissions.
	 * 
	 * @return a collection of KiWiService descriptors
	 */
	@Override
	public Collection<KiWiService> getServices() {
		return services.values();
	}


	/**
	 * Return a collection of all widgets. May use the context to select only the relevant widgets
	 * based on user permissions and current content item.
	 * 
	 * @return a collection of KiWiWidget descriptors
	 */
	@Override
	public Collection<KiWiWidget> getWidgets() {
		return widgets.values();
	}

	
	
	/**
	 * Return the action with the given identifier.
	 * 
	 * @return the KiWiAction with the given identifier, or null if no such action exists
	 */
	@Override
	public KiWiAction getAction(String identifier) {
		return actions.get(identifier);
	}

	/**
	 * Return the application with the given identifier.
	 * 
	 * @return the KiWiApplication with the given identifier, or null if no such application exists
	 */
	@Override
	public KiWiApplication getApplication(String identifier) {
		KiWiApplication app = applications.get(identifier);
		if(app == null) {
			log.error("application #0 was not found; applications=#1",identifier,applications);
		}
		return app;
	}

	/**
	 * Return the service with the given identifier.
	 * 
	 * @return the KiWiService with the given identifier, or null if no such service exists
	 */
	@Override
	public KiWiService getService(String identifier) {
		return services.get(identifier);
	}

	/**
	 * Return the widget with the given identifier.
	 * 
	 * @return the KiWiWidget with the given identifier, or null if no such widget exists
	 */
	@Override
	public KiWiWidget getWidget(String identifier) {
		return widgets.get(identifier);
	}

	
	
	
}
