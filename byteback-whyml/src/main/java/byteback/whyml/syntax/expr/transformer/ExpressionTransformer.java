package byteback.whyml.syntax.expr.transformer;

import byteback.whyml.syntax.expr.BooleanLiteral;
import byteback.whyml.syntax.expr.ClassCastExpression;
import byteback.whyml.syntax.expr.ClassLiteralExpression;
import byteback.whyml.syntax.expr.ConditionalExpression;
import byteback.whyml.syntax.expr.DoubleLiteral;
import byteback.whyml.syntax.expr.Expression;
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
		return new InstanceOfExpression(source.reference().accept(this), source.checkType(), source.assertNotNull());
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
		final Access access = source.access() instanceof Access.Instance instance ?
				Access.instance(instance.getBase().accept(this), instance.getField()) :
				source.access();

		return new FieldExpression(source.operation(), access);
	}

	public Expression transformQuantifierExpression(QuantifierExpression source) {
		return new QuantifierExpression(source.kind(),
				source.variable(),
				source.inner().accept(this));
	}

	public Expression transformUnaryExpression(UnaryExpression source) {
		return new UnaryExpression(source.operator(), source.operand().accept(this));
	}

	public Expression transformUnitLiteral(UnitLiteral source) {
		return source;
	}

	public Expression transformConditionalExpression(ConditionalExpression source) {
		return new ConditionalExpression(
				source.conditional().accept(this),
				source.thenExpr().accept(this),
				source.elseExpr().accept(this)
		);
	}

	public Expression transformBooleanLiteral(BooleanLiteral source) {
		return source;
	}

	public Expression transformFunctionCall(FunctionCall source) {
		return new FunctionCall(
				source.name(),
				source.signature(),
				source.actualParams().stream()
						.map(e -> e.accept(this))
						.toList());
	}

	public Expression transformWholeNumberLiteral(WholeNumberLiteral source) {
		return source;
	}

	public Expression transformPrimitiveCastExpression(PrimitiveCastExpression source) {
		return new PrimitiveCastExpression(source.inner().accept(this), source.targetType());
	}

	public Expression transformNullLiteral(NullLiteral source) {
		return source;
	}

	public Expression transformStringLiteralExpression(StringLiteralExpression source) {
		return source;
	}

	public Expression transformArrayExpression(ArrayExpression source) {
		final ArrayOperation op = source.operation() instanceof ArrayOperation.Load load
				? ArrayOperation.load(load.getIndex().accept(this))
				: source.operation() instanceof ArrayOperation.IsElem isElem
				? ArrayOperation.isElem(isElem.getIndex().accept(this))
				: source.operation();

		return new ArrayExpression(source.base().accept(this), source.elementType(), op);
	}

	public Expression transformLocalVariableExpression(LocalExpression source) {
		return source;
	}

	public Expression transformNewArrayExpression(NewArrayExpression newArrayExpression) {
		return new NewArrayExpression(newArrayExpression.baseType(), newArrayExpression.size().accept(this));
	}

	public Expression transformNewExpression(NewExpression newExpression) {
		return newExpression;
	}

	public Expression transformClassLiteralExpression(ClassLiteralExpression classLiteralExpression) {
		return classLiteralExpression;
	}
}
