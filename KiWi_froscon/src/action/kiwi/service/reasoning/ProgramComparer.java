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

import kiwi.service.reasoning.ast.Program;
import kiwi.service.reasoning.ast.Rule;

/** Compares two parsed programs and returns a ProgramUpdate consisting of added and removed rules.
 * 
 * As a side effect expands their namespaces.
 * 
 * @author Jakub Kotowski
 *
 */
public class ProgramComparer {

	/** Compares two parsed programs and returns a ProgramUpdate consisting of added and removed rules.
	 * 
	 * If a rule changed and its name remained unchanged then it will still be counted as a removal and adding.
	 * 
	 * As a side effect expands the programs' namespaces.
	 *
	 */
	public static ProgramUpdateTask compare(Program oldProgram, Program newProgram) {
		ProgramUpdateTask update = new ProgramUpdateTask();
		update.setProgramName(oldProgram.getName());
		
		if (!oldProgram.getName().equals(newProgram.getName()))
			throw new IllegalArgumentException("Can't compare different programs: "+oldProgram.getName()+" and "+newProgram.getName());
		
		ProgramCompiler.expandNamespaces(oldProgram);
		ProgramCompiler.expandNamespaces(newProgram);
		
		for (Rule rule : oldProgram.getRules()) {
			if (newProgram.contains(rule))
				continue;
			
			update.addRemovedRule(rule);
		}
		
		for (Rule rule : newProgram.getRules()) {
			if (oldProgram.contains(rule))
				continue;
			
			update.addAddedRule(rule);
		}		
		
		return update;
	}
}


