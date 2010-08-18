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
package info.kwarc.swim.api.transform;

import java.util.Map;

import kiwi.model.kbase.KiWiResource;
import nu.xom.Document;

/**
 * This service transforms documents using XSLT
 * 
 * @author christoph.lange
 *
 */
public interface TransformingService {
	/**
	 * transforms mathematical annotations in the HTML from TinyMCE to OMDoc and other mathematical markup languages
	 */
	public final static String HTML2OMDOC = "html2omdoc.xsl";
	/**
	 * transforms OMDoc and other mathematical markup to annotations that the TinyMCE HTML editor can deal with
	 */
	public final static String OMDOC2HTML = "omdoc2html.xsl";
	
	public void initialise();
	
	public void shutdown();
	
	/**
	 * Transforms a document (usually the document associated with the resource <code>context</code>) with the given XSLT stylesheet
	 * 
	 * @param context the resource that gives the context for this transformation (and usually holds the document)
	 * @param document the document to be transformed
	 * @param stylesheet the XSLT stylesheet (relative filename) to be applied
	 * @return the document that results from the transformation (if the transformation succeeds)
	 */
	public Document transform(KiWiResource context, Document document, String stylesheet);

	/**
	 * Transforms a document (usually the document associated with the resource <code>context</code>) with the given XSLT stylesheet
	 * 
	 * @param context the resource that gives the context for this transformation (and usually holds the document)
	 * @param document the document to be transformed
	 * @param stylesheet the XSLT stylesheet (relative filename) to be applied
	 * @param parameters any parameters to be passed into the stylesheet
	 * @return the document that results from the transformation (if the transformation succeeds)
	 */
	public Document transform(KiWiResource context, Document document, String stylesheet, Map<String, Object> parameters);

}
