package kiwi.action.search.sun.pojo;

public class JSONSingleRecommendation {

	private String title;
	private String url;
	private String author_name;
	
	public JSONSingleRecommendation() {}
	
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

	public String getAuthor_name() {
		return author_name;
	}

	public void setAuthor_name(String authorName) {
		author_name = authorName;
	}

	public String toJSON () {
		StringBuffer b = new StringBuffer();
		b.append("{");
		b.append("'title':'");
		b.append(title);
		b.append("','url':'");
		b.append(url);
		b.append("','author_name':'");
		b.append(author_name);
		b.append("'}");
		return b.toString();
	};
	
}
