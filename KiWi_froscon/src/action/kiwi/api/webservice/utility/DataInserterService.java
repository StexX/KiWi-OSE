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
package kiwi.api.webservice.utility;

import kiwi.exception.DataImportException;
import kiwi.webservice.DataSpecialFormat;
import kiwi.webservice.DataUnit;
import kiwi.webservice.utility.TemplateData;

/**
 * This interface specifies the functionality for importing data from the LUC into KiWi.
 * Maybe it would be a good idea to outsource this code into another extension.
 * 
 * @author Karsten Jahn
 *
 */
public interface DataInserterService {
	
	/**
	 * Creates template pages.
	 * @return true if operation was successful.
	 */
	public boolean createTemplates();

	/**
	 * Creates a new content item by copying a template.
	 * @return a string representing the id of the created content item.
	 * @throws DataImportException 
	 */
	public String publish(String type, String name, DataUnit data, String source) throws DataImportException;

	public String publishQND(String type, String name, DataSpecialFormat data,
			String source);

	/**
	 * Publishes data in a page with detailled information based on a template. In this place, we assume that data is already been checked and it contains data. Meaning: at least one field is included (single or in a set) and the 3 main fields are set.
	 * @param data - The data to be represented. Only placeholders in the template will be substituted, no information that is not in the template, will be published. If data is not present but placeholders exist, a default value will be represented.
	 * @param source - A <code>String</code> that represents the client.
	 * @return A <code>String</code> that represents the KiWi Identifier of the created content item. 
	 * @throws DataImportException 
	 */
	public String publishData(TemplateData data, String source) throws DataImportException;

	/**
	 * Creates a structure for a work page with specific data. Basically like a template, but mostly a list of headlines.
	 * @param data - The data to be represented. Only placeholders in the template will be substituted, no information that is not in the template, will be published. If data is not present but placeholders exist, a default value will be represented.
	 * @param source - A <code>String</code> that represents the client. 
	 * @return A <code>String</code> that represents the KiWi Identifier of the created content item. 
	 */
	public String createStructurePage(TemplateData data, String source);

}