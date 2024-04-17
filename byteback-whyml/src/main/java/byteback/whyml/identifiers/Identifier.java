package byteback.whyml.identifiers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
		public static final FQDN STRING = FQDN.special("Java", "Lang", "String");
		public static final FQDN NULL_POINTER_EXCEPTION = FQDN.special("Java", "Lang", "NullPointerException");
		public static final FQDN ARRAY_INDEX_OUT_OF_BOUNDS_EXCEPTION = FQDN.special("Java", "Lang", "ArrayIndexOutOfBoundsException");
		public static final FQDN ARRAY_STORE_EXCEPTION = FQDN.special("Java", "Lang", "ArrayStoreException");
		public static final FQDN NEGATIVE_ARRAY_SIZE_EXCEPTION = FQDN.special("Java", "Lang", "NegativeArraySizeException");
		public static final FQDN CLASS_CAST_EXCEPTION = FQDN.special("Java", "Lang", "ClassCastException");
		public static final Identifier.U DEFAULT_PACKAGE = new U("Default" + IdentifierEscaper.PRELUDE_RESERVED);
		public static final Identifier.L RESULT = new L("result");
		public static final Identifier.L HEAP = new L("heap" + IdentifierEscaper.PRELUDE_RESERVED);
		public static final Identifier.L EXCEPTION_PARAM = new L("e" + IdentifierEscaper.PRELUDE_RESERVED);

		private Special() {
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

		public FQDN qualify(Identifier.U... identifier) {
			return new FQDN(Stream.concat(identifiers.stream(), Stream.of(identifier)).toList());
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
