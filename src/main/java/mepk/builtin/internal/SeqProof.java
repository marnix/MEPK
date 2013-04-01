package mepk.builtin.internal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import mepk.builtin.TrustedProof;
import mepk.kernel.Abbreviation;
import mepk.kernel.Justification;
import mepk.kernel.MEPKException;
import mepk.kernel.Proof;
import mepk.kernel.Statement;

/**
 * A 'sequential proof' merges two proofs 'in sequence', where the second proof
 * builds on the second one.
 */
public class SeqProof implements TrustedProof.Internal {

	private Proof proof1;
	private Proof proof2;

	/**
	 * Create an instance from two proofs.
	 * 
	 * @param proof1
	 *            the first proof
	 * @param proof2
	 *            the second proof
	 */
	public SeqProof(Proof proof1, Proof proof2) {

		Set<Statement> proof2GroundingWithoutProof1 = new HashSet<Statement>();
		proof2GroundingWithoutProof1.addAll(proof2.getGrounding());
		proof2GroundingWithoutProof1.removeAll(proof1.getGrounded());

		this.proof1 = TrustedProof.Par(proof1, TrustedProof.Trivial(proof2GroundingWithoutProof1));
		this.proof2 = TrustedProof.Par(proof2, TrustedProof.Trivial(proof1.getGrounded()));

		assert this.proof1.getGrounded().equals(this.proof2.getGrounding());

		Set<String> abbrs1 = proof1.getAbbreviations().keySet();
		Set<String> abbrs2 = proof2.getAbbreviations().keySet();
		Set<String> commonAbbrs = new HashSet<String>(abbrs1);
		commonAbbrs.retainAll(abbrs2);
		if (!commonAbbrs.isEmpty()) {
			throw new MEPKException("Sequential proof parts both contain abbreviation(s) " + commonAbbrs);
		}
	}

	@Override
	public Set<Statement> getGrounding() {
		return proof1.getGrounding();
	}

	@Override
	public Set<Statement> getGrounded() {
		Set<Statement> result = new HashSet<Statement>();
		result.addAll(proof1.getGrounded());
		result.addAll(proof2.getGrounded());
		return result;
	}

	@Override
	public Map<String, Abbreviation> getAbbreviations() {
		HashMap<String, Abbreviation> result = new HashMap<String, Abbreviation>();
		result.putAll(proof1.getAbbreviations());
		result.putAll(proof2.getAbbreviations());
		return result;
	}

	@Override
	public Justification getJustificationFor(Statement statement) {
		Justification ti2Justification = proof2.getJustificationFor(statement);
		if (ti2Justification != null) {
			return new Justification(ti2Justification.getProofStep(), TrustedProof.Seq(proof1, ti2Justification.getProof()));
		} else {
			return proof1.getJustificationFor(statement);
		}
	}
}
