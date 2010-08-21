package kiwi.action.search.sun.pojo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.User;

import org.json.JSONException;
import org.json.JSONObject;


public class JSONCommunityResult extends JSONObject implements JSONSingleResult {

	public static final String type = "communityTypeItem";
	private static final String temp = "communityTypeItemTemplateUrl";
	
	private List<Map<String, Object>> admins = new LinkedList<Map<String,Object>>();
	private List<Map<String, Object>> topContributions = new LinkedList<Map<String,Object>>();
	
	@Override
	public String getTemp() {
		return temp;
	}
	@Override
	public String getType() {
		return type;
	}
	@Override
	public String toJSON() {
		try {
			put("type", getType());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return super.toString();
	}
	public void setName(String name) throws JSONException {
		put("name", name);
	}
	public void setMemberCount(int memberCount) throws JSONException {
		put("member_count", memberCount);
	}
	public void addAdmin(User adminUser) throws JSONException {
		HashMap<String, Object> adminMap = new HashMap<String, Object>();
		adminMap.put("name", adminUser.getLogin());
		admins.add(adminMap);
		put("admins", admins);
	}
	public void addTopContribution(ContentItem topContribution) throws JSONException {
		HashMap<String, Object> topContributionMap = new HashMap<String, Object>();
		topContributionMap.put("uri", getCIUri(topContribution));
		topContributionMap.put("title", topContribution.getTitle());
		
		topContributions.add(topContributionMap);
		put("top_contributions", topContributions);		
	}
	
	public void setTopContributor(User topContributor) throws JSONException {
		HashMap<String, Object> topContributorMap = new HashMap<String, Object>();
		topContributorMap.put("login", topContributor.getLogin());
		
		put("top_contributor", topContributorMap);
	}

	private String getCIUri(ContentItem ci) {
		return ((KiWiUriResource)ci.getResource()).getUri();
	}
	public void setCeq(double communityEquity) throws JSONException {
		put("ceq", communityEquity);
	}
	public void setContribsCount(int contribsCount) throws JSONException {
		put("contribs_count", contribsCount);
	}
	
}
