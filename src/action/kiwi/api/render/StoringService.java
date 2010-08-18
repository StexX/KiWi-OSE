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
package kiwi.api.render;

import kiwi.model.content.MediaContent;
import kiwi.model.content.TextContent;
import kiwi.model.kbase.KiWiResource;

/**
 * The storing pipeline provides functionality that is executed when storing a content item. For example,
 * it may be used to bring the content in a normalised, cleaned form or for extracting information out of the
 * content. Note that the StoringPipeline does not take care of the actual storing itself; this is done by the
 * KiWiKnowledgeStore. 
 * 
 * 
 * @author Sebastian Schaffert
 *
 */
public interface StoringService {

	public void initialise(String dummyString);
	
	public void shutdown();

	/**
     * Process savelets operating on the raw HTML source, e.g. for running regular expressions over
     * the content. Typically, this method is called *before* actually creating the TextContent object and
     * calling processTextContent.
     * 
     * @param context
     * @param content
     * @return
     */
	public String processHtmlSource(KiWiResource context, String content);
	
    /**
     * Process savelets operating on TextContent objects. These savelets may e.g. operate on the XOM representation
     * of the content using XPath or on the relations of the ContentItem. Typically called after processHtmlSource
     * 
     * @param context
     * @param content
     * @return
     */
	public TextContent processTextContent(KiWiResource context, TextContent content);
	
    /**
     * Process savelets operating on MediaContent objects, e.g. for extracting or adding metadata to the
     * media object.
     * 
     * @param context
     * @param content
     * @return
     */
	public MediaContent processMediaContent(KiWiResource context, MediaContent content);
}
