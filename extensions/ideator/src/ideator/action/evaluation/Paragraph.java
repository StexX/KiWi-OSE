package ideator.action.evaluation;

import java.util.LinkedList;

public class Paragraph {

	private LinkedList<Criteria> criterias;
	private String title;
	private String user;
	private boolean rendered;
	
	public Paragraph(String title) {
		this.title = title;
		this.criterias = new LinkedList<Criteria>();
		this.rendered = false;
	}
	
	public void add(Criteria q) {
		this.criterias.add(q);
	}
	
	public LinkedList<Criteria> getCriteria() {
		return this.criterias;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}
	
	public void setUser(String user) {
		this.user = user;
	}

	public String getUser() {
		return user;
	}
	
	public boolean isEvaluated() {
		for( Criteria c : criterias ) {
			if( !c.isEvaluated() ) return false;
		}
		return true;
	}

	public void setRendered(boolean rendered) {
		this.rendered = rendered;
	}

	public boolean isRendered() {
		return rendered;
	}

	public String toXMLString() {
		StringBuffer b = new StringBuffer();
		b.append("<paragraph title=\""+title+"\"");
		if( user != null ) {
			b.append(" user=\""+user+"\"");
		}
		b.append(">");
		for( Criteria c : criterias ) {
			b.append(c.toXMLString());
		}
		b.append("</paragraph>");
		return b.toString();
	}
	
}
