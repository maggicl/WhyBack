package byteback.whyml.vimp.expr;

import byteback.analysis.JimpleStmtSwitch;
import byteback.whyml.syntax.expr.Expression;
import soot.Unit;
import soot.jimple.IdentityStmt;
import soot.jimple.ReturnStmt;

public class FunctionBodyExtractor extends JimpleStmtSwitch<Expression> {
	private final PureExpressionExtractor extractor;

	private Expression result;

	public FunctionBodyExtractor(PureExpressionExtractor extractor) {
		this.extractor = extractor;
	}

	@Override
	public void caseIdentityStmt(final IdentityStmt identity) {
		// IMPORTANT: a spec function is a static method with a single return statement. However, soot declares each
		// parameter as a local variable with an identity statement in the form "l<n>: @parameter<n>" where 0
		// is `this` if the method is not static, 1 in the first parameter (or second for statics), and so on.
		// For the translation process we simply rely on the `l<n>` local variable name, and we name the Why function
		// params using the same convention

		final IdentityStmt a = identity;

		// FIXME: bullshit, if the local variable table is present names are preserved. FIX THIS
	}

	@Override
	public void caseReturnStmt(final ReturnStmt returnStatement) {
		result = extractor.visit(returnStatement.getOp());
	}

	@Override
	public void caseDefault(final Unit unit) {
		throw new IllegalStateException("Unable to convert statement " + unit);
	}

	@Override
	public Expression result() {
		return result;
	}
}
