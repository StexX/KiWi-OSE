/**
 * 
 */
package kiwi.action.tagging.pojo;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kiwi.action.tagging.TaggingWebService.OrderTypes;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Consumes information for the response for a getTags WebService request and through the 
 * toString() method produces a JSON String. 
 * @author Szaby Grünwald
 */
public class JSONTagListResults extends JSONObject {

	private List<Map<String,Object>> tags;
	
	public JSONTagListResults() {
		super();
		tags = new LinkedList<Map<String,Object>>();
		try {
			this.put("items", tags);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void setResource(String docUri) throws JSONException {
		this.put("resource", docUri);
	}
	
	public void addTag(String uri, Long usage, String label, double tq, boolean controlled, boolean isOwnTag) {
		Map<String, Object> tagJSON = new LinkedHashMap<String, Object>();
		tagJSON.put("uri", uri);
		tagJSON.put("label", label);
		tagJSON.put("usage", usage);
		tagJSON.put("tq", tq);
		tagJSON.put("controlled", controlled);
		tagJSON.put("isOwnTag", isOwnTag);
		
		tags.add(tagJSON);
		try {
			put("items", tags);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void setCurrentLogin(String login) {
		try {
			put("currentLogin", login);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param orderType defines the order of display- 'alpha' (alphabetically), 'usage', 'equity' (tag equity) 
	 * @param reverse
	 */
	public void sortTags(OrderTypes orderType, boolean reverse) {
		
		Comparator<Map<String,Object>> tagComp = null;
		switch (orderType) {
		case ALPHA:
			tagComp = new Comparator<Map<String,Object>>() {
				
				@Override
				public int compare(Map<String, Object> arg0,
						Map<String, Object> arg1) {
					String label0=(String)arg0.get("label");
					String label1=(String)arg1.get("label");
					return label0.compareToIgnoreCase(label1);
				}
				
			};
			break;

		case USAGE:
			tagComp = new Comparator<Map<String,Object>>() {
				
				@Override
				public int compare(Map<String, Object> arg0,
						Map<String, Object> arg1) {
					Long usage0 = (Long)arg0.get("usage");
					Long usage1 = (Long)arg1.get("usage");
					return usage0.compareTo(usage1);
				}
				
			};
			break;

		case EQUITY:
			tagComp = new Comparator<Map<String,Object>>() {
				
				@Override
				public int compare(Map<String, Object> arg0,
						Map<String, Object> arg1) {
					Double tq0 = (Double)arg0.get("tq");
					Double tq1 = (Double)arg1.get("tq");
					return tq0.compareTo(tq1);
				}
				
			};
			break;
			
		}
		if(tagComp!=null)
			Collections.sort(tags, tagComp);
		if(reverse)
			Collections.reverse(tags);
		
	}

	public void setOrder(String order) {
		try {
			put("order", order);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void setReverse(String reverse) {
		try {
			put("reverse", reverse);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
}
