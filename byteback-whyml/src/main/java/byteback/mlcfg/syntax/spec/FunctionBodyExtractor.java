//package byteback.mlcfg.syntax.spec;
//
//import byteback.analysis.JimpleStmtSwitch;
//import byteback.frontend.boogie.ast.Expression;
//import soot.Unit;
//import soot.Value;
//import soot.jimple.IdentityStmt;
//import soot.jimple.ReturnStmt;
//
//public class FunctionBodyExtractor extends JimpleStmtSwitch<Expression> {
//
//	private Expression result;
//
//	@Override
//	public void caseIdentityStmt(final IdentityStmt identity) {
//	}
//
//	@Override
//	public void caseReturnStmt(final ReturnStmt returnStatement) {
//		final Value operand = returnStatement.getOp();
//		result = new FunctionExpressionExtractor().visit(operand);
//	}
//
//	@Override
//	public void caseDefault(final Unit unit) {
//		throw new IllegalStateException("Unable to convert statement " + unit);
//	}
//
//	@Override
//	public Expression result() {
//		return result;
//	}
//
//}
