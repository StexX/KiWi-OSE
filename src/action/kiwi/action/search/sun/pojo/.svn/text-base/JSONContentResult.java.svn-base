package kiwi.action.search.sun.pojo;

import org.json.JSONException;
import org.json.JSONObject;


public class JSONContentResult extends JSONObject implements JSONSingleResult {

	private static final String type = "contentTypeItem";
	private static final String temp = "contentTypeItemTemplateUrl";
	
/*	
	public String toJSON() {
		StringBuffer b = new StringBuffer();
		b.append("{");
		
		b.append("'type':'");
		b.append(type);
		b.append("',");
		
		b.append("'title':'");
		b.append(title);
		b.append("',");
		
		b.append("'url':'");
		b.append(url);
		b.append("',");
		
		b.append("'description':'");
		b.append(description);
		b.append("',");
		
		b.append("'author_name':'");
		b.append(author_name);
		b.append("',");
		
		b.append("'author_url':'");
		b.append(author_url);
		b.append("',");
		
		b.append("'rating':");
		b.append(rating);
		b.append(",");
		
		b.append("'iq':");
		b.append(iq);
		b.append(",");
		
		b.append("'views':");
		b.append(views);
		b.append(",");
		
		b.append("}");
		return b.toString();
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAuthor_name() {
		return author_name;
	}

	public void setAuthor_name(String authorName) {
		author_name = authorName;
	}

	public String getAuthor_url() {
		return author_url;
	}

	public void setAuthor_url(String authorurl) {
		author_url = authorurl;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public double getIq() {
		return iq;
	}

	public void setIq(double iq) {
		this.iq = iq;
	}

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

 */
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

	/**
	 * @param title the title to set
	 * @throws JSONException 
	 */
	public void setTitle(String title) throws JSONException {
		put("title", title);
	}

	/**
	 * @param url the url to set
	 * @throws JSONException 
	 */
	public void setUrl(String url) throws JSONException {
		put("url", url);
	}

	/**
	 * @param description the description to set
	 * @throws JSONException 
	 */
	public void setDescription(String description) throws JSONException {
		put("description", description);
	}

	/**
	 * @param authorName the author_name to set
	 * @throws JSONException 
	 */
	public void setAuthor_name(String authorName) throws JSONException {
		put("author_name", authorName);
	}

	/**
	 * @param authorUrl the author_url to set
	 * @throws JSONException 
	 */
	public void setAuthor_url(String authorUrl) throws JSONException {
		put("author_url", authorUrl);
	}

	/**
	 * @param rating the rating to set
	 * @throws JSONException 
	 */
	public void setRating(double rating) throws JSONException {
		put("rating", rating);
	}

	/**
	 * @param iq the iq to set
	 * @throws JSONException 
	 */
	public void setIq(double iq) throws JSONException {
		put("iq", iq);
	}

	/**
	 * @param views the views to set
	 * @throws JSONException 
	 */
	public void setViews(int views) throws JSONException {
		put("views", views);
	}
}
