package byteback.core.converter.soot.boogie;

import byteback.core.converter.soot.LocalExtractor;
import byteback.core.representation.soot.body.SootExpression;
import byteback.core.representation.soot.body.SootStatementVisitor;
import byteback.core.representation.soot.type.SootType;
import byteback.core.util.CountingMap;
import byteback.frontend.boogie.ast.*;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.*;
import soot.jimple.*;

public class FunctionBodyExtractor extends SootStatementVisitor<Expression> {

	private static final Logger log = LoggerFactory.getLogger(FunctionBodyExtractor.class);

	private final CountingMap<Local, Expression> expressionTable;

	private final Substituter inliningTable;

	private final SootType returnType;

	private Expression result;

	public FunctionBodyExtractor(final SootType returnType) {
		this.returnType = returnType;
		this.expressionTable = new CountingMap<>();
		this.inliningTable = new Substituter(this.expressionTable);
	}

	@Override
	public void caseAssignStmt(final AssignStmt assignment) {
		final SootExpression left = new SootExpression(assignment.getLeftOp());
		final SootExpression right = new SootExpression(assignment.getRightOp());
		final Local local = new LocalExtractor().visit(left);
		final Expression boogieExpression = new SubstitutingExtractor(inliningTable).visit(right,
				left.getType());
		inliningTable.put(local, boogieExpression);
	}

	@Override
	public void caseReturnStmt(final ReturnStmt returnStatement) {
		final SootExpression operand = new SootExpression(returnStatement.getOp());
		result = new SubstitutingExtractor(inliningTable).visit(operand, returnType);

		for (Entry<Local, Integer> entry : expressionTable.getAccessCount().entrySet()) {
			if (entry.getValue() == 0) {
				log.warn("Local assignment {} unused in final expansion", entry.getKey());
			}
		}
	}

	@Override
	public void caseDefault(final Unit unit) {
		throw new UnsupportedOperationException("Cannot substitute statement " + unit);
	}

	@Override
	public Expression result() {
		return result;
	}

}
