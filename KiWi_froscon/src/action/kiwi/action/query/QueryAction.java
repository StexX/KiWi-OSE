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
package kiwi.action.query;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import kiwi.api.config.ConfigurationService;
import kiwi.api.query.KiWiQueryResult;
import kiwi.api.query.QueryService;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.content.ContentItemI;
import kiwi.model.kbase.KiWiEntity;
import kiwi.model.kbase.KiWiResource;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * Backing bean for embedded queries.
 * TODO Give examples for query
 * @author Sebastian Schaffert
 *
 */
@Name("queryAction")
@Scope(ScopeType.PAGE)
public class QueryAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger
	private Log log;
	
	@In
	private QueryService queryService;
	
	@In
	private ConfigurationService configurationService;
	
	@In
	private TripleStore tripleStore;
	
	private HashMap<String,KiWiQueryResult> cache;
	
	/**
	 * 
	 * @param query
	 * @param language
	 * @return The UI requires a non-null value
	 */
	public KiWiQueryResult query(String query, String language) {
		log.info("query: '#0' lang: '#1'", query, language);
		if(cache == null) {
			cache = new HashMap<String, KiWiQueryResult>();
		}
		if(cache.get(query) != null) {
			return cache.get(query);
		} else {
			KiWiQueryResult r = queryService.query(query, language);
			 
			if (r==null)
				r=new KiWiQueryResult();
			cache.put(query,r);
			return r;
		}
	}
	
	public String formatHeader(String prop) {
		if (prop == null) {
			return "unknown";
		} else if ("".equals(prop)) {
			return "Title";
		} else if (! prop.contains(":")) {
			return prop;
		} else {
			KiWiResource r = tripleStore.createUriResourceBySPARQLId(prop);
			
			if(r != null) {
				return r.getLabel();
			} else {
				return prop;
			}
		}
	}
	
	public String formatCell(Object o) {
		if(o instanceof ContentItemI) {
			ContentItemI item = (ContentItemI)o;
			return 
				"<a href=\""+configurationService.getBaseUri()+"/home.seam?kiwiid="+item.getResource().getKiwiIdentifier()+"\">"+
				item.getTitle() +
				"</a>";
		} else if(o instanceof KiWiEntity) {
			KiWiEntity entity = (KiWiEntity)o;
			return 
				"<a href=\""+configurationService.getBaseUri()+"/home.seam?kiwiid="+entity.getResource().getKiwiIdentifier()+"\">"+
				entity.getResource().getLabel() +
				"</a>";
		} else if(o instanceof KiWiResource) {
			KiWiResource res = (KiWiResource)o;
			return 
				"<a href=\""+configurationService.getBaseUri()+"/home.seam?kiwiid="+res.getKiwiIdentifier()+"\">"+
				res.getLabel() +
				"</a>";
		} else if(o == null) {
			return "";
		} else {
			return o.toString();
		}
	}
	
	public String textCell(Object o) {
		if(o instanceof ContentItemI) {
			ContentItemI item = (ContentItemI)o;
			return 
				item.getTitle();
		} else if(o instanceof KiWiEntity) {
			KiWiEntity entity = (KiWiEntity)o;
			return 
				entity.getResource().getLabel();
		} else if(o instanceof KiWiResource) {
			KiWiResource res = (KiWiResource)o;
			return 
				res.getLabel();
		} else if(o == null) {
			return "";
		} else {
			return o.toString();
		}
		
	}
	
	public double getAvgLat(KiWiQueryResult r) {
		double min = 90.0;
		double max = -90.0;
		
		for(Map<String,Object> m : r) {
			if(m.get("geo:lat")!=null){
				Double lat = Double.parseDouble(m.get("geo:lat").toString());
				if(lat < min) {
					min = lat;
				}
				if(lat > max) {
					max = lat;
				}
			}
		}
		log.info("max lat #0, min lat #1", max,min);
		return (min + max) / 2;
	}

	public double getAvgLong(KiWiQueryResult r) {
		double min = 180.0;
		double max = -180.0;
		
		for(Map<String,Object> m : r) {
			if(m.get("geo:long")!=null){
				Double lng = Double.parseDouble(m.get("geo:long").toString());
				if(lng < min) {
					min = lng;
				}
				if(lng > max) {
					max = lng;
				}
			}
		}
		log.info("max lng #0, min lng #1", max,min);
		return (min + max) / 2;
	}
}
