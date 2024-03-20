package byteback.mlcfg.vimp.expr;

import byteback.analysis.Namespace;
import byteback.analysis.QuantifierExpr;
import byteback.analysis.vimp.LogicConstant;
import byteback.analysis.vimp.LogicExistsExpr;
import byteback.analysis.vimp.LogicForallExpr;
import byteback.analysis.vimp.OldExpr;
import byteback.analysis.vimp.VoidConstant;
import byteback.mlcfg.syntax.expr.BooleanLiteral;
import byteback.mlcfg.syntax.expr.DoubleLiteral;
import byteback.mlcfg.syntax.expr.FloatLiteral;
import byteback.mlcfg.syntax.expr.NullLiteral;
import byteback.mlcfg.syntax.expr.NumericLiteral;
import byteback.mlcfg.syntax.expr.UnaryExpression;
import byteback.mlcfg.syntax.expr.binary.BinaryOperator;
import byteback.mlcfg.syntax.expr.binary.Comparison;
import byteback.mlcfg.syntax.expr.binary.LogicConnector;
import byteback.mlcfg.syntax.expr.binary.PrefixOperator;
import byteback.mlcfg.syntax.types.WhyJVMType;
import byteback.mlcfg.vimp.TypeResolver;
import byteback.mlcfg.vimp.VimpMethodSignatureParser;
import java.util.stream.Stream;
import soot.Local;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Value;
import soot.jimple.AddExpr;
import soot.jimple.AndExpr;
import soot.jimple.ArrayRef;
import soot.jimple.CastExpr;
import soot.jimple.ClassConstant;
import soot.jimple.CmpExpr;
import soot.jimple.CmpgExpr;
import soot.jimple.CmplExpr;
import soot.jimple.ConditionExpr;
import soot.jimple.DivExpr;
import soot.jimple.DoubleConstant;
import soot.jimple.EqExpr;
import soot.jimple.FloatConstant;
import soot.jimple.GeExpr;
import soot.jimple.GtExpr;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InstanceOfExpr;
import soot.jimple.IntConstant;
import soot.jimple.LeExpr;
import soot.jimple.LengthExpr;
import soot.jimple.LongConstant;
import soot.jimple.LtExpr;
import soot.jimple.MulExpr;
import soot.jimple.NeExpr;
import soot.jimple.NegExpr;
import soot.jimple.NullConstant;
import soot.jimple.OrExpr;
import soot.jimple.RemExpr;
import soot.jimple.ShlExpr;
import soot.jimple.ShrExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.StringConstant;
import soot.jimple.SubExpr;
import soot.jimple.UshrExpr;
import soot.jimple.XorExpr;

public class PureExpressionExtractor extends BaseExpressionExtractor {
	private final TypeResolver typeResolver;

	public PureExpressionExtractor(VimpMethodSignatureParser methodSignatureParser, TypeResolver typeResolver) {
		super(methodSignatureParser);
		this.typeResolver = typeResolver;
	}

	@Override
	public void caseStaticInvokeExpr(final StaticInvokeExpr v) {
		final SootMethod method = v.getMethod();
		final Iterable<Value> arguments = v.getArgs();
		setFunctionReference(method, arguments);
	}

	@Override
	public void caseInstanceInvokeExpr(final InstanceInvokeExpr v) {
		final SootMethod method = v.getMethod();
		final Value base = v.getBase();
		final Iterable<Value> arguments = Stream.concat(Stream.of(base), v.getArgs().stream())::iterator;
		setFunctionReference(method, arguments);
	}

	@Override
	public void caseAddExpr(final AddExpr v) {
		final WhyJVMType opType = typeResolver.resolveJVMType(v.getType());
		final BinaryOperator op = switch (opType) {
			case INT -> PrefixOperator.IADD;
			case LONG -> PrefixOperator.LADD;
			case FLOAT -> PrefixOperator.FADD;
			case DOUBLE -> PrefixOperator.DADD;
			default -> throw new IllegalArgumentException("add operation not supported on type " + opType);
		};
		setBinaryExpression(v, op);
	}

	@Override
	public void caseSubExpr(final SubExpr v) {
		final WhyJVMType opType = typeResolver.resolveJVMType(v.getType());
		final BinaryOperator op = switch (opType) {
			case INT -> PrefixOperator.ISUB;
			case LONG -> PrefixOperator.LSUB;
			case FLOAT -> PrefixOperator.FSUB;
			case DOUBLE -> PrefixOperator.DSUB;
			default -> throw new IllegalArgumentException("sub operation not supported on type " + opType);
		};
		setBinaryExpression(v, op);
	}

	@Override
	public void caseDivExpr(final DivExpr v) {
		final WhyJVMType opType = typeResolver.resolveJVMType(v.getType());
		final BinaryOperator op = switch (opType) {
			case INT -> PrefixOperator.IDIV;
			case LONG -> PrefixOperator.LDIV;
			case FLOAT -> PrefixOperator.FDIV;
			case DOUBLE -> PrefixOperator.DDIV;
			default -> throw new IllegalArgumentException("div operation not supported on type " + opType);
		};
		setBinaryExpression(v, op);
	}

	@Override
	public void caseMulExpr(final MulExpr v) {
		final WhyJVMType opType = typeResolver.resolveJVMType(v.getType());
		final BinaryOperator op = switch (opType) {
			case INT -> PrefixOperator.IMUL;
			case LONG -> PrefixOperator.LMUL;
			case FLOAT -> PrefixOperator.FMUL;
			case DOUBLE -> PrefixOperator.DMUL;
			default -> throw new IllegalArgumentException("mul operation not supported on type " + opType);
		};
		setBinaryExpression(v, op);
	}

	@Override
	public void caseRemExpr(final RemExpr v) {
		final WhyJVMType opType = typeResolver.resolveJVMType(v.getType());
		final BinaryOperator op = switch (opType) {
			case INT -> PrefixOperator.IREM;
			case LONG -> PrefixOperator.LREM;
			case FLOAT -> PrefixOperator.FREM;
			case DOUBLE -> PrefixOperator.DREM;
			default -> throw new IllegalArgumentException("rem operation not supported on type " + opType);
		};
		setBinaryExpression(v, op);
	}

	@Override
	public void caseNegExpr(final NegExpr v) {
		final WhyJVMType opType = typeResolver.resolveJVMType(v.getType());
		final UnaryExpression.Operator op = switch (opType) {
			case INT -> UnaryExpression.Operator.INEG;
			case LONG -> UnaryExpression.Operator.LNEG;
			case FLOAT -> UnaryExpression.Operator.FNEG;
			case DOUBLE -> UnaryExpression.Operator.DNEG;
			default -> throw new IllegalArgumentException("add operation not supported on type " + opType);
		};
		setUnaryExpression(v, op);
	}

	@Override
	public void caseOrExpr(final OrExpr v) {
		final WhyJVMType opType = typeResolver.resolveJVMType(v.getType());
		final BinaryOperator op = switch (opType) {
			case BOOL -> LogicConnector.OR; // logical or
			case INT -> PrefixOperator.IOR; // bitwise int or
			case LONG -> PrefixOperator.LOR; // bitwise long or
			default -> throw new IllegalArgumentException("or operation not supported on type " + opType);
		};
		setBinaryExpression(v, op);
	}

	@Override
	public void caseAndExpr(final AndExpr v) {
		final WhyJVMType opType = typeResolver.resolveJVMType(v.getType());
		final BinaryOperator op = switch (opType) {
			case BOOL -> LogicConnector.AND; // logical and
			case INT -> PrefixOperator.IAND; // bitwise int and
			case LONG -> PrefixOperator.LAND; // bitwise long and
			default -> throw new IllegalArgumentException("and operation not supported on type " + opType);
		};
		setBinaryExpression(v, op);
	}

	@Override
	public void caseXorExpr(final XorExpr v) {
		final WhyJVMType opType = typeResolver.resolveJVMType(v.getType());
		final BinaryOperator op = switch (opType) {
			case BOOL -> new Comparison(WhyJVMType.BOOL, Comparison.Kind.NE); // logical xor
			case INT -> PrefixOperator.IXOR; // bitwise int xor
			case LONG -> PrefixOperator.LXOR; // bitwise long xor
			default -> throw new IllegalArgumentException("xor operation not supported on type " + opType);
		};
		setBinaryExpression(v, op);
	}

	@Override
	public void caseCmplExpr(final CmplExpr v) {
		final WhyJVMType opType = typeResolver.resolveJVMType(v.getType());
		final BinaryOperator op = switch (opType) {
			case FLOAT -> PrefixOperator.FCMPL;
			case DOUBLE -> PrefixOperator.DCMPL;
			default -> throw new IllegalArgumentException("cmpl operation not supported on type " + opType);
		};
		setBinaryExpression(v, op);
	}

	@Override
	public void caseCmpgExpr(final CmpgExpr v) {
		final WhyJVMType opType = typeResolver.resolveJVMType(v.getType());
		final BinaryOperator op = switch (opType) {
			case FLOAT -> PrefixOperator.FCMPG;
			case DOUBLE -> PrefixOperator.DCMPG;
			default -> throw new IllegalArgumentException("cmpg operation not supported on type " + opType);
		};
		setBinaryExpression(v, op);
	}

	@Override
	public void caseCmpExpr(final CmpExpr v) {
		final WhyJVMType opType = typeResolver.resolveJVMType(v.getType());
		if (opType != WhyJVMType.LONG) {
			throw new IllegalArgumentException("cmp operation not supported on type " + opType);
		}
		setBinaryExpression(v, PrefixOperator.LCMP);
	}

	private void setConditionExpr(final ConditionExpr expr, final Comparison.Kind kind) {
		final WhyJVMType opType = typeResolver.resolveJVMType(expr.getType());
		setBinaryExpression(expr, new Comparison(opType, kind));
	}

	@Override
	public void caseEqExpr(final EqExpr v) {
		setConditionExpr(v, Comparison.Kind.EQ);
	}

	@Override
	public void caseNeExpr(final NeExpr v) {
		setConditionExpr(v, Comparison.Kind.NE);
	}

	@Override
	public void caseGtExpr(final GtExpr v) {
		setConditionExpr(v, Comparison.Kind.GT);
	}

	@Override
	public void caseGeExpr(final GeExpr v) {
		setConditionExpr(v, Comparison.Kind.GE);
	}

	@Override
	public void caseLtExpr(final LtExpr v) {
		setConditionExpr(v, Comparison.Kind.LT);
	}

	@Override
	public void caseLeExpr(final LeExpr v) {
		setConditionExpr(v, Comparison.Kind.LE);
	}

	@Override
	public void caseShlExpr(final ShlExpr v) {
		final WhyJVMType opType = typeResolver.resolveJVMType(v.getType());
		final BinaryOperator op = switch (opType) {
			case INT -> PrefixOperator.ISHL;
			case LONG -> PrefixOperator.LSHL;
			default -> throw new IllegalArgumentException("shl operation not supported on type " + opType);
		};
		setBinaryExpression(v, op);
	}

	@Override
	public void caseShrExpr(final ShrExpr v) {
		final WhyJVMType opType = typeResolver.resolveJVMType(v.getType());
		final BinaryOperator op = switch (opType) {
			case INT -> PrefixOperator.ISHR;
			case LONG -> PrefixOperator.LSHR;
			default -> throw new IllegalArgumentException("shr operation not supported on type " + opType);
		};
		setBinaryExpression(v, op);
	}

	@Override
	public void caseUshrExpr(final UshrExpr v) {
		final WhyJVMType opType = typeResolver.resolveJVMType(v.getType());
		final BinaryOperator op = switch (opType) {
			case INT -> PrefixOperator.IUSHR;
			case LONG -> PrefixOperator.LUSHR;
			default -> throw new IllegalArgumentException("ushr operation not supported on type " + opType);
		};
		setBinaryExpression(v, op);
	}

	@Override
	public void caseIntConstant(final IntConstant v) {
		setExpression(new NumericLiteral(WhyJVMType.INT, v.value));
	}

	@Override
	public void caseLogicConstant(final LogicConstant v) {
		setExpression(new BooleanLiteral(v.value));
	}

	@Override
	public void caseLongConstant(final LongConstant v) {
		setExpression(new NumericLiteral(WhyJVMType.LONG, v.value));
	}

	@Override
	public void caseDoubleConstant(final DoubleConstant v) {
		setExpression(new DoubleLiteral(v.value));
	}

	@Override
	public void caseFloatConstant(final FloatConstant v) {
		setExpression(new FloatLiteral(v.value));
	}

	@Override
	public void caseNullConstant(final NullConstant v) {
		setExpression(NullLiteral.INSTANCE);
	}

	@Override
	public void caseCastExpr(final CastExpr v) {
		final Value operand = v.getOp();
		final Type toType = v.getCastType();
		final Type fromType = operand.getType();
		final Function<Expression, Expression> caster = new CasterProvider(toType).visit(fromType);

		setExpression(caster.apply(visit(operand)));
	}

	@Override
	public void caseVoidConstant(final VoidConstant v) {
		setExpression(Prelude.v().getVoidConstant().makeValueReference());
	}

	@Override
	public void caseStringConstant(final StringConstant v) {
		final int code = v.value.hashCode();
		setExpression(Prelude.v().makeStringConstExpression(new NumberLiteral(Integer.toString(code))));
	}

	@Override
	public void caseClassConstant(final ClassConstant classConstant) {
		final String className = Namespace.stripConstantDescriptor(classConstant.getValue());
		final ValueReference valueReference = ValueReference.of(ReferenceTypeConverter.typeName(className));
		final FunctionReference typeReference = Prelude.v().getTypeReferenceFunction().makeFunctionReference();
		typeReference.addArgument(valueReference);
		setExpression(typeReference);
	}

	@Override
	public void caseLocal(final Local v) {
		setExpression(ValueReference.of(localName(v)));
	}

	@Override
	public void caseInstanceFieldRef(final InstanceFieldRef v) {
		final SootField field = v.getField();
		final Value base = v.getBase();
		final Expression reference = ValueReference.of(FieldConverter.fieldName(field));
		final Expression heapAccess = Prelude.v().makeHeapAccessExpression(visit(base), reference);
		setExpression(heapAccess);
	}

	@Override
	public void caseStaticFieldRef(final StaticFieldRef v) {
		final SootField field = v.getField();
		final SootClass base = field.getDeclaringClass();
		final Expression reference = ValueReference.of(FieldConverter.fieldName(field));
		final Expression heapAccess = Prelude.v()
				.makeStaticAccessExpression(ValueReference.of(ReferenceTypeConverter.typeName(base)), reference);
		setExpression(heapAccess);
	}

	@Override
	public void caseArrayRef(final ArrayRef v) {
		final Value base = v.getBase();
		final Type type = v.getType();
		final var index = v.getIndex();
		final TypeAccess typeAccess = new TypeAccessExtractor().visit(type);
		setExpression(Prelude.v().makeArrayAccessExpression(typeAccess, visit(base), visit(index)));
	}

	@Override
	public void caseLengthExpr(final LengthExpr v) {
		final Value operand = v.getOp();
		setExpression(Prelude.v().getLengthAccessExpression(visit(operand)));
	}

	@Override
	public void caseInstanceOfExpr(final InstanceOfExpr v) {
		final Value left = v.getOp();
		final SymbolicReference typeReference = new TypeReferenceExtractor().visit(v.getCheckType());
		setExpression(Prelude.v().makeTypeCheckExpression(PureExpressionExtractor.this.visit(left), typeReference));
	}

	public QuantifierExpression makeQuantifierExpression(final QuantifierExpr v) {
		final var quantifierExpression = new QuantifierExpression();

		for (Local local : v.getFreeLocals()) {
			quantifierExpression.addBinding(FunctionExpressionExtractor.makeQuantifierBinding(local));
		}

		quantifierExpression.setOperand(visit(v.getValue()));

		return quantifierExpression;
	}

	@Override
	public void caseLogicForallExpr(final LogicForallExpr v) {
		final var quantifierExpression = makeQuantifierExpression(v);
		quantifierExpression.setQuantifier(new UniversalQuantifier());
		setExpression(quantifierExpression);
	}

	@Override
	public void caseLogicExistsExpr(final LogicExistsExpr v) {
		final var quantifierExpression = makeQuantifierExpression(v);
		quantifierExpression.setQuantifier(new ExistentialQuantifier());
		setExpression(quantifierExpression);
	}

	@Override
	public void caseOldExpr(final OldExpr v) {
		final Expression operand = makeExpressionExtractor().visit(v.getOp());
		final Expression oldReference = new OldReference(operand);
		setExpression(oldReference);
	}

	@Override
	public void caseDefault(final Value v) {
		throw new IllegalArgumentException("Unable to convert expression of type " + v.getClass().getName());
	}

}
