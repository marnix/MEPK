package mepk;

import java.util.Set;

/**
 * A proof is a recipe for constructing one set of statements from another using
 * only {@link ProofStep ProofSteps}. Proofs should be values: it should not be
 * possible to modify them after they have been created.
 */
public abstract class Proof {

	/** Create a new proof. */
	protected Proof() {
		// nothing needs to be done here
	}

	/**
	 * Return the set of statements which form the basis of this proof.
	 * 
	 * @return the grounding statements
	 */
	public abstract Set<Statement> getGrounding();

	/**
	 * Return the set of statements constructed by this proof from the
	 * {@link #getGrounding() grounding} statements.
	 * 
	 * @return the grounded statements
	 */
	public abstract Set<Statement> getGrounded();

	// TODO: public abstract Map<String, Abbreviation> getAbbreviations();

	/**
	 * Return a justification for the given statement.
	 * 
	 * @param statement
	 *            a statement which is an element of {@link #getGrounded()}
	 * @return either {@code null}, meaning that no justification is necessary
	 *         because {@code getGrounding().contains(statement)}; or a
	 *         {@link Justification} which gives a
	 *         {@link Justification#getProofStep() proof step} that constructs
	 *         the statement, and a {@link Justification#getProof() proof} that
	 *         constructs all prerequisites of the proof step.
	 */
	public abstract Justification getJustificationFor(Statement statement);

	/**
	 * Verify this proof, by checking its {@link Justification justifications}
	 * and recursively verifying their {@link Justification#getProof() proofs}.
	 * 
	 * @throws VerificationException
	 *             if the verification fails.
	 */
	public final void verify() throws VerificationException {
		// this is the verification algorithm in very rough form,
		// in recursive form, for easier debugging
		// (in practice one doesn't want to blow up the Java stack arbitrarily)
		// TODO: change the verification to a non-recursive implementation
		verifyStatementsAreJustified(this.getGrounded());
	}

	private void verifyStatementsAreJustified(Set<Statement> grounded) throws VerificationException {
		assert this.getGrounded().containsAll(grounded);
		for (Statement s : grounded) {
			Justification justification = getJustificationFor(s);
			if (justification == null) {
				if (!this.getGrounding().contains(s)) {
					throw new VerificationException("no justification for " + s);
				}
			} else {
				Proof ti2 = justification.getProof();
				ProofStep proofStep = justification.getProofStep();
				if (!proofStep.getGrounded().equals(s)) {
					throw new VerificationException("incorrect justification for " + s + ": justifying proof step should conclude "
							+ s + " but did conclude " + proofStep.getGrounded());
				}
				if (!ti2.getGrounded().containsAll(proofStep.getGrounding())) {
					throw new VerificationException("incorrect justification for " + s
							+ ": justifying proof does not prove everything that is required for the proof step");
				}
				if (!this.getGrounding().containsAll(ti2.getGrounding())) {
					throw new VerificationException("incorrect justification for " + s
							+ ": justifying proof uses more assumptions than proof being verified");
				}
				ti2.verifyStatementsAreJustified(proofStep.getGrounding());
			}
		}
	}
}
