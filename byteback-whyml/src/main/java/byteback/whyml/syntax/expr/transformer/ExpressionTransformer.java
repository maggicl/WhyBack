package byteback.whyml.syntax.expr.transformer;

import byteback.whyml.syntax.expr.BooleanLiteral;
import byteback.whyml.syntax.expr.ClassCastExpression;
import byteback.whyml.syntax.expr.ConditionalExpression;
import byteback.whyml.syntax.expr.DoubleLiteral;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.expr.FloatLiteral;
import byteback.whyml.syntax.expr.FunctionCall;
import byteback.whyml.syntax.expr.InstanceOfExpression;
import byteback.whyml.syntax.expr.LocalVariableExpression;
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
import byteback.whyml.syntax.expr.field.Operation;

public class ExpressionTransformer {
	public Expression transformFloatLiteral(FloatLiteral source) {
		return source;
	}

	public Expression transformBinaryExpression(BinaryExpression source) {
		return new BinaryExpression(source.getOperator(),
				source.getFirstOperand().accept(this),
				source.getSecondOperand().accept(this));
	}

	public Expression transformInstanceOfExpression(InstanceOfExpression source) {
		return new InstanceOfExpression(source.reference().accept(this), source.checkType());
	}

	public Expression transformClassCastExpression(ClassCastExpression source) {
		return new ClassCastExpression(source.reference().accept(this), source.exactType(), source.forSpec());
	}

	public Expression transformOldReference(OldReference source) {
		return new OldReference(source.inner().accept(this));
	}

	public Expression transformDoubleLiteral(DoubleLiteral source) {
		return source;
	}

	public Expression transformFieldExpression(FieldExpression source) {
		final Operation op = source.getOperation() instanceof Operation.Put put
				? Operation.put(put.getValue().accept(this))
				: source.getOperation(); // both get and is do not have parameters

		final Access access = source.getAccess() instanceof Access.Instance instance ?
				Access.instance(instance.getBase().accept(this), instance.getField()) :
				source.getAccess();

		return new FieldExpression(op, access);
	}

	public Expression transformQuantifierExpression(QuantifierExpression source) {
		return new QuantifierExpression(source.getKind(),
				source.getVariable(),
				source.getInner().accept(this));
	}

	public Expression transformUnaryExpression(UnaryExpression source) {
		return new UnaryExpression(source.getOperator(), source.getOperand().accept(this));
	}

	public Expression transformUnitLiteral(UnitLiteral source) {
		return source;
	}

	public Expression transformConditionalExpression(ConditionalExpression source) {
		return new ConditionalExpression(
				source.getConditional().accept(this),
				source.getThenExpr().accept(this),
				source.getElseExpr().accept(this)
		);
	}

	public Expression transformBooleanLiteral(BooleanLiteral source) {
		return source;
	}

	public Expression transformFunctionCall(FunctionCall source) {
		return new FunctionCall(
				source.name(),
				source.formalParams(),
				source.returnType(),
				source.actualParams().stream()
						.map(e -> e.accept(this))
						.toList());
	}

	public Expression transformWholeNumberLiteral(WholeNumberLiteral source) {
		return source;
	}

	public Expression transformPrimitiveCastExpression(PrimitiveCastExpression source) {
		return new PrimitiveCastExpression(source.getInner().accept(this), source.getTargetType());
	}

	public Expression transformNullLiteral(NullLiteral source) {
		return source;
	}

	public Expression transformStringLiteralExpression(StringLiteralExpression source) {
		return source;
	}

	public Expression transformArrayExpression(ArrayExpression source) {
		final ArrayOperation op = source.getOperation() instanceof ArrayOperation.Store store
				? ArrayOperation.store(store.getIndex().accept(this), store.getValue().accept(this))
				: source.getOperation() instanceof ArrayOperation.Load load
				? ArrayOperation.load(load.getIndex().accept(this))
				: source.getOperation() instanceof ArrayOperation.IsElem isElem
				? ArrayOperation.isElem(isElem.getIndex().accept(this))
				: source.getOperation();

		return new ArrayExpression(source.getBase().accept(this), source.getElementType(), op);
	}

	public Expression transformLocalVariableExpression(LocalVariableExpression source) {
		return source;
	}
}
