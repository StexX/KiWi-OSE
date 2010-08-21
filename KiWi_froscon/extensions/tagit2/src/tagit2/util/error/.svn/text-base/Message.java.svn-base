package tagit2.util.error;

import java.util.LinkedList;

public class Message {
	
	public static final int ERROR = 0;
	public static final int MESSAGE = 1;
	
	private static final String MESSAGE_HEADER = "Folgende Angaben wurden geändert:";
	private static final String ERROR_HEADER = "Folgende Fehler sind aufgetreten:";
	
	private String title;
	private LinkedList<String> changes;
	private int type;
	
	public Message() {
		type = MESSAGE;
		changes = new LinkedList<String>();
	}
	
	public Message(String title) {
		this.title = title;
		type = MESSAGE;
		changes = new LinkedList<String>();
	}
	
	public void addLine(String s) {
		changes.add(s);
	}

	public String getValue() {
		if( changes.isEmpty() && title == null )
			return null;
		else {
			StringBuffer b = new StringBuffer();
			b.append("<h2>");

			if( title != null ) {
				b.append(title);
			} else if( type == ERROR ) {
				b.append(ERROR_HEADER);
			} else {
				b.append(MESSAGE_HEADER);
			}
			b.append("</h2><ul>");
			
			for( String s : changes ) {
				b.append("<li>"+s+"</li>");
			}
			
			b.append("</ul>");
			return b.toString();
		}
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}
	
	public boolean hasMessages() {
		return !changes.isEmpty();
	}
}
