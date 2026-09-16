package mepk.kernel;

/**
 * The common base class for all MEPK-related exceptions.
 */
public class MEPKException extends RuntimeException {

	/**
	 * Create an new instance.
	 * 
	 * @param message
	 *            the exception message
	 */
	public MEPKException(String message) {
		super(message);
	}

	/**
	 * Create a new instance.
	 * 
	 * @param cause
	 *            the exception cause
	 */
	public MEPKException(Throwable cause) {
		super(cause);
	}

	private static final long serialVersionUID = 1L;

}
