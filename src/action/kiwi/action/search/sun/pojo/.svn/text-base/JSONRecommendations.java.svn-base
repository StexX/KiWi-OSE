package kiwi.action.search.sun.pojo;

import java.util.LinkedList;

public class JSONRecommendations {
	
	LinkedList<JSONSingleRecommendation> array;
	
	public JSONRecommendations() {
		array = new LinkedList<JSONSingleRecommendation>();
	}
	
	public void addRecommendation(JSONSingleRecommendation rec) {
		array.add(rec);
	}
	
	public String toJSON() {
		StringBuffer b = new StringBuffer();
		b.append("'recommendations':{");
		
		//array
		b.append("'array':[");
		for( int i = 0; i < array.size(); i++ ) {
			b.append(array.get(i).toJSON());
			if( i < array.size()-1 ) b.append(",");
		}
		b.append("]");
		
		b.append("}");
		return b.toString();
	}

}
