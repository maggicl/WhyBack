package byteback.whyml.syntax.expr.field;

import byteback.whyml.printer.SExpr;
import static byteback.whyml.printer.SExpr.terminal;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.field.WhyField;
import byteback.whyml.syntax.field.WhyInstanceField;
import byteback.whyml.syntax.field.WhyStaticField;
import byteback.whyml.syntax.type.WhyJVMType;

public sealed abstract class Access {

	public static Instance instance(Expression base, WhyInstanceField field) {
		return new Instance(base, field);
	}

	public static Static staticAccess(WhyStaticField field) {
		return new Static(field);
	}

	public abstract WhyField getField();

	public abstract SExpr referenceToWhy();

	public static final class Instance extends Access {
		private final Expression base;
		private final WhyInstanceField field;

		private Instance(Expression base, WhyInstanceField field) {
			if (base.type() != WhyJVMType.PTR) {
				throw new IllegalArgumentException("base expression must be a vimp");
			}

			this.base = base;
			this.field = field;
		}

		public Expression getBase() {
			return base;
		}

		@Override
		public WhyInstanceField getField() {
			return field;
		}

		@Override
		public SExpr referenceToWhy() {
			return base.toWhy();
		}

		@Override
		public String toString() {
			return "Access.Instance{" +
					"base=" + base +
					", field=" + field +
					'}';
		}
	}

	public static final class Static extends Access {
		private final WhyStaticField field;

		private Static(WhyStaticField field) {
			this.field = field;
		}

		@Override
		public WhyStaticField getField() {
			return field;
		}

		@Override
		public SExpr referenceToWhy() {
			return terminal(field.getClazz().toString() + ".class");
		}

		@Override
		public String toString() {
			return "Access.Static{" +
					"field=" + field +
					'}';
		}
	}
}
