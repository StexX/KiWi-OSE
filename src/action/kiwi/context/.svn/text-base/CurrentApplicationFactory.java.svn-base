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
package kiwi.context;

import kiwi.api.config.ConfigurationService;
import kiwi.api.extension.ExtensionService;
import kiwi.api.extension.KiWiApplication;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.theme.ThemeSelector;

/**
 * @author Sebastian Schaffert
 *
 */
@Name("currentApplicationFactory")
@Scope(ScopeType.STATELESS)
@AutoCreate
public class CurrentApplicationFactory {

	/** The serial number of this component. */
	private static final long serialVersionUID = 3407152404934610271L;

	
	/**
	 * Inject a Seam logger for logging purposes inside this component.
	 */
	@Logger
	private static Log log;

	/**
	 * Faces Messages to display status results.
	 */
	@In
	FacesMessages facesMessages;


	/**
	 * The currently active application for the user's conversation.
	 */
	@In(required = false)
	@Out(scope = ScopeType.SESSION, required = false)
	private KiWiApplication currentApplication;
	
	@In(create=true)
	private ThemeSelector themeSelector;
	
	@In
	private ExtensionService extensionService;
	
	@In
	private ConfigurationService configurationService;
	
	
	@Factory("currentApplication")
	public void createApplication() {
		if(currentApplication == null) {
			// if no application is selected, select the wiki application by default
			currentApplication = extensionService.getApplication(
					configurationService.getConfiguration("kiwi.app.default", "wiki").getStringValue()
			);
			log.debug("setting currentApplication to default '#0'", currentApplication.getName());
		}
	}
		
	public void switchApplication(KiWiApplication app) {
		currentApplication = app;
		log.debug("switching currentApplication to '#0'", currentApplication.getName());
		themeSelector.setTheme(currentApplication.getTheme());
	}
	
	public void switchApplication(String identifier) {
		currentApplication = extensionService.getApplication(identifier);

		if(currentApplication == null) {
			currentApplication = extensionService.getApplication("wiki");
		}
		
		log.debug("switching currentApplication to '#0'", currentApplication.getName());
		themeSelector.setTheme(currentApplication.getTheme());
	}

}
