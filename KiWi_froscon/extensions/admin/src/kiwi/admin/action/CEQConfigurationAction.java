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
package kiwi.admin.action;

import java.io.Serializable;

import kiwi.api.config.ConfigurationService;
import kiwi.model.Constants;

import org.hibernate.validator.Range;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

@Name("kiwi.admin.ceqConfigurationAction")
@Scope(ScopeType.PAGE)
public class CEQConfigurationAction implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Range(min=0, max=100)
	private double addTag;
	@Range(min=0, max=100)
	private double  removeTag;
	@Range(min=0, max=100)
	private double  addContent;
	@Range(min=0, max=100)
	private double  editContent;
	@Range(min=0, max=100)
	private double  removeContent;
	@Range(min=0, max=100)
	private double  visitContent;
	@Range(min=0, max=100)
	private double  shareContent;
	@Range(min=0, max=100)
	private double  annotateContent;
	
	@Logger
	private Log log;
	
	@In
	private ConfigurationService configurationService;
	
	@Create
	public void begin() {
		addTag = configurationService.getConfiguration(Constants.CEQ_ADD_TAG_UNIT, "10.0").getDoubleValue();
		removeTag = configurationService.getConfiguration(Constants.CEQ_REMOVE_TAG_UNIT, "10.0").getDoubleValue();
		addContent = configurationService.getConfiguration(Constants.CEQ_ADD_CONTENT_UNIT, "10.0").getDoubleValue();
		editContent = configurationService.getConfiguration(Constants.CEQ_EDIT_CONTENT_UNIT, "10.0").getDoubleValue();
		removeContent = configurationService.getConfiguration(Constants.CEQ_REMOVE_CONTENT_UNIT, "10.0").getDoubleValue();
		visitContent = configurationService.getConfiguration(Constants.CEQ_VISIT_CONTENT_UNIT, "10.0").getDoubleValue();
		shareContent = configurationService.getConfiguration(Constants.CEQ_SHARE_CONTENT_UNIT, "10.0").getDoubleValue();
		annotateContent = configurationService.getConfiguration(Constants.CEQ_ANNOTATE_CONTENT_UNIT, "10.0").getDoubleValue();
	}
	
	public String save() {
		configurationService.setConfiguration(Constants.CEQ_ADD_TAG_UNIT, String.valueOf(addTag));
		configurationService.setConfiguration(Constants.CEQ_REMOVE_TAG_UNIT, String.valueOf(removeTag));
		configurationService.setConfiguration(Constants.CEQ_ADD_CONTENT_UNIT, String.valueOf(addContent));
		configurationService.setConfiguration(Constants.CEQ_REMOVE_CONTENT_UNIT, String.valueOf(removeContent));
		configurationService.setConfiguration(Constants.CEQ_EDIT_CONTENT_UNIT, String.valueOf(editContent));
		configurationService.setConfiguration(Constants.CEQ_VISIT_CONTENT_UNIT, String.valueOf(visitContent));
		configurationService.setConfiguration(Constants.CEQ_SHARE_CONTENT_UNIT, String.valueOf(shareContent));
		configurationService.setConfiguration(Constants.CEQ_ANNOTATE_CONTENT_UNIT, String.valueOf(annotateContent));
		log.info("saved ceq-config successfull");
		return "admin/ceq.xhtml";
	}
	
	public String cancel() {
		log.info("canceled ceq-config");
		return "admin/ceq.xhtml";
	}

	public double getAddTag() {
		return addTag;
	}

	public void setAddTag(double addTag) {
		this.addTag = addTag;
	}

	public double getRemoveTag() {
		return removeTag;
	}

	public void setRemoveTag(double removeTag) {
		this.removeTag = removeTag;
	}

	public double getAddContent() {
		return addContent;
	}

	public void setAddContent(double addContent) {
		this.addContent = addContent;
	}

	public double getEditContent() {
		return editContent;
	}

	public void setEditContent(double editContent) {
		this.editContent = editContent;
	}

	public double getRemoveContent() {
		return removeContent;
	}

	public void setRemoveContent(double removeContent) {
		this.removeContent = removeContent;
	}

	public double getVisitContent() {
		return visitContent;
	}

	public void setVisitContent(double visitContent) {
		this.visitContent = visitContent;
	}

	public double getShareContent() {
		return shareContent;
	}

	public void setShareContent(double shareContent) {
		this.shareContent = shareContent;
	}

	public double getAnnotateContent() {
		return annotateContent;
	}

	public void setAnnotateContent(double annotateContent) {
		this.annotateContent = annotateContent;
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}
	
}
