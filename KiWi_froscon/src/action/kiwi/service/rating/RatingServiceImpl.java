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
package kiwi.service.rating;

import java.util.LinkedList;

import javax.ejb.Stateless;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.event.KiWiEvents;
import kiwi.api.rating.RatingDataFacade;
import kiwi.api.rating.RatingFacade;
import kiwi.api.rating.RatingServiceLocal;
import kiwi.api.rating.RatingServiceRemote;
import kiwi.model.content.ContentItem;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.jboss.seam.log.Log;

/**
 * 
 * @author Szaby Gruenwald
 *
 */
@Stateless
@Scope(ScopeType.STATELESS)
@AutoCreate
@Name("ratingService")
public class RatingServiceImpl implements RatingServiceRemote, RatingServiceLocal {

	@In
	private KiWiEntityManager kiwiEntityManager;

	@In(create = true)
	private ContentItemService contentItemService;

	@Logger
	private Log log;
	
	@Override
	public void setRating(ContentItem item, User user, int rating) {
		
		final RatingFacade ratingF = kiwiEntityManager.createFacade(
				item, RatingFacade.class);
		
		final RatingDataFacade ratingData;
		final ContentItem ratingDataCI;
		
		final double ratingAverage = this.getRatingAverage(item);
		final int nrOfRatings = this.getNrOfRatings(item);
		boolean newRating = true;
		int oldRating = 0;

		if(!userRated(item, user)){
			ratingDataCI = contentItemService.createContentItem();
			ratingData = kiwiEntityManager.createFacade(
					ratingDataCI , RatingDataFacade.class);
			
			ratingData.setRatingFacade(ratingF);
			ratingData.setAuthor(user);
		}else {
			ratingData = getRatingData(item, user);
			ratingDataCI = ratingData.getDelegate();
			oldRating = ratingData.getRating();
			newRating = false;
		}
		
		ratingData.setRating(rating);
		
		LinkedList<RatingDataFacade> ratingDataFacades;
		if(ratingF.getRatingDataFacades().size()== 0){
			ratingDataFacades = 
				new LinkedList<RatingDataFacade>();
			ratingDataFacades.add(ratingData);
		} else {
			ratingDataFacades = 
				ratingF.getRatingDataFacades();
			ratingDataFacades.add(ratingData);
		}
		ratingF.setRatingDataFacades(ratingDataFacades);
		
		// storing the new average rating as a property triple hanging directly on the contentItem.
		int newNrOfRatings = nrOfRatings + (newRating ? 1:0);
		final double oldsum = ratingAverage * nrOfRatings;
		final double newsum = (oldsum + rating) - oldRating;
		final double newRatingAverage = newsum / newNrOfRatings;
		log.info("setRating: ratingAverage #0", newRatingAverage);
		item.setRating(newRatingAverage);
		
		log.info("setRating: item is #0", item);
		
		kiwiEntityManager.persist(item);
		// kiwiEntityManager.persist(ratingDataCI);
		
		kiwiEntityManager.persist(ratingData);
		log.info("setRating: ratingF is #0", ratingF);
		kiwiEntityManager.persist(ratingF);

		Events.instance().raiseEvent(KiWiEvents.ACTIVITY_RATECONTENTITEM, 
				user, item, rating);
//		kiwiEntityManager.persist(item);

		log.debug("setRating: item: #0, user: #1, rating: #2", 
			item.getKiwiIdentifier(), user.getLogin(), rating);
	}
	
	private RatingDataFacade getRatingData(ContentItem item, User user) {
		final RatingFacade ratingF = kiwiEntityManager.createFacade(
				item, RatingFacade.class);
		for(RatingDataFacade ratingData:ratingF.getRatingDataFacades()){
			if(user.equals(ratingData.getAuthor())){
				return ratingData;
			}
		}
		log.info("getRatingData: user #0 has not rated", user.getLogin());
		return null;
	}

	@Override
	public double getRatingAverage(ContentItem item) {
		
		final RatingFacade ratingF = kiwiEntityManager.createFacade(
				item, RatingFacade.class);
		double res=0;
		for(RatingDataFacade ratingData:ratingF.getRatingDataFacades()){
			res += ratingData.getRating();
			
			try {
				log.info("getRatingAverage: rating user #0 value #1",
						ratingData.getAuthor().getLogin(), ratingData.getRating());
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
		if (ratingF.getRatingDataFacades().size() > 0)
			res = res / ratingF.getRatingDataFacades().size();
		else
			res = 0;
		log.info("getRatingAverage: item: #0 value #1", item.getKiwiIdentifier(), res);
		return res;
	}

	@Override
	public boolean userRated(ContentItem item, User user) {
		boolean res=false;
		res = getRatingData(item, user)==null ? false : true;
		log.debug("userRated?: item: #0, user: #1 -> #2", 
				item.getKiwiIdentifier(), user.getLogin(), res);
		return res;
	}

	@Override
	public int getNrOfRatings(ContentItem item) {
		final RatingFacade ratingF = kiwiEntityManager.createFacade(
				item, RatingFacade.class);
		LinkedList<RatingDataFacade> ratingDataFacades = ratingF.getRatingDataFacades();
		for(RatingDataFacade data:ratingDataFacades)
			log.info("getNrOfRatings: ratingData found for #0 with rating #1", data.getAuthor(),data.getRating());
		return ratingDataFacades.size();
	}
	
	@Override
	public void cancelRating(ContentItem item, User user) {
		final RatingFacade ratingF = kiwiEntityManager.createFacade(
				item, RatingFacade.class);
		
		final RatingDataFacade ratingData = getRatingData(item, user);
		final ContentItem ratingDataCI;
		LinkedList<RatingDataFacade> ratingDataFacades;
		
		
		if(ratingData == null){
			return;
		} else {
			// storing the new average rating as a property triple hanging directly on the contentItem.
			final double ratingAverage = getRatingAverage(item);
			final int nrOfRatings = getNrOfRatings(item);
			int rating = ratingData.getRating();
			if(nrOfRatings == 1)item.setRating(null);
			else {
				final double oldsum = ratingAverage * nrOfRatings;
				final int newNrOfRatings = nrOfRatings -1;
				final double newsum = oldsum - rating;
				final double newRatingAverage = newsum / newNrOfRatings;
				item.setRating(newRatingAverage);
			}

			// remove rating at the facade layer
			ratingDataCI = ratingData.getDelegate();
			contentItemService.removeContentItem(ratingDataCI);
			log.info("cancelRating: #0 cancelled rating for #1", 
					item.getKiwiIdentifier(), user.getLogin());
			ratingDataFacades = 
				ratingF.getRatingDataFacades();
			ratingDataFacades.remove(ratingData);
			ratingF.setRatingDataFacades(ratingDataFacades);
			
			kiwiEntityManager.persist(item);
			Events.instance().raiseEvent(KiWiEvents.ACTIVITY_UNRATECONTENTITEM, 
					user, item);

		}
	}

}
