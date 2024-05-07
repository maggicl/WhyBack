package byteback.whyml.syntax.expr.field;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.printer.SExpr;
import static byteback.whyml.printer.SExpr.prefix;
import static byteback.whyml.printer.SExpr.terminal;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.expr.transformer.ExpressionTransformer;
import byteback.whyml.syntax.expr.transformer.ExpressionVisitor;
import byteback.whyml.syntax.type.WhyJVMType;

public record FieldExpression(Operation operation, Access access) implements Expression {
	@Override
	public SExpr toWhy() {
		final String accessor = access.getField().getType().jvm().getWhyAccessorScope();
		final String instanceOrStatic = access instanceof Access.Instance ? "f" : "s";
		final Identifier.FQDN fieldFqdn = access.getField().getClazz().qualify(access.getField().getName());

		return prefix(
				"%s.%s%s".formatted(accessor, operation.whyKeyword(), instanceOrStatic),
				terminal(Identifier.Special.HEAP),
				access.referenceToWhy(),
				terminal(fieldFqdn + ".v")
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
