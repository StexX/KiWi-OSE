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

import java.lang.management.ManagementFactory;
import java.util.Random;

import javax.ejb.Stateless;

import kiwi.api.user.PasswordGeneratorServiceLocal;
import kiwi.api.user.PasswordGeneratorServiceRemote;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 * The password-generator generates pseudo-random passwords for users.
 * The password-length differs between 8 and 11.
 * The password may contain the following characters:
 * 		0-9 A-Z a-z !"$%&'()*+,-./:;<=>?@[\]^_`{|}~ 
 * 
 * @author Stephanie Stroka
 *			(stephanie.stroka@salzburgresearch.at)
 *
 */
@Name("passwordGenerator")
@Stateless
@AutoCreate
@Scope(ScopeType.STATELESS)
public class PasswordGeneratorServiceImpl 
	implements PasswordGeneratorServiceLocal, PasswordGeneratorServiceRemote {

	/**
	 * Generates pseudo-random passwords with a length between 8 and 11 characters.
	 * 
	 * Probably not very secure...anyway, for the beginning, it's good enough
	 */
	@Override
	public String generatePassword() {
		Random rnd = new Random(
				System.currentTimeMillis() % 
				ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime());
		int length = 8 + rnd.nextInt(3);
		return generatePassword(length);
	}
	
	@Override
	public String generatePassword(int length) {
		Random rnd = new Random(
				System.currentTimeMillis() % 
				ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime());
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < length; i++) {
			int r = rnd.nextInt(126-33);
			sb.append((char) (r + 33));
		}
		return sb.toString();
	}

	/**
	 * The following method tests the password generation... 
	 * @param args
	 */
	public static void main(String[] args) {
		PasswordGeneratorServiceImpl pwgen = new PasswordGeneratorServiceImpl();
		for(int i = 0; i < 40; i++) {
			String test = pwgen.generatePassword();
			System.out.println("Generated password: " + test + " with length " + test.length());
			// The following prevents that the method returns the same password during a fast run.
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
