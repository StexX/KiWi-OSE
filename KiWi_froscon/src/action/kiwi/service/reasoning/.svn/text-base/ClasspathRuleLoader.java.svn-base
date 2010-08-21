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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;

import kiwi.api.reasoning.RuleLoader;

/** Loads a file from classpath line by line.
 * 
 * 
 * @author Jakub Kotowski
 *
 */
public class ClasspathRuleLoader implements RuleLoader {
	private static String RULES_FILE = "kiwi/service/reasoning/rules.txt";
	//"($1 http://www.w3.org/1999/02/22-rdf-syntax-ns#type $2) -> ($1 http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.w3.org/1999/02/22-rdf-syntax-ns#Resource)"
	/*static String[] rules = {"($1 http://xmlns.com/foaf/0.1/firstName $2) -> (http://dummy.com/person/dummy http://xmlns.com/foaf/0.1/firstName $2)",
		"($1 http://www.w3.org/1999/02/22-rdf-syntax-ns#subClass $2), ($2 http://www.w3.org/1999/02/22-rdf-syntax-ns#subClass $3) -> ($1 http://www.w3.org/1999/02/22-rdf-syntax-ns#subClass $3)",
		"($1 http://ex.com/p $2), ($2 http://ex.com/p $1), ($1 http://ex.com/sc $3) -> ($1 http://ex.com/sc $3), (http://ex.com/u/u1 http://ex.com/p $2)",
		"($1 http://xmlns.com/foaf/0.1/firstName $2) -> ($1 http://xmlns.com/foaf/0.1/firstName \"Inferred First Name\")"
		}; */
	private ArrayList<String> rules;		
	Log log = Logging.getLog(this.getClass());
	
	public ClasspathRuleLoader() {
	}
	
	/** Loads rules from a file on classpath.
	 * 
	 * Expects the file to contain one rule per line, ignores empty lines.
	 */
	private ArrayList<String> loadRulesFromClasspath() {
		rules = new ArrayList<String>();
		
		java.net.URL url = Thread.currentThread().getContextClassLoader().getResource(RULES_FILE); //ClassLoader.getSystemResource(RULES_FILE);
		//log.info("Loading rules from #0", url);
		
		try {
			File f = new File(url.toURI());
		    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
		    String line;
		    while ((line = reader.readLine()) != null) {
		    	line = line.trim();
		    	if (!line.equals("")) 
		    		rules.add(line);
		    }
		} catch (URISyntaxException e) {
			log.error("Error while loading rules from file "+RULES_FILE+". The error was: "+e.getMessage());
			return null;
		} catch (FileNotFoundException e) {
			log.error("File "+RULES_FILE+" not found. The error was: "+e.getMessage());
			return null;			
		} catch (IOException e) {
			log.error("Error while reading from file "+RULES_FILE+". The error was: "+e.getMessage());
			return null;
		}	
		//log.info("Rules loaded.");
		
		return rules;
	}		
	
	public ArrayList<String> loadRules() {
		return loadRulesFromClasspath();
	}
}





