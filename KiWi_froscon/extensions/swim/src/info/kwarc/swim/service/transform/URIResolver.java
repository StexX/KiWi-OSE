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

package info.kwarc.swim.service.transform;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;

public class URIResolver implements javax.xml.transform.URIResolver {

    @Override
    public Source resolve(String href, String base) throws TransformerException {
//		/*
//		 * If the base is an article in the database, we resolve to the page
//		 * "href" in the database i.e. we assume that "href" is an inline
//		 * include-like link in the document "base".
//		 * 
//		 * href is assumed to have the format [prefix:]local-name
//		 */
//		// TODO what if resolvent is a fragment like page#frag?
//		try {
//			/*
//			 * FIXME rethink this. It's really unefficient if IkeWiki has to
//			 * create a pseudo namespace for every, e.g., xsl:included XSLT
//			 */
//			WikiResource resolvent = storageBackend.getResourceByUri(base);
//
//			if (resolvent != null
//					&& !resolvent.getNamespace().getPrefix().equals("EXTERN")) {
//				/*
//				 * Note that the resolvent does not need to exist (in terms of
//				 * storageBackend.existsArticle). Only its namespace must be
//				 * known to IkeWiki. E.g. when RDF is extracted from imported
//				 * articles, XIncludes in them are resolved before the article
//				 * itself is stored in the database.
//				 */
//				return getSourceFromWiki(resolvent, href);
//			}
//		} catch (DBException exc) {
//
//		}
//		return super.resolve(href, base);
    	return null;
	}

//    private Source getSourceFromWiki(WikiResource resolvent, String uri) throws TransformerException {
//        Article a;
//        Document d;
//        DOMResult dr = new DOMResult();
//        try {
//        		if (uri.contains(":")) {
//        			a = storageBackend.getArticleByQTitle(uri);
//        		}
//        		else {
//        			// log.info("trying to get " + resolvent.getNamespace().getPrefix() + ":" + uri);
//        			a = storageBackend.getArticleByNamespaceTitle(resolvent.getNamespace().getPrefix(), uri);
//        		}
//                // log.info("got article " + uri + " from storage");
//        		*
//        		 * Strange behaviour: If XSLT_EXTRACT_CONTENT does not clone (via identity.xsl) its input
//        		 * but copies using xsl:copy-of, we get this error message:
//        		 * 
//        		 * org.w3c.dom.DOMException: HIERARCHY_REQUEST_ERR: An attempt was made to insert a node
//        		 * where it is not permitted.
//        		 * 
//        		 * --Christoph Lange, 2007/01/31
//        		 *
//        		renderingPipeline.transformXML(null, a, OutputMode.ARTICLE_CONTENT, Locale.getDefault(), dr);
//        }
//        catch (DBException exc) {
//                log.error("DBException when creating source for " + uri);
//                throw new TransformerException(exc);
//        }
//        catch (RenderingException exc) {
//                log.error("RenderingException when creating source for " + uri);
//                throw new TransformerException(exc);
//        }
//
//        DOMSource ds = new DOMSource(dr.getNode());
//        ds.setSystemId(a.getUri());
//
//        return ds;
//    }
}
