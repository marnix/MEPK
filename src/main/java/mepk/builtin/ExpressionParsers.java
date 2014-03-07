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

public class ExpressionParsers {

	public static Parser<Expression> variable() {
		return IDENTIFIER.map(new Map<String, Expression>() {
			@Override
			public Expression map(String name) {
				return Var(name);
			}
		});
	}

	private static Parser<String> token() {
		return Scanners.notAmong("() \t\n\r").many1().source();
	}

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
		expressionRef.set(variable().or(application));
		return application;
	}

	public static Parser<Expression> expression() {
		return variable().or(application());
	}

}
