package mepk.builtin;

import static mepk.kernel.Expression.*;

import java.util.List;

import mepk.kernel.Expression;

import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.Parsers;
import org.codehaus.jparsec.Scanners;
import org.codehaus.jparsec.Terminals;
import org.codehaus.jparsec.Token;
import org.codehaus.jparsec.error.ParserException;
import org.codehaus.jparsec.functors.Map;
import org.codehaus.jparsec.functors.Pair;

/**
 * JParsec-based parsers for expression.
 * <p>
 * Example: {@code MEPKParsers.Expr("(> (+ x 1) 0)")}.
 */
public class MEPKParsers {

	private static final Terminals OPERATORS = Terminals.operators("(", ")");

	private static Parser<Token> term(String tokenName) {
		return OPERATORS.token(tokenName);
	}

	private static final Parser<Expression> EXPRESSION;

	static {
		Parser<?> ignored = Parsers.or(Scanners.lineComment(";"), Scanners.WHITESPACES);

		Parser<?> idTokenizer = Parsers.or(Terminals.Identifier.TOKENIZER, Terminals.StringLiteral.SINGLE_QUOTE_TOKENIZER);
		Parser<?> tokenizer = Parsers.or(OPERATORS.tokenizer(), idTokenizer);

		Parser.Reference<Expression> exprRef = new Parser.Reference<>();
		Parser<String> identifier_ = Terminals.Identifier.PARSER.or(Terminals.StringLiteral.PARSER);
		Parser<Expression> application_ = Parsers.pair(identifier_, exprRef.lazy().many()).between(term("("), term(")"))
				.map(new Map<Pair<String, List<Expression>>, Expression>() {
					@Override
					public Expression map(Pair<String, List<Expression>> from) {
						return App(from.a, from.b.toArray(new Expression[from.b.size()]));
					}
				});

		Parser<Expression> variable_ = identifier_.map(
				new Map<String, Expression>() {
					@Override
					public Expression map(String from) {
						return Var(from);
					}
				});
		Parser<Expression> expression_ = application_.or(variable_);
		exprRef.set(expression_);

		EXPRESSION = expression_.from(tokenizer, ignored.skipMany());
	}

	/**
	 * Parse the given expression string (in "GHilbert format") to an
	 * {@link Expression} instance.
	 * 
	 * @param expressionAsString
	 *            the string to parse
	 * @return the parsed expression
	 * @throws MEPKParseException
	 *             if parsing failed
	 */
	public static Expression Expr(String expressionAsString) {
		try {
			return EXPRESSION.parse(expressionAsString);
		} catch (ParserException ex) {
			throw new MEPKParseException(ex);
		}
	}

	/**
	 * This constructor should not be used: this class contains only static
	 * methods.
	 */
	private MEPKParsers() {
		assert false : "unreached";
	}

}
