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
