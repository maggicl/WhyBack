package byteback.whyml.syntax.function;

import byteback.whyml.printer.Code;
import static byteback.whyml.printer.Code.indent;
import static byteback.whyml.printer.Code.line;
import static byteback.whyml.printer.Code.many;
import byteback.whyml.syntax.statement.CFGTerminator;
import byteback.whyml.syntax.statement.CFGStatement;
import java.util.List;

public record CFGBlock(CFGLabel label, List<CFGStatement> CFGStatements, CFGTerminator terminator) {
	public Code toWhy() {
		return many(
				line("%s {".formatted(label.name())),
				indent(
						many(CFGStatements.stream().map(CFGStatement::toWhy)),
						terminator.toWhy()
				),
				line("}")
		);
	}

	public CFGLabel fallThroughLabel() {
		return new CFGLabel(label().number() + 1);
	}
}
