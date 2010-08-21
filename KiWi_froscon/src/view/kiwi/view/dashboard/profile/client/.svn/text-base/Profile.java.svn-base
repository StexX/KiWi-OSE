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
package kiwi.view.dashboard.profile.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.fields.FileItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import kiwi.view.dashboard.main.client.UIFormBlock;
import kiwi.view.dashboard.main.client.UIFormFieldValue;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * The profile view and editing UI using smartGWT.
 * @author Szaby Grünwald
 *
 */
public class Profile extends UIFormBlock implements EntryPoint {

	public Profile(){
		super();
		setConfigurable(true);
	}
	
	private static LinkedList<String> fieldOrder;
	static{
		fieldOrder = new LinkedList<String>();
		fieldOrder.add("firstName");
		fieldOrder.add("lastName");
		fieldOrder.add("gender");
		fieldOrder.add("birthday");
        fieldOrder.add("profilePhoto");
	}
	
	@Override
	public LinkedList<String> getFieldOrder() {
		return fieldOrder;
	}
	
	@Override
	protected String getFormLabel(){
		return "User profile";
	}

	@Override
	protected FormItem createFormField(String field, UIFormFieldValue formField) {
		if("gender".equals(field)){
			LinkedHashMap<String, String> genders = new LinkedHashMap<String, String>();
			genders.put("male","Male");
			genders.put("female","Female");
			return createSelectField(field, formField, genders);
        } else if("profilePhoto".equals(field)){
            return imageField(field, formField);
//		} else if(field == "someCheckboxField"){
//			return createCheckboxField(field, formField, null);
		} else {
			return createDefaultFormField(field, formField); 
		}
	}

    @Override
    protected Widget getSpecialViewElement(String field, String label, Object value) {
        if("profilePhoto".equals(field)){
            Image w = new Image();
            w.setUrl((String)value);
            w.addLoadHandler(new LoadHandler(){
                @Override
                public void onLoad(LoadEvent loadEvent) {
                    SC.say("image loaded!");
                }
            });
            return w;
        }
        else return null;
    }

    private FormItem imageField(String field, UIFormFieldValue formField) {
        FileItem res = new FileItem(field);
        res.setTitle(formField.getLabel());
        res.addChangeHandler(new ChangeHandler(){
            @Override
            public void onChange(ChangeEvent changeEvent) {
                SC.say(changeEvent.getValue().toString());
            }
        });
//        res.setImageURLSuffix("*.jpg");
        return res;  //To change body of created methods use File | Settings | File Templates.
    }

    @Override
	protected void getData(AsyncCallback<LinkedHashMap<String, UIFormFieldValue>> asyncCallback) {
		getService().getProfileMap(asyncCallback);		
	}

	@Override
	protected void remoteSave(LinkedHashMap<String, UIFormFieldValue> changeMap,
			AsyncCallback<Void> asyncCallback) {
		getService().saveChanges(changeMap, asyncCallback);		
	}

	/**
	 * Instantiate the ProfileGWTAction backend service
	 * @return
	 */
	private ProfileGWTActionAsync getService() {
		String endpointURL = GWT.getModuleBaseURL() + "../../seam/resource/gwt";
	
		ProfileGWTActionAsync svc = GWT.create(ProfileGWTAction.class);
		((ServiceDefTarget) svc).setServiceEntryPoint(endpointURL);
		return svc;
	}

	@Override
	public void onModuleLoad() {
        String elName = getJSStringVariable("profileElement");
        if(!"".equals(elName)){
            RootPanel.get(elName).add(getWidgetPanel());
        }
	}

}
