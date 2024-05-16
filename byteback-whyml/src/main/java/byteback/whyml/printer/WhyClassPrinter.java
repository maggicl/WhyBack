package byteback.whyml.printer;

import byteback.whyml.Utils;
import byteback.whyml.identifiers.Identifier;
import byteback.whyml.identifiers.IdentifierEscaper;
import static byteback.whyml.printer.Code.block;
import static byteback.whyml.printer.Code.indent;
import static byteback.whyml.printer.Code.line;
import static byteback.whyml.printer.Code.lines;
import static byteback.whyml.printer.Code.many;
import byteback.whyml.syntax.WhyClass;
import byteback.whyml.syntax.type.WhyReference;
import byteback.whyml.vimp.WhyResolver;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class WhyClassPrinter {

	/**
	 * Set of types for which the `class` constant declaration is responsibility of the WhyML prelude. This is done as
	 * these types are used in prelude primitives
	 */
	public static final Set<Identifier.FQDN> BOOTSTRAP_TYPES = Set.of(
			Identifier.Special.OBJECT,
			Identifier.Special.STRING,
			Identifier.Special.CLASS,
			Identifier.Special.NULL_POINTER_EXCEPTION,
			Identifier.Special.CLASS_CAST_EXCEPTION,
			Identifier.Special.ARRAY_INDEX_OUT_OF_BOUNDS_EXCEPTION,
			Identifier.Special.NEGATIVE_ARRAY_SIZE_EXCEPTION,
			Identifier.Special.ARRAY_STORE_EXCEPTION
	);

	private final WhyFieldPrinter printer;

	public WhyClassPrinter(WhyFieldPrinter printer) {
		this.printer = printer;
	}

	public byteback.whyml.printer.WhyClassDeclaration toWhy(final WhyClass clazz, final WhyResolver resolver) {
		final boolean typeDeclared = BOOTSTRAP_TYPES.contains(clazz.name());

		final String classReference = "Class %sclass".formatted(typeDeclared ? (clazz.name() + ".") : "");

		final String hierarchyStatements = Utils.trimToNull(clazz.superNames()
				.filter(e -> resolver.isClassResolved(new WhyReference(e)) && !e.equals(Identifier.Special.OBJECT))
				.map(e -> "(%s :> Class %s.class)".formatted(classReference, e))
				.collect(Collectors.joining(" &&\n")));

		final Code hierarchy = hierarchyStatements == null
				? many()
				: many(
				line("axiom hierarchy" + IdentifierEscaper.PRELUDE_RESERVED + ":"),
				indent(lines(Arrays.stream(hierarchyStatements.split("\n"))))
		);

		final WhyClassScope scope = new WhyClassScope(clazz.name());

		return new byteback.whyml.printer.WhyClassDeclaration(
				// make sure we don't open a scope if we have no type information to put in
				typeDeclared && hierarchyStatements == null
						? many()
						: scope.with(typeDeclared ? many() : block(line("val constant class: Type.class")), hierarchy),
				clazz.fields().isEmpty() ? Optional.empty() : Optional.of(scope.with(
						many(clazz.fields().stream().map(e -> printer.toWhy(e, resolver)))
				))
		);
	}
}
