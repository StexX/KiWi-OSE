package kiwi.action.search.sun.pojo;

import java.util.LinkedList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="searchResponse")
public class JSONSearchResponse {
	
	private String callback;
	
	public boolean hasRecommendations;
	
	private JSONQueryStr queryStr;
	private LinkedList<JSONTemplate> templates;
	private JSONResults result;
	private JSONRecommendations recom;
	
	public JSONSearchResponse(String callback) {
		this.queryStr = new JSONQueryStr();
		this.templates = new LinkedList<JSONTemplate>();
		this.result = new JSONResults();
		this.callback = callback;
	}
	
	public JSONSearchResponse(String callback,boolean hasRecommendations) {
		this(callback);
		this.hasRecommendations = hasRecommendations;
	}
	
	public void setOrder(String order) {
		this.result.order = order;
	}
	
	public void setOrderBy(String orderby) {
		this.result.orderBy = orderby;
	}
	
	public void setTotal(int total) {
		this.result.total = total;
	}
	
	public void setPage(int page) {
		this.result.page = page;
	}
	
	public void setQueryStr(String query) {
		queryStr.query = query;
	}
	
	public void addResult(JSONSingleResult result) {
		this.result.array.add(result);
	}
	
	public void setRecommendations(JSONRecommendations recommendations) {
		this.recom = recommendations;
	}
	
	public String toJSON() {
		
		//set necessary templates
		setTemplates();
		
		StringBuffer b = new StringBuffer();
		b.append(callback);
		b.append("({");
		
		//query
		b.append(queryStr.toJSON());
		b.append(",");
		
		//templates
		b.append("'templates':{");
		for( int i = 0; i < templates.size(); i++ ) {
			b.append(templates.get(i).toJSON());
			if( i < templates.size()-1 ) b.append(",");
		}
		b.append("},");
		
		//search results
		b.append(result.toJSON());
		
		if( hasRecommendations ) {
			b.append(",");
			b.append(recom.toJSON());
		}
		
		b.append("});");
		return b.toString();
	}
	
	private void setTemplates() {
		for( JSONSingleResult r : result.array ) {
			boolean contains = false;
			for( JSONTemplate t : templates ) {
				if( r.getType().equals(t.type)) {
					contains = true;
					break;
				}
			}
			if( !contains ) templates.add(new JSONTemplate(r.getType(), r.getTemp()));
		}
	}

}
