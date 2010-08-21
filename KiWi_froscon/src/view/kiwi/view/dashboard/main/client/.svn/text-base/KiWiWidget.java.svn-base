/*
 * File : KiWiWidget.java
 * Date : Mar 30, 2010
 * 
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2008 The KiWi Project. All rights reserved.
 * http://www.kiwi-project.eu
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  KiWi designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 *
 * Contributor(s): Szaby Grünwald
 */
package kiwi.view.dashboard.main.client;

import kiwi.transport.client.Properties;
import kiwi.transport.client.WidgetConfig;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * A KiWiWidget is a configurable piece of a UI for a specific functionality, 
 * such as showing and editing a part of the users profile.
 * @author Szaby Grünwald
 * @version 0.7
 * @since 0.7
 */
public abstract class KiWiWidget extends VLayout {

	private WidgetConfig widgetConfig;
	private Gadget containerGadget;
	private boolean isConfigurable = false;
	protected Properties properties;
	
	public KiWiWidget() {
        setWidth100();
        setHeight100();
	}
	
	/**
	 * Get the Canvas element for addMember(Canvas) in the widget container body.
	 * @return
	 */
	public abstract Canvas getWidgetPanel();

	/**
	 * @return the widgetConfig
	 */
	public WidgetConfig getWidgetConfig() {
		if(widgetConfig==null){
			widgetConfig=new WidgetConfig();
			widgetConfig.setWidgetId("n/a");
			widgetConfig.setProperties("");
		}
		return widgetConfig;
	}

	/**
	 * @param widgetConfig the widgetConfig to set
	 */
	public void setWidgetConfig(WidgetConfig widgetConfig) {
		this.widgetConfig = widgetConfig;
		Properties properties = new Properties();
		properties.load(widgetConfig.getProperties());
		setProperties(properties);
	}

	private void setProperties(Properties properties) {
		this.properties = properties;
	}

	public void setContainerGadget(Gadget gadget) {
		this.containerGadget = gadget;
	}

	/**
	 * @return the containerGadget
	 */
	public Gadget getContainerGadget() {
		return containerGadget;
	}

	public boolean isConfigurable() {
		return isConfigurable;
	}

	/**
	 * Decides if the configuration header button is shown. Default is false.
	 * @param isConfigurable the isConfigurable to set
	 */
	public void setConfigurable(boolean isConfigurable) {
		this.isConfigurable = isConfigurable;
	}

	/**
	 * strictly for debugging, it calls the good old-fashioned alert box
	 * and stops execution. 
	 * The problem with the alternatives SC.warn(), SC.say() is that
	 * those overwrite each other so in case multiple of them are called,
	 * one can only see the last message.
	 * @param message
	 */
	public static native void log(Object message) /*-{
		alert(message);
	}-*/;

	public Layout getConfigPanel() {
		Layout configPanel = new VLayout(); 
		configPanel.setWidth100();
		
		Label label = new Label();
		label.setAutoFit(true);
		label.setWidth100();
		label.setContents("The setup widget not yet implemented. In order to implement it overwrite the "+
				"methos public Canvas getConfigPanel() in your KiWiWidget!");
		configPanel.addMember(label);
		
		Button close = new Button("Close");
		close.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				hideConfig();
			}

		});
		configPanel.addMember(close);
		return configPanel;
	}

	protected void hideConfig() {
		containerGadget.hideConfig();
	}
	
	protected void saveProperties(Properties properties, AsyncCallback<Void> callback) {
		widgetConfig.setProperties(properties.serialize());
		containerGadget.saveWidgetConfig(widgetConfig, callback);
	}

    public static native String getJSStringVariable(String name) /*-{
        var variable = eval("$wnd."+name);
        if(variable == null){
            variable = "";
        }
        return variable;
    }-*/;



}
