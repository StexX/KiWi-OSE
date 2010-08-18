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
 * sschaffe
 * 
 */
package kiwi.api.perspectives;

import java.util.List;

import kiwi.model.content.ContentItem;
import kiwi.model.perspective.Perspective;

/**
 * The PerspectiveService provides backing services for view and editor perspectives.
 *
 * @author Sebastian Schaffert
 *
 */
public interface PerspectiveService {

	/**
	 * Retrieve a perspective by name and return it. Returns null if the perspective does not exist.
	 * @return
	 */
	public Perspective getPerspective(String name);
	
	/**
	 * Add a new perspective to the system. The perspective must have a unique name.
	 * @param perspective
	 */
	public void addPerspective(Perspective perspective);

	
	/**
	 * Update the existing perspective given as parameter. Does nothing if perspective does not exist.
	 * <p>
	 * Note that the Perspective entity is read only; updates need to be performed using HQL
	 * 
	 * @param perspective
	 */
	public void updatePerspective(Perspective perspective);
	
	
	/**
	 * Remove the existing perspective given as parameter. Does nothing if perspective does not exist.
	 * <p>
	 * Note that the Perspective entity is read only; updates need to be performed using HQL
	 * 
	 * @param perspective
	 */
	public void removePerspective(Perspective perspective);
	
	
	/**
	 * Return the system default perspective used as standard when creating new content items.
	 * @return
	 */
	public Perspective getDefaultPerspective();
	
	/**
	 * Return a list of all perspectives.
	 * 
	 * @return
	 */
	public List<Perspective> listPerspectives();
	
	/**
	 * Attach the perspective given as second parameter to the content item given as first parameter if
	 * it is not already there. In case the perspective is already attached, nothing happens.
	 * @param ci
	 * @param perspective
	 */
	public void attachPerspective(ContentItem ci, Perspective perspective);
}
