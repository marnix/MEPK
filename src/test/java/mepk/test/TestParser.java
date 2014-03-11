package mepk.test;

import static mepk.builtin.MEPKParsers.*;
import static mepk.kernel.Expression.*;
import static org.junit.Assert.*;
import mepk.builtin.MEPKParseException;

import org.junit.Test;

public class TestParser {

	@Test
	public void testParseIdentifierAsVariable() throws Throwable {
		assertEquals(Var("x"), Expr("x"));
	}

	@Test
	public void testFailParseNonIdentifier() throws Throwable {
		try {
			Expr("0");
			fail();
		} catch (MEPKParseException e) {
		}
	}

	@Test
	public void testParseListExpressionAsApplication() throws Throwable {
		assertEquals(App("0"), Expr(" ( 0)"));
		assertEquals(App(" 0"), Expr("(' 0')"));
		assertEquals(AppV("-", "x"), Expr("(- x)"));
		assertEquals(AppV("-", "x"), Expr("(- 'x')"));
		assertEquals(AppV("-", "x"), Expr("('-' x)"));
		assertEquals(AppV("-", "x"), Expr("('-' 'x')"));
		assertEquals(AppV("0", "b2", "_3", ";'"), Expr("(0 b2 ; this is comment\n_3 ';''')"));
	}
}
