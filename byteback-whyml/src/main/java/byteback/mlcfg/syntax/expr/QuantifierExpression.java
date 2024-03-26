package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.printer.SExpr;
import static byteback.mlcfg.printer.SExpr.infix;
import static byteback.mlcfg.printer.SExpr.prefix;
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
	public SExpr toWhy() {
		final String whyVarList = variableList.stream()
				.map(e -> "%s: %s".formatted(e.name(), e.type().getWhyType()))
				.collect(Collectors.joining(", "));

		final Optional<SExpr> conditionList = variableList.stream()
				.map(WhyFunctionParam::condition)
				.flatMap(Optional::stream)
				.map(SExpr::terminal)
				.reduce((a, b) -> infix("&&", a, b));

		// TODO: consider splitting the binding variables in the quantifiers in terminals to make the line shorter
		final String bindings = "%s %s.".formatted(kind.symbol, whyVarList);

		return prefix(bindings, conditionList.map(sExpr -> infix("->", sExpr, inner.toWhy())).orElseGet(inner::toWhy));
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
