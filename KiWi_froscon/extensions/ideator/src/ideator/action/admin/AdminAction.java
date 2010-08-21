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
package ideator.action.admin;

import ideator.service.IdeatorUserService;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.group.GroupService;
import kiwi.api.search.KiWiSearchCriteria;
import kiwi.api.search.SolrService;
import kiwi.api.search.KiWiSearchResults.SearchResult;
import kiwi.api.user.UserService;
import kiwi.exception.GroupExistsException;
import kiwi.model.Constants;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.Group;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

/**
 * @author Rolf Sint
 * @version 0.7
 * @since 0.7
 * 
 */
@Name("ideator.adminAction")
@Scope(ScopeType.CONVERSATION)
//@Transactional
public class AdminAction {

    @In
    private SolrService solrService;

    @In
    private UserService userService;

    @In
    private GroupService groupService;

    @In(value = "ideator.userService")
    private IdeatorUserService ideatorUserService;

    @In
    private KiWiEntityManager kiwiEntityManager;

    @DataModel
    private List<UserPojo> usersl;

    @DataModelSelection
    private UserPojo selectedUser;

    @Logger
    private Log log;

    @Factory("usersl")
    public void init() {
	usersl = new LinkedList<UserPojo>();

	// query
	KiWiSearchCriteria c = new KiWiSearchCriteria();
	c.setSolrSearchString("type:\"uri::" + Constants.NS_KIWI_CORE
		+ "User\"");
	c.setLimit(-1);
	for (SearchResult r : solrService.search(c).getResults()) {
	    User u = userService.getUserByProfile(r.getItem());
	    UserPojo up = new UserPojo();
	    up.setUser(u);
	    up.setIdeatorUser(checkIdeatorUser(u));
	    up.setIdeaManager(checkIdeaManager(u));
	    usersl.add(up);
	}
	log.info("#0 results", usersl.size());
    }

    private boolean checkIdeatorUser(User user) {
	Collection<KiWiResource> kl = user.getResource().getTypes();
	for (KiWiResource kr : kl) {
	    if (kr.isUriResource()) {
		KiWiUriResource kur = (KiWiUriResource) kr;

		if ((kur.getUri().contains(Constants.IDEATOR_CORE
			+ "IdeatorUser"))) {
		    return true;
		}
	    }
	}
	return false;
    }

    private void deleteIdeatorUser(User user) {
	// delete the type
	Collection<KiWiResource> kl = user.getResource().getTypes();
	for (KiWiResource kr : kl) {
	    if (kr.isUriResource()) {
		KiWiUriResource kur = (KiWiUriResource) kr;

		if ((kur.getUri().contains(Constants.IDEATOR_CORE
			+ "IdeatorUser"))) {
		    user.getContentItem().getResource().removeType(kur);
		}
	    }
	}
    }

    public boolean checkIdeaManager(User user) {

	List<Group> sr = groupService.getGroupsByUser(user);
	//List<Group> sr = user.getGroups();
	for (Group g : sr) {
	    log.info("AdminAction" + g.getName());
	    if (g.getName().equals("Ideamanager")) {
		return true;
	    }
	}
	return false;

    }
    

    public void changeIdeatorUser(UserPojo userPojo) {
	boolean b = checkIdeatorUser(userPojo.getUser());

	if (b == true) {
	    log.info("Delete Type ...");
	    deleteIdeatorUser(userPojo.getUser());
	    FacesMessages.instance().add(
		    "User " + userPojo.getUser().getLogin()
			    + "+ is no Ideatoruser anymore");
	} else {
	    log.info("Convert user to Ideatoruser ...");
	    ideatorUserService.createUser(userPojo.getUser());
	    FacesMessages.instance().add(
		    "KiWi user " + userPojo.getUser().getLogin()
			    + " converted to Ideatoruser");
	}

    }

    public void changeIdeaManager(UserPojo userPojo) {
	log.info("Ideamanager changed");

	if (userPojo.isIdeaManager() == true) {
	    if (checkIdeatorUser(userPojo.getUser())) {
		addToIdeaManager(userPojo.getUser());
		FacesMessages.instance().add("user is now idea manager");
	    } else {
		FacesMessages.instance().add(
			"Error! User is not an Ideatoruser!");
	    }
	} else {
	    removeIdeaManager(userPojo);
	    FacesMessages.instance().add("removed idea manager role");
	}
    }

    private void removeIdeaManager(UserPojo userPojo) {
	Group g = null;
	g = groupService.getGroupByName("Ideamanager");
	groupService.removeGroupFromUser(userPojo.getUser(), g);
    }

    private void addToIdeaManager(User user) {

	log.info(user.getLogin());
	Group g = null;
	g = groupService.getGroupByName("Ideamanager");
	if (g == null) {
	    try {
		g = groupService.createGroup("Ideamanager");
	    } catch (GroupExistsException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
	log.info(g.getName());
	groupService.addUserToGroup(g, user);
    }
}
