package byteback.whyml.syntax.expr;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.printer.SExpr;
import static byteback.whyml.printer.SExpr.terminal;
import byteback.whyml.syntax.expr.transformer.ExpressionTransformer;
import byteback.whyml.syntax.expr.transformer.ExpressionVisitor;
import byteback.whyml.syntax.type.WhyJVMType;

public class LocalVariableExpression implements Expression {
	public Identifier.L getName() {
		return name;
	}

	public WhyJVMType getType() {
		return type;
	}

	private final Identifier.L name;
	private final WhyJVMType type;

	public LocalVariableExpression(Identifier.L name, WhyJVMType type) {
		this.name = name;
		this.type = type;
	}

	@Override
	public SExpr toWhy() {
		return terminal(name.toString());
	}

	@Override
	public WhyJVMType type() {
		return type;
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
