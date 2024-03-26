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

public class ExpressionTransformer {
	public FloatLiteral transformFloatLiteral(FloatLiteral source) {
		return source;
	}

	public BinaryExpression transformBinaryExpression(BinaryExpression source) {
		return new BinaryExpression(source.getOperator(),
				source.getFirstOperand().visit(this),
				source.getSecondOperand().visit(this));
	}

	public InstanceOfExpression transformInstanceOfExpression(InstanceOfExpression source) {
		return new InstanceOfExpression(source.getReference().visit(this), source.getType());
	}

	public ClassCastExpression transformClassCastExpression(ClassCastExpression source) {
		return new ClassCastExpression(source.getReference().visit(this), source.getType());
	}

	public OldReference transformOldReference(OldReference source) {
		return new OldReference(source.getInner().visit(this));
	}

	public DoubleLiteral transformDoubleLiteral(DoubleLiteral source) {
		return source;
	}

	public FieldExpression transformFieldExpression(FieldExpression source) {
		final Operation op = source.getOperation() instanceof Operation.Put put
				? Operation.put(put.getValue().visit(this))
				: source.getOperation();

		final Access access = source.getAccess() instanceof Access.Instance instance ?
				Access.instance(instance.getBase().visit(this), instance.getField()) :
				source.getAccess();

		return new FieldExpression(op, access);
	}

	public QuantifierExpression transformQuantifierExpression(QuantifierExpression source) {
		return new QuantifierExpression(source.getKind(),
				source.getVariableList(),
				source.getInner().visit(this));
	}

	public UnaryExpression transformUnaryExpression(UnaryExpression source) {
		return new UnaryExpression(source.getOperator(), source.getOperand().visit(this));
	}

	public UnitLiteral transformUnitLiteral(UnitLiteral source) {
		return source;
	}

	public ConditionalExpression transformConditionalExpression(ConditionalExpression source) {
		return new ConditionalExpression(
				source.getConditional().visit(this),
				source.getThenExpr().visit(this),
				source.getElseExpr().visit(this)
		);
	}

	public BooleanLiteral transformBooleanLiteral(BooleanLiteral source) {
		return source;
	}

	public FunctionCall transformFunctionCall(FunctionCall source) {
		return new FunctionCall(
				source.getFunction(),
				source.getParams().stream()
						.map(e -> e.visit(this))
						.toList());
	}

	public WholeNumberLiteral transformWholeNumberLiteral(WholeNumberLiteral source) {
		return source;
	}

	public PrimitiveCastExpression transformPrimitiveCastExpression(PrimitiveCastExpression source) {
		return new PrimitiveCastExpression(source.getInner().visit(this), source.getTargetType());
	}

	public NullLiteral transformNullLiteral(NullLiteral source) {
		return source;
	}

	public StringLiteralExpression transformStringLiteralExpression(StringLiteralExpression source) {
		return source;
	}

	public ArrayExpression transformArrayExpression(ArrayExpression source) {
		final ArrayOperation op = source.getOperation() instanceof ArrayOperation.Store store
				? ArrayOperation.store(store.getIndex().visit(this), store.getValue().visit(this))
				: source.getOperation() instanceof ArrayOperation.Load load
				? ArrayOperation.load(load.getIndex().visit(this))
				: source.getOperation();

		return new ArrayExpression(source.getBase().visit(this), source.getElementType(), op);
	}

	public LocalVariableExpression transformLocalVariableExpression(LocalVariableExpression source) {
		return source;
	}
}
