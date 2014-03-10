package mepk.test;

import static mepk.builtin.ExpressionParsers.*;
import static mepk.kernel.Expression.*;
import static org.junit.Assert.*;

import org.codehaus.jparsec.error.ParserException;
import org.junit.Test;

public class TestParser {

	@Test
	public void testParseIdentifierAsVariable() throws Throwable {
		assertEquals(Var("x"), VARIABLE.parse("x"));
		assertEquals(Var("x"), EXPRESSION.parse("x"));
	}

	@Test
	public void testFailParseNonIdentifier() throws Throwable {
		try {
			VARIABLE.parse("0");
			fail();
		} catch (ParserException e) {
		}
		try {
			EXPRESSION.parse("0");
			fail();
		} catch (ParserException e) {
		}
	}

	@Test
	public void testParseListExpressionAsApplication() throws Throwable {
		// TODO: Make (0) legal, instead of having to use ('0')
		assertEquals(App("0"), APPLICATION.parse("('0')"));
		assertEquals(App("0"), EXPRESSION.parse("('0')"));
		assertEquals(App("-", Var("x")), APPLICATION.parse("('-' x)"));
		assertEquals(App("-", Var("x")), EXPRESSION.parse("('-' x)"));
		assertEquals(App("0", Var("b2"), Var("c3"), Var(";'")), EXPRESSION.parse("('0' b2 ; this is comment\nc3 ';''')"));
	}
}
