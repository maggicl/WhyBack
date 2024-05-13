package byteback.whyml.syntax.statement;

import byteback.whyml.printer.Code;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.function.WhyLocal;
import byteback.whyml.syntax.statement.visitor.StatementVisitor;

public record LocalAssignment(WhyLocal lValue, Expression rValue) implements CFGStatement {
	@Override
	public Code toWhy() {
		return rValue.toWhy().statement("%s <- ".formatted(lValue.name()), ";");
	}

	@Override
	public void accept(StatementVisitor visitor) {
		visitor.visitLocalAssignmentStatement(this);
	}
}
