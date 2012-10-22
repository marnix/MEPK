package mepk.test;

import static org.junit.Assert.*;

import java.util.Arrays;

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
		assertEquals(DVRSet.EMPTY, DVRSet.EMPTY.add(DVRSet.EMPTY));
	}

	@Test
	public void test3() {
		try {
			DVRSet.EMPTY.add("x", "x");
			fail();
		} catch (MEPKException e) {
			AssertUtil.assertTrueElseException(e.getMessage().contains("Variable 'x' may not be disjoint with itself"), e);
		}
	}

	@Test
	public void test4() {
		DVRSet d = DVRSet.EMPTY.add("x", "y");
		try {
			d.add("y", "y");
			fail();
		} catch (MEPKException e) {
			AssertUtil.assertTrueElseException(e.getMessage().contains("Variable 'y' may not be disjoint with itself"), e);
		}
	}

	@Test
	public void testEquals() {
		DVRSet d1 = DVRSet.EMPTY.add("x", "y");
		DVRSet d2 = DVRSet.EMPTY.add("x", "y");
		DVRSet d3 = DVRSet.EMPTY.add("y", "x");
		assertTrue(d1.equals(d2));
		assertTrue(d1.equals(d3));
		assertTrue(d2.equals(d1));
		assertTrue(d2.equals(d3));
		assertTrue(d3.equals(d1));
		assertTrue(d3.equals(d2));
	}
}
