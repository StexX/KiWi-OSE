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
package ideator.action.idea.wizard;

import java.util.LinkedList;

import kiwi.model.ontology.SKOSConcept;
import kiwi.model.user.User;

/**
 * @author Rolf Sint
 * @version 0.7
 * @since 0.7
 *
 */
public class CoAuthorTmp {
	
	private String login;
	
	private String coAuthorsFirstName;
	
	private String coAuthorsLastName;
	
	private String coAuthorsEmail;
	
	private String pwd;

	private LinkedList<SKOSConcept> positions;

	private User user;
	
	public String getCoAuthorsFirstName() {
		return coAuthorsFirstName;
	}

	public void setCoAuthorsFirstName(String coAuthorsFirstName) {
		this.coAuthorsFirstName = coAuthorsFirstName;
	}

	public String getCoAuthorsLastName() {
		return coAuthorsLastName;
	}

	public void setCoAuthorsLastName(String coAuthorsLastName) {
		this.coAuthorsLastName = coAuthorsLastName;
	}

	public String getCoAuthorsEmail() {
		return coAuthorsEmail;
	}

	public void setCoAuthorsEmail(String coAuthorsEmail) {
		this.coAuthorsEmail = coAuthorsEmail;
	}

	public LinkedList<SKOSConcept> getPositions() {
		return positions;
	}

	public void setPositions(LinkedList<SKOSConcept> positions) {
		this.positions = positions;
	}
	
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public void setUser(User user) {
		login = user.getLogin();
		coAuthorsFirstName = user.getFirstName();
		coAuthorsLastName = user.getLastName();
		coAuthorsEmail = user.getEmail();
		pwd = user.getPassword();
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public boolean exists() {
		return user != null;
	}
	
}
