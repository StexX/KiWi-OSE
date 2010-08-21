package kiwi.transport.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * This contains a map of the language details with the locale as key plus
 * a list of all the related concepts.
 */
public class TransportSkosDetails implements IsSerializable {
	public static class TransportLanguageDetails implements IsSerializable {
		public String prefLabel;
		public List<String> altLabels;
		public List<String> hiddenLabels;
		public String definition;
		
		public TransportLanguageDetails() {}
		public TransportLanguageDetails(boolean init)
		{
			prefLabel = "";
			definition = "";
			altLabels = new ArrayList<String>();
			hiddenLabels = new ArrayList<String>();
		}
	}
	
	public long id;
	public String label;
	public Map<String, TransportLanguageDetails> languages;
	public List<TransportSkosConcept> related;
	
	public TransportSkosDetails() {}
	public TransportSkosDetails(long aId, String aLabel)
	{
		id = aId;
		label = aLabel;
		languages = new HashMap<String, TransportLanguageDetails>();
	}
}
