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
package kiwi.service.informationextraction;

import gate.Annotation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.regex.Pattern;

import kiwi.api.informationextraction.InstanceExtractor;
import kiwi.api.tagging.TaggingService;
import kiwi.model.content.ContentItem;
import kiwi.model.content.TextContent;
import kiwi.model.informationextraction.ClassifierEntity;
import kiwi.model.informationextraction.Context;
import kiwi.model.informationextraction.InstanceEntity;
import kiwi.model.informationextraction.InstanceExtractorEntity;
import kiwi.model.user.User;

import org.jboss.seam.Component;

import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.FeatureSequence2FeatureVector;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.types.Instance;

public class DocumentInstanceExtractor implements InstanceExtractor {
	
	private Pipe pipe;

	@Override
	public Collection<InstanceEntity> extractInstances(InstanceExtractorEntity entity, ContentItem ci, gate.Document doc) {
		
		Collection<InstanceEntity> ret = new LinkedList<InstanceEntity> ();
		
		TextContent tc = ci.getTextContent();
		if (tc == null) {
			return ret;
		}
		
		//gate.AnnotationSet nounChunks = doc.getAnnotations().get("NounChunk");
		gate.AnnotationSet tokens = doc.getAnnotations().get("Token");
		
		StringBuilder feat = new StringBuilder();
		for (Annotation token : tokens) {
			gate.FeatureMap f = token.getFeatures();
			String stem = (String)f.get("stem");
			
			feat.append(" ");
			feat.append(stem);
		}
		
		InstanceEntity inst = new InstanceEntity();
		inst.setInstanceType(entity);
		inst.setSourceResource(ci.getResource());
		inst.setSourceTextContent(tc);
		inst.setContext(new Context());
			
		// Create the mallet instance, process it with the processing pipe and store it.
		Instance malletInstance = new Instance(feat.toString(), null, null, null);
		inst.setMalletInstance(pipe.instanceFrom(malletInstance));
			
		ret.add(inst);
		
		return ret;
	}

	@Override
	public void init(InstanceExtractorEntity entity) {
		ArrayList<Pipe> pipeList = new ArrayList<Pipe>();
		Pattern tokenPattern =
            Pattern.compile("\\S+");
		pipeList.add(new CharSequence2TokenSequence(tokenPattern));
		
		pipeList.add(new TokenSequence2FeatureSequence(entity.getMalletAlphabet()));
		
		pipeList.add(new FeatureSequence2FeatureVector());
		 
		pipe = new SerialPipes(pipeList);
		pipe.setTargetProcessing(false);
	}

	@Override
	public void realizeInstance(InstanceExtractorEntity entity,
			ClassifierEntity classifier, User user, InstanceEntity instance) {
		
		TaggingService taggingService = (TaggingService)Component.getInstance("taggingService");
		taggingService.createTagging(classifier.getResource().getLabel(), instance.getSourceResource().getContentItem(), classifier.getResource().getContentItem(), user);
	}
}
