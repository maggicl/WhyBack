package byteback.whyml.identifiers;

import byteback.whyml.ListComparator;
import byteback.whyml.syntax.type.WhyJVMType;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public sealed class Identifier implements Comparable<Identifier> permits Identifier.L, Identifier.U {
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

	@Override
	public int compareTo(Identifier o) {
		return this.contents.compareTo(o.contents);
	}

	public static boolean isLegalChar(int e) {
		return ('0' <= e && e <= '9') || ('a' <= e && e <= 'z') || ('A' <= e && e <= 'Z');
	}

	public final static class Special {
		public static final FQDN OBJECT = FQDN.special("Java", "Lang", "Object");
		public static final FQDN STRING = FQDN.special("Java", "Lang", "String");
		public static final FQDN CLASS = FQDN.special("Java", "Lang", "Class");
		public static final FQDN NULL_POINTER_EXCEPTION = FQDN.special("Java", "Lang", "NullPointerException");
		public static final FQDN ARRAY_INDEX_OUT_OF_BOUNDS_EXCEPTION = FQDN.special("Java", "Lang", "ArrayIndexOutOfBoundsException");
		public static final FQDN ARRAY_STORE_EXCEPTION = FQDN.special("Java", "Lang", "ArrayStoreException");
		public static final FQDN NEGATIVE_ARRAY_SIZE_EXCEPTION = FQDN.special("Java", "Lang", "NegativeArraySizeException");
		public static final FQDN CLASS_CAST_EXCEPTION = FQDN.special("Java", "Lang", "ClassCastException");
		public static final Identifier.U DEFAULT_PACKAGE = new U("Default" + IdentifierEscaper.PRELUDE_RESERVED);
		public static final Identifier.L RESULT = new L("result");
		public static final Identifier.L HEAP = new L("heap");
		public static final Identifier.L CAUGHT_EXCEPTION = new L("caughtexception");
		public static final Identifier.L THIS = new L("this");

		public static Identifier.L getHeap(WhyJVMType type) {
			return new L("heap.%s".formatted(type.getWhyAccessorScope().toLowerCase(Locale.ROOT)));
		}

		public static Identifier.L getArrayHeap(WhyJVMType type) {
			if (type == WhyJVMType.PTR) {
				// heap.rl_* data is part of heap itself to allow type assertions on elements (which
				// are references and thus their type is in l.typeof)
				return HEAP;
			}

			return new L("heap.r%s".formatted(type.getWhyAccessorScope().toLowerCase(Locale.ROOT)));
		}

		/**
		 * Generate an MLCFG label given a label number
		 */
		public static Identifier.U label(int number) {
			return new Identifier.U("L" + number);
		}

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

	public static final class FQDN implements Comparable<FQDN> {
		private static final Comparator<List<U>> IDENTIFIERS_ORDER = (ListComparator<U>) Identifier::compareTo;
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

		public String descriptor() {
			return identifiers.stream().map(U::toString)
					.collect(Collectors.joining(IdentifierEscaper.DESCRIPTOR_SEPARATOR))
					+ IdentifierEscaper.DESCRIPTOR_END;
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

		@Override
		public int compareTo(FQDN o) {
			return IDENTIFIERS_ORDER.compare(this.identifiers, o.identifiers);
		}
	}
}
