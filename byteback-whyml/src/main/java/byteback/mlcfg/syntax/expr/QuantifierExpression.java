package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.syntax.WhyFunctionParam;
import byteback.mlcfg.syntax.types.WhyJVMType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class QuantifierExpression implements Expression {
	private final Kind kind;
	private final List<WhyFunctionParam> variableList;
	private final Expression inner;

	public QuantifierExpression(Kind kind, List<WhyFunctionParam> variableList, Expression inner) {
		if (variableList.isEmpty()) {
			throw new IllegalArgumentException("quantifier must have one or more variables");
		}

		this.kind = kind;
		this.variableList = variableList;
		this.inner = inner;
	}

	@Override
	public String toWhy() {
		final String whyVarList = variableList.stream()
				.map(e -> "%s: %s".formatted(e.name(), e.type().getWhyType()))
				.collect(Collectors.joining(", "));

		final String conditionList = variableList.stream()
				.map(WhyFunctionParam::condition)
				.flatMap(Optional::stream)
				.collect(Collectors.joining(" && "));

		if (conditionList.isEmpty()) {
			return "(%s %s. (%s))".formatted(
					kind.symbol,
					whyVarList,
					inner.toWhy()
			);
		} else {
			return "(%s %s. (%s) -> (%s))".formatted(
					kind.symbol,
					whyVarList,
					conditionList,
					inner.toWhy()
			);
		}
	}

	@Override
	public WhyJVMType type() {
		return WhyJVMType.BOOL;
	}

	public enum Kind {
		EXISTS("exists"),
		FORALL("forall");

		private final String symbol;

		Kind(String symbol) {
			this.symbol = symbol;
		}
	}
}
