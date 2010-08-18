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
package kiwi.action.template;

import java.io.Serializable;

import kiwi.api.entity.KiWiEntityManager;
import kiwi.model.content.ContentItem;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * @author Rolf Sint
 * 
 */
@Name("sFormManage")
@Scope(ScopeType.CONVERSATION)
public class SFormManage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@In(create = true)
	private ContentItem currentContentItem;

	@In(create = true)
	private KiWiEntityManager kiwiEntityManager;

	@Logger
	private static Log log;

	private String fileName;

	private String formName;
	
//	private List<SForm> formList;
//
//	@Create
//	public void init(){
//		formList = currentContentItem.getsForms();
//	}
//	
//	//Todocreate
//	
//
//	public void add() {
//		log.info("Adding new form ...");
//			
//		//TODO: Check if the form already exists
//		
//		SForm sform = new SForm();
//
//		sform.setFilename(fileName);
//		sform.setName(formName);
//
//		kiwiEntityManager.persist(sform);
//
//		formList.add(sform);
//		
//		currentContentItem.setsForms(formList);
//		kiwiEntityManager.persist(currentContentItem);
//		FacesMessages.instance().add("Form "+formName+" added");
//		log.info(formName);
//		log.info(fileName);
//		fileName = "";
//		formName="";
//	}
//		
//	public List<String> getFiles(){
//		String path = System.getProperty("ear.path");		
//		path = path + "kiwiext-semForms.jar";
//		log.info("System ear path "+path);
//		File f = new File(path); 
//		log.info("file exists "+f.exists());
//		File [] fl = f.listFiles();
//		LinkedList<String> ll = new LinkedList<String>();
//		
//		for(int i = 0; i < fl.length; i++){
//			String fileName = fl[i].getName();
//			if(fileName.contains("html")){
//				ll.add(fl[i].getName());
//			}
//		}
//		return ll;		
//	}
//	
//	public List<SForm> getForms() {
//		for (Iterator<SForm> iterator = formList.iterator(); iterator.hasNext();) {
//			SForm sform = iterator.next();
//			log.info("Value:: "+sform.getName());
//		}
//		return formList;
//	}
//	
//	public void remove(SForm form) {
//		log.info("Trying to remove form "+form.getName());
//		List<SForm> cList = currentContentItem.getsForms();
//		cList.remove(form);
//		currentContentItem.setsForms(cList);
//		kiwiEntityManager.persist(currentContentItem);
//	}
//
//	public String getFileName() {
//		return fileName;
//	}
//
//	public void setFileName(String fileName) {
//		this.fileName = fileName;
//	}
//
//	public String getFormName() {
//		return formName;
//	}
//
//	public void setFormName(String formName) {
//		this.formName = formName;
//	}
}
