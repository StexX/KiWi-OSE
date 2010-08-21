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
package kiwi.model.kbase;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

import javax.persistence.Basic;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import kiwi.api.triplestore.TripleStore;
import kiwi.model.Constants;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;


/**
 * The KiWiUriResource is a subclass of KiWiResource that 
 * is represented by an URI.
 *
 * @author Sebastian Schaffert
 */
@Entity
@DiscriminatorValue("u")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@BatchSize(size = 100)
public class KiWiUriResource extends KiWiResource implements Serializable {

	private static final long serialVersionUID = -6294168809422493844L;

	@Basic
//	@Column(unique=true)
    @Index(name="uri_index")
    @Length(max=2048)
    @NotNull
    private String uri;
    
	@Transient
	private String namespacePrefixCache;
	
    public KiWiUriResource() {
    	this.uri="";
    }
    
    public KiWiUriResource(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
    
    

    @Override
    public int hashCode() {
        return this.getUri().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (! (obj instanceof KiWiNode && ((KiWiNode)obj).isUriResource()) ) {
            return false;
        }
        final KiWiUriResource other = (KiWiUriResource) obj;
        if (this.uri != other.uri && (this.uri == null || !this.uri.equals(other.uri))) {
            return false;
        }
        return true;
    }

    /**
     * Return the SeRQL id of this resource
     * @return
     */
    @Override
    public String getSeRQLID() {
        return "<" + getUri() + ">";        
    }

    @Override
    public String toString() {
        return getUri();
    }
	
	@Override
	public void addType(KiWiUriResource type) {
		TripleStore ts = getContext();
		
		ts.createTriple(this, "http://www.w3.org/1999/02/22-rdf-syntax-ns#type", type);
		
//        KiWiUriResource rdf_type = ts.createUriResource();
//        ts.createTriple(this, rdf_type, type);
    }
	
	
	@Deprecated
	public static String getRDFUri() {
		return Constants.NS_RDF;
	}
	
	@Deprecated
	public static String getRDFSUri() {
		return Constants.NS_RDFS;
	}
	
	@Deprecated
	public static String getOWLUri() {
		return Constants.NS_OWL;
	}
	
	@Deprecated
	public static String getXMLUri() {
		return Constants.NS_XML;
	}
	
	@Deprecated
	public static String getXMLSUri() {
		return Constants.NS_XSD;
	}
	
	

	@Override
	public String getKiwiIdentifier() {
		return "uri::"+getUri();
	}

	/**
	 * @author
	 * @return
	 * @see kiwi.model.kbase.KiWiNode#getCacheKey()
	 */
	@Override
	public String getCacheKey() {
		return uri;
	}

	/**
	 * Return the namespace prefix for this uri resource.
	 * @return
	 */
	public String getNamespacePrefix() {
		// note that this caching is unsafe as it is not refreshed on namespace change, but
		// such changes occur infrequently and are non-critical
		if(namespacePrefixCache == null) {
			namespacePrefixCache = getContext().getNamespacePrefix(uri);
		}
		return namespacePrefixCache;
	}

	/**
	 * Return local name part of this uri resource.
	 * 
	 * TODO: this is completely unefficient, but hopefully not called too frequently.
	 * 
	 * @return
	 */
	public String getLocalName() {
		KiWiNamespace ns = getContext().getNamespace(getNamespacePrefix());
		return getUri().substring(ns.getUri().length());
	}

	/** Creates a short name for the URI - either the fragment or the word after the (second to the) last slash.
	 * 
	 * For 
	 * 		http://example.com/ns#frament 		it will return "fragment"
	 * 		http://example.com/ontology/		it will return "ontology"
	 * 		http://example.com/ontology			it will return "ontology"
	 * 
	 *
	 * @return
	 * @throws URISyntaxException 
	 */
	public String getShortName() throws URISyntaxException {
		String uri = getUri();
		if (uri == null)
			return "there's a bug somewhere";
		
		String name = new URI(uri).getFragment(); //URI-reference = [ absoluteURI | relativeURI ] [ "#" fragment ] http://www.ietf.org/rfc/rfc2396.txt
		if (name == null || name.length() == 0) {
			if ((uri.lastIndexOf('/')+1) == uri.length())
				uri = uri.substring(0,uri.lastIndexOf('/'));
			
			name = uri.substring(uri.lastIndexOf('/')+1);
		}
		return name;
	}
	
	@Override
	public boolean isAnonymousResource() {
		return false;
	}

	@Override
	public boolean isLiteral() {
		return false;
	}

	@Override
	public boolean isUriResource() {
		return true;
	}
	
	
	
}
