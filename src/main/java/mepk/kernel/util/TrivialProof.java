package mepk.kernel.util;

import java.util.Set;

import mepk.kernel.Justification;
import mepk.kernel.Proof;
import mepk.kernel.Statement;

/**
 * A proof which trivially grounds exactly its grounding statements.
 */
public final class TrivialProof extends Proof {

	private final Set<Statement> statements;

	public TrivialProof(Set<Statement> statements) {
		this.statements = statements;
	}

	@Override
	public Set<Statement> getGrounding() {
		return statements;
	}

	@Override
	public Set<Statement> getGrounded() {
		return getGrounding();
	}

	@Override
	public Justification getJustificationFor(Statement statement) {
		return null;
	}

}