package byteback.core.converter.soottoboogie.expression;

import byteback.core.converter.soottoboogie.Namespace;
import byteback.core.converter.soottoboogie.Prelude;
import byteback.core.converter.soottoboogie.method.MethodConverter;
import byteback.core.converter.soottoboogie.type.CasterProvider;
import byteback.core.representation.soot.annotation.SootAnnotations;
import byteback.core.representation.soot.annotation.SootAnnotationElems.StringElemExtractor;
import byteback.core.representation.soot.body.SootExpressionVisitor;
import byteback.core.representation.soot.type.SootType;
import byteback.core.representation.soot.unit.SootMethods;
import byteback.frontend.boogie.ast.BinaryExpression;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.FunctionReference;
import byteback.frontend.boogie.ast.List;
import byteback.frontend.boogie.ast.ValueReference;
import byteback.frontend.boogie.builder.FunctionReferenceBuilder;

import java.util.ArrayList;
import java.util.Iterator;

import soot.SootMethod;
import soot.Type;
import soot.Value;
import soot.jimple.BinopExpr;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.VirtualInvokeExpr;

public abstract class ExpressionVisitor extends SootExpressionVisitor<Expression> {

	protected Expression expression;

	protected final Type type;

	public ExpressionVisitor(final Type type) {
		this.type = type;
	}

	public abstract ExpressionVisitor makeExpressionVisitor(final Type type);

	public Type getType() {
		return type;
	}

	public Expression visit(final Value value, final Type type) {
		return makeExpressionVisitor(type).visit(value);
	}

	public void setExpression(final Expression expression) {
		this.expression = expression;
	}

	public void setCastExpression(final Expression expression, final Type fromType) {
		final var caster = new CasterProvider(getType()).visit(fromType);
		setExpression(caster.apply(expression));
	}

	public void setCastExpression(final Expression expression, final Value value) {
		setCastExpression(expression, value.getType());
	}

	public void setBinaryExpression(final BinopExpr source, final BinaryExpression expression) {
		final Value left = source.getOp1();
		final Value right = source.getOp2();
		final Type type = SootType.join(left.getType(), right.getType());

		expression.setLeftOperand(visit(left, type));
		expression.setRightOperand(visit(right, type));
		setCastExpression(expression, getType());
	}

	public void setSpecialBinaryExpression(final BinopExpr source, final FunctionReference reference) {
		final Value left = source.getOp1();
		final Value right = source.getOp2();
		final Type type = SootType.join(left.getType(), right.getType());

		reference.addArgument(visit(left, type));
		reference.addArgument(visit(right, type));
		setCastExpression(reference, getType());
	}

	public List<Expression> convertArguments(final SootMethod method, final Iterable<Value> sources) {

		final java.util.List<Type> types = new ArrayList<>(method.getParameterTypes());
		final List<Expression> arguments = new List<>();

		if (!method.isStatic()) {
			types.add(0, method.getDeclaringClass().getType());
		}

		final Iterator<Value> sourceIterator = sources.iterator();
		final Iterator<Type> typeIterator = types.iterator();

		while (typeIterator.hasNext() && sourceIterator.hasNext()) {
			arguments.add(visit(sourceIterator.next(), typeIterator.next()));
		}

		return arguments;
	}

	public void pushFunctionReference(final SootMethod method, final Iterable<Value> arguments) {
		final var referenceBuilder = new FunctionReferenceBuilder();
		final String name = SootMethods
			.getAnnotation(method, Namespace.PRELUDE_ANNOTATION)
			.flatMap(SootAnnotations::getValue)
			.map((element) -> new StringElemExtractor().visit(element))
			.orElseGet(() -> MethodConverter.methodName(method));
		referenceBuilder.name(name);

		if (!SootMethods.hasAnnotation(method, Namespace.PRIMITIVE_ANNOTATION)) {
			final ValueReference heapReference = Prelude.v().getHeapVariable()
				.makeValueReference();
			referenceBuilder.prependArgument(heapReference);
		}

		referenceBuilder.addArguments(convertArguments(method, arguments));
		setCastExpression(referenceBuilder.build(), getType());
	}

	abstract public void caseInstanceInvokeExpr(final InstanceInvokeExpr invoke);

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

}
