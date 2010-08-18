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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

import kiwi.api.reasoning.ProgramLoader;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;

/** Loads a file from classpath.
 * 
 * 
 * @author Jakub Kotowski
 *
 */
@Scope(ScopeType.STATELESS)
@AutoCreate
@Name("classpathProgramLoader")
public class ClasspathProgramLoader extends ProgramLoader {
	private static final String PROGRAM_FILE = "kiwi/service/reasoning/owl2-rl.skwrl";
	private static final HashMap<String, String> nameToClasspathMap = new HashMap<String, String>();
	static {
//		nameToClasspathMap.put(ReasoningConstants.KIWI_DEFAULT_SKWRL_PROGRAM, "kiwi/service/reasoning/owl2-rl.skwrl"); // --> now hardcoded in the triplestore program loader
		nameToClasspathMap.put(ReasoningConstants.KIWI_RDFS_SKWRL_PROGRAM, "kiwi/service/reasoning/rdfs.skwrl");
		nameToClasspathMap.put(ReasoningConstants.KIWI_OWL2RL_SKWRL_PROGRAM, "kiwi/service/reasoning/owl2-rl.skwrl");
	}
	Log log = Logging.getLog(this.getClass());
	
	public ClasspathProgramLoader() {}
	
	@Override
	public String getProgramContent(String name) {
		if (!nameToClasspathMap.containsKey(name))
			throw new IllegalArgumentException("ClasspathProgramLoader doesn't know the location of program "+name+". See ReasoningConstants for valid program names.");
		
		java.net.URL url = Thread.currentThread().getContextClassLoader().getResource(nameToClasspathMap.get(name)); //ClassLoader.getSystemResource(RULES_FILE);
		log.info("Loading program #0", url);
		
		try {
			File f = new File(url.toURI());
		    byte[] buffer = new byte[(int) f.length()];
		    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
		    bis.read(buffer);
		    return new String(buffer);
		} catch (URISyntaxException e) {
			log.error("Error while loading rule program from file "+PROGRAM_FILE+". The error was: "+e.getMessage());
			return null;
		} catch (FileNotFoundException e) {
			log.error("File "+PROGRAM_FILE+" not found. The error was: "+e.getMessage());
			return null;			
		} catch (IOException e) {
			log.error("Error while reading from file "+PROGRAM_FILE+". The error was: "+e.getMessage());
			return null;
		}		
	}

}




