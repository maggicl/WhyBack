package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.annotation.SootAnnotation;
import byteback.core.representation.soot.annotation.SootAnnotationElement.StringElementExtractor;
import byteback.core.representation.soot.body.SootExpression;
import byteback.core.representation.soot.body.SootExpressionVisitor;
import byteback.core.representation.soot.type.SootType;
import byteback.core.representation.soot.type.SootTypeVisitor;
import byteback.core.representation.soot.unit.SootField;
import byteback.core.representation.soot.unit.SootMethod;
import byteback.frontend.boogie.ast.Accessor;
import byteback.frontend.boogie.ast.AdditionOperation;
import byteback.frontend.boogie.ast.AndOperation;
import byteback.frontend.boogie.ast.BinaryExpression;
import byteback.frontend.boogie.ast.BooleanLiteral;
import byteback.frontend.boogie.ast.DivisionOperation;
import byteback.frontend.boogie.ast.EqualsOperation;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.FunctionReference;
import byteback.frontend.boogie.ast.GreaterThanEqualsOperation;
import byteback.frontend.boogie.ast.GreaterThanOperation;
import byteback.frontend.boogie.ast.LessThanEqualsOperation;
import byteback.frontend.boogie.ast.LessThanOperation;
import byteback.frontend.boogie.ast.List;
import byteback.frontend.boogie.ast.ModuloOperation;
import byteback.frontend.boogie.ast.MultiplicationOperation;
import byteback.frontend.boogie.ast.NegationOperation;
import byteback.frontend.boogie.ast.NotEqualsOperation;
import byteback.frontend.boogie.ast.NumberLiteral;
import byteback.frontend.boogie.ast.OrOperation;
import byteback.frontend.boogie.ast.RealLiteral;
import byteback.frontend.boogie.ast.SubtractionOperation;
import byteback.frontend.boogie.ast.ValueReference;
import java.util.Iterator;
import java.util.Stack;
import soot.BooleanType;
import soot.Local;
import soot.Type;
import soot.UnknownType;
import soot.Value;
import soot.jimple.AddExpr;
import soot.jimple.AndExpr;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.CmpgExpr;
import soot.jimple.CmplExpr;
import soot.jimple.DivExpr;
import soot.jimple.DoubleConstant;
import soot.jimple.EqExpr;
import soot.jimple.FloatConstant;
import soot.jimple.GeExpr;
import soot.jimple.GtExpr;
import soot.jimple.InstanceFieldRef;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.LeExpr;
import soot.jimple.LongConstant;
import soot.jimple.LtExpr;
import soot.jimple.MulExpr;
import soot.jimple.NeExpr;
import soot.jimple.NegExpr;
import soot.jimple.NullConstant;
import soot.jimple.OrExpr;
import soot.jimple.RemExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.SubExpr;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.XorExpr;

public class ExpressionExtractor extends SootExpressionVisitor<Expression> {

	protected final Stack<Expression> operands;

	protected final Stack<SootType> types;

	public ExpressionExtractor() {
		this.operands = new Stack<>();
		this.types = new Stack<>();
	}

	public Expression visit(final SootExpression expression, final SootType type) {
		types.push(type);

		return super.visit(expression);
	}

	public Expression visit(final SootExpression expression) {
		return visit(expression, new SootType(UnknownType.v()));
	}

	public SootType getType() {
		return types.peek();
	}

	public void pushExpression(final Expression expression) {
		operands.push(expression);
	}

	public void pushBinaryExpression(final BinopExpr source, final BinaryExpression expression) {
		final SootExpression left = new SootExpression(source.getOp1());
		final SootExpression right = new SootExpression(source.getOp2());
		expression.setLeftOperand(visit(left, getType()));
		expression.setRightOperand(visit(right, getType()));
		pushExpression(expression);
	}

	public void pushCmpExpression(final BinopExpr cmp) {
		final FunctionReference cmpReference = Prelude.getCmpReference();
		final SootExpression left = new SootExpression(cmp.getOp1());
		final SootExpression right = new SootExpression(cmp.getOp2());
		cmpReference.addArgument(visit(left));
		cmpReference.addArgument(visit(right));
		pushExpression(cmpReference);
	}

	public void pushFunctionReference(final SootMethod method, final List<Expression> arguments) {
		final FunctionReference functionReference = new FunctionReference();
		final String methodName = method.getAnnotation(Annotations.PRELUDE_ANNOTATION).flatMap(SootAnnotation::getValue)
				.map((element) -> new StringElementExtractor().visit(element))
				.orElseGet(() -> NameConverter.methodName(method));
		arguments.insertChild(Prelude.getHeapVariable().getValueReference(), 0);
		functionReference.setAccessor(new Accessor(methodName));
		functionReference.setArgumentList(arguments);
		pushExpression(functionReference);
	}

	public List<Expression> makeArguments(final InvokeExpr invoke) {
		final List<Expression> arguments = new List<>();
		final Iterator<Type> type = invoke.getMethod().getParameterTypes().iterator();
		final Iterator<Value> value = invoke.getArgs().iterator();

		while (type.hasNext() && value.hasNext()) {
			arguments.add(visit(new SootExpression(value.next()), new SootType(type.next())));
		}

		return arguments;
	}

	@Override
	public void caseStaticInvokeExpr(final StaticInvokeExpr invoke) {
		final SootMethod method = new SootMethod(invoke.getMethod());
		final List<Expression> arguments = makeArguments(invoke);
		pushFunctionReference(method, arguments);
	}

	@Override
	public void caseVirtualInvokeExpr(final VirtualInvokeExpr invoke) {
		final SootMethod method = new SootMethod(invoke.getMethod());
		final List<Expression> arguments = makeArguments(invoke);
		final SootExpression base = new SootExpression(invoke.getBase());
		final Expression target = visit(base);
		arguments.insertChild(target, 0);
		pushFunctionReference(method, arguments);
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
		pushBinaryExpression(division, new DivisionOperation());
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
		pushExpression(new NegationOperation(visit(operand)));
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
				throw new IllegalArgumentException("Bitwise OR is currently not supported for type " + type);
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
				throw new IllegalArgumentException("Bitwise AND is currently not supported for type " + type);
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
				throw new IllegalArgumentException("Bitwise XOR is currently not supported for type " + type);
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
		final SootExpression operand = new SootExpression(casting.getOp());
		final SootType toType = new SootType(casting.getCastType());
		final SootType fromType = new SootType(casting.getType());
		final var caster = new CasterProvider(toType).visit(fromType);
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
		pushExpression(new NumberLiteral(literal.substring(0, literal.length() - 1)));
	}

	@Override
	public void caseDoubleConstant(final DoubleConstant doubleConstant) {
		pushExpression(new RealLiteral(doubleConstant.toString()));
	}

	@Override
	public void caseFloatConstant(final FloatConstant floatConstant) {
		pushExpression(new RealLiteral(floatConstant.toString()));
	}

	@Override
	public void caseNullConstant(final NullConstant nullConstant) {
		pushExpression(Prelude.getNullConstant().getValueReference());
	}

	@Override
	public void caseLocal(final Local local) {
		final SootExpression expression = new SootExpression(local);
		final var caster = new CasterProvider(getType()).visit(expression.getType());
		pushExpression(caster.apply(ValueReference.of(local.getName())));
	}

	@Override
	public void caseInstanceFieldRef(final InstanceFieldRef instanceFieldReference) {
		final SootField field = new SootField(instanceFieldReference.getField());
		final SootExpression base = new SootExpression(instanceFieldReference.getBase());
		final var caster = new CasterProvider(getType()).visit(field.getType());
		final Expression reference = ValueReference.of(NameConverter.fieldName(field));
		final Expression heapAccess = Prelude.getHeapAccessExpression(visit(base), reference);
		pushExpression(caster.apply(heapAccess));
	}

	@Override
	public void caseDefault(final Value expression) {
		throw new UnsupportedOperationException("Unable to convert Jimple expression " + expression + " to Boogie");
	}

	@Override
	public Expression result() {
		types.pop();
		return operands.pop();
	}

}
