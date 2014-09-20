package mepk.kernel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An abbreviation.
 */
public final class Abbreviation {

	private final Expression abbreviation;
	private final Expression expansion;
	private final Expression[] conditions;

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

		Set<String> varsAllowedInConditions = new HashSet<>();
		varsAllowedInConditions.addAll(abbreviation.getVarNames());
		varsAllowedInConditions.addAll(expansion.getVarNames());
		for (Expression condition : conditions) {
			Set<String> v = new HashSet<>(condition.getVarNames());
			v.removeAll(varsAllowedInConditions);
			if (!v.isEmpty()) {
				throw new MEPKException("error creating abbreviation: condition " + condition + " uses unknown variables " + v);
			}
		}

		// TODO: check that every 'floating' (expansion-only) variable has a
		// condition to type it

		this.abbreviation = abbreviation;
		this.expansion = expansion;
		this.conditions = conditions;
	}

	// TODO: Add equals, hashCode, toString for Abbreviation

	/**
	 * Returns the name of this abbreviation.
	 * 
	 * @return the name
	 */
	public String getConstName() {
		return abbreviation.asApp().getConstName();
	}

	/**
	 * Returns the 'normal' variable names used in this abbreviation, so
	 * excluding the 'floating' variable names that are only used in the
	 * expansion (TODO: What terminology does Ghilbert use here?).
	 * 
	 * @return the names, in order
	 */
	public List<String> getNormalVarNames() {
		List<String> result = new ArrayList<>();
		for (Expression subExpression : abbreviation.asApp().getSubexpressions()) {
			result.add(subExpression.asVar().getVarName());
		}
		return result;
	}

	/**
	 * Returns the expansion of the abbreviation.
	 * 
	 * @return the expansion
	 */
	public Expression getExpansion() {
		return expansion;
	}
}
