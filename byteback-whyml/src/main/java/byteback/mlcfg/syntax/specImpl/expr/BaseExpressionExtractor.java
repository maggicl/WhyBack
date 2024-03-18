//package byteback.mlcfg.syntax.spec.expr;
//
//import byteback.analysis.JimpleValueSwitch;
//import byteback.analysis.Namespace;
//import byteback.analysis.util.AnnotationElems.StringElemExtractor;
//import byteback.analysis.util.SootAnnotations;
//import byteback.analysis.util.SootHosts;
//import byteback.frontend.boogie.builder.FunctionReferenceBuilder;
//import byteback.mlcfg.printer.Statement;
//import soot.SootMethod;
//import soot.Value;
//import soot.jimple.BinopExpr;
//import soot.jimple.InstanceInvokeExpr;
//import soot.jimple.InterfaceInvokeExpr;
//import soot.jimple.VirtualInvokeExpr;
//
//public abstract class BaseExpressionExtractor extends JimpleValueSwitch<Statement> {
//
//	protected Statement expression;
//
//	public abstract BaseExpressionExtractor makeExpressionExtractor();
//
//	public void setExpression(final Statement expression) {
//		this.expression = expression;
//	}
//
//	public void setBinaryExpression(final BinopExpr source, final BinaryExpression expression) {
//		final Value left = source.getOp1();
//		final Value right = source.getOp2();
//		expression.setLeftOperand(visit(left));
//		expression.setRightOperand(visit(right));
//		setExpression(expression);
//	}
//
//	public void setSpecialBinaryExpression(final BinopExpr source, final FunctionReference reference) {
//		final Value left = source.getOp1();
//		final Value right = source.getOp2();
//		reference.addArgument(visit(left));
//		reference.addArgument(visit(right));
//		setExpression(reference);
//	}
//
//	public List<Expression> convertArguments(final SootMethod method, final Iterable<Value> sources) {
//		final List<Expression> arguments = new List<>();
//
//		for (Value source : sources) {
//			arguments.add(visit(source));
//		}
//
//		return arguments;
//	}
//
//	public void setFunctionReference(final SootMethod method, final Iterable<Value> arguments) {
//		final var referenceBuilder = new FunctionReferenceBuilder();
//		final String name = SootHosts.getAnnotation(method, Namespace.PRELUDE_ANNOTATION)
//				.flatMap(SootAnnotations::getValue).map((element) -> new StringElemExtractor().visit(element))
//				.orElseGet(() -> MethodConverter.methodName(method));
//		referenceBuilder.name(name);
//
//		if (!SootHosts.hasAnnotation(method, Namespace.PRIMITIVE_ANNOTATION)) {
//			final ValueReference heapReference = Prelude.v().getHeapVariable().makeValueReference();
//			referenceBuilder.prependArgument(heapReference);
//		}
//
//		referenceBuilder.addArguments(convertArguments(method, arguments));
//		setExpression(referenceBuilder.build());
//	}
//
//	abstract public void caseInstanceInvokeExpr(final InstanceInvokeExpr invoke);
//
//	@Override
//	public void caseVirtualInvokeExpr(final VirtualInvokeExpr invoke) {
//		caseInstanceInvokeExpr(invoke);
//	}
//
//	@Override
//	public void caseInterfaceInvokeExpr(final InterfaceInvokeExpr invoke) {
//		caseInstanceInvokeExpr(invoke);
//	}
//
//	@Override
//	public Expression result() {
//		return expression;
//	}
//
//}
