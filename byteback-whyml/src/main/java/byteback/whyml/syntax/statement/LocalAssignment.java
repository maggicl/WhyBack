package byteback.whyml.syntax.statement;

import byteback.whyml.printer.Code;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.function.WhyLocal;

public record LocalAssignment(WhyLocal lValue, Expression rValue) implements CFGStatement {
	@Override
	public Code toWhy() {
		return rValue.toWhy().statement("%s <- ".formatted(lValue.name()), ";");
	}
}
