package byteback.mlcfg.printer;

import byteback.mlcfg.identifiers.Identifier;
import static byteback.mlcfg.printer.Statement.block;
import static byteback.mlcfg.printer.Statement.indent;
import static byteback.mlcfg.printer.Statement.line;
import static byteback.mlcfg.printer.Statement.many;
import byteback.mlcfg.syntax.WhyFunctionKind;
import byteback.mlcfg.syntax.WhyFunctionParam;
import byteback.mlcfg.syntax.WhyFunctionSignature;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WhySignaturePrinter {

	public Statement toWhy(WhyFunctionSignature m) {
		final List<WhyFunctionParam> paramsList = m.params().toList();

		final String params = paramsList.stream()
				.map(e -> "(%s: %s)".formatted(e.name(), e.type().getWhyType()))
				.collect(Collectors.joining(" "));

		final Statement paramPreconditions = many(paramsList.stream()
				.map(WhyFunctionParam::condition)
				.flatMap(Optional::stream)
				.map(e -> line("requires { %s }".formatted(e))));

		final Statement resultPostcondition = many(
				new WhyFunctionParam(Identifier.Special.RESULT, m.returnType(), false)
						.condition()
						.map("ensures { %s }"::formatted)
						.map(Statement::line)
						.stream());


		return many(
				line("%s %s (%sheap: Heap.t) %s%s".formatted(
						m.kind().getWhyDeclaration(),
						m.name(),
						m.kind().isSpec() ? "" : "ghost ",
						params,
						m.kind() == WhyFunctionKind.PREDICATE ?
								"" :
								" : %s".formatted(m.returnType().getWhyType())
				)),
				indent(
						paramPreconditions,
						resultPostcondition
				)
		);
	}

	public Statement toWhy(Identifier.FQDN declaringClass, List<WhyFunctionSignature> methods) {
		final WhyClassScope scope = new WhyClassScope(declaringClass);
		return scope.with(block(methods.stream().map(this::toWhy).map(Statement::block)));
	}
}
