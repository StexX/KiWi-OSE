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
package kiwi.widgets.recommendations;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import kiwi.api.content.ContentItemService;
import kiwi.api.recommendation.RecommendationService;
import kiwi.api.user.UserService;
import kiwi.model.content.ContentItem;
import kiwi.model.user.User;

import org.jboss.resteasy.annotations.providers.jaxb.Wrapped;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.cache.CacheProvider;
import org.jboss.seam.log.Log;

/**
 * @author Sebastian Schaffert
 *
 */
@Name("kiwi.widgets.recommendationsWidget")
@Scope(ScopeType.STATELESS)
@Path("/widgets/recommendations")
//@Transactional
public class RecommendationWidgetWebService {

	@Logger
	private Log log;
	
	@In
	private RecommendationService recommendationService;
	
	@In
	private ContentItemService contentItemService;
	
	@In
	private UserService userService;
	
	@In
	private CacheProvider cacheProvider;
	
	/**
	 * Return a JSON object representing a list of recommendations (RecommendationJSON). The list of
	 * recommendations is calculated using the simple recommendation algorithm based on current item and
	 * current user.
	 * The web service is called using 
	 * /KiWi/seam/resource/service/widgets/recommendations/simple?uri=<CONTEXT_URI> 
	 * by the recommendations widget.
	 * 
	 * @param uri the URI of the content item for which to list the activities
	 * @param login the login of the user for whom to calculate recommendations
	 * @param num the number of results to return
	 * @return
	 */
	@GET
	@Path("/simple")
	@Produces("application/json")
	@Wrapped(element="recommendations")
	public List<RecommendationJSON> getSimpleRecommendations(
			                                     @QueryParam("uri") String uri,
											     @QueryParam("user") String login,
			                                     @QueryParam("num") @DefaultValue("10") int num) {
		List<RecommendationJSON> recommendations = new LinkedList<RecommendationJSON>();
		
		ContentItem item = contentItemService.getContentItemByUri(uri);
		User user = userService.getUserByLogin(login);
		
		if(item != null && user != null) {
			for(ContentItem ci: recommendationService.getRecommendations(item, user)) {
				recommendations.add(new RecommendationJSON(ci));
			}
		}
		
		
		return recommendations;
	}

	/**
	 * Return a JSON object representing a list of recommendations (RecommendationJSON). The multifactor
	 * recommendation algorithm is used to calculate the recommendations based on item and user. 
	 * The web service is called using 
	 * /KiWi/seam/resource/service/widgets/recommendations/multifactor?uri=<CONTEXT_URI> 
	 * by the recommendations widget.
	 * 
	 * @param uri the URI of the content item for which to list the activities
	 * @param login the login of the user for whom to calculate recommendations
	 * @param num the number of results to return
	 * @return
	 */
	@GET
	@Path("/multifactor")
	@Produces("application/json")
	@Wrapped(element="recommendations")
	public List<RecommendationJSON> getMultifactorRecommendations(
			                                     @QueryParam("uri") String uri,
											     @QueryParam("user") String login,
			                                     @QueryParam("num") @DefaultValue("10") int num) {
		List<RecommendationJSON> recommendations = new LinkedList<RecommendationJSON>();
		
		ContentItem item = contentItemService.getContentItemByUri(uri);
		User user = userService.getUserByLogin(login);
		
		if(item != null && user != null) {
			for(ContentItem ci: recommendationService.getMultiFactorRecommendations(item, user)) {
				recommendations.add(new RecommendationJSON(ci));
			}
		}
		
		return recommendations;
	}

	/**
	 * Return a JSON object representing a list of recommendations (RecommendationJSON). This 
	 * method returns a list of recommendations for a user, independent of the currently displayed
	 * content item.
	 * The web service is called using 
	 * /KiWi/seam/resource/service/widgets/recommendations/personal?uri=<CONTEXT_URI> 
	 * by the recommendations widget.
	 * 
	 * @param uri the URI of the content item for which to list the activities
	 * @param user the login of the user for whom to calculate recommendations
	 * @param num the number of results to return
	 * @return
	 */
	@GET
	@Path("/personal")
	@Produces("application/json")
	@Wrapped(element="recommendations")
	public List<RecommendationJSON> getPersonalRecommendations(
											     @QueryParam("user") String login,
			                                     @QueryParam("num") @DefaultValue("10") int num) {
		List<RecommendationJSON> recommendations = new LinkedList<RecommendationJSON>();

		User user = userService.getUserByLogin(login);
		
		if(user != null) {
			for(ContentItem ci: recommendationService.getPersonalRecommendations(user)) {
				recommendations.add(new RecommendationJSON(ci));
			}
		}

		return recommendations;
	}

	/**
	 * Return a JSON object representing a list of recommendations (RecommendationJSON). This 
	 * method returns a list of recommendations for the currently displayed content item using
	 * the semantic vectors analysis of the content.
	 * 
	 * The web service is called using 
	 * /KiWi/seam/resource/service/widgets/recommendations/semantic?uri=<CONTEXT_URI> 
	 * by the recommendations widget.
	 * 
	 * @param uri the URI of the content item for which to list the activities
	 * @param num the number of results to return
	 * @return
	 */
	@GET
	@Path("/vectors")
	@Produces("application/json")
	@Wrapped(element="recommendations")
	public List<RecommendationJSON> getSemanticRecommendations(
            									 @QueryParam("uri") String uri,
			                                     @QueryParam("num") @DefaultValue("10") int num) {
		List<RecommendationJSON> recommendations = 
			(List<RecommendationJSON>)cacheProvider.get("recommendations.semvector", uri);
			
		if(recommendations == null) {
			recommendations = new LinkedList<RecommendationJSON>();

			ContentItem item = contentItemService.getContentItemByUri(uri);
			
			try {
				if(item != null) {
					for(ContentItem ci: recommendationService.getSemanticVectorRecommendations(item)) {
						recommendations.add(new RecommendationJSON(ci));
					}
					cacheProvider.put("recommendations.semvector", uri, recommendations);
					
					log.info("recommendation widget: #0 recommedations",recommendations.size());
				}
			} catch(Throwable t) {
				t.printStackTrace();
			}
		}
		return recommendations;
	}

}
