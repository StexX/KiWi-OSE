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

package info.kwarc.swim.service.triplestore;

import info.kwarc.swim.api.triplestore.MetadataProvider;

import java.util.Collection;
import java.util.Collections;

import javax.ejb.Stateless;

import kiwi.api.triplestore.TripleStore;
import kiwi.exception.NamespaceResolvingException;
import kiwi.model.kbase.KiWiLiteral;
import kiwi.model.kbase.KiWiNode;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

/**
 * Makes the triple store accessible for <code>omdoc2html.xsl</code> in order to retrieve metadata while rendering a document.
 * 
 * @author clange
 *
 */
// @Scope(ScopeType.STATELESS)
@Stateless
@Name("metadataProvider")
@AutoCreate
public class MetadataProviderImpl implements MetadataProvider {
	@In
	private TripleStore tripleStore;

	/* (non-Javadoc)
	 * @see info.kwarc.swim.service.triplestore.MetadataProvider#getProperty(java.lang.String, java.lang.String)
	 */
	public String getProperty(String resource_uri, String property_qname) {
		// TODO this could be merged with the successor of StorageBackend.getMetadataFieldValue
		KiWiResource article = tripleStore.createUriResource(resource_uri);
		// we can't simply call article.getProperty, as that would only return literal properties
		Collection<KiWiTriple> objects;
		try {
			objects = article.listOutgoing(property_qname, 1,false);
		} catch (NamespaceResolvingException e) {
			e.printStackTrace();
			objects = Collections.emptyList();
		}
		for (KiWiTriple t: objects) {
			// this loop is only entered if objects is non-empty
			KiWiNode n = t.getObject();
			if (n instanceof KiWiLiteral) {
				return ((KiWiLiteral) n).getContent();
			} else if (n instanceof KiWiUriResource) {
				return ((KiWiUriResource) n).getUri();
			} else {
				// we don't support other types of node (i.e. KiWiAnonResource) here
				break;
			}
		}
		return "";
	}
	
}
