package kiwi.service.messages;

import java.util.Map;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

@Scope(ScopeType.STATELESS)
@Name("kiwi.messages")
public class MessagesService {

	@In
	protected Map<String, String> messages;
	
	@Logger
	Log log;
	
	public String getProperty(String s) {
		log.info("message for #0", s);
		return messages.get(s);
	}
	
}
