package byteback.whyml.syntax.statement;

import byteback.whyml.printer.SExpr;
import static byteback.whyml.printer.SExpr.conditional;
import static byteback.whyml.printer.SExpr.prefix;
import static byteback.whyml.printer.SExpr.switchEq;
import static byteback.whyml.printer.SExpr.terminal;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.function.CFGLabel;
import byteback.whyml.syntax.type.WhyJVMType;
import java.util.Map;

public sealed abstract class CFGTerminator {
	private static SExpr labelToWhy(CFGLabel label) {
		return terminal("goto %s".formatted(label.name()));
	}

	public abstract SExpr toWhy();

	public static final class Return extends CFGTerminator {
		private final Expression value;

		public Return(Expression value) {
			this.value = value;
		}

		@Override
		public SExpr toWhy() {
			return prefix("return", value.toWhy());
		}
	}

	public static final class Goto extends CFGTerminator {
		private final CFGLabel next;

		public Goto(CFGLabel next) {
			this.next = next;
		}

		@Override
		public SExpr toWhy() {
			return labelToWhy(next);
		}
	}

	public static final class If extends CFGTerminator {
		private final Expression expression;
		private final CFGLabel trueBranch;
		private final CFGLabel falseBranch;

		public If(Expression expression, CFGLabel trueBranch, CFGLabel falseBranch) {
			if (expression.type() != WhyJVMType.BOOL) {
				throw new IllegalArgumentException("Conditional expression " + expression +
						" in if CFG terminator must be of type BOOL");
			}

			this.expression = expression;
			this.trueBranch = trueBranch;
			this.falseBranch = falseBranch;
		}

		@Override
		public SExpr toWhy() {
			return conditional(expression.toWhy(), labelToWhy(trueBranch), labelToWhy(falseBranch));
		}
	}

	public static final class Switch extends CFGTerminator {
		private final Expression test;
		private final Map<Integer, CFGLabel> cases;

		public Switch(Expression test, Map<Integer, CFGLabel> cases) {
			if (!test.type().isWholeNumber() || test.type() == WhyJVMType.LONG) {
				throw new IllegalArgumentException("Conditional test " + test +
						" in switch CFG terminator must be of type BYTE, SHORT, CHAR, or INT");
			}

			this.test = test;
			this.cases = cases;
		}

		@Override
		public SExpr toWhy() {
			return switchEq(test.toWhy(), cases.entrySet().stream()
					.sorted(Map.Entry.comparingByKey())
					.map(e -> Map.entry(e.getKey(), CFGTerminator.labelToWhy(e.getValue())))
					.toList());
		}
	}
}
