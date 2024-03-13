package byteback.mlcfg.printer;

import byteback.mlcfg.Utils;
import byteback.mlcfg.identifiers.Identifier;
import static byteback.mlcfg.printer.Statement.line;
import static byteback.mlcfg.printer.Statement.lines;
import static byteback.mlcfg.printer.Statement.many;
import static byteback.mlcfg.printer.Statement.scope;
import byteback.mlcfg.syntax.WhyClassDeclaration;
import java.util.List;

public class WhyClassPrinter {
	private final WhyFieldPrinter printer;

	public WhyClassPrinter(WhyFieldPrinter printer) {
		this.printer = printer;
	}

	public Statement toWhy(final WhyClassDeclaration clazz) {
		final List<Identifier.U> identifiers = clazz.name().getIdentifiers();

		return many(
				lines(identifiers.stream().map("scope %s"::formatted)),
				scope(
						line("val constant class: Type.class"),
						many(clazz.fields().stream().map(printer::toWhy))
				),
				lines(Utils.repeat(identifiers.size(), "end"))
		);
	}
}
