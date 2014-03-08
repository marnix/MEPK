package mepk.builtin;

import static mepk.kernel.Expression.*;
import static org.codehaus.jparsec.Parsers.*;
import static org.codehaus.jparsec.Scanners.*;

import java.util.List;

import mepk.kernel.Expression;

import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.Scanners;
import org.codehaus.jparsec.functors.Map;
import org.codehaus.jparsec.functors.Pair;

/**
 * JParsec-based parsers for expression.
 * <p>
 * Example: {@code ExpressionParsers.expression().parse("(> (+ x 1) 0)")}.
 */
public class ExpressionParsers {

	/**
	 * This constructor should not be used: this class contains only static
	 * methods.
	 */
	private ExpressionParsers() {
	}

	/**
	 * A JParsec parser for an expression consisting of just a variable.
	 * 
	 * @return the parser
	 */
	public static Parser<Expression> variable() {
		return IDENTIFIER.map(new Map<String, Expression>() {
			@Override
			public Expression map(String name) {
				return Var(name);
			}
		});
	}

	/**
	 * A JParsec parser for an application expression.
	 * 
	 * @return the parser
	 */
	public static Parser<Expression> application() {
		final Parser.Reference<Expression> expressionRef = new Parser.Reference<Expression>();
		Parser<List<Expression>> exprList = expressionRef.lazy().sepBy1(WHITESPACES);
		Parser<Pair<String, List<Expression>>> applicationRaw = tuple(token(), WHITESPACES.next(exprList).optional())
				.between(isChar('('), isChar(')'));
		final Parser<Expression> application = applicationRaw.map(new Map<Pair<String, List<Expression>>, Expression>() {
			@Override
			public Expression map(Pair<String, List<Expression>> tuple) {
				return tuple.b == null ? App(tuple.a) : App(tuple.a, tuple.b.toArray(new Expression[tuple.b.size()]));
			}
		});
		// NOTE the duplication with method expression(), which seems
		// unavoidable because of the mutual recursion.
		expressionRef.set(variable().or(application));
		return application;
	}

	/**
	 * A JParsec parser for an expression, i.e., a variable or an application of
	 * a constant to a list of zero or more expressions.
	 * 
	 * @return the parser
	 */
	public static Parser<Expression> expression() {
		return variable().or(application());
	}

	private static Parser<String> token() {
		return Scanners.notAmong("() \t\n\r").many1().source();
	}

}
