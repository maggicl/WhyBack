package byteback.whyml.syntax.function;

import byteback.whyml.printer.Code;
import static byteback.whyml.printer.Code.indent;
import static byteback.whyml.printer.Code.line;
import static byteback.whyml.printer.Code.many;
import byteback.whyml.syntax.statement.CFGTerminator;
import byteback.whyml.syntax.statement.CFGStatement;
import java.util.List;
import java.util.stream.Stream;

public record CFGBlock(CFGLabel label, List<CFGStatement> cfgStatements, CFGTerminator terminator) {
	public Stream<CFGStatement> allStatements() {
		return Stream.concat(cfgStatements.stream(), Stream.of(terminator));
	}

	public Code toWhy() {
		return many(
				line("%s {".formatted(label.name())),
				indent(
						many(cfgStatements.stream().map(CFGStatement::toWhy)),
						terminator.toWhy()
				),
				line("}")
		);
	}

	public CFGLabel fallThroughLabel() {
		return new CFGLabel(label().number() + 1);
	}
}
