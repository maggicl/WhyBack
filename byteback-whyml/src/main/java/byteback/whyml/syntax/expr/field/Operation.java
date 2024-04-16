package byteback.whyml.syntax.expr.field;

import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.type.WhyJVMType;
import java.util.Optional;

public sealed abstract class Operation {
	public abstract Optional<WhyJVMType> fixedReturnType();

	public static Is is() {
		return Is.INSTANCE;
	}

	public static Get get() {
		return Get.INSTANCE;
	}

	public static Put put(Expression value) {
		return new Put(value);
	}

	public static final class Get extends Operation {
		private static final Get INSTANCE = new Get();

		private Get() {
		}

		@Override
		public Optional<WhyJVMType> fixedReturnType() {
			return Optional.empty();
		}
	}

	/**
	 * Like get, but for spec functions
	 */
	public static final class Is extends Operation {
		private static final Is INSTANCE = new Is();

		private Is() {
		}

		@Override
		public Optional<WhyJVMType> fixedReturnType() {
			return Optional.empty();
		}
	}

	public static final class Put extends Operation {
		private final Expression value;

		private Put(Expression value) {
			this.value = value;
		}

		public Expression getValue() {
			return value;
		}

		@Override
		public Optional<WhyJVMType> fixedReturnType() {
			return Optional.of(WhyJVMType.UNIT);
		}
	}
}
