package kiwi.exception;

public class RegisterException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8670379009777836957L;

	/**
	 * 
	 */
	public RegisterException() {
	}

	/**
	 * @param message
	 */
	public RegisterException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public RegisterException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public RegisterException(String message, Throwable cause) {
		super(message, cause);
	}

}
