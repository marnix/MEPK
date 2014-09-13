package mepk.test;

import java.util.Set;

import org.junit.Assert;

public class AssertUtil {

	public static void assertTrueElseException(boolean condition, Error ex) {
		if (!condition) {
			throw new AssertionError(ex);
		}
	}

	public static void assertTrueElseException(boolean condition, RuntimeException ex) {
		if (!condition) {
			throw new AssertionError(ex);
		}
	}

	public static <T> void assertSubset(Set<T> a, Set<T> b) {
		Assert.assertTrue(b.containsAll(a));
	}

}
