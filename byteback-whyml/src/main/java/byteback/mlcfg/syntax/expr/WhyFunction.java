package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.syntax.WhyFunctionSignature;

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
