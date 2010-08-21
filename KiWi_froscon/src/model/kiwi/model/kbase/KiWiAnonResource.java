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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * The KiWiAnonResaource represents the anonymous RDF resource.
 * 
 * @author Sebastian Schaffert
 */
@Entity
@DiscriminatorValue("a")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@BatchSize(size = 100)
public class KiWiAnonResource extends KiWiResource implements Serializable {
    
	private static final long serialVersionUID = -873594698794527452L;

	@Basic
    @Column(unique=true)
    @Index(name="anonid_index")
    private String anonId;

    public KiWiAnonResource() {
    }
    
    public KiWiAnonResource(String id) {
        super();
        this.anonId = id;
    }

    public String getAnonId() {
        return anonId;
    }

    public void setAnonId(String id) {
        this.anonId = id;
    }

    /**
     * Return the SeRQL identifier of this resource.
     * @return
     */
    @Override
    public String getSeRQLID() {
        return "_:"+getAnonId();
    }

    @Override
    public String toString() {
        return "_:"+anonId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (! (obj instanceof KiWiAnonResource) ) {
            return false;
        }
        final KiWiAnonResource other = (KiWiAnonResource) obj;
        if (this.anonId != other.anonId && (this.anonId == null || !this.anonId.equals(other.anonId))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.anonId != null ? this.anonId.hashCode() : 0);
        return hash;
    }

	@Override
	public String getKiwiIdentifier() {
		return "bnode::"+getAnonId();
	}

	/**
	 * @author
	 * @return
	 * @see kiwi.model.kbase.KiWiNode#getCacheKey()
	 */
	@Override
	public String getCacheKey() {
		return anonId;
	}
    
	public String getNamespacePrefix() {
		return "blank";
	}

	@Override
	public boolean isAnonymousResource() {
		return true;
	}

	@Override
	public boolean isLiteral() {
		return false;
	}

	@Override
	public boolean isUriResource() {
		return false;
	}

	@Override
	public String getShortName() {
		return toString();
	}
	
	
}
