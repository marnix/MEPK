package mepk.kernel;

import java.util.Map;
import java.util.Set;

import mepk.kernel.util.ExpandedAbbreviationsProof;

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

	/**
	 * Return the abbreviations used by this proof.
	 * 
	 * @return the abbreviations
	 */
	public abstract Map<String, Abbreviation> getAbbreviations();

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

	@Override
	public final int hashCode() {
		return getGrounding().hashCode() ^ getGrounded().hashCode();
	}

	@Override
	public final boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (!(other instanceof Proof)) {
			return false;
		}
		Proof that = (Proof) other;
		return this.getGrounding().equals(that.getGrounding()) && this.getGrounded().equals(that.getGrounded());
		// TODO: add abbreviations
	}

	/**
	 * Verify this proof, by checking its {@link Justification justifications}
	 * and recursively verifying their {@link Justification#getProof() proofs}.
	 * 
	 * @throws MEPKVerificationException
	 *             if the verification fails.
	 */
	public final void verify() throws MEPKVerificationException {
		// This is the verification algorithm, in recursive form, for easier
		// debugging. (One wouldn't want to blow up the Java stack arbitrarily,
		// so in the future we may want to change the verification to a
		// non-recursive implementation. However, it doesn't seem likely in
		// practice that we will be hitting JVM limits any time soon.)
		new ExpandedAbbreviationsProof(this).verifyStatementsAreJustified(this.getGrounded());
	}

	protected final void verifyStatementsAreJustified(Set<Statement> grounded) throws MEPKVerificationException {
		if (!this.getAbbreviations().isEmpty()) {
			throw new MEPKException("illegal use of verification algorithm: did not expect abbreviations "
					+ this.getAbbreviations());
		}
		assert this.getGrounded().containsAll(grounded);
		for (Statement s : grounded) {
			Justification justification = getJustificationFor(s);
			if (justification == null) {
				if (!this.getGrounding().contains(s)) {
					throw new MEPKVerificationException("no justification for " + s);
				}
			} else {
				Proof subproof = justification.getProof();
				ProofStep proofStep = justification.getProofStep();
				if (!proofStep.getGrounded1().equals(s)) {
					throw new MEPKVerificationException("incorrect justification for " + s
							+ ": justifying proof step should conclude " + s + " but did conclude " + proofStep.getGrounded1());
				}
				if (!subproof.getGrounded().containsAll(proofStep.getGrounding())) {
					throw new MEPKVerificationException("incorrect justification for " + s
							+ ": justifying proof does not prove everything that is required for the proof step");
				}
				if (!this.getGrounding().containsAll(subproof.getGrounding())) {
					throw new MEPKVerificationException("incorrect justification for " + s
							+ ": justifying proof uses more assumptions than proof being verified");
				}
				subproof.verifyStatementsAreJustified(proofStep.getGrounding());
			}
		}
	}
}
