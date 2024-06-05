package byteback.whyml.syntax.expr.field;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.printer.SExpr;
import static byteback.whyml.printer.SExpr.prefix;
import static byteback.whyml.printer.SExpr.terminal;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.expr.transformer.ExpressionTransformer;
import byteback.whyml.syntax.expr.transformer.ExpressionVisitor;
import byteback.whyml.syntax.type.WhyJVMType;

/**
 * @param elementType We need to know the element type of the array as the array expression only has a JVM type of "ref" no matter what
 */
public record ArrayExpression(Expression base, WhyJVMType elementType, ArrayOperation operation) implements Expression {
	public ArrayExpression {
		if (base.type() != WhyJVMType.PTR) {
			throw new IllegalArgumentException("base of an array expression must be of type PTR (i.e. an array)");
		}
	}

	@Override
	public SExpr toWhy() {
		final String accessor = "R" + elementType.getWhyAccessorScope();
		final Identifier.L heap = Identifier.Special.getArrayHeap(elementType);

		if (operation instanceof ArrayOperation.Load load) {
			return prefix(
					accessor + ".load",
					terminal(heap),
					base.toWhy(),
					load.getIndex().toWhy()
			);
		} else if (operation instanceof ArrayOperation.IsElem isElem) {
			return prefix(
					accessor + ".iselem",
					terminal(heap),
					base.toWhy(),
					isElem.getIndex().toWhy()
			);
		} else {
			return prefix(
					accessor + ".arraylength",
					terminal(heap),
					base.toWhy()
			);
		}
	}

	@Override
	public WhyJVMType type() {
		return operation.fixedReturnType().orElse(elementType);
	}

	@Override
	public Expression accept(ExpressionTransformer transformer) {
		return transformer.transformArrayExpression(this);
	}

	@Override
	public void accept(ExpressionVisitor visitor) {
		visitor.visitArrayExpression(this);
	}
}
