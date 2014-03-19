package mepk.test;

import static mepk.builtin.MEPKParsers.*;
import static org.junit.Assert.*;
import mepk.kernel.ProofStep;
import mepk.kernel.Statement;

import org.junit.Test;

public class TestCompose {

	@Test
	public void test() {
		Statement mp1 = Stat("(bool P) AND (bool Q) AND P AND (=> P Q) ==> Q");
		Statement mp2 = Stat("(bool Q) AND (bool R) AND Q AND (=> Q R) ==> R");
		ProofStep actual = ProofStep.Compose(mp2, mp1);
		Statement expected = Stat("(bool Q) AND (bool R) AND (bool P) AND (bool Q) AND P AND (=> P Q) AND (=> Q R) ==> R");
		assertEquals(expected, actual.getGrounded1());
	}

	@Test
	public void test2() {
		Statement mp1 = Stat("DISTINCT (P Q) AND (bool P) AND (bool Q) AND P AND (=> P Q) ==> Q");
		Statement mp2 = Stat("DISTINCT (Q R) AND (bool Q) AND (bool R) AND Q AND (=> Q R) ==> R");
		ProofStep actual = ProofStep.Compose(mp2, mp1);
		Statement expected = Stat("DISTINCT (R Q) AND DISTINCT (P Q) AND"
				+ " (bool Q) AND (bool R) AND (bool P) AND (bool Q) AND P AND (=> P Q) AND (=> Q R) ==> R");
		assertEquals(expected, actual.getGrounded1());
	}
}
