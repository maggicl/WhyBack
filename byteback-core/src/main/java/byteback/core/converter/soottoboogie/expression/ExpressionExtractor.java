package byteback.core.converter.soottoboogie.expression;

import byteback.core.converter.soottoboogie.*;
import byteback.core.converter.soottoboogie.field.FieldConverter;
import byteback.core.converter.soottoboogie.type.*;
import byteback.core.representation.soot.type.*;
import byteback.frontend.boogie.ast.*;
import java.util.function.Function;
import java.util.stream.Stream;

import soot.BooleanType;
import soot.IntType;
import soot.Local;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Value;
import soot.jimple.*;

public class ExpressionExtractor extends ExpressionVisitor {

	public static final String LOCAL_PREFIX = "_";

	public static String localName(final Local local) {
		return LOCAL_PREFIX + local.getName();
	}

	public ExpressionExtractor(final Type type) {
		super(type);
	}

	@Override
	public ExpressionVisitor makeExpressionVisitor(final Type type) {
		return new ExpressionExtractor(type);
	}

	@Override
	public void caseStaticInvokeExpr(final StaticInvokeExpr invoke) {
		final SootMethod method = invoke.getMethod();
		final Iterable<Value> arguments = invoke.getArgs();
		pushFunctionReference(method, arguments);
	}

	@Override
	public void caseInstanceInvokeExpr(final InstanceInvokeExpr invoke) {
		final SootMethod method = invoke.getMethod();
		final Value base = invoke.getBase();
		final Iterable<Value> arguments = Stream.concat(Stream.of(base), invoke.getArgs().stream())::iterator;
		pushFunctionReference(method, arguments);
	}

	public void pushCmpExpression(final BinopExpr cmp) {
		setSpecialBinaryExpression(cmp, Prelude.v().getCmpFunction().makeFunctionReference());
	}

	@Override
	public void caseAddExpr(final AddExpr addition) {
		setBinaryExpression(addition, new AdditionOperation());
	}

	@Override
	public void caseSubExpr(final SubExpr subtraction) {
		setBinaryExpression(subtraction, new SubtractionOperation());
	}

	@Override
	public void caseDivExpr(final DivExpr division) {
		Type.toMachineType(getType()).apply(new SootTypeVisitor<>() {

			@Override
			public void caseIntType(final IntType integerType) {
				setBinaryExpression(division, new IntegerDivisionOperation());
			}

			@Override
			public void caseDefault(final Type integerType) {
				setBinaryExpression(division, new RealDivisionOperation());
			}

		});
	}

	@Override
	public void caseMulExpr(final MulExpr multiplication) {
		setBinaryExpression(multiplication, new MultiplicationOperation());
	}

	@Override
	public void caseRemExpr(final RemExpr modulo) {
		setBinaryExpression(modulo, new ModuloOperation());
	}

	@Override
	public void caseNegExpr(final NegExpr negation) {
		final Value operand = negation.getOp();
		final Expression expression = visit(operand);
		getType().apply(new SootTypeVisitor<>() {

			@Override
			public void caseBooleanType(final BooleanType type) {
				setCastExpression(new NegationOperation(expression), operand);
			}

			@Override
			public void caseDefault(final Type type) {
				setCastExpression(new MinusOperation(expression), operand);
			}

		});
	}

	@Override
	public void caseOrExpr(final OrExpr or) {
		getType().apply(new SootTypeVisitor<>() {

			@Override
			public void caseBooleanType(final BooleanType type) {
				setBinaryExpression(or, new OrOperation());
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
				setBinaryExpression(and, new AndOperation());
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
				setBinaryExpression(xor, new NotEqualsOperation());
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
		setBinaryCastExpression(equals, new EqualsOperation());
	}

	@Override
	public void caseNeExpr(final NeExpr notEquals) {
		setBinaryCastExpression(notEquals, new NotEqualsOperation());
	}

	@Override
	public void caseGtExpr(final GtExpr greaterThan) {
		setBinaryCastExpression(greaterThan, new GreaterThanOperation());
	}

	@Override
	public void caseGeExpr(final GeExpr greaterEquals) {
		setBinaryCastExpression(greaterEquals, new GreaterThanEqualsOperation());
	}

	@Override
	public void caseLtExpr(final LtExpr lessThan) {
		setBinaryCastExpression(lessThan, new LessThanOperation());
	}

	@Override
	public void caseLeExpr(final LeExpr lessEquals) {
		setBinaryCastExpression(lessEquals, new LessThanEqualsOperation());
	}

	@Override
	public void caseCastExpr(final CastExpr casting) {
		final Value operand = casting.getOp();
		final Type toType = casting.getCastType();
		final Type fromType = casting.getType();
		final Function<Expression, Expression> caster = new CasterProvider(toType).visit(fromType);

		setExpression(caster.apply(visit(operand, fromType)));
	}

	@Override
	public void caseIntConstant(final IntConstant intConstant) {
		getType().apply(new SootTypeVisitor<>() {

			@Override
			public void caseBooleanType(final BooleanType type) {
				setExpression(intConstant.value != 0 ? BooleanLiteral.makeTrue() : BooleanLiteral.makeFalse());
			}

			@Override
			public void caseDefault(final Type type) {
				setExpression(new NumberLiteral(intConstant.toString()));
			}

		});
	}

	@Override
	public void caseLongConstant(final LongConstant longConstant) {
		final String literal = longConstant.toString();
		final String strippedLiteral = literal.substring(0, literal.length() - 1);
		setExpression(new NumberLiteral(strippedLiteral));
	}

	@Override
	public void caseDoubleConstant(final DoubleConstant doubleConstant) {
		setExpression(new RealLiteral(doubleConstant.toString()));
	}

	@Override
	public void caseFloatConstant(final FloatConstant floatConstant) {
		final String literal = floatConstant.toString();
		final String strippedLiteral = literal.substring(0, literal.length() - 1);
		setExpression(new RealLiteral(strippedLiteral));
	}

	@Override
	public void caseNullConstant(final NullConstant nullConstant) {
		setExpression(Prelude.v().getNullConstant().makeValueReference());
	}

	@Override
	public void caseLocal(final Local local) {
		setCastExpression(ValueReference.of(localName(local)), local);
	}

	@Override
	public void caseInstanceFieldRef(final InstanceFieldRef instanceFieldRef) {
		final SootField field = instanceFieldRef.getField();
		final Value base = instanceFieldRef.getBase();
		final Expression reference = ValueReference.of(FieldConverter.fieldName(field));
		final Expression heapAccess = Prelude.v().makeHeapAccessExpression(visit(base), reference);
		setCastExpression(heapAccess, field.getType());
	}

	@Override
	public void caseStaticFieldRef(final StaticFieldRef staticFieldReference) {
		final SootField field = staticFieldReference.getField();
		final SootClass base = field.getDeclaringClass();
		final Expression reference = ValueReference.of(FieldConverter.fieldName(field));
		final Expression heapAccess = Prelude.v()
				.makeStaticAccessExpression(ValueReference.of(ReferenceTypeConverter.typeName(base)), reference);
		setCastExpression(heapAccess, field.getType());
	}

	@Override
	public void caseArrayRef(final ArrayRef arrayReference) {
		final Value base = arrayReference.getBase();
		final Type type = arrayReference.getType();
		final var index = arrayReference.getIndex();
		final TypeAccess typeAccess = new TypeAccessExtractor().visit(type);
		setCastExpression(Prelude.v().makeArrayAccessExpression(typeAccess, visit(base, base.getType()), visit(index, IntType.v())), type);
	}

	@Override
	public void caseLengthExpr(final LengthExpr length) {
		final Value operand = length.getOp();
		setExpression(Prelude.v().getLengthAccessExpression(visit(operand, operand.getType())));
	}

	@Override
	public void caseInstanceOfExpr(final InstanceOfExpr instanceOf) {
		final Value left = instanceOf.getOp();
		final SymbolicReference typeReference = new TypeReferenceExtractor().visit(instanceOf.getCheckType());
		setCastExpression(Prelude.v().makeTypeCheckExpression(ExpressionExtractor.this.visit(left), typeReference), instanceOf.getType());
	}

	@Override
	public void caseDefault(final Value value) {
		throw new ExpressionConversionException(value,
				"Unable to convert expression of type " + value.getClass().getName());
	}

}
