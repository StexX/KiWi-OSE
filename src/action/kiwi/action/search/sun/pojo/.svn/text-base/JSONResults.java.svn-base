package kiwi.action.search.sun.pojo;

import java.util.LinkedList;

public class JSONResults {
	
	public String order;
	public String orderBy;
	public int total;
	public int page;
	
	public LinkedList<JSONSingleResult> array;
	
	public JSONResults() {
		array = new LinkedList<JSONSingleResult>();
	}
	
	public String toJSON() {
		StringBuffer b = new StringBuffer();
		b.append("'results':{");
		
		//order
		b.append("'order':'");
		b.append(order);
		b.append("',");
		
		//total
		b.append("'total':");
		b.append(total);
		b.append(",");
		
		//page
		b.append("'page':");
		b.append(page);
		b.append(",");
		
		//results
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
