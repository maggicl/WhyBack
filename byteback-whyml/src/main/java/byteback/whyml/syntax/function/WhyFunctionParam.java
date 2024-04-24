package byteback.whyml.syntax.function;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.syntax.type.WhyJVMType;
import byteback.whyml.syntax.type.WhyType;
import java.util.Optional;

public record WhyFunctionParam(Identifier.L name, WhyType type, boolean isThis) {
	public Optional<String> condition() {
		if (type.jvm() != WhyJVMType.PTR) {
			return Optional.empty();
		}

		final String precondition = "Heap.instanceof %s %s (%s)".formatted(
				Identifier.Special.HEAP,
				name,
				type.getPreludeType());

		return Optional.of(isThis
				? "%s <> Ptr.null && %s".formatted(name, precondition)
				: precondition);
	}
}
