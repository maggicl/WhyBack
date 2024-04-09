package byteback.mlcfg.syntax;

import byteback.mlcfg.syntax.expr.BooleanLiteral;
import byteback.mlcfg.syntax.expr.Expression;
import java.util.Optional;

public sealed abstract class WhyCondition {

	public static final class Requires extends WhyCondition {
		private final String value;

		public Requires(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
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
	}

	public static final class Returns extends WhyCondition {
		private final Optional<String> when;

		public Returns(Optional<String> when) {
			this.when = when;
		}

		public Optional<String> getWhen() {
			return when;
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
	}
}
