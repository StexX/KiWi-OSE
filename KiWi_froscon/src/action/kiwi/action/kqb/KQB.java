package kiwi.action.kqb;

/**
 * interface for the action that allows the KWQL Query Builder 
 * to save and load queries
 * 
 * @author Andreas Hartl
 */


import org.jboss.seam.annotations.remoting.WebRemote;
import javax.ejb.Local;

@Local
public interface KQB
{

	@WebRemote
	public String SaveQuery (String name, String value, boolean overwrite);
	@WebRemote
    public String ShowQueries(String dummy);
	@WebRemote
	public String DeleteQuery (String name);

}
