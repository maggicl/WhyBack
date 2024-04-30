package byteback.whyml.vimp.expr;

import byteback.analysis.JimpleStmtSwitch;
import byteback.whyml.syntax.expr.Expression;
import soot.Unit;
import soot.jimple.IdentityStmt;
import soot.jimple.ReturnStmt;

public class PureBodyExtractor extends JimpleStmtSwitch<Expression> {
	private final PureExpressionExtractor extractor;

	private Expression result;

	public PureBodyExtractor(PureExpressionExtractor extractor) {
		this.extractor = extractor;
	}

	@Override
	public void caseIdentityStmt(final IdentityStmt identity) {
		// IMPORTANT: a spec function is a static method with a single return statement. However, soot declares each
		// parameter as a local variable with an identity statement in the form "<var>: @parameter<n>" where `<var>` is
		// either the local variable name of the parameter if the local variable table has been preserved, or an
		// auto-generated name. For the translation process we simply declare `<var>` as the WhyML parameter name, so
		// we don't need to process these assignments
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
