package kiwi.action.search.sun.pojo;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kiwi.model.content.ContentItem;

import org.json.JSONException;
import org.json.JSONObject;


public class JSONPersonResult extends JSONObject implements JSONSingleResult {

	public static final String type = "personTypeItem";
	private static final String temp = "personTypeItemTemplateUrl";
	
	private List<Map<String, Object>> topContributions = new LinkedList<Map<String, Object>>();
		
	public String getType() {
		return type;
	}

	public String getTemp() {
		return temp;
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

	public void setUrl(String uri) throws JSONException {
		put("url", uri);
	}

	public void setFirstName(String firstName) throws JSONException {
		put("first_name", firstName);
	}

	public void setLastName(String lastName) throws JSONException {
		put("last_name", lastName);
	}

	public void setPhone(String phone) throws JSONException {
		put("phone", phone);
	}

	public void setEmail(String email) throws JSONException {
		put("email", email);
	}

	public void setCeq(double communityEquity) throws JSONException {
		put("ceq", communityEquity);
	}

	public void setPeq(double personalEquity) throws JSONException {
		put("peq",personalEquity);
	}

	public void addTopContribution(ContentItem topContr) throws JSONException {
		// TODO implement a top contribution
		
		put("topContributions", topContributions);
	}

	public void setAvatarUrl(String profilePhotoDownloadUrl) throws JSONException {
		put("avatar_url", profilePhotoDownloadUrl);
	}

	public void setId(Long id) throws JSONException {
		put("id", id);
	}
	
}
