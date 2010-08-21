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
package kiwi.api.reasoning;

import kiwi.service.reasoning.ProgramParser;
import kiwi.service.reasoning.ast.Program;

import org.jboss.seam.annotations.In;

/** Abstract class for loading sKWRL programs from various sources.
 * 
 * @author Jakub Kotowski
 *
 */
public abstract class ProgramLoader {
	@In ProgramParser programParser;
	
	/** Returns parsed program identified by "name".
	 * 
	 * @param name Name of the program to load.
	 * @return Returns the parsed program or null if the program couldn't be retrieved or was empty.
	 */
	public Program loadProgram(String name) {
		String content = getProgramContent(name);
		
		if (content == null || content.isEmpty())
			return null;
		
		Program program = programParser.parseProgram(content);
		
		if(program != null) {
			program.setName(name);
			
			return program;
		} else {
			return null;
		}
	}
	
	/** Loads the content of the program identified as "name" as a String.
	 * 
	 * @param name Name of the program to load.
	 * @return String representation of the program.
	 */
	protected abstract String getProgramContent(String name);
}
