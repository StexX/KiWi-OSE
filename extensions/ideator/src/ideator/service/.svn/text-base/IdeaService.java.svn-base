/*
 * File : IdeaService.java.java
 * Date : 26.03.2010
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

import ideator.datamodel.IdeaFacade;
import ideator.datamodel.IdeatorUserFacade;

import java.io.IOException;
import java.util.LinkedList;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.model.content.ContentItem;
import kiwi.model.user.User;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
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
@Scope(ScopeType.STATELESS)
@Name("ideaService")
//@Transactional
@AutoCreate
public class IdeaService {
	
	@In
	private ContentItemService contentItemService;
	
	@In
	private KiWiEntityManager kiwiEntityManager;
	
	@In
	private User currentUser;
	
	@Logger
	private static Log log;
	
	public void addCoauthor(IdeaFacade ideaFacade, String login){
		LinkedList<String> iuf = ideaFacade.getCoAuthors();
		iuf.add(login);
		ideaFacade.setCoAuthors(iuf);
	}
	
	public IdeaFacade createIdea(String title, String desc, String isDesc, String shouldDesc, boolean anonymous, LinkedList<UploadItem> upload){
		
		log.info("Idea #0", title);
		
		final ContentItem ideaItem = contentItemService.createContentItem();
		IdeaFacade ideaFacade = kiwiEntityManager.createFacade(ideaItem, IdeaFacade.class);

		ideaFacade.setStatus(IdeaFacade.NEW);
		
		if(title != null){
			contentItemService.updateTitle(ideaItem, title);
		}
		if(isDesc != null){
			contentItemService.updateTextContentItem(ideaItem, desc);
		}
		if(shouldDesc != null){
			ideaFacade.setShouldDesc(shouldDesc);
		}
		ideaFacade.setAnonymous(anonymous);
		
		if(upload != null){
			for(UploadItem item: upload){
				log.info("uploading file #0" + item.getFileName());
				byte[] data = item.getData();
				
				if (item.isTempFile()) {
					log.info("uploaded file is Tempfile");
					try {
						data = FileUtils.readFileToByteArray(item.getFile());
					} catch (IOException e) {
						log.error("error reading file #0", item.getFile()
								.getAbsolutePath());
					}
				}
				
				
				String name = FilenameUtils.getName(item.getFileName());
				String type = item.getContentType();
				
				ContentItem mediaContentItem = contentItemService.createMediaContentItem(data, type, name);
				mediaContentItem.setAuthor(currentUser);		
				kiwiEntityManager.persist(mediaContentItem);
				
				java.util.LinkedList<ContentItem> cl = new java.util.LinkedList<ContentItem>();
	
				cl = ideaFacade.getMediaContents();
				
				cl.add(mediaContentItem);
				
				ideaFacade.setMediaContents(cl);			
			}
		}
		kiwiEntityManager.persist(ideaFacade);
		
		return ideaFacade;
	}
	
	public void setStatus( ContentItem ci, int status ) {
		IdeaFacade idea = kiwiEntityManager.createFacade(ci, IdeaFacade.class);
		idea.setStatus(status);
		kiwiEntityManager.persist(idea);
	}
	
}
