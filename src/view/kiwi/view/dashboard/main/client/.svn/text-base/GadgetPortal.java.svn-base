/*
 * File : GadgetPortal.java
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

import com.google.gwt.user.client.Window;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.DropEvent;
import com.smartgwt.client.widgets.events.DropHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;

/**
 * 
 * @author Szaby Grünwald
 * @version 0.7
 * @since 0.7
 *
 */
public class GadgetPortal extends VLayout {

	final GadgetContainer portalLayout;
	private Dashboard dashboard;
	
	public void addGadget(int column, Gadget gadget){
		portalLayout.addGadget(column, gadget);
	}
	
	public List<GadgetColumn> getColumns(){
		return portalLayout.getColumns();
	}
	/**
	 * Constructor
	 */
	public GadgetPortal(int numColumns) {
		super(15);

		portalLayout = new GadgetContainer(numColumns);
		portalLayout.setWidth100();
		portalLayout.setHeight100();

		final DynamicForm form = new DynamicForm();
		form.setAutoWidth();
		form.setNumCols(1);
/*
		// button to add gadgets
		final ButtonItem addPortletButton = new ButtonItem("Add Portlet");
//		addPortlet.setIcon("silk/application_side_expand.png");
		addPortletButton.setAutoFit(true);
		addPortletButton.setStartRow(false);
		addPortletButton.setEndRow(false);
		addPortletButton
				.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
					public void onClick(
							com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {

						final Gadget newPortlet = new Gadget();
						newPortlet.setTitle("Portlet ");
						int height = new Double(300 * Math.random()).intValue();
						newPortlet.setHeight(height);

						Label label = new Label();
						label.setAlign(Alignment.CENTER);
						label.setLayoutAlign(VerticalAlignment.CENTER);
						label.setContents("Portlet contents");
						label.setBackgroundColor("FF6600");
						newPortlet.addItem(label);

						newPortlet.setVisible(false);
						GadgetColumn column = portalLayout
								.addGadget(newPortlet);

						// also insert a blank spacer element, which will
						// trigger the built-in
						// animateMembers layout animation
						final LayoutSpacer placeHolder = new LayoutSpacer();
						placeHolder.setRect(newPortlet.getRect());
						column.addMember(placeHolder, 0); // add to top

						// create an outline around the clicked button
						final Canvas outline = new Canvas();
						outline.setLeft(form.getAbsoluteLeft()
								+ addPortletButton.getLeft());
						outline.setTop(form.getAbsoluteTop());
						outline.setWidth(addPortletButton.getWidth());
						outline.setHeight(addPortletButton.getHeight());
						outline.setBorder("2px solid 8289A6");
						outline.draw();
						outline.bringToFront();

						outline.animateRect(newPortlet.getPageLeft(),
								newPortlet.getPageTop(), newPortlet
										.getVisibleWidth(), newPortlet
										.getViewportHeight(),
								new AnimationCallback() {
									public void execute(boolean earlyFinish) {
										// callback at end of animation -
										// destroy placeholder and outline; show
										// the new portlet
										placeHolder.destroy();
										outline.destroy();
										newPortlet.show();
									}
								}, 750);
					}
				});

		// add one gadget (TEST)
//		Gadget gadget = new Gadget();
//		gadget.setTitle("My Portlet");
//		portalLayout.addGadget(gadget);

		form.setItems(addPortletButton);

//		this.addMember(form);
 */
		this.addMember(portalLayout);
	}
	
	public void setDashboard(Dashboard dashboard) {
		this.dashboard = dashboard;
	}

	public int getColumnNr(GadgetColumn gadgetColumn) {
		int i=-1;
		for(GadgetColumn col:portalLayout.getColumns()){
			i++;
			if(col.equals(gadgetColumn))
				return i;
		}
		Window.alert(gadgetColumn.toString());
		return -1;
	}

	/**
	 * PortalColumn class definition
	 */
	public class GadgetColumn extends VStack {

		
		public GadgetColumn() {

			// leave some space between portlets
			setMembersMargin(6);

			// enable predefined component animation
			setAnimateMembers(true);
			setAnimateMemberTime(300);

			// enable drop handling
			setCanAcceptDrop(true);
			
			setCanDropComponents(true);

			// change appearance of drag placeholder and drop indicator
			setDropLineThickness(4);
			this.addDropHandler(new DropHandler() {
				@Override
				public void onDrop(DropEvent event) {
					dashboard.dropHappened(event);
				}
			});
			
			
			Canvas dropLineProperties = new Canvas();
			dropLineProperties.setBackgroundColor("aqua");
			setDropLineProperties(dropLineProperties);

			setShowDragPlaceHolder(true);

			Canvas placeHolderProperties = new Canvas();
			placeHolderProperties.setBorder("2px solid #8289A6");
			setPlaceHolderProperties(placeHolderProperties);
		}
		
	}

	private class GadgetContainer extends HLayout {

		List<GadgetColumn> columns = new LinkedList<GadgetColumn>();
		
		public GadgetContainer(int numColumns) {
			setMembersMargin(6);
			for (int i = 0; i < numColumns; i++) {
				GadgetColumn column = new GadgetColumn();
				columns.add(column);
				addMember(column);
			}
		}

		/**
		 * put a Gadget on a GadgetColumn with the index column.
		 * @param gadget
		 * @return
		 */
		public GadgetColumn addGadget(int column, Gadget gadget) {
			// find the column with the fewest portlets
			GadgetColumn gadgetColumn = (GadgetColumn) getMember(column);
			return addGadget(gadgetColumn, gadget);
		}

		/**
		 * Put a Gadget on the column with the fewest gadgets.
		 * @param gadget
		 * @return
		 */
		public GadgetColumn addGadget(Gadget gadget) {
			// find the column with the fewest portlets
			int fewestPortlets = Integer.MAX_VALUE;
			GadgetColumn fewestPortletsColumn = null;
			for (int i = 0; i < getMembers().length; i++) {
				int numPortlets = ((GadgetColumn) getMember(i)).getMembers().length;
				if (numPortlets < fewestPortlets) {
					fewestPortlets = numPortlets;
					fewestPortletsColumn = (GadgetColumn) getMember(i);
				}
			}
			return addGadget(fewestPortletsColumn, gadget);
		}
		
		/**
		 * Put a Gadget on a specific GadgetColumn
		 * @param column
		 * @param gadget
		 * @return
		 */
		public GadgetColumn addGadget(GadgetColumn column, Gadget gadget) {
			column.addMember(gadget);
			return column;
		}

		/**
		 * @return the columns
		 */
		public List<GadgetColumn> getColumns() {
			return columns;
		}

}


}
