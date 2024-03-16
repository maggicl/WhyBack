//package byteback.mlcfg.syntax.spec.expr;
//
//import byteback.analysis.Namespace;
//import byteback.analysis.QuantifierExpr;
//import byteback.analysis.TypeSwitch;
//import byteback.analysis.vimp.LogicConstant;
//import byteback.analysis.vimp.LogicExistsExpr;
//import byteback.analysis.vimp.LogicForallExpr;
//import byteback.analysis.vimp.OldExpr;
//import byteback.analysis.vimp.VoidConstant;
//import byteback.frontend.boogie.ast.AdditionOperation;
//import byteback.frontend.boogie.ast.AndOperation;
//import byteback.frontend.boogie.ast.BooleanLiteral;
//import byteback.frontend.boogie.ast.EqualsOperation;
//import byteback.frontend.boogie.ast.ExistentialQuantifier;
//import byteback.frontend.boogie.ast.Expression;
//import byteback.frontend.boogie.ast.FunctionReference;
//import byteback.frontend.boogie.ast.GreaterThanEqualsOperation;
//import byteback.frontend.boogie.ast.GreaterThanOperation;
//import byteback.frontend.boogie.ast.IntegerDivisionOperation;
//import byteback.frontend.boogie.ast.LessThanEqualsOperation;
//import byteback.frontend.boogie.ast.LessThanOperation;
//import byteback.frontend.boogie.ast.MinusOperation;
//import byteback.frontend.boogie.ast.ModuloOperation;
//import byteback.frontend.boogie.ast.MultiplicationOperation;
//import byteback.frontend.boogie.ast.NegationOperation;
//import byteback.frontend.boogie.ast.NotEqualsOperation;
//import byteback.frontend.boogie.ast.NumberLiteral;
//import byteback.frontend.boogie.ast.OldReference;
//import byteback.frontend.boogie.ast.OrOperation;
//import byteback.frontend.boogie.ast.QuantifierExpression;
//import byteback.frontend.boogie.ast.RealDivisionOperation;
//import byteback.frontend.boogie.ast.RealLiteral;
//import byteback.frontend.boogie.ast.SubtractionOperation;
//import byteback.frontend.boogie.ast.SymbolicReference;
//import byteback.frontend.boogie.ast.TypeAccess;
//import byteback.frontend.boogie.ast.UniversalQuantifier;
//import byteback.frontend.boogie.ast.ValueReference;
//import byteback.mlcfg.syntax.spec.ExpressionConversionException;
//import byteback.mlcfg.syntax.spec.FunctionExpressionExtractor;
//import java.util.function.Function;
//import java.util.stream.Stream;
//import soot.BooleanType;
//import soot.IntType;
//import soot.Local;
//import soot.SootClass;
//import soot.SootField;
//import soot.SootMethod;
//import soot.Type;
//import soot.Value;
//import soot.jimple.AddExpr;
//import soot.jimple.AndExpr;
//import soot.jimple.ArrayRef;
//import soot.jimple.BinopExpr;
//import soot.jimple.CastExpr;
//import soot.jimple.ClassConstant;
//import soot.jimple.CmpExpr;
//import soot.jimple.CmpgExpr;
//import soot.jimple.CmplExpr;
//import soot.jimple.DivExpr;
//import soot.jimple.DoubleConstant;
//import soot.jimple.EqExpr;
//import soot.jimple.FloatConstant;
//import soot.jimple.GeExpr;
//import soot.jimple.GtExpr;
//import soot.jimple.InstanceFieldRef;
//import soot.jimple.InstanceInvokeExpr;
//import soot.jimple.InstanceOfExpr;
//import soot.jimple.IntConstant;
//import soot.jimple.LeExpr;
//import soot.jimple.LengthExpr;
//import soot.jimple.LongConstant;
//import soot.jimple.LtExpr;
//import soot.jimple.MulExpr;
//import soot.jimple.NeExpr;
//import soot.jimple.NegExpr;
//import soot.jimple.NullConstant;
//import soot.jimple.OrExpr;
//import soot.jimple.RemExpr;
//import soot.jimple.ShlExpr;
//import soot.jimple.ShrExpr;
//import soot.jimple.StaticFieldRef;
//import soot.jimple.StaticInvokeExpr;
//import soot.jimple.StringConstant;
//import soot.jimple.SubExpr;
//import soot.jimple.UshrExpr;
//import soot.jimple.XorExpr;
//
//public class PureExpressionExtractor extends BaseExpressionExtractor {
//
//	public static final String LOCAL_PREFIX = "_";
//
//	public static String sanitizeName(final String name) {
//		return name.replace("<", "#lt#").replace(">", "#gt#").replace("-", "#m#");
//	}
//
//	public static String localName(final Local local) {
//		return LOCAL_PREFIX + sanitizeName(local.getName());
//	}
//
//	@Override
//	public BaseExpressionExtractor makeExpressionExtractor() {
//		return new PureExpressionExtractor();
//	}
//
//	@Override
//	public void caseStaticInvokeExpr(final StaticInvokeExpr v) {
//		final SootMethod method = v.getMethod();
//		final Iterable<Value> arguments = v.getArgs();
//		setFunctionReference(method, arguments);
//	}
//
//	@Override
//	public void caseInstanceInvokeExpr(final InstanceInvokeExpr v) {
//		final SootMethod method = v.getMethod();
//		final Value base = v.getBase();
//		final Iterable<Value> arguments = Stream.concat(Stream.of(base), v.getArgs().stream())::iterator;
//		setFunctionReference(method, arguments);
//	}
//
//	public void pushCmpExpression(final BinopExpr v) {
//		setSpecialBinaryExpression(v, Prelude.v().getCmpFunction().makeFunctionReference());
//	}
//
//	@Override
//	public void caseAddExpr(final AddExpr v) {
//		setBinaryExpression(v, new AdditionOperation());
//	}
//
//	@Override
//	public void caseSubExpr(final SubExpr v) {
//		setBinaryExpression(v, new SubtractionOperation());
//	}
//
//	@Override
//	public void caseDivExpr(final DivExpr v) {
//		Type.toMachineType(v.getType()).apply(new TypeSwitch<>() {
//
//			@Override
//			public void caseIntType(final IntType $) {
//				setBinaryExpression(v, new IntegerDivisionOperation());
//			}
//
//			@Override
//			public void caseDefault(final Type $) {
//				setBinaryExpression(v, new RealDivisionOperation());
//			}
//
//		});
//	}
//
//	@Override
//	public void caseMulExpr(final MulExpr v) {
//		setBinaryExpression(v, new MultiplicationOperation());
//	}
//
//	@Override
//	public void caseRemExpr(final RemExpr v) {
//		setBinaryExpression(v, new ModuloOperation());
//	}
//
//	@Override
//	public void caseNegExpr(final NegExpr v) {
//		final Value operand = v.getOp();
//		final Expression expression = visit(operand);
//		v.getType().apply(new TypeSwitch<>() {
//
//			@Override
//			public void caseBooleanType(final BooleanType $) {
//				setExpression(new NegationOperation(expression));
//			}
//
//			@Override
//			public void caseDefault(final Type $) {
//				setExpression(new MinusOperation(expression));
//			}
//
//		});
//	}
//
//	@Override
//	public void caseOrExpr(final OrExpr v) {
//		v.getType().apply(new TypeSwitch<>() {
//
//			@Override
//			public void caseBooleanType(final BooleanType $) {
//				setBinaryExpression(v, new OrOperation());
//			}
//
//			@Override
//			public void caseDefault(final Type type) {
//				throw new ExpressionConversionException(v, "Bitwise OR is currently not supported for type " + type);
//			}
//
//		});
//	}
//
//	@Override
//	public void caseAndExpr(final AndExpr v) {
//		v.getType().apply(new TypeSwitch<>() {
//
//			@Override
//			public void caseBooleanType(final BooleanType $) {
//				setBinaryExpression(v, new AndOperation());
//			}
//
//			@Override
//			public void caseIntType(final IntType $) {
//				throw new ExpressionConversionException(v, "Bitwise AND is currently not supported");
//			}
//
//		});
//	}
//
//	@Override
//	public void caseXorExpr(final XorExpr v) {
//		v.getType().apply(new TypeSwitch<>() {
//
//			@Override
//			public void caseBooleanType(final BooleanType $) {
//				setBinaryExpression(v, new NotEqualsOperation());
//			}
//
//			@Override
//			public void caseIntType(final IntType $) {
//				throw new ExpressionConversionException(v, "Bitwise XOR is currently not supported");
//			}
//
//		});
//	}
//
//	@Override
//	public void caseCmplExpr(final CmplExpr v) {
//		pushCmpExpression(v);
//	}
//
//	@Override
//	public void caseCmpgExpr(final CmpgExpr v) {
//		pushCmpExpression(v);
//	}
//
//	@Override
//	public void caseCmpExpr(final CmpExpr v) {
//		pushCmpExpression(v);
//	}
//
//	@Override
//	public void caseEqExpr(final EqExpr v) {
//		setBinaryExpression(v, new EqualsOperation());
//	}
//
//	@Override
//	public void caseNeExpr(final NeExpr v) {
//		setBinaryExpression(v, new NotEqualsOperation());
//	}
//
//	@Override
//	public void caseGtExpr(final GtExpr v) {
//		setBinaryExpression(v, new GreaterThanOperation());
//	}
//
//	@Override
//	public void caseGeExpr(final GeExpr v) {
//		setBinaryExpression(v, new GreaterThanEqualsOperation());
//	}
//
//	@Override
//	public void caseLtExpr(final LtExpr v) {
//		setBinaryExpression(v, new LessThanOperation());
//	}
//
//	@Override
//	public void caseLeExpr(final LeExpr v) {
//		setBinaryExpression(v, new LessThanEqualsOperation());
//	}
//
//	@Override
//	public void caseShlExpr(final ShlExpr v) {
//		setSpecialBinaryExpression(v, Prelude.v().getShlFunction().makeFunctionReference());
//	}
//
//	@Override
//	public void caseShrExpr(final ShrExpr v) {
//		setSpecialBinaryExpression(v, Prelude.v().getShrFunction().makeFunctionReference());
//	}
//
//	@Override
//	public void caseUshrExpr(final UshrExpr v) {
//		setSpecialBinaryExpression(v, Prelude.v().getShrFunction().makeFunctionReference());
//	}
//
//	@Override
//	public void caseCastExpr(final CastExpr v) {
//		final Value operand = v.getOp();
//		final Type toType = v.getCastType();
//		final Type fromType = operand.getType();
//		final Function<Expression, Expression> caster = new CasterProvider(toType).visit(fromType);
//
//		setExpression(caster.apply(visit(operand)));
//	}
//
//	@Override
//	public void caseIntConstant(final IntConstant v) {
//		setExpression(new NumberLiteral(v.toString()));
//	}
//
//	@Override
//	public void caseLogicConstant(final LogicConstant v) {
//		setExpression(v.getValue() ? BooleanLiteral.makeTrue() : BooleanLiteral.makeFalse());
//	}
//
//	@Override
//	public void caseLongConstant(final LongConstant v) {
//		final String literal = v.toString();
//		final String strippedLiteral = literal.substring(0, literal.length() - 1);
//		setExpression(new NumberLiteral(strippedLiteral));
//	}
//
//	@Override
//	public void caseDoubleConstant(final DoubleConstant v) {
//		setExpression(new RealLiteral(v.toString()));
//	}
//
//	@Override
//	public void caseFloatConstant(final FloatConstant v) {
//		final String literal = v.toString();
//		final String strippedLiteral = literal.substring(0, literal.length() - 1);
//		setExpression(new RealLiteral(strippedLiteral));
//	}
//
//	@Override
//	public void caseNullConstant(final NullConstant v) {
//		setExpression(Prelude.v().getNullConstant().makeValueReference());
//	}
//
//	@Override
//	public void caseVoidConstant(final VoidConstant v) {
//		setExpression(Prelude.v().getVoidConstant().makeValueReference());
//	}
//
//	@Override
//	public void caseStringConstant(final StringConstant v) {
//		final int code = v.value.hashCode();
//		setExpression(Prelude.v().makeStringConstExpression(new NumberLiteral(Integer.toString(code))));
//	}
//
//	@Override
//	public void caseClassConstant(final ClassConstant classConstant) {
//		final String className = Namespace.stripConstantDescriptor(classConstant.getValue());
//		final ValueReference valueReference = ValueReference.of(ReferenceTypeConverter.typeName(className));
//		final FunctionReference typeReference = Prelude.v().getTypeReferenceFunction().makeFunctionReference();
//		typeReference.addArgument(valueReference);
//		setExpression(typeReference);
//	}
//
//	@Override
//	public void caseLocal(final Local v) {
//		setExpression(ValueReference.of(localName(v)));
//	}
//
//	@Override
//	public void caseInstanceFieldRef(final InstanceFieldRef v) {
//		final SootField field = v.getField();
//		final Value base = v.getBase();
//		final Expression reference = ValueReference.of(FieldConverter.fieldName(field));
//		final Expression heapAccess = Prelude.v().makeHeapAccessExpression(visit(base), reference);
//		setExpression(heapAccess);
//	}
//
//	@Override
//	public void caseStaticFieldRef(final StaticFieldRef v) {
//		final SootField field = v.getField();
//		final SootClass base = field.getDeclaringClass();
//		final Expression reference = ValueReference.of(FieldConverter.fieldName(field));
//		final Expression heapAccess = Prelude.v()
//				.makeStaticAccessExpression(ValueReference.of(ReferenceTypeConverter.typeName(base)), reference);
//		setExpression(heapAccess);
//	}
//
//	@Override
//	public void caseArrayRef(final ArrayRef v) {
//		final Value base = v.getBase();
//		final Type type = v.getType();
//		final var index = v.getIndex();
//		final TypeAccess typeAccess = new TypeAccessExtractor().visit(type);
//		setExpression(Prelude.v().makeArrayAccessExpression(typeAccess, visit(base), visit(index)));
//	}
//
//	@Override
//	public void caseLengthExpr(final LengthExpr v) {
//		final Value operand = v.getOp();
//		setExpression(Prelude.v().getLengthAccessExpression(visit(operand)));
//	}
//
//	@Override
//	public void caseInstanceOfExpr(final InstanceOfExpr v) {
//		final Value left = v.getOp();
//		final SymbolicReference typeReference = new TypeReferenceExtractor().visit(v.getCheckType());
//		setExpression(Prelude.v().makeTypeCheckExpression(PureExpressionExtractor.this.visit(left), typeReference));
//	}
//
//	public QuantifierExpression makeQuantifierExpression(final QuantifierExpr v) {
//		final var quantifierExpression = new QuantifierExpression();
//
//		for (Local local : v.getFreeLocals()) {
//			quantifierExpression.addBinding(FunctionExpressionExtractor.makeQuantifierBinding(local));
//		}
//
//		quantifierExpression.setOperand(visit(v.getValue()));
//
//		return quantifierExpression;
//	}
//
//	@Override
//	public void caseLogicForallExpr(final LogicForallExpr v) {
//		final var quantifierExpression = makeQuantifierExpression(v);
//		quantifierExpression.setQuantifier(new UniversalQuantifier());
//		setExpression(quantifierExpression);
//	}
//
//	@Override
//	public void caseLogicExistsExpr(final LogicExistsExpr v) {
//		final var quantifierExpression = makeQuantifierExpression(v);
//		quantifierExpression.setQuantifier(new ExistentialQuantifier());
//		setExpression(quantifierExpression);
//	}
//
//	@Override
//	public void caseOldExpr(final OldExpr v) {
//		final Expression operand = makeExpressionExtractor().visit(v.getOp());
//		final Expression oldReference = new OldReference(operand);
//		setExpression(oldReference);
//	}
//
//	@Override
//	public void caseDefault(final Value v) {
//		throw new ExpressionConversionException(v, "Unable to convert expression of type " + v.getClass().getName());
//	}
//
//}
