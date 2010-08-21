/*
 * File : CoAuthorAction.java.java
 * Date : 29.03.2010
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
package ideator.action.idea.wizard;

import ideator.action.admin.MailAction;
import ideator.action.register.PositionAction;
import ideator.datamodel.IdeatorUserFacade;
import ideator.service.IdeatorAutocompletionService;
import ideator.service.IdeatorUserService;
import ideator.utils.IdeatorUtils;

import java.util.LinkedList;
import java.util.List;

import kiwi.api.user.UserService;
import kiwi.exception.RegisterException;
import kiwi.model.ontology.SKOSConcept;
import kiwi.model.user.User;

import org.hibernate.validator.Email;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;


/**
 * @author Rolf Sint
 * @version 0.7
 * @since 0.7
 *
 */
@Name("ideator.coAuthorAction")
@Scope(ScopeType.CONVERSATION)
//@Transactional
public class CoAuthorAction {
	
	
	@In
	public IdeaBean ideaBean;
	
	@In(create=true)
	private PositionAction positionAction;
		
	@Logger
	private static Log log;
	
	private String coAuthorsLogin;
	
	private String coAuthorsFirstName;
	
	private String coAuthorsLastName;
	
	private User selectedUser;
	
	@Email
	private String coAuthorsEmail;
	
	private LinkedList<IdeatorUserFacade> coAuthors;
	
	@In(value="ideator.ideaBean")
	private IdeaBean ideBean;
	
	@In(value="ideator.userService")
	private IdeatorUserService ideatorUserService;
	
	@In(value="ideator.autocompletionService")
	private IdeatorAutocompletionService autocompleteUserService;
	
	@In
    protected UserService userService;
	
	private List<User> recommendedUsers = new LinkedList<User>();
	
	
	@Create
	public void init(){
		positionAction.clear();		
	}
	
	@End
	public String addUser(){
				
		CoAuthorTmp ct = new CoAuthorTmp();
		
		if(isUser()) {
			ct.setUser(selectedUser);
			log.info(selectedUser.getPassword());
		//	mailAction.send(ct.getCoAuthorsEmail(), ct.getCoAuthorsFirstName()+" "+ct.getCoAuthorsLastName(), ct.getLogin(), ideaBean.getTitle(), ideaBean.getAuthor().getLogin());
		} 
		else
		{
			String pwd = IdeatorUtils.generatePassword(6);
			ct.setLogin(coAuthorsFirstName+coAuthorsLastName);
			ct.setCoAuthorsFirstName(coAuthorsFirstName);
			ct.setCoAuthorsLastName(coAuthorsLastName);
			ct.setCoAuthorsEmail(coAuthorsEmail);
			ct.setPwd(pwd);
				
			LinkedList<SKOSConcept> positionsTmp = positionAction.getChosenConcepts();
			
			if(positionsTmp.size() < 1){
				FacesMessages.instance().add("No Position selected");
				return "coAuthorsPage";
			}
			
			ct.setPositions(positionsTmp);					
		//	mailAction.send(ct.getCoAuthorsEmail(), ct.getCoAuthorsFirstName()+" "+ct.getCoAuthorsLastName(), ct.getLogin(), ideaBean.getTitle(), pwd, ideaBean.getAuthor().getLogin());
		}
				
			
		ideaBean.getCoAuthors().add(ct);
		return "new_idea";
	}
	
	private boolean isUser() {
		return userService.userExists(coAuthorsLogin);
	}
	
	public String setUserToSelection() {
		log.info("selected user #0", selectedUser.getLogin());
		coAuthorsLogin = selectedUser.getFirstName();
		coAuthorsFirstName = selectedUser.getFirstName();
		coAuthorsLastName = selectedUser.getLastName();
		coAuthorsEmail = selectedUser.getEmail();
		IdeatorUserFacade iuser = ideatorUserService.getUser(selectedUser);
		
		LinkedList<SKOSConcept> ls = iuser.getPositions();
		log.info(ls.size());
		positionAction.setChosenConcepts(ls);
		
		return "coAuthorsPage";
	}
	
	@End
	public String cancel(){
		return "new_idea";
	}
	
	public String selectUser( User user ) {
		//set user
		log.info("selected user #0", user.getLogin());
		coAuthorsLogin = user.getLogin();
		coAuthorsFirstName = user.getFirstName();
		coAuthorsLastName = user.getLastName();
		coAuthorsEmail = user.getEmail();
		IdeatorUserFacade iuser = ideatorUserService.getUser(user);
		
		LinkedList<SKOSConcept> ls = iuser.getPositions();
		log.info(ls.size());
		positionAction.setChosenConcepts(ls);
		
		selectedUser = user;
		
		return "coAuthorsPage";
	}
	
	public String unselectUser() {
		coAuthorsFirstName = "";
		coAuthorsLastName =  "";
		coAuthorsEmail = "";
		positionAction.setChosenConcepts(new LinkedList<SKOSConcept>());
		selectedUser = null;
		return "coAuthorsPage";
	}
	
	//return if list should be displayed
	public boolean autocompleteUsername() {
		if( coAuthorsFirstName == null || coAuthorsFirstName.length() < 3 ) return false;
		recommendedUsers = autocompleteUserService.getUsersByKeyword(coAuthorsFirstName);
		return recommendedUsers.size() > 0;
	}
	
	public LinkedList<IdeatorUserFacade> getCoAuthors() {
		return coAuthors;
	}

	public void setCoAuthors(LinkedList<IdeatorUserFacade> coAuthors) {
		this.coAuthors = coAuthors;
	}

	public String getCoAuthorsFirstName() {
		return coAuthorsFirstName;
	}

	public void setCoAuthorsFirstName(String coAuthorsFirstName) {
		this.coAuthorsFirstName = coAuthorsFirstName;
	}

	public String getCoAuthorsLastName() {
		return coAuthorsLastName;
	}

	public void setCoAuthorsLastName(String coAuthorsLastName) {
		this.coAuthorsLastName = coAuthorsLastName;
	}
	
	public String getCoAuthorsEmail() {
		return coAuthorsEmail;
	}

	public void setCoAuthorsEmail(String coAuthorsEmail) {
		this.coAuthorsEmail = coAuthorsEmail;
	}
	
	public void setRecommendedUsers(List<User> recommendedUsers) {
		this.recommendedUsers = recommendedUsers;
	}

	public List<User> getRecommendedUsers() {
		return recommendedUsers;
	}

	public boolean isExistingUser() {
		return selectedUser != null;
	}
}
