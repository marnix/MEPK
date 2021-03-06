package mepk.builtin.internal;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import mepk.builtin.TrustedProof;
import mepk.kernel.Abbreviation;
import mepk.kernel.Justification;
import mepk.kernel.Statement;

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
	public Map<String, Abbreviation> getAbbreviations() {
		return Collections.emptyMap();
	}

	@Override
	public Justification getJustificationFor(Statement statement) {
		assert getGrounded().contains(statement);
		// ...therefore no justification is necessary, and we can return null
		return null;
	}

}
