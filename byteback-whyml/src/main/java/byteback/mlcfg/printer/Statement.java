package byteback.mlcfg.printer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public sealed abstract class Statement {
	public static final String INDENTATION = "  ";

	public static Statement line(String line) {
		return new Single(line);
	}

	public static Statement block(Statement... lines) {
		return new Multiple(Arrays.asList(lines), 0, true);
	}

	public static Statement block(Stream<Statement> lines) {
		return new Multiple(lines.toList(), 0, true);
	}

	public static Statement lines(Stream<String> lines) {
		return new Multiple(lines.map(Single::new).toList(), 0, false);
	}

	public static Statement many(Stream<Statement> lines) {
		return new Multiple(lines.toList(), 0, false);
	}

	public static Statement many(Statement... lines) {
		return new Multiple(Arrays.asList(lines), 0, false);
	}

	public static Statement indent(Statement... lines) {
		return new Multiple(Arrays.asList(lines), 1, false);
	}

	protected abstract Stream<Line> getLines();

	@Override
	public String toString() {
		final List<Line> lines = getLines().toList();
		final StringBuilder sb = new StringBuilder();

		for (int i = 0; i < lines.size() - 1; i++) {
			final Line line = lines.get(i);
			final Line next = lines.get(i + 1);

			if (!line.text.isEmpty()) {
				sb.append(line).append('\n');
			} else if (line.indent == next.indent && !next.text.isEmpty()) {
				sb.append('\n');
			}
		}
		sb.append(lines.get(lines.size() - 1));

		return sb.toString();
	}

	protected static class Line {
		private final String text;
		private final int indent;

		private Line(String text, int indent) {
			this.text = text;
			this.indent = indent;
		}

		private Line indent(int howMuch) {
			return new Line(text, indent + howMuch);
		}

		public String toString() {
			return INDENTATION.repeat(indent) + text;
		}
	}

	public static final class Single extends Statement {
		private final String line;

		private Single(String line) {
			this.line = line;
		}

		@Override
		protected Stream<Line> getLines() {
			return Stream.of(new Line(line, 0));
		}
	}

	public static final class Multiple extends Statement {
		private final List<? extends Statement> statements;
		private final int indent;
		private final boolean newline;

		private Multiple(List<? extends Statement> statements, int indent, boolean newline) {
			this.statements = statements;
			this.indent = indent;
			this.newline = newline;
		}

		@Override
		protected Stream<Line> getLines() {
			final Stream<Line> lines = statements.stream()
					.flatMap(Statement::getLines)
					.map(e -> e.indent(indent));

			return newline ? Stream.concat(lines, Stream.of(new Line("", indent))) : lines;
		}
	}
}
