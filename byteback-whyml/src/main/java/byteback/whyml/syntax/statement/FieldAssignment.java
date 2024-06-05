package byteback.whyml.syntax.statement;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.printer.Code;
import static byteback.whyml.printer.SExpr.prefix;
import static byteback.whyml.printer.SExpr.terminal;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.expr.field.Access;
import byteback.whyml.syntax.expr.harmonization.WhyTypeHarmonizer;
import byteback.whyml.syntax.field.WhyField;
import byteback.whyml.syntax.statement.CFGStatement;
import byteback.whyml.syntax.statement.visitor.StatementVisitor;
import byteback.whyml.syntax.type.WhyJVMType;

public record FieldAssignment(Access access, Expression value) implements CFGStatement {
	public FieldAssignment {
		final WhyJVMType valueType = value.type();
		final WhyJVMType fieldType = access.getField().getType().jvm();

		if (fieldType != valueType) {
			throw new IllegalArgumentException("cannot assign to field %s with type %s an expression with type %s"
					.formatted(access.getField(), fieldType, valueType));
		}
	}

	public static FieldAssignment build(Access access, Expression value) {
		try {
			final Expression actualValue = WhyTypeHarmonizer.harmonizeExpression(access.getField().getType(), value);
			return new FieldAssignment(access, actualValue);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("illegal field assignment on %s: %s".formatted(access, e.getMessage()), e);
		}
	}

	@Override
	public Code toWhy() {
		final WhyField field = access.getField();
		final String accessor = field.getType().jvm().getWhyAccessorScope();
		final String instanceOrStatic = access instanceof Access.Instance ? "f" : "s";

		return prefix(
				"%s.put%s".formatted(accessor, instanceOrStatic),
				terminal(Identifier.Special.getHeap(field.getType().jvm())),
				access.referenceToWhy(),
				terminal("%s.%s".formatted(field.getClazz(), field.getName())),
				value.toWhy()
		).statement("", ";");
	}

	@Override
	public void accept(StatementVisitor visitor) {
		visitor.visitFieldAssignmentStatement(this);
	}
}
