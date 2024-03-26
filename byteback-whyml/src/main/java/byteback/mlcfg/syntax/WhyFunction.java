package byteback.mlcfg.syntax;

import byteback.mlcfg.syntax.WhyFunctionSignature;
import byteback.mlcfg.syntax.expr.Expression;

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
}
