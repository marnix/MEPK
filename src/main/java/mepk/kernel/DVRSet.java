package mepk.kernel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * A set of 'distinct variable restrictions', each of which is an unordered pair
 * of different variable names. DVR sets are values: they cannot be modified
 * after they have been created. They are {@link #equals(Object) equal} if (and
 * only if) they have the same structure. It is only possible to create
 * instances using the methods in this class.
 */
public final class DVRSet {

	/** The empty DVRSet. */
	public static final DVRSet EMPTY = new DVRSet(new HashMap<String, Set<String>>());

	private final Map<String, Set<String>> dvrMap;

	private DVRSet(Map<String, Set<String>> dvrMap) {
		this.dvrMap = dvrMap;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dvrMap == null) ? 0 : dvrMap.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof DVRSet)) {
			return false;
		}
		DVRSet other = (DVRSet) obj;
		if (dvrMap == null) {
			if (other.dvrMap != null) {
				return false;
			}
		} else if (!dvrMap.equals(other.dvrMap)) {
			return false;
		}
		return true;
	}

	/**
	 * Replace the given variable name by the given set of variable names.
	 * 
	 * @param varName
	 *            the variable name to be replaced
	 * @param varNames
	 *            the replacing variable names
	 * @return the new DVR set
	 */
	public DVRSet substitute(String varName, Iterable<String> varNames) {
		return this; // TODO: implement DVRSet substitution
	}

	/**
	 * Create a new DVR set by adding the given DVRs.
	 * 
	 * @param addedDVRs
	 *            the DVRs to be added
	 * @return the new DVR set
	 */
	public DVRSet add(DVRSet addedDVRs) {
		// copy the existing map
		Map<String, Set<String>> newDVRMap = new HashMap<String, Set<String>>(dvrMap);
		
		Map<String, Set<String>> addedDVRMap = addedDVRs.dvrMap;
		for (Entry<String, Set<String>> entry : addedDVRMap.entrySet()) {
			String varName = entry.getKey();
			Set<String> distinctVarNames = entry.getValue();

			Set<String> newDistinctVarNames = distinctVarNames;
			if (newDVRMap.containsKey(varName)) {
				newDistinctVarNames = new HashSet<String>(newDistinctVarNames);
				newDistinctVarNames.addAll(newDVRMap.get(varName));
			}
			newDVRMap.put(varName, newDistinctVarNames);
		}

		// create a new set
		return new DVRSet(newDVRMap);
	}

	/**
	 * Create a new DVR set by adding a DVR for each pair of the given variable
	 * names.
	 * 
	 * @param varNames
	 *            the variables which should be distinct.
	 * @return the new DVR set
	 */
	public DVRSet add(String... varNames) {
		// copy the existing map
		Map<String, Set<String>> newDVRMap = new HashMap<String, Set<String>>();
		for (String key : dvrMap.keySet()) {
			newDVRMap.put(key, new HashSet<String>(dvrMap.get(key)));
		}

		// add all new combinations
		for (int i = 0; i < varNames.length; i++) {
			for (int j = 0; j < varNames.length; j++) {
				if (i != j) {
					String v = varNames[i];
					String w = varNames[j];
					if (v.equals(w)) {
						throw new MEPKException(String.format("Variable '%s' may not be disjoint with itself", v));
					}
					if (!newDVRMap.containsKey(v)) {
						newDVRMap.put(v, new HashSet<String>());
					}
					Set<String> vvv = newDVRMap.get(v);
					vvv.add(w);
				}
			}
		}

		// create a new set
		return new DVRSet(newDVRMap);
	}
}