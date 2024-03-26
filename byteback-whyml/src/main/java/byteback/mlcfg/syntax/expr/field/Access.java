package byteback.mlcfg.syntax.expr.field;

import byteback.mlcfg.printer.SExpr;
import static byteback.mlcfg.printer.SExpr.terminal;
import byteback.mlcfg.syntax.WhyField;
import byteback.mlcfg.syntax.WhyInstanceField;
import byteback.mlcfg.syntax.WhyStaticField;
import byteback.mlcfg.syntax.expr.Expression;
import byteback.mlcfg.syntax.types.WhyJVMType;

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
				throw new IllegalArgumentException("base expression must be a reference");
			}

			this.base = base;
			this.field = field;
		}

		@Override
		public WhyField getField() {
			return field;
		}

		@Override
		public SExpr referenceToWhy() {
			return base.toWhy();
		}
	}

	public static final class Static extends Access {
		private final WhyStaticField field;

		private Static(WhyStaticField field) {
			this.field = field;
		}

		@Override
		public WhyField getField() {
			return field;
		}

		@Override
		public SExpr referenceToWhy() {
			return terminal(field.getClazz().toString() + ".class");
		}
	}
}
