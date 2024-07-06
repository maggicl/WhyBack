package byteback.whyml.syntax;

import byteback.frontend.boogie.ast.Printable;
import byteback.whyml.printer.Code;
import static byteback.whyml.printer.Code.block;
import static byteback.whyml.printer.Code.indent;
import static byteback.whyml.printer.Code.line;
import static byteback.whyml.printer.Code.many;
import java.util.List;

public record WhyProgram(Code program, HeapKind kind) implements Printable {
	private Code getImports() {
		final List<String> primitives = List.of("Z", "B", "S", "C", "I", "J", "F", "D");

		return block(
				line("(* prelude import *)"),
				line("use prelude.ptr.Ptr"),
				line("use prelude.typing.Type"),
				line("use prelude.exceptions.Throw"),
				line("use export prelude.bootstrap.Bootstrap"),
				line("use export prelude.logic.Operators"),
				line("use export prelude.%s.Operators".formatted(kind.getName())),
				line("use export prelude.boolean.Operators"),
				line("use export prelude.ptr.Operators"),
				many(primitives.stream().map(e -> line("use prelude.heap_%s.%s".formatted(kind.getName(), e)))),
				line("use prelude.heap_%s.L".formatted(kind.getName())),
				many(primitives.stream().map(e -> line("use prelude.heap_%s.R%s".formatted(kind.getName(), e)))),
				line("use prelude.heap_%s.Heap".formatted(kind.getName()))
		);
	}

	@Override
	public void print(StringBuilder b) {
		b.append(block(
				line("module Program"),
				indent(getImports(), program),
				line("end")
		));
	}
}