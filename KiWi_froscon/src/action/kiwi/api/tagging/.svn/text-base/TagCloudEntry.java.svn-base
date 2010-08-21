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
package kiwi.api.tagging;

import java.text.Collator;
import java.util.Comparator;
import java.util.Date;

/**
 * @author Sebastian Schaffert
 *
 */
public class TagCloudEntry {

	private String tagTitle;
	
	private int count;
	
	// the most recent tag in the set done by this ContentItem
	private Date lastTaggingDate;
	
	private int size = 100;

	/**
	 *  the color code is used for varying the colours between tags, by default based on
	 *  the ranking in the time of modifications within the set.
	 */
	private int colorCode = 0;
	
	public TagCloudEntry(int count, String title) {
		this.count = count;
		this.tagTitle = title;
	}
	
	
	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}



	/**
	 * @return the lastTaggingDate
	 */
	public Date getLastTaggingDate() {
		return lastTaggingDate;
	}



	/**
	 * @param lastTaggingDate the lastTaggingDate to set
	 */
	public void setLastTaggingDate(Date lastTaggingDate) {
		this.lastTaggingDate = lastTaggingDate;
	}

	
	



	public String getTagTitle() {
		return tagTitle;
	}


	public void setTagTitle(String tagTitle) {
		this.tagTitle = tagTitle;
	}


	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}



	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}
	
	
	/**
	 * @return the colorCode
	 */
	public int getColorCode() {
		return colorCode;
	}



	/**
	 * @param colorCode the colorCode to set
	 */
	public void setColorCode(int colorCode) {
		this.colorCode = colorCode;
	}




	public static Comparator<TagCloudEntry> COMP_TITLE = new Comparator<TagCloudEntry>() {
		public int compare(TagCloudEntry arg0, TagCloudEntry arg1) {
			Collator myCollator = Collator.getInstance();
			return myCollator.compare(
					arg0.getTagTitle(),
					arg1.getTagTitle());
		}
	};
}
