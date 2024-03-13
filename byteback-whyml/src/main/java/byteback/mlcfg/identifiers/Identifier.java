package byteback.mlcfg.identifiers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public sealed class Identifier permits Identifier.L, Identifier.U {
	private final String contents;

	Identifier(String contents) {
		this.contents = contents;
	}

	public String toString() {
		return contents;
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

		public List<U> getIdentifiers() {
			return identifiers;
		}

		public String toString() {
			return identifiers.stream().map(U::toString).collect(Collectors.joining("."));
		}
	}
}
