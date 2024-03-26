package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.identifiers.Identifier;
import byteback.mlcfg.printer.SExpr;
import static byteback.mlcfg.printer.SExpr.terminal;
import byteback.mlcfg.syntax.expr.transformer.ExpressionTransformer;
import byteback.mlcfg.syntax.types.WhyJVMType;

public class LocalVariableExpression implements Expression {
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
	public Expression visit(ExpressionTransformer transformer) {
		return transformer.transformLocalVariableExpression(this);
	}
}
