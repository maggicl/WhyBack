package byteback.whyml.syntax.function;

import byteback.whyml.syntax.expr.Expression;
import java.util.Objects;

public record WhySpecFunction(WhyFunctionSignature signature, Expression body) {
	public WhySpecFunction {
		if (!signature.declaration().isSpec()) {
			throw new IllegalArgumentException("a WhyFunction cannot be a program function");
		}
	}

	@Override
	public String toString() {
		return "WhyFunction{signature=%s}".formatted(signature);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		WhySpecFunction that = (WhySpecFunction) o;
		return Objects.equals(signature, that.signature);
	}

	@Override
	public int hashCode() {
		return Objects.hash(signature);
	}
}
