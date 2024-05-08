package byteback.whyml.vimp.expr;

import byteback.analysis.Inline;
import byteback.analysis.JimpleValueSwitch;
import byteback.analysis.Namespace;
import byteback.analysis.util.SootHosts;
import byteback.whyml.syntax.expr.ConditionalExpression;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.expr.FunctionCall;
import byteback.whyml.syntax.expr.OldReference;
import byteback.whyml.syntax.expr.UnaryExpression;
import byteback.whyml.syntax.expr.binary.BinaryExpression;
import byteback.whyml.syntax.expr.binary.BinaryOperator;
import byteback.whyml.syntax.expr.field.Access;
import byteback.whyml.syntax.expr.field.ArrayExpression;
import byteback.whyml.syntax.expr.field.ArrayOperation;
import byteback.whyml.syntax.expr.field.FieldExpression;
import byteback.whyml.syntax.expr.field.Operation;
import byteback.whyml.syntax.field.WhyField;
import byteback.whyml.syntax.field.WhyInstanceField;
import byteback.whyml.syntax.field.WhyStaticField;
import byteback.whyml.syntax.function.WhyFunctionSignature;
import byteback.whyml.syntax.type.WhyArrayType;
import byteback.whyml.syntax.type.WhyJVMType;
import byteback.whyml.syntax.type.WhyType;
import byteback.whyml.vimp.TypeResolver;
import byteback.whyml.vimp.VimpFieldParser;
import byteback.whyml.vimp.VimpMethodNameParser;
import byteback.whyml.vimp.VimpMethodParser;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import soot.SootMethod;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.BinopExpr;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.UnopExpr;
import soot.jimple.VirtualInvokeExpr;

public abstract class BaseExpressionExtractor extends JimpleValueSwitch<Expression> {
	protected final VimpFieldParser fieldParser;
	protected final TypeResolver typeResolver;
	private final VimpMethodParser methodSignatureParser;
	private final VimpMethodNameParser methodNameParser;
	protected Expression expression;

	protected BaseExpressionExtractor(VimpMethodParser methodSignatureParser,
									  VimpMethodNameParser methodNameParser,
									  VimpFieldParser fieldParser,
									  TypeResolver typeResolver) {
		this.methodSignatureParser = methodSignatureParser;
		this.methodNameParser = methodNameParser;
		this.fieldParser = fieldParser;
		this.typeResolver = typeResolver;
	}

	protected void setExpression(final Expression expression) {
		this.expression = expression;
	}

	protected void setBinaryExpression(final BinopExpr source, final BinaryOperator operator) {
		final Expression v1 = visit(source.getOp1());
		final Expression v2 = visit(source.getOp2());
		setExpression(new BinaryExpression(operator, v1, v2));
	}

	protected void setUnaryExpression(final UnopExpr source, final UnaryExpression.Operator operator) {
		setExpression(new UnaryExpression(operator, visit(source.getOp())));
	}

	protected void setFunctionReference(final SootMethod method, final Iterable<Value> arguments) {
		final List<Expression> argExpressions = StreamSupport.stream(arguments.spliterator(), false)
				.map(this::visit)
				.toList();

		if (method.getDeclaringClass().getName().equals(Namespace.SPECIAL_CLASS_NAME)) {
			if (!method.isStatic()) {
				throw new IllegalStateException("unknown instance method %s in class %s"
						.formatted(method.getName(), Namespace.SPECIAL_CLASS_NAME));
			}

			// handle special "old" and "conditional" methods
			switch (method.getName()) {
				case Namespace.OLD_NAME:
					// TODO: check if old expression is allowed in current scope (i.e. only in @Predicate s)
					setExpression(new OldReference(argExpressions.get(0)));
					break;
				case Namespace.CONDITIONAL_NAME:
					Expression conditional1 = argExpressions.get(0);
					Expression thenExpr1 = argExpressions.get(1);
					Expression elseExpr1 = argExpressions.get(2);
					setExpression(new ConditionalExpression(conditional1, thenExpr1, elseExpr1));
					break;
				default:
					throw new IllegalStateException("unknown static method %s in class %s".formatted(
							method.getName(),
							Namespace.SPECIAL_CLASS_NAME));
			}
		} else if (SootHosts.hasAnnotation(method, Namespace.PRIMITIVE_ANNOTATION)) {
			setExpression(PreludeFunctionParser.parse(method, argExpressions));
		} else {
			final WhyFunctionSignature sig = VimpMethodParser.declaration(method)
					.flatMap(decl -> Inline.parse(method).must()
							? Optional.empty()
							: Optional.of(methodSignatureParser.signature(method, decl)))
					.orElseThrow(() -> new IllegalStateException("method " + method + " is not callable"));

			setExpression(new FunctionCall(methodNameParser.methodName(sig), sig, argExpressions));
		}
	}

	abstract protected void caseInstanceInvokeExpr(final InstanceInvokeExpr invoke);

	@Override
	public void caseVirtualInvokeExpr(final VirtualInvokeExpr invoke) {
		caseInstanceInvokeExpr(invoke);
	}

	@Override
	public void caseInterfaceInvokeExpr(final InterfaceInvokeExpr invoke) {
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
			throw new IllegalStateException("InstanceFieldRef has a non-instance field");
		}

		final Expression base = visit(v.getBase());
		setExpression(new FieldExpression(fieldAccess(), Access.instance(base, (WhyInstanceField) field)));
	}

	@Override
	public void caseStaticFieldRef(final StaticFieldRef v) {
		final WhyField field = fieldParser.parse(v.getField());
		if (!(field instanceof WhyStaticField)) {
			throw new IllegalStateException("InstanceFieldRef has a non-instance field");
		}

		setExpression(new FieldExpression(fieldAccess(), Access.staticAccess((WhyStaticField) field)));
	}

	@Override
	public void caseArrayRef(final ArrayRef v) {
		final WhyType type = typeResolver.resolveType(v.getBase().getType());
		if (!(type instanceof WhyArrayType)) {
			throw new IllegalStateException("type of array ref expression is not array type");
		}

		final WhyJVMType elemType = ((WhyArrayType) type).baseType().jvm();
		final Expression base = visit(v.getBase());
		final Expression index = visit(v.getIndex());

		setExpression(new ArrayExpression(base, elemType, arrayElemAccess(index)));
	}

	protected abstract Operation fieldAccess();

	protected abstract ArrayOperation arrayElemAccess(Expression index);
}
