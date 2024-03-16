package byteback.mlcfg.syntax;

import byteback.mlcfg.identifiers.Identifier;
import byteback.mlcfg.syntax.types.WhyType;

public record WhyFunctionParam(Identifier.L name, WhyType type, boolean isNonNull) {
	public String condition() {
		final String precondition = "Heap.instanceof heap %s (%s)".formatted(
				name,
				type.getPreludeType());
		return isNonNull ? "%s <> Ptr.null && %s".formatted(name, precondition) : precondition;
	}
}
