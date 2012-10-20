package mepk.kernel;

/**
 * A justification gives a {@link ProofStep} that constructs a statement, and a
 * {@link Proof} that constructs all prerequisites of the proof step.
 */
public class Justification {

	private ProofStep proofStep;
	private Proof proof;

	/**
	 * Create a new justification.
	 * 
	 * @param proofStep
	 *            the proof step
	 * @param proof
	 *            the proof
	 */
	public Justification(ProofStep proofStep, Proof proof) {
		this.proofStep = proofStep;
		this.proof = proof;
		assert proof.getGrounded().containsAll(proofStep.getGrounding());
	}

	/**
	 * Return the proof step.
	 * 
	 * @return the proof step
	 */
	public ProofStep getProofStep() {
		return proofStep;
	}

	/**
	 * Return the proof.
	 * 
	 * @return the proof
	 */
	public Proof getProof() {
		return proof;
	}

}
