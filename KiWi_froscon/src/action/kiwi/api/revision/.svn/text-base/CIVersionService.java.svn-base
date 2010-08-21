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

import kiwi.api.revision.UpdateTextContentService.PreviewStyle;
import kiwi.exception.ContentItemDoesNotExistException;
import kiwi.exception.VersionDoesNotExistException;
import kiwi.model.content.ContentItem;
import kiwi.model.revision.CIVersion;

/**
 * Interface for ContentItem Version Service. Specifies creation, 
 * restoring, undo, commit and rollback methods for CIVersions 
 * and it's containing Updates
 * 
 * @author Stephanie Stroka 
 * 			(stephanie.stroka@salzburgresearch.at)
 *
 */
public interface CIVersionService {

	public void initialise();
	
	/**
	 * creates contentitem version and adds updates
	 * @param _tcus
	 * @param _mcus
	 * @param _mdus
	 * @param _dus
	 * @param _reus
	 * @param _ruls
	 * @param _tus
	 * @param revisedContentItem
	 * @return
	 */
	public CIVersion createCIVersion(ContentItem revisedContentItem);
	
	/**
	 * restores the contentitem version
	 * @param rev
	 * @return
	 */
	public boolean restore(CIVersion rev);
	
	/**
	 * commits updates done in this contentitem version
	 * @param rev
	 * @return
	 */
	public boolean commit(CIVersion rev);
	
	/**
	 * rollback changes done in that contentitem version
	 * @param rev
	 * @return
	 */
	public boolean rollback(CIVersion rev);
	
	/**
	 * creates a textcontentpreview for the contentitem version
	 * @param rev
	 * @param style
	 * @param showDeleted
	 * @return
	 * @throws VersionDoesNotExistException 
	 * @throws ContentItemDoesNotExistException
	 */
	public String createPreview(CIVersion rev, PreviewStyle style, 
			boolean showDeleted) throws VersionDoesNotExistException, 
			ContentItemDoesNotExistException;

	/**
	 * 
	 * @param v
	 * @return
	 */
	public boolean undo(CIVersion v);
}
