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

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import kiwi.api.config.ConfigurationService;
import kiwi.api.extension.ExtensionService;
import kiwi.api.extension.KiWiApplication;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Conversation;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

/**
 * @author Sebastian Schaffert
 *
 */
@Name("kiwi.admin.applicationConfigurationAction")
@Scope(ScopeType.CONVERSATION)
public class ApplicationConfigurationAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger
	private Log log;
	
	@In
	private ConfigurationService configurationService;
	
	@In
	private ExtensionService extensionService;
	
	@In
	private FacesMessages facesMessages;
	
	private List<KiWiApplication> applications;
	
	public void start() {
		if(!Conversation.instance().isNested()) {
			Conversation.instance().beginNested();
		}
	}

	@End
	public String commit() {
		try {
			for(int i = 0; i<applications.size(); i++) {
				KiWiApplication app = applications.get(i);
				app.setOrder(i);
			}
			
			facesMessages.add("Reordering of applications stored in configuration");
			
			return "success";
		} catch(Exception ex) {
			log.error("error while storing new application order",ex);
			facesMessages.add("Error while storing new application order: #0",ex.getMessage());
			return null;
		}
	}

	
	
	/**
	 * Create "applications" as a list internal to the application configuration.
	 * the order of the list is then used to set the order of applications on
	 * commit
	 */
	public List<KiWiApplication> getApplications() {
		if(applications == null) {
			applications = new LinkedList<KiWiApplication>();
			for(KiWiApplication app : extensionService.getApplications()) {
				applications.add(app);
			}
		}
		return applications;
	}
	
	public void setApplications(List<KiWiApplication> applications) {
		this.applications = applications;
	}
		
}
