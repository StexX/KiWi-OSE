/*
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
 *
 *
 */

package kiwi.wiki.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import kiwi.api.config.ConfigurationService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.extension.ExtensionService;
import kiwi.api.extension.KiWiApplication;
import kiwi.api.triplestore.TripleStore;
import kiwi.api.user.UserService;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.user.User;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * @author sschaffe
 * 
 */
@Name("kiwi.wiki.ui.layoutAction")
@Scope(ScopeType.SESSION)
@Deprecated
public class LayoutAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3999033478186517647L;

	@Logger
	protected Log log;

	@In
	protected ConfigurationService configurationService;

	@In
	protected KiWiEntityManager kiwiEntityManager;

	@In(required = true)
	protected TripleStore tripleStore;

	@In(required = false)
	protected ContentItem currentContentItem;

	@In
	private User currentUser;

	/**
	 * viewMode can be compact or expanded
	 */
	public String viewMode;

	public List<String> getViewModes() {
		List<String> res = new ArrayList<String>();
		res.addAll(Arrays.asList("compact", "expanded"));
		return res;
	}

	// TODO: add drag listeners to remove widgets instead of removing them on
	// drop

	@Destroy
	public void destroy() {
	}

	/**
	 * returns the UI fragment path for displaying a search result.
	 * 
	 * @param ci
	 * @return
	 */
	public String getSearchResultItemTemplate(ContentItem ci) {
		log.debug("viewMode: #0 ", getViewMode());
		String res = "";
		if ("expanded".equals(getViewMode())) {
			ExtensionService es = (ExtensionService) Component
					.getInstance("extensionService");
			Collection<KiWiApplication> apps = es.getApplications();
			Iterator<KiWiApplication> it = apps.iterator();
			res = "searchResultItemTemplates/defaultsearchresultItem.xhtml";

			if (ci == null) {
				log.debug("getSearchResultItemTemplate(ci): ci was null.");
				return res;
			} else {
				log.debug("getSearchResultItemTemplate(ci): ci is #0.", ci
						.getTitle());
			}

			for (KiWiResource type : ci.getTypes()) {
				String seRQLID = type.getSeRQLID();
				log.debug("type: #0", seRQLID);
				if (seRQLID.contains(Constants.NS_KIWI_CORE + "Image")) {
					res = "searchResultItemTemplates/imageresultItem.xhtml";
				} else {
					while (it.hasNext()) {
						KiWiApplication app = it.next();
						String candidate = app
								.getSearchResultItemTemplatePath(ci);
						if (candidate != null) {
							res = candidate;
						}
					}
				}
			}
		} else if ("compact".equals(getViewMode())) {
			res = "searchResultItemTemplates/compactsearchresultItem.xhtml";
		}
		return res;
	}

	/**
	 * return the ui fragment path for editing a specific contentItem. TODO
	 * still missing a mechanism for conflicts between extensions. (In case more
	 * extensions have a candidate template)
	 * 
	 * @param ci
	 * @return
	 */
	public String getEditorTemplate(ContentItem ci) {
		String res = null;

		// Otherwise ask the KiWiApplications if they have a template to
		// display.
		ExtensionService es = (ExtensionService) Component
		.getInstance("extensionService");
		Collection<KiWiApplication> apps = es.getApplications();
		
		for (KiWiApplication app : apps) {
			String candidate = app.getEditTemplatePath(ci);
			if (candidate != null) {
				res = candidate;
			}
		}
		
		if(res==null){
			// Find out if the contentItem is a user (hasType kiwi:User)
			res = "editortemplates/defaultEditor.xhtml";
			for (KiWiResource type : ci.getTypes()) {
				String seRQLID = type.getSeRQLID();
				log.debug("type: #0", seRQLID);
			//	if (seRQLID.contains(Constants.NS_KIWI_CORE + "Image")) {
			//		res = "editortemplates/imageEditor.xhtml";
			//	}
			}
		}
		log.debug("getEditorTemplate: using #0 to edit #1", res, ci.getTitle());
		return res;
	}

	/**
	 * return the ui fragment path for displaying a specific contentItem. TODO
	 * still missing a mechanism for conflicts between extensions. (In case more
	 * extensions have a candidate template)
	 * 
	 * @param ci
	 * @return
	 */
	public String getContentItemTemplate(ContentItem ci) {
		String res = null;

		ExtensionService es = (ExtensionService) Component
				.getInstance("extensionService");
		Collection<KiWiApplication> apps = es.getApplications();
		// ask all applications if they have a template other then the default one to use
		for (KiWiApplication app : apps) {
			String appRes = app.getContentItemTemplatePath(ci);
			if (appRes != null) {
				res = appRes;
			}
		}
		
		// If no application has implemented a better visualisation, 
		// use the default one.
		if (res == null) {
			res = "typetemplates/defaultcontentitem.xhtml";
			// Find out if the contentItem is a user (hasType kiwi:User)
			for (KiWiResource type : ci.getTypes()) {
				String seRQLID = type.getSeRQLID();
				log.debug("type: #0", seRQLID);
				if (seRQLID.contains(Constants.NS_KIWI_CORE + "User")) {
					res = "typetemplates/userprofile.xhtml";
				}
			}
		}
		log.debug("getContentItemTemplate: using #0 to display #1", res, ci
				.getTitle());
		return res;
	}

	/**
	 * tells if ci is a user.
	 * 
	 * @param ci
	 * @return
	 */
	public boolean isUser(ContentItem ci) {
		boolean res = false;
		for (KiWiResource type : ci.getTypes()) {
			if (type.getSeRQLID().contains(
					"<" + Constants.NS_KIWI_CORE + "User>"))
				res = true;
		}
		return res;
	}

	/**
	 * tells whether or not the given contentItem is currentUser
	 * 
	 * @param ci
	 * @return
	 */
	public boolean isMyProfile(ContentItem ci) {
		boolean res = false;
		UserService userService = (UserService) Component
				.getInstance("userService");
		res = isUser(ci)
				&& userService.getUserByProfile(ci).getLogin().equals(
						currentUser.getLogin());
		return res;
	}

	public String getViewMode() {
		if (viewMode == null)
			viewMode = "expanded";
		return viewMode;
	}

	public void setViewMode(String viewMode) {
		log.debug("New ViewMode stored: #0", viewMode);
		this.viewMode = viewMode;
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

}
