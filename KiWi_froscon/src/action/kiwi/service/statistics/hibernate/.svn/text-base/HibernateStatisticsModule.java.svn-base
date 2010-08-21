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
package kiwi.service.statistics.hibernate;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import kiwi.api.statistics.StatisticsModule;

import org.hibernate.stat.Statistics;
import org.jboss.seam.Component;
import org.jboss.seam.persistence.HibernateSessionProxy;

/**
 * HibernateStatisticsModule
 *
 * @author Sebastian Schaffert
 *
 */
public class HibernateStatisticsModule implements StatisticsModule {

	/* (non-Javadoc)
	 * @see kiwi.api.statistics.StatisticsModule#enable()
	 */
	@Override
	public void enable() {
	    EntityManager em = (EntityManager)Component.getInstance("entityManager");

		Statistics stat = ((HibernateSessionProxy)em.getDelegate()).getSessionFactory().getStatistics();
		stat.setStatisticsEnabled(true);
	}

	/* (non-Javadoc)
	 * @see kiwi.api.statistics.StatisticsModule#disable()
	 */
	@Override
	public void disable() {
	    EntityManager em = (EntityManager)Component.getInstance("entityManager");

		Statistics stat = ((HibernateSessionProxy)em.getDelegate()).getSessionFactory().getStatistics();
		stat.clear();
		stat.setStatisticsEnabled(false);
	}
	
	

	/* (non-Javadoc)
	 * @see kiwi.api.statistics.StatisticsModule#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
	    EntityManager em = (EntityManager)Component.getInstance("entityManager");

		Statistics stat = ((HibernateSessionProxy)em.getDelegate()).getSessionFactory().getStatistics();
		return stat.isStatisticsEnabled();
	}

	
	
	
	/* (non-Javadoc)
	 * @see kiwi.api.statistics.StatisticsModule#getPropertyNames()
	 */
	@Override
	public List<String> getPropertyNames() {
		List<String> result = new LinkedList<String>();
		
		result.add("flushes");
		result.add("sessions opened");
		result.add("sessions closed");


		result.add("entities loaded");
		result.add("entities fetched");
		result.add("entities deleted");
		result.add("entities inserted");
		result.add("entities updated");

		result.add("collections loaded");
		result.add("collections fetched");
		result.add("collections deleted");
		result.add("collections recreated");
		result.add("collections updated");

		result.add("query count");
		result.add("max query time");
		result.add("max query string");

		result.add("second level cache hits");
		result.add("second level cache misses");
		result.add("second level cache adds");

		result.add("query cache hits");
		result.add("query cache misses");
		result.add("query cache adds");

		// TODO Auto-generated method stub
		return result;

	}

	/* (non-Javadoc)
	 * @see kiwi.api.statistics.StatisticsModule#getStatistics()
	 */
	@Override
	public Map<String, String> getStatistics() {
		if(isEnabled()) {
			EntityManager em = (EntityManager)Component.getInstance("entityManager");

			Statistics stat = ((HibernateSessionProxy)em.getDelegate()).getSessionFactory().getStatistics();

			LinkedHashMap<String,String> result = new LinkedHashMap<String, String>();

			result.put("flushes",""+stat.getFlushCount());
			result.put("sessions opened",""+stat.getSessionOpenCount());
			result.put("sessions closed",""+stat.getSessionCloseCount());


			result.put("entities loaded",""+stat.getEntityLoadCount());
			result.put("entities fetched",""+stat.getEntityFetchCount());
			result.put("entities deleted",""+stat.getEntityDeleteCount());
			result.put("entities inserted",""+stat.getEntityInsertCount());
			result.put("entities updated",""+stat.getEntityUpdateCount());

			result.put("collections loaded",""+stat.getCollectionLoadCount());
			result.put("collections fetched",""+stat.getCollectionFetchCount());
			result.put("collections deleted",""+stat.getCollectionRemoveCount());
			result.put("collections recreated",""+stat.getCollectionRecreateCount());
			result.put("collections updated",""+stat.getCollectionUpdateCount());

			result.put("query count",""+stat.getQueryExecutionCount());
			result.put("max query time",""+stat.getQueryExecutionMaxTime());
			result.put("max query string",""+stat.getQueryExecutionMaxTimeQueryString());

			result.put("second level cache hits",""+stat.getSecondLevelCacheHitCount());
			result.put("second level cache misses",""+stat.getSecondLevelCacheMissCount());
			result.put("second level cache puts",""+stat.getSecondLevelCachePutCount());

			result.put("query cache hits",""+stat.getQueryCacheHitCount());
			result.put("query cache misses",""+stat.getQueryCacheMissCount());
			result.put("query cache puts",""+stat.getQueryCachePutCount());

			// TODO Auto-generated method stub
			return result;
		} else {
			return new HashMap<String,String>();
		}
	}

}
