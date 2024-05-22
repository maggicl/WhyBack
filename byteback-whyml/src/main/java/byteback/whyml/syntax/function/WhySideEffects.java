package byteback.whyml.syntax.function;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public record WhySideEffects(Set<String> reads, Set<String> writes, Set<WhyFunctionSignature> calls) {
	public static WhySideEffects combine(Collection<WhySideEffects> effects) {
		return new WhySideEffects(
				effects.stream().map(WhySideEffects::reads).flatMap(Set::stream).collect(Collectors.toSet()),
				effects.stream().map(WhySideEffects::writes).flatMap(Set::stream).collect(Collectors.toSet()),
				effects.stream().map(WhySideEffects::calls).flatMap(Set::stream).collect(Collectors.toSet())
		);
	}
}
