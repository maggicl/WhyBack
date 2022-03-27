package byteback.core.converter.soot.boogie;

import byteback.core.converter.soot.SootLocalExtractor;
import byteback.core.representation.soot.body.SootExpression;
import byteback.core.representation.soot.body.SootStatementVisitor;
import byteback.core.representation.soot.type.SootType;
import byteback.core.util.CountingMap;
import byteback.frontend.boogie.ast.*;
import java.util.Map.Entry;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.*;
import soot.jimple.*;

public class FunctionBodyExtractor extends SootStatementVisitor<Expression> {

	private static final Logger log = LoggerFactory.getLogger(FunctionBodyExtractor.class);

	private final CountingMap<Local, Optional<Expression>> expressionIndex;

	private final SootType returnType;

	private Expression result;

	public FunctionBodyExtractor(final SootType returnType) {
		this.returnType = returnType;
		this.expressionIndex = new CountingMap<>();
	}

	@Override
	public void caseAssignStmt(final AssignStmt assignment) {
		final SootExpression left = new SootExpression(assignment.getLeftOp());
		final SootExpression right = new SootExpression(assignment.getRightOp());
		final Local local = new SootLocalExtractor().visit(left);
		final Expression expression = new InlineExtractor(left.getType(), expressionIndex).visit(right);
		expressionIndex.put(local, Optional.of(expression));
	}

	@Override
	public void caseReturnStmt(final ReturnStmt returnStatement) {
		final SootExpression operand = new SootExpression(returnStatement.getOp());
		final Expression expression = new InlineExtractor(returnType, expressionIndex).visit(operand);

		for (Entry<Local, Integer> entry : expressionIndex.getAccessCount().entrySet()) {
			if (entry.getValue() == 0) {
				log.warn("Local assignment {} unused in final expansion", entry.getKey());
			}
		}

		result = expression;
	}

	@Override
	public void caseDefault(final Unit unit) {
		throw new UnsupportedOperationException("Cannot inline statements of type " + unit.getClass().getName());
	}

	@Override
	public Expression result() {
		return result;
	}

}
