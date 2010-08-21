/**
 * 
 */
package kiwi.action.vmt;

/**
 * @author Rolf Sint
 *
 */
public class LanguageNotDefinedExeption extends NullPointerException{
	  public LanguageNotDefinedExeption()
	  {
		  super("Language is not defined error, language is null");
	  }

}
