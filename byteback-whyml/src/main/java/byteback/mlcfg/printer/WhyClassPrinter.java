package byteback.mlcfg.printer;

import byteback.mlcfg.Utils;
import byteback.mlcfg.syntax.WhyClassDeclaration;

import byteback.mlcfg.syntax.WhyFieldDeclaration;
import byteback.mlcfg.syntax.WhyProgram;

public class WhyClassPrinter {
    private final WhyFieldPrinter printer;

	public WhyClassPrinter(WhyFieldPrinter printer) {
		this.printer = printer;
	}

    public String toWhy(final WhyClassDeclaration clazz) {
		final StringBuilder sb = new StringBuilder();

		final String[] scopes = clazz.name().split("\\.");

		// build scope opening
		for (final String scope : scopes) {
			sb.append("scope ").append(scope).append('\n');
		}

		// declare the class identifier for this class
		sb.append(WhyProgram.INDENT).append("val constant class: Type.class\n\n");

		// define all fields
		for (final WhyFieldDeclaration field : clazz.fields()) {
			sb.append(Utils.indent(printer.toWhy(field), 1));
		}

		// close the opened scopes
		sb.append("end\n".repeat(scopes.length));

		return sb.toString();
    }
}
