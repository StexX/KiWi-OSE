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
package kiwi.api.multimedia;

import java.io.File;

import kiwi.model.content.ContentItem;

/**
 * The multimedia service provides functions for working with multimedia content, e.g.
 * for determining the content type, for extracting metadata, etc.
 * 
 * @author Sebastian Schaffert
 *
 */
public interface MultimediaService {

	/**
	 * Determine the mime type of the file with the given name and content. Filename may be null, in which
	 * case the system has to solely rely on magic detection of the content, which may be slow and unreliable.
	 * 
	 * @param filename
	 * @param data
	 * @return a string representing the mime type of the content
	 */
	public String getMimeType(String filename, byte[] data);
	
	/**
	 * Determine the mime type of the file provided as argument.
	 * 
	 * @param file
	 * @return a string representing the mime type of the content
	 */
	public String getMimeType(File file);
	
	
	/**
	 * Extract RDF metadata from the media content object passed as argument. Currently works for images,
	 * for PDF documents, and for MS Word documents.
	 */
	public void extractMetadata(ContentItem item);
}
