package byteback.whyml.syntax.function;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.syntax.expr.transformer.ExpressionVisitor;

public sealed abstract class WhyCondition {

	public abstract void visit(WhyCondition.Visitor visitor);
	public abstract void visit(ExpressionVisitor visitor);

	public abstract WhySideEffects sideEffects();

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
		private final WhyFunctionBody.SpecBody value;

		public Requires(WhyFunctionBody.SpecBody value) {
			this.value = value;
		}

		public WhyFunctionBody.SpecBody value() {
			return value;
		}

		@Override
		public void visit(Visitor visitor) {
			visitor.visitRequires(this);
		}

		@Override
		public void visit(ExpressionVisitor visitor) {
			value.accept(visitor);
		}

		@Override
		public WhySideEffects sideEffects() {
			return value.sideEffects();
		}
	}

	public static final class Ensures extends WhyCondition {
		private final WhyFunctionBody.SpecBody value;

		public Ensures(WhyFunctionBody.SpecBody value) {
			this.value = value;
		}

		public WhyFunctionBody.SpecBody value() {
			return value;
		}

		@Override
		public void visit(Visitor visitor) {
			visitor.visitEnsures(this);
		}

		@Override
		public void visit(ExpressionVisitor visitor) {
			value.accept(visitor);
		}

		@Override
		public WhySideEffects sideEffects() {
			return value.sideEffects();
		}
	}

	public static final class Decreases extends WhyCondition {
		private final WhyFunctionBody.SpecBody value;

		public Decreases(WhyFunctionBody.SpecBody value) {
			this.value = value;
		}

		public WhyFunctionBody.SpecBody value() {
			return value;
		}

		@Override
		public void visit(Visitor visitor) {
			visitor.visitDecreases(this);
		}

		@Override
		public void visit(ExpressionVisitor visitor) {
			value.accept(visitor);
		}

		@Override
		public WhySideEffects sideEffects() {
			return value.sideEffects();
		}
	}

	public static final class Returns extends WhyCondition {
		private final WhyFunctionBody.SpecBody when;

		public Returns(WhyFunctionBody.SpecBody when) {
			this.when = when;
		}

		public WhyFunctionBody.SpecBody when() {
			return when;
		}

		@Override
		public void visit(Visitor visitor) {
			visitor.visitReturns(this);
		}

		@Override
		public void visit(ExpressionVisitor visitor) {
			when.accept(visitor);
		}

		@Override
		public WhySideEffects sideEffects() {
			return when.sideEffects();
		}
	}

	public static final class Raises extends WhyCondition {
		private final WhyFunctionBody.SpecBody when;
		private final Identifier.FQDN exception;

		public Raises(WhyFunctionBody.SpecBody when, Identifier.FQDN exception) {
			this.when = when;
			this.exception = exception;
		}

		public WhyFunctionBody.SpecBody getWhen() {
			return when;
		}

		public Identifier.FQDN getException() {
			return exception;
		}

		@Override
		public void visit(Visitor visitor) {
			visitor.visitRaises(this);
		}

		@Override
		public void visit(ExpressionVisitor visitor) {
			when.accept(visitor);
		}

		@Override
		public WhySideEffects sideEffects() {
			return when.sideEffects();
		}
	}
}
