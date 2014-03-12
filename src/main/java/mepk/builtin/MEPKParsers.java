package mepk.builtin;

import static mepk.kernel.Expression.*;

import java.util.Collections;
import java.util.List;

import mepk.kernel.DVRSet;
import mepk.kernel.Expression;
import mepk.kernel.Statement;

import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.Parsers;
import org.codehaus.jparsec.Scanners;
import org.codehaus.jparsec.Terminals;
import org.codehaus.jparsec.Token;
import org.codehaus.jparsec.TokenMap;
import org.codehaus.jparsec.Tokens;
import org.codehaus.jparsec.Tokens.Fragment;
import org.codehaus.jparsec.error.ParserException;
import org.codehaus.jparsec.functors.Map;
import org.codehaus.jparsec.functors.Pair;

/**
 * Parsers for expressions, etc.
 * <p>
 * Example: {@code MEPKParsers.Expr("(> (+ x (1)) (0))")}.
 */
public class MEPKParsers {

	private static final Parser<Fragment> WORD_TOKENIZER = Scanners.notAmong(" ()").many1().source()
			.map(new Map<String, Fragment>() {
				@Override
				public Fragment map(String from) {
					return Tokens.fragment(from, "word");
				}
			});

	private static final Parser<String> WORD_PARSER = Parsers.token(new TokenMap<String>() {
		@Override
		public String map(Token token) {
			Fragment f = (Fragment) token.value();
			return f.tag().equals("word") ? f.text() : null;
		}

		@Override
		public String toString() {
			return "word";
		}
	});

	private static Parser<Token> word(final String value) {
		return Parsers.token(new TokenMap<Token>() {
			@Override
			public Token map(Token token) {
				Fragment f = (Fragment) token.value();
				return f.tag().equals("word") ? (value.equals(f.text()) ? token : null) : null;
			}

			@Override
			public String toString() {
				return value;
			}
		});
	}

	private static final Terminals OPERATORS = Terminals.operators("(", ")");

	private static Parser<Token> term(String tokenName) {
		return OPERATORS.token(tokenName);
	}

	private static final Parser<Expression> EXPRESSION;

	private static final Parser<Statement> STATEMENT;

	static {
		Parser<?> ignored = Parsers.or(Scanners.lineComment(";"), Scanners.WHITESPACES);

		Parser<?> tokenizer = Parsers.or(OPERATORS.tokenizer(), Terminals.StringLiteral.SINGLE_QUOTE_TOKENIZER, WORD_TOKENIZER);

		Parser.Reference<Expression> exprRef = new Parser.Reference<>();
		Parser<String> identifier_ = Terminals.StringLiteral.PARSER.or(WORD_PARSER);
		Parser<Expression> application_ = Parsers.pair(identifier_, exprRef.lazy().many()).between(term("("), term(")"))
				.map(new Map<Pair<String, List<Expression>>, Expression>() {
					@Override
					public Expression map(Pair<String, List<Expression>> from) {
						return App(from.a, from.b.toArray(new Expression[from.b.size()]));
					}
				});

		Parser<Expression> variable_ = identifier_.map(new Map<String, Expression>() {
			@Override
			public Expression map(String from) {
				return Var(from);
			}
		});
		Parser<Expression> expression_ = application_.or(variable_);
		exprRef.set(expression_);

		EXPRESSION = expression_.from(tokenizer, ignored.skipMany());

		Parser<DVRSet> distinct_ = word("DISTINCT").next(
				identifier_.many1().between(term("("), term(")")).map(new Map<List<String>, DVRSet>() {
					@Override
					public DVRSet map(List<String> from) {
						return DVRSet.Distinct(from);
					}
				}));
		Parser<List<DVRSet>> distincts_ = distinct_.followedBy(word("AND")).many();
		Parser<List<Expression>> hypotheses_ = expression_.sepBy(word("AND"));

		Parser<Statement> statement1_ = word("==>").next(expression_).map(new Map<Expression, Statement>() {
			@Override
			public Statement map(Expression from) {
				return Statement.Stat(Collections.<Expression> emptyList(), from);
			}
		});
		Parser<Statement> statement2_ = Parsers.pair(Parsers.pair(distincts_, hypotheses_), word("==>").next(expression_)).map(
				new Map<Pair<Pair<List<DVRSet>, List<Expression>>, Expression>, Statement>() {
					@Override
					public Statement map(Pair<Pair<List<DVRSet>, List<Expression>>, Expression> from) {
						return Statement.Stat(DVRSet.Distinct(from.a.a), from.a.b, from.b);
					}
				});
		Parser<Statement> statement_ = statement1_.or(statement2_);

		STATEMENT = statement_.from(tokenizer, ignored.skipMany());
	}

	/**
	 * Parse the given expression string (in "Ghilbert format") to an
	 * {@link Expression} instance. An expression is of the form
	 * {@code (> (+ x (1)) (0))}
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
	 * Parse the given statement string to a {@link Statement} instance. A
	 * statement is of the form
	 * {@code DISTINCT (x y) AND (Nat x) AND (Real y) AND (> x y) ==> (Real x)}.
	 * 
	 * @param statementAsString
	 *            the string to parse
	 * @return the parsed statement
	 * @throws MEPKParseException
	 *             if parsing failed
	 */
	public static Statement Stat(String statementAsString) {
		try {
			return STATEMENT.parse(statementAsString);
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
