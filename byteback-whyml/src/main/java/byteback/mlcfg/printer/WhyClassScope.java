package byteback.mlcfg.printer;

import byteback.mlcfg.Utils;
import byteback.mlcfg.identifiers.Identifier;
import static byteback.mlcfg.printer.Statement.block;
import static byteback.mlcfg.printer.Statement.indent;
import static byteback.mlcfg.printer.Statement.lines;
import java.util.List;

public class WhyClassScope {
	private final List<Identifier.U> identifiers;

	public WhyClassScope(Identifier.FQDN className) {
		this.identifiers = className.getIdentifiers();
	}

	public Statement open() {
		return lines(identifiers.stream().map("scope %s"::formatted));
	}

	public Statement close() {
		return lines(Utils.repeat(identifiers.size(), "end"));
	}

	public Statement with(Statement... lines) {
		return block(
				open(),
				indent(lines),
				close()
		);
	}
}
