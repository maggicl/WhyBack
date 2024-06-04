package byteback.whyml.syntax.statement.visitor;

import byteback.whyml.syntax.expr.BooleanLiteral;
import byteback.whyml.syntax.expr.ClassCastExpression;
import byteback.whyml.syntax.expr.InstanceOfExpression;
import byteback.whyml.syntax.expr.NewArrayExpression;
import byteback.whyml.syntax.expr.NewExpression;
import byteback.whyml.syntax.expr.field.ArrayExpression;
import byteback.whyml.syntax.expr.field.FieldExpression;
import byteback.whyml.syntax.statement.ArrayAssignment;
import byteback.whyml.syntax.statement.CFGLogicalStatement;
import byteback.whyml.syntax.statement.FieldAssignment;

public class StatsVisitor extends StatementVisitor {
	private int heapAccessCount = 0;
	private int missingInvariantCount = 0;

	public int getHeapAccessCount() {
		return heapAccessCount;
	}

	public int getMissingInvariantCount() {
		return missingInvariantCount;
	}

	@Override
	public void visitArrayAssignmentStatement(ArrayAssignment arrayAssignment) {
		heapAccessCount++;
		super.visitArrayAssignmentStatement(arrayAssignment);
	}

	@Override
	public void visitFieldAssignmentStatement(FieldAssignment fieldAssignment) {
		heapAccessCount++;
		super.visitFieldAssignmentStatement(fieldAssignment);
	}

	@Override
	public void visitInstanceOfExpression(InstanceOfExpression source) {
		heapAccessCount++;
		super.visitInstanceOfExpression(source);
	}

	@Override
	public void visitClassCastExpression(ClassCastExpression source) {
		heapAccessCount++;
		super.visitClassCastExpression(source);
	}

	@Override
	public void visitFieldExpression(FieldExpression source) {
		heapAccessCount++;
		super.visitFieldExpression(source);
	}

	@Override
	public void visitArrayExpression(ArrayExpression source) {
		heapAccessCount++;
		super.visitArrayExpression(source);
	}

	@Override
	public void visitNewArrayExpression(NewArrayExpression newArrayExpression) {
		heapAccessCount++;
		super.visitNewArrayExpression(newArrayExpression);
	}

	@Override
	public void visitNewExpression(NewExpression newExpression) {
		heapAccessCount++;
		super.visitNewExpression(newExpression);
	}

	@Override
	public void visitLogicalStatement(CFGLogicalStatement cfgLogicalStatement) {
		if (cfgLogicalStatement.kind() == CFGLogicalStatement.Kind.INVARIANT
				&& cfgLogicalStatement.expression() == BooleanLiteral.TRUE) {
			missingInvariantCount++;
		}

		super.visitLogicalStatement(cfgLogicalStatement);
	}
}
