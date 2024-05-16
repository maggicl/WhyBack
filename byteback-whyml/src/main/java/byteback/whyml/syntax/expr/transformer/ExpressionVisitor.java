package byteback.whyml.syntax.expr.transformer;

import byteback.whyml.syntax.expr.BooleanLiteral;
import byteback.whyml.syntax.expr.ClassCastExpression;
import byteback.whyml.syntax.expr.ClassLiteralExpression;
import byteback.whyml.syntax.expr.ConditionalExpression;
import byteback.whyml.syntax.expr.DoubleLiteral;
import byteback.whyml.syntax.expr.FloatLiteral;
import byteback.whyml.syntax.expr.FunctionCall;
import byteback.whyml.syntax.expr.InstanceOfExpression;
import byteback.whyml.syntax.expr.LocalExpression;
import byteback.whyml.syntax.expr.NewArrayExpression;
import byteback.whyml.syntax.expr.NewExpression;
import byteback.whyml.syntax.expr.NullLiteral;
import byteback.whyml.syntax.expr.OldReference;
import byteback.whyml.syntax.expr.PrimitiveCastExpression;
import byteback.whyml.syntax.expr.QuantifierExpression;
import byteback.whyml.syntax.expr.StringLiteralExpression;
import byteback.whyml.syntax.expr.UnaryExpression;
import byteback.whyml.syntax.expr.UnitLiteral;
import byteback.whyml.syntax.expr.WholeNumberLiteral;
import byteback.whyml.syntax.expr.binary.BinaryExpression;
import byteback.whyml.syntax.expr.field.Access;
import byteback.whyml.syntax.expr.field.ArrayExpression;
import byteback.whyml.syntax.expr.field.ArrayOperation;
import byteback.whyml.syntax.expr.field.FieldExpression;

public class ExpressionVisitor {
	public void visitFloatLiteral(FloatLiteral source) {
	}

	public void visitBinaryExpression(BinaryExpression source) {
		source.getFirstOperand().accept(this);
		source.getSecondOperand().accept(this);
	}

	public void visitInstanceOfExpression(InstanceOfExpression source) {
		source.reference().accept(this);
	}

	public void visitClassCastExpression(ClassCastExpression source) {
		source.reference().accept(this);
	}

	public void visitOldReference(OldReference source) {
		source.inner().accept(this);
	}

	public void visitDoubleLiteral(DoubleLiteral source) {
	}

	public void visitFieldExpression(FieldExpression source) {
		if (source.access() instanceof Access.Instance instance) {
			instance.getBase().accept(this);
		}
	}

	public void visitQuantifierExpression(QuantifierExpression source) {
		source.inner().accept(this);
	}

	public void visitUnaryExpression(UnaryExpression source) {
		source.operand().accept(this);
	}

	public void visitUnitLiteral(UnitLiteral source) {
	}

	public void visitConditionalExpression(ConditionalExpression source) {
		source.conditional().accept(this);
		source.thenExpr().accept(this);
		source.elseExpr().accept(this);
	}

	public void visitBooleanLiteral(BooleanLiteral source) {
	}

	public void visitFunctionCall(FunctionCall source) {
		source.actualParams().forEach(e -> e.accept(this));
	}

	public void visitWholeNumberLiteral(WholeNumberLiteral source) {
	}

	public void visitPrimitiveCastExpression(PrimitiveCastExpression source) {
		source.inner().accept(this);
	}

	public void visitNullLiteral(NullLiteral source) {
	}

	public void visitStringLiteralExpression(StringLiteralExpression source) {
	}

	public void visitArrayExpression(ArrayExpression source) {
		source.base().accept(this);

		if (source.operation() instanceof ArrayOperation.AbstractLoad load) {
			load.getIndex().accept(this);
		}
	}

	public void visitLocalVariableExpression(LocalExpression source) {
	}

	public void visitNewArrayExpression(NewArrayExpression newArrayExpression) {
		newArrayExpression.size().accept(this);
	}

	public void visitNewExpression(NewExpression newExpression) {
	}

	public void visitClassLiteralExpression(ClassLiteralExpression classLiteralExpression) {
	}
}
