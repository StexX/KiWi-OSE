package kiwi.action.search.sun;

import java.util.LinkedList;
import java.util.List;

import kiwi.action.search.sun.pojo.JSONCommunityResult;
import kiwi.action.search.sun.pojo.JSONContentResult;
import kiwi.action.search.sun.pojo.JSONPersonResult;
import kiwi.action.search.sun.pojo.JSONRecommendations;
import kiwi.action.search.sun.pojo.JSONSearchResponse;
import kiwi.action.search.sun.pojo.JSONSingleRecommendation;
import kiwi.action.search.sun.pojo.JSONSingleResult;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.equity.EquityService;
import kiwi.api.rating.RatingService;
import kiwi.api.search.KiWiSearchResults;
import kiwi.api.search.KiWiSearchResults.SearchResult;
import kiwi.api.user.KiWiProfileFacade;
import kiwi.api.user.ProfileService;
import kiwi.api.user.UserService;
import kiwi.model.Constants;
import kiwi.model.ceq.SunSpaceComunityFacade;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.json.JSONException;

@Name("kiwi.service.sunSearchResultService")
@Scope(ScopeType.STATELESS)
public class SunSearchResultService {
	
	private static final int DESCRIPTION_LENGTH = 100;
	
	@Logger
	private static Log log;

	@In
	private EquityService equityService;
	
	@In
	private UserService userService;
	
	@In(create=true)
	private ProfileService profileService;
	
	@In
	private KiWiEntityManager kiwiEntityManager;

	@In
	private RatingService ratingService;
	
	public JSONSearchResponse toJSONResponse( KiWiSearchResults kiwi_res, String callback ) {
		JSONSearchResponse response = new JSONSearchResponse(callback);
		log.info("toJSONResponse: nr of KiWiSearchResults found: #0", kiwi_res.getResultCount());
		log.info("toJSONResponse: nr of KiWiSearchResults to serialize: #0", kiwi_res.getResults().size());
		for( SearchResult res : kiwi_res.getResults() ) {
			ContentItem ci = res.getItem();
			JSONSingleResult jsonResult = null;
			if(ci==null){
				log.error("ci is null!");
			} else {
				for( KiWiResource type : ci.getTypes() ) {
					String seRQLID = type.getSeRQLID();
					
					// is the content a user?
					if(seRQLID.contains(Constants.NS_KIWI_CORE + "User")) {
						jsonResult = getJSONPersonResult(res);
						break;
					}
					
					// is the content a community?
					if(seRQLID.contains(Constants.NS_SUN_SPACE + "SunSpaceComunity")) {
						jsonResult = getJSONCommunityResult(res);
						break;
					}
				}
				
				// not any of the above? then default
				if(jsonResult == null)
					jsonResult = getJSONContentResult(res);
				
				response.addResult(jsonResult);
			}
		}
		return response;
	}
	
	public JSONRecommendations getJSONRecommendations(List<ContentItem> list) {
		JSONRecommendations rec = new JSONRecommendations();
		for( ContentItem ci : list ) {
			JSONSingleRecommendation r = new JSONSingleRecommendation();
			r.setTitle(ci.getTitle());
			r.setUrl(((KiWiUriResource)ci.getResource()).getUri());
			r.setAuthor_name(ci.getAuthor().getLogin());
			rec.addRecommendation(r);
		}
		return rec;
	}
	
	private JSONCommunityResult getJSONCommunityResult(SearchResult sr) {
		JSONCommunityResult res = new JSONCommunityResult();
		ContentItem ci = sr.getItem();
		SunSpaceComunityFacade com = kiwiEntityManager.createFacade(ci, SunSpaceComunityFacade.class);
		
		log.debug("getJSONCommunityResult: ci.title(id): #0(#1)", ci.getTitle(), ci.getId());

		try {
			res.setName(com.getTitle());
			res.setMemberCount(com.getParticipants().size());
			for(ContentItem admin:com.getAdministrators()){
				String adminUri = ((KiWiUriResource)admin.getResource()).getUri();
				User adminUser = userService.getUserByUri(adminUri);
				res.addAdmin(adminUser);
			}
			for(ContentItem topContribution:getTopContributions(com)){
				res.addTopContribution(topContribution);
			}
			res.setTopContributor(getTopContributor(com));
			
			res.setCeq(equityService.getCommunityEquity(com));
			res.setContribsCount(getContributions(com).size());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	private User getTopContributor(SunSpaceComunityFacade com) {
		List<ContentItem> contributors = com.getParticipants();
		User res=null;
		double maxCeq=-999;
		for(ContentItem contr:contributors){
			User contributor = userService.getUserByUri(getCIUri(contr));
			double ceq = equityService.getCommunityEquity(contributor);
			if(ceq>maxCeq){
				maxCeq = ceq;
				res = contributor;
			}
		}
		return res;
	}

	private List<ContentItem> getTopContributions(SunSpaceComunityFacade com) {
		// TODO Auto-generated method stub
		return new LinkedList<ContentItem>();
	}

	private List<ContentItem> getContributions(SunSpaceComunityFacade com) {
		// TODO Auto-generated method stub
		
		return new LinkedList<ContentItem>();
	}

	private JSONPersonResult getJSONPersonResult(SearchResult sr) {
		JSONPersonResult res = new JSONPersonResult();
		ContentItem ci = sr.getItem();
		String uri = ((KiWiUriResource)ci.getResource()).getUri();
		User u = userService.getUserByUri(uri);
		KiWiProfileFacade profile = profileService.getProfile(u);
		
		log.info("getJSONPersonResult: ci.title(id): #0(#1)", ci.getTitle(), ci.getId());

		try {
			res.setUrl(uri);
			res.setAvatarUrl(profileService.getProfilePhotoDownloadUrl(u));
			res.setFirstName(u.getFirstName());
			res.setLastName(u.getLastName());
			res.setPhone(profile.getPhone());
			res.setEmail(profile.getEmail());
			res.setCeq(equityService.getCommunityEquity(u));
			res.setPeq(equityService.getPersonalEquity(u));
			res.setId(u.getId());
			List<ContentItem> topContributions = equityService.getTopContributions(u);
			for(ContentItem topContr:topContributions){
				res.addTopContribution(topContr);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	private String getCIUri(ContentItem ci) {
		return ((KiWiUriResource)ci.getResource()).getUri();
	}
	
	private JSONContentResult getJSONContentResult(SearchResult sr) {
		JSONContentResult res = new JSONContentResult();
		
		ContentItem ci = sr.getItem();
		
		log.info("getJSONContentResult: ci.title(id): #0(#1)", ci.getTitle(), ci.getId());
		try {
			//author
			if(ci.getAuthor() == null){
				log.error("getJSONContentResult: contentItem.author is null! title(id): #0(#1)", ci.getTitle(), ci.getId());
				res.setAuthor_name("n/a");
				res.setAuthor_url("n/a");
			} else {
				res.setAuthor_name(ci.getAuthor().getLogin());
				res.setAuthor_url(((KiWiUriResource)ci.getAuthor().getContentItem().getResource()).getUri());
			}
			
			//description
			String hp = sr.getHighlightPreview();
			if( hp == null || hp.equals("") ) {
				if( ci.getTextContent() != null )
					hp = ci.getTextContent().getPlainString();
				else
					hp = "";
				if( hp != null && hp.length() > DESCRIPTION_LENGTH )
					hp = hp.substring(0, DESCRIPTION_LENGTH);
			}
			res.setDescription(clearString(hp));
			
			//ceq and rating
			res.setIq(sr.getCeq());
			res.setRating(ratingService.getRatingAverage(ci));
			
			//title
			res.setTitle(ci.getTitle());
			
			//uri
			res.setUrl(((KiWiUriResource)ci.getResource()).getUri());
			
			//views
			res.setViews( equityService.getHits(ci) );
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	private String clearString(String s) {
		//replace linebreaks and tabs
	    s = s.replaceAll("\\n","");
	    s = s.replaceAll("\\t","");
		return s;
	}
	
}
