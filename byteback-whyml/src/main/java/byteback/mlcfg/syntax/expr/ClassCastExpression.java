package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.printer.SExpr;
import static byteback.mlcfg.printer.SExpr.prefix;
import static byteback.mlcfg.printer.SExpr.terminal;
import byteback.mlcfg.syntax.expr.transformer.ExpressionTransformer;
import byteback.mlcfg.syntax.types.WhyJVMType;
import byteback.mlcfg.syntax.types.WhyType;

public class ClassCastExpression implements Expression {
	private final Expression reference;
	private final WhyType type;

	public ClassCastExpression(Expression reference, WhyType type) {
		if (reference.type() != WhyJVMType.PTR) {
			throw new IllegalArgumentException("checkcast expression must have type PTR, given " + reference.type());
		}

		if (type.jvm() != WhyJVMType.PTR) {
			throw new IllegalArgumentException("checkcast type to check must have JVM type PTR, given " + type.jvm());
		}

		this.reference = reference;
		this.type = type;
	}

	public Expression getReference() {
		return reference;
	}

	public WhyType getType() {
		return type;
	}

	@Override
	public SExpr toWhy() {
		return prefix(
				"checkcast",
				terminal("heap"),
				reference.toWhy(),
				terminal(type.getPreludeType())
		);
	}

	@Override
	public WhyJVMType type() {
		return WhyJVMType.BOOL;
	}

	@Override
	public Expression visit(ExpressionTransformer transformer) {
		return transformer.transformClassCastExpression(this);
	}
}
