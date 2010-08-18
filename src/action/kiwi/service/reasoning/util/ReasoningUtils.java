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
package kiwi.service.reasoning.util;

import java.util.HashSet;
import java.util.Set;

import org.jboss.seam.log.Log;

import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;

public class ReasoningUtils {
	public static void printSetToLog(Set<KiWiTriple> set, Log log) {
		for (KiWiTriple triple : set)
			if (triple.isInferred()) {
				/*if (triple.isBase())
					log.info("\t INF BAS #0", triple.toString());
				else*/
					log.info("\t INF #0", triple.toString());					
			} else
				log.info("\t BAS    #0", triple.toString());
	}
	
	/**
	 * @return
	 */
	public static Set<KiWiTriple> getTriples(Set<KiWiTriple> set, boolean inferred) {
		Set<KiWiTriple> newSet = new HashSet<KiWiTriple>();
		for (KiWiTriple triple : set) {
			if (inferred ^ triple.isInferred())
				continue;
			
			newSet.add(triple);
		}
		
		return newSet;
	}	
	
	public static Set<KiWiTriple> getInferredTriples(Set<KiWiTriple> set) {
		return getTriples(set, true);
	}

	public static Set<KiWiTriple> getBaseTriples(Set<KiWiTriple> set) {
		return getTriples(set, false);
	}
	
	private static boolean hasPropertyNs(KiWiTriple triple, Set<String> properties) {
		for (String prop : properties)
			if (triple.getProperty().getUri().startsWith(prop))
				return true;
		
		return false;
	}
	
	public static Set<KiWiTriple> getTriplesWithPropertyNS(Set<KiWiTriple> set, Set<String> properties) {
		Set<KiWiTriple> newSet = new HashSet<KiWiTriple>();
		
		for (KiWiTriple triple : set)
			if (hasPropertyNs(triple, properties))
				newSet.add(triple);
		
		return newSet;
	}
	
	public static Set<KiWiTriple> getTriplesWithPropertyNS(Set<KiWiTriple> set, String propertyNS) {
		Set<KiWiTriple> newSet = new HashSet<KiWiTriple>();
		
		for (KiWiTriple triple : set) {
			KiWiUriResource property = triple.getProperty();
			
			if (property.getUri().startsWith(propertyNS))
				newSet.add(triple);
		}
		
		return newSet;
	}
}




