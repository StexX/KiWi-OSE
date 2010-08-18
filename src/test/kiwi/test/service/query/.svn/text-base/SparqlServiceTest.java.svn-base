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
package kiwi.test.service.query;

import java.util.Map;

import junit.framework.Assert;

import org.jboss.seam.Component;
import org.jboss.seam.mock.AbstractSeamTest.FacesRequest;
import org.testng.annotations.Test;

import kiwi.api.query.KiWiQueryResult;
import kiwi.api.query.sparql.SparqlService;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.kbase.KiWiQueryLanguage;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.test.base.KiWiTest;

/**
 * SparqlServiceTest
 *
 * @author Sebastian Schaffert
 *
 */
public class SparqlServiceTest extends KiWiTest {

	@Test
	public void testSparqlQuery() throws Exception {
		clearDatabase();

    	final String MY_NS = "http://www.example.com/myns/";
    	
		/*
		 * Check querying using SPARQL queries
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	TripleStore ts   = (TripleStore) Component.getInstance("tripleStore");
    			SparqlService ss = (SparqlService)     Component.getInstance("kiwi.query.sparqlService");


            	KiWiUriResource subject  = ts.createUriResource(MY_NS + "Subject");
            	KiWiUriResource property = ts.createUriResource(MY_NS+ "predicate");
            	KiWiUriResource object1   = ts.createUriResource(MY_NS + "Object1");
            	KiWiUriResource object2   = ts.createUriResource(MY_NS + "Object2");
           	
            	ts.createTriple(subject, property, object1);
            	ts.createTriple(subject, property, object2);
            
            }
        }.run();

        
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	TripleStore ts   = (TripleStore) Component.getInstance("tripleStore");
    			SparqlService ss = (SparqlService)     Component.getInstance("kiwi.query.sparqlService");


            	KiWiUriResource subject  = ts.createUriResource(MY_NS + "Subject");
           	
            	// test queryResource / queryNode
            	for(KiWiResource r : ss.queryResource("SELECT ?S WHERE { ?S <"+MY_NS+"predicate> <"+MY_NS+"Object1> }", KiWiQueryLanguage.SPARQL)) {
            		Assert.assertEquals(subject, r);
            	}

            	for(KiWiResource r : ss.queryResource("SELECT ?S WHERE { ?S <"+MY_NS+"predicate> ?X . ?X <"+MY_NS+"predicate> <"+MY_NS+"Object2> }", KiWiQueryLanguage.SPARQL)) {
            		Assert.assertEquals(subject, r);
            	}
            	
            	// test standard query function
            	KiWiQueryResult result = ss.query("SELECT ?S WHERE { ?S <"+MY_NS+"predicate> <"+MY_NS+"Object1> }");
            	for(Map<String,Object> row : result) {
            		Assert.assertEquals(subject.getContentItem(), row.get("S"));
            	}
            }
        }.run();
        
        clearDatabase();
	}

}
