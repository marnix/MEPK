package mepk.builtin;

import java.util.Set;

import mepk.kernel.Expression;
import mepk.kernel.MEPKException;
import mepk.kernel.Statement;

/**
 * This exception indicates that a String could not be parsed to an
 * {@link Expression}, a {@link Statement}, or a {@link Set} of Statements.
 */
@SuppressWarnings("serial")
public class MEPKParseException extends MEPKException {

	/**
	 * Create new instance.
	 * 
	 * @param cause
	 *            the exception cause.
	 */
	public MEPKParseException(Throwable cause) {
		super(cause);
	}

}
