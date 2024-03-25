package byteback.mlcfg.vimp.expr;

import byteback.analysis.QuantifierExpr;
import byteback.analysis.vimp.LogicConstant;
import byteback.analysis.vimp.LogicExistsExpr;
import byteback.analysis.vimp.LogicForallExpr;
import byteback.analysis.vimp.OldExpr;
import byteback.analysis.vimp.VoidConstant;
import byteback.mlcfg.identifiers.IdentifierEscaper;
import byteback.mlcfg.syntax.WhyField;
import byteback.mlcfg.syntax.WhyFunctionParam;
import byteback.mlcfg.syntax.WhyInstanceField;
import byteback.mlcfg.syntax.WhyStaticField;
import byteback.mlcfg.syntax.expr.BooleanLiteral;
import byteback.mlcfg.syntax.expr.ClassCastExpression;
import byteback.mlcfg.syntax.expr.DoubleLiteral;
import byteback.mlcfg.syntax.expr.Expression;
import byteback.mlcfg.syntax.expr.FloatLiteral;
import byteback.mlcfg.syntax.expr.InstanceOfExpression;
import byteback.mlcfg.syntax.expr.LocalVariableExpression;
import byteback.mlcfg.syntax.expr.NullLiteral;
import byteback.mlcfg.syntax.expr.NumericLiteral;
import byteback.mlcfg.syntax.expr.OldReference;
import byteback.mlcfg.syntax.expr.QuantifierExpression;
import byteback.mlcfg.syntax.expr.StringLiteralExpression;
import byteback.mlcfg.syntax.expr.UnaryExpression;
import byteback.mlcfg.syntax.expr.UnitLiteral;
import byteback.mlcfg.syntax.expr.binary.BinaryOperator;
import byteback.mlcfg.syntax.expr.binary.Comparison;
import byteback.mlcfg.syntax.expr.binary.LogicConnector;
import byteback.mlcfg.syntax.expr.binary.PrefixOperator;
import byteback.mlcfg.syntax.expr.field.Access;
import byteback.mlcfg.syntax.expr.field.ArrayExpression;
import byteback.mlcfg.syntax.expr.field.ArrayOperation;
import byteback.mlcfg.syntax.expr.field.FieldExpression;
import byteback.mlcfg.syntax.expr.field.Operation;
import byteback.mlcfg.syntax.types.WhyArrayType;
import byteback.mlcfg.syntax.types.WhyJVMType;
import byteback.mlcfg.syntax.types.WhyType;
import byteback.mlcfg.vimp.TypeResolver;
import byteback.mlcfg.vimp.VimpFieldParser;
import byteback.mlcfg.vimp.VimpMethodParser;
import java.util.List;
import java.util.stream.Stream;
import soot.Local;
import soot.SootMethod;
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
	private final VimpFieldParser fieldParser;
	private final IdentifierEscaper identifierEscaper;

	public PureExpressionExtractor(VimpMethodParser methodSignatureParser, TypeResolver typeResolver,
								   VimpFieldParser fieldParser, IdentifierEscaper identifierEscaper) {
		super(methodSignatureParser);
		this.typeResolver = typeResolver;
		this.fieldParser = fieldParser;
		this.identifierEscaper = identifierEscaper;
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
	public void caseVoidConstant(final VoidConstant v) {
		setExpression(UnitLiteral.INSTANCE);
	}

	@Override
	public void caseCastExpr(final CastExpr v) {
		setExpression(new ClassCastExpression(
				visit(v.getOp()),
				typeResolver.resolveType(v.getCastType())
		));
	}

	@Override
	public void caseStringConstant(final StringConstant v) {
		setExpression(new StringLiteralExpression(v.value));
	}

	@Override
	public void caseClassConstant(final ClassConstant classConstant) {
		// FIXME: find what this does
		throw new UnsupportedOperationException("not implemented");

//		final String className = Namespace.stripConstantDescriptor(classConstant.getValue());
//		final ValueReference valueReference = ValueReference.of(ReferenceTypeConverter.typeName(className));
//		final FunctionReference typeReference = Prelude.v().getTypeReferenceFunction().makeFunctionReference();
//		typeReference.addArgument(valueReference);
//		setExpression(typeReference);
	}

	@Override
	public void caseInstanceOfExpr(final InstanceOfExpr v) {
		setExpression(new InstanceOfExpression(
				visit(v.getOp()),
				typeResolver.resolveType(v.getCheckType())
		));
	}

	@Override
	public void caseLocal(final Local v) {
		setExpression(new LocalVariableExpression(
				identifierEscaper.escapeL("lv_" + v.getName()),
				typeResolver.resolveJVMType(v.getType())
		));
	}

	@Override
	public void caseInstanceFieldRef(final InstanceFieldRef v) {
		final WhyField field = fieldParser.parse(v.getField());
		if (!(field instanceof WhyInstanceField)) {
			throw new IllegalStateException("InstanceFieldRef has a non-instance field");
		}

		final Expression base = visit(v.getBase());
		setExpression(new FieldExpression(Operation.get(), Access.instance(base, (WhyInstanceField) field)));
	}

	@Override
	public void caseStaticFieldRef(final StaticFieldRef v) {
		final WhyField field = fieldParser.parse(v.getField());
		if (!(field instanceof WhyStaticField)) {
			throw new IllegalStateException("InstanceFieldRef has a non-instance field");
		}

		setExpression(new FieldExpression(Operation.get(), Access.staticAccess((WhyStaticField) field)));
	}

	@Override
	public void caseArrayRef(final ArrayRef v) {
		final WhyType type = typeResolver.resolveType(v.getType());
		if (!(type instanceof WhyArrayType)) {
			throw new IllegalStateException("type of array ref expression is not array type");
		}

		final WhyJVMType elemType = ((WhyArrayType) type).baseType().jvm();
		final Expression base = visit(v.getBase());
		final Expression index = visit(v.getIndex());

		setExpression(new ArrayExpression(base, elemType, ArrayOperation.load(index)));
	}

	@Override
	public void caseLengthExpr(final LengthExpr v) {
		final WhyType type = typeResolver.resolveType(v.getType());
		if (!(type instanceof WhyArrayType)) {
			throw new IllegalStateException("type of array ref expression is not array type");
		}

		final WhyJVMType elemType = ((WhyArrayType) type).baseType().jvm();
		final Expression base = visit(v.getOp());

		setExpression(new ArrayExpression(base, elemType, ArrayOperation.length()));
	}

	public QuantifierExpression quantifierExpression(final QuantifierExpression.Kind kind, final QuantifierExpr v) {
		final List<WhyFunctionParam> variables = v.getFreeLocals().stream()
				.map(e -> new WhyFunctionParam(
						identifierEscaper.escapeL(e.getName()),
						typeResolver.resolveType(e.getType()),
						false))
				.toList();

		return new QuantifierExpression(kind, variables, visit(v.getValue()));
	}

	@Override
	public void caseLogicForallExpr(final LogicForallExpr v) {
		setExpression(quantifierExpression(QuantifierExpression.Kind.FORALL, v));
	}

	@Override
	public void caseLogicExistsExpr(final LogicExistsExpr v) {
		setExpression(quantifierExpression(QuantifierExpression.Kind.EXISTS, v));
	}

	@Override
	public void caseOldExpr(final OldExpr v) {
		setExpression(new OldReference(visit(v.getOp())));
	}

	@Override
	public void caseDefault(final Value v) {
		throw new IllegalArgumentException("Unable to convert expression of type " + v.getClass().getName());
	}
}
