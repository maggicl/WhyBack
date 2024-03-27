package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.identifiers.Identifier;
import byteback.mlcfg.printer.SExpr;
import static byteback.mlcfg.printer.SExpr.terminal;
import byteback.mlcfg.syntax.expr.transformer.ExpressionTransformer;
import byteback.mlcfg.syntax.expr.transformer.ExpressionVisitor;
import byteback.mlcfg.syntax.types.WhyJVMType;

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
