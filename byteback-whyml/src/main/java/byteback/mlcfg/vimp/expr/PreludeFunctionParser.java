package byteback.mlcfg.vimp.expr;

import byteback.mlcfg.syntax.expr.Expression;
import byteback.mlcfg.syntax.expr.UnaryExpression;
import byteback.mlcfg.syntax.expr.binary.BinaryExpression;
import byteback.mlcfg.syntax.expr.binary.BinaryOperator;
import byteback.mlcfg.syntax.expr.binary.Comparison;
import byteback.mlcfg.syntax.expr.binary.LogicConnector;
import byteback.mlcfg.syntax.types.WhyPrimitive;
import byteback.mlcfg.syntax.types.WhyType;
import java.util.List;
import soot.SootMethod;

public final class PreludeFunctionParser {
	private PreludeFunctionParser() {}

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

		final WhyType firstArgType = arguments.get(0).type();
		final WhyType secondArgType = arguments.get(1).type();

		if (!firstArgType.equals(secondArgType)) {
			throw new IllegalArgumentException("the comparison operator %s is called with non homogeneous arguments: %s and %s"
					.formatted(kind, firstArgType, secondArgType));
		}

		final Comparison comparison = firstArgType instanceof WhyPrimitive
				? Comparison.ofPrimitive((WhyPrimitive) firstArgType, kind)
				: Comparison.ofObject(kind);

		return new BinaryExpression(comparison, arguments.get(0), arguments.get(1));
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
