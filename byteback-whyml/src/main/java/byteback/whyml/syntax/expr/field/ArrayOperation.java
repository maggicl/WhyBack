package byteback.whyml.syntax.expr.field;

import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.type.WhyJVMType;
import java.util.Optional;

public abstract sealed class ArrayOperation {

	public static Load load(Expression index) {
		return new Load(index);
	}

	public static IsElem isElem(Expression index) {
		return new IsElem(index);
	}

	public static Store store(Expression index, Expression value) {
		return new Store(index, value);
	}

	public static Length length() {
		return Length.INSTANCE;
	}

	public abstract Optional<WhyJVMType> fixedReturnType();

	private abstract static sealed class AbstractLoad extends ArrayOperation {
		private final Expression index;

		protected AbstractLoad(Expression index) {
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

	public final static class Load extends AbstractLoad {
		private Load(Expression index) {
			super(index);
		}
	}

	/**
	 * Like load but pure, does not throw exception if pointer is null or index is out of bounds, for spec code
	 */
	public final static class IsElem extends AbstractLoad {
		private IsElem(Expression index) {
			super(index);
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
