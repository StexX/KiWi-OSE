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
package kiwi.service.statistics.system;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kiwi.api.statistics.StatisticsModule;

/**
 * SystemStatisticsModule
 *
 * @author Sebastian Schaffert
 *
 */
public class SystemStatisticsModule implements StatisticsModule {

	/* (non-Javadoc)
	 * @see kiwi.api.statistics.StatisticsModule#enable()
	 */
	@Override
	public void enable() {
		// do nothing, system statistics always enabled
	}

	/* (non-Javadoc)
	 * @see kiwi.api.statistics.StatisticsModule#disable()
	 */
	@Override
	public void disable() {
		// do nothing, system statistics always enabled
	}

	
	
	/* (non-Javadoc)
	 * @see kiwi.api.statistics.StatisticsModule#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}

	
	
	/* (non-Javadoc)
	 * @see kiwi.api.statistics.StatisticsModule#getPropertyNames()
	 */
	@Override
	public List<String> getPropertyNames() {
		List<String> result = new LinkedList<String>();
		
		result.add("java version");
		result.add("java vendor");
		result.add("operating system");
		
		Runtime rt = Runtime.getRuntime();
		result.add("free memory");
		result.add("total memory");
		result.add("max memory");
		
		return result;
	}

	/* (non-Javadoc)
	 * @see kiwi.api.statistics.StatisticsModule#getStatistics()
	 */
	@Override
	public Map<String, String> getStatistics() {

		
		
		LinkedHashMap<String,String> result = new LinkedHashMap<String, String>();

		result.put("java version", System.getProperty("java.version"));
		result.put("java vendor", System.getProperty("java.vendor"));
		result.put("operating system", System.getProperty("os.name")+" ("+System.getProperty("os.version")+")");
		
		Runtime rt = Runtime.getRuntime();
		result.put("free memory",""+rt.freeMemory()/(1024*1024)+"MB");
		result.put("total memory",""+rt.totalMemory()/(1024*1024)+"MB");
		result.put("max memory",""+rt.maxMemory()/(1024*1024)+"MB");
		
		return result;
	}

}
