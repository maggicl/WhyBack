package byteback.mlcfg.syntax;

import byteback.mlcfg.syntax.WhyFunctionSignature;
import byteback.mlcfg.syntax.expr.Expression;
import java.util.Objects;

public class WhyFunction {
	private final WhyFunctionSignature signature;
	private final Expression body;

	public WhyFunction(WhyFunctionSignature signature, Expression body) {
		if (!signature.kind().isSpec()) {
			throw new IllegalArgumentException("a WhyFunction cannot be a program function");
		}

		this.signature = signature;
		this.body = body;
	}

	public WhyFunctionSignature getSignature() {
		return signature;
	}

	public Expression getBody() {
		return body;
	}

	@Override
	public String toString() {
		return "WhyFunction{" +
				"signature=" + signature.identifier() +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		WhyFunction that = (WhyFunction) o;
		return Objects.equals(signature, that.signature);
	}

	@Override
	public int hashCode() {
		return Objects.hash(signature);
	}
}
