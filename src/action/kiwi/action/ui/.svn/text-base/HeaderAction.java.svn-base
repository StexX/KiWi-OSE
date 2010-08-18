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
package kiwi.action.ui;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import kiwi.api.extension.ExtensionService;
import kiwi.api.extension.KiWiApplication;
import kiwi.api.user.UserService;
import kiwi.context.CurrentApplicationFactory;
import kiwi.mobile.MobileConfiguration;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;


/**
 * This action is a backing bean for the KiWi core user interface
 * that provides support for the header box that is displayed in
 * all KiWi applications.
 * <p>
 * It currently provides the following functionalities:
 * <ul>
 * <li>list all applications</li>
 * </ul>
 * 
 * @author Sebastian Schaffert
 */
@Name("kiwi.ui.headerAction")
@Scope(ScopeType.SESSION)
public class HeaderAction implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger
    private Log log;

    @In
    private ExtensionService extensionService;

    @In
    private CurrentApplicationFactory currentApplicationFactory;

    @In(create = true)
    private KiWiApplication currentApplication;
    
    @In
    private MobileConfiguration mobileConfiguration;

    private List<KiWiApplication> applications;

    @In(create=true)
    private User currentUser;
    
    @In
    private UserService userService;
    
    public List<KiWiApplication> getApplications() {
        if (applications == null) {
            applications = new LinkedList<KiWiApplication>();
            applications.addAll(extensionService.getApplications());
        }
        return applications;
    }
    
    public boolean isVisible(KiWiApplication app) {
    	if(app != null && app.isVisible() && app.getPermissibleRoles() == null) {
    		return true;
    	} else if(app != null && app.isVisible() && app.getPermissibleRoles() != null) {
    		for(String s : app.getPermissibleRoles()) {
    			if(userService.hasRole(currentUser,s))
    				return true;
    		}
    		return false;
    	} else {
    		return false;
    	}
    }

    public String showActiveClass(KiWiApplication app) {
        if (app.equals(currentApplication)) {
            return " kiwiAppActive";
        } else {
            return "";
        }
    }

    public String switchApplication(KiWiApplication app) {
        final String appIdentifier = app.getIdentifier();
        log.info("switching to application #0", app.getIdentifier());
        currentApplicationFactory.switchApplication(app);

// the old way to build a application uri
// return "/" + app.getIdentifier() + "/home.xhtml";
        final boolean isMobileDevice = mobileConfiguration.isMobileDevice();
        final String result =
                buildApplicationURI(appIdentifier, isMobileDevice);
        return result;
    }

    /**
     * Builds a application home uri for the given parameters.
     * 
     * @param appIdentifier the application identifier.
     * @param isMobile true if the request comes from a mobile
     *            device.
     * @return a application home uri for the given parameters.
     */
    private String buildApplicationURI(String appIdentifier, boolean isMobile) {
        final StringBuffer result = new StringBuffer();
        result.append("/");
        result.append(appIdentifier);
        result.append(isMobile ? "/home.mobile.xhtml" : "/home.xhtml");

        return result.toString();
    }

    @Observer(value = "configurationChanged", create = false)
    public void clear() {
        applications = null;
    }
}
