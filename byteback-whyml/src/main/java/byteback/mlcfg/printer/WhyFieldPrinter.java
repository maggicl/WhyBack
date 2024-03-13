package byteback.mlcfg.printer;

import byteback.mlcfg.syntax.WhyFieldDeclaration;
import byteback.mlcfg.syntax.WhyProgram;
import byteback.mlcfg.syntax.types.WhyPrimitive;
import byteback.mlcfg.syntax.types.WhyType;

public class WhyFieldPrinter {
	public String toWhy(WhyFieldDeclaration field) {
		final WhyType fieldType = field.type();

		if (fieldType instanceof WhyPrimitive) {
			return "clone prelude.heap.Field as %s with val f = %s, axiom of\n"
					.formatted(field.name(), fieldType.getPreludeType());
		} else {
			// TODO
		}
	}
}
