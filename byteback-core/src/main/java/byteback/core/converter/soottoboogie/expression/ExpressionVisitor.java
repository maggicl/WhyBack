package byteback.core.converter.soottoboogie.expression;

import byteback.core.converter.soottoboogie.Namespace;
import byteback.core.converter.soottoboogie.Prelude;
import byteback.core.converter.soottoboogie.method.MethodConverter;
import byteback.core.converter.soottoboogie.type.CasterProvider;
import byteback.core.representation.soot.annotation.SootAnnotation;
import byteback.core.representation.soot.annotation.SootAnnotationElement.StringElementExtractor;
import byteback.core.representation.soot.body.SootExpression;
import byteback.core.representation.soot.body.SootExpressionVisitor;
import byteback.core.representation.soot.type.SootType;
import byteback.core.representation.soot.unit.SootMethod;
import byteback.frontend.boogie.ast.BinaryExpression;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.FunctionReference;
import byteback.frontend.boogie.ast.List;
import byteback.frontend.boogie.ast.ValueReference;
import byteback.frontend.boogie.builder.FunctionReferenceBuilder;
import java.util.Iterator;
import java.util.Stack;
import java.util.stream.Stream;
import soot.UnknownType;
import soot.Value;
import soot.jimple.BinopExpr;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.VirtualInvokeExpr;

public abstract class ExpressionVisitor extends SootExpressionVisitor<Expression> {

	protected final Stack<Expression> operands;

	protected final Stack<SootType> types;

	public ExpressionVisitor() {
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

	public void pushCastExpression(final Expression expression, final SootType fromType) {
		final var caster = new CasterProvider(getType()).visit(fromType);
		operands.push(caster.apply(expression));
	}

	public void pushCastExpression(final Expression expression, final Value value) {
		pushCastExpression(expression, new SootType(value.getType()));
	}

	public void pushBinaryExpression(final BinopExpr source, final BinaryExpression expression) {
		final var left = new SootExpression(source.getOp1());
		final var right = new SootExpression(source.getOp2());
		expression.setLeftOperand(visit(left, getType()));
		expression.setRightOperand(visit(right, getType()));
		pushExpression(expression);
	}

	public void pushSpecialBinaryExpression(final BinopExpr source, final FunctionReference reference) {
		final var left = new SootExpression(source.getOp1());
		final var right = new SootExpression(source.getOp2());
		reference.addArgument(visit(left));
		reference.addArgument(visit(right));
		pushExpression(reference);
	}

	public List<Expression> convertArguments(final SootMethod method, final Iterable<SootExpression> sources) {
		Stream<SootType> types = method.parameterTypes();
		final List<Expression> arguments = new List<>();
		final Iterator<SootExpression> sourceIterator = sources.iterator();

		if (!method.isStatic()) {
			types = Stream.concat(Stream.of(method.getSootClass().getType()), types);
		}

		final Iterator<SootType> typeIterator = types.iterator();

		while (typeIterator.hasNext() && sourceIterator.hasNext()) {
			arguments.add(visit(sourceIterator.next(), typeIterator.next()));
		}

		return arguments;
	}

	public void pushFunctionReference(final SootMethod method, final Iterable<SootExpression> arguments) {
		final var referenceBuilder = new FunctionReferenceBuilder();
		final String name = method.annotation(Namespace.PRELUDE_ANNOTATION).flatMap(SootAnnotation::getValue)
				.map((element) -> new StringElementExtractor().visit(element))
				.orElseGet(() -> MethodConverter.methodName(method));
		referenceBuilder.name(name);

		if (method.annotation(Namespace.PRIMITIVE_ANNOTATION).isEmpty()) {
			final ValueReference heapReference = Prelude.instance().getHeapVariable().makeValueReference();
			referenceBuilder.prependArgument(heapReference);
		}

		referenceBuilder.addArguments(convertArguments(method, arguments));
		pushExpression(referenceBuilder.build());
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
		types.pop();

		return operands.pop();
	}

}
