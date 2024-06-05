package byteback.whyml.printer;

import byteback.whyml.identifiers.Identifier;
import static byteback.whyml.printer.Code.block;
import static byteback.whyml.printer.Code.line;
import byteback.whyml.syntax.field.WhyField;
import byteback.whyml.syntax.type.WhyJVMType;
import byteback.whyml.syntax.type.WhyType;
import byteback.whyml.vimp.WhyResolver;

public class WhyFieldPrinter {
	public Code toWhy(WhyField field, WhyResolver resolver) {
		final Identifier.L name = field.getName();
		final WhyType fieldType = field.getType();
		final WhyJVMType jvmType = field.getType().jvm();

		final Code decl = line("val constant %s: %s.%s".formatted(
				name,
				jvmType.getWhyAccessorScope(),
				field.isStatic() ? "static_field" : "instance_field"
		));

		if (jvmType == WhyJVMType.PTR) {
			final boolean notResolved = !resolver.isClassResolved(field.getType());

			return block(
					decl,
					line("axiom %s_type: %s.field_type = %s".formatted(
							name,
							name,
							fieldType.getPreludeType().statement(
									notResolved ? "Type.unknown (* " : "",
									notResolved ? " *)" : ""
							)
					))
			);
		} else {
			return block(decl);
		}
	}
}
