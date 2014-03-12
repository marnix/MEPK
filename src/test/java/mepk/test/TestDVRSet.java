package mepk.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;

import mepk.kernel.DVRSet;
import mepk.kernel.MEPKException;

import org.junit.Test;

public class TestDVRSet {

	@Test
	public void test1() {
		assertEquals(DVRSet.EMPTY, DVRSet.EMPTY.substitute("x", Arrays.asList("y")));
	}

	@Test
	public void test2() {
		assertEquals(DVRSet.EMPTY, DVRSet.EMPTY.andDistinct(DVRSet.EMPTY));
	}

	@Test
	public void test3() {
		try {
			DVRSet.Distinct("x", "x");
			fail();
		} catch (MEPKException e) {
			AssertUtil.assertTrueElseException(e.getMessage().contains("Variable 'x' may not be disjoint with itself"), e);
		}
	}

	@Test
	public void test4() {
		DVRSet d = DVRSet.Distinct("x", "y");
		try {
			d.andDistinct("y", "y");
			fail();
		} catch (MEPKException e) {
			AssertUtil.assertTrueElseException(e.getMessage().contains("Variable 'y' may not be disjoint with itself"), e);
		}
	}

	@Test
	public void testEquals() {
		DVRSet d1 = DVRSet.Distinct("x", "y");
		DVRSet d2 = DVRSet.Distinct("x", "y");
		DVRSet d3 = DVRSet.Distinct("y", "x");
		assertTrue(d1.equals(d2));
		assertTrue(d1.equals(d3));
		assertTrue(d2.equals(d1));
		assertTrue(d2.equals(d3));
		assertTrue(d3.equals(d1));
		assertTrue(d3.equals(d2));
	}

	@Test
	public void testAddSets() {
		DVRSet d1 = DVRSet.EMPTY;
		DVRSet d2 = d1.andDistinct("x", "y");
		DVRSet d3 = d1.andDistinct("y", "x");
		assertEquals(d2, d1.andDistinct(d2));
		assertEquals(d2, d2.andDistinct(d1));
		assertEquals(d2, d1.andDistinct(d3));
		assertEquals(d2, d3.andDistinct(d1));
		assertEquals(d2, d2.andDistinct(d3));
		assertEquals(d2, d3.andDistinct(d2));
		assertEquals(d3, d1.andDistinct(d3));
		assertEquals(d3, d3.andDistinct(d1));
		assertEquals(d3, d2.andDistinct(d3));
		assertEquals(d3, d3.andDistinct(d2));
	}

	@Test
	public void testAdd() {
		DVRSet xy = DVRSet.Distinct("x", "y");
		DVRSet xz = DVRSet.Distinct("x", "z");
		DVRSet xyxz1 = xy.andDistinct("x", "z");
		DVRSet xyxz2 = xy.andDistinct(xz);
		assertTrue(xyxz1.equals(xyxz2));
		assertTrue(xyxz2.equals(xyxz1));
		assertEquals(DVRSet.Distinct("x", "y", "z"), xyxz1.andDistinct("y", "z"));
	}

	@Test
	public void testUnion() {
		assertEquals(DVRSet.EMPTY, DVRSet.Distinct(Collections.<DVRSet> emptyList()));
		assertEquals(DVRSet.Distinct("x", "y"), DVRSet.Distinct(Arrays.asList(DVRSet.Distinct("y", "x"))));
	}

	@Test
	public void testSubstitute1() {
		DVRSet xy = DVRSet.Distinct("x", "y");
		assertEquals(DVRSet.Distinct("x", "z"), xy.substitute("y", Arrays.asList("z")));
	}

	@Test
	public void testSubstitute2() {
		DVRSet xy = DVRSet.Distinct("x", "y");
		assertEquals(xy, xy.substitute("y", Arrays.asList("y")));
	}

	@Test
	public void testSubstitute3() {
		DVRSet xy = DVRSet.Distinct("x", "y");
		assertEquals(DVRSet.EMPTY, xy.substitute("y", Arrays.<String> asList()));
	}

	@Test
	public void testSubstitute4() {
		DVRSet xy = DVRSet.Distinct("x", "y");
		assertEquals(DVRSet.Distinct("x", "a").andDistinct("x", "b"), xy.substitute("y", Arrays.asList("a", "b")));
	}

	@Test
	public void testSubstitute5() {
		assertEquals(DVRSet.EMPTY, DVRSet.EMPTY.substitute("y", Arrays.asList("a", "b")));
	}

	@Test
	public void testSubstitute6() {
		DVRSet xy = DVRSet.Distinct("x", "y");
		try {
			xy.substitute("y", Arrays.asList("x"));
			fail();
		} catch (MEPKException e) {
			AssertUtil.assertTrueElseException(e.getMessage().contains("Variable 'x' may not be disjoint with itself"), e);
		}
	}
}
