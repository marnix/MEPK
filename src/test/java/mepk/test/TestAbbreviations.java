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
	public void testNonAbbreviation() {
		try {
			new Abbreviation(Expr("(> x (0))"), Expr("(Positive x)"));
		} catch (MEPKException ex) {
			assertTrue(ex.getMessage().contains("cannot be used as abbreviation"));
		}
	}

	@Test
	public void testAbbreviationWithIncorrectConditionVariable() {
		try {
			new Abbreviation(Expr("(Elem x)"), Expr("(Positive x)"), Expr("(Strange z)"));
		} catch (MEPKException ex) {
			assertTrue(ex.getMessage().contains("uses unknown variables [z]"));
		}
	}

	@Test
	public void test0() {
		Abbreviation a = new Abbreviation(Expr("(group-elem x)"), Expr("(Real x)"));
		Statement expandedStat = Stat("(Real x) AND (Real y) ==> (Real (op x y))");
		assertEquals(Collections.singleton(expandedStat), originalStat.expand(a));
	}

	@Test
	public void test1() {
		Abbreviation a = new Abbreviation(Expr("(op x y)"), Expr("(+ y x)"));
		Statement expandedStat = Stat("(group-elem x) AND (group-elem y) ==> (group-elem (+ y x))");
		assertEquals(Collections.singleton(expandedStat), originalStat.expand(a));
	}

	@Ignore("TODO: Complete the implementation to make this test case work")
	@Test
	public void test2() {
		Abbreviation a = new Abbreviation(Expr("(group-elem x)"), Expr("(Real x)"), Expr("(> x (0))"));
		List<Statement> expandedStats = Arrays.asList(
				Stat("(Real x) AND (> x (0)) AND (Real y) AND (> y (0)) ==> (Real (op x y))"),
				Stat("(Real x) AND (> x (0)) AND (Real y) AND (> y (0)) ==> (> (op x y) (0))"));
		assertEquals(new HashSet<>(expandedStats), originalStat.expand(a));
	}

}
