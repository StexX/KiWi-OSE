/*
 * File : WidgetConfig.java.java
 * Date : Mar 17, 2010
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
 * Contributor(s):
 */
package kiwi.transport.client;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * A WidgetConfig holds the configuration of a KiWi widget on an application
 * portal. column and row are position indexes, widgetId tells which widget
 * has to be instantiated, peoperties holds the configuration for the widget.  
 * @author Szaby Grünwald
 * @version 0.7
 * @since 0.7
 *
 */
public class WidgetConfig implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1381713464291081920L;
	private int column;
	private int row;
	private String title;
	private String widgetId;
	private String properties;
	/*
	 * Remark: In case you have to extend this class, don't forget to extend the 
	 * serialize() and deserialize() methods as well, otherwise the extended 
	 * fields won't be saved!
	 */
	
	/**
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}
	/**
	 * @return the row
	 */
	public int getRow() {
		return row;
	}
	/**
	 * @return the widgetId
	 */
	public String getWidgetId() {
		return widgetId;
	}
	/**
	 * @return the properties
	 */
	public String getProperties() {
		return properties;
	}
	/**
	 * @param column the column to set
	 */
	public void setColumn(int column) {
		this.column = column;
	}
	/**
	 * @param row the row to set
	 */
	public void setRow(int row) {
		this.row = row;
	}
	/**
	 * @param widgetId the widgetId to set
	 */
	public void setWidgetId(String widgetId) {
		this.widgetId = widgetId;
	}
	/**
	 * @param properties the properties to set
	 */
	public void setProperties(String properties) {
		this.properties = properties;
	}
	
	public LinkedList<String> serialize(){
		LinkedList<String> res = new LinkedList<String>();
		res.add(String.valueOf(this.getColumn()));
		res.add(String.valueOf(this.getRow()));
		res.add(this.getWidgetId());
		res.add(this.getTitle());
		res.add(this.getProperties());
		return res;
	}
	
	public List<String> deserialize(List<String> params){
		this.column = Integer.parseInt(params.remove(0));
		this.row = Integer.parseInt(params.remove(0));
		this.widgetId = params.remove(0);
		this.title = params.remove(0);
		this.properties = params.remove(0);
		return params;
	}
	public static WidgetConfig generate(String widgetId, String title, int column, int row,
			String properties) {
		WidgetConfig res = new WidgetConfig();
		res.setWidgetId(widgetId);
		res.setTitle(title);
		res.setColumn(column);
		res.setRow(row);
		res.setProperties(properties);
		return res;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		if("".equals(title) || title == null)
			return widgetId;
		else
			return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
}
