package byteback.mlcfg.printer;

import byteback.mlcfg.Utils;
import byteback.mlcfg.identifiers.IdentifierEscaper;
import static byteback.mlcfg.printer.Statement.block;
import static byteback.mlcfg.printer.Statement.line;
import static byteback.mlcfg.printer.Statement.many;
import byteback.mlcfg.syntax.WhyClass;
import byteback.mlcfg.syntax.types.WhyReference;
import byteback.mlcfg.vimp.WhyResolver;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WhyClassPrinter {
	private final WhyFieldPrinter printer;

	public WhyClassPrinter(WhyFieldPrinter printer) {
		this.printer = printer;
	}

	public WhyClassDeclaration toWhy(final WhyClass clazz, final WhyResolver resolver) {
		final String hierarchyStatement = clazz.superNames()
				.filter(e -> resolver.isResolved(new WhyReference(e)))
				.map("(Class class :> Class %s.class)"::formatted)
				.collect(Collectors.joining(" && "));

		final Stream<Statement> hierarchy = Stream.ofNullable(Utils.trimToNull(hierarchyStatement))
				.map(e -> line("axiom hierarchy%s: %s".formatted(IdentifierEscaper.PRELUDE_RESERVED, e)));

		final WhyClassScope scope = new WhyClassScope(clazz.name());

		return new WhyClassDeclaration(
				scope.with(
						block(line("val constant class: Type.class")),
						many(hierarchy)
				),
				clazz.fields().isEmpty() ? Optional.empty() : Optional.of(scope.with(
						many(clazz.fields().stream().map(e -> printer.toWhy(e, resolver)))
				))
		);
	}
}
