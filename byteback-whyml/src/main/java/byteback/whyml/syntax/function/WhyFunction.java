package byteback.whyml.syntax.function;

import byteback.whyml.vimp.graph.Node;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public record WhyFunction(WhyFunctionContract contract,
						  Optional<WhyFunctionBody> body) implements Node<WhyFunctionSignature, WhyFunction> {
	public WhyFunction {
		if (body.isEmpty() && contract.signature().declaration().isSpec()) {
			throw new IllegalArgumentException("a spec WhyFunction cannot have an empty body");
		}

		if (body.isPresent() && !body.get().forDecls().contains(contract.signature().declaration())) {
			throw new IllegalArgumentException("body is incompatible with spec declaration body");
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		WhyFunction that = (WhyFunction) o;
		return Objects.equals(contract, that.contract);
	}

	@Override
	public int hashCode() {
		return Objects.hash(contract);
	}

	@Override
	public WhyFunctionSignature index() {
		return contract.signature();
	}

	@Override
	public List<WhyFunctionSignature> nearTo() {
		return Stream.concat(
			contract.conditions().stream()
					.map(WhyCondition::sideEffects)
					.map(WhySideEffects::calls)
					.flatMap(Set::stream),
			body.stream().flatMap(e -> e.sideEffects().calls().stream())
		).sorted().toList();
	}
}
