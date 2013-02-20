package mepk.kernel;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import mepk.kernel.internal.Substitute;
import mepk.kernel.internal.Weaken;
import mepk.kernel.util.TrivialProof;

/**
 * A proof step is the smallest step in a proof: it shows how to create a new
 * statement from existing ones. Proof steps are values: they cannot be modified
 * after they have been created. It is only possible to create an instance using
 * the static methods in this class.
 */
public final class ProofStep extends Proof {

	/** An internal version of a {@link ProofStep}. */
	public interface Internal {
		/**
		 * Return the set of statements which form the basis of this proof step.
		 * 
		 * @return the grounding statements
		 */
		Set<Statement> getGrounding();

		/**
		 * Return the statement constructed by this proof step.
		 * 
		 * @return the grounded statement
		 */
		Statement getGrounded1();
	}

	/**
	 * Create a substitution proof step, which takes a statement {@code s} and a
	 * substitution, and constructs (grounds) the statement resulting from the
	 * substitution.
	 * 
	 * @param statement
	 *            the grounding statement
	 * @param varName
	 *            the variable name
	 * @param replacement
	 *            the replacement expression
	 * @param typesOfNewVars
	 *            the additional type expressions
	 * @return the created proof step
	 */
	public static ProofStep Substitute(Statement statement, String varName, Expression replacement,
			Map<String, Expression> typesOfNewVars) {
		return new ProofStep(new Substitute(statement, varName, replacement, typesOfNewVars));
	}

	/**
	 * Create a weakening proof step, which takes a statement and adds
	 * hypotheses and DVRs.
	 * 
	 * @param statement
	 *            the grounding statement
	 * @param addedHypotheses
	 *            the added hypotheses
	 * @param addedDVRs
	 *            the added DVRs
	 * @return the created proof step
	 */
	public static ProofStep Weaken(Statement statement, DVRSet addedDVRs, Expression... addedHypotheses) {
		return new ProofStep(new Weaken(statement, addedDVRs, addedHypotheses));
	}

	public static void Compose(Statement x, Statement... y) {
		// TODO: Implement
	}

	private final ProofStep.Internal internalProofStep;

	private ProofStep(ProofStep.Internal internalProofStep) {
		this.internalProofStep = internalProofStep;
	}

	/**
	 * Return the set of statements which form the basis of this proof step.
	 * 
	 * @return the grounding statements
	 */
	@Override
	public Set<Statement> getGrounding() {
		return internalProofStep.getGrounding();
	}

	/**
	 * Return the statement constructed by this proof step.
	 * 
	 * @return the grounded statement
	 */
	public Statement getGrounded1() {
		return internalProofStep.getGrounded1();
	}

	@Override
	public Set<Statement> getGrounded() {
		return Collections.singleton(getGrounded1());
	}

	@Override
	public Justification getJustificationFor(Statement statement) {
		return new Justification(this, new TrivialProof(getGrounding()));
	}

}
