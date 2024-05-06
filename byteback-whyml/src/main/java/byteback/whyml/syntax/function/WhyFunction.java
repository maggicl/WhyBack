package byteback.whyml.syntax.function;

import java.util.Objects;
import java.util.Optional;

public record WhyFunction(WhyFunctionContract contract, Optional<WhyFunctionBody> body) {
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
}
