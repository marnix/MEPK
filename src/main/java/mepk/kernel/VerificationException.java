package mepk.kernel;

/**
 * This exception indicates that a {@link Proof} did not {@link Proof#verify()}
 * correctly.
 */
@SuppressWarnings("serial")
public class VerificationException extends MEPKException {

	/**
	 * Create new instance.
	 * 
	 * @param message
	 *            the exception message.
	 */
	public VerificationException(String message) {
		super(message);
	}

}
