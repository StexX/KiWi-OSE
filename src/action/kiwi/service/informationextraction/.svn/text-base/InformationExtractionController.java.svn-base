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
package kiwi.service.informationextraction;

import java.util.Collection;

import kiwi.api.event.KiWiEvents;
import kiwi.api.informationextraction.InformationExtractionService;
import kiwi.context.CurrentUserFactory;
import kiwi.model.content.ContentItem;
import kiwi.model.informationextraction.ClassifierEntity;
import kiwi.model.tagging.Tag;
import kiwi.model.user.User;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.core.Events;
import org.jboss.seam.security.Identity;

@Name("kiwi.informationextraction.informationExtractionController")
@Scope(ScopeType.STATELESS)
public class InformationExtractionController {
	
//	@Transactional
	@Asynchronous
	@Observer(value=KiWiEvents.CONTENT_UPDATED+"AsyncIE")
	public void onContentUpdatedAsyncIE(ContentItem item, User currentUser, Identity identity) {
		CurrentUserFactory currentUserFactory = 
			(CurrentUserFactory) Component.getInstance("currentUserFactory");
		currentUserFactory.setCurrentUser(currentUser);
		
		InformationExtractionService service = (InformationExtractionService)Component.getInstance("kiwi.informationextraction.informationExtractionService");
		
		if (service.getOnline()) {
			service.extractInformationAsync(item);
		}
	}
	
//	@Transactional
	@Asynchronous
	@Observer(value=KiWiEvents.ACTIVITY_ADDTAG+"AsyncIE")
	public void onAddTagAsyncIE(User taggingUser, Tag tag, User currentUser, Identity identity) {
		/*
		CurrentUserFactory currentUserFactory = 
			(CurrentUserFactory) Component.getInstance("currentUserFactory");
		currentUserFactory.setCurrentUser(currentUser);
		
		InformationExtractionService service = (InformationExtractionService)Component.getInstance("kiwi.informationextraction.informationExtractionService");
		service.createPositiveExampleForTagging(tag);
		
		if (tag.getTaggingResource() == null) {
			return;
		}
			
		Collection<ClassifierEntity> classifiers = service.getClassifiersForResource(tag.getTaggingResource().getResource());
		for (ClassifierEntity classifier : classifiers) {
			service.trainAndSuggest(classifier);
		}*/
	}
	
//	@Transactional
	@Asynchronous
	@Observer(value=KiWiEvents.ACTIVITY_REMOVETAG+"AsyncIE")
	public void onRemoveTagAsyncIE(User taggingUser, Tag tag, User currentUser, Identity identity) {
		/*
		CurrentUserFactory currentUserFactory = 
			(CurrentUserFactory) Component.getInstance("currentUserFactory");
		currentUserFactory.setCurrentUser(currentUser);
		
		InformationExtractionService service = (InformationExtractionService)Component.getInstance("kiwi.informationextraction.informationExtractionService");
		service.createNegativeExampleForTagging(tag);
		
		if (tag.getTaggingResource() == null) {
			return;
		}
		
		Collection<ClassifierEntity> classifiers = service.getClassifiersForResource(tag.getTaggingResource().getResource());
		for (ClassifierEntity classifier : classifiers) {
			service.trainAndSuggest(classifier);
		}*/
	}
	
	
	@Observer(value=KiWiEvents.CONTENT_UPDATED)
	public void onContentUpdated(ContentItem item) {
		User currentUser = (User) Component.getInstance("currentUser");
		Events.instance().raiseAsynchronousEvent(KiWiEvents.CONTENT_UPDATED+"AsyncIE", item, currentUser, Identity.instance());
	}
	
	@Observer(value=KiWiEvents.ACTIVITY_ADDTAG)
	public void onAddTag(User taggingUser, Tag tag) {
		User currentUser = (User) Component.getInstance("currentUser");
		Events.instance().raiseAsynchronousEvent(KiWiEvents.ACTIVITY_ADDTAG+"AsyncIE", taggingUser, tag, currentUser, Identity.instance());
	}
	
	@Observer(value=KiWiEvents.ACTIVITY_REMOVETAG)
	public void onRemoveTag(User taggingUser, Tag tag) {
		User currentUser = (User) Component.getInstance("currentUser");
		Events.instance().raiseAsynchronousEvent(KiWiEvents.ACTIVITY_REMOVETAG+"AsyncIE", taggingUser, tag, currentUser, Identity.instance());
	}
	
	// kick the task processing up
	@Observer(KiWiEvents.SEAM_POSTINIT)
	public void runTasks() throws Exception {
		InformationExtractionService service = (InformationExtractionService)Component.getInstance("kiwi.informationextraction.informationExtractionService");
		service.runTasks(1000L);
	}
}
