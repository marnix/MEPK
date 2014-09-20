package mepk.kernel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mepk.kernel.Expression.Internal.Wrapper;

/**
 * This class represents an expression, i.e., either a variable, or a constant
 * applied to a list of subexpressions. Expressions are values: they cannot be
 * modified after they have been created. They are {@link #equals(Object) equal}
 * if (and only if) they have the same structure. It is only possible to create
 * an instance using the static methods in this class.
 */
public final class Expression {

	/**
	 * An internal version of an {@link Expression}. Instances are unmodifiable
	 * values. They are {@link #equals(Object) equal} if (and only if) they have
	 * the same structure.
	 */
	public interface Internal {

		/**
		 * An object that can wrap an {@link Internal Expression.Internal} up as
		 * an {@link Expression}.
		 */
		public interface Wrapper {
			/**
			 * Wrap the given internal expression.
			 * 
			 * @param internalExpression
			 *            the internal expression to be wrapped
			 * @return the resulting expression
			 */
			public Expression wrap(Internal internalExpression);
		}

		@Override
		public int hashCode();

		@Override
		public boolean equals(Object obj);

		/**
		 * Create a new internal expression by replacing a variable by an
		 * internal expression.
		 * 
		 * @param varName
		 *            the variable to replace
		 * @param replacement
		 *            the replacement expression
		 * @param wrapper
		 *            expression wrapper
		 * @return the new expression
		 */
		public Expression substitute(String varName, Expression.Internal replacement, Wrapper wrapper);

		/**
		 * Create a new (TODO: internal?) expression by expanding all instances
		 * of the given abbreviation, and collect all conditions in accu.
		 * 
		 * @param abbreviation
		 *            the abbreviation to expand
		 * @param accu
		 *            the accumulator for the (expanded) conditions
		 * @param wrapper
		 *            expression wrapper
		 * @return the new expression
		 */
		public Expression expand(Abbreviation abbreviation, List<Expression> accu, Wrapper wrapper);

		/**
		 * Find all variable names in this expression, and add them to the given
		 * set.
		 * 
		 * @param result
		 *            the accumulating set
		 */
		public void addVarNamesTo(Set<String> result);
	}

	private static final Wrapper WRAPPER = new Wrapper() {
		@Override
		public Expression wrap(Internal internalExpression) {
			return new Expression(internalExpression);
		}
	};

	/**
	 * Create a variable expression.
	 * 
	 * @param name
	 *            the variable name
	 * @return the created expression
	 */
	public static Expression Var(String name) {
		if (name == null) {
			throw new MEPKException("Variable name may not be 'null'");
		}
		return new Expression(new Var(name));
	}

	/**
	 * Create an application of a constant to a list of subexpressions.
	 * 
	 * @param constantName
	 *            the constant name
	 * @param subexpressions
	 *            the subexpressions
	 * @return the created expression
	 */
	public static Expression App(String constantName, Expression... subexpressions) {
		if (constantName == null) {
			throw new MEPKException("Constant name may not be 'null'");
		}
		return new Expression(new App(constantName, subexpressions));
	}

	/**
	 * Create an application of a constant to a list of variable names.
	 * 
	 * @param constantName
	 *            the constant name
	 * @param variableNames
	 *            the variable names
	 * @return the created expression
	 */
	public static Expression AppV(String constantName, String... variableNames) {
		List<Expression> bb = new ArrayList<Expression>();
		for (String c : variableNames) {
			bb.add(Var(c));
		}
		return App(constantName, bb.toArray(new Expression[bb.size()]));
	}

	/**
	 * Create a new expression saying 'this variable has that type'.
	 * 
	 * @param varName
	 *            the expression
	 * @param typeName
	 *            the type name
	 * @return the type expression
	 */
	public static Expression Type(String varName, String typeName) {
		return Type(Var(varName), typeName);
	}

	/**
	 * Create a new expression saying 'this expression has that type'.
	 * 
	 * @param expr
	 *            the expression
	 * @param typeName
	 *            the type name
	 * @return the type expression
	 */
	public static Expression Type(Expression expr, String typeName) {
		return App(typeName, new Expression[] { expr });
	}

	private final Expression.Internal internalExpression;

	private Expression(Expression.Internal internalExpression) {
		this.internalExpression = internalExpression;
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
		result = prime * result + ((internalExpression == null) ? 0 : internalExpression.hashCode());
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
		if (!(obj instanceof Expression)) {
			return false;
		}
		Expression other = (Expression) obj;
		if (internalExpression == null) {
			if (other.internalExpression != null) {
				return false;
			}
		} else if (!internalExpression.equals(other.internalExpression)) {
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
		return internalExpression.toString();
	}

	public Expression.Internal getInternalExpression() {
		return internalExpression;
	}

	/**
	 * Return all variables in this expression.
	 * 
	 * @return the variables
	 */
	public Set<String> getVarNames() {
		Set<String> result = new HashSet<String>();
		addVarNamesTo(result);
		return result;
	}

	/**
	 * Cast this expression to a variable expression.
	 * 
	 * @return this expression converted to a {@link Var}, or {@code null} if it
	 *         is not a variable expression.
	 */
	public Var asVar() {
		return (internalExpression instanceof Var ? (Var) internalExpression : null);
	}

	/**
	 * Cast this expression to a constant-application expression.
	 * 
	 * @return this expression converted to a {@link App}, or {@code null} if it
	 *         is not a constant-application expression.
	 */
	public App asApp() {
		return (internalExpression instanceof App ? (App) internalExpression : null);
	}

	/**
	 * Create a new expression by replacing a variable by an expression.
	 * 
	 * @param varName
	 *            the variable to replace
	 * @param replacement
	 *            the replacement expression
	 * @return the new expression
	 */
	public Expression substitute(String varName, Expression replacement) {
		return internalExpression.substitute(varName, replacement.internalExpression, WRAPPER);
	}

	/**
	 * Find all variable names in this expression, and add them to the given
	 * set.
	 * 
	 * @param result
	 *            the accumulating set
	 */
	public void addVarNamesTo(Set<String> result) {
		internalExpression.addVarNamesTo(result);
	}

	/**
	 * Is this a simple App expression, i.e., one that contains no nested App
	 * expressions?
	 * 
	 * @return true or false
	 */
	public boolean isSimpleApp() {
		App a = this.asApp();
		if (a == null)
			return false;
		for (Expression sub : a.getSubexpressions()) {
			if (sub.asVar() == null)
				return false;
		}
		return true;
	}

	/**
	 * Create a new expression by expanding all instances of the given
	 * abbreviation, and collect all conditions in accu.
	 * 
	 * @param abbreviation
	 *            the abbreviation to expand
	 * @param accu
	 *            the accumulator for the (expanded) conditions
	 * 
	 * @return the new expression
	 */
	public Expression expand(Abbreviation abbreviation, List<Expression> accu) {
		return internalExpression.expand(abbreviation, accu, WRAPPER);
	}
}
