package mepk.kernel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * A constant-application expression. Instances are obtained through
 * {@link Expression#asApp()}, for expressions which have been created through
 * {@link Expression#App(String, Expression...)} and related methods.
 */
public class App implements Expression.Internal {

	private final String constName;
	private final Expression[] subexpressions;

	App(String constName, Expression... subexpressions) {
		this.constName = constName;
		this.subexpressions = subexpressions;
	}

	/**
	 * Returns the constant name of this constant-application expression.
	 * 
	 * @return the constant name of this expression
	 */
	public String getConstName() {
		return constName;
	}

	/**
	 * Returns the subexpressions of this constant-application expression.
	 * 
	 * @return the subexpressions of this expression.
	 */
	public List<Expression> getSubexpressions() {
		return Collections.unmodifiableList(Arrays.asList(subexpressions));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(subexpressions);
		result = prime * result + ((constName == null) ? 0 : constName.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof App)) {
			return false;
		}
		App other = (App) obj;
		if (!Arrays.equals(subexpressions, other.subexpressions)) {
			return false;
		}
		if (constName == null) {
			if (other.constName != null) {
				return false;
			}
		} else if (!constName.equals(other.constName)) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append('(');
		result.append(constName.isEmpty() ? "''" : constName);
		for (Expression e : subexpressions) {
			result.append(' ');
			result.append(e.toString());
		}
		result.append(')');
		return result.toString();
	}

	@Override
	public void addVarNamesTo(Set<String> result) {
		for (Expression e : subexpressions) {
			e.addVarNamesTo(result);
		}
	}

	@Override
	public Expression substitute(String varName, Expression.Internal replacement, Wrapper wrapper) {
		List<Expression> es = new ArrayList<Expression>();
		for (Expression e : subexpressions) {
			es.add(e.getInternalExpression().substitute(varName, replacement, wrapper));
		}
		return wrapper.wrap(new App(constName, es.toArray(new Expression[es.size()])));
	}

	@Override
	public Expression expand(Abbreviation abbreviation, List<Expression> accu, Wrapper wrapper) {
		List<Expression> replacements = new ArrayList<>();
		for (Expression e : this.getSubexpressions()) {
			replacements.add(e.expand(abbreviation, accu));
		}
		List<String> normalVarNames = abbreviation.getNormalVarNames();
		if (this.getConstName().equals(abbreviation.getConstName()) && normalVarNames.size() == replacements.size()) {
			Expression thisExpanded = abbreviation.getExpansion();
			for (int i = 0; i < replacements.size(); i++) {
				String v = abbreviation.getNormalVarNames().get(i);
				Expression replacement = replacements.get(i);
				thisExpanded = thisExpanded.substitute(v, replacement);
			}
			return thisExpanded;
		} else {
			return wrapper.wrap(new App(constName, replacements.toArray(new Expression[replacements.size()])));
		}
	}
}