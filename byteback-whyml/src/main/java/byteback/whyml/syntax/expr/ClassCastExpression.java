package byteback.whyml.syntax.expr;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.printer.SExpr;
import static byteback.whyml.printer.SExpr.prefix;
import static byteback.whyml.printer.SExpr.terminal;
import byteback.whyml.syntax.expr.transformer.ExpressionTransformer;
import byteback.whyml.syntax.expr.transformer.ExpressionVisitor;
import byteback.whyml.syntax.type.WhyJVMType;
import byteback.whyml.syntax.type.WhyType;

public record ClassCastExpression(Expression reference, WhyType exactType, boolean forSpec) implements Expression {
	public ClassCastExpression {
		if (reference.type() != WhyJVMType.PTR) {
			throw new IllegalArgumentException("checkcast expression must have type PTR, given " + reference.type());
		}

		if (exactType.jvm() != WhyJVMType.PTR) {
			throw new IllegalArgumentException("checkcast type to check must have JVM type PTR, given " + exactType.jvm());
		}
	}

	@Override
	public SExpr toWhy() {
		return prefix(
				forSpec ? "iscast" : "checkcast",
				terminal(Identifier.Special.HEAP),
				reference.toWhy(),
				exactType.getPreludeType()
		);
	}

	@Override
	public WhyJVMType type() {
		return WhyJVMType.PTR;
	}

	@Override
	public Expression accept(ExpressionTransformer transformer) {
		return transformer.transformClassCastExpression(this);
	}

	@Override
	public void accept(ExpressionVisitor visitor) {
		visitor.visitClassCastExpression(this);
	}
}
