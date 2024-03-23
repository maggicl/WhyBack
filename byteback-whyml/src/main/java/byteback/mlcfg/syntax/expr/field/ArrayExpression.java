package byteback.mlcfg.syntax.expr.field;

import byteback.mlcfg.syntax.expr.Expression;
import byteback.mlcfg.syntax.types.WhyJVMType;

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

	@Override
	public String toWhy() {
		final String accessor = "R" + elementType.getWhyAccessorScope();

		if (operation instanceof ArrayOperation.Store store) {
			return "(%s.store heap %s %s %s)".formatted(
					accessor,
					base.toWhy(),
					store.getIndex().toWhy(),
					store.getValue().toWhy()
			);
		} else if (operation instanceof ArrayOperation.Load load) {
			return "(%s.load heap %s %s)".formatted(
					accessor,
					base.toWhy(),
					load.getIndex().toWhy()
			);
		} else {
			return "(%s.arraylength heap %s)".formatted(
					accessor,
					base.toWhy()
			);
		}
	}

	@Override
	public WhyJVMType type() {
		return operation.fixedReturnType().orElse(elementType);
	}
}
