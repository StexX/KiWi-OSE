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
package kiwi.mobile;


import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;


/**
 * This session scoped seam component is used to store
 * information about the actual mobile device.
 * 
 * @author Mihai
 * @version 04-pre
 * @since 04-pre
 */
@Scope(ScopeType.SESSION)
@Name("mobileConfiguration")
@AutoCreate
public class MobileConfiguration implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * If is true the client for the session is a mobile device.
     */
    private boolean isMobileDevice;

    /**
     * Identify the mobile device.
     */
    private MobileDevice device;

    /**
     * Returns true the client for the session is a mobile
     * device.
     * 
     * @return true the client for the session is a mobile
     *         device.
     */
    public boolean isMobileDevice() {
        return isMobileDevice;
    }

    /**
     * Enables the mobile device.
     */
    public void enableMobileDevice() {
        isMobileDevice = true;
    }

    /**
     * Disable the mobile device.
     */
    public void disableMobileDevice() {
        isMobileDevice = false;
    }

    /**
     * Enable / disable the mobile device.
     * 
     * @param isMobileDevice true to enable the mobile device.
     */
    public void setIsMobileDevice(boolean isMobileDevice) {
        this.isMobileDevice = isMobileDevice;
    }

    /**
     * Returns true the client for the session is a mobile
     * device.
     * 
     * @return true the client for the session is a mobile
     *         device.
     */
    public boolean getIsMobileDevice() {
        return isMobileDevice;
    }

    /**
     * Returns the actual mobile device, if the result is null
     * then there is no registered device.
     * 
     * @return the actual mobile device.
     */
    public MobileDevice getDevice() {
        return device;
    }

    /**
     * Register a new mobile device.
     * 
     * @param device the device to set.
     */
    public void setDevice(MobileDevice device) {
        this.device = device;
    }
}
