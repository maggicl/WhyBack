package byteback.whyml.printer;

import byteback.whyml.identifiers.Identifier;
import static byteback.whyml.printer.Code.block;
import static byteback.whyml.printer.Code.indent;
import static byteback.whyml.printer.Code.line;
import byteback.whyml.syntax.field.WhyField;
import byteback.whyml.syntax.type.WhyJVMType;
import byteback.whyml.syntax.type.WhyType;
import byteback.whyml.vimp.WhyResolver;

public class WhyFieldPrinter {
	public Code toWhy(WhyField field, WhyResolver resolver) {
		final Identifier.U name = field.getName();
		final WhyType fieldType = field.getType();

		if (fieldType instanceof WhyJVMType) {
			return block(fieldType.getPreludeType()
					.statement("clone prelude.heap.Field as %s with val f = ".formatted(name), ", axiom of"));
		} else {
			final String declaration = "let constant f: Type.t = ";

			final boolean notResolved = !resolver.isClassResolved(field.getType());
			final String prefix = declaration + (notResolved ? "Type.unknown (* " : "");
			final String postfix = notResolved ? " *)" : "";

			return block(
					line("scope %s".formatted(name)),
					indent(
							fieldType.getPreludeType().statement(prefix, postfix),
							line("clone export prelude.heap.PtrField with val f = f, axiom of")
					),
					line("end")
			);
		}
	}
}
