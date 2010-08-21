/*
	Copyright © Rolf Sint, Michael Schneider, Christian Osterrieder, 2010
 */

package artaround.action.artwork.techniqueTree;



import kiwi.commons.treeChosser.TreeChooserAction;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;

/**
 * @author Rolf Sint
 * @version 0.7
 * @since 0.7
 *
 */
@Name("techniqueAction")
@Scope(ScopeType.CONVERSATION)
//@Transactional
public class TechniqueAction extends TreeChooserAction{
	
	@Override
	public String getRoot() {
		return "ArtAround Thesaurus";	
	}
	
	public String getSelectToRemoveMsg(){
		return "artaround.error.technique_select_to_remove";
	}
	
	public String getSelectToAddMsg(){
		return "artaround.error.technique_select_to_add_msg";
	}
	
	public String getNotValidMsg(){
		return "artaround.error.technique_not_valid";
	}
	
	public String getAlreadySelectedMsg(){
		return "artaround.error.technique_already_selected";
	}
		
}
