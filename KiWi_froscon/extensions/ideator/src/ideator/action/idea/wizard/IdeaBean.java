/*
 * File : IdeaController.java.java
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
package ideator.action.idea.wizard;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;

import kiwi.model.content.ContentItem;
import kiwi.model.user.User;

import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;
import org.richfaces.model.UploadItem;

/**
 * @author Rolf Sint
 * @version 0.7
 * @since 0.7
 *
 */
@Name("ideator.ideaBean")
@Scope(ScopeType.CONVERSATION)
//@Transactional
public class IdeaBean {
	
	@Logger
	private static Log log;	
	
	private String title;
	private String desc, isDesc, shouldDesc;
	private boolean anonymous;
	private LinkedList<ContentItem> mediaContents;
	private LinkedList<CoAuthorTmp> coAuthors;
	private java.util.LinkedList<UploadItem> upload;
	
	private User author;

	@Begin(join=true)
	@Create
	public void init(){
		upload = new java.util.LinkedList<UploadItem>();
		coAuthors = new LinkedList<CoAuthorTmp>();
	}
	

	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getIsDesc() {
		return isDesc;
	}
	public void setIsDesc(String isDesc) {
		this.isDesc = isDesc;
	}
	public String getShouldDesc() {
		return shouldDesc;
	}
	public void setShouldDesc(String shouldDesc) {
		this.shouldDesc = shouldDesc;
	}
	public boolean isAnonymous() {
		return anonymous;
	}
	public void setAnonymous(boolean anonymous) {
		this.anonymous = anonymous;
	}
	public List<ContentItem> getMediaContents() {
		return mediaContents;
	}
	public void setMediaContents(LinkedList<ContentItem> mediaContents) {
		this.mediaContents = mediaContents;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public java.util.LinkedList<UploadItem> getUpload() {
		return upload;
	}

	public void setUpload(java.util.LinkedList<UploadItem> upload) {
		this.upload = upload;
	}


	public LinkedList<CoAuthorTmp> getCoAuthors() {
		return coAuthors;
	}

	public void setCoAuthors(LinkedList<CoAuthorTmp> coAuthors) {
		this.coAuthors = coAuthors;
	}
	
	public User getAuthor() {
		return author;
	}


	public void setAuthor(User author) {
		this.author = author;
	}


}
