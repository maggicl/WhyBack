package byteback.whyml.syntax.function;

import byteback.whyml.printer.Code;
import static byteback.whyml.printer.Code.indent;
import static byteback.whyml.printer.Code.line;
import static byteback.whyml.printer.Code.many;
import byteback.whyml.syntax.statement.CFGTerminator;
import byteback.whyml.syntax.statement.Statement;
import java.util.List;

public record CFGBlock(CFGLabel label, List<Statement> statements, CFGTerminator terminator) {
	public Code toWhy() {
		return many(
				line("%s {".formatted(label.name())),
				indent(
						many(statements.stream().map(Statement::toWhy)),
						terminator.toWhy().statement()
				),
				line("}")
		);
	}
}
