package tagit2.util.exchange;

public class BlogPost {
	
	private String type,title,teaser,content,modified;
	private double latitude,longitude;
	private long id;

	public BlogPost(long id, String type, String title, String teaser, String content, double latitude, double longitude, String modified) {
		this.title = title;
		this.teaser = teaser;
		this.content = content;
		this.type = type;
		this.modified = modified;
		this.setLatitude(latitude);
		this.setLongitude(longitude);
		this.setId(id);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTeaser() {
		return teaser;
	}

	public void setTeaser(String teaser) {
		this.teaser = teaser;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

}
