package mepk.internal;

import java.util.Collections;
import java.util.Set;

import mepk.DVRSet;
import mepk.Expression;
import mepk.ProofStep;
import mepk.Statement;

/**
 * A weakening proof step constructs a new statement by adding DVRs and/or
 * hypotheses to an existing statement.
 */
public class Weaken implements ProofStep.Internal {

	private final Statement statement;
	private final Statement result;

	/**
	 * Create an instance.
	 * 
	 * @param statement
	 *            the grounding statement
	 * @param addedDVRs
	 *            the added DVRs
	 * @param addedHypotheses
	 *            the added hypotheses
	 */
	// TODO: Add DVRs argument
	public Weaken(Statement statement, DVRSet addedDVRs, Expression... addedHypotheses) {
		this.statement = statement;
		result = statement.weaken(addedDVRs, addedHypotheses);
	}

	@Override
	public Set<Statement> getGrounding() {
		return Collections.singleton(statement);
	}

	@Override
	public Statement getGrounded() {
		return result;
	}

}