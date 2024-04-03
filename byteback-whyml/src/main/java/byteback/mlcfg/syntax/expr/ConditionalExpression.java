package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.printer.SExpr;
import byteback.mlcfg.syntax.expr.harmonization.BinaryOpTypeHarmonizer;
import byteback.mlcfg.syntax.expr.harmonization.HarmonizationResult;
import byteback.mlcfg.syntax.expr.transformer.ExpressionTransformer;
import byteback.mlcfg.syntax.expr.transformer.ExpressionVisitor;
import byteback.mlcfg.syntax.types.WhyJVMType;

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
