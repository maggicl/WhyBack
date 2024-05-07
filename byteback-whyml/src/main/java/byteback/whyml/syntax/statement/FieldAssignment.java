package byteback.whyml.syntax.statement;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.printer.Code;
import static byteback.whyml.printer.SExpr.prefix;
import static byteback.whyml.printer.SExpr.terminal;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.expr.field.Access;
import byteback.whyml.syntax.statement.CFGStatement;
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

	@Override
	public Code toWhy() {
		final String accessor = access.getField().getType().jvm().getWhyAccessorScope();
		final String instanceOrStatic = access instanceof Access.Instance ? "f" : "s";
		final Identifier.FQDN fieldFqdn = access.getField().getClazz().qualify(access.getField().getName());

		return prefix(
				"%s.put%s".formatted(accessor, instanceOrStatic),
				terminal(Identifier.Special.HEAP),
				access.referenceToWhy(),
				terminal(fieldFqdn + ".v"),
				value.toWhy()
		).statement("", ";");
	}
}
