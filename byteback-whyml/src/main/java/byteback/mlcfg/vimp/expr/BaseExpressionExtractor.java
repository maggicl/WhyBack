package byteback.mlcfg.vimp.expr;

import byteback.analysis.JimpleValueSwitch;
import byteback.analysis.Namespace;
import byteback.analysis.util.SootHosts;
import byteback.mlcfg.syntax.expr.Expression;
import byteback.mlcfg.syntax.expr.FunctionCall;
import byteback.mlcfg.syntax.expr.UnaryExpression;
import byteback.mlcfg.syntax.expr.binary.BinaryExpression;
import byteback.mlcfg.syntax.expr.binary.BinaryOperator;
import byteback.mlcfg.vimp.VimpMethodParser;
import java.util.List;
import java.util.stream.StreamSupport;
import soot.SootMethod;
import soot.Value;
import soot.jimple.BinopExpr;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.UnopExpr;
import soot.jimple.VirtualInvokeExpr;

public abstract class BaseExpressionExtractor extends JimpleValueSwitch<Expression> {
	private final VimpMethodParser methodSignatureParser;

	protected Expression expression;

	protected BaseExpressionExtractor(VimpMethodParser methodSignatureParser) {
		this.methodSignatureParser = methodSignatureParser;
	}

	protected void setExpression(final Expression expression) {
		this.expression = expression;
	}

	protected void setBinaryExpression(final BinopExpr source, final BinaryOperator operator) {
		setExpression(new BinaryExpression(operator, visit(source.getOp1()), visit(source.getOp2())));
	}

	protected void setUnaryExpression(final UnopExpr source, final UnaryExpression.Operator operator) {
		setExpression(new UnaryExpression(operator, visit(source.getOp())));
	}

	protected void setFunctionReference(final SootMethod method, final Iterable<Value> arguments) {
		final List<Expression> argExpressions = StreamSupport.stream(arguments.spliterator(), false)
				.map(this::visit)
				.toList();

		if (SootHosts.hasAnnotation(method, Namespace.PRIMITIVE_ANNOTATION)) {
			setExpression(PreludeFunctionParser.parse(method, argExpressions));
		} else {
			methodSignatureParser.parseSignature(method).ifPresent(e -> setExpression(new FunctionCall(e, argExpressions)));
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
}
