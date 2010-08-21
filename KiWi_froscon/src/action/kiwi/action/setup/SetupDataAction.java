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
package kiwi.action.setup;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.persistence.EntityManager;

import kiwi.api.config.ConfigurationService;
import kiwi.api.search.SemanticIndexingService;
import kiwi.model.perspective.Perspective;
import kiwi.service.config.ConfigurationServiceImpl;

import org.apache.commons.io.FileUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;


/**
 * SetupDataAction
 *
 * @author Sebastian Schaffert
 *
 */
@Name("kiwi.setup.setupDataAction")
@Scope(ScopeType.PAGE)
//@Transactional
public class SetupDataAction implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger
    private Log log;

    @In
    FacesMessages facesMessages;


    
    @In("kiwi.core.semanticIndexingService")
    private SemanticIndexingService semanticIndexingService;

    private boolean runSemVector = true;

    private boolean loadHelp = false;

    private boolean loadDemo = false;

    private boolean changePersistence = true;

    public String loadData() {
        doSemanticIndexing();
        doChangePersistence();

        ConfigurationServiceImpl.setupInProgress = false;
        ConfigurationServiceImpl.configurationInProgress = false;
        return "success";
    }
    
    
    

    public void doSemanticIndexing() {
        if(runSemVector) {
            semanticIndexingService.reIndex();
        }
    }

    public void doChangePersistence() {
        if(changePersistence) {
            // TODO: it's a hack!
            String earPath = System.getProperty("ear.path");

            File kiwiP = new File(earPath + File.separator + "KiWi.jar" +
                                           File.separator + "META-INF" +
                                           File.separator + "persistence.xml");
            try {
                String s = FileUtils.readFileToString(kiwiP);
                s = s.replace("\"create\"", "\"validate\"");
                FileUtils.writeStringToFile(kiwiP,s);
            } catch (IOException e) {
                facesMessages.add("I/O error while reading KiWi persistence.xml");
                log.error("I/O error while reading KiWi persistence.xml",e);
            }
        }
    }

    /**
     * @return the runSemVector
     */
    public boolean isRunSemVector() {
        return runSemVector;
    }



    /**
     * @param runSemVector the runSemVector to set
     */
    public void setRunSemVector(boolean runSemVector) {
        this.runSemVector = runSemVector;
    }



    /**
     * @return the loadHelp
     */
    public boolean isLoadHelp() {
        return loadHelp;
    }



    /**
     * @param loadHelp the loadHelp to set
     */
    public void setLoadHelp(boolean loadHelp) {
        this.loadHelp = loadHelp;
    }



    /**
     * @return the loadDemo
     */
    public boolean isLoadDemo() {
        return loadDemo;
    }



    /**
     * @param loadDemo the loadDemo to set
     */
    public void setLoadDemo(boolean loadDemo) {
        this.loadDemo = loadDemo;
    }



    /**
     * @return the changePersistence
     */
    public boolean isChangePersistence() {
        return changePersistence;
    }



    /**
     * @param changePersistence the changePersistence to set
     */
    public void setChangePersistence(boolean changePersistence) {
        this.changePersistence = changePersistence;
    }


}
