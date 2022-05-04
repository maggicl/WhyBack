package byteback.core.converter.soottoboogie.expression;

import byteback.core.converter.soottoboogie.*;
import byteback.core.converter.soottoboogie.field.FieldConverter;
import byteback.core.converter.soottoboogie.type.*;
import byteback.core.representation.soot.body.*;
import byteback.core.representation.soot.type.*;
import byteback.core.representation.soot.unit.*;
import byteback.frontend.boogie.ast.*;
import java.util.function.Function;
import java.util.stream.Stream;
import soot.BooleanType;
import soot.IntType;
import soot.Local;
import soot.RefType;
import soot.Type;
import soot.Value;
import soot.jimple.*;

public class ExpressionExtractor extends ExpressionVisitor {

	public static String localName(final Local local) {
		return "$" + local.getName();
	}

	@Override
	public void caseStaticInvokeExpr(final StaticInvokeExpr invoke) {
		final var method = new SootMethod(invoke.getMethod());
		final Iterable<SootExpression> arguments = invoke.getArgs().stream().map(SootExpression::new)::iterator;
		pushFunctionReference(method, arguments);
	}

	@Override
	public void caseInstanceInvokeExpr(final InstanceInvokeExpr invoke) {
		final var method = new SootMethod(invoke.getMethod());
		final var base = new SootExpression(invoke.getBase());
		final Iterable<SootExpression> arguments = Stream.concat(Stream.of(base),
				invoke.getArgs().stream().map(SootExpression::new))::iterator;
		pushFunctionReference(method, arguments);
	}

	public void pushCmpExpression(final BinopExpr cmp) {
		pushSpecialBinaryExpression(cmp, Prelude.instance().getCmpFunction().makeFunctionReference());
	}

	@Override
	public void caseAddExpr(final AddExpr addition) {
		pushBinaryExpression(addition, new AdditionOperation());
	}

	@Override
	public void caseSubExpr(final SubExpr subtraction) {
		pushBinaryExpression(subtraction, new SubtractionOperation());
	}

	@Override
	public void caseDivExpr(final DivExpr division) {
		getType().getMachineType().apply(new SootTypeVisitor<>() {

			@Override
			public void caseIntType(final IntType integerType) {
				pushBinaryExpression(division, new IntegerDivisionOperation());
			}

			@Override
			public void caseDefault(final Type integerType) {
				pushBinaryExpression(division, new RealDivisionOperation());
			}

		});
	}

	@Override
	public void caseMulExpr(final MulExpr multiplication) {
		pushBinaryExpression(multiplication, new MultiplicationOperation());
	}

	@Override
	public void caseRemExpr(final RemExpr modulo) {
		pushBinaryExpression(modulo, new ModuloOperation());
	}

	@Override
	public void caseNegExpr(final NegExpr negation) {
		final SootExpression operand = new SootExpression(negation.getOp());
		final Expression expression = visit(operand);
		getType().apply(new SootTypeVisitor<>() {

			@Override
			public void caseBooleanType(final BooleanType type) {
				pushExpression(new NegationOperation(expression));
			}

			@Override
			public void caseDefault(final Type type) {
				pushExpression(new MinusOperation(expression));
			}

		});
	}

	@Override
	public void caseOrExpr(final OrExpr or) {
		getType().apply(new SootTypeVisitor<>() {

			@Override
			public void caseBooleanType(final BooleanType type) {
				pushBinaryExpression(or, new OrOperation());
			}

			@Override
			public void caseDefault(final Type type) {
				throw new ExpressionConversionException(or, "Bitwise OR is currently not supported for type " + type);
			}

		});
	}

	@Override
	public void caseAndExpr(final AndExpr and) {
		getType().apply(new SootTypeVisitor<>() {

			@Override
			public void caseBooleanType(final BooleanType type) {
				pushBinaryExpression(and, new AndOperation());
			}

			@Override
			public void caseDefault(final Type type) {
				throw new ExpressionConversionException(and, "Bitwise AND is currently not supported for type " + type);
			}

		});
	}

	@Override
	public void caseXorExpr(final XorExpr xor) {
		getType().apply(new SootTypeVisitor<>() {

			@Override
			public void caseBooleanType(final BooleanType type) {
				pushBinaryExpression(xor, new NotEqualsOperation());
			}

			@Override
			public void caseDefault(final Type type) {
				throw new ExpressionConversionException(xor, "Bitwise XOR is currently not supported for type " + type);
			}

		});
	}

	@Override
	public void caseCmplExpr(final CmplExpr cmpl) {
		pushCmpExpression(cmpl);
	}

	@Override
	public void caseCmpgExpr(final CmpgExpr cmpg) {
		pushCmpExpression(cmpg);
	}

	@Override
	public void caseCmpExpr(final CmpExpr cmp) {
		pushCmpExpression(cmp);
	}

	@Override
	public void caseEqExpr(final EqExpr equals) {
		pushBinaryExpression(equals, new EqualsOperation());
	}

	@Override
	public void caseNeExpr(final NeExpr notEquals) {
		pushBinaryExpression(notEquals, new NotEqualsOperation());
	}

	@Override
	public void caseGtExpr(final GtExpr greaterThan) {
		pushBinaryExpression(greaterThan, new GreaterThanOperation());
	}

	@Override
	public void caseGeExpr(final GeExpr greaterEquals) {
		pushBinaryExpression(greaterEquals, new GreaterThanEqualsOperation());
	}

	@Override
	public void caseLtExpr(final LtExpr lessThan) {
		pushBinaryExpression(lessThan, new LessThanOperation());
	}

	@Override
	public void caseLeExpr(final LeExpr lessEquals) {
		pushBinaryExpression(lessEquals, new LessThanEqualsOperation());
	}

	@Override
	public void caseCastExpr(final CastExpr casting) {
		final var operand = new SootExpression(casting.getOp());
		final var toType = new SootType(casting.getCastType());
		final var fromType = new SootType(casting.getType());
		final Function<Expression, Expression> caster = new CasterProvider(toType).visit(fromType);
		pushExpression(caster.apply(visit(operand, fromType)));
	}

	@Override
	public void caseIntConstant(final IntConstant intConstant) {
		getType().apply(new SootTypeVisitor<>() {

			@Override
			public void caseBooleanType(final BooleanType type) {
				pushExpression(intConstant.value != 0 ? BooleanLiteral.makeTrue() : BooleanLiteral.makeFalse());
			}

			@Override
			public void caseDefault(final Type type) {
				pushExpression(new NumberLiteral(intConstant.toString()));
			}

		});
	}

	@Override
	public void caseLongConstant(final LongConstant longConstant) {
		final String literal = longConstant.toString();
		final String strippedLiteral = literal.substring(0, literal.length() - 1);
		pushExpression(new NumberLiteral(strippedLiteral));
	}

	@Override
	public void caseDoubleConstant(final DoubleConstant doubleConstant) {
		pushExpression(new RealLiteral(doubleConstant.toString()));
	}

	@Override
	public void caseFloatConstant(final FloatConstant floatConstant) {
		final String literal = floatConstant.toString();
		final String strippedLiteral = literal.substring(0, literal.length() - 1);
		pushExpression(new RealLiteral(strippedLiteral));
	}

	@Override
	public void caseNullConstant(final NullConstant nullConstant) {
		pushExpression(Prelude.instance().getNullConstant().makeValueReference());
	}

	@Override
	public void caseLocal(final Local local) {
		pushCastExpression(ValueReference.of(localName(local)), local);
	}

	@Override
	public void caseInstanceFieldRef(final InstanceFieldRef instanceFieldReference) {
		final var field = new SootField(instanceFieldReference.getField());
		final var base = new SootExpression(instanceFieldReference.getBase());
		final Expression reference = ValueReference.of(FieldConverter.fieldName(field));
		final Expression heapAccess = Prelude.instance().makeHeapAccessExpression(visit(base), reference);
		pushCastExpression(heapAccess, field.getType());
	}

	@Override
	public void caseStaticFieldRef(final StaticFieldRef staticFieldReference) {
		final var field = new SootField(staticFieldReference.getField());
		final SootClass base = field.getSootClass();
		final Expression reference = ValueReference.of(FieldConverter.fieldName(field));
		final Expression heapAccess = Prelude.instance()
				.makeStaticAccessExpression(ValueReference.of(ReferenceTypeConverter.typeName(base)), reference);
		pushCastExpression(heapAccess, field.getType());
	}

	@Override
	public void caseArrayRef(final ArrayRef arrayReference) {
		final var base = new SootExpression(arrayReference.getBase());
		final var arrayType = new SootType(arrayReference.getType());
		final var index = new SootExpression(arrayReference.getIndex());
		final TypeAccess typeAccess = new TypeAccessExtractor().visit(arrayType);
		pushCastExpression(Prelude.instance().makeArrayAccessExpression(typeAccess, visit(base), visit(index)),
				arrayType);
	}

	@Override
	public void caseLengthExpr(final LengthExpr length) {
		final var operand = new SootExpression(length.getOp());
		pushExpression(Prelude.instance().getLengthAccessExpression(visit(operand)));
	}

	@Override
	public void caseInstanceOfExpr(final InstanceOfExpr instanceOf) {
		final var left = new SootExpression(instanceOf.getOp());
		instanceOf.getCheckType().apply(new SootTypeVisitor<>() {

			@Override
			public void caseRefType(final RefType referenceType) {
				final ValueReference typeReference = ValueReference.of(referenceType.getClassName());
				pushExpression(Prelude.instance().makeTypeCheckExpression(ExpressionExtractor.this.visit(left),
						typeReference));
			}

			@Override
			public void caseDefault(final Type type) {
				throw new ConversionException("Cannot convert `instanceof` expressions for type " + type);
			}

		});
	}

	@Override
	public void caseDefault(final Value value) {
		throw new ExpressionConversionException(value,
				"Unable to convert expression of type " + value.getClass().getName());
	}

}
