package byteback.whyml.printer;

import byteback.whyml.identifiers.Identifier;
import static byteback.whyml.printer.SExpr.infix;
import static byteback.whyml.printer.SExpr.prefix;
import static byteback.whyml.printer.SExpr.terminal;
import static byteback.whyml.printer.Statement.many;
import byteback.whyml.syntax.function.WhyCondition;
import java.util.ArrayList;
import java.util.List;

public class WhyConditionsPrinter implements WhyCondition.Visitor {
	private final List<Statement> requiresList = new ArrayList<>();
	private final List<Statement> ensuresList = new ArrayList<>();
	private final List<Statement> returnsList = new ArrayList<>();
	private final List<SExpr> raisesList = new ArrayList<>();

	@Override
	public void visitRequires(WhyCondition.Requires r) {
		requiresList.add(r.value().toWhy().statement("requires {", "}"));
	}

	@Override
	public void visitEnsures(WhyCondition.Ensures r) {
		ensuresList.add(r.value().toWhy().statement("ensures {", "}"));
	}

	@Override
	public void visitReturns(WhyCondition.Returns r) {
		// TODO: check if returns condition in WhyML denotes normal returns
		returnsList.add(r.when().toWhy().statement("returns {", "}"));
	}

	@Override
	public void visitRaises(WhyCondition.Raises r) {
		raisesList.add(
				infix("&&",
						infix(":>",
								prefix("Class", terminal(Identifier.Special.EXCEPTION_PARAM.toString())),
								prefix("Class", terminal(r.getException() + ".class"))
						),
						r.getWhen().toWhy()
				)
		);
	}

	public void print(List<WhyCondition> conditions) {
		for (final WhyCondition c : conditions) {
			visit(c);
		}
	}

	public Statement conditionStatements() {
		return many(
				many(requiresList.stream()),
				many(ensuresList.stream()),
				many(returnsList.stream()),
				many(raisesList.stream()
						.reduce((a, b) -> infix("||", a, b))
						.map(e -> e.statement("raises {", "}"))
						.stream())
		);
	}
}
