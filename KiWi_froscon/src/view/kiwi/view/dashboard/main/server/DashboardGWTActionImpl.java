/*
 * File : DashboardGWTActionImpl.java.java
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
package kiwi.view.dashboard.main.server;

import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.remoting.WebRemote;

import kiwi.api.config.PortalConfigService;
import kiwi.exception.KiWiException;
import kiwi.model.user.User;
import kiwi.transport.client.KiWiSerializableException;
import kiwi.transport.client.WidgetConfig;
import kiwi.view.dashboard.main.client.DashboardGWTAction;

/**
 * @see kiwi.view.dashboard.main.client.DashboardGWTAction
 * @author Szaby Grünwald
 * @version 0.7
 * @since 0.7
 *
 */
@Name("kiwi.view.dashboard.main.client.DashboardGWTAction")
//@Transactional
@Scope(ScopeType.SESSION)
public class DashboardGWTActionImpl implements DashboardGWTAction {

	@In(create=true)
	User currentUser;
	
	@In(value="kiwi.core.portalConfigService", create=true)
	PortalConfigService portalConfigService;

	/* (non-Javadoc)
	 * @see kiwi.view.dashboard.main.client.DashboardGWTAction#getNumberOfPortalColumns(java.lang.String)
	 */
	@Override
	@WebRemote
	public int getNumberOfPortalColumns(String applicationId) {
		return portalConfigService.getNumberOfPortalColumns(currentUser, applicationId);
	}

	/* (non-Javadoc)
	 * @see kiwi.view.dashboard.main.client.DashboardGWTAction#getPortalConfiguration(java.lang.String)
	 */
	@Override
	@WebRemote
	public List<WidgetConfig> getPortalConfiguration(String applicationId) {
		return portalConfigService.getPortalConfiguration(currentUser, applicationId);
	}

	/* (non-Javadoc)
	 * @see kiwi.view.dashboard.main.client.DashboardGWTAction#setPortalConfiguration(java.lang.String, java.util.List)
	 */
	@Override
	@WebRemote
	public void setPortalConfiguration(String applicationId,
			List<WidgetConfig> configList) {
		portalConfigService.setPortalConfiguration(currentUser, applicationId, configList);
	}

	/* (non-Javadoc)
	 * @see kiwi.view.dashboard.main.client.DashboardGWTAction#setWidgetConfiguration(java.lang.String, kiwi.transport.client.WidgetConfig)
	 */
	@Override
	@WebRemote
	public void setWidgetConfiguration(String applicationId, WidgetConfig config) throws KiWiSerializableException {
		try {
			portalConfigService.setWidgetConfiguration(currentUser, applicationId, config);
		} catch (KiWiException e) {
			throw new KiWiSerializableException(e.getMessage());
		}
		
	}

	/* (non-Javadoc)
	 * @see kiwi.view.dashboard.main.client.DashboardGWTAction#resetPortalConfiguration(java.lang.String) 
	 */
	@Override
	@WebRemote
	public void resetPortalConfiguration(String applicationId) {
		portalConfigService.resetPortalConfiguration(currentUser, applicationId);
	}
	

}
