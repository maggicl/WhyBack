package byteback.mlcfg.syntax.types;

import java.util.Optional;

public enum WhyPrimitive implements WhyType {
	/**
	 * "true" or "false"
	 */
	BOOL("bool"),

	/**
	 * An unbounded integer type
	 */
	INT_INT("int.Int"), // TODO: consider bounded integer types for all JVM primitive types

	/**
	 * IEEE 754 single precision
	 */
	FLOAT_8_23("<float 8 23>"),

	/**
	 * IEEE 754 double precision
	 */
	FLOAT_11_52("<float 11 52>");

	private final String label;

	WhyPrimitive(String typeLabel) {
		this.label = typeLabel;
	}

	@Override
	public Optional<String> getPrefix() {
		return Optional.empty();
	}

	@Override
	public String getIdentifier() {
		return label;
	}
}
