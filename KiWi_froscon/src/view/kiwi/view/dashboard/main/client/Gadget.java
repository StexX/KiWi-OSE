/*
 * File : Gadget.java
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

import kiwi.transport.client.Properties;
import kiwi.transport.client.WidgetConfig;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.DragAppearance;
import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.types.LayoutPolicy;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HeaderControl;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * 
 * @author Szaby Grünwald
 * @version 0.7
 * @since 0.7
 *
 */
public class Gadget extends Window {
	private KiWiWidget kiWiWidget;
	
	private Canvas configPanel = new VLayout();

	private Dashboard dashboard;

	public Gadget(KiWiWidget kiWiWidget) {
		super();
		configPanel.hide();
		configPanel.setBorder("solid #cccccc 0 0 2px 0");
		configPanel.setWidth100();
		configPanel.setHeight100();
		configPanel.setPadding(10);
		this.addItem(configPanel);
		
		if(kiWiWidget == null)
			KiWiWidget.log("Gadgets need to be initialized with a KiWiWidget");
		kiWiWidget.setContainerGadget(this);
		this.setTitle(kiWiWidget.getWidgetConfig().getTitle());
		this.setKiWiWidget(kiWiWidget);
		
		setShowShadow(false);
		// enable predefined component animation
		setAnimateMinimize(true);

		// Window is draggable with "outline" appearance by default.
		// "target" is the solid appearance.
		setDragAppearance(DragAppearance.OUTLINE);
		setCanDrop(true);

		// show either a shadow, or translucency, when dragging a portlet
		// (could do both at the same time, but these are not visually
		// compatible effects)
		// setShowDragShadow(true);
		setDragOpacity(30);

		// these settings enable the portlet to autosize its height only to fit
		// its contents
		// (since width is determined from the containing layout, not the
		// portlet contents)
		// setVPolicy(LayoutPolicy.NONE);
		setVPolicy(LayoutPolicy.FILL);

		// set the widget resizable
		// setShowFooter(true);setShowResizer(true);setCanDragResize(true);

		setAutoSize(true);
		setOverflow(Overflow.CLIP_H);
	}

	private void setHeaders() {
		// customize the appearance and order of the controls in the window
		// header
		HeaderControl settingIcon = new HeaderControl(HeaderControl.SETTINGS);
		final Gadget thisGadget = this;
		settingIcon.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				thisGadget.showConfigPanel();
			}
		});

		addCloseClickHandler(new CloseClickHandler() {

			@Override
			public void onCloseClick(CloseClientEvent event) {
				// TODO Auto-generated method stub
				KiWiWidget.log("close handler");

			}
		});
		
		setHeaderControls(
       		 HeaderControls.HEADER_LABEL, 
//       		 new HeaderControl(HeaderControl.HELP), 
       		 settingIcon, 
       		 HeaderControls.MINIMIZE_BUTTON, 
       		 HeaderControls.CLOSE_BUTTON);  

		if(!kiWiWidget.isConfigurable()){
			settingIcon.hide();
		}
	}

	protected void showConfigPanel() {
		Layout configPanel2 = kiWiWidget.getConfigPanel();
		configPanel2.setPadding(10);
		configPanel.addChild(configPanel2);
		configPanel.show();
	}

	public void setKiWiWidget(KiWiWidget widget) {
		this.kiWiWidget = widget;
		Canvas widgetCanvas = widget.getWidgetPanel();
		this.addItem(widgetCanvas);
		kiWiWidget.setContainerGadget(this);
		this.setTitle(kiWiWidget.getWidgetConfig().getTitle());
		setHeaders();
	}

	/**
	 * @return the kiWiWidget
	 */
	public KiWiWidget getKiWiWidget() {
		return kiWiWidget;
	}

	public void saveWidgetConfig(WidgetConfig config, AsyncCallback<Void> callback) {
		dashboard.saveWidgetConfig(config, callback);
	}

	/**
	 * @return the dashboard
	 */
	public Dashboard getDashboard() {
		return dashboard;
	}

	/**
	 * @param dashboard the dashboard to set
	 */
	public void setDashboard(Dashboard dashboard) {
		this.dashboard = dashboard;
	}

	public void hideConfig() {
		configPanel.hide();
	}
}
