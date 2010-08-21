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
package kiwi.api.query.kwql;

import java.util.regex.Pattern;

import kiwi.api.fragment.FragmentFacade;
import kiwi.model.Constants;
import kiwi.util.MD5;

public enum KwqlLinkQualifier {
	target("u_" + MD5.md5sum(Constants.NS_KIWI_CORE + "internalLink"), KwqlQualifierType.axis),
	origin(KwqlQualifierType.unsupported),
	tag(KwqlQualifierType.resource),
	anchorText(KwqlQualifierType.unsupported);
	
	/** Indicates whether this item is a real qualifier or just some kind of dummy qualifier */
	KwqlQualifierType type;
	
	/** The name of the corresponding Solr field */
	String fieldName;
	
	/** A regular expressed which checks whether the data type of a value matches the required data type of this qualifier */
	Pattern pattern;
	
	private KwqlLinkQualifier() {
		this(KwqlQualifierType.value);
	}
	
	private KwqlLinkQualifier(KwqlQualifierType type) {
		this.type = type;
	}
	
	private KwqlLinkQualifier(String fieldName) {
		this(KwqlQualifierType.value);
		
		this.fieldName = fieldName;
	}
	
	private KwqlLinkQualifier(String fieldName, KwqlQualifierType type) {
		this.fieldName = fieldName;
		this.type = type;
	}
	
	private KwqlLinkQualifier(String fieldName, String pattern) {
		this(KwqlQualifierType.value);
		
		this.fieldName = fieldName;
		this.pattern = Pattern.compile(pattern);
	}
	
	private KwqlLinkQualifier(String fieldName, String pattern, KwqlQualifierType type) {
		this.fieldName = fieldName;
		this.pattern = Pattern.compile(pattern);
		this.type = type;
	}
	
	/**
	 * Checks whether a given String could be used as a parameter for the corresponding Solr field of this qualifier.
	 * 
	 * @param input the value whose datatype should be checked
	 * @return true, if the datatype of input matches the required datatype of this qualifier
	 */
	public boolean isApplicable(CharSequence input) {
		if (pattern == null) {
			return true;
		}
		
		return pattern.matcher(input).matches();
	}
	
	public boolean isValue() {
		return type == KwqlQualifierType.value;
	}
	
	public boolean isAxis() {
		return type == KwqlQualifierType.axis;
	}
	
	public boolean isResource() {
		return type == KwqlQualifierType.resource;
	}
	
	/**
	 * Checks whether this qualifier can be used as a Solr field.
	 * 
	 * @return true, if this qualifier is a Solr field name.
	 */
	public boolean isField() {
		return type == KwqlQualifierType.value || type == KwqlQualifierType.dummy; 
	}
	
	/**
	 * Returns the Solr field name of this qualifier.
	 * 
	 * @return field name of this qualifier
	 */
	public String getFieldName() {
		if (fieldName == null) {
			return name();
		} else {
			return fieldName;
		}
	}
	
	public String[] getValues(FragmentFacade ff) {
		if (ff == null) {
			return null;
		}
		
		switch (this) {
			case anchorText:
				throw new UnsupportedOperationException();
				
			default:
				throw new IllegalArgumentException();
		}
	}
}
