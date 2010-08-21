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

import javax.mail.MethodNotSupportedException;

import kiwi.exception.ContentItemDoesNotExistException;
import kiwi.model.revision.CIVersion;

/**
 * KiWiUpdateService is an interface for standard
 * procedures that take place during an update.
 * It's implementation delegates to the specific updateservices.
 * @author 	Stephanie Stroka 
 * 			(stephaniestroka@salzburgresearch.at)
 *
 */
public interface KiWiUpdateService {
	
	/**
	 * Restores the changes of a version
	 * @param version the version that will be restored
	 * @throws ContentItemDoesNotExistException 
	 * @throws MethodNotSupportedException 
	 */
	public void restore(CIVersion verion) throws ContentItemDoesNotExistException;
	
	/**
	 * Reverts the changes of a version
	 * @param version the version that will be restored
	 * @throws ContentItemDoesNotExistException 
	 */
	public void undo(CIVersion version) throws ContentItemDoesNotExistException;
	
	/**
	 * Commits the update of a version
	 * @param version
	 */
	public void commitUpdate(CIVersion version);
	
	/**
	 * Rolls back the update of a version
	 * @param version
	 */
	public void rollbackUpdate(CIVersion version);
}	
