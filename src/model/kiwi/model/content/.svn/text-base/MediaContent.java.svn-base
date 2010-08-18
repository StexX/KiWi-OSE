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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import kiwi.model.user.User;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.OptimisticLockType;

/**
 * MediaContent is a certain type of content for pictures, videos 
 * and other media data. The data itself is stored in a byte array.
 * Furthremore it contains the mime-type of the data and a file name.
 *
 * @author Sebastian Schaffert
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
//@BatchSize(size=10)
//@Proxy(lazy=true)
@Immutable
@AccessType("property")
@org.hibernate.annotations.Entity(optimisticLock=OptimisticLockType.VERSION)
public class MediaContent extends Content implements Serializable {

	private static final long serialVersionUID = -5020666543607571411L;

    private byte[] data;
    
    private String mimeType;

    private String fileName;
    
    /**
     * Default Constructor
     */
    public MediaContent() {
    	super();
    }
    
    /**
     * Constructor
     * Sets the resource, the ContentItem that holds this content, the author and the language
     * @param item
     * @param author
     */
    @Deprecated
    public MediaContent(ContentItem item, User author) {
    	super();
//        this.contentItem = item;
    }

    /**
     * Constructor
     * Sets the resource, the ContentItem that holds this content
     * @param item
     */
    public MediaContent(ContentItem item) {
    	super();
//        this.contentItem = item;
    }
    
//    /**
//     * Returns the content item that includes this content
//     * @return
//     */
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
     * Get the data of this media content object as a byte array.
     * @return
     */
    @Lob
    @Basic(fetch=FetchType.LAZY)
    public byte[] getData() {
        return data;
    }

    /**
     * Set the data of this media content object as a byte array.
     * @param data
     */
    public void setData(byte[] data) {
        this.data = data;
    }

    /**
     * Get the data of this media content object as an input stream.
     * 
     * Currently just wraps the byte array in a ByteArrayInputStream, but more
     * efficient implementations could directly refer to the database instead.
     * @return
     */
    @Transient
    public InputStream getDataInputStream() {
        return new ByteArrayInputStream(getData());
    }

    /**
     * Get an output stream that allows to write data into this media content object.
     * 
     * Currently wraps the byte array in a ByteArrayOutputStream that is saved back to the
     * object when .close() is called.
     * @return
     */
    @Transient
    public OutputStream getDataOutputStream() {
        return new ByteArrayOutputStream() {
            @Override
            public void close() throws IOException {
                super.close();
                setData(this.toByteArray());
            }
            
        };
    }
    
    /**
     * Return the mime type of the media content contained in this object, e.g. image/jpeg.
     * 
     * This information is necessary for properly deciding how to render the content.
     * 
     * @return
     */
    @Basic
    public String getMimeType() {
        return mimeType;
    }

    /**
     * Set the mime type of the media content contained in this object.
     * @param mimeType
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

	/**
	 * @return the fileName
	 */
    @Basic
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
    
    
}
