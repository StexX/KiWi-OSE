/*
 * File : Dashboard.java
 * Date : Mar 26, 2010
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
 * Contributor(s): Szaby Grünwald, Thomas Kurz
 */
package kiwi.view.dashboard.main.client;

import java.util.LinkedList;
import java.util.List;

import kiwi.transport.client.KiWiSerializableException;
import kiwi.transport.client.Properties;
import kiwi.transport.client.WidgetConfig;
import kiwi.view.dashboard.dummy.client.Dummy;
import kiwi.view.dashboard.main.client.GadgetPortal.GadgetColumn;
import kiwi.view.dashboard.profile.client.Profile;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.util.EventHandler;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.DropEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * @author Szaby Grünwald
 * @version 0.7
 * @since 0.7
 *
 */
public class Dashboard implements EntryPoint {

	private GadgetPortal portal;
	private String applicationId = "dashboard";
    private String rootElement= "";

    public void onModuleLoad() {
        rootElement = getJSStringVariable("dashboardRoot");
        if(rootElement != ""){
            showDashboard();
        }
    }

    private void showDashboard(){
		final Layout layout = getDashboardLayout();
		layout.setHeight100();
		layout.setWidth100();
		final Dashboard dashboard = this;
		getContent(new AsyncCallback<List<WidgetConfig>>() {
			
			@Override
			public void onSuccess(List<WidgetConfig> widgetConfigList) {
//				KiWiWidget.log("Nr. of elements is " + widgetConfigList.size());

				// create GadgetPortal with 3 columns 
				portal = new GadgetPortal(3);
				portal.setDashboard(dashboard);
				for(WidgetConfig widgetConfig: widgetConfigList){
					// create widget and add to the layout
//					KiWiWidget.log("WidgetId: " + widgetConfig.getWidgetId());
					Gadget gadget = instantiateGadget(widgetConfig); 
					
					portal.addGadget(widgetConfig.getColumn(), gadget);
					
					portal.setHeight100();
					portal.setWidth100();
				}
				
				final DynamicForm form = new DynamicForm();
				form.setAutoWidth();
				form.setNumCols(1);

				// button to add gadgets
				final ButtonItem removePortalConfigurationButton = new ButtonItem("Remove Configuration");
				
				removePortalConfigurationButton.setAutoFit(true);
				removePortalConfigurationButton.setStartRow(true);
				removePortalConfigurationButton.setEndRow(true);
				removePortalConfigurationButton
						.addClickHandler(new ClickHandler() {
							
							@Override
							public void onClick(ClickEvent event) {
								progress(true);
								getDashboardAction().resetPortalConfiguration(applicationId, new AsyncCallback<Void>() {
									
									@Override
									public void onSuccess(Void result) {
										SC.say("Config reset for " + applicationId + " successful.");
										progress(false);
									}
									
									@Override
									public void onFailure(Throwable caught) {
										SC.warn("Config reset for " + applicationId + " couldn't be finished: " +
												caught.getMessage());
										progress(false);
									}
								});
							}
						});

				form.setItems(removePortalConfigurationButton);

				layout.addMember(form);
				layout.addMember(portal);
				RootPanel.get(rootElement).add(layout);
				progress(false);
//				KiWiWidget.log(RootPanel.get("dashboardCanvas").toString());
			}
			
			private Gadget instantiateGadget(WidgetConfig widgetConfig) {
				if(widgetConfig == null) {
					SC.say("widgetConfig is null!");
					return null;
				} else {
					Properties props = new Properties();
					props.load(widgetConfig.getProperties());
					KiWiWidget widget = instantiateWidget(widgetConfig.getWidgetId());
					if(widget == null){
						SC.say(widgetConfig.getWidgetId() + "couldn't be initialised for the dashboard.");
						return null;
					}
					widget.setWidgetConfig(widgetConfig);
//					Gadget res = new Gadget(widget);
					Gadget res = new Gadget(widget);
//					res.setKiWiWidget(widget);
					return res;
				}
			}

			private KiWiWidget instantiateWidget(String clazz) {
				// TODO make it generic with a factory
//				widgetFactory.getInstance("gwt/dashboardprofile/dashboardprofile");
				// TODO Implement specific gadget instantiation
				
				if("profile".equals(clazz)){
					return new Profile();
//				} else if ("profilephoto".equals(clazz)) {
//					return new ProfilePhoto();
				} else {
					return new Dummy();
				}
				
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
//		layout.addMember(getFooter());
	}
	
	private Layout getDashboardLayout() {
		VLayout layout = new VLayout(); 
		layout.setWidth100();
		layout.setHeight100();
		layout.setMembersMargin(5);
		layout.setLayoutMargin(20);
		return layout;
	}
	
	private void getContent(AsyncCallback<List<WidgetConfig>> callback) {
    	constructPortal(applicationId , callback);
	}
	
	private void constructPortal(String applicationId, AsyncCallback<List<WidgetConfig>> callback) {
//		KiWiWidget.log("remotely call getPortalConfiguration()");
		progress(true);
		getDashboardAction().getPortalConfiguration(applicationId, callback);
	}

	/**
	 * Instantiate the ProfileGWTAction backend service
	 * @return
	 */
	private DashboardGWTActionAsync getDashboardAction() {
		String endpointURL = GWT.getModuleBaseURL() + "../../seam/resource/gwt";
	
		DashboardGWTActionAsync svc = GWT.create(DashboardGWTAction.class);
		((ServiceDefTarget) svc).setServiceEntryPoint(endpointURL);
		return svc;
	}

	/**
	 * Store the new widgetConfiguration
	 * @param event
 */
	public void dropHappened(DropEvent event) {
		updatePositions();
		
		GadgetColumn newColumn = (GadgetColumn) event.getSource();
		int newCol = portal.getColumnNr(newColumn);
		int newRow = newColumn.getDropPosition();
		Gadget draggedGadget = (Gadget)EventHandler.getDragTarget();
		List<WidgetConfig> newConfigList = handleMove(event, draggedGadget, newCol, newRow);
		progress(true);
		getDashboardAction().setPortalConfiguration(applicationId, newConfigList, new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				progress(false);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				SC.say("Something wrong happened, so the changes couldn't be saved. (" + 
						caught.getMessage() + 
						") At this point we should move back the widget, but this isn't implemented yet."); 
				progress(false);
			}
		});
		
	}

	private void progress(boolean inProgress){
		progress(inProgress, "");
	}
	
	private void progress(boolean inProgress, String message) {
		// TODO Show a popup or animated status icon somewhere
//		if(!inProgress)
//			SC.say("finished");
	}
	
	/**
	 * Go through the DOM columns, through the Widgets and Widgetconfigurations and update 
	 * each widgetConfig-s position fields according to the DOM. 
	 */
	private void updatePositions(){
		int colNr=-1;
		for(GadgetColumn col:this.getPortal().getColumns()){
			colNr++;
			int rowNr=-1;
			for(Canvas canv:col.getMembers()){
				Gadget gadget = (Gadget)canv;
				rowNr++;
				KiWiWidget widget = gadget.getKiWiWidget();
				if(widget == null)KiWiWidget.log("Gadgets have to be initialized with a KiWiWidget inside!");
				WidgetConfig conf = widget.getWidgetConfig();
				conf.setColumn(colNr);
				conf.setRow(rowNr);
			}
		}
	}
	
	/**
	 * Change the position fields in widgetConfigs in the changed columns after a DND.
	 * @param event
	 * @param draggedGadget
	 * @param newCol
	 * @param newRow
	 * @return
	 */
	private List<WidgetConfig> handleMove(DropEvent event, Gadget draggedGadget, int newCol, int newRow) {
		WidgetConfig wConf = draggedGadget.getKiWiWidget().getWidgetConfig();
		int oldCol = wConf.getColumn();
		int oldRow = wConf.getRow();
//		KiWiWidget.log("Moving " + draggedGadget.getTitle() + " from " + 
//				String.valueOf(oldCol) + ", " +
//				String.valueOf(oldRow) + " to " +
//				String.valueOf(newCol) + ", " +
//				String.valueOf(newRow));
		
		GadgetColumn srcCol = this.getPortal().getColumns().get(oldCol);
		GadgetColumn targetCol = this.getPortal().getColumns().get(newCol);
		
		// The move is in the same column..
		WidgetConfig widgetConfig = ((Gadget)srcCol.getMember(oldRow)).getKiWiWidget().
			getWidgetConfig();
		if(oldCol == newCol){
			// nothing changed
			if(oldRow == newRow)event.cancel();
			
			// move up
			if(oldRow>newRow){
				shiftRows(srcCol, newRow+1, oldRow-1, +1);
				widgetConfig.setRow(newRow);
			}
			
			// move down
			if(oldRow<newRow){
				shiftRows(srcCol, newRow+1, oldRow-1, +1);
				widgetConfig.setRow(newRow);
			}
		}
		
		// The move is between two different columns
		shiftRows(srcCol, oldRow, srcCol.getMembers().length-1, -1);
		shiftRows(targetCol, newRow, targetCol.getMembers().length-1, +1);
		widgetConfig.setColumn(newCol);
		widgetConfig.setRow(newRow);
		
		List<WidgetConfig> res = new LinkedList<WidgetConfig>();
		
		for(GadgetColumn col:this.getPortal().getColumns()){
			for(Canvas canv:col.getMembers()){
				Gadget gadget = (Gadget)canv;
				res.add(gadget.getKiWiWidget().getWidgetConfig());
			}
		}
		
		return res;
	}

	/**
	 * Increase/decrease the row field of widgetConfigs in a GadgetColumn by offset, 
	 * between the member indexes from and to.
	 * (Note that each member in a column is a Gadget and each gadget contains a KiWiWidget) 
	 * @param srcCol
	 * @param from
	 * @param to
	 * @param offset
	 */
	private void shiftRows(GadgetColumn srcCol, int from, int to, int offset) {
		for(int i=from; i<to;i++){
			WidgetConfig conf = ((Gadget)srcCol.getMember(i)).getKiWiWidget().getWidgetConfig();
			conf.setRow(conf.getRow() + offset);
		}
	}

	/**
	 * @return the portal
	 */
	public GadgetPortal getPortal() {
		return portal;
	}

	public void saveWidgetConfig(WidgetConfig config, AsyncCallback<Void> callback) {
		try {
			getDashboardAction().setWidgetConfiguration(applicationId, config, callback);
		} catch (KiWiSerializableException e) {
			SC.warn(e.getMessage());
		}
	}

    private static native String getJSStringVariable(String name) /*-{
        var variable = eval("$wnd."+name);
        if(variable == null){
            variable = "";
        }
        return variable;
    }-*/;



}