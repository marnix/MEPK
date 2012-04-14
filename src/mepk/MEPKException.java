package mepk;

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

	private static final long serialVersionUID = 1L;

}
