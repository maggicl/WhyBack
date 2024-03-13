package byteback.mlcfg.printer;

import byteback.mlcfg.identifiers.Identifier;
import static byteback.mlcfg.printer.Statement.block;
import static byteback.mlcfg.printer.Statement.indent;
import static byteback.mlcfg.printer.Statement.line;
import byteback.mlcfg.syntax.WhyFieldDeclaration;
import byteback.mlcfg.syntax.types.WhyPrimitive;
import byteback.mlcfg.syntax.types.WhyType;

public class WhyFieldPrinter {
	public Statement toWhy(WhyFieldDeclaration field) {
		final Identifier.U name = field.name();
		final WhyType fieldType = field.type();
		final String preludeType = fieldType.getPreludeType();

		if (fieldType instanceof WhyPrimitive) {
			return block(
					line("clone prelude.heap.Field as %s with val f = %s, axiom of".formatted(name, preludeType))
			);
		} else {
			return block(
					line("scope %s".formatted(name)),
					indent(
							line("let constant f: Type.t = %s".formatted(preludeType)),
							line("clone export prelude.heap.PtrField with val f = f, axiom of")
					),
					line("end")
			);
		}
	}
}
