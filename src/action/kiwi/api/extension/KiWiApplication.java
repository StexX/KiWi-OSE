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
 * Szaby Gruenwald
 * 
 */
package kiwi.api.extension;

import java.util.List;
import java.util.Set;

import kiwi.action.ui.PopupManagerAction;
import kiwi.api.config.ConfigurationService;
import kiwi.config.Configuration;
import kiwi.model.content.ContentItem;
import kiwi.transport.client.WidgetConfig;

import org.jboss.seam.Component;

/**
 * A KiWi application is an extension that uses the KiWi core services, but
 * otherwise mostly comes with its own set of services. KiWi applications register in the 
 * top navigation of the system. For example, the KiWi Wiki and TagIT are KiWi applications.
 * 
 * @author Sebastian Schaffert
 *
 */
public abstract class KiWiApplication implements KiWiExtension {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// cache visibility flag to avoid unnecessary configurationService lookups
	private Boolean visible;
	
	// cache order to avoid unnecessary configurationService lookups
	private Integer order;
	
	/**
	 * Return the name of the KiWi Application. Used as human readable identifier for the 
	 * application, e.g. in the navigation box.
	 * 
	 * @return
	 */
	public abstract String getName();
	
	
	/**
	 * Return a description of the KiWi Application. Used by the administration interface 
	 * and possibly elsewhere to give a more detailed description of the application.
	 */
	public abstract String getDescription();

	/**
	 * Returns the names of the user groups that are 
	 * allowed to use the particular extension
	 * @return names of user groups
	 */
	public abstract Set<String> getPermissibleRoles();
	
	/**
	 * Visibility of the application link in the UI header bar.
	 * @return
	 */
	public boolean isVisible() {
		if(visible == null) {
			ConfigurationService cs = (ConfigurationService) Component.getInstance("configurationService");
			String key = cs.getApplicationPrefix(this)+"visible";
			Configuration configuration = cs.getConfiguration(key, "1");
			visible = new Integer(1).equals(configuration.getIntValue());
		}
		return visible;
	}

	public void setVisible(boolean visible) {
		ConfigurationService cs = (ConfigurationService) Component.getInstance("configurationService");
		cs.setConfiguration(cs.getApplicationPrefix(this)+"visible",visible?"1":"0");
		this.visible = visible;
	}

	/**
	 * Depending on the found ContentItem and the context 
	 * (e.g. type, author, active users interest etc.)
	 * an application can define additional templates to show a search result.
	 * The default value is defined in the layoutAction component.
	 * @param ci
	 * @return a "defaultsearchresultItem.xhtml"-like path string showing to the template,
	 * or null in case the method "decides" not to touch the default.
	 */
	public String getSearchResultItemTemplatePath(ContentItem ci) {
		return null;
	}
	
	/**
	 * Depending on the ContentItem and the context 
	 * (e.g. type, author, active users interest etc.)
	 * an application can define additional templates to show a contentItem.
	 * The default value is defined in the layoutAction component.
	 * @param ci
	 * @return a "typetemplates/defaultcontentitem.xhtml"-like path string 
	 * showing to the template,
	 * or null in case the method "decides" not to touch the default.
	 */
	public String getContentItemTemplatePath(ContentItem ci) {
		return null;
	}
	
	/**
	 * Depending on the ContentItem and the context 
	 * (e.g. type, author, active users interest etc.)
	 * an application can define additional templates to edit a contentItem.
	 * The default value is defined in the layoutAction component.
	 * @param ci
	 * @return a "editortemplates/defaultEditor.xhtml"-like path string 
	 * showing to the template,
	 * or null in case the method "decides" not to touch the default.
	 */
	public String getEditTemplatePath(ContentItem ci) {
		return null;
	}
	
	public void setOrder(int order){
		ConfigurationService cs = (ConfigurationService) Component.getInstance("configurationService");
		cs.setConfiguration(cs.getApplicationPrefix(this)+"order",String.valueOf(order));
	}
	
	/**
	 * Gives back the persisted default appearance order of the application in the UI header.
	 * @return
	 */
	public int getOrder(){
		ConfigurationService cs = (ConfigurationService) Component.getInstance("configurationService");
		order = cs.getConfiguration(cs.getApplicationPrefix(this)+"order","1").getIntValue();
		return order;
	}
	
	/**
	 * Override this method if the extension provides a dialogbox.
	 * It gets called once on starting the application.
	 * 
	 */
	public void initDialogBoxes(PopupManagerAction pma){}


	/**
	 * Override this method if the extension provides a portal view.
	 * @return
	 */
	public List<WidgetConfig> getDefaultPortalConfig() {
		return null;
	}
	
	/**
	 * Override this methode to set a custom richfaces theme
	 */
	public String getTheme() {
		return "kiwiGreen";
	}
}
