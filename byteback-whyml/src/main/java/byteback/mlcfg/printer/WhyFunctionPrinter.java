package byteback.mlcfg.printer;

import byteback.mlcfg.identifiers.Identifier;
import static byteback.mlcfg.printer.Statement.block;
import static byteback.mlcfg.printer.Statement.indent;
import static byteback.mlcfg.printer.Statement.line;
import static byteback.mlcfg.printer.Statement.many;
import byteback.mlcfg.syntax.WhyFunction;
import byteback.mlcfg.syntax.WhyFunctionKind;
import byteback.mlcfg.syntax.WhyFunctionParam;
import byteback.mlcfg.syntax.types.WhyReference;
import byteback.mlcfg.syntax.types.WhyType;
import java.util.List;
import java.util.stream.Collectors;

public class WhyFunctionPrinter {

	public Statement methodToWhy(WhyFunction m) {
		final List<WhyFunctionParam> paramsList = m.params().toList();

		final String params = paramsList.stream()
				.map(e -> "(%s: %s)".formatted(e.name(), e.type().getWhyType()))
				.collect(Collectors.joining(" "));

		final Statement paramPreconditions = many(paramsList.stream()
				.filter(e -> e.type() instanceof WhyReference)
				.map(e -> line("requires { %s }".formatted(e.condition()))));

		final Statement resultPostcondition = many(m.returnType().stream()
				.filter(e -> e instanceof WhyReference)
				.map(e -> line("ensures { %s }".formatted(
						new WhyFunctionParam(Identifier.Special.RESULT, e, false)
								.condition()))));



		return block(
				line("%s %s (ghost heap: Heap.t) %s%s".formatted(
						m.kind().getWhyDeclaration(),
						m.name(),
						params,
						m.kind() == WhyFunctionKind.PREDICATE ? "" : " : %s".formatted(
								m.returnType().map(WhyType::getWhyType).orElse("unit")
						)
				)),
				indent(
						paramPreconditions,
						resultPostcondition
				)
		);
	}

	public Statement toWhy(Identifier.FQDN declaringClass, List<WhyFunction> methods) {
		final WhyClassScope scope = new WhyClassScope(declaringClass);
		return scope.with(block(methods.stream().map(this::methodToWhy)));
	}
}
