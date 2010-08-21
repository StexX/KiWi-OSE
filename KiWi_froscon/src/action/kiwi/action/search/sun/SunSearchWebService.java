package kiwi.action.search.sun;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import kiwi.action.search.KWQLSearchEngine;
import kiwi.action.search.KiWiSearchEngine;
import kiwi.action.search.SearchEngine;
import kiwi.action.search.sun.pojo.JSONSearchResponse;
import kiwi.api.event.KiWiEvents;
import kiwi.api.recommendation.RecommendationService;
import kiwi.api.search.KiWiSearchResults;
import kiwi.api.search.PersonalizedSearchService;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.user.User;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Conversation;
import org.jboss.seam.core.Events;
import org.jboss.seam.log.Log;

@Name("kiwi.webservice.sunSearchWebService")
@Scope(ScopeType.STATELESS)
@Path("/sun/search")
public class SunSearchWebService {

	@Logger
	private static Log log;

	/**
	 * initiate the personalized search
	 */
	@In(create = true)
	PersonalizedSearchService personalizedSearchService;

	@In(create = true)
	RecommendationService recommendationService;

	/**
	 * input current user; might affect the loading of the content item.
	 */
	@In(create = true)
	private User currentUser;

	@In(create = true, value = "kiwi.service.sunSearchResultService")
	private SunSearchResultService resService;

	@GET
	@Path("/query.json")
	@Produces("application/json")
	public Response runSearch(
			@QueryParam("query") String query,
			@QueryParam("jsonpCallback") String callback,
			@QueryParam("pageSize") @DefaultValue("10") int pageSize,
			@QueryParam("page") @DefaultValue("1") int page,
			@QueryParam("order") @DefaultValue("desc") String order,
			@QueryParam("orderBy") @DefaultValue("ceq") String orderBy,
			@QueryParam("qLang") @DefaultValue("kiwi") String queryLanguage,
			@QueryParam("personal") @DefaultValue("false") boolean personalSearch,
			@QueryParam("recommendation") @DefaultValue("false") boolean recommendation) {
		try {

			KiWiSearchResults searchResults;
			
			TripleStore ts = (TripleStore)Component.getInstance("tripleStore");
			ts.setNamespace("sunspace", "http://sunspace.sfbay.sun.com/display/SunSpaceComunity");
			log.info("-----sunspace namespace prefix: #0", ts.getNamespacePrefix("http://sunspace.sfbay.sun.com/display/SunSpaceComunity"));

			log.info("runSearch.s query: #0", query);

			if (query == "" || query == null)
				return Response.noContent().build();

			SearchEngine searchEngine;
			log.info("Query Lang #0, query string #1", queryLanguage, query);
			if ("kwql".equals(queryLanguage)) {
				log.info("searchengine set to kwql");
				searchEngine = (KWQLSearchEngine) Component
						.getInstance("kwqlSearchEngine");
			} else {
				searchEngine = (KiWiSearchEngine) Component
						.getInstance("kiwiSearchEngine");
			}
			log.info("searchengine: #0", searchEngine.toString());
			searchEngine.setPageSize(pageSize);
			searchEngine.setSearchQuery(query);
			searchEngine.setOrder(order);
			searchEngine.setOrderBy(orderBy);
			searchEngine.runSearch();
			searchEngine.setPage(page);
			searchResults = searchEngine.getSearchResults();

			if (personalSearch && (currentUser.getId() != 1L)) {
				searchResults = personalizedSearchService
						.runPersonilazedSearch(searchResults);
			}

			if (currentUser != null
					&& Conversation.instance().getViewId() != null
					&& Conversation.instance().getViewId().contains("search")
					&& query != "")
				Events.instance().raiseEvent(KiWiEvents.ACTIVITY_SEARCH,
						currentUser, query);

			// response
			JSONSearchResponse response = resService.toJSONResponse(
					searchResults, callback);
			response.setQueryStr(query);
			response.setOrder(order);
			response.setOrderBy(orderBy);
			response.setPage(page);
			response.setTotal((int) searchResults.getResultCount());

			// get recommendation
			if (recommendation) {
				try {
					response.hasRecommendations = true;
					response
							.setRecommendations(resService
									.getJSONRecommendations(recommendationService
											.getRecommendationsByTag(query,
													currentUser)));
				} catch (Exception e) {
					e.printStackTrace();
					log.error("some failure while calculating recommendations");
					response.hasRecommendations = false;
				}
			}
			// return
			return Response.ok(response.toJSON()).build();

		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().entity(e.getCause().getLocalizedMessage()).build();
		}
	}

}
