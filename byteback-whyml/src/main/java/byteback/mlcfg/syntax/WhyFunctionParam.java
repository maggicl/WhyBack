package byteback.mlcfg.syntax;

import byteback.mlcfg.identifiers.Identifier;
import byteback.mlcfg.syntax.types.WhyJVMType;
import byteback.mlcfg.syntax.types.WhyType;
import java.util.Optional;

public record WhyFunctionParam(Identifier.L name, WhyType type, boolean isNonNull) {
	public Optional<String> condition() {
		if (type.jvm() != WhyJVMType.PTR) {
			return Optional.empty();
		}

		final String precondition = "Heap.instanceof heap %s (%s)".formatted(
				name,
				type.getPreludeType());

		return Optional.of(isNonNull
				? "%s <> Ptr.null && %s".formatted(name, precondition)
				: precondition);
	}
}
