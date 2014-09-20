package mepk.kernel;

import java.util.List;
import java.util.Set;

/**
 * A variable expression. Instances are obtained through
 * {@link Expression#asVar()}, for expressions which have been created through
 * {@link Expression#Var(String)}.
 */
public class Var implements Expression.Internal {

	private final String varName;

	Var(String varName) {
		this.varName = varName;
	}

	/**
	 * Returns the name of this variable.
	 * 
	 * @return the name of this variable
	 */
	public String getVarName() {
		return varName;
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
		result = prime * result + ((varName == null) ? 0 : varName.hashCode());
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
		if (!(obj instanceof Var)) {
			return false;
		}
		Var other = (Var) obj;
		if (varName == null) {
			if (other.varName != null) {
				return false;
			}
		} else if (!varName.equals(other.varName)) {
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
		return (varName.isEmpty() ? "''" : varName);
	}

	@Override
	public Expression substitute(String varName, Expression.Internal replacement, Wrapper wrapper) {
		return wrapper.wrap(this.varName.equals(varName) ? replacement : this);
	}

	@Override
	public Expression expand(Abbreviation abbreviation, List<Expression> accu, Wrapper wrapper) {
		return wrapper.wrap(this);
	}

	@Override
	public void addVarNamesTo(Set<String> result) {
		result.add(varName);
	}
}