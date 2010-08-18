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
package kiwi.api.statistics;

import java.util.List;

/**
 * StatisticsService - gathers statistics about the system runtime behaviour, e.g.
 * database/hibernate, caching, etc.
 * 
 * 
 *
 * @author Sebastian Schaffert
 *
 */
public interface StatisticsService {

	/**
	 * Initialise the service.
	 */
	public void initialise();
	
	/**
	 * Turn on collection of statistical information for all modules. May introduce additional overhead.
	 */
	public void enableAll();
	
	/**
	 * Turn off collection of statistical information for all modules. 
	 */
	public void disableAll();
	
	/**
	 * Return true if statistics gathering is enabled.
	 * @return
	 */
	public boolean isEnabled();
	
	/**
	 * Enable collection of statistical information for the specified module;
	 * @param mod
	 */
	public void enableModule(String modName);
	
	
	/**
	 * Disable collection of statistical information for the specified module;
	 * @param mod
	 */
	public void disableModule(String modName);
	
	/**
	 * Register the statistics module given as argument with the statistics service
	 * @param mod
	 */
	public void registerModule(String modName, StatisticsModule mod);
	
	/**
	 * Return the statistics module identified by the name passed as parameter.
	 * @param modName
	 * @return
	 */
	public StatisticsModule getModule(String modName);
	
	/**
	 * Return all statistics modules associated with the statistics service.
	 * @return
	 */
	public List<String> listModules();
	
}
