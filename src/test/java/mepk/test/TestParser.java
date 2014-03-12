package mepk.test;

import static mepk.builtin.MEPKParsers.*;
import static mepk.kernel.DVRSet.*;
import static mepk.kernel.Expression.*;
import static mepk.kernel.Statement.*;
import static org.junit.Assert.*;

import java.util.Arrays;

import mepk.kernel.Expression;

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

	@Test
	public void testParseStatement() throws Throwable {
		assertEquals(Stat(Arrays.<Expression> asList(), Expr("(true)")), Stat("==> (true)"));
		// TODO: Support DVR without any hypotheses:
		// assertEquals(Stat(Distinct("y", "x"), Arrays.<Expression> asList(),
		// Expr("(true)")), Stat("DISTINCT (x y) ==> (true)"));
		assertEquals(Stat(Arrays.asList(Expr("(false)")), Expr("(true)")), Stat("(false) ==> (true)"));
		assertEquals(Stat(Distinct("y", "x"), Arrays.asList(Expr("(Nat x)")), Expr("(Real x)")),
				Stat("DISTINCT (x y) AND (Nat x) ==> (Real x)"));

		Stat("DISTINCT (x y) AND (Nat x) AND (Real y) AND (> x y) ==> (Real x)");
	}
}
