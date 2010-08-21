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

package kiwi.service.user;


import java.util.Properties;
import java.util.Set;

import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import kiwi.api.user.MailServiceLocal;
import kiwi.api.user.MailServiceRemote;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * The following service should be used 
 * - to send an activation eMail to users who recently registered at the KiWi platform,
 * - to reset a password for a user
 * - to inform about changes on ContentItems that are followed by the user
 * - etc.
 * 
 * 
 * @author Stephanie Stroka 
 * 			(stephanie.stroka@salzburgresearch.at)
 *
 */
@Stateless
@Name("mailService")
@Scope(ScopeType.STATELESS)
public class MailServiceImpl implements MailServiceLocal, MailServiceRemote {

	@Logger
	private Log log;
		
	/**
	 * Sends an email to one or more 
	 * than one receivers with a given subject and a given content
	 */
	@Override
	public boolean sendMail(Set<String> receiver, String subject, String content) {
		try {
			Properties props = new Properties();
		//	props.put("mail.smtp.host", "smtp.gmail.com");
			Session s = Session.getInstance(props);
			MimeMessage message = new MimeMessage(s);
			/* TODO: This email address should be configurable!! */
//			InternetAddress from = new InternetAddress("kiwi@kiwi-project.eu");
//			message.setFrom(from);
			for(String r : receiver) {
				InternetAddress to = new InternetAddress(r);
				message.addRecipient(Message.RecipientType.TO, to);
			}
			message.setSubject(subject);
			message.setText(content);
			Transport.send(message);
			return true;
		} catch (AddressException e) {
			log.error("Address not found: #0 ", e.getMessage());
			return false;
		} catch (MessagingException e) {
			log.error("MailService failed to send message (MessagingException): #0 ", e.getMessage());
			return false;
		}
	}
}
