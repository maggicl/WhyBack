package byteback.whyml.syntax.statement.visitor;

import byteback.whyml.syntax.expr.transformer.ExpressionVisitor;
import byteback.whyml.syntax.statement.ArrayAssignment;
import byteback.whyml.syntax.statement.LocalAssignment;
import byteback.whyml.syntax.statement.CFGLogicalStatement;
import byteback.whyml.syntax.statement.CFGTerminator;
import byteback.whyml.syntax.statement.FieldAssignment;

public class StatementVisitor extends ExpressionVisitor {
	public void visitArrayAssignmentStatement(ArrayAssignment arrayAssignment) {
		arrayAssignment.base().accept(this);
		arrayAssignment.index().accept(this);
		arrayAssignment.value().accept(this);
	}

	public void visitLogicalStatement(CFGLogicalStatement cfgLogicalStatement) {
		// Do not visit the expression here as it is part of the spec code, not program code, and thus does not include
		// side effects
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

	public void visitSwitchStatement(CFGTerminator.Switch aSwitch) {
		aSwitch.test().accept(this);
	}
}
