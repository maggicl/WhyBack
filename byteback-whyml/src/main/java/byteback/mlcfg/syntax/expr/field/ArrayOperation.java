package byteback.mlcfg.syntax.expr.field;

import byteback.mlcfg.syntax.expr.Expression;
import byteback.mlcfg.syntax.types.WhyJVMType;
import java.util.Optional;

public abstract sealed class ArrayOperation {

	public static Load load(Expression index) {
		return new Load(index);
	}

	public static Store store(Expression index, Expression value) {
		return new Store(index, value);
	}

	public static Length length() {
		return Length.INSTANCE;
	}

	public abstract Optional<WhyJVMType> fixedReturnType();

	public final static class Load extends ArrayOperation {
		private final Expression index;

		private Load(Expression index) {
			if (index.type() != WhyJVMType.INT) {
				throw new IllegalArgumentException("array load operation must have index of type INT");
			}

			this.index = index;
		}

		@Override
		public Optional<WhyJVMType> fixedReturnType() {
			return Optional.empty();
		}

		public Expression getIndex() {
			return index;
		}
	}

	public final static class Store extends ArrayOperation {
		private final Expression index;
		private final Expression value;

		private Store(Expression index, Expression value) {
			if (index.type() != WhyJVMType.INT) {
				throw new IllegalArgumentException("array store operation must have index of type INT");
			}

			this.index = index;
			this.value = value;
		}

		@Override
		public Optional<WhyJVMType> fixedReturnType() {
			return Optional.of(WhyJVMType.UNIT);
		}

		public Expression getValue() {
			return value;
		}

		public Expression getIndex() {
			return index;
		}
	}

	public final static class Length extends ArrayOperation {
		private static final Length INSTANCE = new Length();

		private Length() {
		}

		@Override
		public Optional<WhyJVMType> fixedReturnType() {
			return Optional.of(WhyJVMType.INT);
		}
	}
}
