package byteback.whyml.syntax.expr.transformer;

import byteback.whyml.syntax.function.WhyFunctionSignature;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.expr.FunctionCall;
import java.util.HashSet;
import java.util.Set;

public class CallDependenceVisitor extends ExpressionVisitor {
	private final Set<WhyFunctionSignature> calls = new HashSet<>();

	private CallDependenceVisitor() {}

	public static Set<WhyFunctionSignature> getCallees(Expression expression) {
		final CallDependenceVisitor v = new CallDependenceVisitor();
		expression.accept(v);
		return v.calls;
	}

	@Override
	public void visitFunctionCall(FunctionCall source) {
		calls.add(source.function());
		super.visitFunctionCall(source);
	}
}
