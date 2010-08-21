package kiwi.view.dashboard.main.client;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * Transport object for one field of a form bringing one label and a value to the gwt frontend.
 * TODO extend by all supported gwt  types.
 * @author Szaby Grünwald
 *
 */
public class UIFormFieldValue implements IsSerializable{

	private String label;
	private String type;
	
	private String stringValue;
	private Date dateValue;
	private Boolean booleanValue;
	private Double doubleValue;
	
	public UIFormFieldValue() {
	}
	
	public UIFormFieldValue(String label) {
		super();
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public String getStringValue() {
		return stringValue;
	}

	public Date getDateValue() {
		return dateValue;
	}
	
	public double getDoubleValue() {
		return doubleValue;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
		this.type = "String";
	}

	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
		this.type = "Date";
	}

	public void setDoubleValue(Double doubleValue) {
		this.doubleValue = doubleValue;
		this.type = "Double";
	}

	public Boolean getBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(Boolean booleanValue) {
		this.booleanValue = booleanValue;
		this.type = "Boolean";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getValue() {
		if("String".equals(this.getType()))
			return this.getStringValue();
		else if("Date".equals(this.getType()))
			return this.getDateValue();
		else if("Boolean".equals(this.getType()))
			return this.getBooleanValue();
		else if("Double".equals(this.getType()))
			return this.getDoubleValue();
		else return "Blah";
	}

	/**
	 * 
	 * @param value
	 */
	public void setValue(Object value) {
		if(value instanceof String){
			this.setStringValue((String)value);
		} else if(value instanceof Date){
			this.setDateValue((Date)value);
		} else if(value instanceof Boolean){
			this.setBooleanValue((Boolean)value);
		} else if(value instanceof Double){
			this.setDoubleValue((Double)value);
		}
	}
	
	public void setValue(Object value, String type){
		if(type == "String")
			this.setStringValue((String)value);
		else if(type == "Date")
			this.setDateValue((Date)value);
		else if(type == "Boolean")
			this.setBooleanValue((Boolean)value);
		else if(type == "Double")
			this.setDoubleValue((Double)value);
	}
	
}
