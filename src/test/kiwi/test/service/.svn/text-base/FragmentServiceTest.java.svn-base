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

package kiwi.test.service;

import java.util.Collection;

import kiwi.api.content.ContentItemService;
import kiwi.api.fragment.FragmentFacade;
import kiwi.api.fragment.FragmentService;
import kiwi.api.tagging.TaggingService;
import kiwi.api.user.UserService;
import kiwi.exception.TextContentNotChangedException;
import kiwi.exception.UserExistsException;
import kiwi.model.content.ContentItem;
import kiwi.model.tagging.Tag;
import kiwi.model.user.User;
import kiwi.test.base.KiWiTest;

import nu.xom.Nodes;

import org.jboss.seam.Component;
import org.jboss.seam.security.Identity;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Marek Schmidt
 * 
 * Test basic fragment service features and fragmentsavelet.
 * 
 */
@Test
public final class FragmentServiceTest extends KiWiTest {

	@Test
	public void testFragmentSavelet() throws Exception {
		clearDatabase();

		new FacesRequest() {

			@Override
			protected void invokeApplication() {

				Identity.setSecurityEnabled(false);

				try {
					ContentItemService ciService = (ContentItemService) Component
							.getInstance("contentItemService");
					TaggingService taggingService = (TaggingService) Component
							.getInstance("taggingService");
					UserService userService = (UserService) Component
							.getInstance("userService");
					FragmentService fragmentService = (FragmentService) Component
							.getInstance("fragmentService");

					ContentItem ci = ciService.createContentItem();
					ciService.updateTitle(ci, "ci");

					ContentItem tag = ciService.createContentItem();
					ciService.updateTitle(tag, "tag");

					FragmentFacade ff = fragmentService.createFragment(ci, "uri::http://www.example.com/test",
							FragmentFacade.class);
					ciService.updateTitle(ff, "fragment");

					User user = userService.createUser("jrandom", "Random",
							"Hacker", "1337pw");
					
					ff.setAuthor(user);
					

					/*
					 * km.persist(ci); km.persist(tag);
					 * km.persist(ff.getDelegate()); km.persist(user);
					 */

					ciService.saveContentItem(ci);
					ciService.saveContentItem(tag);
					fragmentService.saveFragment(ff);
//					userService.saveUser(user);

					Tag t = taggingService.createTagging("mytag", ff
							.getDelegate(), tag, user);
					Assert.assertEquals(t.getTaggedResource(), ff.getDelegate());

				} catch (UserExistsException ex) {
					Assert.fail("user already exists");
				}

			}
		}.run();

		new FacesRequest() {

			@Override
			protected void invokeApplication() {

				Identity.setSecurityEnabled(false);

				ContentItemService ciService = (ContentItemService) Component
						.getInstance("contentItemService");
				FragmentService fragmentService = (FragmentService) Component
						.getInstance("fragmentService");

				ContentItem ci = ciService.getContentItemByTitle("ci");
				ContentItem tag = ciService.getContentItemByTitle("tag");

				Assert.assertNotNull(ci);
				Assert.assertNotNull(tag);

				FragmentFacade ff = fragmentService.getContentItemFragment(ci,
						"uri::http://www.example.com/test", FragmentFacade.class);
				Assert.assertNotNull(ff);
				Assert.assertEquals(ff.getTitle(), "fragment");

					ciService
							.updateTextContentItem(ci,
									"<div>Hello, [[BookmarkStart:uri::http://www.example.com/test]]tagged text[[BookmarkEnd:uri::http://www.example.com/test]]</div>");
			}
		}.run();

		new FacesRequest() {

			@Override
			protected void invokeApplication() {

				Identity.setSecurityEnabled(false);

				ContentItemService ciService = (ContentItemService) Component
						.getInstance("contentItemService");
				FragmentService fragmentService = (FragmentService) Component
						.getInstance("fragmentService");

				ContentItem ci = ciService.getContentItemByTitle("ci");

				FragmentFacade ff = fragmentService.getContentItemFragment(ci,
						"uri::http://www.example.com/test", FragmentFacade.class);
				Assert.assertNotNull(ff);
				Assert.assertEquals(ff.getTitle(), "fragment");

				StringBuilder sb = new StringBuilder();
				Assert.assertNotNull(ff.getTextContent());
				Assert.assertNotNull(ff.getTextContent().getXmlDocument());
				Nodes ns = ff.getTextContent().getXmlDocument().query(
						"//text()");

				for (int i = 0; i < ns.size(); ++i) {
					sb.append(ns.get(i).getValue());
				}

				Assert.assertEquals(sb.toString(), "tagged text");
				Assert.assertEquals(ff.getContainingContentItem(), ci);

				Collection<FragmentFacade> ffs = fragmentService
						.getContentItemFragments(ci, FragmentFacade.class);
				Assert.assertEquals(ffs.size(), 1);
				FragmentFacade ffs0 = ffs.iterator().next();

				Assert.assertEquals(ffs0.getDelegate(), ff.getDelegate());
			}
		}.run();

	}
}
