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
package kiwi.webservice.utility;


import kiwi.exception.DataImportException;
import kiwi.webservice.DataStructured;
import kiwi.webservice.DataUnit;

/**
 * This class provides static helper methods 
 * to check whether provided data is 
 * sufficent, meaning not empty.
 * @author Karsten Jahn
 */
public class CheckData {

	/**
	 * Empty Constructor.
	 */
	public CheckData() {
		super();
	}


	/**
	 * Checks whether the source information is sufficent. 
	 * @param dataSource
	 * @throws DataImportException 
	 */
	public static void checkSourceInformation(String dataSource) throws DataImportException {
		if (dataSource == null || dataSource == "") {
			throw new DataImportException("No source information transmitted.");
		}
	}

	/**
	 * checks whether the data is sufficent.
	 * @param data
	 * @throws DataImportException
	 */
	public static void checkStructuredDataArray(DataStructured[] data) throws DataImportException {
		// check whether data has been transmitted
		if (data == null
				|| data.length < 0) {
			throw new DataImportException("No StructuredData transmitted.");
		}
		
		// check whether the array contains data
		boolean empty = true;
		for (DataStructured sd: data) {
			if (sd.getValue() != null 
					&& sd.getValue() != "") {
				empty = false;
			}
		}
		if (empty) throw new DataImportException("Transmitted StructuredData is empty.");
	}


	/**
	 * Checks whether the <code>DataRepresentation</code> is sufficent.
	 * @param DataUnit
	 * @throws DataImportException 
	 */
	public static void checkDataUnit(DataUnit data) throws DataImportException {
		// check whether data has been transmitted
		if (data == null) {
			throw new DataImportException("No DataUnit transmitted.");
		}
		
		checkStructuredDataArray(data.getData());

		// TODO check for quality of DataUnit object (empty or not)
	}


	public static void checkTemplateData(TemplateData data) throws DataImportException {
		// check the basic fields
		checkTemplateDataOnlyHeader(data);
		
		// check for actual data
		if ((data.getFields() == null || data.getFields().length == 0)
				&& (data.getFieldBrackets() == null || data.getFieldBrackets().length == 0)) {
			throw new DataImportException("No data transmitted in TemplateData.");
		}
		
	}


	public static void checkTemplateDataOnlyHeader(TemplateData data) throws DataImportException {
		// check whether data has been transmitted
		if (data == null) {
			throw new DataImportException("No TemplateData transmitted.");
		}
		
		// check for set main attributes
		if (data.getTypeID() == null || data.getTypeID().equals("")) {
			throw new DataImportException("No id transmitted in TemplateData.");
		}
		if (data.getTypeName() == null || data.getTypeName().equals("")) {
			throw new DataImportException("No type name transmitted in TemplateData.");
		}
		if (data.getDataName() == null || data.getDataName().equals("")) {
			throw new DataImportException("No data name transmitted in TemplateData.");
		}
	}
}