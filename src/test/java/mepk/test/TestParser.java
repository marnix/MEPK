package mepk.test;

import static mepk.builtin.MEPKParsers.*;
import static mepk.kernel.Expression.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class TestParser {

	@Test
	public void testParseIdentifierAsVariable() throws Throwable {
		assertEquals(Var("x"), Expr("x"));
	}

	@Test
	public void testParseListExpressionAsApplication() throws Throwable {
		assertEquals(Var("!"), Expr("!"));
		assertEquals(App("z"), Expr("(z)"));
		assertEquals(App("0"), Expr(" ( 0)"));
		assertEquals(App(" 0"), Expr("(' 0')"));
		assertEquals(AppV("-", "x"), Expr("(- x)"));
		assertEquals(AppV("-", "x"), Expr("(- 'x')"));
		assertEquals(AppV("-", "x"), Expr("('-' x)"));
		assertEquals(AppV("-", "x"), Expr("('-' 'x')"));
		assertEquals(AppV("Real", "x"), Expr("(Real x)"));
		assertEquals(AppV("0", "b2", "_3", ";'"), Expr("(0 b2 ; this is comment\n_3 ';''')"));
	}
}
