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
 *	Szaby Gruenwald 
 * 
 */
package kiwi.widgets.rating;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import kiwi.api.content.ContentItemService;
import kiwi.api.rating.RatingService;
import kiwi.api.user.UserService;
import kiwi.model.content.ContentItem;
import kiwi.model.user.User;

import org.jboss.resteasy.annotations.providers.jaxb.Wrapped;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * 
 * @author Szaby Gruenwald
 *
 */
@Name("kiwi.widgets.ratingWidget")
@Scope(ScopeType.STATELESS)
@Path("/widgets/rating")
public class RatingWidgetWebService {

	@Logger
	private Log log;
	
	@In
	private UserService userService;

	@In
	private ContentItemService contentItemService;
	
	@In
	private RatingService ratingService;

	@GET
	@Path("/get")
	@Produces("application/json")
	@Wrapped(element="rating")
	public RatingJSON getRating(
            @QueryParam("uri") String uri,
            @QueryParam("user") String login) {

		log.info("getRating: login #0, uri #1", login, uri);
		
		User user = userService.getUserByLogin(login);
		
		ContentItem item = contentItemService.getContentItemByUri(uri);
		RatingJSON rating = new RatingJSON();
		
		log.info("getRating2: user #0, item #1", user, item);

		if(item != null && user != null) {
			log.info("test: #0", item.getTitle());
			double average = ratingService.getRatingAverage(item);
			int nrOfRatings = ratingService.getNrOfRatings(item);
			boolean userRated = ratingService.userRated(item, user);
			log.info("found data: average #0 userRated #1", average, userRated);
			rating = new RatingJSON(uri, average, nrOfRatings, userRated);
			return rating;
		}
		else 
			return new RatingJSON(uri, false);
	}
	
	@GET
	@Path("/set")
	@Produces("application/json")
	@Wrapped(element="rating")
	public RatingJSON setRating(
            @QueryParam("uri") String uri,
            @QueryParam("user") String login,
            @QueryParam("rating") int rating) {
		
		log.info("setRating: uri #0, login #1, rating #2", uri, login, rating);
		
		User user = userService.getUserByLogin(login);
		
		ContentItem item = contentItemService.getContentItemByUri(uri);
		
		if(rating == 0){
			ratingService.cancelRating(item, user);
		} else {
			ratingService.setRating(item, user, rating);
		}
		double newAverage = ratingService.getRatingAverage(item);
		int nrOfRatings = ratingService.getNrOfRatings(item);
		boolean userRated = true;//ratingService.userRated(item, user);

		RatingJSON newRating = new RatingJSON(uri, newAverage, nrOfRatings, userRated);
		return newRating;
	}
	
	

}
