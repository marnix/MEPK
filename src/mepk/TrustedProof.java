package mepk;

import java.util.Set;

import mepk.internal.ParProof;
import mepk.internal.SeqProof;
import mepk.internal.TrivialProof;

/**
 * A trusted proof is a {@link Proof} which is part of the trusted proof kernel.
 * Trusted proofs are values: they cannot be modified after they have been
 * created. It is only possible to create an instance using the static methods
 * in this class.
 */
public final class TrustedProof extends Proof {

	/** An internal version of a {@link TrustedProof}. */
	public interface Internal {

		/**
		 * Return the set of statements which form the basis of this proof.
		 * 
		 * @return the grounding statements
		 */
		Set<Statement> getGrounding();

		/**
		 * Return the set of statements constructed by this proof from the
		 * {@link #getGrounding() grounding} statements.
		 * 
		 * @return the grounded statements
		 */
		Set<Statement> getGrounded();

		/**
		 * Return a justification for the given statement.
		 * 
		 * @param statement
		 *            a statement which is an element of {@link #getGrounded()}
		 * @return either {@code null}, meaning that no justification is
		 *         necessary because {@code getGrounding().contains(statement)};
		 *         or a {@link Justification} which gives a
		 *         {@link Justification#getProofStep() proof step} that
		 *         constructs the statement, and a
		 *         {@link Justification#getProof() proof} that constructs all
		 *         prerequisites of the proof step.
		 */
		Justification getJustificationFor(Statement statement);
	}

	/**
	 * Create a trivial proof, which does no construction at all: both its
	 * grounding and grounded statements are just the given statements.
	 * 
	 * @param statements
	 *            the statements that will be returned by both
	 *            {@link #getGrounding()} and {@link #getGrounded()}.
	 * @return the created proof
	 */
	public static Proof Trivial(Set<Statement> statements) {
		return new TrustedProof(new TrivialProof(statements));
	}

	/**
	 * Merge two proofs 'in parallel', so that the new proof just collects
	 * everything which the two parts prove.
	 * 
	 * @param ti1
	 *            one proof
	 * @param ti2
	 *            another proof
	 * @return the created proof
	 */
	public static Proof Par(Proof ti1, Proof ti2) {
		return new TrustedProof(new ParProof(ti1, ti2));
	}

	/**
	 * Merge two proofs 'in sequence', where the second proof builds on the
	 * second one.
	 * 
	 * @param ti1
	 *            the first proof
	 * @param ti2
	 *            the second proof
	 * @return the created proof
	 */
	public static Proof Seq(Proof ti1, Proof ti2) {
		// TODO: Perhaps assert ti1.getGrounded().equals(ti2.getGrounding()),
		// and have a separate place where we do the construction which is
		// currently in the constructor of SeqProof.
		return new TrustedProof(new SeqProof(ti1, ti2));
	}

	private TrustedProof.Internal internalTrustedProof;

	private TrustedProof(TrustedProof.Internal internalTrustedProof) {
		this.internalTrustedProof = internalTrustedProof;
	}

	@Override
	public Set<Statement> getGrounding() {
		return internalTrustedProof.getGrounding();
	}

	@Override
	public Set<Statement> getGrounded() {
		return internalTrustedProof.getGrounded();
	}

	@Override
	public Justification getJustificationFor(Statement statement) {
		assert getGrounded().contains(statement);
		Justification justification = internalTrustedProof.getJustificationFor(statement);
		if (justification == null) {
			assert getGrounding().contains(statement);
		} else {
			assert justification.getProof().getGrounded().containsAll(justification.getProofStep().getGrounding());
		}
		return justification;
	}
}
