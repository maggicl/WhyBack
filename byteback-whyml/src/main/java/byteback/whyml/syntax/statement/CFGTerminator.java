package byteback.whyml.syntax.statement;

import byteback.whyml.printer.Code;
import byteback.whyml.printer.SExpr;
import static byteback.whyml.printer.SExpr.prefix;
import static byteback.whyml.printer.SExpr.switchExpr;
import static byteback.whyml.printer.SExpr.terminal;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.function.CFGLabel;
import byteback.whyml.syntax.statement.visitor.StatementVisitor;
import byteback.whyml.syntax.type.WhyJVMType;
import java.util.List;
import java.util.Map;

public sealed abstract class CFGTerminator implements CFGStatement {
	private static SExpr labelToWhy(CFGLabel label) {
		return terminal("goto %s".formatted(label.name()));
	}

	public abstract Code toWhy();

	public static final class Return extends CFGTerminator {
		private final Expression value;

		public Return(Expression value) {
			this.value = value;
		}

		public Expression value() {
			return value;
		}

		@Override
		public Code toWhy() {
			return prefix("return", value.toWhy(false)).statement();
		}

		@Override
		public void accept(StatementVisitor visitor) {
			visitor.visitReturnStatement(this);
		}
	}

	public static final class Throw extends CFGTerminator {
		private final Expression value;

		public Throw(Expression value) {
			if (value.type() != WhyJVMType.PTR) {
				throw new IllegalArgumentException("throw exception expression must be a PTR, given " + value.type());
			}

			this.value = value;
		}

		public Expression value() {
			return value;
		}

		@Override
		public Code toWhy() {
			return prefix("return",
					prefix("jthrow", value.toWhy(false))
			).statement();
		}

		@Override
		public void accept(StatementVisitor visitor) {
			visitor.visitThrowStatement(this);
		}
	}

	public static final class Goto extends CFGTerminator {
		private final CFGLabel next;

		public Goto(CFGLabel next) {
			this.next = next;
		}

		@Override
		public Code toWhy() {
			return labelToWhy(next).statement();
		}

		@Override
		public void accept(StatementVisitor visitor) {
			visitor.visitGotoStatement(this);
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

		public Expression expression() {
			return expression;
		}

		@Override
		public Code toWhy() {
			return switchExpr(expression.toWhy(false), List.of(
					Map.entry("True", CFGTerminator.labelToWhy(trueBranch)),
					Map.entry("False", CFGTerminator.labelToWhy(falseBranch))
			)).statement();
		}

		@Override
		public void accept(StatementVisitor visitor) {
			visitor.visitIfStatement(this);
		}
	}
}
