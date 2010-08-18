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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import kiwi.api.extension.ExtensionService;
import kiwi.api.extension.KiWiApplication;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * provides together with dialog.xhtml and blank.xhtml the possibility of the use of lazy loaded dialog
 * boxes. The dialog content gets rendered only when the dialog is shown. The dialog boxes need to have
 * a unique name. 
 * 
 * To show a dialogbox simply use the JS function showKiwiDialogbox(name). 
 * To hide use hideKiwiDialogbox();
 * 
 * @author Szaby Gruenwald
 *
 */

@Name("popupManagerAction")
@Scope(ScopeType.CONVERSATION)
public class PopupManagerAction implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String elementId="modalWrapper";

	@Logger
	Log log;

	private DialogBox dialog;
	
	Map<String, DialogBox> dialogBoxes;
	private String dialogbox;
	
	@Create
	public void begin(){
		dialogBoxes=new HashMap<String, DialogBox>();
		log.info("Initialize action...");
		// collect the provided dialog boxes
		registerDialogbox("blank", "", "/blank.xhtml", 120,120);
		registerDialogbox("testPanel", "My Dialogbox", "/panel.xhtml", 300, 200);
		
		ExtensionService es = (ExtensionService) Component
			.getInstance("extensionService");
		Collection<KiWiApplication> apps = es.getApplications();
		
		for (KiWiApplication app:apps){
			app.initDialogBoxes(this);
		}
		setDialogbox("blank");
	}

	/**
	 * Let the Layout engine know about a dialogbox so it can be called.
	 * @param name (unique name for the dialog. 
	 * 		Convention is <application name>.dialogName e.g. "wiki.querywizardPanel"
	 * @param title The title shown in the header of the dialog
	 * @param path showing the content e.g. "/wiki/dialogs/qierywizard.xhtml"
	 * @param width Ideal width of the modal panel. In case the viewport itself is smaller, it's shown smaller.
	 * @param height Ideal height of the modal panel. In case the viewport itself is smaller, it's shown smaller.
	 */
	public void registerDialogbox(String name, String title, String path, int width, int height){
		dialogBoxes.put(name, new DialogBox(name, title, path, new Dimensions(width, height)));
	}
	
	public void setDialogbox(String name){
		log.info("setDialogbox: #0", name);
		dialogbox = name;
		dialog = dialogBoxes.get(name);
		if(dialog==null)
			log.error("The Dialogbox #0 is not defined.", name);
	}
	
	public class Dimensions{
		private int height, width;

		public Dimensions(int width, int height) {
			super();
			this.height = height;
			this.width = width;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}
		
	}

	public class DialogBox{
		private String title, path;
		private Dimensions dimensions;
		private String name;
		
		/**
		 * 
		 * @param name supposed to be like <appName>.<popupName> by convention. 
		 * @param title The dialogbox title
		 * @param path Absolute view path to the template to display inside the popup 
		 * @param dimensions a Dimensions object for ideal size. Gets smaller if the actual viewport is smaller.
		 */
		public DialogBox(String name, String title, String path,
				Dimensions dimensions) {
			super();
			this.name = name;
			this.title = title;
			this.path = path;
			this.dimensions = dimensions;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getPath() {
			return path;
		}
		public void setPath(String path) {
			this.path = path;
		}
		public Dimensions getDimensions() {
			log.debug("getDimensions");
			return dimensions;
		}
		public void setDimensions(Dimensions dimensions) {
			this.dimensions = dimensions;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
	}

	public DialogBox getDialog() {
		return dialog;
	}
	
	public void doSomething(){
		
	}

	public String getDialogbox() {
		return dialogbox;
	}

}
