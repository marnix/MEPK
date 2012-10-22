package mepk.builtin.internal;

import java.util.HashSet;
import java.util.Set;

import mepk.builtin.TrustedProof;
import mepk.kernel.Justification;
import mepk.kernel.Proof;
import mepk.kernel.Statement;

/**
 * A 'sequential proof' merges two proofs 'in sequence', where the second proof
 * builds on the second one.
 */
public class SeqProof implements TrustedProof.Internal {

	private Proof proof1;
	private Proof proof2;

	/**
	 * Create an instance from two proofs.
	 * 
	 * @param proof1
	 *            the first proof
	 * @param proof2
	 *            the second proof
	 */
	public SeqProof(Proof proof1, Proof proof2) {

		Set<Statement> proof2GroundingWithoutProof1 = new HashSet<Statement>();
		proof2GroundingWithoutProof1.addAll(proof2.getGrounding());
		proof2GroundingWithoutProof1.removeAll(proof1.getGrounded());

		this.proof1 = TrustedProof.Par(proof1, TrustedProof.Trivial(proof2GroundingWithoutProof1));
		this.proof2 = TrustedProof.Par(proof2, TrustedProof.Trivial(proof1.getGrounded()));

		assert this.proof1.getGrounded().equals(this.proof2.getGrounding());
	}

	@Override
	public Set<Statement> getGrounding() {
		return proof1.getGrounding();
	}

	@Override
	public Set<Statement> getGrounded() {
		Set<Statement> result = new HashSet<Statement>();
		result.addAll(proof1.getGrounded());
		result.addAll(proof2.getGrounded());
		return result;
	}

	@Override
	public Justification getJustificationFor(Statement statement) {
		Justification ti2Justification = proof2.getJustificationFor(statement);
		if (ti2Justification != null) {
			return new Justification(ti2Justification.getProofStep(), TrustedProof.Seq(proof1, ti2Justification.getProof()));
		} else {
			return proof1.getJustificationFor(statement);
		}
	}
}
