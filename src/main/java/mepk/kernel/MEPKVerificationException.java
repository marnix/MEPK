package mepk.kernel;

/**
 * This exception indicates that a {@link Proof} did not {@link Proof#verify()}
 * correctly.
 */
@SuppressWarnings("serial")
public class MEPKVerificationException extends MEPKException {

	/**
	 * Create new instance.
	 * 
	 * @param message
	 *            the exception message.
	 */
	public MEPKVerificationException(String message) {
		super(message);
	}

}
