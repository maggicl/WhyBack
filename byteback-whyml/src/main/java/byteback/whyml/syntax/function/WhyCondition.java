package byteback.whyml.syntax.function;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.syntax.expr.Expression;

public sealed abstract class WhyCondition {

	public abstract void visit(WhyCondition.Visitor visitor);

	public interface Visitor {
		default void visit(WhyCondition cond) {
			cond.visit(this);
		}

		void visitRequires(WhyCondition.Requires r);

		void visitEnsures(WhyCondition.Ensures r);

		void visitDecreases(WhyCondition.Decreases r);

		void visitReturns(WhyCondition.Returns r);

		void visitRaises(WhyCondition.Raises r);
	}

	public static final class Requires extends WhyCondition {
		private final Expression value;

		public Requires(Expression value) {
			this.value = value;
		}

		public Expression value() {
			return value;
		}

		@Override
		public void visit(Visitor visitor) {
			visitor.visitRequires(this);
		}
	}

	public static final class Ensures extends WhyCondition {
		private final Expression value;

		public Ensures(Expression value) {
			this.value = value;
		}

		public Expression value() {
			return value;
		}

		@Override
		public void visit(Visitor visitor) {
			visitor.visitEnsures(this);
		}
	}

	public static final class Decreases extends WhyCondition {
		private final Expression value;

		public Decreases(Expression value) {
			this.value = value;
		}

		public Expression value() {
			return value;
		}

		@Override
		public void visit(Visitor visitor) {
			visitor.visitDecreases(this);
		}
	}

	public static final class Returns extends WhyCondition {
		private final Expression when;

		public Returns(Expression when) {
			this.when = when;
		}

		public Expression when() {
			return when;
		}

		@Override
		public void visit(Visitor visitor) {
			visitor.visitReturns(this);
		}
	}

	public static final class Raises extends WhyCondition {
		private final Expression when;
		private final Identifier.FQDN exception;

		public Raises(Expression when, Identifier.FQDN exception) {
			this.when = when;
			this.exception = exception;
		}

		public Expression getWhen() {
			return when;
		}

		public Identifier.FQDN getException() {
			return exception;
		}

		@Override
		public void visit(Visitor visitor) {
			visitor.visitRaises(this);
		}
	}
}
