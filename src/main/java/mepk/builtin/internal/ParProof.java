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
 * A 'parallel proof' merges two proofs 'in parallel', so that the new proof
 * just collects everything which the two parts prove.
 */
public class ParProof implements TrustedProof.Internal {

	private Proof proof1;
	private Proof proof2;

	/**
	 * Create an instance from two proofs.
	 * 
	 * @param proof1
	 *            one proof
	 * @param proof2
	 *            another proof
	 */
	public ParProof(Proof proof1, Proof proof2) {
		this.proof1 = proof1;
		this.proof2 = proof2;

		Set<String> abbrs1 = proof1.getAbbreviations().keySet();
		Set<String> abbrs2 = proof2.getAbbreviations().keySet();
		Set<String> commonAbbrs = new HashSet<String>(abbrs1);
		commonAbbrs.retainAll(abbrs2);
		for (String commonAbbr : commonAbbrs) {
			if (!proof1.getAbbreviations().get(commonAbbr).equals(proof2.getAbbreviations().get(commonAbbr))) {
				throw new MEPKException("Parallel proofs have different abbreviation " + commonAbbr);
			}
		}
	}

	@Override
	public Set<Statement> getGrounding() {
		Set<Statement> result = new HashSet<Statement>();
		result.addAll(proof2.getGrounding());
		result.addAll(proof1.getGrounding());
		return result;
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
		if (proof1.getGrounded().contains(statement)) {
			// if both ti1 and ti2 justify the given statement, we arbitrarily
			// return ti1's justification
			return proof1.getJustificationFor(statement);
		} else {
			assert proof2.getGrounded().contains(statement);
			return proof2.getJustificationFor(statement);
		}
	}
}
