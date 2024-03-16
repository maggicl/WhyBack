package byteback.mlcfg.identifiers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public sealed class Identifier permits Identifier.L, Identifier.U {
	private final String contents;

	Identifier(String contents) {
		this.contents = Objects.requireNonNull(contents);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Identifier that = (Identifier) o;
		return Objects.equals(contents, that.contents);
	}

	@Override
	public int hashCode() {
		return Objects.hash(contents);
	}

	public String toString() {
		return contents;
	}

	public final static class Special {
		public static final FQDN OBJECT = FQDN.special("Java", "Lang", "Object");
		public static final Identifier.L THIS = new L("this");
		public static final Identifier.L RESULT = new L("result");
		private Special() {
		}

		public static Identifier.L methodParam(int num) {
			return new L("l%d".formatted(num));
		}
	}

	public static final class L extends Identifier {
		L(String contents) {
			super(contents);
		}
	}

	public static final class U extends Identifier {
		U(String contents) {
			super(contents);
		}
	}

	public static final class FQDN {
		private final List<U> identifiers;

		FQDN(List<U> identifiers) {
			this.identifiers = Collections.unmodifiableList(identifiers);
		}

		private static FQDN special(String... identifiers) {
			return new FQDN(Arrays.stream(identifiers).map(U::new).toList());
		}

		public List<U> getIdentifiers() {
			return identifiers;
		}

		public String toString() {
			return identifiers.stream().map(U::toString).collect(Collectors.joining("."));
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			FQDN fqdn = (FQDN) o;
			return Objects.equals(identifiers, fqdn.identifiers);
		}

		@Override
		public int hashCode() {
			return Objects.hash(identifiers);
		}
	}
}
