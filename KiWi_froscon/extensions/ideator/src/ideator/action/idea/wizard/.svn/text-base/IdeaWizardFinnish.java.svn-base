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
package ideator.action.idea.wizard;

import ideator.action.admin.MailAction;
import ideator.datamodel.IdeaFacade;
import ideator.datamodel.IdeatorUserFacade;
import ideator.service.IdeaService;
import ideator.service.IdeatorUserService;

import java.util.LinkedList;

import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.user.UserService;
import kiwi.context.CurrentContentItemFactory;
import kiwi.exception.RegisterException;
import kiwi.exception.UserExistsException;
import kiwi.model.ontology.SKOSConcept;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;

/**
 * @author Rolf Sint
 * @version 0.7
 * @since 0.7
 * 
 */
@Name("ideator.ideaWizardFinnish")
@Scope(ScopeType.CONVERSATION)
//@Transactional
public class IdeaWizardFinnish {

	@In
	public IdeaBean ideaBean;

	@In
	private CurrentContentItemFactory currentContentItemFactory;
	
	@In(required = false)
	private CategoryAction categoryAction;
	
	@In
	private IdeaService ideaService;

	@In(value = "ideator.userService")
	private IdeatorUserService ideatorUserService;
	
	@In
    private KiWiEntityManager kiwiEntityManager;
	
	@In(value ="ideator.mailAction", create = true) 
	private MailAction mailAction;
	
	@In
    protected UserService userService;

	@Logger
	private static Log log;
	
	private boolean isUser(String login) {
		return userService.userExists(login);
	}
	
	public void storeIdea() {
			
		LinkedList<CoAuthorTmp> coTmps = ideaBean.getCoAuthors();
		
		for(CoAuthorTmp ct: coTmps){
			if(isUser(ct.getLogin())){
				log.info(ct.getLogin() + " is user");
				mailAction.send(ct.getCoAuthorsEmail(), ct.getCoAuthorsFirstName()+" "+ct.getCoAuthorsLastName(), ct.getLogin(), ideaBean.getTitle(), ideaBean.getAuthor().getLogin());
			}
			else{
				log.info(ct.getLogin() + " is no user");
				mailAction.send(ct.getCoAuthorsEmail(), ct.getCoAuthorsFirstName()+" "+ct.getCoAuthorsLastName(), ct.getLogin(), ideaBean.getTitle(), ct.getPwd(), ideaBean.getAuthor().getLogin());
			}
		}
		
		
		log.info("Storing idea #0" + ideaBean.getTitle());
		log.info("Storing idea #0" + ideaBean.isAnonymous());
		
		IdeaFacade ideaFacade = ideaService.createIdea(ideaBean.getTitle(),ideaBean.getDesc(), ideaBean.getIsDesc(), ideaBean.getShouldDesc(), ideaBean.isAnonymous(), ideaBean.getUpload());
		
		LinkedList<CoAuthorTmp> cl = ideaBean.getCoAuthors();
		IdeatorUserFacade ideatorUserFacade = null;
		
		LinkedList<SKOSConcept> chosenCategories =  categoryAction.getChosenConcepts();
		
		//store all categories
		if(categoryAction != null)
			storeCategories(ideaFacade, chosenCategories);
				
		storeCoAuthors(ideaFacade, cl, ideatorUserFacade);
		
		kiwiEntityManager.persist(ideaFacade);
		
		//set curContentItem
		currentContentItemFactory.setCurrentItemId(ideaFacade.getId());
	}
//	
//	@CreateProcess(definition = "Idea-manage")
//	public void finish(){
//		
//	}

	/**
	 * @param ideaFacade
	 * @param cl
	 * @param ideatorUserFacade
	 */
	private void storeCoAuthors(IdeaFacade ideaFacade,
			LinkedList<CoAuthorTmp> cl, IdeatorUserFacade ideatorUserFacade) {
		try {
			for(CoAuthorTmp coAuthor: cl){
				
				String login = "";
				//create the user in the case that it does not already exist
				if(coAuthor.getUser() == null){
					login = coAuthor.getCoAuthorsFirstName()+coAuthor.getCoAuthorsLastName();
					ideatorUserFacade = ideatorUserService.createUser(login,coAuthor.getCoAuthorsFirstName(), coAuthor.getCoAuthorsLastName(), coAuthor.getPwd(), coAuthor.getCoAuthorsEmail(), coAuthor.getPositions());
				}
				else{
					login = coAuthor.getUser().getLogin();
				}
					
				
				LinkedList<String> coAuthors = ideaFacade.getCoAuthors();
				coAuthors.add(login);
				ideaFacade.setCoAuthors(coAuthors);
				
			}			
		} catch (UserExistsException e) {
			e.printStackTrace();
		} catch (RegisterException e) {
			e.printStackTrace();
		}
		cl = null;
	}

	/**
	 * @param ideaFacade
	 * @param chosenCategories
	 */
	private void storeCategories(IdeaFacade ideaFacade,
			LinkedList<SKOSConcept> chosenCategories) {
				for(SKOSConcept sk: chosenCategories){
					LinkedList<SKOSConcept> categories =  ideaFacade.getCategories();
					categories.add(sk);
					ideaFacade.setCategories(categories);
				}
	}
	
	public IdeaBean getIdeaBean() {
		return ideaBean;
	}

	public void setIdeaBean(IdeaBean ideaBean) {
		this.ideaBean = ideaBean;
	}

}

