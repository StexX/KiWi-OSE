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
package artaround.action.register;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;

import kiwi.action.user.RegisterAction;
import kiwi.api.content.ContentItemService;
import kiwi.api.multimedia.MultimediaService;
import kiwi.exception.RegisterException;
import kiwi.exception.UserExistsException;
import kiwi.model.content.MediaContent;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import artaround.service.ArtAroundUserService;

/**
 * @author Rolf Sint
 * @version 0.7
 * @since 0.7
 * 
 */
@Name("artaroundRegisterAction")
@Scope(ScopeType.PAGE)
//@Transactional
public class ArtaroundRegisterAction extends RegisterAction implements Serializable {
	
    @Logger
    private Log log;
	
	private Date gebDate;
			
	@In(value = "artaround.userService")
	private ArtAroundUserService artaroundUserService;
	
	@In(create = true, value= "artaround.profilePictureAction")
	private ProfilePictureAction profilePictureAction;
	
	@In
	private MultimediaService multimediaService;
	
	@In
	private ContentItemService contentItemService;
		
	public Date getGebDate() {
		return gebDate;
	}

	public void setGebDate(Date gebDate) {
		this.gebDate = gebDate;
	}
	
	private kiwi.model.content.MediaContent profilePicture;
	
	
	public void listener(UploadEvent event) {
		
		profilePicture = profilePictureAction.upload(event);
		
//		log.info(item.getFileName() == null?"no data":"data exists");
//		log.info(item.getData() == null?"no data":"data exists");
//		
//		
//		log.info("File: '#0' with type '#1' was uploaded", item.getFileName(),
//				item.getContentType());
//		
//		byte [] data = item.getData();
//		if (item.isTempFile()) {
//			try {
//				data = FileUtils.readFileToByteArray(item.getFile());
//			} catch (IOException e) {
//				log.error("error reading file #0", item.getFile()
//						.getAbsolutePath());
//			}
//		}
//		
//		String name = FilenameUtils.getName(item.getFileName());
//		String type = item.getContentType();
//		
//		type = multimediaService.getMimeType(name, data);
//		
//
//		log.info(data == null?"no data":"data exists"+data.length);
//		
//		// TODO: MediaContent is not yet revised. 
//		// Change to ContentItemService.updateMediaContent to historize it
//		profilePicture = new kiwi.model.content.MediaContent();
//		profilePicture.setData(data);		
//		profilePicture.setFileName(name);
//		profilePicture.setMimeType(type);

//		UploadItem item = event.getUploadItem();
//		
//		
//		log.info(item.getFileName() == null?"no data":"data exists");
//		log.info(item.getData() == null?"no data":"data exists");
//		
//		
//		log.info("File: '#0' with type '#1' was uploaded", item.getFileName(),
//				item.getContentType());
//		
//		byte [] data = item.getData();
//		if (item.isTempFile()) {
//			try {
//				data = FileUtils.readFileToByteArray(item.getFile());
//			} catch (IOException e) {
//				log.error("error reading file #0", item.getFile()
//						.getAbsolutePath());
//			}
//		}
//		
//		String name = FilenameUtils.getName(item.getFileName());
//		String type = item.getContentType();
//		
//		type = multimediaService.getMimeType(name, data);
//		
//
//		log.info(data == null?"no data":"data exists"+data.length);
//		
//		
//		profilePicture = new kiwi.model.content.MediaContent();
//		profilePicture.setData(data);		
//		profilePicture.setFileName(name);
//		profilePicture.setMimeType(type);
		
	}

	protected void createUser() throws RegisterException, UserExistsException {
		log.info(gebDate);
		if(profilePicture != null){
			byte [] x = profilePicture.getData();
			log.info(x == null?"no data":"data exists"+x.length);
		}
		else{
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("person1.png");
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			
			try{
			int a = 0;
			while((a = (int) is.read()) != -1){
				bos.write(a);
			}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			byte b[] = bos.toByteArray();
			
			profilePicture = new MediaContent();
			profilePicture.setData(b);
			profilePicture.setFileName("dummy");
		}

		log.info(profilePicture == null?"no profilePicture":"profilePicture exists");
		
		artaroundUserService.createUser(login, firstName, lastName, password,email, 1, 1, gebDate,profilePicture);
		setSuccess(true);
	}

	
}
