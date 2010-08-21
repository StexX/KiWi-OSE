package ideator.action.evaluation;

import java.util.LinkedList;

public class Criteria {
	
	private String title;
	private LinkedList<Rating> ratings;
	private String comment;
	
	private int selected;
	
	public Criteria(String title) {
		this.title = title;
		ratings = new LinkedList<Rating>();
		this.selected = -1;
	}
	
	public void add(Rating a) {
		ratings.add(a);
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}

	public LinkedList<Rating> getRatings() {
		return ratings;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setSelected(int selected) {
		this.selected = selected;
	}

	public int getSelected() {
		return selected;
	}
	
	public boolean isEvaluated() {
		return selected != -1;
	}
	
	public String toXMLString() {
		StringBuffer b = new StringBuffer();
		b.append("<criteria title=\""+title+"\">");
		b.append("<rating>");
		for( int i = 0; i < ratings.size(); i++ ) {
			if( selected == i ) {
				b.append("<choice value=\""+ratings.get(i).getValue()+"\" selected=\"true\">");
			} else {
				b.append("<choice value=\""+ratings.get(i).getValue()+"\">");
			}
			b.append(ratings.get(i).getTitle());
			b.append("</choice>");
		}
		b.append("</rating>");
		if( comment != null ) {
			b.append("<comment>"+comment+"</comment>");
		}
		b.append("</criteria>");
		return b.toString();
	}

}
