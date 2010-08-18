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
 * Contributor(s): Jakub Kotowski
 * 
 * 
 */
package kiwi.service.reasoning;

import java.util.List;

import kiwi.api.system.StatusService;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.status.SystemStatus;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

@Scope(ScopeType.STATELESS)
@AutoCreate
@Name("triplestoreTBoxLoader")
public class TriplestoreTBoxLoader implements TBoxLoader {
	@Logger
	private Log log;
	@In TripleStore tripleStore;
	@In("TBox") TBox tbox;
	@In("kiwi.core.statusService")
	private StatusService statusService;

	
	public void load() {
		log.info("Loading TBox from triple store.");
		SystemStatus status = statusService.getSystemStatus("reasonerStatus");

		
		status.setMessage("Loading TBox (meta property assertions)");
		List<KiWiTriple> triples;
		
		int count = 0;
		for (TBox.MetaProperty property : TBox.MetaProperty.values()) {
			triples = tripleStore.getTriplesByP(property.getUri());
			tbox.addAll(triples);
			count += triples.size();
		}
		
		log.debug("Loaded #0 MetaProperty membership assertions.", count);
		
		status.setMessage("Loading TBox (meta class assertions)");
		count = 0;
		for (TBox.MetaClass cls : TBox.MetaClass.values()) {
			triples = tripleStore.getTriplesByPO(ReasoningConstants.RDF_TYPE, cls.getUri());
			tbox.addAll(triples);
			count += triples.size();
		}
		
		log.debug("Loaded #0 MetaClass membership assertions.", count);
		
		status.setMessage("Loading TBox (RDF collections)");		
		count = 0;
		triples = tripleStore.getTriplesByP(ReasoningConstants.RDF_FIRST);
		tbox.addAll(triples);
		count += triples.size();

		triples = tripleStore.getTriplesByP(ReasoningConstants.RDF_REST);
		tbox.addAll(triples);
		count += triples.size();

		log.debug("Loaded #0 RDF list triples.", count);
		
		//could be removed to postpone the building which will happen automatically when needed anyway
		tbox.buildCollections();	
		tbox.cleanupCollections();
	}

}


