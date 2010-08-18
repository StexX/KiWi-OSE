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
 * sschaffe
 * 
 */
package kiwi.api.system;

import java.util.EventListener;
import java.util.List;

import kiwi.model.status.SystemStatus;

/**
 * The KiWi StatusService offers functionality to display the current system status to users, e.g.
 * when long-running tasks (like reasoning, importing, indexing) are executed in the background.
 * The purpose of this service is to notify users that "something is going on" and that there may
 * be upcoming changes.
 *
 * @author Sebastian Schaffert
 *
 */
public interface StatusService {

	/**
	 * Add a system status information object to the current system status, to be displayed to users 
	 * accessing the system. The StatusService only holds a reference to the status information
	 * object, so the component adding the status can update information in the system status
	 * 
	 * @param status a SystemStatus object containing the status information to be displayed to users
	 */
	public void addSystemStatus(SystemStatus status);
	
	/**
	 * Remove a system status information object from the current system status so that it is no
	 * longer displayed to users.
	 * 
	 * @param status the status object to be removed; must be the same object that was used for
	 *               adding the status
	 */
	public void removeSystemStatus(SystemStatus status);
	
	/**
	 * Return a list of all currently active system status information objects.
	 * 
	 * @return
	 */
	public List<SystemStatus> getSystemStatus();
	
	/** Returns system status by its id.
	 * 
	 * @param id Id of the system status.
	 * @return Returns system status with the id if such exists and null otherwise. For id == null returns null.
	 */
	public SystemStatus getSystemStatus(String id);
	
	/**
	 * Add a Richfaces event listener so that the user interface gets notified that the overall
	 * system status has been updated.
	 * 
	 * @param listener
	 */
	public void addListener(EventListener listener);
}
