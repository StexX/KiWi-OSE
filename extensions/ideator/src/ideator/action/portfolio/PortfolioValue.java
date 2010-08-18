package ideator.action.portfolio;

public class PortfolioValue {
	
    private String color;
    private String title;
    private long cid;
    private boolean clickable;
    private long [][] data;
    private String author;
    private String created;
	
    public PortfolioValue(String color, String title, long cid,
			boolean clickable, long[][] data, String author, String created) {
		this.color = color;
		this.title = title;
		this.cid = cid;
		this.clickable = clickable;
		this.data = data;
		this.author = author;
		this.created = created;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getCid() {
		return cid;
	}

	public void setCid(long cid) {
		this.cid = cid;
	}

	public boolean isClickable() {
		return clickable;
	}

	public void setClickable(boolean clickable) {
		this.clickable = clickable;
	}

	public long[][] getData() {
		return data;
	}

	public void setData(long[][] data) {
		this.data = data;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}
    
    

}
