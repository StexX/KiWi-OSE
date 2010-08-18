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

import gate.Document;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Locale;

import kiwi.api.informationextraction.Extractlet;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.content.ContentItem;
import kiwi.model.content.TextContent;
import kiwi.model.informationextraction.Context;
import kiwi.model.informationextraction.InstanceEntity;
import kiwi.model.informationextraction.Suggestion;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * This is an example of a custom extractlet. 
 * It creates suggestions for a nested item of type nlp:ActionItem 
 * if the paragraph stars with the string "AP:"
 * @author Marek Schmidt 
 *
 */
@Name("kiwi.informationextraction.actionItemExtractlet")
@Scope(ScopeType.STATELESS)
public class ActionItemExtractlet extends AbstractExtractlet {
	
	public ActionItemExtractlet() {
		super("kiwi.informationextraction.actionItemExtractlet");
	}

	@In(create=true)
	TripleStore tripleStore;
	
	@Logger
	private Log log;

	@Override
	public Collection<Suggestion> extract(KiWiResource context,
			TextContent content, Document gateDoc, Locale language) {
		Collection<Suggestion> ret = new LinkedList<Suggestion> ();
		
		String plain = (String)gateDoc.getFeatures().get(Document.DOCUMENT_STRING_CONTENT_PARAMETER_NAME);
		// log.info("extracting actionitems from: #0", plain);
		
		if (plain == null) {
			return ret;
		}
		
		if (context == null) {
			return ret;
		}
		
		KiWiResource actionItemType = tripleStore.createUriResource("http://nlp.fit.vutbr.cz/2010/nlp#ActionItem");
		// do not annotate pages that are action items already
		for (KiWiResource type : context.getTypes()) {
			if (actionItemType.getKiwiIdentifier().equals(type.getKiwiIdentifier())) {
				return ret;
			}
		}
		
		int i = 0;
		while(true) {
			i = plain.indexOf("AP:", i);
			if (i == -1) {
				break;
			}
			
			int start = i;
			int end = plain.indexOf("\n", start);
			if (end == -1) {
				end = plain.length();
			}
			
			Context ctx = new Context();
			ctx.setIsFragment(true);
			ctx.setContextBegin(start);
			ctx.setContextEnd(end);
			ctx.setInBegin(start);
			ctx.setInEnd(end);
			
			InstanceEntity inst = new InstanceEntity();
			inst.setSourceResource(context);
			inst.setSourceTextContent(content);
			inst.setContext(ctx);
			
			Suggestion suggestion = new Suggestion();
			suggestion.setLabel("Action Item");
			suggestion.setKind(Suggestion.NESTED_ITEM);
			suggestion.getTypes().add(actionItemType);
			
			suggestion.setInstance(inst);
			suggestion.setScore(1.0f);
			
			ret.add(suggestion);
			
			i = end;
		}
		
		return ret;
	}
}
