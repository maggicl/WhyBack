package byteback.whyml.syntax.function;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.printer.Code;
import static byteback.whyml.printer.Code.line;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.expr.InstanceOfExpression;
import byteback.whyml.syntax.expr.LocalVariableExpression;
import byteback.whyml.syntax.expr.UnaryExpression;
import byteback.whyml.syntax.expr.binary.BinaryExpression;
import byteback.whyml.syntax.expr.binary.LogicConnector;
import byteback.whyml.syntax.type.WhyJVMType;
import byteback.whyml.syntax.type.WhyType;
import java.util.Locale;
import java.util.Optional;

public record WhyLocal(Identifier.L name, WhyType type, boolean isNotNull) {
	public WhyLocal(Identifier.L name, WhyType type) {
		this(name, type, false);
	}

	public Optional<Expression> condition() {
		if (type.jvm() != WhyJVMType.PTR) {
			return Optional.empty();
		}

		final Expression var = new LocalVariableExpression(name, type.jvm());
		final Expression isType = new InstanceOfExpression(var, type);

		return Optional.of(isNotNull
				? new BinaryExpression(LogicConnector.AND,
				new UnaryExpression(UnaryExpression.Operator.NOT_NULL, var),
				isType)
				: isType);
	}

	public Code toWhy() {
		// TODO: consider adding invariants for object variables
		return line("var %s: %s;".formatted(name, type.getWhyType()));
	}

	public Code initialization() {
		return type.jvm() == WhyJVMType.PTR
				? line("%s <- Ptr.null;".formatted(name))
				: line("%s <- Default.%s;".formatted(name, type.jvm().getWhyAccessorScope().toLowerCase(Locale.ROOT)));
	}
}
