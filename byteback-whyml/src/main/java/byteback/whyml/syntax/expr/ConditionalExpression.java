package byteback.whyml.syntax.expr;

import byteback.whyml.printer.SExpr;
import byteback.whyml.syntax.expr.harmonization.BinaryOpTypeHarmonizer;
import byteback.whyml.syntax.expr.harmonization.HarmonizationResult;
import byteback.whyml.syntax.expr.transformer.ExpressionTransformer;
import byteback.whyml.syntax.expr.transformer.ExpressionVisitor;
import byteback.whyml.syntax.type.WhyJVMType;

public class ConditionalExpression implements Expression {
	private final Expression conditional;
	private final Expression thenExpr;
	private final Expression elseExpr;

	public ConditionalExpression(Expression conditional, Expression thenExpr, Expression elseExpr) {
		if (conditional.type() != WhyJVMType.BOOL) {
			throw new IllegalArgumentException("conditional in conditional expression should have BOOL type");
		}

		final HarmonizationResult hr = BinaryOpTypeHarmonizer.harmonize(thenExpr, elseExpr);
		this.conditional = conditional;
		this.thenExpr = hr.getFirstOp();
		this.elseExpr = hr.getSecondOp();
	}

	public Expression getConditional() {
		return conditional;
	}

	public Expression getThenExpr() {
		return thenExpr;
	}

	public Expression getElseExpr() {
		return elseExpr;
	}

	@Override
	public SExpr toWhy() {
		return SExpr.conditional(conditional.toWhy(), thenExpr.toWhy(), elseExpr.toWhy());
	}

	@Override
	public WhyJVMType type() {
		return thenExpr.type();
	}

	@Override
	public Expression accept(ExpressionTransformer transformer) {
		return transformer.transformConditionalExpression(this);
	}


	@Override
	public void accept(ExpressionVisitor visitor) {
		visitor.visitConditionalExpression(this);
	}
}
