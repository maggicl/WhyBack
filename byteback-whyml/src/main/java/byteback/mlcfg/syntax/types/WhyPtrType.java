package byteback.mlcfg.syntax.types;

import byteback.mlcfg.identifiers.Identifier;
import byteback.mlcfg.identifiers.IdentifierEscaper;
import java.util.Optional;

public interface WhyPtrType extends WhyType {
	String HEAP_VAR_NAME = IdentifierEscaper.PRELUDE_RESERVED + "heap";

	@Override
	default String getWhyType() {
		return "Ptr.t";
	}

	default Optional<String> getPrecondition(String paramName, boolean isNonNull) {
		final String precondition = "Heap.ofclass %s %s (%s)".formatted(HEAP_VAR_NAME, paramName, getPreludeType());
		return Optional.of(isNonNull ? "%s <> Ptr.null && %s".formatted(paramName, precondition) : precondition);
	}
}
