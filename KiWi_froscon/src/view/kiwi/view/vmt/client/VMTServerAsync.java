package kiwi.view.vmt.client;

import java.util.List;
import kiwi.transport.client.TransportSkosConcept;
import kiwi.transport.client.TransportSkosDetails;
import kiwi.transport.client.TransportTag;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface VMTServerAsync {
	void getChildConcepts(long aParent, AsyncCallback<List<TransportSkosConcept>> callback);
	void convertToConcept(long aTag, long aParent, AsyncCallback<Boolean> callback);
	void moveConcept(long aConcept, long aOldParent, long aParent, AsyncCallback<Boolean> callback);
	void copyConcept(long aConcept, long aParent, AsyncCallback<Boolean> callback);
	void setRelated(long aConcept1, long aConcept2, AsyncCallback<Boolean> callback);
	void unsetRelated(long aConcept, List<Long> aConcepts, AsyncCallback<Boolean> callback);
	void getDetails(long aConcept, AsyncCallback<TransportSkosDetails> callback);
	void saveDetails(TransportSkosDetails aConcept, AsyncCallback<Boolean> callback);
	void getFreeTags(AsyncCallback<List<TransportTag>> callback);
	void newConceptScheme(String aTitle, AsyncCallback<Long> callback);
	void newConcept(long aParent, String aTitle, AsyncCallback<Long> callback);
	void removeConcepts(List<String> aConcepts, AsyncCallback<Boolean> callback);
	void setSame(List<Long> aConcepts, AsyncCallback<Boolean> callback);
	void mergeInto(long aConcept, List<Long> aConcepts, AsyncCallback<Boolean> callback);
}
