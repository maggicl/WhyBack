package byteback.mlcfg.printer;

import byteback.mlcfg.Utils;
import byteback.mlcfg.identifiers.Identifier;
import byteback.mlcfg.identifiers.IdentifierEscaper;
import static byteback.mlcfg.printer.Statement.block;
import static byteback.mlcfg.printer.Statement.indent;
import static byteback.mlcfg.printer.Statement.line;
import static byteback.mlcfg.printer.Statement.lines;
import static byteback.mlcfg.printer.Statement.many;
import byteback.mlcfg.syntax.WhyClass;
import byteback.mlcfg.syntax.types.WhyReference;
import byteback.mlcfg.vimp.WhyResolver;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jf.util.StringUtils;

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
