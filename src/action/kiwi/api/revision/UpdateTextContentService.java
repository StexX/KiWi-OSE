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
package kiwi.api.revision;

import java.util.List;

import kiwi.model.content.ContentItemI;
import kiwi.model.content.TextContent;
import kiwi.model.revision.CIVersion;
import kiwi.model.revision.TextContentUpdate;
import kiwi.model.revision.TextContentUpdateDiff;

/**
 * UpdateTextContentService interface
 * Implementation: UpdateTextContentServiceImpl
 * @author Stephanie Stroka (sstroka@salzburgresearch.at)
 *
 */
public interface UpdateTextContentService extends KiWiUpdateServiceLocal, KiWiUpdateServiceRemote {
	
	/**
	 * contains possible styles for the preview
	 * @author Stephanie Stroka (sstroka@salzburgresearch.at)
	 *
	 */
	public enum PreviewStyle {
		LAST, ALLUPDATES, ALLAUTHORS
	}

	/**
	 * Has to be called after a modification of TextContent to get an update
	 * that is generated for revision management.
	 * When calling this method, the text content must not to be set 
	 * explicitly to the content item.
	 */
	public TextContentUpdate updateTextContent(ContentItemI item, TextContent tc);
	
	/**
	 * Creates a preview for a version
	 * @param version
	 * @return
	 */
	public String createPreview(CIVersion version, PreviewStyle style, boolean showDeleted);
	
	/**
	 * @param doc
	 * @param diff
	 * @param revisionId
	 * @param showDeleted
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public nu.xom.Document setStyleWithXOM(nu.xom.Document doc, 
			List<TextContentUpdateDiff> diff, Long revisionId, 
				boolean showDeleted) throws Exception;
	
	/**
	 * generates the diff of two xhtml strings 
	 * @param curXMLContent
	 * @param prevXMLContent
	 * @return
	 */
	public List<TextContentUpdateDiff> generateChanges( String 
			curXMLContent, String prevXMLContent );
}
