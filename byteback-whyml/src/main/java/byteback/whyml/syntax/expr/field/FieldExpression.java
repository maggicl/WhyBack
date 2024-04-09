package byteback.whyml.syntax.expr.field;

import byteback.whyml.printer.SExpr;
import static byteback.whyml.printer.SExpr.prefix;
import static byteback.whyml.printer.SExpr.terminal;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.expr.transformer.ExpressionTransformer;
import byteback.whyml.syntax.expr.transformer.ExpressionVisitor;
import byteback.whyml.syntax.type.WhyJVMType;

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

	public Operation getOperation() {
		return operation;
	}

	public Access getAccess() {
		return access;
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

	@Override
	public Expression accept(ExpressionTransformer transformer) {
		return transformer.transformFieldExpression(this);
	}

	@Override
	public void accept(ExpressionVisitor visitor) {
		visitor.visitFieldExpression(this);
	}
}
