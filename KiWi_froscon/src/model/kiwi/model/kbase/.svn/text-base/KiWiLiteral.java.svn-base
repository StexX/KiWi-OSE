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

import java.util.Locale;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import kiwi.util.MD5;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.validator.NotNull;

/**
 * KiWiLiterals store literal information from the knowledge base. They directly 
 * correspond to an RDF literal stored in Sesame. KiWiLiterals are
 * parametrized with the Java type corresponding to the literal content they store.
 * The method getType() returns the KiWiResource representing the RDF type of this 
 * literal. This information can e.g. be used to provide appropriate user interfaces.
 * 
 * A factory class should be provided that maps between RDF types and Java classes.
 * 
 * @author Sebastian Schaffert
 */
@Entity
@DiscriminatorValue("l")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@BatchSize(size = 100)
@NamedQueries({
	// TODO: I am not sure whether it is safe to use the MD5 value here, this could lead to 
	//       problems in exceptional cases ...
	@NamedQuery(name  = "tripleStore.literalByValueLanguageType", 
			    query = "from KiWiLiteral l " +
			    		"where l.contentMd5  = :md5 " +
			    		"  and l.language = :lang " +
			    		"  and l.type     = :type"),
	@NamedQuery(name  = "tripleStore.literalByValueType", 
			    query = "from KiWiLiteral l " +
			    		"where l.contentMd5  = :md5 " +
			    		"  and l.language is null " +
			    		"  and l.type     = :type")
 })
public class KiWiLiteral extends KiWiNode {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1772323725671607249L;
	
	@Lob
	@NotNull
	private String content;
	

	@Basic
	@NotNull
	@Index(name="idx_literal_content")
	private String contentMd5;
	
	/**
	 * Content as integer value (if appropriate); for efficient querying
	 */
	@Basic
	@Index(name="idx_literal_int")
	private Integer intContent;
	
	/**
	 * Content as double value (if appropriate); for efficient querying
	 */
	@Basic
	@Index(name="idx_literal_double")
	private Double doubleContent;
	
	@Basic
	@Column(length=5)
    private Locale language;
    
    @ManyToOne
    private KiWiResource type;

    public KiWiLiteral() {
    	
    }
    
    public KiWiLiteral(String content, Locale language, KiWiResource type) {
        setContent(content);
        this.language = language;
        this.type = type;
    }

    public KiWiLiteral(Double content, Locale language, KiWiResource type) {
        setDoubleContent(content);
        this.language = language;
        this.type = type;
    }

    public KiWiLiteral(Integer content, Locale language, KiWiResource type) {
        setIntContent(content);
        this.language = language;
        this.type = type;
    }
    
    /**
     * Return the content of the literal, using the parametrized Java type
     * @return
     */
    public String getContent() {
        return content;
    }

    /**
     * Set the content of the literal to the content provided as parameter.
     * @param content
     */
    public void setContent(String content) {
    	try {
    		this.intContent = Integer.parseInt(content);
    	} catch(NumberFormatException ex) {
    		this.intContent = null;
    	}
    	
    	try {
    		this.doubleContent = Double.parseDouble(content);
    	} catch(NumberFormatException ex) {
    		this.doubleContent = null;
    	}
    	
    	this.contentMd5 = MD5.md5sum(content);
    	
        this.content = content;
    }

    /**
     * Get the locale representing the language this literal is in; returns null 
     * if no language is associated with the literal
     * @return
     */
    public Locale getLanguage() {
        return language;
    }

    /**
     * Set the language of this literal to the given locale.
     * @param language
     */
    public void setLanguage(Locale language) {
        this.language = language;
    }

    /**
     * Return the RDF/XSD type of this literal.
     * @return
     */
    public KiWiResource getType() {
        return type;
    }

    /**
     * Set the RDF/XSD type of this literal.
     * @param type
     */
    public void setType(KiWiResource type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("\""+getContent().toString()+"\"");
        if(language != null) {
            result.append("@"+language.getLanguage());
        }
        result.append("^^"+getType().toString());
        return result.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (! (obj instanceof KiWiLiteral) ) {
            return false;
        }
        final KiWiLiteral other = (KiWiLiteral) obj;
        if (this.content != other.content && (this.content == null || !this.content.equals(other.content))) {
            return false;
        }
        if (this.language != other.language && (this.language == null || !this.language.equals(other.language))) {
            return false;
        }
        if (this.type != other.type && (this.type == null || !this.type.equals(other.type))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + (this.content != null ? this.content.hashCode() : 0);
        hash = 89 * hash + (this.language != null ? this.language.hashCode() : 0);
        hash = 89 * hash + (this.type != null ? this.type.hashCode() : 0);
        return hash;
    }

	/**
	 * @author
	 * @return
	 * @see kiwi.model.kbase.KiWiNode#getCacheKey()
	 */
	@Override
	public String getCacheKey() {
 		return content.toString() +  ( language==null?"":("@" + language.toString()) ) + ( type==null?"":("^^" + type.toString()) );
	}

	public Integer getIntContent() {
		return intContent;
	}

	public void setIntContent(Integer intContent) {
		this.intContent = intContent;
		this.content = intContent.toString();
		this.doubleContent = intContent.doubleValue();
	}

	public Double getDoubleContent() {
		return doubleContent;
	}

	public void setDoubleContent(Double doubleContent) {
		this.doubleContent = doubleContent;
		this.content = doubleContent.toString();
		this.intContent = doubleContent.intValue();
	}

	@Override
	public boolean isAnonymousResource() {
		return false;
	}

	@Override
	public boolean isLiteral() {
		return true;
	}

	@Override
	public boolean isUriResource() {
		return false;
	}

	@Override
	public String getShortName() {
		return getContent();
	}

	public String getContentMd5() {
		return contentMd5;
	}

	public void setContentMd5(String contentMd5) {
		this.contentMd5 = contentMd5;
	}
 
    
    
}
