package kiwi.action.search.sun.pojo;


public class JSONQueryStr {
	
	public String query;
	
	public String toJSON() {
		StringBuffer b = new StringBuffer();
		b.append("'queryStr':{");
		
		//query
		b.append("'query':'");
		b.append(query);
		b.append("'");
		
		b.append("}");
		return b.toString();
	}
	
}
