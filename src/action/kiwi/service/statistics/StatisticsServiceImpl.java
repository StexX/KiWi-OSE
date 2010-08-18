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
package kiwi.service.statistics;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import kiwi.api.statistics.StatisticsModule;
import kiwi.api.statistics.StatisticsServiceLocal;
import kiwi.api.statistics.StatisticsServiceRemote;
import kiwi.service.statistics.hibernate.HibernateStatisticsModule;
import kiwi.service.statistics.system.SystemStatisticsModule;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * StatisticsServiceImpl
 *
 * @author Sebastian Schaffert
 *
 */
@Name("statisticsService")
@Scope(ScopeType.APPLICATION)
@AutoCreate
public class StatisticsServiceImpl implements StatisticsServiceLocal, StatisticsServiceRemote {
	
	private LinkedHashMap<String,StatisticsModule> modules;
	
	private boolean enabled = false;
	
	@Logger
	private Log log;
	
	

	/* (non-Javadoc)
	 * @see kiwi.api.statistics.StatisticsService#initialise()
	 */
	@Create
	public void initialise() {
		modules = new LinkedHashMap<String,StatisticsModule>();

		log.info("StatisticsService starting up ...");
		
		registerModule("System", new SystemStatisticsModule());
		registerModule("Hibernate Persistence", new HibernateStatisticsModule());
		
		disableAll();
	}

	/* (non-Javadoc)
	 * @see kiwi.api.statistics.StatisticsService#enableAll()
	 */
	@Override
	public void enableAll() {
		for(StatisticsModule mod : modules.values()) {
			mod.enable();
		}

		enabled = true;
	}

	/* (non-Javadoc)
	 * @see kiwi.api.statistics.StatisticsService#disableAll()
	 */
	@Override
	public void disableAll() {
		for(StatisticsModule mod : modules.values()) {
			mod.disable();
		}
		
		enabled = false;
	}
	
	

	/* (non-Javadoc)
	 * @see kiwi.api.statistics.StatisticsService#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return enabled;
	}

	/* (non-Javadoc)
	 * @see kiwi.api.statistics.StatisticsService#enableModule(java.lang.String)
	 */
	@Override
	public void enableModule(String modName) {
		if(modules.get(modName) != null) {
			modules.get(modName).enable();
		}
	}

	/* (non-Javadoc)
	 * @see kiwi.api.statistics.StatisticsService#disableModule(java.lang.String)
	 */
	@Override
	public void disableModule(String modName) {
		if(modules.get(modName) != null) {
			modules.get(modName).disable();
		}
	}

	/* (non-Javadoc)
	 * @see kiwi.api.statistics.StatisticsService#registerModule(java.lang.String, kiwi.api.statistics.StatisticsModule)
	 */
	@Override
	public void registerModule(String modName, StatisticsModule mod) {
		log.info("registering statistics module \"#0\"", modName);
		modules.put(modName,mod);
	}

	/* (non-Javadoc)
	 * @see kiwi.api.statistics.StatisticsService#getModule(java.lang.String)
	 */
	@Override
	public StatisticsModule getModule(String modName) {
		return modules.get(modName);
	}

	/* (non-Javadoc)
	 * @see kiwi.api.statistics.StatisticsService#listModules()
	 */
	@Override
	public List<String> listModules() {
		return new LinkedList<String>(modules.keySet());
	}

}
