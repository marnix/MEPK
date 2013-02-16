package mepk.builtin.internal;

import java.util.HashSet;
import java.util.Set;

import mepk.builtin.TrustedProof;
import mepk.kernel.Justification;
import mepk.kernel.Proof;
import mepk.kernel.Statement;

/**
 * A 'parallel proof' merges two proofs 'in parallel', so that the new proof
 * just collects everything which the two parts prove.
 */
public class ParProof implements TrustedProof.Internal {

	private Proof proof1;
	private Proof proof2;

	/**
	 * Create an instance from two proofs.
	 * 
	 * @param proof1
	 *            one proof
	 * @param proof2
	 *            another proof
	 */
	public ParProof(Proof proof1, Proof proof2) {
		this.proof1 = proof1;
		this.proof2 = proof2;
	}

	@Override
	public Set<Statement> getGrounding() {
		Set<Statement> result = new HashSet<Statement>();
		result.addAll(proof2.getGrounding());
		result.addAll(proof1.getGrounding());
		return result;
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
		if (proof1.getGrounded().contains(statement)) {
			// if both ti1 and ti2 justify the given statement, we arbitrarily
			// return ti1's justification
			return proof1.getJustificationFor(statement);
		} else {
			assert proof2.getGrounded().contains(statement);
			return proof2.getJustificationFor(statement);
		}
	}
}
