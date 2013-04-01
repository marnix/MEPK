package mepk.kernel.util;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import mepk.kernel.Abbreviation;
import mepk.kernel.Justification;
import mepk.kernel.Proof;
import mepk.kernel.ProofStep;
import mepk.kernel.Statement;

/**
 * A proof which wraps another proof, and expands all that proof's
 * abbreviations.
 */
public class ExpandedAbbreviationsProof extends Proof {

	private final Proof proof;

	public ExpandedAbbreviationsProof(Proof proof) {
		this.proof = proof;
	}

	@Override
	public Set<Statement> getGrounding() {
		return proof.getGrounding();
	}

	@Override
	public Set<Statement> getGrounded() {
		return proof.getGrounded();
		// TODO: ...with all proof.getAbbreviations() expanded
	}

	@Override
	public Map<String, Abbreviation> getAbbreviations() {
		return Collections.emptyMap();
	}

	@Override
	public Justification getJustificationFor(Statement statement) {
		Justification justification = proof.getJustificationFor(statement);
		if (justification == null) {
			return null;
		} else {
			ProofStep proofStep = justification.getProofStep();
			// TODO: ...with all proof.getAbbreviations() expanded
			return new Justification(proofStep, new ExpandedAbbreviationsProof(justification.getProof()));
		}
	}

}
