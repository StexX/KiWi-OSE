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
 * Contributor(s): Jakub Kotowski
 */
package kiwi.service.reasoning.ast;

/**
 * @author Jakub Kotowski
 *
 * Assumes that if the two characters following the first colon are slashes then it's an unprefixed name / uri.
 * Otherwise it's a prefixed name.
 * 
 */
public class Uri extends Term {
	String uri;
	String prefix;
	String localName;
	
	public Uri(String uri) {
		setUri(uri);
	}
	
	public Uri(String prefix, String localName) {
		this.prefix = prefix;
		this.localName = localName;
		this.uri = prefix+":"+localName;
	}
	
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		if (uri == null)
			throw new IllegalArgumentException("Can't set uri string to null.");
		
		//if the two characters following the first colon are slashes then it's an unprefixed name
		int colon = uri.indexOf(":");
		
		if (colon == -1) {
			prefix = null;
			localName = uri;
			this.uri = uri;
			return;
		}
		
		if (uri.charAt(colon+1) == '/' && uri.charAt(colon+2) == '/') {
			this.uri = uri;
			this.prefix = null;
			this.localName = null;
			return;
		}
		
		prefix = uri.substring(0,colon);
		localName = uri.substring(colon+1);
		this.uri = prefix+":"+localName;		
	}

	/**
	 * Assumes that if the two characters following the first colon are slashes then it's an unprefixed name / uri.
	 * Otherwise it's a prefixed name.
	 * @return
	 */
	public boolean isPrefixed() {
		return prefix != null;
	}
	
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	/* (non-Javadoc)
	 * @see kiwi.service.reasoning.ast.Term#getTermType()
	 */
	@Override
	public TermType getTermType() {
		return TermType.URI;
	}
	
	public String toString() {
		return uri;
	}
	
	public static void main(String[] s) {
		Uri uri;
		uri = new Uri("prefix:localName");
		System.out.println("prefix: "+uri.getPrefix()+" localName: "+uri.getLocalName()+" uri: "+uri.getUri());
		uri = new Uri("http://asdlfkj/asdf#039wjfas;k");
		System.out.println("prefix: "+uri.getPrefix()+" localName: "+uri.getLocalName()+" uri: "+uri.getUri());
		uri = new Uri("http:://asdf/asdf/");
		System.out.println("prefix: "+uri.getPrefix()+" localName: "+uri.getLocalName()+" uri: "+uri.getUri());
		uri = new Uri("as;ldfkjasdfa;lskdf");
		System.out.println("prefix: "+uri.getPrefix()+" localName: "+uri.getLocalName()+" uri: "+uri.getUri());
		uri = new Uri("");
		System.out.println("prefix: "+uri.getPrefix()+" localName: "+uri.getLocalName()+" uri: "+uri.getUri());
		uri.setUri("http://exammple.com/asdflkj");
		System.out.println("prefix: "+uri.getPrefix()+" localName: "+uri.getLocalName()+" uri: "+uri.getUri());
	}
}
