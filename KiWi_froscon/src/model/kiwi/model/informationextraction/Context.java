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
package kiwi.model.informationextraction;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Lob;

import kiwi.model.Constants;
import kiwi.util.KiWiXomUtils;
import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;

import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * Represents a fragment context used for classification of fragments.
 * 
 * @author Marek Schmidt
 *
 */
@Embeddable
public class Context implements Serializable {

	private static final long serialVersionUID = 1L;
	/*
	 * The fragment-based examples should use left-in-right contexts.
	 */

	@Lob
	private String leftContext;
	@Lob
	private String rightContext;
	@Lob
	private String inContext;

	/*
     * True for fragment contexts, if false all other properties may be undefined.
     */ 
	private boolean isFragment = false;
	
	/*
	 * coordinates of the fragment in the "text space" of the sourceTextContent. 
	 */
	private int inBegin;
	private int inEnd;
	
	/*
	 * coordinates of the context in the "text space"
	 * (contextBegin <= inBegin <= inEnd <= contextEnd)
	 * 
	 * for non-fragment contexts, such as whole content item,
	 * contextBegin = inBegin = 0 <= inEnd = contextEnd  (the whole content "inside")
	 */
	private int contextBegin;
	private int contextEnd;
	
	
	public String getLeftContext() {
		return leftContext;
	}
	public void setLeftContext(String leftContext) {
		this.leftContext = leftContext;
	}
	public String getRightContext() {
		return rightContext;
	}
	public void setRightContext(String rightContext) {
		this.rightContext = rightContext;
	}
	public String getInContext() {
		return inContext;
	}
	public void setInContext(String inContext) {
		this.inContext = inContext;
	}
	public int getContextBegin() {
		return contextBegin;
	}
	public void setContextBegin(int contextBegin) {
		this.contextBegin = contextBegin;
	}
	public int getContextEnd() {
		return contextEnd;
	}
	public void setContextEnd(int contextEnd) {
		this.contextEnd = contextEnd;
	}
	public int getInBegin() {
		return inBegin;
	}
	public void setInBegin(int inBegin) {
		this.inBegin = inBegin;
	}
	public int getInEnd() {
		return inEnd;
	}
	public void setInEnd(int inEnd) {
		this.inEnd = inEnd;
	}
	
	public String toString() {
		return this.leftContext + "<(" + this.inBegin+")"+ this.inContext + "("+ this.inEnd + ")>" + this.rightContext;
	}

	public boolean getIsFragment() {
		return isFragment;
	}

	public void setIsFragment(boolean isFragment) {
		this.isFragment = isFragment;
	}
	
	/**
	 * Hash for matching "blacklists", therefore is not 
	 * depending on positions of the context in the text content, only on strings...
	 */
	public int hashCode() {
		return new HashCodeBuilder().
	       append(leftContext).
	       append(inContext).
	       append(rightContext).
	       toHashCode();
	}
	
	/**
	 * Creates bookmark elements in the document at the position of this context. The document has to come from the same textcontent as the original context.
	 * (but since this method should not modify the text coordinates itself, it should be possible to fragmentize multiple fragments sequentially.)
	 * @param doc XOM document.
	 * @param fragmentId The fragment ID for the fragment to create.
	 * @return modified XOM document with the bookmark elements created. 
	 */
	public void fragmentize (Document doc, String fragmentId, int offset) {
        
		Element e_begin = new Element("kiwi:bookmarkstart", Constants.NS_KIWI_HTML);
        e_begin.addAttribute(new Attribute("id", fragmentId));
        
		KiWiXomUtils.insertNodeAtPos(doc, 0, inBegin + offset, e_begin);
		
		Element e_end = new Element("kiwi:bookmarkend", Constants.NS_KIWI_HTML);
        e_end.addAttribute(new Attribute("id", fragmentId));
        
        KiWiXomUtils.insertNodeAtPos(doc, 0, inEnd + offset, e_end);
	}
	
	public void fragmentize (Document doc, String fragmentId) {
		fragmentize(doc, fragmentId, 0);
	}
}
