package byteback.whyml.syntax.statement;

import byteback.whyml.printer.Code;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.statement.visitor.StatementVisitor;
import byteback.whyml.syntax.type.WhyJVMType;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

public record CFGLogicalStatement(CFGLogicalStatement.Kind kind,
								  Optional<WhyLocation> location,
								  Expression expression,
								  boolean inferredAutomatically) implements CFGStatement {
	public CFGLogicalStatement {
		if (!kind.expressionTypes.contains(expression.type())) {
			throw new IllegalArgumentException("%s logical statements must have %s expression types, got %s"
					.formatted(kind.keyword, kind.expressionTypes, expression.type()));
		}
	}

	@Override
	public Code toWhy() {
		return expression.toWhy(true).statement(
				"%s { %s%s".formatted(kind.keyword,
						inferredAutomatically ? "[@expl:inferred] " : "",
						location.map(WhyLocation::toWhy)
						.map(e -> e + " ")
						.orElse("")),
				" };"
		);
	}

	@Override
	public void accept(StatementVisitor visitor) {
		visitor.visitLogicalStatement(this);
	}

	public enum Kind {
		ASSUME("assume", EnumSet.of(WhyJVMType.BOOL)),
		ASSERT("assert", EnumSet.of(WhyJVMType.BOOL)),
		INVARIANT("invariant", EnumSet.of(WhyJVMType.BOOL)),
		VARIANT("variant", EnumSet.of(
				WhyJVMType.BYTE, WhyJVMType.SHORT, WhyJVMType.CHAR, WhyJVMType.INT, WhyJVMType.LONG));


		private final String keyword;
		private final Set<WhyJVMType> expressionTypes;

		Kind(String keyword, Set<WhyJVMType> expressionTypes) {
			this.keyword = keyword;
			this.expressionTypes = expressionTypes;
		}
	}
}
