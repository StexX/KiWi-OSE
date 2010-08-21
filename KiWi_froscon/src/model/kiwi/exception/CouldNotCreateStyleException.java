package kiwi.exception;

/**
 * Will be thrown if the style for textcontent changes cannot be
 * created.
 *  
 * @author Stephanie Stroka
 * 			(stephanie.stroka@salzburgresearch.at)
 *
 */
public class CouldNotCreateStyleException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2776766174587064558L;

	public CouldNotCreateStyleException() {
		super();
	}
	
	public CouldNotCreateStyleException(String msg) {
		super(msg);
	}
}
