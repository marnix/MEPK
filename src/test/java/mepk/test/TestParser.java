package mepk.test;

import static mepk.builtin.ExpressionParsers.*;
import static mepk.kernel.Expression.*;
import static org.junit.Assert.*;

import org.codehaus.jparsec.error.ParserException;
import org.junit.Test;

public class TestParser {

	@Test
	public void testParseIdentifierAsVariable() throws Throwable {
		assertEquals(Var("x"), variable().parse("x"));
		assertEquals(Var("x"), expression().parse("x"));
	}

	@Test
	public void testFailParseNonIdentifier() throws Throwable {
		try {
			variable().parse("0");
			fail();
		} catch (ParserException e) {
		}
		try {
			expression().parse("0");
			fail();
		} catch (ParserException e) {
		}
	}

	@Test
	public void testParseListExpressionAsApplication() throws Throwable {
		assertEquals(App("0"), application().parse("(0)"));
		assertEquals(App("0"), expression().parse("(0)"));
		assertEquals(App("-", Var("x")), application().parse("(- x)"));
		assertEquals(App("-", Var("x")), expression().parse("(- x)"));
	}
}
