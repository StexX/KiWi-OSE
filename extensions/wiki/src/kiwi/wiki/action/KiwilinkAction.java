package kiwi.wiki.action;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Conversational;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Conversation;
import org.jboss.seam.log.Log;

@Name("kiwilinkAction")
@Scope(ScopeType.CONVERSATION)
@Conversational
public class KiwilinkAction implements Serializable {

	private static final long serialVersionUID = 311980733918385036L;
	
	@Logger
	private static Log log;
	
	@Create
	public void begin(){
		Conversation.instance().begin(true, false);
		log.info("init action (cid: #0)", Conversation.instance().getId());
	}
	
	/**
	 * The wikitext the user selected in the editor. Can be empty or 
	 * something like "[[linkTarget|linkLabel]]" 
	 */
	private String selectionHTML;
	
	private String linkLabel;
	
	private String linkTarget;
	
	/**
	 * This flag stores if the form is active instead of the editor UI.
	 * It's actually a workaround to avoid JSF(?) overwriting the 
	 * form fields in the bean with the DOMs values after 
	 * extracting them from the selection and setting by the bean.
	 * TODO Solve it nicer.
	 */
	private Boolean formActive = false;
	
	/**
	 * @return the linkLabel
	 */
	public String getLinkLabel() {
		log.debug("getLinkLabel: '#0' (cid: #1)", linkLabel, Conversation.instance().getId());
		return linkLabel;
	}

	/**
	 * @param linkLabel the linkLabel to set
	 */
	public void setLinkLabel(String linkLabel) {
//		log.info("#0, setting linkLabel to #1", formActive, linkLabel);
		if(!formActive){
			return;
		}
		this.linkLabel = linkLabel;
	}

	/**
	 * @param linkTarget the linkTarget to set
	 */
	public void setLinkTarget(String linkTarget) {
//		log.info("#0, setting linkTarget to #1", formActive, linkLabel);
		if(!formActive){
			return;
		}
		this.linkTarget = linkTarget;
	}

	/**
	 * @return the linkTarget
	 */
	public String getLinkTarget() {
		return linkTarget;
	}

	/**
	 * called by the editor to set the user's selection before 
	 * invoking the link editor.
	 * @param selection
	 */
	public void setSelectionHTML(String selection){
		log.debug("selection is #0 (cid: #1)", selection, Conversation.instance().getId());
		String linkRegex = "\\[\\[(.*)\\]\\]";
		if(selection.matches(linkRegex)){
			String[] linkParts = selection.replaceFirst(linkRegex, "$1").split("\\|");
			if (linkParts.length>1){
				linkLabel=linkParts[1];
			}
			linkTarget=linkParts[0];
		}else{
			this.linkLabel=selection;
			this.linkTarget=selection;
		}
		this.selectionHTML = selection;
	}
	
	/**
	 * @return the selectionHTML
	 */
	public String getSelectionHTML() {
		
		return selectionHTML;
	}

	public static void main(String[] args){
		KiwilinkAction _this = new KiwilinkAction();
		_this.setSelectionHTML("[[ugfzasgfu|sag]]");
		
		System.out.println("Label: " + _this.linkLabel);
		System.out.println("Target: " + _this.linkTarget);
		System.out.println("Orig: " + _this.selectionHTML);
	}
	
	/**
	 * Generates the kiwitext replacement to insert in the editor instead of 
	 * the selection the user made before clicking the kiwilink button.
	 * @return
	 */
	private String generateReplacement(){
		String replacementHTML="";
		if(this.linkLabel!=null && this.linkLabel!=""){
			replacementHTML = "[[" + this.linkTarget + "|" + this.linkLabel + "]] ";
		}
		return replacementHTML;
	}
	
	/**
	 * Called when the user clicks Ok for creating the link in the editor.
	 */
	public void createLink(){
		log.info("Creating Kiwilink, html: " + this.generateReplacement());
		this.formActive=false;
	}
	
	/**
	 * Called when the user clicks Abort creating a link.
	 */
	public void abortLinkCreation(){
		this.formActive=false;
	}
	
	@Begin(join=true)
	public void formActivate(){
		this.formActive=true;
	}
	
	/**
	 * Returns the wikitext to insert instead of the 
	 * editor selection.
	 * @return
	 */
	public String getReplacementHTML(){
		log.debug("getReplacementHTML: '#0' (cid: #1)", generateReplacement(), Conversation.instance().getId());
		return this.generateReplacement();
	}
}
