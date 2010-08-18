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

package kiwi.test.service.render.renderlet;

import kiwi.api.content.ContentItemService;
import kiwi.api.render.RenderingService;
import kiwi.exception.NamespaceResolvingException;
import kiwi.exception.NonUniqueRelationException;
import kiwi.model.content.ContentItem;
import kiwi.model.user.User;
import kiwi.test.base.KiWiTest;

import org.jboss.seam.Component;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.jboss.seam.security.Identity;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Marek Schmidt
 *
 * Test RDFa related renderlets and savelets
 *
 */
@Test
public final class RdfaRenderletTest extends KiWiTest {

	@Test
	public void testRdfaRenderlet() throws Exception {
		clearDatabase();

		new FacesRequest() {

			@Override
			protected void invokeApplication() {

				Identity.setSecurityEnabled(false);

				final ContentItemService ciService = (ContentItemService) Component
						.getInstance("contentItemService");

				final ContentItem property = ciService.createExternContentItem("http://example.com/property");
				ciService.updateTitle(property, "someproperty");

				final ContentItem ci = ciService.createContentItem();
            	ciService.updateTitle(ci, "ci");

				ciService.saveContentItem(property);
				ciService.saveContentItem(ci);
			}
		}.run();

		new FacesRequest() {

			@Override
			protected void invokeApplication() {

				Identity.setSecurityEnabled(false);

				final ContentItemService ciService = (ContentItemService) Component
						.getInstance("contentItemService");

				final ContentItem ci = ciService.getContentItemByTitle("ci");
				final ContentItem property = ciService.getContentItemByTitle("someproperty");

				Assert.assertNotNull(ci);
				Assert.assertNotNull(property);

				ciService.updateTextContentItem(ci,
					"<div>The value of a property shall be <span property=\"uri::http://example.com/property\">xyzzy</span></div>");

				ciService.saveContentItem(ci);
			}
		}.run();

		new FacesRequest() {

			@Override
			protected void invokeApplication() {

				Identity.setSecurityEnabled(false);

				final ContentItemService ciService = (ContentItemService) Component
						.getInstance("contentItemService");

				final ContentItem ci = ciService.getContentItemByTitle("ci");
				final ContentItem property = ciService.getContentItemByTitle("someproperty");

				Assert.assertNotNull(ci);
				Assert.assertNotNull(ci.getTextContent());
				Assert.assertNotNull(property);

				try {
					final String value = ci.getResource().getProperty(property.getResource().getSeRQLID());
					Assert.assertEquals(value, "xyzzy");
				} catch (final NonUniqueRelationException e) {
					Assert.fail("it should be unique!");
				}

				try {
					ci.getResource().setProperty(property.getResource().getSeRQLID(), "foobar");
				} catch (final NamespaceResolvingException e) {
					Assert.fail("no namespace resolving problems should occur!");
				}

				ciService.saveContentItem(ci);
			}
		}.run();

		new FacesRequest() {

			@Override
			protected void invokeApplication() {

				Identity.setSecurityEnabled(false);

				final Log log = Logging.getLog(this.getClass());

				final ContentItemService ciService = (ContentItemService) Component
						.getInstance("contentItemService");
				final RenderingService rService = (RenderingService) Component.getInstance("renderingPipeline");

				final ContentItem ci = ciService.getContentItemByTitle("ci");
				final ContentItem property = ciService.getContentItemByTitle("someproperty");

				Assert.assertNotNull(ci);
				Assert.assertNotNull(ci.getTextContent());
				Assert.assertNotNull(property);

				final String html = rService.renderHTML(ci);
				log.info("html:\n"+html);
				Assert.assertEquals(html.contains("<span property=\"http://example.com/property\">foobar</span>"), true);

				try {
					final String value = ci.getResource().getProperty(property.getResource().getSeRQLID());
					Assert.assertEquals(value, "foobar");
				} catch (final NonUniqueRelationException e) {
					Assert.fail("it should be unique!");
				}

				User currentUser = (User) Component.getInstance("currentUser");
				final String editor = rService.renderEditor(ci, currentUser);
				log.info("editor:\n"+editor);
				Assert.assertEquals(editor.contains("<span property=\"uri::http://example.com/property\">foobar</span>"), true);

				ciService.updateTextContentItem(ci, editor);

				ciService.saveContentItem(ci);
			}
		}.run();

		new FacesRequest() {

			@Override
			protected void invokeApplication() {
				Identity.setSecurityEnabled(false);

				final ContentItemService ciService = (ContentItemService) Component
						.getInstance("contentItemService");

				final ContentItem ci = ciService.getContentItemByTitle("ci");
				final ContentItem property = ciService.getContentItemByTitle("someproperty");

				Assert.assertNotNull(ci);
				Assert.assertNotNull(ci.getTextContent());
				Assert.assertNotNull(property);

				try {
					final String value = ci.getResource().getProperty(property.getResource().getSeRQLID());
					Assert.assertEquals(value, "foobar");
				} catch (final NonUniqueRelationException e) {
					Assert.fail("it should be unique!");
				}
			}
		}.run();
	}
}
