package byteback.mlcfg.syntax.expr.transformer;

import byteback.mlcfg.syntax.expr.BooleanLiteral;
import byteback.mlcfg.syntax.expr.ClassCastExpression;
import byteback.mlcfg.syntax.expr.ConditionalExpression;
import byteback.mlcfg.syntax.expr.DoubleLiteral;
import byteback.mlcfg.syntax.expr.FloatLiteral;
import byteback.mlcfg.syntax.expr.FunctionCall;
import byteback.mlcfg.syntax.expr.InstanceOfExpression;
import byteback.mlcfg.syntax.expr.LocalVariableExpression;
import byteback.mlcfg.syntax.expr.NullLiteral;
import byteback.mlcfg.syntax.expr.OldReference;
import byteback.mlcfg.syntax.expr.PrimitiveCastExpression;
import byteback.mlcfg.syntax.expr.QuantifierExpression;
import byteback.mlcfg.syntax.expr.StringLiteralExpression;
import byteback.mlcfg.syntax.expr.UnaryExpression;
import byteback.mlcfg.syntax.expr.UnitLiteral;
import byteback.mlcfg.syntax.expr.WholeNumberLiteral;
import byteback.mlcfg.syntax.expr.binary.BinaryExpression;
import byteback.mlcfg.syntax.expr.field.Access;
import byteback.mlcfg.syntax.expr.field.ArrayExpression;
import byteback.mlcfg.syntax.expr.field.ArrayOperation;
import byteback.mlcfg.syntax.expr.field.FieldExpression;
import byteback.mlcfg.syntax.expr.field.Operation;

public class ExpressionVisitor {
	public void visitFloatLiteral(FloatLiteral source) {
	}

	public void visitBinaryExpression(BinaryExpression source) {
		source.getFirstOperand().accept(this);
		source.getSecondOperand().accept(this);
	}

	public void visitInstanceOfExpression(InstanceOfExpression source) {
		source.getReference().accept(this);
	}

	public void visitClassCastExpression(ClassCastExpression source) {
		source.getReference().accept(this);
	}

	public void visitOldReference(OldReference source) {
		source.getInner().accept(this);
	}

	public void visitDoubleLiteral(DoubleLiteral source) {
	}

	public void visitFieldExpression(FieldExpression source) {
		if (source.getOperation() instanceof Operation.Put put) {
			put.getValue().accept(this);
		}

		if (source.getAccess() instanceof Access.Instance instance) {
			instance.getBase().accept(this);
		}
	}

	public void visitQuantifierExpression(QuantifierExpression source) {
		source.getInner().accept(this);
	}

	public void visitUnaryExpression(UnaryExpression source) {
		source.getOperand().accept(this);
	}

	public void visitUnitLiteral(UnitLiteral source) {
	}

	public void visitConditionalExpression(ConditionalExpression source) {
		source.getConditional().accept(this);
		source.getThenExpr().accept(this);
		source.getElseExpr().accept(this);
	}

	public void visitBooleanLiteral(BooleanLiteral source) {
	}

	public void visitFunctionCall(FunctionCall source) {
		source.params().forEach(e -> e.accept(this));
	}

	public void visitWholeNumberLiteral(WholeNumberLiteral source) {
	}

	public void visitPrimitiveCastExpression(PrimitiveCastExpression source) {
		source.getInner().accept(this);
	}

	public void visitNullLiteral(NullLiteral source) {
	}

	public void visitStringLiteralExpression(StringLiteralExpression source) {
	}

	public void visitArrayExpression(ArrayExpression source) {
		source.getBase().accept(this);

		if (source.getOperation() instanceof ArrayOperation.Store store) {
			store.getIndex().accept(this);
			store.getValue().accept(this);
		} else if (source.getOperation() instanceof ArrayOperation.Load load) {
			load.getIndex().accept(this);
		}
	}

	public void visitLocalVariableExpression(LocalVariableExpression source) {
	}
}
