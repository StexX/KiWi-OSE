/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 2008-2009, The KiWi Project (http://www.kiwi-project.eu)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * - Redistributions of source code must retain the above copyright notice, 
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, 
 *   this list of conditions and the following disclaimer in the documentation 
 *   and/or other materials provided with the distribution.
 * - Neither the name of the KiWi Project nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * Contributor(s):
 * 
 * 
 */
package kiwi.view.dashboard.main.client;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Generic form block widget for the KiWi UI. 
 * The common behaviours:
 * - show a set of fields in view mode and give the user the possibility 
 * to switch to switch to edit mode.
 * - The view panel has a button to switch to edit mode
 * - Clicking the edit button the panel switches to a DynamicForm
 * - After editing the user can click Cancel or Save
 * - the used rpc exchange format is common
 * - the used save exchange format is common
 * - There is a default way of showing View and Edit of a field, but 
 * it's possible to change that default behaviour by overwriting the 
 * function in the subclass
 * 
 * A UIFormBlock is a can be shown on it's own by making the subclass to
 * an EntryPoint or use it as a Gadget in a Dashboard. 
 * @author Szaby Grünwald
 *
 */
public abstract class UIFormBlock  extends KiWiWidget {

	private Layout layout = new VLayout();
	
	private DynamicForm editForm;
	private LinkedHashMap<String, UIFormFieldValue> dataMap;
	
	/**
	 * return a map of field definitions and current values and gives it
	 * to the callback as parameter.
	 * The map has to contain all the fields listed by the getFieldOrder method
	 * and an extra Boolean field "editable", telling the ui whether to offer
	 * editing.
	 * @param asyncCallback
	 */
	protected abstract void getData(AsyncCallback<LinkedHashMap<String, UIFormFieldValue>> asyncCallback);

	/*
	 * View
	 */
	
	/**
	 * clears the layout and shows the new generated view.
	 */
	public void switchToView(boolean refresh) {
		layout.removeMembers(layout.getMembers());
		if(refresh){
			getViewPanel();// gets the data first!
		} else {
			layout.addMember(getViewForm());
		}
	}

	/**
	 * Generates the view layout. Takes the getFieldOrder and generates a field for each entry in the dataMap
	 * @return
	 */
	private Layout getViewForm() {
		Layout panel = new VLayout();
		panel.setWidth(300);
		panel.setDefaultHeight(10);

		for(String field:getFieldOrder()){
			UIFormFieldValue uiField = dataMap.get(field);
			if(uiField==null){
				log("!!uiField for " + field + " was null!");
            }

			addViewElement(panel, field, uiField.getLabel(), uiField.getValue());
		}
		
		final Button editButton = new Button("Edit " + getFormLabel());
		editButton.setPadding(5);
		editButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				switchToEdit();
			}
		});
		if(dataMap.get("editable")!=null)
			editButton.setDisabled(!(Boolean)(dataMap.get("editable").getValue()));

		panel.addMember(editButton);

		return panel;
	}


    /**
     * Adds a view field element to the view panel.
     * Overwrite this method for specific fields where a String or Date should simply just shown as a String or a date.
     * Don't forget to call addViewElement(panel,label,value);
     * @param panel
     * @param field
     * @param label
     * @param value
     */
    private void addViewElement(Layout panel, String field, String label, Object value) {
        final Widget specialViewElement = getSpecialViewElement(field, label, value);
        if(specialViewElement == null){
            addViewElement(panel,label,value);
        }

    }

    protected abstract Widget getSpecialViewElement(String field, String label, Object value);

    /**
	 * Add default view element for the field with label, showing the value to the panel
	 * @param panel
	 * @param label
	 * @param value
	 */
	private void addViewElement(Layout panel, String label, Object value) {
		if(value instanceof String){
			addViewElement(panel, label, (String)value);
		} else if(value instanceof Date)
			addViewElement(panel, label, (Date)value);
	}

    /**
	 * Put a label-value pair on the panel for view. 
	 * Only happens if the value is not empty.
	 * @param panel
	 * @param label
	 * @param value
	 */
	private void addViewElement(Layout panel, String label, String value) {
		if(value!=null && value.trim() != ""){
			final HTML element = new HTML(label + ": <b>" + value + "</b>");
			element.setHeight("2em");
			panel.addMember(element);
		}
	}

	/**
	 * Put a label-value pair on the panel for view. 
	 * Only happens if the value is not empty.
	 * @param panel
	 * @param label
	 * @param dateValue
	 */
	private void addViewElement(Layout panel, String label, Date dateValue) {
		if(dateValue != null)
			addViewElement(panel, label, dateValue.toString());
	}
	

	/*
	 * Edit
	 */
	
	/**
	 * clears the layout and shows the new generated edit form.
	 */
	public void switchToEdit() {
		layout.removeMembers(layout.getMembers());
		layout.addMember(createEditForm());
	}

	/**
	 * Generate the form layout.
	 * @return
	 */
	private Layout createEditForm() {
		
		/**
		 * The layout panel we're generating
		 */
		Layout resPanel = new VLayout();

		editForm = new DynamicForm();
		editForm.setWidth(400);
		
		FormItem[] fieldList = new FormItem[]{};
		
		/*
		 * Generate a FormField for each field
		 */
		for(String field:getFieldOrder()){
			UIFormFieldValue formField = dataMap.get(field);
			if(formField != null){
				FormItem formFieldItem = createFormField(field, formField); 
				if(formFieldItem != null)
					fieldList[fieldList.length] = formFieldItem;
				else log("!!formfield " + field + " creation gave back null!");
			} else log("!!formfield definition for " + field + " was null!");
			
		}
		editForm.setFields(fieldList);
		resPanel.addMember(editForm);
		
		final Button saveButton = new Button("Save " + getFormLabel());
		saveButton.setAutoFit(true);
		saveButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				save(new AsyncCallback<Void>() {
					
					@Override
					public void onSuccess(Void result) {
						switchToView(true);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						SC.warn("Caught exception: " + caught.getMessage());
					}
				});
			}
		});

		final Button cancelButton = new Button("cancel");
		cancelButton.setAutoFit(true);
		cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				switchToView(false);
			}
		});
		HLayout buttons = new HLayout();
		buttons.addMember(saveButton);
		
		buttons.addMember(cancelButton);
		buttons.setHeight("5em");
		buttons.setMembersMargin(5);
		resPanel.addMember(buttons);

		return resPanel;
	}

	/**
	 * Overwriting this method you can change the default behaviour of
	 * generating a FormItem.
	 * @param field
	 * @param formField
	 * @return
	 */
	protected FormItem createFormField(String field, UIFormFieldValue formField) {
		return createDefaultFormField(field, formField); 
	}

	/**
	 * This is a not implemented stub for a new type of FormItem. To take it as a template.
	 * It can be a slider, a map, anything.
	 * @param field
	 * @param formField
	 * @param checkboxList
	 * @return
	 */
	protected FormItem createCheckboxField(String field,
			UIFormFieldValue formField, LinkedHashMap<String, String> checkboxList) {
		FormItem res = null;
//		FormItem res = new SelectItem(field, formField.getLabel());
//		res.setValueMap(valueMap);
//		res.setValue((String)formField.getValue());
		return res;
	}


	/**
	 * returns a smartgwt FormItem with the name field for the formField 
	 * value descriptor. The type of the field depends on the value type.
	 * TODO extend with other types GWT and smartGWT supports
	 * @param field
	 * @param formField
	 * @return
	 */
	protected FormItem createDefaultFormField(String field,
			UIFormFieldValue formField) {
		if("String".equals(formField.getType())){
			return createStringFormField(field, formField);
		} else if("Date".equals(formField.getType())){
			return createDateFormField(field, formField);
		}
		SC.warn("The field " + field + " has an unimplemented type!");
		return null;
	}

	/**
	 * returns a smartgwt TextItem with the name field for the formField 
	 * value descriptor.
	 * @param field
	 * @param formField
	 * @return
	 */
	private FormItem createStringFormField(String field,
			UIFormFieldValue formField) {
//		log("createStringFormField for " + field);
		FormItem res = new TextItem(field, formField.getLabel());
		res.setValue((String)formField.getValue());
		return res;
	}

	/**
	 * returns a smartgwt DateItem with the name field for the formField 
	 * value descriptor.
	 * @param field
	 * @param formField
	 * @return
	 */
	private FormItem createDateFormField(String field,
			UIFormFieldValue formField) {
		DateItem res = new DateItem(field, formField.getLabel());
		res.setUseTextField(true);
		
		Date value = (Date)formField.getValue();
		if(value != null)
			res.setValue(value);
		return res;
	}

	/**
	 * returns a smartgwt SelectItem with the name field for the formField 
	 * value descriptor.
	 * @param field
	 * @param formField
	 * @param valueMap The selection possibilities.
	 * @return
	 */
	protected FormItem createSelectField(String field,
			UIFormFieldValue formField, LinkedHashMap<String, String> valueMap) {
		FormItem res = new SelectItem(field, formField.getLabel());
		res.setValueMap(valueMap);
		res.setValue((String)formField.getValue());
		return res;
	}

	/**
	 * collect all changes the user made in the form and call remoteSave.
	 * @param callback
	 */
	private void save(AsyncCallback<Void> callback) {
		LinkedHashMap<String, UIFormFieldValue> changeMap = getChanges(editForm, getFieldOrder());
		remoteSave(changeMap, callback);
	}

	/**
	 * Send a map of changes to the backend for storage.
	 * @param changeMap
	 * @param asyncCallback
	 */
	protected abstract void remoteSave(LinkedHashMap<String, UIFormFieldValue> changeMap,
			AsyncCallback<Void> asyncCallback);


	/**
	 * Collect the changed fields on the form for sending to the backend for storage.
	 * @param fields
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private LinkedHashMap<String, UIFormFieldValue> getChanges(DynamicForm form, LinkedList<String> fields) {
		LinkedHashMap<String, UIFormFieldValue> res = new LinkedHashMap<String, UIFormFieldValue>();
		LinkedHashMap<String, Object> formValues = (LinkedHashMap<String, Object>)form.getValues();
		for(String field:fields){
			UIFormFieldValue oldFormField = dataMap.get(field);
			Object oldValue = oldFormField.getValue();
			Object newValue = formValues.get(field);
			if(newValue == null){
				if(oldValue!=null)
					res.put(field, null);
			} else if(!newValue.equals(oldValue)){
				UIFormFieldValue newTO = new UIFormFieldValue();
				newTO.setValue(newValue);//, oldFormField.getType());
				res.put(field, newTO);
			}
		}
		return res;
	}

	/**
	 * strictly for debugging, it calls the good old-fashioned alert box
	 * and stops execution. 
	 * The problem with the alternatives SC.warn(), SC.say() is that
	 * those overwrite each other so in case multiple of them are called,
	 * one can only see the last message.
	 * @param message
	 */
	public static native void log(Object message) /*-{
		alert(message);
	}-*/;

	/**
	 * return the canvas layout element of the form block. 
	 */
	public Layout getLayout() {
		return layout;
	}
	
	/**
	 * return the DynamicForm of the last displayed edit form.
	 * @return
	 */
	public DynamicForm getEditForm() {
		return editForm;
	}

	/**
	 * Returns the dataMap came from the backend. This doesn't get changed.
	 * For finding the changes see the getChanges() method.
	 * @return
	 */
	public LinkedHashMap<String, UIFormFieldValue> getDataMap() {
		return dataMap;
	}

	/**
	 * return a list of fields to show. All of those fields are supposed 
	 * to be delivered by the server in the dataMap.
	 * @return
	 */
	abstract public LinkedList<String> getFieldOrder();

	/**
	 * Return the form Label
	 * @return
	 */
	abstract protected String getFormLabel();
	
	/**
	 * Set the canvas layout element where the view and the
	 * edit form can be displayed.
	 * @param layout
	 */
	public void setLayout(Layout layout) {
		this.layout = layout;
	}

	public Canvas getViewPanel() {
		getData(new AsyncCallback<LinkedHashMap<String,UIFormFieldValue>>() {
			
			@Override
			public void onSuccess(LinkedHashMap<String, UIFormFieldValue> result) {
				dataMap = result;
				layout.addMember(getViewForm());
			}
			
			@Override
			public void onFailure(Throwable caught) {
				SC.warn("Couldn't load " + getFormLabel() + "(" + caught.getMessage() + ")");
			}
		});
		
		layout.setPadding(10);
		return layout;
	}

	/* (non-Javadoc)
	 * @see kiwi.view.dashboard.main.client.KiWiWidget#getWidgetPanel()
	 */
	@Override
	public Canvas getWidgetPanel() {
		return getViewPanel();
	}

}
