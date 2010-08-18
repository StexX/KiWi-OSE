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
package kiwi.api.extension;

import java.util.Collection;

/**
 * The KiWi Extension Service manages the registration of services upon startup. KiWi currently
 * supports the following four kinds of extensions:
 * <ul>
 * 	<li>applications; a KiWi application is an extension that uses the KiWi core services, but
 *      otherwise mostly comes with its own set of services. KiWi applications register in the 
 *      top navigation of the system. For example, the KiWi Wiki and TagIT are KiWi applications
 *  </li>
 *  <li>services; a KiWi service is essentially a "service plugin" that offers an additional
 *      service as an extension, i.e. bundled in a separate JAR file.
 *  </li>
 *  <li>widgets; a KiWi widget is a small user interface component that offers a certain 
 *      information and/or functionality to the user. Widgets can be placed on the user's
 *      Dashboard and in the Wiki view.
 *  </li>
 *  <li>actions; a KiWi action is a certain kind of functionality that can be "plugged into" the
 *      system for performing certain actions on content like "geolocate", "set time", etc.
 *  </li>
 *  The extension service offers four methods for registering the different kinds of extensions.
 *  These methods are called by the initialisation functions of the respective extensions, i.e.
 *  the methods that observe org.jboss.seam.postInitialization.
 * </ul>
 * 
 * @author Sebastian Schaffert
 *
 */
public interface ExtensionService {

	/**
	 * Register a KiWi application with the core system.
	 * 
	 * @param app an instance of KiWiApplication serving as descriptor of the application to be
	 *            registered.
	 */
	public void registerApplication(KiWiApplication app);
	
	/**
	 * Register a KiWi service with the core system.
	 * 
	 * @param srv an instance of KiWiService serving as descriptor of the service to be registered.
	 */
	public void registerService(KiWiService srv);
	
	/**
	 * Register a KiWi widget with the core system.
	 * 
	 * @param wdt an instance of KiWiWidget serving as descriptor of the widget to be registered.
	 */
	public void registerWidget(KiWiWidget wdt);
	
	/**
	 * Register a KiWi action with the core system.
	 * 
	 * @param act an instance of KiWiAction serving as descriptor of the action to be registered
	 */
	public void registerAction(KiWiAction act);
	
	
	/**
	 * Return the application with the given identifier.
	 * 
	 * @return the KiWiApplication with the given identifier, or null if no such application exists
	 */
	public KiWiApplication getApplication(String identifier);
	
	/**
	 * Return a collection of all applications (e.g. for displaying in top navigation bar).
	 * May use the context to restrict the list based on user permissions.
	 * 
	 * @return a collection of KiWiApplication descriptors
	 */
	public Collection<KiWiApplication> getApplications();
	

	/**
	 * Return the service with the given identifier.
	 * 
	 * @return the KiWiService with the given identifier, or null if no such service exists
	 */
	public KiWiService getService(String identifier);
	
	/**
	 * Return a collection of all services. May use the context to restrict the list based on
	 * user permissions.
	 * 
	 * @return a collection of KiWiService descriptors
	 */
	public Collection<KiWiService> getServices();
	

	/**
	 * Return the action with the given identifier.
	 * 
	 * @return the KiWiAction with the given identifier, or null if no such action exists
	 */
	public KiWiAction getAction(String identifier);
	
	/**
	 * Return a collection of all actions. May use the context to select only the relevant actions
	 * based on user permissions and current content item.
	 * 
	 * @return a collection of KiWiAction descriptors
	 */
	public Collection<KiWiAction> getActions();
	
	/**
	 * Return the widget with the given identifier.
	 * 
	 * @return the KiWiWidget with the given identifier, or null if no such widget exists
	 */
	public KiWiWidget getWidget(String identifier);
	
	/**
	 * Return a collection of all widgets. May use the context to select only the relevant widgets
	 * based on user permissions and current content item.
	 * 
	 * @return a collection of KiWiWidget descriptors
	 */
	public Collection<KiWiWidget> getWidgets();
	
	
	public void initialise();
	
	public void shutdown();
	
	public void destroy();
}
