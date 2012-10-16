package mepk.test;

import static mepk.Expression.*;
import static mepk.Statement.*;
import static mepk.test.AssertUtil.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import mepk.DVRSet;
import mepk.Expression;
import mepk.Justification;
import mepk.MEPKException;
import mepk.Proof;
import mepk.ProofStep;
import mepk.Statement;
import mepk.TrustedProof;
import mepk.Types;

import org.junit.Test;

/**
 * 
 */
public class Test1 {

	@Test
	public void test1() {
		Statement s = Stat(Arrays.asList(Expression.Type("P", "bool"), Var("P")), Var("P"));
		ProofStep p = ProofStep.Substitute(s, "P", Var("Q"), Types.EmptyMap());
		Statement expected = Stat(Arrays.asList(Expression.Type("Q", "bool"), Var("Q")), Var("Q"));
		assertEquals(expected, p.getGrounded1());
	}

	@Test
	public void test2() {
		Statement s = Stat(Arrays.asList(Expression.Type("P", "bool"), Var("P")), Var("P"));
		ProofStep p = ProofStep.Substitute(s, "P", AppV("and", "Q", "R"), Types.map("Q", "bool").map("R", "bool"));
		Statement expected = Stat(
				Arrays.asList(Expression.Type("Q", "bool"), Expression.Type("R", "bool"),
						Expression.Type(AppV("and", "Q", "R"), "bool"), AppV("and", "Q", "R")), AppV("and", "Q", "R"));
		assertEquals(expected, p.getGrounded1());
	}

	@Test
	public void test3() {
		try {
			Stat(Arrays.asList(Expression.Type("Q", "bool"), Var("P")), Var("P"));
			fail();
		} catch (MEPKException ex) {
			assertTrueElseException(ex.getMessage().contains("Ill-typed"), ex);
		}
	}

	@Test
	public void test4() throws Throwable {
		Statement sp = Stat(Arrays.asList(Expression.Type("P", "bool"), Var("P")), Var("P"));
		ProofStep p = ProofStep.Substitute(sp, "P", Var("Q"), Types.EmptyMap());
		Proof ti = TrustedProof.From(p);

		assertEquals(Collections.singleton(sp), ti.getGrounding());
		Statement sq = Stat(Arrays.asList(Expression.Type("Q", "bool"), Var("Q")), Var("Q"));
		assertEquals(Collections.singleton(sq), ti.getGrounded());

		ti.verify();
	}

	@Test
	public void test5() throws Throwable {
		Statement sp = Stat(Arrays.asList(Expression.Type("P", "bool"), Var("P")), Var("P"));
		Statement sq = Stat(Arrays.asList(Expression.Type("Q", "bool"), Var("Q")), Var("Q"));
		ProofStep pq = ProofStep.Substitute(sp, "P", Var("Q"), Types.EmptyMap());
		ProofStep qr = ProofStep.Substitute(sq, "Q", Var("R"), Types.EmptyMap());
		Proof ti = TrustedProof.Seq(TrustedProof.From(pq), TrustedProof.From(qr));

		assertEquals(Collections.singleton(sp), ti.getGrounding());
		Statement sr = Stat(Arrays.asList(Expression.Type("R", "bool"), Var("R")), Var("R"));
		assertEquals(new HashSet<Statement>(Arrays.asList(sq, sr)), ti.getGrounded());

		ti.verify();
	}

	@Test
	public void test6() throws Throwable {
		Statement sp = Stat(Arrays.asList(Expression.Type("P", "bool"), Var("P")), Var("P"));
		Statement sq = Stat(Arrays.asList(Expression.Type("Q", "bool"), Var("Q")), Var("Q"));
		Statement sr = Stat(Arrays.asList(Expression.Type("Q", "bool"), Var("Q")), Var("Q"));
		ProofStep pq = ProofStep.Substitute(sp, "P", Var("Q"), Types.EmptyMap());
		ProofStep qr = ProofStep.Substitute(sq, "Q", Var("R"), Types.EmptyMap());
		ProofStep rs = ProofStep.Substitute(sr, "R", Var("S"), Types.EmptyMap());
		Proof ti = TrustedProof.Seq(TrustedProof.Seq(TrustedProof.From(pq), TrustedProof.From(qr)), TrustedProof.From(rs));

		ti.verify();
	}

	@Test
	public void test6b() throws Throwable {
		Statement sp = Stat(Arrays.asList(Expression.Type("P", "bool"), Var("P")), Var("P"));
		Statement sq = Stat(Arrays.asList(Expression.Type("Q", "bool"), Var("Q")), Var("Q"));
		Statement sr = Stat(Arrays.asList(Expression.Type("R", "bool"), Var("R")), Var("R"));
		ProofStep pq = ProofStep.Substitute(sp, "P", Var("Q"), Types.EmptyMap());
		ProofStep qr = ProofStep.Substitute(sq, "Q", Var("R"), Types.EmptyMap());
		ProofStep rs = ProofStep.Substitute(sr, "R", Var("S"), Types.EmptyMap());

		Proof ti = TrustedProof.Seq(TrustedProof.From(pq), TrustedProof.Seq(TrustedProof.From(qr), TrustedProof.From(rs)));
		ti.verify();

		Proof ti2 = TrustedProof.Seq(TrustedProof.Seq(TrustedProof.From(pq), TrustedProof.From(qr)), TrustedProof.From(rs));
		ti2.verify();
	}

	@Test
	public void testWeaken() throws Throwable {
		Statement sp = Stat(Arrays.asList(Expression.Type("P", "bool"), Var("P")), Var("P"));
		Statement spq = Stat(Arrays.asList(Expression.Type("P", "bool"), Var("P"), Expression.Type("Q", "bool"), Var("Q")),
				Var("P"));
		ProofStep w = ProofStep.Weaken(sp, DVRSet.EMPTY, Expression.Type("Q", "bool"), Var("Q"));

		assertEquals(Collections.singleton(sp), w.getGrounding());
		assertEquals(spq, w.getGrounded1());

		Proof ti = TrustedProof.From(w);

		assertEquals(Collections.singleton(sp), ti.getGrounding());
		assertEquals(Collections.singleton(spq), ti.getGrounded());

		ti.verify();
	}

	@SuppressWarnings("unused")
	private void verifyTIStatementsToBasis(Proof ti, Set<Statement> statements, Set<Statement> baseStatements) {
		assertTrue(ti.getGrounded().containsAll(statements));
		for (Statement s : statements) {
			Justification justification = ti.getJustificationFor(s);
			if (justification == null) {
				assertTrue(baseStatements.contains(s));
			} else {
				ProofStep proofStep = justification.getProofStep();
				Proof ti2 = justification.getProof();
				verifyTIStatementsToBasis(ti2, proofStep.getGrounding(), baseStatements);
			}
		}
	}
}
