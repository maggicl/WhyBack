package byteback.mlcfg.printer;

import byteback.mlcfg.identifiers.Identifier;
import static byteback.mlcfg.printer.Statement.block;
import static byteback.mlcfg.printer.Statement.indent;
import static byteback.mlcfg.printer.Statement.line;
import static byteback.mlcfg.printer.Statement.many;
import byteback.mlcfg.syntax.WhyMethod;
import byteback.mlcfg.syntax.WhyMethodParam;
import byteback.mlcfg.syntax.types.WhyReference;
import byteback.mlcfg.syntax.types.WhyType;
import java.util.List;
import java.util.stream.Collectors;

public class WhyMethodPrinter {

	public Statement methodToWhy(WhyMethod m) {
		final List<WhyMethodParam> paramsList = m.params().toList();

		final String params = paramsList.stream()
				.map(e -> "(%s: %s)".formatted(e.name(), e.type().getWhyType()))
				.collect(Collectors.joining(" "));

		final Statement paramPreconditions = many(paramsList.stream()
				.filter(e -> e.type() instanceof WhyReference)
				.map(e -> line("requires { %s }".formatted(e.precondition()))));


		return many(
				line("val %s (ghost heap: Heap.t) %s : %s".formatted(
						m.name(),
						params,
						m.returnType().map(WhyType::getWhyType).orElse("unit")
				)),
				indent(paramPreconditions)
		);
	}

	public Statement toWhy(Identifier.FQDN declaringClass, List<WhyMethod> methods) {
		final WhyClassScope scope = new WhyClassScope(declaringClass);
		return scope.with(block(methods.stream().map(this::methodToWhy)));
	}
}
