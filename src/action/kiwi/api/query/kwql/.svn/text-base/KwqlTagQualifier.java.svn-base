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

import java.text.DateFormat;
import java.util.regex.Pattern;

import kiwi.model.tagging.Tag;

public enum KwqlTagQualifier {
	name("tag_names"),
	author("tag_authors"),
	login("tag_author_logins", KwqlQualifierType.dummy),
	created("tag_created", "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z"),
	URI("tag_uris"),
	disagree("disagree", "\\d+", KwqlQualifierType.unsupported);
	
	/** Indicates whether this item is a real qualifier or just some kind of dummy qualifier */
	KwqlQualifierType type;
	
	/** The name of the corresponding Solr field */
	String fieldName;
	
	/** A regular expressed which checks whether the data type of a value matches the required data type of this qualifier */
	Pattern pattern;
	
	private KwqlTagQualifier() {
		this(KwqlQualifierType.value);
	}

	private KwqlTagQualifier(KwqlQualifierType type) {
		this.type = type;
	}
	
	private KwqlTagQualifier(String fieldName) {
		this(KwqlQualifierType.value);
		
		this.fieldName = fieldName;
	}
	
	private KwqlTagQualifier(String fieldName, KwqlQualifierType type) {
		this.fieldName = fieldName;
		this.type = type;
	}

	private KwqlTagQualifier(String fieldName, String pattern) {
		this(KwqlQualifierType.value);
		
		this.fieldName = fieldName;
		this.pattern = Pattern.compile(pattern);
	}
	
	private KwqlTagQualifier(String fieldName, String pattern, KwqlQualifierType type) {
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

	/**
	 * Returns the Solr field name of this qualifier. The resource on which the
	 * qualifier should correspond to can be specified. ci yields the field such
	 * that there is a content item that has a tag with the according field, tag
	 * yields the field such that there is a tag with the according field and
	 * fragment yields the field such that there is a fragment contained in a
	 * content item that has a tag with the according field.
	 * 
	 * @param resource
	 *            the resource on which the qualifier should correspond to
	 * @return field name of this qualifier
	 */
	public String getFieldName(KwqlResource resource) {
		switch (resource) {
			case ci:
				return getFieldName();
			case fragment:
				return "fragment_" + getFieldName();
			default:
				throw new IllegalArgumentException();
		}
	}

	public String[] getValues(Tag tag) {
		if (tag == null) {
			return null;			
		}
		
		switch (this) {
			case name:
				if (tag.getResource().getContentItem().getTitle() == null) {
					return null;
				} else {
					return new String[] { tag.getResource().getContentItem().getTitle().substring(5) };
				}
				
			case author:
				String login = tag.getTaggedBy().getLogin();
				String username = tag.getTaggedBy().getFirstName() + " " + tag.getTaggedBy().getLastName();
				
				return new String[] { login, username };
				
			case created:
				return new String[] { DateFormat.getDateInstance().format(tag.getCreationTime()) };
				
			case URI:
				return new String[] { tag.getResource().getKiwiIdentifier() };
				
			case disagree:
				throw new UnsupportedOperationException(String.format("qualifier '%s' not yet supported", name()));
				
			default:
				throw new IllegalArgumentException();
		}
	}
}
