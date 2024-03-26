package byteback.mlcfg.printer;

import static byteback.mlcfg.printer.Statement.indent;
import static byteback.mlcfg.printer.Statement.line;
import static byteback.mlcfg.printer.Statement.many;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public sealed abstract class SExpr {
	private static final int MAX_LENGTH = 80;

	private static <T> Stream<T> allButLast(List<T> list) {
		return list.isEmpty() ? Stream.of() : list.stream().limit(list.size() - 1);
	}

	private static <T> T last(List<T> list) {
		return list.get(list.size() - 1);
	}

	public static SExpr terminal(String line) {
		return new Terminal(line);
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

	public final Statement statement() {
		return statement("", "");
	}

	public final Statement statement(String prefix, String postfix) {
		if (opLength() <= MAX_LENGTH) {
			return line(prefix + toLine() + postfix);
		} else {
			return withParens(prefix, postfix);
		}
	}

	protected abstract String toLine();

	protected abstract Statement withParens(String prefix, String postfix);

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
		protected Statement withParens(String prefix, String postfix) {
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
			return Stream.concat(Stream.of(op), exprs.stream().map(SExpr::toLine))
					.collect(Collectors.joining(" ", "(", ")"));
		}

		@Override
		public Statement withParens(String prefix, String postfix) {
			return many(
					line(prefix + "(" + op),
					indent(many(allButLast(exprs).map(SExpr::statement))),
					indent(many(last(exprs).statement("", ")" + postfix)))
			);
		}

		@Override
		protected int opLength() {
			return op.length() + exprs.stream().mapToInt(SExpr::opLength).sum();
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
			return Stream.of(left.toLine(), op, right.toLine())
					.collect(Collectors.joining(" ", "(", ")"));
		}

		@Override
		protected Statement withParens(String prefix, String postfix) {
			return many(
					left.statement(prefix + "(", ""),
					line(op),
					right.statement("", ")" + postfix)
			);
		}

		@Override
		protected int opLength() {
			return op.length() + left.opLength() + right.opLength();
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
		protected Statement withParens(String prefix, String postfix) {
			return many(conditional.statement(prefix + "if ", " then"),
					thenBranch.statement("", ""),
					elseBranch.statement("else ", postfix)
			);
		}

		@Override
		protected int opLength() {
			return conditional.opLength() + thenBranch.opLength() + elseBranch.opLength();
		}
	}
}
