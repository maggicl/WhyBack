package byteback.whyml.syntax.statement;

import byteback.whyml.printer.Code;
import static byteback.whyml.printer.SExpr.prefix;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.statement.visitor.StatementVisitor;

/**
 * Statement where a method is invoked and its return value is discarded
 */
public record CFGInvokeStmt(Expression invokeExpr) implements CFGStatement {
	@Override
	public Code toWhy() {
		// just call the method and discard the return value
		return prefix("void", invokeExpr.toWhy(false)).statement("", ";");
	}

	@Override
	public void accept(StatementVisitor visitor) {
		visitor.visitInvokeStmt(this);
	}
}
