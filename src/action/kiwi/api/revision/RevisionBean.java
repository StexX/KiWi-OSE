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
package kiwi.api.revision;

import kiwi.model.revision.CIVersion;

import org.jboss.seam.annotations.intercept.BypassInterceptors;

/**
 * 
 * 
 * @author Stephanie Stroka
 *			(stephanie.stroka@salzburgresearch.at)
 *
 */
@BypassInterceptors
public class RevisionBean {

	private String prettyDate;
	
	private String title;
	
	private String userlogin;
	
	private boolean checked = false;
	
	private CIVersion version;
	
	private String previewContentHtml;

	/**
	 * @return the prettyDate
	 */
	public String getPrettyDate() {
		return prettyDate;
	}

	/**
	 * @param prettyDate the prettyDate to set
	 */
	public void setPrettyDate(String prettyDate) {
		this.prettyDate = prettyDate;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the checked
	 */
	public boolean isChecked() {
		return checked;
	}

	/**
	 * @param checked the checked to set
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	/**
	 * @return the version
	 */
	public CIVersion getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(CIVersion version) {
		this.version = version;
	}

	/**
	 * @return the previewContentHtml
	 */
	public String getPreviewContentHtml() {
		return previewContentHtml;
	}

	/**
	 * @param previewContentHtml the previewContentHtml to set
	 */
	public void setPreviewContentHtml(String previewContentHtml) {
		this.previewContentHtml = previewContentHtml;
	}
	
	/**
	 * @return the userlogin
	 */
	public String getUserlogin() {
		return userlogin;
	}

	/**
	 * @param userlogin the userlogin to set
	 */
	public void setUserlogin(String userlogin) {
		this.userlogin = userlogin;
	}

	/**
	 * @return the version number
	 */
	public Long getVersionNumber() {
		return version.getVersionId();
	}
}
