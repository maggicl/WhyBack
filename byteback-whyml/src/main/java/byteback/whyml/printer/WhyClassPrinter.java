package byteback.whyml.printer;

import byteback.whyml.Utils;
import byteback.whyml.identifiers.Identifier;
import byteback.whyml.identifiers.IdentifierEscaper;
import static byteback.whyml.printer.Statement.block;
import static byteback.whyml.printer.Statement.indent;
import static byteback.whyml.printer.Statement.line;
import static byteback.whyml.printer.Statement.lines;
import static byteback.whyml.printer.Statement.many;
import byteback.whyml.syntax.WhyClass;
import byteback.whyml.syntax.type.WhyReference;
import byteback.whyml.vimp.WhyResolver;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class WhyClassPrinter {
	public static final Map<Identifier.FQDN, String> SPECIAL_TYPES = Map.of(
			Identifier.Special.OBJECT, "Type.root",
			Identifier.Special.STRING, "Type.string"
	);

	private final WhyFieldPrinter printer;

	public WhyClassPrinter(WhyFieldPrinter printer) {
		this.printer = printer;
	}

	public WhyClassDeclaration toWhy(final WhyClass clazz, final WhyResolver resolver) {
		final String hierarchyStatements = Utils.trimToNull(clazz.superNames()
				.filter(e -> resolver.isResolved(new WhyReference(e)) && !e.equals(Identifier.Special.OBJECT))
				.map("(Class class :> Class %s.class)"::formatted)
				.collect(Collectors.joining(" &&\n")));

		final Statement hierarchy = hierarchyStatements == null
				? many()
				: many(
						line("axiom hierarchy" + IdentifierEscaper.PRELUDE_RESERVED + ":"),
						indent(lines(Arrays.stream(hierarchyStatements.split("\n"))))
		);

		final WhyClassScope scope = new WhyClassScope(clazz.name());

		final String classConstantStatement = Optional.ofNullable(SPECIAL_TYPES.get(clazz.name()))
				.map("let constant class: Type.class = %s"::formatted)
				.orElse("val constant class: Type.class");

		return new WhyClassDeclaration(
				scope.with(
						block(line(classConstantStatement)),
						hierarchy
				),
				clazz.fields().isEmpty() ? Optional.empty() : Optional.of(scope.with(
						many(clazz.fields().stream().map(e -> printer.toWhy(e, resolver)))
				))
		);
	}
}
