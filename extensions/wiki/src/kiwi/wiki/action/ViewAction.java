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

package kiwi.wiki.action;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import javax.faces.context.FacesContext;
import javax.ws.rs.PathParam;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.event.KiWiEvents;
import kiwi.api.render.RenderingService;
import kiwi.context.CurrentUserFactory;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.user.User;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.richfaces.component.UITogglePanel;

/**
 * @author Sebastian Schaffert
 * 
 */
@Name("viewAction")
@Scope(ScopeType.CONVERSATION)
// event scoped so that it is rerendered when the page is displayed again
public class ViewAction implements Serializable {

	private static final long serialVersionUID = 5668835011461415359L;
	
	@Logger
	private static Log log;

	@In(create = true)
	private User currentUser;

	@In(create = true)
	private ContentItem currentContentItem;

	@In
	private ContentItemService contentItemService;

    @In
    private KiWiEntityManager kiwiEntityManager;

	private String currentContentHtml;
	private String description;
	
	private String htmlRef;
	
	@In
	private RenderingService renderingPipeline;
	
	public void begin() {
		description = renderingPipeline.renderEditor(currentContentItem, currentUser);
	}

	/**
	 * @return the currentContentHtml
	 */
//	public String getCurrentContentHtml() {
//		if(currentContentHtml == null) {
//			log.debug(currentContentItem.getTitle());
//			
//			
//			if (currentContentItem.getTextContent() != null) {
//				currentContentHtml = renderingPipeline.renderHTML(currentContentItem);
//				log.debug("rendering html content: #0", currentContentHtml);
//			} else {
//				currentContentHtml = "<p>Please add initial content</p>";
//			}
//		}
//		return currentContentHtml;
//	}

	// For small description editors like the one on the imageTemplate.xhtml
	public void saveDescription(){
    	contentItemService.updateTextContentItem(currentContentItem, description);
		switchDescToView();
		kiwiEntityManager.persist(currentContentItem);
	}
	public void cancelSaveDescription(){
		switchDescToView();
	}
	private void switchDescToView(){
		UITogglePanel descriptionTogglePanel=(UITogglePanel) 
		FacesContext.getCurrentInstance().getViewRoot().findComponent("formWikiContent:DescriptionEditor");
		if(descriptionTogglePanel!=null)
		descriptionTogglePanel.setValue("descView");

	}

	public String getDescription(){
		return description;
	}
	
	public void setDescription(String desc){
		description = desc;
	}
	
	/**
	 * @param currentContentHtml
	 *            the currentContentHtml to set
	 */
	public void setCurrentContentHtml(String currentContentHtml) {
		this.currentContentHtml = currentContentHtml;
	}

	public String getHtmlRef() {
		return htmlRef;
	}

	public void setHtmlRef(String htmlRef) {
		this.htmlRef = htmlRef;
	}

	@Observer(value={KiWiEvents.CONTENT_UPDATED, KiWiEvents.METADATA_UPDATED},create=false)
	public void clear(ContentItem item) {
		this.currentContentHtml = null;
	}
	
	public String getCurrentContentHtml() {
		
		if (currentContentItem.getTextContent() != null) {
			currentContentHtml = renderingPipeline
					.renderHTML(currentContentItem, currentUser);
		}  else {
			currentContentHtml = "<p>Please add initial content</p>";
		}

		// hackish workaround for Facelets-Bug (331), fixes KIWI-528
//		currentContentHtml = currentContentHtml.replaceAll("\\s<", "#{' '}<");

		// strip out all kiwi xml namespace declarations, workaround for
		// KIWI-540 / Facelets-350
		// currentContentHtml =
		// currentContentHtml.replaceAll("xmlns(:[a-zA-Z]+)?=\"[^\"]+\"", "");
		currentContentHtml = currentContentHtml.replaceAll(
				"xmlns:kiwi=\"[^\"]+\"", "");

		// fixes KIWI-500, prevents currentContentHtml from being surrounded
		// with any <html>...</html>
		String result = "<html><head></head><body><ui:composition "
				+ "xmlns:ui=\"http://java.sun.com/jsf/facelets\" "
				+ "xmlns:kiwi=\"" + Constants.NS_KIWI_HTML + "\" " + "xmlns=\""
				+ Constants.NS_XHTML + "\">\n" + currentContentHtml
				+ "</ui:composition></body></html>\n";

		// // line wrapping; needed so that RESTEasy does not break

		log.debug("getHtml: #0", result);

		return currentContentHtml;
	}
}
