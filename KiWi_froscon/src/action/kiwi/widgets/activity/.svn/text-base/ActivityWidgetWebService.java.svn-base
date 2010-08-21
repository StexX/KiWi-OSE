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
package kiwi.widgets.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import kiwi.model.activity.Activity;

import org.jboss.resteasy.annotations.providers.jaxb.Wrapped;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.log.Log;

/**
 * Backing webservice for the stream of activities jQuery widget.
 * 
 * @author Sebastian Schaffert
 *
 */
@Name("kiwi.widgets.activityWidget")
@Scope(ScopeType.STATELESS)
@Path("/widgets/activity")
//@Transactional
public class ActivityWidgetWebService {

	@Logger
	private Log log;
	
	@In
	private EntityManager entityManager;

	@In(create=true) 
	private Map<String, String> messages;
	
	private List<ActivityJSON> activities;
	
	
	/**
	 * Return a JSON object representing a list of activities (ActivityJSON). The web service is 
	 * called using /KiWi/seam/resource/service/widgets/activity/list?uri=<CONTEXT_URI> by the
	 * activity widget.
	 * 
	 * @param uri the URI of the content item for which to list the activities
	 * @return
	 */
	@GET
	@Path("/list")
	@Produces("application/json")
	@Wrapped(element="activities")
	public List<ActivityJSON> getActivities(@QueryParam("uri") String uri,
			                                @QueryParam("num") @DefaultValue("10") int num) {
		//entityManager.setFlushMode(FlushModeType.COMMIT);
		
		if(activities == null && uri != null) {
			Query q = entityManager.createNamedQuery("activities.listActivitiesByContentItemURI");
			q.setMaxResults(num);
			q.setParameter("uri", uri);
			q.setHint("org.hibernate.cacheable", true);
			
			List<Activity> results = q.getResultList();
			
			activities = new LinkedList<ActivityJSON>();
			
			for(Activity a : results) {
				// outject current activity so that message EL replacement works
				Contexts.getEventContext().set("activity", a);
				activities.add(
						new ActivityJSON(
								formatDate(a.getCreated()),
								messages.get("widget." + a.getMessageIdentifier())
						)
				);
			}
			log.info("returned #0 activities",activities.size());
		}
		
		
		return activities;
	}
	
	private static String formatDate(Date d) {
		SimpleDateFormat df;
		if(System.currentTimeMillis() - d.getTime() < 24 * 60 * 60 * 1000) {
			// today
			df = new SimpleDateFormat("HH:mm");
			return df.format(d);
		} else if(System.currentTimeMillis() - d.getTime() < 2 * 24 * 60 * 60 * 1000) {
			// yesterday
			return "Yesterday";			
		} else {
			df = new SimpleDateFormat("dd/MM/yyyy");
			return df.format(d);
		}
	}
}
