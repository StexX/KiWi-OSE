/*
 * File : DashboardGWTActionAsync.java.java
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
 * Contributor(s):
 */
package kiwi.view.dashboard.main.client;

import java.util.List;

import kiwi.transport.client.KiWiSerializableException;
import kiwi.transport.client.WidgetConfig;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * @author Szaby Grünwald
 * @version 0.7
 * @since 0.7
 *
 */
public interface DashboardGWTAction  extends RemoteService {
	/**
	 * Return the portal configuration list of WidgetConfig elements for the current user and application.
	 * @param applicationId (e.g. "dashboard")
	 * @return
	 */
	public List<WidgetConfig> getPortalConfiguration(String applicationId);
	
	/**
	 * Return the maximum column number among the WidgetConfig objects.
	 * @param applicationId (e.g. "dashboard")
	 * @return
	 */
	public int getNumberOfPortalColumns(String applicationId);
	
	/**
	 * Store the whole portal configuration for the current user and an application.
	 * @param applicationId (e.g. "dashboard")
	 * @param configList
	 */
	public void setPortalConfiguration(String applicationId, List<WidgetConfig> configList);
	
	/**
	 * Store a single widget configuration for the current user in an application. Not to use if positions change. 
	 * The stored WidgetConfig will be compared to the stored one stored. If the stored config list 
	 * doesn't have the config with the same position (column and row) and widgetId, 
	 * a KiWiSerializableException is thrown.   
	 * @param applicationId (e.g. "dashboard")
	 * @param config
	 * @throws KiWiSerializableException is thrown if the position is changed as well.
	 */
	public void setWidgetConfiguration(String applicationId, WidgetConfig config) throws KiWiSerializableException;
	
	/**
	 * Remove the stored portal configuration for the current user so the next time the 
	 * default configuration is going to be loaded.  
	 * @param applicationId
	 */
	public void resetPortalConfiguration(String applicationId);

}
