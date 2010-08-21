/*
 * File : PortalConfigService.java.java
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
package kiwi.api.config;

import java.util.List;

import kiwi.exception.KiWiException;
import kiwi.model.user.User;
import kiwi.transport.client.WidgetConfig;

/**
 * 
 * @author Szaby Grünwald
 * @version 0.7
 * @since 0.7
 *
 */
public interface PortalConfigService {
	
	/**
	 * Return the portal configuration list of WidgetConfig elements for a specific user 
	 * and application. Returns null if no configuration is stored for the user and
	 * the application with the applicationId doesn't provide a default configuration. 
	 * @param user
	 * @param applicationId (e.g. "dashboard")
	 * @return a sorted list of widget configurations
	 */
	public List<WidgetConfig> getPortalConfiguration(User user, String applicationId);
	
	/**
	 * Return the maximum column number among the WidgetConfig objects.
	 * @param user
	 * @param applicationId (e.g. "dashboard")
	 * @return
	 */
	public int getNumberOfPortalColumns(User user, String applicationId);
	
	/**
	 * Store the whole portal configuration for a user and an application.
	 * @param user
	 * @param applicationId (e.g. "dashboard")
	 * @param configList
	 */
	public void setPortalConfiguration(User user, String applicationId, List<WidgetConfig> configList);
	
	/**
	 * Reset the portal configuration for the given user and applicationId.
	 * @param user
	 * @param applicationId
	 */
	public void resetPortalConfiguration(User user, String applicationId);
	
	/**
	 * Store a single widget configuration for a user in an application. Not to use if positions change. 
	 * The stored WidgetConfig will be compared to the stored one stored. If the stored config list 
	 * doesn't have the config with the same position (column and row) and widgetId, 
	 * a KiWiException is thrown.   
	 * @param user
	 * @param applicationId (e.g. "dashboard")
	 * @param config
	 * @throws KiWiException is thrown if the position is changed as well.
	 */
	public void setWidgetConfiguration(User user, String applicationId, WidgetConfig config) throws KiWiException;
	
}
