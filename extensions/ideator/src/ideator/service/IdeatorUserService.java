/*
 * File : IdeatorUserService.java.java
 * Date : 23.03.2010
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
package ideator.service;

import ideator.datamodel.IdeatorUserFacade;

import java.util.LinkedList;
import java.util.Map;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.user.UserService;
import kiwi.exception.RegisterException;
import kiwi.exception.UserExistsException;
import kiwi.model.ontology.SKOSConcept;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;

/**
 * @author Rolf Sint
 * @version 0.7
 * @since 0.7
 *
 */
@Name("ideator.userService")
@Scope(ScopeType.STATELESS)
@AutoCreate
//@Transactional
public class IdeatorUserService {

	@In
    protected UserService userService;
	
	@In
	private ContentItemService contentItemService;
	
	@In
	private KiWiEntityManager kiwiEntityManager;
	
	@In
	protected Map<String, String> messages;
	
	public IdeatorUserFacade createUser(String login, String firstName, String lastName, String password, String email, LinkedList<SKOSConcept> positions) throws UserExistsException, RegisterException {

		if( positions != null && positions.size() > 0 ) {
			User user = userService.createUser(login, firstName, lastName, password);
			user.setEmail(email);
			final IdeatorUserFacade userFacade = kiwiEntityManager.createFacade(user.getContentItem(), IdeatorUserFacade.class);
			userFacade.setPositions(positions);
			kiwiEntityManager.persist(userFacade);
			contentItemService.updateTitle(userFacade.getDelegate(), firstName+" "+lastName);
			return userFacade;
		} else {
			throw new RegisterException(messages.get("ideator.error.position"));
		}
	}
	
	public IdeatorUserFacade createUser(User user){
	    final IdeatorUserFacade userFacade = kiwiEntityManager.createFacade(user.getContentItem(), IdeatorUserFacade.class);
	    kiwiEntityManager.persist(userFacade);
	    contentItemService.updateTitle(userFacade.getDelegate(), user.getFirstName()+" "+user.getLastName());
	    return userFacade;
	}
	
	public IdeatorUserFacade getUser( User user ) {
		return kiwiEntityManager.createFacade(user.getContentItem(), IdeatorUserFacade.class);
	}

}
