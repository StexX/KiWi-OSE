package kiwi.action.search.sun.pojo;


public class JSONTemplate {
	public String type;
	public String templateUrl;
	
	public JSONTemplate(String type, String url) {
		this.type = type;
		this.templateUrl = url;
	}
	
	public String toJSON() {
		return "'"+type+"':'"+templateUrl+"'";
	}
}
