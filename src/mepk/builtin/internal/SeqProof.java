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

	private Proof ti1;
	private Proof ti2;

	/**
	 * Create an instance from two proofs.
	 * 
	 * @param ti1
	 *            the first proof
	 * @param ti2
	 *            the second proof
	 */
	public SeqProof(Proof ti1, Proof ti2) {

		Set<Statement> cnob = new HashSet<Statement>();
		cnob.addAll(ti2.getGrounding());
		cnob.removeAll(ti1.getGrounded());

		this.ti1 = TrustedProof.Par(ti1, TrustedProof.Trivial(cnob));
		this.ti2 = TrustedProof.Par(ti2, TrustedProof.Trivial(ti1.getGrounded()));

		assert this.ti1.getGrounded().equals(this.ti2.getGrounding());
	}

	@Override
	public Set<Statement> getGrounding() {
		return ti1.getGrounding();
	}

	@Override
	public Set<Statement> getGrounded() {
		Set<Statement> result = new HashSet<Statement>();
		result.addAll(ti1.getGrounded());
		result.addAll(ti2.getGrounded());
		return result;
	}

	@Override
	public Justification getJustificationFor(Statement statement) {
		Justification ti2Justification = ti2.getJustificationFor(statement);
		if (ti2Justification != null) {
			return new Justification(ti2Justification.getProofStep(), TrustedProof.Seq(ti1, ti2Justification.getProof()));
		} else {
			return ti1.getJustificationFor(statement);
		}
	}
}
