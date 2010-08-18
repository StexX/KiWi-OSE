package tagit2.util.error;

import java.util.LinkedList;
import java.util.List;

/**
 * this class is used to bundle several errors and to return a html string
 * @author tkurz
 *
 */
public class ErrorMessage {
	
	private static final String DEFAULT_HEADER = "Folgende Fehler sind aufgetreten und müssen behoben werden:";
	
	private String header;
	private List<String> errors;
	
	public ErrorMessage(String s) {
		header = s;
		errors = new LinkedList<String>();
	}
	
	public ErrorMessage() {
		this(DEFAULT_HEADER);
	}
	
	public void addMessage(String m) {
		this.errors.add(m);
	}
	
	@Override
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("<h1>");
		b.append(header);
		b.append("</h1>");
		b.append("<ul>");
		for( String e : errors ) {
			b.append("<li>");
			b.append(e);
			b.append("</li>");
		}
		b.append("</ul>");
		return b.toString();
	}
	
	public boolean errorOccurred() {
		return !errors.isEmpty();
	}

}
