package byteback.whyml.syntax.statement.visitor;

import byteback.whyml.syntax.expr.transformer.ExpressionVisitor;
import byteback.whyml.syntax.statement.ArrayAssignment;
import byteback.whyml.syntax.statement.CFGLogicalStatement;
import byteback.whyml.syntax.statement.CFGTerminator;
import byteback.whyml.syntax.statement.FieldAssignment;
import byteback.whyml.syntax.statement.LocalAssignment;

public class StatementVisitor extends ExpressionVisitor {
	public void visitArrayAssignmentStatement(ArrayAssignment arrayAssignment) {
		arrayAssignment.base().accept(this);
		arrayAssignment.index().accept(this);
		arrayAssignment.value().accept(this);
	}

	public void visitLogicalStatement(CFGLogicalStatement cfgLogicalStatement) {
		// visit this no matter what to capture call dependency. Logical statements do not have other side effects by definition
		cfgLogicalStatement.expression().accept(this);
	}

	public void visitFieldAssignmentStatement(FieldAssignment fieldAssignment) {
		fieldAssignment.value().accept(this);
	}

	public void visitLocalAssignmentStatement(LocalAssignment localAssignment) {
		localAssignment.rValue().accept(this);
	}

	public void visitReturnStatement(CFGTerminator.Return aReturn) {
		aReturn.value().accept(this);
	}

	public void visitThrowStatement(CFGTerminator.Throw aThrow) {
		aThrow.value().accept(this);
	}

	public void visitGotoStatement(CFGTerminator.Goto aGoto) {
	}

	public void visitIfStatement(CFGTerminator.If anIf) {
		anIf.expression().accept(this);
	}
}
