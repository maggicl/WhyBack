package byteback.mlcfg.printer;

import byteback.mlcfg.identifiers.Identifier;
import static byteback.mlcfg.printer.Statement.block;
import static byteback.mlcfg.printer.Statement.indent;
import static byteback.mlcfg.printer.Statement.line;
import byteback.mlcfg.syntax.WhyField;
import byteback.mlcfg.syntax.types.WhyPrimitive;
import byteback.mlcfg.syntax.types.WhyType;
import byteback.mlcfg.vimp.WhyResolver;

public class WhyFieldPrinter {
	public Statement toWhy(WhyField field, WhyResolver resolver, Identifier.FQDN currentScope) {
		final Identifier.U name = field.name();
		final WhyType fieldType = field.type();
		final String preludeType = fieldType.getPreludeType(currentScope);

		if (fieldType instanceof WhyPrimitive) {
			return block(
					line("clone prelude.heap.Field as %s with val f = %s, axiom of".formatted(name, preludeType))
			);
		} else {
			final String resolvedType = resolver.isResolved(field.type())
					? preludeType
					: "Type.unknown (* %s *)".formatted(preludeType);

			return block(
					line("scope %s".formatted(name)),
					indent(
							line("let constant f: Type.t = %s".formatted(resolvedType)),
							line("clone export prelude.heap.PtrField with val f = f, axiom of")
					),
					line("end")
			);
		}
	}
}
