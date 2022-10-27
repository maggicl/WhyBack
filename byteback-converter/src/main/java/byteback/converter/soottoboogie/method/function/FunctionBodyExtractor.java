package byteback.converter.soottoboogie.method.function;

import byteback.analysis.JimpleStmtSwitch;
import byteback.converter.soottoboogie.LocalExtractor;
import byteback.converter.soottoboogie.expression.ExpressionExtractor;
import byteback.converter.soottoboogie.statement.StatementConversionException;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.ValueReference;
import soot.Local;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.*;

public class FunctionBodyExtractor extends JimpleStmtSwitch<Expression> {

	private final Type returnType;

	private Expression result;

	public FunctionBodyExtractor(final Type returnType) {
		this.returnType = returnType;
	}

	@Override
	public void caseAssignStmt(final AssignStmt assignment) {
		final Value left = assignment.getLeftOp();
		final Value right = assignment.getRightOp();
		final Local local = new LocalExtractor().visit(left);
		new FunctionExpressionExtractor(local.getType()) {

			@Override
			public void pushBinding(final SootMethod method, final Iterable<Value> argumentsIterable) {
				setExpression(ValueReference.of(ExpressionExtractor.localName(local)));
			}

			@Override
			public void caseDefault(final Value value) {
				FunctionBodyExtractor.this.caseDefault(assignment);
			}

		}.visit(right);
	}

	@Override
	public void caseReturnStmt(final ReturnStmt returnStatement) {
		final Value operand = returnStatement.getOp();
		result = new FunctionExpressionExtractor(returnType).visit(operand);
	}

	@Override
	public void caseDefault(final Unit unit) {
		throw new StatementConversionException(unit, "Unable to convert statement " + unit);
	}

	@Override
	public Expression result() {
		return result;
	}

}
