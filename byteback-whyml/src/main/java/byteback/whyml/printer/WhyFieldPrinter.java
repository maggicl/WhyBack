package byteback.whyml.printer;

import byteback.whyml.identifiers.Identifier;
import static byteback.whyml.printer.Statement.block;
import static byteback.whyml.printer.Statement.indent;
import static byteback.whyml.printer.Statement.line;
import byteback.whyml.syntax.field.WhyField;
import byteback.whyml.syntax.type.WhyJVMType;
import byteback.whyml.syntax.type.WhyType;
import byteback.whyml.vimp.WhyResolver;

public class WhyFieldPrinter {
	public Statement toWhy(WhyField field, WhyResolver resolver) {
		final Identifier.U name = field.getName();
		final WhyType fieldType = field.getType();
		final String preludeType = fieldType.getPreludeType();

		if (fieldType instanceof WhyJVMType) {
			return block(
					line("clone prelude.heap.Field as %s with val f = %s, axiom of".formatted(name, preludeType))
			);
		} else {
			final String resolvedType = resolver.isResolved(field.getType())
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
