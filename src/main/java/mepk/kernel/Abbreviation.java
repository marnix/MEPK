package mepk.kernel;

/**
 * An abbreviation.
 */
public final class Abbreviation {

	/**
	 * Create a new abbreviation.
	 * 
	 * @param abbreviation
	 *            the abbreviating expression, must be an {@link App} of
	 *            {@link Var}s
	 * @param expansion
	 *            the expression after expansion, may use more or less
	 *            {@link Var}s than the abbreviation
	 * @param conditions
	 *            a (possibly empty) list of conditions under which the
	 *            abbreviation applies, may only contain {@link Var}s that are
	 *            in either the abbreviation or the expansion
	 */
	public Abbreviation(Expression abbreviation, Expression expansion, Expression... conditions) {
		if (!abbreviation.isSimpleApp()) {
			throw new MEPKException("error creating abbreviation: " + abbreviation + " cannot be used as abbreviation");
		}
		// TODO: check that the conditions don't add new variables
	}
}
