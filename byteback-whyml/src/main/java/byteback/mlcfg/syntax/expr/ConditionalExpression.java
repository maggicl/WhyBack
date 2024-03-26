package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.syntax.types.WhyJVMType;

public class ConditionalExpression implements Expression {
	private final Expression conditional;
	private final Expression thenExpr;
	private final Expression elseExpr;

	public ConditionalExpression(Expression conditional, Expression thenExpr, Expression elseExpr) {
		if (conditional.type() != WhyJVMType.BOOL) {
			throw new IllegalArgumentException("conditional in conditional expression should have BOOL type");
		}

		if (thenExpr.type() != elseExpr.type()) {
			throw new IllegalArgumentException(
					"then and else branch in conditional expression should have same type, given %s and %s".formatted(
							thenExpr.type(),
							elseExpr.type()
					));
		}

		this.conditional = conditional;
		this.thenExpr = thenExpr;
		this.elseExpr = elseExpr;
	}


	@Override
	public String toWhy() {
		return "(if (%s) then (%s) else (%s))".formatted(conditional.toWhy(), thenExpr.toWhy(), elseExpr.toWhy());
	}

	@Override
	public WhyJVMType type() {
		return thenExpr.type();
	}
}
