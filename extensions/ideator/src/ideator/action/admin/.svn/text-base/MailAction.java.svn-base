package ideator.action.admin;

import java.util.HashSet;
import java.util.Set;

import kiwi.api.user.MailService;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;


@Name("ideator.mailAction")
@Scope(ScopeType.CONVERSATION)
//@Transactional
public class MailAction {

	@In(value = "mailService", create = true)
	private MailService mailService;
	
	@Logger
	private Log log;
	
	// sent with password
	public void send(String receiver, String name, String username, String ideaname, String ideaAuthor) {
		String subject = "Access to Ideator";
		String content = "Dear "+name+". You were added as a coauthor of the idea "+ideaname+" created by "+ideaAuthor;
		Set<String> s = new HashSet<String>();
		s.add(receiver);
		
		log.info("Sending mail "+ content);
		log.info("to "+ username);
		
		mailService.sendMail(s, subject, content);
	}
	
	// sent with password
	public void send(String receiver, String name, String username, String ideaname, String pwd, String ideaAuthor) {
		String subject = "Access to Ideator";
		String content = "Dear "+name;
		content = content +" You were added as a coauthor of the idea "+ideaname+" created by" + ideaAuthor;
		content = content + " You have Access to the Ideator idea management tool.";
		content = content + " Yoour username is " + username;
		content = content + " Your password is " + pwd;
		Set<String> s = new HashSet<String>();
		s.add(receiver);
		
		log.info("Sending mail "+ content);
		log.info("to "+ username);
		
		mailService.sendMail(s, subject, content);
	}
}