package mepk.internal;

import java.util.HashSet;
import java.util.Set;

import mepk.Justification;
import mepk.Proof;
import mepk.Statement;
import mepk.TrustedProof;

/**
 * A 'parallel proof' merges two proofs 'in parallel', so that the new proof
 * just collects everything which the two parts prove.
 */
public class ParProof implements TrustedProof.Internal {

	private Proof ti1;
	private Proof ti2;

	/**
	 * Create an instance from two proofs.
	 * 
	 * @param ti1
	 *            one proof
	 * @param ti2
	 *            another proof
	 */
	public ParProof(Proof ti1, Proof ti2) {
		this.ti1 = ti1;
		this.ti2 = ti2;
	}

	@Override
	public Set<Statement> getGrounding() {
		Set<Statement> result = new HashSet<Statement>();
		result.addAll(ti2.getGrounding());
		result.addAll(ti1.getGrounding());
		return result;
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
		if (ti1.getGrounded().contains(statement)) {
			// if both ti1 and ti2 justify the given statement, we arbitrarily
			// return ti1's justification
			return ti1.getJustificationFor(statement);
		} else {
			assert ti2.getGrounded().contains(statement);
			return ti2.getJustificationFor(statement);
		}
	}
}
