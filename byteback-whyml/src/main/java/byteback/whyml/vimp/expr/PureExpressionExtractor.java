package byteback.whyml.vimp.expr;

import byteback.analysis.Inline;
import byteback.analysis.JimpleValueSwitch;
import byteback.analysis.Namespace;
import byteback.analysis.QuantifierExpr;
import byteback.analysis.transformer.QuantifierValueTransformer;
import byteback.analysis.util.SootHosts;
import byteback.analysis.vimp.LogicConstant;
import byteback.analysis.vimp.LogicExistsExpr;
import byteback.analysis.vimp.LogicForallExpr;
import byteback.analysis.vimp.OldExpr;
import byteback.analysis.vimp.VoidConstant;
import byteback.whyml.identifiers.Identifier;
import byteback.whyml.identifiers.IdentifierEscaper;
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
import byteback.whyml.syntax.expr.NullLiteral;
import byteback.whyml.syntax.expr.OldReference;
import byteback.whyml.syntax.expr.PrimitiveCastExpression;
import byteback.whyml.syntax.expr.QuantifierExpression;
import byteback.whyml.syntax.expr.StringLiteralExpression;
import byteback.whyml.syntax.expr.UnaryExpression;
import byteback.whyml.syntax.expr.WholeNumberLiteral;
import byteback.whyml.syntax.expr.binary.BinaryExpression;
import byteback.whyml.syntax.expr.binary.BinaryOperator;
import byteback.whyml.syntax.expr.binary.Comparison;
import byteback.whyml.syntax.expr.binary.LogicConnector;
import byteback.whyml.syntax.expr.binary.PrefixOperator;
import byteback.whyml.syntax.expr.field.Access;
import byteback.whyml.syntax.expr.field.ArrayExpression;
import byteback.whyml.syntax.expr.field.ArrayOperation;
import byteback.whyml.syntax.expr.field.FieldExpression;
import byteback.whyml.syntax.expr.field.Operation;
import byteback.whyml.syntax.field.WhyField;
import byteback.whyml.syntax.field.WhyInstanceField;
import byteback.whyml.syntax.field.WhyStaticField;
import byteback.whyml.syntax.function.WhyFunctionDeclaration;
import byteback.whyml.syntax.function.WhyFunctionSignature;
import byteback.whyml.syntax.function.WhyLocal;
import byteback.whyml.syntax.type.WhyArrayType;
import byteback.whyml.syntax.type.WhyJVMType;
import byteback.whyml.syntax.type.WhyReference;
import byteback.whyml.syntax.type.WhyType;
import byteback.whyml.vimp.TypeResolver;
import byteback.whyml.vimp.VimpFieldParser;
import byteback.whyml.vimp.VimpMethodNameParser;
import byteback.whyml.vimp.VimpMethodParser;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import soot.Local;
import soot.SootMethod;
import soot.Value;
import soot.jimple.AddExpr;
import soot.jimple.AndExpr;
import soot.jimple.ArrayRef;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.CaughtExceptionRef;
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
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
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
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.StringConstant;
import soot.jimple.SubExpr;
import soot.jimple.UnopExpr;
import soot.jimple.UshrExpr;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.XorExpr;
import soot.jimple.toolkits.infoflow.FakeJimpleLocal;

public class PureExpressionExtractor extends JimpleValueSwitch<Expression> {
	protected final VimpFieldParser fieldParser;
	protected final TypeResolver typeResolver;
	protected final VimpMethodParser methodSignatureParser;
	protected final VimpMethodNameParser methodNameParser;
	protected final IdentifierEscaper identifierEscaper;

	protected Expression expression;

	public PureExpressionExtractor(VimpFieldParser fieldParser,
								   TypeResolver typeResolver,
								   VimpMethodParser methodSignatureParser,
								   VimpMethodNameParser methodNameParser,
								   IdentifierEscaper identifierEscaper) {
		this.fieldParser = fieldParser;
		this.typeResolver = typeResolver;
		this.methodSignatureParser = methodSignatureParser;
		this.methodNameParser = methodNameParser;
		this.identifierEscaper = identifierEscaper;
	}

	protected void setExpression(final Expression expression) {
		this.expression = expression;
	}

	protected void setBinaryExpression(final BinopExpr source, final BinaryOperator operator) {
		final Expression v1 = visit(source.getOp1());
		final Expression v2 = visit(source.getOp2());
		try {
			setExpression(new BinaryExpression(operator, v1, v2));
		} catch (RuntimeException e) {
			throw new WhyTranslationException(source, "Cannot construct " + operator + " Why binary expression: " + e.getMessage());
		}
	}

	protected void setUnaryExpression(final UnopExpr source, final UnaryExpression.Operator operator) {
		try {
			setExpression(new UnaryExpression(operator, visit(source.getOp())));
		} catch (RuntimeException e) {
			throw new WhyTranslationException(source, "Cannot build " + operator + " Why unary expression: " + e.getMessage());
		}
	}

	@Override
	public void caseAddExpr(final AddExpr v) {
		final WhyJVMType opType = typeResolver.resolveJVMType(v.getType());
		final BinaryOperator op = switch (opType) {
			case INT -> PrefixOperator.IADD;
			case LONG -> PrefixOperator.LADD;
			case FLOAT -> PrefixOperator.FADD;
			case DOUBLE -> PrefixOperator.DADD;
			default -> throw new WhyTranslationException(v, "AddExpr not supported on type: " + opType);
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
			default -> throw new WhyTranslationException(v, "SubExpr not supported on type: " + opType);
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
			default -> throw new WhyTranslationException(v, "DivExpr not supported on type: " + opType);
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
			default -> throw new WhyTranslationException(v, "MulExpr not supported on type: " + opType);
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
			default -> throw new WhyTranslationException(v, "RemExpr operation not supported on type: " + opType);
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
			default -> throw new WhyTranslationException(v, "NegExpr not supported on type: " + opType);
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
			default -> throw new WhyTranslationException(v, "OrExpr not supported on type: " + opType);
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
			default -> throw new WhyTranslationException(v, "AndExpr not supported on type: " + opType);
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
			default -> throw new WhyTranslationException(v, "XorExpr not supported on type " + opType);
		};
		setBinaryExpression(v, op);
	}

	@Override
	public void caseCmplExpr(final CmplExpr v) {
		final WhyJVMType opType = typeResolver.resolveJVMType(v.getOp1().getType());
		final BinaryOperator op = switch (opType) {
			case FLOAT -> PrefixOperator.FCMPL;
			case DOUBLE -> PrefixOperator.DCMPL;
			default -> throw new WhyTranslationException(v, "CmplExpr not supported on type: " + opType);
		};
		setBinaryExpression(v, op);
	}

	@Override
	public void caseCmpgExpr(final CmpgExpr v) {
		final WhyJVMType opType = typeResolver.resolveJVMType(v.getOp1().getType());
		final BinaryOperator op = switch (opType) {
			case FLOAT -> PrefixOperator.FCMPG;
			case DOUBLE -> PrefixOperator.DCMPG;
			default -> throw new WhyTranslationException(v, "CmpgExpr operation not supported on type " + opType);
		};
		setBinaryExpression(v, op);
	}

	@Override
	public void caseCmpExpr(final CmpExpr v) {
		final WhyJVMType op1Type = typeResolver.resolveJVMType(v.getOp1().getType());
		final WhyJVMType op2Type = typeResolver.resolveJVMType(v.getOp2().getType());

		if (op1Type != WhyJVMType.LONG || op2Type != WhyJVMType.LONG) {
			throw new WhyTranslationException(v, "CmpExpr not supported on types: " + op1Type + ", " + op2Type);
		}

		setBinaryExpression(v, PrefixOperator.LCMP);
	}

	private void setConditionExpr(final ConditionExpr expr, final Comparison.Kind kind) {
		final Expression v1 = visit(expr.getOp1());
		final Expression v2 = visit(expr.getOp2());

		if (v1.type() != v2.type()) {
			throw new WhyTranslationException(expr, "ConditionExpr has operands '%s' and '%s' with incompatible types: %s, %s"
					.formatted(v1, v2, v1.type(), v2.type()));
		}

		try {
			setExpression(new BinaryExpression(new Comparison(v1.type(), kind), v1, v2));
		} catch (IllegalArgumentException e) {
			throw new WhyTranslationException(expr, "Cannot build comparison binary expression: " + e.getMessage());
		}
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
			default -> throw new WhyTranslationException(v, "ShlExpr operation not supported on type: " + opType);
		};
		setBinaryExpression(v, op);
	}

	@Override
	public void caseShrExpr(final ShrExpr v) {
		final WhyJVMType opType = typeResolver.resolveJVMType(v.getType());
		final BinaryOperator op = switch (opType) {
			case INT -> PrefixOperator.ISHR;
			case LONG -> PrefixOperator.LSHR;
			default -> throw new WhyTranslationException(v, "ShrExpr operation not supported on type: " + opType);
		};
		setBinaryExpression(v, op);
	}

	@Override
	public void caseUshrExpr(final UshrExpr v) {
		final WhyJVMType opType = typeResolver.resolveJVMType(v.getType());
		final BinaryOperator op = switch (opType) {
			case INT -> PrefixOperator.IUSHR;
			case LONG -> PrefixOperator.LUSHR;
			default -> throw new WhyTranslationException(v, "UshrExpr operation not supported on type: " + opType);
		};
		setBinaryExpression(v, op);
	}

	@Override
	public void caseIntConstant(final IntConstant v) {
		setExpression(new WholeNumberLiteral(WhyJVMType.INT, v.value));
	}

	@Override
	public void caseLogicConstant(final LogicConstant v) {
		setExpression(new BooleanLiteral(v.value));
	}

	@Override
	public void caseLongConstant(final LongConstant v) {
		setExpression(new WholeNumberLiteral(WhyJVMType.LONG, v.value));
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
		final Expression op = visit(v.getOp());
		final WhyJVMType sourceType = op.type();

		final WhyType targetType = typeResolver.resolveType(v.getCastType());
		final WhyJVMType targetJVMType = targetType.jvm();

		if (!sourceType.isMeta() && !targetJVMType.isMeta()) {
			setExpression(new PrimitiveCastExpression(op, targetJVMType));
		} else if (sourceType == WhyJVMType.PTR && targetJVMType == WhyJVMType.PTR) {
			setExpression(new ClassCastExpression(op, targetType, true));
		} else {
			throw new WhyTranslationException(v, "CastExpr not supported on types: %s, %s".formatted(op.type(), targetType));
		}
	}

	@Override
	public void caseStringConstant(final StringConstant v) {
		setExpression(new StringLiteralExpression(v.value));
	}

	@Override
	public void caseClassConstant(final ClassConstant v) {
		final WhyType classType = typeResolver.resolveType(v.getType());
		if (classType instanceof WhyReference ref) {
			setExpression(new ClassLiteralExpression(ref));
		} else {
			throw new WhyTranslationException(v, "ClassConstant type must be reference: " + classType);
		}
	}

	@Override
	public void caseInstanceOfExpr(final InstanceOfExpr v) {
		try {
			setExpression(new InstanceOfExpression(
					visit(v.getOp()),
					typeResolver.resolveType(v.getCheckType())
			));
		} catch (IllegalArgumentException e) {
			throw new WhyTranslationException(v, "Cannot build Why InstanceOfExpression: " + e.getMessage());
		}
	}

	protected Identifier.L localIdentifier(Local variable) {
		return identifierEscaper.escapeParam(variable.getName());
	}

	@Override
	public void caseLocal(final Local v) {
		try {
			setExpression(new LocalExpression(
					localIdentifier(v),
					typeResolver.resolveJVMType(v.getType())
			));
		} catch (IllegalArgumentException e) {
			throw new WhyTranslationException(v, "Cannot build Why LocalExpression: " + e.getMessage());
		}
	}

	@Override
	public void caseLengthExpr(final LengthExpr v) {
		final WhyType type = typeResolver.resolveType(v.getOp().getType());
		if (!(type instanceof WhyArrayType)) {
			throw new WhyTranslationException(v, "LengthExpr operator is not array type: " + type);
		}

		final WhyJVMType elemType = ((WhyArrayType) type).baseType().jvm();
		final Expression base = visit(v.getOp());

		try {
			setExpression(new ArrayExpression(base, elemType, ArrayOperation.length()));
		} catch (IllegalArgumentException e) {
			throw new WhyTranslationException(v, "Cannot build Why ArrayExpression for length: " + e);
		}
	}

	public QuantifierExpression quantifierExpression(final QuantifierExpression.Kind kind, final QuantifierExpr v) {
		final List<WhyLocal> variables = v.getFreeLocals().stream()
				.map(e -> new WhyLocal(
						localIdentifier(e),
						typeResolver.resolveType(e.getType()),
						false))
				.toList();

		if (variables.size() != 1) {
			throw new WhyTranslationException(v, "%s QuantifierExpr must have exactly one free variable, given: %s"
					.formatted(kind, variables));
		}

		return new QuantifierExpression(kind, variables.get(0), visit(v.getValue()));
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
	public void caseCaughtExceptionRef(CaughtExceptionRef v) {
		setExpression(new LocalExpression(WhyLocal.CAUGHT_EXCEPTION));
	}

	@Override
	public void caseVoidConstant(VoidConstant v) {
		setExpression(NullLiteral.INSTANCE);
	}

	@Override
	public void caseDefault(final Value v) {
		throw new WhyTranslationException(v, "Unknown Soot value type: %s".formatted(v.getClass().getName()));
	}

	protected Expression parseSpecialClassMethod(InvokeExpr call, List<Expression> argExpressions) {
		final SootMethod method = call.getMethod();

		if (!method.isStatic()) {
			throw new WhyTranslationException(call, "instance method '%s' in special class is unknown"
					.formatted(method.getName()));
		}

		// handle special "old" and "conditional" methods
		return switch (method.getName()) {
			case Namespace.OLD_NAME -> new OldReference(argExpressions.get(0));
			case Namespace.CONDITIONAL_NAME -> new ConditionalExpression(
					argExpressions.get(0),
					argExpressions.get(1),
					argExpressions.get(2));
			default -> throw new WhyTranslationException(call,
					"static method '%s' in special class is unknown".formatted(method.getName()));
		};
	}

	protected Expression parsePrimitiveOpMethod(InvokeExpr call, List<Expression> argExpressions) {
		return PreludeFunctionParser.parse(call, argExpressions);
	}

	protected Expression parseMethodCall(InvokeExpr call, List<Expression> argExpressions) {
		final SootMethod method = call.getMethod();

		final WhyFunctionSignature sig = VimpMethodParser.declaration(method)
				.filter(WhyFunctionDeclaration::isSpec)
				.map(decl -> methodSignatureParser.signature(method, decl))
				.orElseThrow(() -> new WhyTranslationException(call,
						"method '%s' is not callable from a pure expression".formatted(method)));

		return FunctionCall.build(methodNameParser.methodName(sig), sig, argExpressions);
	}

	protected void setFunctionReference(final InvokeExpr call, final Iterable<Value> arguments) {
		final SootMethod method = call.getMethod();

		final List<Expression> argExpressions = StreamSupport.stream(arguments.spliterator(), false)
				.map(this::visit)
				.toList();

		if (method.getDeclaringClass().getName().equals(Namespace.SPECIAL_CLASS_NAME)) {
			try {
				setExpression(parseSpecialClassMethod(call, argExpressions));
			} catch (IllegalArgumentException e) {
				throw new WhyTranslationException(call, "cannot translate special byteback class method call: "
						+ e.getMessage());
			}
		} else if (SootHosts.hasAnnotation(method, Namespace.PRIMITIVE_ANNOTATION)) {
			setExpression(parsePrimitiveOpMethod(call, argExpressions));
		} else {
			if (Inline.parse(method).must()) {
				throw new WhyTranslationException(call, "method '%s' MUST be inlined and cannot be called".formatted(method));
			}

			try {
				setExpression(parseMethodCall(call, argExpressions));
			} catch (IllegalArgumentException e) {
				throw new WhyTranslationException(call, "cannot translate regular method call: " + e.getMessage());
			}
		}
	}

	@Override
	public void caseStaticInvokeExpr(final StaticInvokeExpr v) {
		setFunctionReference(v, v.getArgs());
	}

	public void caseInstanceInvokeExpr(final InstanceInvokeExpr v) {
		final Value base = v.getBase();
		final Iterable<Value> arguments = Stream.concat(Stream.of(base), v.getArgs().stream())::iterator;
		setFunctionReference(v, arguments);
	}

	@Override
	public void caseVirtualInvokeExpr(final VirtualInvokeExpr invoke) {
		caseInstanceInvokeExpr(invoke);
	}

	@Override
	public void caseInterfaceInvokeExpr(final InterfaceInvokeExpr invoke) {
		caseInstanceInvokeExpr(invoke);
	}

	@Override
	public void caseSpecialInvokeExpr(SpecialInvokeExpr invoke) {
		caseInstanceInvokeExpr(invoke);
	}

	@Override
	public Expression result() {
		return expression;
	}

	@Override
	public void caseInstanceFieldRef(final InstanceFieldRef v) {
		final WhyField field = fieldParser.parse(v.getField());
		if (!(field instanceof WhyInstanceField)) {
			throw new WhyTranslationException(v, "InstanceFieldRef field is not an instance field: " + field);
		}

		final Expression base = visit(v.getBase());
		try {
			setExpression(new FieldExpression(fieldAccess(), Access.instance(base, (WhyInstanceField) field)));
		} catch (IllegalArgumentException e) {
			throw new WhyTranslationException(v, "Cannot build Why FieldExpression for instance: " + e);
		}
	}

	@Override
	public void caseStaticFieldRef(final StaticFieldRef v) {
		final WhyField field = fieldParser.parse(v.getField());
		if (!(field instanceof WhyStaticField)) {
			throw new WhyTranslationException(v, "StaticFieldRef field is not a static field: " + field);
		}

		try {
			setExpression(new FieldExpression(fieldAccess(), Access.staticAccess((WhyStaticField) field)));
		} catch (IllegalArgumentException e) {
			throw new WhyTranslationException(v, "Cannot build Why FieldExpression for static: " + e);
		}
	}

	@Override
	public void caseArrayRef(final ArrayRef v) {
		final WhyType type = typeResolver.resolveType(v.getBase().getType());
		if (!(type instanceof WhyArrayType)) {
			throw new WhyTranslationException(v, "ArrayRef field type is not an array: " + type);
		}

		final WhyJVMType elemType = ((WhyArrayType) type).baseType().jvm();
		final Expression base = visit(v.getBase());
		final Expression index = visit(v.getIndex());

		try {
			setExpression(new ArrayExpression(base, elemType, arrayElemAccess(index)));
		} catch (IllegalArgumentException e) {
			throw new WhyTranslationException(v, "Cannot build Why ArrayExpression: " + e);
		}
	}

	protected Operation fieldAccess() {
		return Operation.IS;
	}

	protected ArrayOperation arrayElemAccess(Expression index) {
		return ArrayOperation.isElem(index);
	}
}
