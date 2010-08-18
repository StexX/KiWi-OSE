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

package kiwi.service.render;

import java.util.LinkedList;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateless;

import kiwi.api.config.ConfigurationService;
import kiwi.api.event.KiWiEvents;
import kiwi.api.render.StoringServiceLocal;
import kiwi.api.render.StoringServiceRemote;
import kiwi.model.content.MediaContent;
import kiwi.model.content.TextContent;
import kiwi.model.kbase.KiWiResource;
import kiwi.service.render.savelet.MediaContentSavelet;
import kiwi.service.render.savelet.SourceSavelet;
import kiwi.service.render.savelet.TextContentSavelet;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * The storing pipeline provides functionality that is executed when storing a content item. For example,
 * it may be used to bring the content in a normalised, cleaned form or for extracting information out of the
 * content. Note that the StoringPipeline does not take care of the actual storing itself; this is done by the
 * KiWiKnowledgeStore. 
 * 
 * 
 * @author sschaffe
 *
 */
@Stateless
@Name("storingPipeline")
@Scope(ScopeType.STATELESS)
@AutoCreate
@Install(dependencies="configurationService")
public class StoringServiceImpl implements StoringServiceLocal, StoringServiceRemote {

	@In(value="storingService.textSavelets",scope=ScopeType.APPLICATION, required=false)
	@Out(value="storingService.textSavelets",scope=ScopeType.APPLICATION)
	private List<TextContentSavelet> textSavelets;

	@In(value="storingService.mediaSavelets",scope=ScopeType.APPLICATION, required=false)
	@Out(value="storingService.mediaSavelets",scope=ScopeType.APPLICATION)
	private List<MediaContentSavelet> mediaSavelets;

	@In(value="storingService.sourceSavelets",scope=ScopeType.APPLICATION, required=false)
	@Out(value="storingService.sourceSavelets",scope=ScopeType.APPLICATION)
	private List<SourceSavelet> sourceSavelets;

	@Logger
	private static Log log;

	@In(value="configurationService") 
	ConfigurationService configurationService;
	
	
	@Observer(KiWiEvents.CONFIGURATIONSERVICE_INIT)
    public void initialise(String dummyString) {
    	log.info("initialising storing pipeline ...");
    	
    	textSavelets = new LinkedList<TextContentSavelet>();
    	mediaSavelets = new LinkedList<MediaContentSavelet>();
    	sourceSavelets = new LinkedList<SourceSavelet>();
    	
    	// initialise savelets from configuration in kspace
    	// savelets.source -> sourceSavelets
    	// savelets.text   -> textSavelets
    	// savelets.media  -> mediaSavelets
    	
    	for(String cls : configurationService.getConfiguration("savelets.source").getListValue()) {
    		initSavelet(sourceSavelets,cls);
    	}
    	log.info("storing pipeline: #0 source savelets", sourceSavelets.size());
    	
    	for(String cls : configurationService.getConfiguration("savelets.text").getListValue()) {
    		initSavelet(textSavelets,cls);
    	}
    	log.info("storing pipeline: #0 text savelets", textSavelets.size());

    	for(String cls : configurationService.getConfiguration("savelets.media").getListValue()) {
    		initSavelet(mediaSavelets,cls);
    	}
    	log.info("storing pipeline: #0 media savelets", mediaSavelets.size());
    }

    
    
    private <T> void initSavelet(List<T> list, String cls) {
    	try {
			//T savelet = (T)Class.forName(cls).newInstance();
			T savelet = (T)Component.getInstance(cls);
    		
			if(savelet != null)
				list.add(savelet);
			else
				log.warn("warning: savelet #0 was null after initialisation", cls);
			
		} catch (Exception e) {
			log.error("error while instantiating savelet #0: #1",cls,e.getMessage());
		}
    }
    
    
    /**
     * Process savelets operating on the raw HTML source, e.g. for running regular expressions over
     * the content. Typically, this method is called *before* actually creating the TextContent object and
     * calling processTextContent.
     * 
     * @param context
     * @param content
     * @return
     */
    public String processHtmlSource(KiWiResource context, String content) {
    	String result = content;
    	long start;
    	for(SourceSavelet savelet : sourceSavelets) {
    		start = System.currentTimeMillis();
    		result = savelet.apply(context,result);
    		log.debug("running savelet #0 took #1 ms", savelet.getClass().getName(),System.currentTimeMillis() - start);
    	}
    	return result;
    }
    
    /**
     * Process savelets operating on TextContent objects. These savelets may e.g. operate on the XOM representation
     * of the content using XPath or on the relations of the ContentItem. Typically called after processHtmlSource
     * 
     * @param context
     * @param content
     * @return
     */
    public TextContent processTextContent(KiWiResource context, TextContent content) {
    	TextContent result = content;
    	if(content != null) {
	    	long start;
	    	for(TextContentSavelet savelet : textSavelets) {
	    		start = System.currentTimeMillis();
	    		result = savelet.apply(context,result);
	    		log.debug("running savelet #0 took #1 ms", savelet.getClass().getName(),System.currentTimeMillis() - start);
	    	}
    	}
    	return result;
    }
    
    /**
     * Process savelets operating on MediaContent objects, e.g. for extracting or adding metadata to the
     * media object.
     * 
     * @param context
     * @param content
     * @return
     */
    public MediaContent processMediaContent(KiWiResource context, MediaContent content) {
    	MediaContent result = content;
    	for(MediaContentSavelet savelet : mediaSavelets) {
    		result = savelet.apply(context,result);
    	}
    	return result;    	
    }
    
    
    @Remove
    public void shutdown() {
    }
}
