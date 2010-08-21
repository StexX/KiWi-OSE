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
package kiwi.model.content;

import kiwi.exception.InconsistentStateException;
import kiwi.model.Constants;
import kiwi.model.kbase.KiWiUriResource;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;
import nu.xom.XPathContext;

/**
 * TextFragments are parts of TextContent objects marked by bookmarks in the XML representation of the document.
 * 
 * Bookmarks may span XML elements and break the hierarchical structure. For this reason, they are identified by
 * a <bookmarkStart/> and a <bookmarkEnd/> element in XML. For example, a text content might look like this:
 * 
 * <content>
 *      <p> bla bla <bookmarkstart resource="..."/> bla bla</p> 
 *      <p> bla <bookmarkend resource="..."/> bla ...</p>
 * </content>
 * 
 * 
 * @author sschaffe
 */
public class TextFragment {

    private TextContent containingContent;
    private KiWiUriResource resource;
    
    public TextFragment(TextContent containingContent, KiWiUriResource resource) {
        this.containingContent = containingContent;
        this.resource          = resource;
    }

    /**
     * Get the textual content containing this fragmemt
     * @return
     */
    public TextContent getContainingContent() {
        return containingContent;
    }

    /**
     * Get the resource identifying this fragment.
     * @return
     */
    public KiWiUriResource getResource() {
        return resource;
    }
    
    /**
     * Get the starting bookmark element of this fragment.
     */
    public Element getBookmarkStart() {
    	XPathContext namespaces = new XPathContext();
    	namespaces.addNamespace("kiwi", Constants.NS_KIWI_HTML);
        Nodes nodes = containingContent.getXmlDocument().query("//kiwi:bookmarkstart[@id='"+resource.getKiwiIdentifier()+"']", namespaces);
        if(nodes.size() == 1) {
            return (Element)nodes.get(0);
        } else {
            throw new InconsistentStateException("The start element of the fragment with id "+resource.getKiwiIdentifier()+" could either not be found or is duplicated in the XML content of the object "+containingContent.getId());
        }
    }

    /**
     * Get the ending bookmark element of this fragment.
     */
    public Element getBookmarkEnd() {
    	XPathContext namespaces = new XPathContext();
    	namespaces.addNamespace("kiwi", Constants.NS_KIWI_HTML);
        Nodes nodes = containingContent.getXmlDocument().query("//kiwi:bookmarkend[@id='"+resource.getKiwiIdentifier()+"']", namespaces);        
        if(nodes.size() == 1) {
            return (Element)nodes.get(0);
        } else {
            throw new InconsistentStateException("The end element of the fragment with id "+resource.getKiwiIdentifier()+" could either not be found or is duplicated in the XML content of the object "+containingContent.getId());
        }
    }
    
    /**
     * Return a pruned tree containing only the fragment and elements relevant to this fragment
     * @return the pruned copy of the original document
     */
    public Document getFragmentTree() {
    	
    	final Document doc = containingContent.getXmlDocument();
    	
    	final Document ret = new Document(doc);
    	
    	final Element bookmarkStart;
    	final Element bookmarkEnd;
    	Nodes nodes;
    	XPathContext namespaces = new XPathContext();
    	namespaces.addNamespace("kiwi", Constants.NS_KIWI_HTML);
    	
    	nodes = ret.query("//kiwi:bookmarkstart[@id='"+resource.getKiwiIdentifier()+"']", namespaces);
    	if(nodes.size() == 1) {
            bookmarkStart = (Element)nodes.get(0);
        } else {
            throw new InconsistentStateException("The start element of the fragment with id "+resource.getKiwiIdentifier()+" could either not be found or is duplicated in the XML content of the object "+containingContent.getId());
        }
    	
    	nodes = ret.query("//kiwi:bookmarkend[@id='"+resource.getKiwiIdentifier()+"']", namespaces);
    	if(nodes.size() == 1) {
            bookmarkEnd = (Element)nodes.get(0);
        } else {
            throw new InconsistentStateException("The start element of the fragment with id "+resource.getKiwiIdentifier()+" could either not be found or is duplicated in the XML content of the object "+containingContent.getId());
        }
    	
    	// Prune all before the bookmarkStart...
    	Node n = bookmarkStart;
    	while (n.getParent() != null) {
    		if (n.getParent().getChild(0) == n) {
    			// we have deleted all before this node, go one level up
    			n = n.getParent();
    		}
    		else {
    			n.getParent().removeChild(0);
    		}
    	}
    	
    	// Prune all after the bookmarkEnd
    	n = bookmarkEnd;
    	while (n.getParent() != null) {
    		if (n.getParent().getChild(n.getParent().getChildCount() - 1) == n) {
    			n = n.getParent();
    		}
    		else {
    			n.getParent().removeChild(n.getParent().getChildCount() - 1);
    		}
    	}
    	
    	/*
    	 * Delete all the fragment tags in this fragment tree (because they are 
    	 * not fragments of this fragment... ), fragments can't contain other fragments
    	 * (because there is currently no way to identify which one is the containing resource...
    	 */
 
    	// TODO: I am sure this two can be written as a single XPath ... 
    	nodes = ret.query("//kiwi:bookmarkstart", namespaces);
    	for (int i = 0; i < nodes.size(); ++i) {
    		Node e = nodes.get(i);
    		e.detach();
    	}
    	nodes = ret.query("//kiwi:bookmarkend", namespaces);
    	for (int i = 0; i < nodes.size(); ++i) {
    		Node e = nodes.get(i);
    		e.detach();
    	}
    	
    	return ret;
    }

    /**
     * Return a string representing this fragment in XML format, i.e. the part of the XML document inbetween the starting and the ending bookmark. 
     * Note that this string is not necessarily well-formed, as fragments may overlap and break the structural hierarchy.
     * @return
     */
    public String getFragmentString() {       
        Document doc = getFragmentTree();
        return doc.toXML();
    }
}
