package byteback.whyml.syntax.expr.field;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.printer.SExpr;
import static byteback.whyml.printer.SExpr.prefix;
import static byteback.whyml.printer.SExpr.terminal;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.expr.transformer.ExpressionTransformer;
import byteback.whyml.syntax.expr.transformer.ExpressionVisitor;
import byteback.whyml.syntax.field.WhyField;
import byteback.whyml.syntax.type.WhyJVMType;

public record FieldExpression(Operation operation, Access access) implements Expression {
	@Override
	public SExpr toWhy() {
		final WhyField field = access.getField();
		final String accessor = field.getType().jvm().getWhyAccessorScope();
		final String instanceOrStatic = access instanceof Access.Instance ? "f" : "s";

		return prefix(
				"%s.%s%s".formatted(accessor, operation.whyKeyword(), instanceOrStatic),
				terminal(Identifier.Special.getHeap(field.getType().jvm())),
				access.referenceToWhy(),
				terminal("%s.%s".formatted(field.getClazz(), field.getName()))
		);
	}

	@Override
	public WhyJVMType type() {
		return access.getField().getType().jvm();
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
