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
package kiwi.widgets.imagebrowser;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import kiwi.action.search.KiWiSearchEngine;
import kiwi.api.search.KiWiSearchResults;
import kiwi.api.search.KiWiSearchResults.SearchResult;
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

@Name("kiwi.widgets.imagebrowserWidget")
@Scope(ScopeType.STATELESS)
@Path("/widgets/imagebrowser")
public class ImagebrowserWidgetWebService {
	@Logger
	private Log log;
	
	@In
	private UserService userService;

	@GET
	@Path("/getStatus")
	@Produces("application/json")
	@Wrapped(element="status")
	public ImageBrowserResponseJSON getStatus(
            @QueryParam("user") String login) {
		User user = userService.getUserByLogin(login);
		
		// The user exists
		if (user!=null){
			log.debug("getStatus: user #0 has loaded the imagebrowser widget", login);
			return new ImageBrowserResponseJSON(0, "ok");
		} else{
			return new ImageBrowserResponseJSON(1, "User doesn't exist");
		}
	}
	
	@GET
	@Path("/query")
	@Produces("application/json")
	@Wrapped(element="status")
	public ImageBrowserResponseJSON runQuery(
            @QueryParam("user") String login,
            @QueryParam("query") @DefaultValue("") String query,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("pageSize") @DefaultValue("12") int pageSize) {
		User user = userService.getUserByLogin(login);

		log.info("query: login #0 query #1 page #2 pageSize #3", login, query, page, pageSize);

		if (user!=null){
			ImageBrowserResponseJSON res = new ImageBrowserResponseJSON(true);
			KiWiSearchEngine searchEngine = new KiWiSearchEngine();
			
			log.info("query: searchEngine created, setting query");

			searchEngine.setSearchQuery(query);
			log.info("query: setting page..");

			searchEngine.setPage(page);
			log.info("query: setting pagesize..");

			searchEngine.setPageSize(pageSize);
			// searchEngine.runSearch();
			
			log.info("getting the results..");
			
			// KiWiSearchResults results = searchEngine.getSearchResults(query, page, pageSize);
			KiWiSearchResults results = searchEngine.getSearchResults();
			log.info("query: results: #0", results.getResultCount());
			
			for (SearchResult sres:results.getResults()){
				ContentItem item = sres.getItem();
				
				log.info("query: for cycle: item kiwiId is #0", item.getKiwiIdentifier());
				
				String someUrl= "some Url to produce so it can be the src of an <image/>";
				
				res.addImageItem(new ImageItemJSON(
						item.getKiwiIdentifier(), 
						item.getTitle(), 
						someUrl,
						sres.getScore()));
			}
			res.addImageItem(new ImageItemJSON(
					"anUri", "a title", "an imageUrl", (float)1.234));
			res.addImageItem(new ImageItemJSON(
					"anUri2", "a title2", "an imageUrl2", (float)2.234));
			return res;
		}else{
			log.info("user #0 doesn't exist.", login);
			return new ImageBrowserResponseJSON(1, "User doesn't exist");
		}
	}
	
	
}
