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
package kiwi.webservice;

/**
 * This class is a description of the data included in a template.
 * @author Karsten Jahn
 */
public class TemplateDescription {
	private String name;		// name of the template
	private String[] fields;	// names of the fields in the template - not ordered!
	private FieldSet[] fieldSet;	// sets of field that describe a repeatable paragraph in the template.
	
	/**
	 * Empty Constructor
	 */
	public TemplateDescription() {
		super();
	}

	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		if (name.startsWith("Template: ")) {
			this.name = name.substring(10);
		} else {
			this.name = name;
		}
	}

	/**
	 * @return the fields
	 */
	public String[] getFields() {
		return fields;
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(String[] fields) {
		this.fields = fields;
	}


	/**
	 * @return the filedSet
	 */
	public FieldSet[] getFiledSet() {
		return fieldSet;
	}


	/**
	 * @param filedSet the filedSet to set
	 */
	public void setFiledSet(FieldSet[] fieldSet) {
		this.fieldSet = fieldSet;
	}


	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Fields: ");
		for (String field : fields) {
			sb.append(field + ", ");
		}
		sb.replace(sb.length()-2, sb.length(), ".");
		
		if (fieldSet.length > 0) {
			sb.append(" FieldSets: ");
			for (FieldSet set: fieldSet) {
				sb.append("[");
				for (String setField: set.getFields()) {
					sb.append(setField + ", ");
				}
				sb.replace(sb.length()-2, sb.length(), ".");
				sb.append("], ");
			}
			sb.replace(sb.length()-2, sb.length(), ".");
		}
		return sb.toString();
	}

	
}