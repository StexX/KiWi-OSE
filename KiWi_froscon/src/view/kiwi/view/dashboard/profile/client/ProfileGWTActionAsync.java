package kiwi.view.dashboard.profile.client;

import java.util.LinkedHashMap;

import kiwi.view.dashboard.main.client.UIFormFieldValue;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ProfileGWTActionAsync {

	void saveChanges(LinkedHashMap<String, UIFormFieldValue> changeMap, AsyncCallback<Void> callback);
	void getProfileMap(AsyncCallback<LinkedHashMap<String, UIFormFieldValue>> callback);

}
