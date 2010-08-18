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
 * 		Szaby Gruenwald
 * 
 */
package kiwi.api.rating;

import kiwi.model.content.ContentItem;
import kiwi.model.user.User;

/**
 * The RatingService manages the rating functionality around contentItems. 
 * @author Szaby Gruenwald
 *
 */
public interface RatingService {
	
	/**
	 * Average of all ratings given by the users for a ContentItem.
	 * @param item
	 * @return
	 */
	public double getRatingAverage(ContentItem contentItem);

	/**
	 * Set the rating for ContentItem and User.
	 * @param contentItem
	 * @param user
	 * @param rating
	 */
	public void setRating(ContentItem contentItem, User user, int rating);

	/**
	 * Remove rating data for the contentItem given earlier by a user. 
	 * In case there is no rating for the user it doesn't do anything. 
	 * @param contentItem
	 * @param user
	 */
	public void cancelRating(ContentItem contentItem, User user);

	/**
	 * Says if user has given a rating for the contentItem.
	 * @param contentItem
	 * @param user
	 * @return
	 */
	public boolean userRated(ContentItem contentItem, User user);

	/**
	 * Says How many ratings are there from user for the contentItem.
	 * @param contentItem
	 * @return
	 */
	public int getNrOfRatings(ContentItem contentItem);

}
