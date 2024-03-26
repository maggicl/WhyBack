package byteback.mlcfg.syntax.expr.field;

import byteback.mlcfg.printer.SExpr;
import static byteback.mlcfg.printer.SExpr.prefix;
import static byteback.mlcfg.printer.SExpr.terminal;
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
	public SExpr toWhy() {
		final String accessor = access.getField().getType().jvm().getWhyAccessorScope();
		final String instanceOrStatic = access instanceof Access.Instance ? "f" : "s";

		if (operation instanceof Operation.Put put) {
			return prefix(
					"%s.put%s".formatted(accessor, instanceOrStatic),
					terminal("heap"),
					access.referenceToWhy(),
					terminal(access.getField().getName() + ".v"),
					put.getValue().toWhy()
			);
		} else {
			return prefix(
					"%s.get%s".formatted(accessor, instanceOrStatic),
					terminal("heap"),
					access.referenceToWhy(),
					terminal(access.getField().getName() + ".v")
			);
		}
	}

	@Override
	public WhyJVMType type() {
		return operation.fixedReturnType().orElseGet(() -> access.getField().getType().jvm());
	}
}
