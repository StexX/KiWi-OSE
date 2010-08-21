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
package kiwi.service.system;

import java.util.EventListener;
import java.util.EventObject;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;

import kiwi.api.system.StatusServiceLocal;
import kiwi.api.system.StatusServiceRemote;
import kiwi.model.status.SystemStatus;

import org.ajax4jsf.event.PushEventListener;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * StatusServiceImpl
 *
 * @author Sebastian Schaffert
 *
 */
@Stateless
@Name("kiwi.core.statusService")
@Scope(ScopeType.STATELESS)
@AutoCreate
public class StatusServiceImpl implements StatusServiceLocal, StatusServiceRemote {
	
	@Logger
	private Log log;
	
	@In(value="statusService.systemStatus", scope=ScopeType.APPLICATION, required=false)
	@Out(value="statusService.systemStatus", scope=ScopeType.APPLICATION, required=false)
	private LinkedList<SystemStatus> systemStatus;
	
	@In(value="statusService.statusListeners", scope=ScopeType.APPLICATION, required=false)
	@Out(value="statusService.statusListeners", scope=ScopeType.APPLICATION, required=false)
	private LinkedList<EventListener> statusListeners;
	
	
	
	/* (non-Javadoc)
	 * @see kiwi.api.system.StatusService#addSystemStatus(kiwi.model.status.SystemStatus)
	 */
	@Override
	public void addSystemStatus(SystemStatus status) {
		getSystemStatus().add(status);
		notifyListeners(status);
		
		log.info("#0 started running ...", status.getName());
	}

	/* (non-Javadoc)
	 * @see kiwi.api.system.StatusService#getSystemStatus()
	 */
	@Override
	public List<SystemStatus> getSystemStatus() {
		if(systemStatus == null) {
			systemStatus = new LinkedList<SystemStatus>();
		}
		return systemStatus;
	}
	
	/* (non-Javadoc)
	 * @see kiwi.api.system.StatusService#getSystemStatus(java.lang.String)
	 */	
	@Override 
	public SystemStatus getSystemStatus(String id) {
		if (id == null)
			return null;
		
		for (SystemStatus status : systemStatus) 
			if ( id.equals(status.getId()) )
				return status;
		
		return null;
	}

	/* (non-Javadoc)
	 * @see kiwi.api.system.StatusService#removeSystemStatus(kiwi.model.status.SystemStatus)
	 */
	@Override
	public void removeSystemStatus(SystemStatus status) {
		getSystemStatus().remove(status);
		notifyListeners(status);
		
		log.info("#0 stopped running", status.getName());
	}
	
	private void notifyListeners(SystemStatus status) {
		for(EventListener l : getListeners()) {
			if(l instanceof PushEventListener) {
				((PushEventListener)l).onEvent(new EventObject(status));
			}
		}
	}
	
	private LinkedList<EventListener> getListeners() {
		if(statusListeners == null) {
			statusListeners = new LinkedList<EventListener>();
		}
		return statusListeners;
	}

	@Override
	public void addListener(EventListener listener) {
		if(statusListeners == null) {
			statusListeners = new LinkedList<EventListener>();
		}
		statusListeners.add(listener);
		
	}
	
	
}
