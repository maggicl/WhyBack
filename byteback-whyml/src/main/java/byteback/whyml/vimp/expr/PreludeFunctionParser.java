package byteback.whyml.vimp.expr;

import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.expr.UnaryExpression;
import byteback.whyml.syntax.expr.binary.BinaryExpression;
import byteback.whyml.syntax.expr.binary.BinaryOperator;
import byteback.whyml.syntax.expr.binary.Comparison;
import byteback.whyml.syntax.expr.binary.LogicConnector;
import byteback.whyml.syntax.expr.harmonization.WhyTypeHarmonizer;
import byteback.whyml.syntax.expr.harmonization.HarmonizationResult;
import java.util.List;
import soot.SootMethod;
import soot.jimple.InvokeExpr;

public final class PreludeFunctionParser {
	private PreludeFunctionParser() {
	}

	public static Expression parse(final InvokeExpr call, final List<Expression> arguments) {
		final SootMethod method = call.getMethod();

		try {
			return switch (method.getName()) {
				case "not" -> buildNotExpr(arguments);
				case "and" -> buildBinaryExpr(LogicConnector.AND, arguments);
				case "or" -> buildBinaryExpr(LogicConnector.OR, arguments);
				case "iff" -> buildBinaryExpr(LogicConnector.IFF, arguments);
				case "implies" -> buildBinaryExpr(LogicConnector.IMPLIES, arguments);
				case "gt" -> buildComparisonExpr(Comparison.Kind.GT, arguments);
				case "gte" -> buildComparisonExpr(Comparison.Kind.GE, arguments);
				case "lt" -> buildComparisonExpr(Comparison.Kind.LT, arguments);
				case "lte" -> buildComparisonExpr(Comparison.Kind.LE, arguments);
				case "eq" -> buildComparisonExpr(Comparison.Kind.EQ, arguments);
				case "neq" -> buildComparisonExpr(Comparison.Kind.NE, arguments);
				default -> throw new WhyTranslationException(call,
						"method name is not a known operator: " + method.getName());
			};
		} catch (IllegalArgumentException e) {
			throw new WhyTranslationException(call, "cannot translate '%s' operator: %s".formatted(method.getName(), e.getMessage()));
		}
	}

	private static BinaryExpression buildComparisonExpr(Comparison.Kind kind, List<Expression> arguments) {
		if (arguments.size() != 2) {
			throw new IllegalArgumentException("the comparison operator %s accepts 2 arguments, %d given".formatted(
					kind, arguments.size()));
		}

		final HarmonizationResult hr = WhyTypeHarmonizer.harmonize(arguments.get(0), arguments.get(1));
		return new BinaryExpression(new Comparison(hr.getType(), kind), hr.getFirstOp(), hr.getSecondOp());
	}

	private static BinaryExpression buildBinaryExpr(BinaryOperator op, List<Expression> arguments) {
		if (arguments.size() != 2) {
			throw new IllegalStateException("the operator %s accepts 2 arguments, %d given".formatted(
					op, arguments.size()));
		}

		return new BinaryExpression(op, arguments.get(0), arguments.get(1));
	}


	private static UnaryExpression buildNotExpr(List<Expression> arguments) {
		if (arguments.size() != 1) {
			throw new IllegalStateException("the operator %s accepts 1 argument, %d given".formatted(
					UnaryExpression.Operator.NOT, arguments.size()));
		}

		return new UnaryExpression(UnaryExpression.Operator.NOT, arguments.get(0));
	}
}
