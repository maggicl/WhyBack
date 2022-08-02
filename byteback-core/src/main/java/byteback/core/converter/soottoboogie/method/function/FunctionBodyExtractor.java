package byteback.core.converter.soottoboogie.method.function;

import byteback.core.converter.soottoboogie.DependencyExtractor;
import byteback.core.converter.soottoboogie.LocalExtractor;
import byteback.core.converter.soottoboogie.expression.ExpressionExtractor;
import byteback.core.converter.soottoboogie.expression.Substitutor;
import byteback.core.converter.soottoboogie.statement.StatementConversionException;
import byteback.core.representation.soot.body.SootStatementVisitor;
import byteback.core.util.CountingMap;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.ValueReference;
import java.util.Map.Entry;
import soot.Local;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.*;

public class FunctionBodyExtractor extends SootStatementVisitor<Expression> {

	private final CountingMap<Local, Expression> expressionTable;

	private final Substitutor substitutor;

	private final Type returnType;

	private Expression result;

	public FunctionBodyExtractor(final Type returnType) {
		this.returnType = returnType;
		this.expressionTable = new CountingMap<>();
		this.substitutor = new Substitutor(this.expressionTable);
	}

	@Override
	public void caseAssignStmt(final AssignStmt assignment) {
		final Value left = assignment.getLeftOp();
		final Value right = assignment.getRightOp();
		final Local local = new LocalExtractor().visit(left);
		final Expression expression = new FunctionExpressionExtractor(substitutor) {

			@Override
			public void pushBinding(final SootMethod method, final Iterable<Value> argumentsIterable) {
				pushExpression(ValueReference.of(ExpressionExtractor.localName(local)));
			}

		}.visit(right, left.getType());
		substitutor.put(local, new DependencyExtractor().visit(right), expression);
	}

	@Override
	public void caseReturnStmt(final ReturnStmt returnStatement) {
		final Value operand = returnStatement.getOp();
		result = new FunctionExpressionExtractor(substitutor).visit(operand, returnType);

		for (Entry<Local, Integer> entry : expressionTable.getAccessCount().entrySet()) {
			if (entry.getValue() == 0) {
				throw new StatementConversionException(returnStatement, "Unused local in expansion " + entry.getKey());
			}
		}
	}

	@Override
	public void caseDefault(final Unit unit) {
		throw new StatementConversionException(unit, "Cannot substitute statement " + unit);
	}

	@Override
	public Expression result() {
		return result;
	}

}
