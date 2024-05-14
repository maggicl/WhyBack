package byteback.whyml.syntax.function;

import java.util.Set;

public record WhySideEffects(Set<String> reads, Set<String> writes, Set<WhyFunctionSignature> calls) {
}
