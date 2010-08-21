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
package kiwi.api.informationextraction;

import java.util.List;

import kiwi.model.content.ContentItem;
import kiwi.model.informationextraction.Label;
import kiwi.model.kbase.KiWiResource;

/**
 * 
 * The label service and its model (Label entity) maintains alternative labels 
 * of resources. It is used by the information extraction services as 
 * a gazetteer and for matching interesting terms to concepts.
 * 
 * @author Marek Schmidt
 *
 */
public interface LabelService {
	
	/**
	 * Initialize the labels of the current content items.
	 */
	public void initLabels();
	
	/**
	 * Retrive all the labels and their resource (String label, Long resource_id) 
	 * @return List of pairs (String label, Long resource_id)
	 */
	public List<Object[]> getLabels();
	
	/**
	 * Find all resources matching the specified label.
	 * @param label
	 * @return 
	 */
	public List<KiWiResource> matchResources(String label);
	
	/**
	 * Get the list of label entities based on the current state of the Content Item. This method does not persist the labels.
	 * @param ci
	 * @return
	 */
	public List<Label> getLabels(ContentItem ci);

	/**
	 * Creates the label entities for a particular content item.
	 * @param ci
	 * @return
	 */
	public void createLabels(ContentItem ci);

	public void onTitleEvent(ContentItem item);

	public void onTitleUpdated(ContentItem ci);
}
