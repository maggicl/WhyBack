package byteback.mlcfg.syntax.expr.field;

import byteback.mlcfg.syntax.expr.Expression;
import byteback.mlcfg.syntax.types.WhyJVMType;

public class FieldExpression implements Expression {
	private final Operation operation;
	private final Access access;

	public FieldExpression(Operation operation, Access access) {
		if (operation instanceof Operation.Put put) {
			final WhyJVMType valueType = put.getValue().type();
			final WhyJVMType fieldType = access.getField().getType().jvm();

			if (fieldType != valueType) {
				throw new IllegalArgumentException("cannot assign to field %s with type %s an expression with type %s"
						.formatted(access.getField(), fieldType, valueType));
			}
		}

		this.operation = operation;
		this.access = access;
	}

	@Override
	public String toWhy() {
		final String accessor = access.getField().getType().jvm().getWhyAccessorScope();
		final String instanceOrStatic = access instanceof Access.Instance ? "f" : "s";

		if (operation instanceof Operation.Put put) {
			return "(%s.put%s heap %s %s.v %s)".formatted(
					accessor,
					instanceOrStatic,
					access.referenceToWhy(),
					access.getField().getName(),
					put.getValue().toWhy()
			);
		} else {
			return "(%s.get%s heap %s %s.v)".formatted(
					accessor,
					instanceOrStatic,
					access.referenceToWhy(),
					access.getField().getName()
			);
		}
	}

	@Override
	public WhyJVMType type() {
		return operation.fixedReturnType().orElseGet(() -> access.getField().getType().jvm());
	}
}
