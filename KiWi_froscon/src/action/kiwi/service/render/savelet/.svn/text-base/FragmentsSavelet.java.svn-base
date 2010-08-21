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

package kiwi.service.render.savelet;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.ejb.Stateless;

import kiwi.api.content.ContentItemService;
import kiwi.api.fragment.FragmentFacade;
import kiwi.api.fragment.FragmentService;
import kiwi.model.content.ContentItem;
import kiwi.model.content.TextContent;
import kiwi.model.content.TextFragment;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.User;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

/**
 * This savelet updates the content of the fragments inserted 
 * into this page and removes the fragments that are no longer
 * referenced in the content.
 * 
 * @author Marek Schmidt
 *
 */
@Stateless
@AutoCreate
@Name("kiwi.service.render.savelet.FragmentsSavelet")
public class FragmentsSavelet implements TextContentSavelet {
	
	@In
	private ContentItemService contentItemService;
	
	@In
	private FragmentService fragmentService;

	@Logger
	private Log log;
	
	@In(create=true) 
	private User currentUser;
	
	/* (non-Javadoc)
	 * @see kiwi.service.render.savelet.Savelet#apply(kiwi.model.kbase.KiWiResource, java.lang.Object)
	 */
	@Override
	public TextContent apply(KiWiResource context, TextContent content) {
		ContentItem ci = context.getContentItem();
		
		Set<KiWiResource> referenced = new HashSet<KiWiResource> ();
		
		for (String fragmentId : content.getFragmentIds()) {
			FragmentFacade ff = fragmentService.getContentItemFragment(ci, fragmentId, FragmentFacade.class);
			if (ff == null) {
				// Fragments can also exist only in the text content (e.g. as suggestions), so this is valid
				log.info("ff is null for fragmentId #0", fragmentId);
				continue;
			}
			
			TextFragment fragment = content.getFragment(((KiWiUriResource)ff.getResource()));
			
			ContentItem _item = ff.getDelegate();
			
			referenced.add(_item.getResource());

			contentItemService.updateTextContentItem(_item, fragment.getFragmentTree());

			// update last author
			_item.setAuthor(currentUser);

			// update modification
			_item.setModified(new Date());

			contentItemService.saveContentItem(_item);
		}
		
		// Delete the not-referenced fragments
		for (FragmentFacade ff : fragmentService.getContentItemFragments(ci, FragmentFacade.class)) {
			if (!referenced.contains(ff.getDelegate().getResource())) {
				fragmentService.removeFragment(ff);
			}
		}
    	
		return content;
	}
}
