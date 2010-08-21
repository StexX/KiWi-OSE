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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import kiwi.service.reasoning.ast.Program;
import kiwi.service.reasoning.ast.Rule;
import kiwi.service.reasoning.parser.ParseException;
import kiwi.service.reasoning.parser.SimpleKWRLProgramParser;
import kiwi.service.reasoning.parser.TokenMgrError;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * 
 * @author Jakub Kotowski
 *
 */
@Scope(ScopeType.APPLICATION)
@AutoCreate
@Name("programParser")
public class ProgramParser {	
	@Logger
	private Log log;
	
	private SimpleKWRLProgramParser parser;
	private InputStream is;		
	
	@Create
	public void inititalize() {
		/*loadRulesFromClassPath();

		if (rules.size() == 0) {
			log.info("No rules to process.");
			return;
		}*/
			
		//log.info("Initializing parser");
		try {
			is = new ByteArrayInputStream("".getBytes("UTF-8"));
			parser = new SimpleKWRLProgramParser(is);
		} catch(UnsupportedEncodingException ex) {
			log.error("Could not initialize parser. Error was: "+ex.getMessage());
			return;
		} 				
	}
	
	@SuppressWarnings("static-access")
	public Program parseProgram(String program) {
		log.debug("Parsing program");
		Program parsedProgram = null;
		
		try {
			is = new ByteArrayInputStream(program.getBytes("UTF-8"));
			parser.ReInit(is);
			
			parsedProgram = parser.Program();

		} catch(UnsupportedEncodingException ex) {
			log.error("Could not read program, error was: #0", ex.getMessage());
		} catch(ParseException ex) {
			log.error("Could not parse program, error was: #0", ex.getMessage());
		} catch(TokenMgrError ex) {
			//Stupid, stupid, stupid javacc...
			log.error("Could not parse progmra, error was: #0", ex.getMessage());
		}
				
		return parsedProgram;
	}	
	
	@SuppressWarnings("static-access")
	public ArrayList<Rule> parseRules(List<String> rules) {
		ArrayList<Rule> parsedRules = new ArrayList<Rule>(rules.size());
		
		//log.info("Parsing rules.");
		for (int i=0; i < rules.size(); i++) {
			String ruleStr = rules.get(i);
			//log.info("Parsing rule #0", ruleStr);
			
			try {
				is = new ByteArrayInputStream(ruleStr.getBytes("UTF-8"));
				parser.ReInit(is);
				
				Rule rule = parser.Rule();
				rule.setName(ruleStr);
				parsedRules.add(rule);
				
			} catch(UnsupportedEncodingException ex) {
				log.error("Could not read rule '#0', error was: #1", ruleStr, ex.getMessage());
				continue;
			} catch(ParseException ex) {
				log.error("Could not parse query '#0', error was: #1", ruleStr, ex.getMessage());
				continue;
			} catch(TokenMgrError ex) {
				//Stupid, stupid, stupid javacc...
				log.error("Could not parse query '#0', error was: #1", ruleStr, ex.getMessage());
				continue;				
			}

		}
		
		//log.info("Rules parsed.");
		
		return parsedRules;
	}
		
}
