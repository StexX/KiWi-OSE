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
import gate.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.FeatureSequence2FeatureVector;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.types.Instance;

import nu.xom.Node;
import nu.xom.Text;

import kiwi.api.informationextraction.InstanceExtractor;
import kiwi.model.content.ContentItem;
import kiwi.model.content.TextContent;
import kiwi.model.informationextraction.ClassifierEntity;
import kiwi.model.informationextraction.Context;
import kiwi.model.informationextraction.InstanceEntity;
import kiwi.model.informationextraction.InstanceExtractorEntity;
import kiwi.model.user.User;
import kiwi.util.KiWiXomUtils;
import kiwi.util.KiWiXomUtils.NodePos;
import kiwi.util.KiWiXomUtils.NodePosIterator;

/**
 * 
 * @author Marek Schmidt
 *
 */
public class NounPhraseInstanceExtractor implements InstanceExtractor {

	private Pipe pipe;
	
	@Override
	public Collection<InstanceEntity> extractInstances(InstanceExtractorEntity entity, ContentItem ci, Document doc) {
		
		Collection<InstanceEntity> ret = new LinkedList<InstanceEntity> ();
		
		TextContent tc = ci.getTextContent();
		if (tc == null) {
			return ret;
		}
			
		gate.AnnotationSet nounChunks = doc.getAnnotations().get("NounChunk");
		
		for (Annotation nounChunk : nounChunks) {
			InstanceEntity inst = new InstanceEntity();
			inst.setInstanceType(entity);
			inst.setSourceResource(ci.getResource());
			inst.setSourceTextContent(tc);
			
			Context ctx = generateBlockContext(tc.getXmlDocument(), 
					nounChunk.getStartNode().getOffset().intValue(),
					nounChunk.getEndNode().getOffset().intValue());
			
			inst.setContext(ctx);
			
			String featstr = generateContextFeatures(doc, nounChunk, ctx);
			
			// Create the mallet instance, process it with the processing pipe and store it.
			Instance malletInstance = new Instance(featstr, null, null, null);
			inst.setMalletInstance(pipe.instanceFrom(malletInstance));
			
			ret.add(inst);
		}
		
		return ret;
	}

	private void generateFeaturesFromGateTokens (List<gate.Annotation> tokens, String prefix, StringBuilder sb) {
		
		Set<String> nouns = new HashSet<String> ();
		nouns.add("NN");
		nouns.add("NNP");
		nouns.add("NNS");
		nouns.add("NNPS");
		
		int i = 0;
		for (Annotation token : tokens) {
			gate.FeatureMap f = token.getFeatures();
			String stem = (String)f.get("stem");
			String category = (String)f.get("category");
			String orth = (String)f.get("orth");
			
			// with position 
			if (i < 4) {
				sb.append(' ');
				sb.append(prefix);
				sb.append(i);
				sb.append('s');
				sb.append(stem);
		
				sb.append(' ');
				sb.append(prefix);
				sb.append(i);
				sb.append('c');
				sb.append(category);
			
				sb.append(' ');
				sb.append(prefix);
				sb.append(i);
				sb.append('o');
				sb.append(orth);
			}
			
			// without position
			if (nouns.contains(category)) {
				sb.append(' ');
				sb.append(prefix);
				sb.append('s');
				sb.append(stem);
			}
			
			++i;
			
			if (i > 10) break;
		}
	}
	
	/**
	 * Generate a context for a fragment specified by text coordinates.
	 * The context will be based on the the smallest common XML subtree.
	 * @param doc
	 * @param begin
	 * @return
	 */
	private Context generateBlockContext(nu.xom.Document doc, int begin, int end) {
		
		NodePos npBegin = KiWiXomUtils.getNode(doc, 0, begin);
		NodePos npEnd = KiWiXomUtils.getNode(doc, 0, end);
			
		Node parent = KiWiXomUtils.getLeastCommonParent(npBegin.getNode(), npEnd.getNode());
		
		Node contextBegin = KiWiXomUtils.getFirstTextNode(parent);
		Node contextEnd = KiWiXomUtils.getLastTextNode(parent);
		
		int contextBeginPos = -1;
		int contextEndPos = -1;
		
		NodePosIterator npi = new NodePosIterator(doc);
		while(npi.hasNext()) {
			NodePos np = npi.next();
			
			if (np.getNode().equals(contextBegin)) {
				contextBeginPos = np.getPos();
			}
			if (np.getNode().equals(contextEnd)) {
				if (contextEnd instanceof Text) {
					contextEndPos = np.getPos() + ((Text)contextEnd).getValue().length();
				}
			}
		}
		
		String plain = KiWiXomUtils.xom2plain(doc);
		if (contextBeginPos < 0) contextBeginPos = 0;
		if (contextEndPos < 0 || contextEndPos > plain.length()) contextEndPos = plain.length();
		
		Context ret = new Context();
		ret.setLeftContext(plain.substring(contextBeginPos, begin));
		ret.setInContext(plain.substring(begin, end));
		ret.setRightContext(plain.substring(end, contextEndPos));
		ret.setContextBegin(contextBeginPos);
		ret.setContextEnd(contextEndPos);
		ret.setInBegin(begin);
		ret.setInEnd(end);
		ret.setIsFragment(true);
		
		return ret;
	}
	

	private String generateContextFeatures(gate.Document doc, Annotation contextAnnotation, Context ctx) {

		gate.AnnotationSet tokens = doc.getAnnotations().get("Token");
		gate.AnnotationSet preTokensAS = null;
		gate.AnnotationSet postTokensAS = null;
		gate.AnnotationSet inTokensAS = null;
			
		preTokensAS = tokens.getContained((long)ctx.getContextBegin(), contextAnnotation.getStartNode().getOffset());
		postTokensAS = tokens.getContained(contextAnnotation.getEndNode().getOffset(), (long)ctx.getContextEnd());
		inTokensAS = tokens.getContained(contextAnnotation.getStartNode().getOffset(), contextAnnotation.getEndNode().getOffset());

		LinkedList<Annotation> preTokens = new LinkedList<Annotation> ();
		LinkedList<Annotation> postTokens = new LinkedList<Annotation> ();
		LinkedList<Annotation> inTokens = new LinkedList<Annotation> ();
		
		preTokens.addAll(preTokensAS);
		postTokens.addAll(postTokensAS);
		inTokens.addAll(inTokensAS);
		
		Comparator<Annotation> annotationComparator =  new Comparator<Annotation>() {
			@Override
			public int compare(Annotation o1, Annotation o2) {
				return o1.getStartNode().getOffset().compareTo(o2.getStartNode().getOffset());
		}};
		
		Comparator<Annotation> annotationComparatorReverse =  new Comparator<Annotation>() {
			@Override
			public int compare(Annotation o1, Annotation o2) {
				return o2.getStartNode().getOffset().compareTo(o1.getStartNode().getOffset());
		}};
			
		Collections.sort(preTokens, annotationComparatorReverse);
		Collections.sort(postTokens, annotationComparator);
		Collections.sort(inTokens, annotationComparator);
		
		StringBuilder sb = new StringBuilder();
		generateFeaturesFromGateTokens (preTokens, "-", sb);
		generateFeaturesFromGateTokens (postTokens, "+", sb);
		generateFeaturesFromGateTokens (inTokens, "", sb);
				
		return sb.toString();
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
		// TODO Auto-generated method stub
		
	}
}
