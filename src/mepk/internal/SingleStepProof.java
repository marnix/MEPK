package mepk.internal;

import java.util.Collections;
import java.util.Set;

import mepk.Justification;
import mepk.ProofStep;
import mepk.Statement;
import mepk.TrustedProof;

/**
 * A single-step proof is essentially a proof step, dressed up as a proof.
 * 
 */
public class SingleStepProof implements TrustedProof.Internal {

	private ProofStep proofStep;

	/**
	 * Create an instance.
	 * 
	 * @param proofStep
	 *            the proof step which contains the essence of this proof.
	 */
	public SingleStepProof(ProofStep proofStep) {
		this.proofStep = proofStep;
	}

	@Override
	public Set<Statement> getGrounding() {
		return proofStep.getGrounding();
	}

	@Override
	public Set<Statement> getGrounded() {
		return Collections.singleton(proofStep.getGrounded1());
	}

	@Override
	public Justification getJustificationFor(Statement statement) {
		return new Justification(proofStep, TrustedProof.Trivial(proofStep.getGrounding()));
	}
}
