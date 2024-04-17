package byteback.whyml.syntax.expr.field;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.printer.SExpr;
import static byteback.whyml.printer.SExpr.prefix;
import static byteback.whyml.printer.SExpr.terminal;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.expr.transformer.ExpressionTransformer;
import byteback.whyml.syntax.expr.transformer.ExpressionVisitor;
import byteback.whyml.syntax.type.WhyJVMType;

public class ArrayExpression implements Expression {
	private final Expression base;
	// We need to know the element type of the array as the array expression only has a JVM type of "ref" no matter what
	private final WhyJVMType elementType;
	private final ArrayOperation operation;

	public ArrayExpression(Expression base, WhyJVMType elementType, ArrayOperation operation) {
		if (base.type() != WhyJVMType.PTR) {
			throw new IllegalArgumentException("base of an array expression must be of type PTR (i.e. an array)");
		}

		if (operation instanceof ArrayOperation.Store store) {
			final WhyJVMType valueType = store.getValue().type();

			if (elementType != valueType) {
				throw new IllegalArgumentException("cannot assign to array with type %s an expression with type %s"
						.formatted(elementType, valueType));
			}
		}

		this.base = base;
		this.elementType = elementType;
		this.operation = operation;
	}

	public Expression getBase() {
		return base;
	}

	public WhyJVMType getElementType() {
		return elementType;
	}

	public ArrayOperation getOperation() {
		return operation;
	}

	@Override
	public SExpr toWhy() {
		final String accessor = "R" + elementType.getWhyAccessorScope();

		if (operation instanceof ArrayOperation.Store store) {
			return prefix(
					accessor + ".store",
					terminal(Identifier.Special.HEAP),
					base.toWhy(),
					store.getIndex().toWhy(),
					store.getValue().toWhy()
			);
		} else if (operation instanceof ArrayOperation.Load load) {
			return prefix(
					accessor + ".load",
					terminal(Identifier.Special.HEAP),
					base.toWhy(),
					load.getIndex().toWhy()
			);
		} else if (operation instanceof ArrayOperation.IsElem isElem) {
			return prefix(
					accessor + ".iselem",
					terminal(Identifier.Special.HEAP),
					base.toWhy(),
					isElem.getIndex().toWhy()
			);
		} else {
			return prefix(
					accessor + ".arraylength",
					terminal(Identifier.Special.HEAP),
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
