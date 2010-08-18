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
package kiwi.service.reasoning;

import java.util.HashSet;
import java.util.Set;

/** A class to store constants commonly used in reasoning. 
 * It is likely just a temporary solution until there's a better one.
 * 
 * @author Jakub Kotowski
 *
 */
public class ReasoningConstants {
	public static final String RDF_NS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	public static final String RDFS_NS = "http://www.w3.org/2000/01/rdf-schema#";
	public static final String OWL_NS = "http://www.w3.org/2002/07/owl#";
	public static final String DEFAULT_RDF_GRAPH_CI = "http://www.kiwi-project.eu/kiwi/core/RDFContentItem/default";
	public static final String KIWI_SKWRL_PROGRAM = "http://www.kiwi-project.eu/kiwi/core/sKWRLProgram";
	public static final String KIWI_PROGRAM_HAS_RULE = "http://www.kiwi-project.eu/kiwi/core/hasRule";
	public static final String KIWI_PROGRAM_HAS_NAMESPACE = "http://www.kiwi-project.eu/kiwi/core/hasNamespace";
	public static final String RDF_TYPE = RDF_NS+"type";
	public static final String RDF_FIRST = RDF_NS+"first";
	public static final String RDF_REST = RDF_NS+"rest";
	public static final String RDF_NIL = RDF_NS+"nil";
	public static final String KIWI_INCONSISTENCY = "http://www.kiwi-project.eu/kiwi/core/inconsistency";
	public static final String KIWI_DEFAULT_SKWRL_PROGRAM = ReasoningConstants.KIWI_SKWRL_PROGRAM+"/defaultProgram";
	public static final String KIWI_RDFS_SKWRL_PROGRAM = ReasoningConstants.KIWI_SKWRL_PROGRAM+"/RDFSProgram";
	public static final String KIWI_OWL2RL_SKWRL_PROGRAM = ReasoningConstants.KIWI_SKWRL_PROGRAM+"/OWL2RLProgram";
	public static final int MAX_REASONING_TASK_STATS = 1000;
	public static final Set<String> PREDEFINED_PROGRAMS = new HashSet<String>();
	static {
		PREDEFINED_PROGRAMS.add(KIWI_RDFS_SKWRL_PROGRAM);
		PREDEFINED_PROGRAMS.add(KIWI_OWL2RL_SKWRL_PROGRAM);
	}
}
