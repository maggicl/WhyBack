package byteback.mlcfg.syntax;

import java.util.Optional;

public sealed abstract class WhyCondition {
	public abstract <T> T transform(Transformer<T> transformer);

	public interface Transformer<T> {
		default T transform(WhyCondition cond) {
			return cond.transform(this);
		}

		T transformRequires(Requires r);

		T transformEnsures(Ensures r);

		T transformReturns(Returns r);

		T transformRaises(Raises r);
	}

	public static final class Requires extends WhyCondition {
		private final String value;

		public Requires(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		@Override
		public <T> T transform(Transformer<T> transformer) {
			return transformer.transformRequires(this);
		}
	}

	public static final class Ensures extends WhyCondition {
		private final String value;

		public Ensures(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		@Override
		public <T> T transform(Transformer<T> transformer) {
			return transformer.transformEnsures(this);
		}
	}

	public static final class Returns extends WhyCondition {
		private final Optional<String> when;

		public Returns(Optional<String> when) {
			this.when = when;
		}

		public Optional<String> getWhen() {
			return when;
		}

		@Override
		public <T> T transform(Transformer<T> transformer) {
			return transformer.transformReturns(this);
		}
	}

	public static final class Raises extends WhyCondition {
		private final Optional<String> when;
		private final String exception;

		public Raises(Optional<String> when, String exception) {
			this.when = when;
			this.exception = exception;
		}

		public Optional<String> getWhen() {
			return when;
		}

		public String getException() {
			return exception;
		}

		@Override
		public <T> T transform(Transformer<T> transformer) {
			return transformer.transformRaises(this);
		}
	}
}
