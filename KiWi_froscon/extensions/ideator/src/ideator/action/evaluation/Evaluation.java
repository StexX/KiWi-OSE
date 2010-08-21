package ideator.action.evaluation;

import java.util.LinkedList;

public class Evaluation {
	
	private LinkedList<Paragraph> paragraphs;

	public Evaluation() {
		this.paragraphs = new LinkedList<Paragraph>();
	}
	
	public void add(Paragraph p) {
		this.paragraphs.add(p);
	}

	public LinkedList<Paragraph> getParagraphs() {
		return this.paragraphs;
	}
	
	public boolean isComplete() {
		for( Paragraph p : paragraphs ) {
			if( !p.isEvaluated() ) return false;
		}
		return true;
	}
	
	public String toXMLString() {
		StringBuffer b = new StringBuffer();
		b.append("<?xml version=\"1.0\"?>");
		b.append("<evaluation>");
		for( Paragraph p : paragraphs ) {
			b.append( p.toXMLString() );
		}
		b.append("</evaluation>");
		return b.toString();
	}
	
}
