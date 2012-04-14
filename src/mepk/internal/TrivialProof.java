package mepk.internal;

import java.util.HashSet;
import java.util.Set;

import mepk.Justification;
import mepk.Statement;
import mepk.TrustedProof;

/**
 * A trivial proof grounds exactly its grounding statements, and therefore it
 * does not have to construct anything.
 */
public class TrivialProof implements TrustedProof.Internal {

	private Set<Statement> statements;

	/**
	 * Create an instance.
	 * 
	 * @param statements
	 *            the statements grounding, and trivially grounded by, this
	 *            proof
	 */
	public TrivialProof(Set<Statement> statements) {
		this.statements = statements;
	}

	@Override
	public Set<Statement> getGrounding() {
		Set<Statement> result = new HashSet<Statement>();
		result.addAll(statements);
		return result;
	}

	@Override
	public Set<Statement> getGrounded() {
		Set<Statement> result = new HashSet<Statement>();
		result.addAll(statements);
		return result;
	}

	@Override
	public Justification getJustificationFor(Statement statement) {
		assert getGrounded().contains(statement);
		// ...therefore no justification is necessary, and we can return null
		return null;
	}

}
