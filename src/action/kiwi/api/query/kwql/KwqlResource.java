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

import kiwi.service.query.kwql.parser.KWQL;

@SuppressWarnings("unchecked")
public enum KwqlResource {
	ci(KwqlContentItemQualifier.class, KWQL.CI, "CI"),
	fragment(KwqlFragmentQualifier.class, KWQL.FRAG, "FRAG"),
	tag(KwqlTagQualifier.class, KWQL.TAG, "TAG"),
	link(KwqlLinkQualifier.class, KWQL.LINK, "LINK");
	
	private int antlrId;
	private String antlrName;
	private Class qualifiers;
	
	private KwqlResource(Class qualifiers, int antlrId, String antlrName) {
		this.qualifiers = qualifiers;
		this.antlrId = antlrId;
		this.antlrName = antlrName;
	}
	
	public boolean hasQualifier(String name) {
		try {
			Enum.valueOf(qualifiers, name);
			
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
	
	public boolean hasResource(String name) {
		try {
			Enum qualifier = Enum.valueOf(qualifiers, name);
			
			if (qualifier instanceof KwqlContentItemQualifier) {
				return ((KwqlContentItemQualifier)qualifier).isResource();
			} else if (qualifier instanceof KwqlFragmentQualifier) {
				return ((KwqlFragmentQualifier)qualifier).isResource();
			} else if (qualifier instanceof KwqlTagQualifier) {
				return ((KwqlTagQualifier)qualifier).isResource();
			} else if (qualifier instanceof KwqlLinkQualifier) {
				return ((KwqlLinkQualifier)qualifier).isResource();
			}
		} catch (IllegalArgumentException e) {
			return false;
		}
		
		throw new IllegalArgumentException();
	}

	
	public static KwqlResource valueOf(int antlrId) {
		for (KwqlResource resource : KwqlResource.values()) {
			if (resource.antlrId == antlrId) {
				return resource;
			}
		}
		
		throw new IllegalArgumentException();
	}
	
	public int getAntlrId() {
		return antlrId;
	}
	
	public String getAntlrName() {
		return antlrName;
	}
}
