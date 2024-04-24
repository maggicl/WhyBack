package byteback.whyml.syntax.function;

import byteback.whyml.syntax.expr.Expression;
import java.util.Objects;

public record WhySpecFunction(WhyFunctionContract contract, Expression body) {
	public WhySpecFunction {
		if (!contract.signature().declaration().isSpec()) {
			throw new IllegalArgumentException("a WhyFunction cannot be a program function");
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		WhySpecFunction that = (WhySpecFunction) o;
		return Objects.equals(contract, that.contract);
	}

	@Override
	public int hashCode() {
		return Objects.hash(contract);
	}
}
