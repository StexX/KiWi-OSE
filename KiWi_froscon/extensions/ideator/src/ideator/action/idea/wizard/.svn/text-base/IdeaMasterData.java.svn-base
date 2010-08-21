/*
 * File : IdeaBasicDataAction.java.java
 * Date : 30.03.2010
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

import java.util.LinkedList;

import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

/**
 * @author Rolf Sint
 * @version 0.7
 * @since 0.7
 *
 */
@Name("ideator.ideaMasterData")
@Scope(ScopeType.CONVERSATION)
@AutoCreate
//@Transactional
public class IdeaMasterData {
		
	@In
	public IdeaBean ideaBean;
	
	public int uploadsAvailable = 5;
	
	@In
	private User currentUser;
	
	@In(value = "ideator.ideaWizardFinnish", create=true)
	public IdeaWizardFinnish ideaWizardFinnish;
	
	@Logger
	private static Log log;	
	
	@org.jboss.seam.annotations.Create
	public void init(){
		ideaBean.setAuthor(currentUser);
	}
	
	@Begin(join=true)
	public void listener(UploadEvent event) {
		UploadItem item = event.getUploadItem();
		log.info("File: '#0' with type '#1' will be uploaded", item.getFileName(),
				item.getContentType());
		
		LinkedList<UploadItem> lu = ideaBean.getUpload();
		lu.add(item);
		
		ideaBean.setUpload(lu);
	}
		
	public int getUploadsAvailable() {
		return uploadsAvailable;
	}
	
	public void setUploadsAvailable(int uploadsAvailable) {
		this.uploadsAvailable = uploadsAvailable;
	}
		
	@End
	public String cancel(){
		return "home";
	}
	
	@End
	public String finnish(){
		ideaWizardFinnish.storeIdea();
		return "idea";
	}
	
	@Begin(nested=true)
	public String coAuthorsPage(){
		return "coAuthorsPage";
	}
	
	@Begin(nested=true)
	public String selectCategories(){
		return "selectCategories";
	}
}
