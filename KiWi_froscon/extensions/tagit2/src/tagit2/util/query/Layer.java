package tagit2.util.query;

import kiwi.model.content.ContentItem;

public class Layer {
	
	private ContentItem layer;
	//in days
	private String timerange;
	
	private boolean show;
	
	private LayerRangeUnit range;
	
	public Layer(ContentItem layer) {
		this.layer = layer;
		this.timerange = null;
		this.show = true;
		this.range = LayerRangeUnit.DAY;
	}
	public ContentItem getLayer() {
		return layer;
	}
	public void setLayer(ContentItem layer) {
		this.layer = layer;
	}
	public String getTimerange() {
		return timerange;
	}
	public void setTimerange(String timerange) {
		this.timerange = timerange;
	}
	
	public String getQueryString(boolean useTimerange) {
		StringBuffer qString = new StringBuffer();
		if( !useTimerange || timerange == null) {
			qString.append("type:\"");
			qString.append(layer.getResource().getKiwiIdentifier());
			qString.append("\"");
		} else {
			qString.append(" (type:\"");
			qString.append(layer.getResource().getKiwiIdentifier());
			qString.append("\" AND ");
			qString.append("modified:[NOW-");
			qString.append(timerange);
			qString.append("/DAY TO NOW])");
		}
		return qString.toString();
	}
	public void setShow(boolean show) {
		this.show = show;
	}
	public boolean isShow() {
		return show;
	}
	public LayerRangeUnit getRange() {
		return range;
	}
	
	public String getTitle() {
		return layer.getTitle();
	}
	
	public void setRange(LayerRangeUnit range) {
		this.range = range;
	}
	
}
