package byteback.whyml.printer;

import byteback.whyml.identifiers.Identifier;
import static byteback.whyml.printer.Code.indent;
import static byteback.whyml.printer.Code.line;
import static byteback.whyml.printer.Code.many;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public sealed abstract class SExpr {
	private static final int MAX_LENGTH = 80;

	private static <T> Stream<T> allButLast(List<T> list) {
		return list.isEmpty() ? Stream.of() : list.stream().limit(list.size() - 1);
	}

	private static String lineParens(SExpr t) {
		if (t instanceof Terminal) {
			return t.toLine();
		} else {
			return "(%s)".formatted(t.toLine());
		}
	}

	private static Code multilineParens(SExpr t) {
		return multilineParens(t, "", "");
	}

	private static Code multilineParens(SExpr t, String prefix, String postfix) {
		return t instanceof Terminal
				? t.statement(prefix, postfix)
				: t.statement(prefix + "(", ")" + postfix);
	}

	private static <T> T last(List<T> list) {
		return list.get(list.size() - 1);
	}

	public static SExpr terminal(String line) {
		return new Terminal(line);
	}

	public static SExpr terminal(Identifier.L line) {
		return new Terminal(line.toString());
	}

	public static SExpr prefix(String op, SExpr... exprs) {
		return new Prefix(op, Arrays.asList(exprs));
	}

	public static SExpr prefix(String op, Stream<SExpr> exprs) {
		return new Prefix(op, exprs.toList());
	}

	public static SExpr infix(String op, SExpr left, SExpr right) {
		return new Infix(op, left, right);
	}

	public static SExpr conditional(SExpr op, SExpr left, SExpr right) {
		return new Conditional(op, left, right);
	}

	public static SExpr natMapping(List<SExpr> elements) {
		return new NatMapping(elements);
	}

	public static SExpr switchExpr(SExpr test, List<Map.Entry<String, SExpr>> branches) {
		return new Switch(test, branches);
	}

	public final Code statement() {
		return statement("", "");
	}

	public final Code statement(String prefix, String postfix) {
		int op = opLength();

		if (op > 0 && prefix.length() + op + postfix.length() <= MAX_LENGTH) {
			return line(prefix + toLine() + postfix);
		} else {
			return withParens(prefix, postfix);
		}
	}

	protected abstract String toLine();

	protected abstract Code withParens(String prefix, String postfix);

	protected abstract int opLength();

	private static final class Terminal extends SExpr {
		private final String value;

		private Terminal(String value) {
			this.value = value;
		}

		@Override
		protected String toLine() {
			return value;
		}

		@Override
		protected Code withParens(String prefix, String postfix) {
			return line(prefix + value + postfix);
		}

		@Override
		protected int opLength() {
			return value.length();
		}
	}

	private static final class Prefix extends SExpr {
		private final String op;
		private final List<SExpr> exprs;

		private Prefix(String op, List<SExpr> exprs) {
			this.op = op;
			this.exprs = exprs;
		}

		@Override
		protected String toLine() {
			return Stream.concat(Stream.of(op), exprs.stream().map(SExpr::lineParens))
					.collect(Collectors.joining(" "));
		}

		@Override
		public Code withParens(String prefix, String postfix) {
			return many(
					line(prefix + op),
					indent(many(allButLast(exprs).map(SExpr::multilineParens))),
					indent(many(last(exprs).statement("(", ")" + postfix)))
			);
		}

		@Override
		protected int opLength() {
			return op.length() + exprs.stream().mapToInt(SExpr::opLength).sum() + "( )".length();
		}
	}

	public static final class Infix extends SExpr {
		private final String op;
		private final SExpr left;
		private final SExpr right;

		private Infix(String op, SExpr left, SExpr right) {
			this.op = op;
			this.left = left;
			this.right = right;
		}

		@Override
		protected String toLine() {
			return String.join(" ", lineParens(left), op, lineParens(right));
		}

		@Override
		protected Code withParens(String prefix, String postfix) {
			return many(
					multilineParens(left, prefix, ""),
					multilineParens(right, "%s ".formatted(op), postfix)
			);
		}

		@Override
		protected int opLength() {
			return op.length() + left.opLength() + right.opLength() + "()()".length();
		}
	}

	private static final class Conditional extends SExpr {
		private final SExpr conditional;
		private final SExpr thenBranch;
		private final SExpr elseBranch;

		private Conditional(SExpr conditional, SExpr thenBranch, SExpr elseBranch) {
			this.conditional = conditional;
			this.thenBranch = thenBranch;
			this.elseBranch = elseBranch;
		}


		@Override
		protected String toLine() {
			return "if %s then %s else %s".formatted(conditional, thenBranch.toLine(), elseBranch.toLine());
		}

		@Override
		protected Code withParens(String prefix, String postfix) {
			return many(conditional.statement(prefix + "if ", " then"),
					indent(thenBranch.statement("", ""),
							elseBranch.statement("else ", postfix))
			);
		}

		@Override
		protected int opLength() {
			return conditional.opLength() + thenBranch.opLength() + elseBranch.opLength() + "if then else ".length();
		}
	}

	private static final class NatMapping extends SExpr {
		private final List<SExpr> elements;

		private NatMapping(List<SExpr> elements) {
			this.elements = elements;
		}

		@Override
		protected String toLine() {
			return elements.stream()
					.map(SExpr::toLine)
					.collect(Collectors.joining("; ", "[|", "|]"));
		}

		@Override
		protected Code withParens(String prefix, String postfix) {
			return many(
					line("[|"),
					indent(many(elements.stream().map(SExpr::statement))),
					line("|]")
			);
		}

		@Override
		protected int opLength() {
			return elements.stream().mapToInt(SExpr::opLength).sum()
					+ (elements.size() - 1) * "; ".length()
					+ "[||]".length();
		}
	}

	private static final class Switch extends SExpr {
		private final SExpr test;
		private final List<Map.Entry<String, SExpr>> branches;

		private Switch(SExpr test, List<Map.Entry<String, SExpr>> branches) {
			this.test = test;
			this.branches = branches;
		}

		@Override
		protected String toLine() {
			throw new UnsupportedOperationException("switch expression is always multiline");
		}

		@Override
		protected Code withParens(String prefix, String postfix) {
			return many(
					test.statement(prefix + "switch (", ")"),
					many(branches.stream()
							.map(e -> e.getValue().statement("| %s -> ".formatted(e.getKey()), ""))),
					line("end")
			);
		}

		@Override
		protected int opLength() {
			// switch is always multiline
			return -1;
		}
	}
}
