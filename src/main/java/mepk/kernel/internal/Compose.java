package mepk.kernel.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mepk.kernel.DVRSet;
import mepk.kernel.Expression;
import mepk.kernel.MEPKException;
import mepk.kernel.ProofStep;
import mepk.kernel.Statement;

public class Compose implements ProofStep.Internal {

	private final HashSet<Statement> grounding;
	private final Statement grounded;

	public Compose(Statement statement, Statement... statements) {
		grounding = new HashSet<Statement>(Arrays.asList(statements));
		grounding.add(statement);
		
		List<Expression> hypotheses = new ArrayList<Expression>();
		Set<Expression> conclusions = new HashSet<Expression>();
		DVRSet dvrs = DVRSet.EMPTY;
		for (Statement s : statements) {
			hypotheses.addAll(s.getHypotheses());
			conclusions.add(s.getConclusion());
			dvrs.add(s.getDVRs());
		}
		dvrs.add(statement.getDVRs());
		if (!conclusions.equals(statement.getHypotheses())) {
			throw new MEPKException(String.format(
					"Conclusions of sub-statements, %s, should be equal to hypotheses of composing statement, %s", conclusions,
					statement.getHypotheses()));
		}
		grounded = Statement.Stat(dvrs, hypotheses, statement.getConclusion());
	}

	@Override
	public Set<Statement> getGrounding() {
		return grounding;
	}

	@Override
	public Statement getGrounded1() {
		return grounded;
	}

}
