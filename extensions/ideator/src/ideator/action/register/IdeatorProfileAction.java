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
package ideator.action.register;

import ideator.datamodel.IdeatorUserFacade;
import ideator.service.IdeatorUserService;
import kiwi.commons.userProfile.ProfileAction;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.faces.FacesMessages;

/**
 * @author Rolf Sint
 * @version 0.7
 * @since 0.7
 *
 */
@Name("ideatorProfileAction")
@Scope(ScopeType.CONVERSATION)
@Transactional
public class IdeatorProfileAction extends ProfileAction {
    	
	@In(required = false,create=true)
	@Out(required = false)
	private PositionAction positionAction;
	
	@In(value = "ideator.userService")
	private IdeatorUserService ideatorUserService;
    	
    	
	//implements specific behavior in the init method, because it is not allowed to implement 2 create methods 
	@Override
	protected void specificBehavior(){
	    IdeatorUserFacade ideatorUser = ideatorUserService.getUser(currentUser);
	    log.info(ideatorUser.getPositions().size());
	    positionAction.setChosenConcepts(ideatorUser.getPositions());
	}
	
	//is called after saving
	protected void postSave(){
	    IdeatorUserFacade ideatorUser = ideatorUserService.getUser(currentUser);
	    ideatorUser.setPositions(positionAction.getChosenConcepts());
	    log.info(ideatorUser.getPositions().size());
	    kiwiEntityManager.persist(ideatorUser);
	    FacesMessages.instance().add("Userprofile saved");
	}
	
	//no regex, every string is a valid name
	protected boolean checkValues() {
	    boolean error = false;
	    return error;
	}
	
		   
}
