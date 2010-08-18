package kiwi.widgets.equity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "activity")
@XmlAccessorType(XmlAccessType.FIELD)
public class CEQActivityJSON implements Comparable<CEQActivityJSON>{

	private static final DateFormat DF = new SimpleDateFormat("dd.MM.yy HH:mm:ss", Locale.GERMAN);
	
	private String type;
	
	private Date date;
	
	private String smallDate;

	public CEQActivityJSON() {
		super();
	}

	public CEQActivityJSON(String type, Date date) {
		super();
		this.type = type;
		this.date = date;
		this.smallDate = DF.format(date);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSmallDate() {
		return smallDate;
	}

	public void setSmallDate(String smallDate) {
		this.smallDate = smallDate;
	}

	@Override
	public int compareTo(CEQActivityJSON o) {
		return date.compareTo(o.getDate());
	}
	
}
