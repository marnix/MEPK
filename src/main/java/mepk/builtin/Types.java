package mepk.builtin;

import java.util.LinkedHashMap;

/**
 * A helper class for easily creating a {@code Map<String,String>}, which maps
 * variable names to type expressions.
 */
public class Types {

	/**
	 * This constructor should not be used: this class contains only static
	 * methods.
	 */
	private Types() {
	}

	/**
	 * Create an empty type map.
	 * 
	 * @return the map
	 */
	public static Types.TypeMapBuilder EmptyMap() {
		return new TypeMapBuilder();
	}

	/**
	 * Create a single-entry type map.
	 * 
	 * @param variableName
	 *            the variable
	 * @param typeName
	 *            the type name
	 * @return the extended map
	 */
	public static Types.TypeMapBuilder map(String variableName, String typeName) {
		return EmptyMap().map(variableName, typeName);
	}

	/**
	 * A {@code HashMap<String,String>} mapping variables to type expressions.
	 */
	public static class TypeMapBuilder extends LinkedHashMap<String, String> {
		private static final long serialVersionUID = 1L;

		private TypeMapBuilder() {
		}

		/**
		 * Add the given entry to the type map.
		 * 
		 * @param variableName
		 *            the variable
		 * @param typeName
		 *            the type name
		 * @return this
		 */
		public Types.TypeMapBuilder map(String variableName, String typeName) {
			put(variableName, typeName);
			return this;
		}
	}
}