package byteback.mlcfg.vimp.expr;

import byteback.analysis.JimpleValueSwitch;
import byteback.analysis.Namespace;
import byteback.analysis.util.SootHosts;
import byteback.mlcfg.syntax.expr.ConditionalExpression;
import byteback.mlcfg.syntax.expr.Expression;
import byteback.mlcfg.syntax.expr.FunctionCall;
import byteback.mlcfg.syntax.expr.OldReference;
import byteback.mlcfg.syntax.expr.UnaryExpression;
import byteback.mlcfg.syntax.expr.WholeNumberLiteral;
import byteback.mlcfg.syntax.expr.binary.BinaryExpression;
import byteback.mlcfg.syntax.expr.binary.BinaryOperator;
import byteback.mlcfg.syntax.types.WhyJVMType;
import byteback.mlcfg.vimp.VimpMethodParser;
import fj.P;
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

		final String specialClassName = "byteback.annotations.Special";

		if (method.getDeclaringClass().getName().equals(specialClassName)) {
			if (!method.isStatic()) {
				throw new IllegalStateException("unknown instance method %s in class %s".formatted(method.getName(), specialClassName));
			}

			// handle special "old" and "conditional" methods
			switch (method.getName()) {
				case "old":
					setExpression(new OldReference(argExpressions.get(0)));
					break;
				case "conditional":
					Expression conditional1 = argExpressions.get(0);
					Expression thenExpr1 = argExpressions.get(1);
					Expression elseExpr1 = argExpressions.get(2);
					setExpression(new ConditionalExpression(conditional1, thenExpr1, elseExpr1));
					break;
				default:
					throw new IllegalStateException("unknown static method %s in class %s".formatted(method.getName(), specialClassName));
			}
		} else if (SootHosts.hasAnnotation(method, Namespace.PRIMITIVE_ANNOTATION)) {
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
