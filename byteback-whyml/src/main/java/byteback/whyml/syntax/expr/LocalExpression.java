package byteback.whyml.syntax.expr;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.printer.SExpr;
import static byteback.whyml.printer.SExpr.terminal;
import byteback.whyml.syntax.expr.transformer.ExpressionTransformer;
import byteback.whyml.syntax.expr.transformer.ExpressionVisitor;
import byteback.whyml.syntax.function.WhyLocal;
import byteback.whyml.syntax.type.WhyJVMType;

public record LocalExpression(Identifier.L name, WhyJVMType type) implements Expression {
	public LocalExpression(WhyLocal local) {
		this(local.name(), local.type().jvm());
	}

	@Override
	public SExpr toWhy() {
		return terminal(name.toString());
	}

	@Override
	public Expression accept(ExpressionTransformer transformer) {
		return transformer.transformLocalVariableExpression(this);
	}

	@Override
	public void accept(ExpressionVisitor visitor) {
		visitor.visitLocalVariableExpression(this);
	}
}
