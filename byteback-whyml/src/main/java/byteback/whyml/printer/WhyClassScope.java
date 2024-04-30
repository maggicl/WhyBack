package byteback.whyml.printer;

import byteback.whyml.Utils;
import byteback.whyml.identifiers.Identifier;
import static byteback.whyml.printer.Code.block;
import static byteback.whyml.printer.Code.indent;
import static byteback.whyml.printer.Code.lines;
import java.util.List;

public class WhyClassScope {
	private final List<Identifier.U> identifiers;

	public WhyClassScope(Identifier.FQDN className) {
		this.identifiers = className.getIdentifiers();
	}

	public Code open() {
		return lines(identifiers.stream().map("scope %s"::formatted));
	}

	public Code close() {
		return lines(Utils.repeat(identifiers.size(), "end"));
	}

	public Code with(Code... lines) {
		return block(
				open(),
				indent(lines),
				close()
		);
	}
}
