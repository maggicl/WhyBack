package byteback.mlcfg.syntax.expr.field;

import byteback.mlcfg.syntax.expr.Expression;
import byteback.mlcfg.syntax.types.WhyJVMType;
import java.util.Optional;

public sealed abstract class Operation {
	public abstract Optional<WhyJVMType> fixedReturnType();

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
