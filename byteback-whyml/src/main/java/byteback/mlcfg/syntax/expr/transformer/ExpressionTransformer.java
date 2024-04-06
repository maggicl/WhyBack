package byteback.mlcfg.syntax.expr.transformer;

import byteback.mlcfg.syntax.expr.BooleanLiteral;
import byteback.mlcfg.syntax.expr.ClassCastExpression;
import byteback.mlcfg.syntax.expr.ConditionalExpression;
import byteback.mlcfg.syntax.expr.DoubleLiteral;
import byteback.mlcfg.syntax.expr.Expression;
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
		return new InstanceOfExpression(source.getReference().accept(this), source.getType());
	}

	public Expression transformClassCastExpression(ClassCastExpression source) {
		return new ClassCastExpression(source.getReference().accept(this), source.getType());
	}

	public Expression transformOldReference(OldReference source) {
		return new OldReference(source.getInner().accept(this));
	}

	public Expression transformDoubleLiteral(DoubleLiteral source) {
		return source;
	}

	public Expression transformFieldExpression(FieldExpression source) {
		final Operation op = source.getOperation() instanceof Operation.Put put
				? Operation.put(put.getValue().accept(this))
				: source.getOperation();

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
				source.function(),
				source.params().stream()
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
				: source.getOperation();

		return new ArrayExpression(source.getBase().accept(this), source.getElementType(), op);
	}

	public Expression transformLocalVariableExpression(LocalVariableExpression source) {
		return source;
	}
}
