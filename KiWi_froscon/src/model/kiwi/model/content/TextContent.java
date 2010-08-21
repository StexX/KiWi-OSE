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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import kiwi.model.Constants;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.User;
import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import nu.xom.ParsingException;
import nu.xom.XPathContext;
import nu.xom.canonical.Canonicalizer;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.OptimisticLockType;

/**
 * Representation of textual content in the KiWi system. Backed by an XML document represented 
 * in XOM format.
 *
 * TODO: there should be a way to check validity of the XML document, particularly 
 * regarding the bookmarks for text fragments.
 * 
 * @author Sebastian Schaffert
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@AccessType("property")
@Immutable
@org.hibernate.annotations.Entity(optimisticLock=OptimisticLockType.VERSION)
public class TextContent extends Content implements Serializable {

	private static final long serialVersionUID = 2742586720018796723L;

//	private ContentItem contentItem;
 
    private String xmlStringCacheCanonical;
    private String xmlStringCacheNormal;
    private String xmlStringCacheSetter;
    
    
	/**
     * XOM representation of the textual content.
     */
    private Document xmlDocument;
    
    public TextContent() {
    	super();
    }

    /**
     * Constructor
     * Sets the resource, the ContentItem that holds this content, the author and the language
     * @param item
     * @param author
     */
    @Deprecated
    public TextContent(ContentItem item, User author) {
    	super();
//        this.contentItem = item;
    }

    /**
     * Constructor
     * Sets the resource, the ContentItem that holds this content
     * @param item
     */
    public TextContent(ContentItem item) {
    	super();
//        this.contentItem = item;
    }
        
//    /**
//     * Returns the content item that includes this content
//     * @return
//     */
//	@OneToOne(fetch=FetchType.EAGER,mappedBy="textContent")
//    public ContentItem getContentItem() {
//        return contentItem;
//    }
//
//    /**
//     * sets the content item that includes this content
//     * @param contentItem
//     */
//    public void setContentItem(ContentItem contentItem) {
//        this.contentItem = contentItem;
//    }
 

    /**
     * @return XOM representation of the textual content.
     */
    @Transient
    public Document getXmlDocument() {
    	// parse document on first access
    	if(xmlDocument == null && xmlStringCacheSetter != null) {
 	        Builder builder = new Builder();
	        try {
	            xmlDocument = builder.build(xmlStringCacheSetter, "kiwi://");
	        	xmlStringCacheCanonical = null;
	        	xmlStringCacheNormal    = null;
	        	xmlStringCacheSetter    = null;
	        } catch(ParsingException ex) {
	        	try {
					xmlDocument = builder.build("<p></p>", "kiwi://");
				} catch (Exception e) {
					e.printStackTrace();
				};
	        } catch(IOException ex) {
	        	xmlDocument = null;
	        }
    	}

        return xmlDocument;
    }

    /**
     * Set the XOM representation of the XML document representing the content of this TextContent.
     * @param xmlDocument
     */
    public void setXmlDocument(Document xmlDocument) {
    	xmlStringCacheCanonical = null;
    	xmlStringCacheNormal    = null;
    	xmlStringCacheSetter    = null;
        this.xmlDocument = xmlDocument;
    }

    /**
     * @return the XOM representation of the XML document representing the content of this TextContent
     * object.
     */
   public Document copyXmlDocument() {
	   Document doc = getXmlDocument();
	   if(doc != null) {
		   return (Document) doc.copy();
	   } else {
		   return null;
	   }
    }
    
    
    
    /**
     * Serialise the XML document as an XML string.
     * @param canonical if true, create a canonical XML representation of the XML document
     * @return
     */
    public String getXmlString(boolean canonical) {
    	// if document not yet initialised, simply return the string ...
    	if(xmlDocument == null && xmlStringCacheSetter != null) {
    		return xmlStringCacheSetter;
    	}
    	if(canonical) {
    		if(xmlStringCacheCanonical == null) {
    			xmlStringCacheCanonical = xom2string(this.getXmlDocument(), canonical);
    		}
    		return xmlStringCacheCanonical;
    	} else {
    		if(xmlStringCacheNormal == null) {
    			xmlStringCacheNormal = xom2string(this.getXmlDocument(), canonical);
    		}
    		return xmlStringCacheNormal;
    		
    	}
    }
    
    /**
     * Serialise the XML document for displaying in HTML; looks for the KiWi page root 
     * and renders all contained elements
     * @return
     */
    @Transient
    public String getHtmlContent() {
    	String result;
    	XPathContext namespaces = new XPathContext();
    	namespaces.addNamespace("kiwi", Constants.NS_KIWI_HTML);
    	Nodes nl = this.getXmlDocument().query("//*[@kiwi:type='page']/child::node()",namespaces);
    	if(nl.size() > 0) {
    		result = "";
    		for(int i = 0; i < nl.size(); i++) {
    			result += xom2string(nl.get(i),true);
    		}
    	} else {
    		result = getXmlString(true);
    	}
    	return result;
    }
    
    /**
     * @return the content in XML form
     */
    @Lob
    @Column(nullable=false)
    public String getXmlString() {
        return getXmlString(true);
    }

    /**
     * Sets the content
     * @param xml a string representation of an XML document
     */
    public void setXmlString(String xml) {
    	xmlDocument = null;
    	xmlStringCacheCanonical = null;
    	xmlStringCacheNormal = null;
    	xmlStringCacheSetter = xml;
    }
    
    @Transient
    public String getPlainString() {
    	String xml = getXmlString();
    	
    	Pattern p = Pattern.compile("<[^>]+>");
    	Matcher m = p.matcher(xml);
    	
    	StringBuffer sb = new StringBuffer();
    	
    	while(m.find()) {
    		m.appendReplacement(sb, " ");
    	}
    	m.appendTail(sb);
    	
    	return sb.toString();
    }
    
    public static final Document string2xom(String xml) {
		if (xml != null) { // Seam/JPA seems to need this
			Builder builder = new Builder();
			try {
				Document doc = builder.build(xml, "kiwi://");
				return doc;
			} catch (ParsingException ex) {
				return null;
			} catch (IOException ex) {
				return null;
			}
		} else {
			return null;
		}
	}

	public static final String xom2string(Node dom, boolean canonical) {
		if (dom != null) {
			if (canonical) {
				// use a canonicalizer from XOM to create a canonical
				// representation of the document
				ByteArrayOutputStream out = new ByteArrayOutputStream();

				Canonicalizer c14n = new Canonicalizer(out,
						Canonicalizer.EXCLUSIVE_XML_CANONICALIZATION);

				try {
					c14n.write(dom);
				} catch (IOException ex) {
					throw new RuntimeException(
							"I/O Exception while serialising XML document; this should never happen!",
							ex);
				}

				String result = "";
				try {
					result = new String(out.toByteArray(), "UTF-8");
				} catch (UnsupportedEncodingException exc) {
				}
				return result;
			} else {
				// use the XOM native methods to create a String representation
				return dom.toXML();
			}
		} else {
			return null;
		}
	}
    
    /**
     * List the ids of the marked fragments in this textual content.
     *
     * The textual fragments that are contained in this TextContent. In the XML representation,
     * text fragments are identified by so-called bookmarks with different ids.
     *
     * @return
     */
    @Transient
    public Iterable<String> getFragmentIds() {
    	XPathContext namespaces = new XPathContext();
    	namespaces.addNamespace("kiwi", Constants.NS_KIWI_HTML);
        final Nodes nodes = getXmlDocument().query("//kiwi:bookmarkstart/@id", namespaces);
      
        return new Iterable<String>() {

            public Iterator<String> iterator() {
                return new Iterator<String>() {
                    int i = 0;

                    public boolean hasNext() {
                       return i<nodes.size();
                    }

                    public String next() {
                       Node n = nodes.get(i++);
                       if(n instanceof Attribute) {
                           return ((Attribute)n).getValue();
                       } else {
                           throw new IllegalStateException("result at position "+i+" was no attribute; while retrieving bookmarks for textual content.");
                       }
                    }

                    public void remove() {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }  
                };
            }
            
        };
    }
    
    /**
     * Return the fragment of this text content.
     * @param resource Fragment resource
     * @return
     */
    public TextFragment getFragment(KiWiUriResource resource) {
        return new TextFragment(this, resource);
    }
}
