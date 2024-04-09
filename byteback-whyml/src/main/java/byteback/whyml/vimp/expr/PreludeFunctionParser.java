package byteback.whyml.vimp.expr;

import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.expr.UnaryExpression;
import byteback.whyml.syntax.expr.binary.BinaryExpression;
import byteback.whyml.syntax.expr.binary.BinaryOperator;
import byteback.whyml.syntax.expr.binary.Comparison;
import byteback.whyml.syntax.expr.binary.LogicConnector;
import byteback.whyml.syntax.expr.harmonization.BinaryOpTypeHarmonizer;
import byteback.whyml.syntax.expr.harmonization.HarmonizationResult;
import java.util.List;
import soot.SootMethod;

public final class PreludeFunctionParser {
	private PreludeFunctionParser() {
	}

	public static Expression parse(final SootMethod method, final List<Expression> arguments) {
		return switch (method.getName()) {
			case "and" -> buildBinaryExpr(LogicConnector.AND, arguments);
			case "or" -> buildBinaryExpr(LogicConnector.OR, arguments);
			case "iff" -> buildBinaryExpr(LogicConnector.IFF, arguments);
			case "implies" -> buildBinaryExpr(LogicConnector.IMPLIES, arguments);
			case "not" -> buildUnaryExpr(UnaryExpression.Operator.NOT, arguments);
			case "gt" -> buildComparisonExpr(Comparison.Kind.GT, arguments);
			case "gte" -> buildComparisonExpr(Comparison.Kind.GE, arguments);
			case "lt" -> buildComparisonExpr(Comparison.Kind.LT, arguments);
			case "lte" -> buildComparisonExpr(Comparison.Kind.LE, arguments);
			case "eq" -> buildComparisonExpr(Comparison.Kind.EQ, arguments);
			case "neq" -> buildComparisonExpr(Comparison.Kind.NE, arguments);
			default -> throw new IllegalArgumentException("prelude operator %s not recognized"
					.formatted(method.getName()));
		};
	}

	private static BinaryExpression buildComparisonExpr(Comparison.Kind kind, List<Expression> arguments) {
		if (arguments.size() != 2) {
			throw new IllegalArgumentException("the comparison operator %s accepts 2 arguments, %d given".formatted(
					kind, arguments.size()));
		}

		final HarmonizationResult hr = BinaryOpTypeHarmonizer.harmonize(arguments.get(0), arguments.get(1));
		return new BinaryExpression(new Comparison(hr.getType(), kind), hr.getFirstOp(), hr.getSecondOp());
	}

	private static BinaryExpression buildBinaryExpr(BinaryOperator op, List<Expression> arguments) {
		if (arguments.size() != 2) {
			throw new IllegalStateException("the operator %s accepts 2 arguments, %d given".formatted(
					op, arguments.size()));
		}

		return new BinaryExpression(op, arguments.get(0), arguments.get(1));
	}


	private static UnaryExpression buildUnaryExpr(UnaryExpression.Operator op, List<Expression> arguments) {
		if (arguments.size() != 1) {
			throw new IllegalStateException("the operator %s accepts 1 argument, %d given".formatted(
					op, arguments.size()));
		}

		return new UnaryExpression(op, arguments.get(0));
	}
}
