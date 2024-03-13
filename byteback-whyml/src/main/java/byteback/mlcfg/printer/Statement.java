package byteback.mlcfg.printer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public sealed abstract class Statement {
	public static final String INDENTATION = "  ";

	public static Statement line(String line) {
		return new Single(line);
	}

	public static Statement many(Statement... lines) {
		return new Multiple(Arrays.asList(lines), 0);
	}

	public static Statement lines(Stream<String> lines) {
		return new Multiple(lines.map(Single::new).toList(), 0);
	}

	public static Statement many(Stream<Statement> lines) {
		return new Multiple(lines.toList(), 0);
	}

	public static Statement scope(Statement... lines) {
		return new Multiple(Arrays.asList(lines), 1);
	}

	protected abstract int getIndent();

	protected abstract Stream<String> getLines();

	@Override
	public String toString() {
		return getLines().collect(Collectors.joining("\n"));
	}

	public static final class Single extends Statement {
		private final String line;

		private Single(String line) {
			this.line = line;
		}

		@Override
		protected int getIndent() {
			return 0;
		}

		@Override
		protected Stream<String> getLines() {
			return Stream.of(line);
		}
	}

	public static final class Multiple extends Statement {
		private final List<? extends Statement> statements;
		private final int indent;

		private Multiple(List<? extends Statement> statements, int indent) {
			this.statements = statements;
			this.indent = indent;
		}

		@Override
		protected int getIndent() {
			return indent;
		}

		@Override
		protected Stream<String> getLines() {
			return statements.stream()
					.flatMap(Statement::getLines)
					.map(e -> INDENTATION.repeat(indent) + e);
		}
	}
}
