package kiwi.transport.client;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TransportSkosConcept implements IsSerializable {
	public long id;
	public String label;
	public boolean hasChildren;
	public boolean isScheme;
	
	public TransportSkosConcept() {}
	
	public TransportSkosConcept(long aId, String aLabel, boolean aHasChildren, boolean aIsScheme)
	{
		super();
		id = aId;
		label = aLabel;
		hasChildren = aHasChildren;
		isScheme = aIsScheme;
	}
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof TransportSkosConcept))
			return false;
		return id == ((TransportSkosConcept) obj).id;
	}
}
