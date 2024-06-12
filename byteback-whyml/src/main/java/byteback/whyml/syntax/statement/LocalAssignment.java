package byteback.whyml.syntax.statement;

import byteback.whyml.printer.Code;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.expr.harmonization.WhyTypeHarmonizer;
import byteback.whyml.syntax.function.WhyLocal;
import byteback.whyml.syntax.statement.visitor.StatementVisitor;

public record LocalAssignment(WhyLocal lValue, Expression rValue) implements CFGStatement {

	public static LocalAssignment build(WhyLocal lValue, Expression rValue) {
		try {
			final Expression actualValue = WhyTypeHarmonizer.harmonizeExpression(lValue.type(), rValue);
			return new LocalAssignment(lValue, actualValue);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("illegal assignment on local variable %s: %s".formatted(lValue, e.getMessage()), e);
		}
	}

	@Override
	public Code toWhy() {
		return rValue.toWhy(false).statement("%s <- ".formatted(lValue.name()), ";");
	}

	@Override
	public void accept(StatementVisitor visitor) {
		visitor.visitLocalAssignmentStatement(this);
	}
}
