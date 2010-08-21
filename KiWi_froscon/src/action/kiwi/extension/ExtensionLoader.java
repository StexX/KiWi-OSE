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
package kiwi.extension;


import java.io.IOException;
import java.util.List;

import kiwi.api.extension.ExtensionService;
import kiwi.api.extension.KiWiApplication;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;


/**
 * Used to install all the extensions described with descriptors
 * in the <i>kiwi.extension.descriptors</i> package. This is done
 * with the install extension method - this method is triggered
 * by the seam container in the post initialization pahse.
 *
 * @author mradules
 * @version 00.01-1
 * @since 00.01-1
 */
@Name("kiwi.ExtensionLoader")
@Scope(ScopeType.STATELESS)
public class ExtensionLoader {

    /**
     * The package for the extensions descriptors.
     */
    private static final String BASE_PACKAGE = "kiwi.extension.descriptor";

    /**
     * The Logger instance. All log messages from this class are
     * routed through this member.
     */
    @Logger
    private Log log;

    /**
     * Used to register the extension.
     */
    @In
    private ExtensionService extensionService;

    /**
     * Default constructor.
     */
    public ExtensionLoader() {
        // UNIMPLEMENTED
    }

    @Observer("org.jboss.seam.postInitialization")
    public void installExtension() {

        log.info("KiWi TagIT Extension initialising ...");

        final List<KiWiApplication> apps;
        try {
            apps = DescriptorFactory.getDescriptors(BASE_PACKAGE);
        } catch (final IOException e) {
            log.warn("Extension load fails", e);
            // FIXME : here the exception must be propagate on
            // the upper levels.
            return;
        }

        for (final KiWiApplication app : apps) {
            register(extensionService, app);
        }
    }

    /**
     * Checks if the given <code>ExtensionService</code> contains
     * already a certain <code>KiWiApplication</code> and if not
     * then it register it.
     *
     * @param service the <code>ExtensionService</code> where the
     *            application is register.
     * @param app the application to register.
     */
    private void register(ExtensionService service, KiWiApplication app) {
        final String name = app.getName();
        final KiWiApplication getIt = service.getApplication(name);
        if (getIt == null) {
            service.registerApplication(app);
            log.debug("Extension #0 was succesfull install", name);
        }
    }
}
