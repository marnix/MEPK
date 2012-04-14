package mepk.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import mepk.DVRSet;
import mepk.MEPKException;

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
}
