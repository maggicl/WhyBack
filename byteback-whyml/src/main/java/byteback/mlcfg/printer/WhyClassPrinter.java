package byteback.mlcfg.printer;

import byteback.mlcfg.Utils;
import byteback.mlcfg.identifiers.Identifier;
import static byteback.mlcfg.printer.Statement.block;
import static byteback.mlcfg.printer.Statement.indent;
import static byteback.mlcfg.printer.Statement.line;
import static byteback.mlcfg.printer.Statement.lines;
import static byteback.mlcfg.printer.Statement.many;
import byteback.mlcfg.syntax.WhyClass;
import byteback.mlcfg.vimp.WhyResolver;
import java.util.List;

public class WhyClassPrinter {
	private final WhyFieldPrinter printer;

	public WhyClassPrinter(WhyFieldPrinter printer) {
		this.printer = printer;
	}

	public Statement toWhy(final WhyClass clazz, final WhyResolver resolver) {
		final List<Identifier.U> identifiers = clazz.name().getIdentifiers();

		return block(
				lines(identifiers.stream().map("scope %s"::formatted)),
				indent(
						block(line("val constant class: Type.class")),
						many(clazz.fields().stream().map(e -> printer.toWhy(e, resolver, clazz.name())))
				),
				lines(Utils.repeat(identifiers.size(), "end"))
		);
	}
}
