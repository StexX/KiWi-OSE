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

/**
 * @author Karsten Jahn
 *
 */
public class TemplateData {
	private String typeID;
	private String typeName;
	private String dataName;
	private FieldData[] fields;
	private FieldBracket[] fieldBrackets;
	
	/**
	 * empty constructor
	 */
	public TemplateData() {
		super();
	}

	/**
	 * @return the typeID
	 */
	public String getTypeID() {
		return typeID;
	}

	/**
	 * @param typeID the typeID to set
	 */
	public void setTypeID(String typeID) {
		this.typeID = typeID;
	}

	/**
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * @param typeName the typeName to set
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * @return the dataName
	 */
	public String getDataName() {
		return dataName;
	}

	/**
	 * @param dataName the dataName to set
	 */
	public void setDataName(String dataName) {
		this.dataName = dataName;
	}

	/**
	 * @return the field
	 */
	public FieldData[] getFields() {
		return fields;
	}

	/**
	 * @param field the field to set
	 */
	public void setFields(FieldData[] fields) {
		this.fields = fields;
	}

	/**
	 * @return the fieldSet
	 */
	public FieldBracket[] getFieldBrackets() {
		return fieldBrackets;
	}

	/**
	 * @param fieldSet the fieldSet to set
	 */
	public void setFieldBrackets(FieldBracket[] fieldBrackets) {
		this.fieldBrackets = fieldBrackets;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("TemplateData [");
		if (typeName != null) {
			sb.append("Type: " + typeName);
			sb.append("; ");
		}
		if (typeID != null) {
			sb.append("ID: " + typeID);
			sb.append("; ");
		}
		if (dataName != null) {
			sb.append("Name: " + dataName);
			sb.append("; ");
		}
		if (fields != null) {
			sb.append("[");
			for (FieldData field : fields) {
				sb.append(field);
			}
			sb.append("]");
			sb.append("; ");
		}
		if (fieldBrackets != null) {
			sb.append("[");
			for (FieldBracket fieldBracket : fieldBrackets) {
				sb.append(fieldBracket);
			}
			sb.append("]");
		}
		sb.append("]");
		return sb.toString();
	}
	
}
