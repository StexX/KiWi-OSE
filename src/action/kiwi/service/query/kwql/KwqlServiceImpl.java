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
package kiwi.service.query.kwql;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import kiwi.api.query.KiWiQueryResult;
import kiwi.api.query.kwql.KwqlQueryService;
import kiwi.api.query.kwql.KwqlServiceLocal;
import kiwi.api.query.kwql.KwqlServiceRemote;
import kiwi.api.search.KiWiSearchResults;
import kiwi.service.query.kwql.KWQLComponent;
import kiwi.service.query.kwql.KWQLConstraintViolationException;
import kiwi.service.query.kwql.KWQLQuery;

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.Tree;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

/**
 * Implementation of the KWQL evaluation engine.
 * 
 * @author Klara Weiand
 * 
 */
@Name("kiwi.query.kwqlService")
@Scope(ScopeType.STATELESS)
@AutoCreate
public class KwqlServiceImpl implements KwqlServiceLocal, KwqlServiceRemote {

	@Logger
	private Log log;

	@In
	private KwqlQueryService kwqlQueryService;

	@In
	private FacesMessages facesMessages;

	@Override
	public KiWiSearchResults search(String queryString) {
		KiWiSearchResults result = new KiWiSearchResults();
		KWQLQuery kwqlQuery;
		try {
			kwqlQuery = preprocessQuery(queryString);
			ArrayList<KWQLComponent> bodies = kwqlQuery.getBodies();
			Tree head = kwqlQuery.getHead();
			//if (kwqlQuery.isGlobal()) {
				Iterator<KWQLComponent> i = bodies.iterator();
				while (i.hasNext()) {
					result = kwqlQueryService.search(i.next());
				}
			//} else {
			//	Iterator<KWQLComponent> i = bodies.iterator();
			//	result = kwqlQueryService.search(i.next());
			//}
		} catch (KWQLConstraintViolationException e) {
			// TODO Auto-generated catch block
			log.error("KWQL query could not be evaluated:");
			e.printStackTrace();

			facesMessages.add("KWQL query could not be evaluated: "+ e.getMessage());
		} catch (Throwable e) {
			log.error("KWQL query could not be evaluated:");
			e.printStackTrace();

			facesMessages.add("KWQL query could not be evaluated.");
		}

		return result;
	}

	/**
	 * @param queryString
	 * @return
	 * @throws KWQLConstraintViolationException 
	 * @throws RecognitionException 
	 * @throws KWQLConstraintViolationException
	 * @throws RecognitionException
	 * @throws RecognitionException
	 * @throws KWQLConstraintViolationException
	 */
	private KWQLQuery preprocessQuery(String queryString) throws RecognitionException, KWQLConstraintViolationException {
		KWQLQuery kwqlQuery = null;
			kwqlQuery = new KWQLQuery(queryString);

		return kwqlQuery;
	}

	@Override
	public KiWiQueryResult query(String queryString) {
		KiWiQueryResult result = new KiWiQueryResult();
		KWQLQuery kwqlQuery;
		try {
			kwqlQuery = preprocessQuery(queryString);
			Tree head = kwqlQuery.getHead();
			//if (kwqlQuery.isGlobal()) {
			ArrayList<KWQLComponent> bodies = kwqlQuery.getBodies();
				Iterator<KWQLComponent> i = bodies.iterator();
				while (i.hasNext()) {
					result = kwqlQueryService.query(i.next());
				}
			//} else {
			//	Iterator<Tree> i = bodies.iterator();
			//	KWQLBody kwqlBody = new KWQLBody(i.next());
			//	result = kwqlQueryService.query(kwqlBody);
		} catch (KWQLConstraintViolationException e) {
			// TODO Auto-generated catch block
			log.error("KWQL query could not be evaluated:");
			e.printStackTrace();

			facesMessages.add("KWQL query could not be evaluated: "+ e.getMessage());
		} catch (Throwable e) {
			log.error("KWQL query could not be evaluated:");
			e.printStackTrace();

			facesMessages.add("KWQL query could not be evaluated.");
		}
		return result;
	}

}