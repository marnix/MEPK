package mepk.test;

import static mepk.builtin.MEPKParsers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import mepk.kernel.Abbreviation;
import mepk.kernel.MEPKException;
import mepk.kernel.Statement;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TestAbbreviations {

	private Statement originalStat;

	@Before
	public void setUp() {
		originalStat = Stat("(group-elem x) AND (group-elem y) ==> (group-elem (op x y))");
	}

	@Test
	public void testIncorrectAbbreviation() {
		try {
			new Abbreviation(Expr("(> x (0))"), Expr("(Positive x)"));
		} catch (MEPKException ex) {
			assertTrue(ex.getMessage().contains("cannot be used as abbreviation"));
		}
	}

	@Ignore("TODO: Complete the implementation to make this test case work")
	@Test
	public void test1() {
		Abbreviation a = new Abbreviation(Expr("(op x y)"), Expr("(* x y)"));
		Statement expandedStat = Stat("(group-elem x) AND (group-elem y) ==> (group-elem (* x y))");
		assertEquals(Collections.singleton(expandedStat), originalStat.expand(a));
	}

	@Ignore("TODO: Complete the implementation to make this test case work")
	@Test
	public void test2() {
		Abbreviation a = new Abbreviation(Expr("(group-elem x)"), Expr("(Real x)"), Expr("(> x (0))"));
		List<Statement> expandedStats = Arrays.asList(Stat("(Real x) AND (> x (0)) AND (Real y) AND (> y (0)) ==> (Real (* x y))"),
				Stat("(Real x) AND (> x (0)) AND (Real y) AND (> y (0)) ==> (> (* x y) (0))"));
		assertEquals(new HashSet<>(expandedStats), originalStat.expand(a));
	}

}
