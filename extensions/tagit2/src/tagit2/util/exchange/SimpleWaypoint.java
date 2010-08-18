package tagit2.util.exchange;

public class SimpleWaypoint {
	
	private double longitude;
	private double latitude;
	private String title;
	private long id;
	private boolean empty;
	
	//TODO undo!!!
	private int width, height;
	
	public SimpleWaypoint(long id, double latitude, double longitude, String title) {
		this.id = id;
		this.longitude = longitude;
		this.latitude = latitude;
		this.title = title;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	public void setEmpty(boolean empty) {
		this.empty = empty;
	}
	public boolean isEmpty() {
		return empty;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getHeight() {
		return height;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getWidth() {
		return width;
	}

}
