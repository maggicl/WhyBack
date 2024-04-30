package byteback.whyml.syntax.function;

import byteback.whyml.ListComparator;
import byteback.whyml.identifiers.Identifier;
import byteback.whyml.identifiers.IdentifierEscaper;
import byteback.whyml.syntax.type.WhyType;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public record WhyFunctionSignature(
		WhyFunctionDeclaration declaration,
		Identifier.FQDN className,
		String name,
		List<WhyFunctionParam> params,
		WhyType returnType) implements Comparable<WhyFunctionSignature> {

	public WhyFunctionParam resultParam() {
		// spec functions return a pure result, while program function return a result encapsulated in a Result.t object
		// which encapsulates a possibly thrown exception, hence RESULT_VAR is used.
		return new WhyFunctionParam(
				declaration.isSpec()
						? Identifier.Special.RESULT
						: Identifier.Special.RESULT_VAR,
				returnType,
				false
		);
	}

	private static final Comparator<List<WhyFunctionParam>> PARAMETERS_ORDER =
			(ListComparator<WhyFunctionParam>) (a, b) -> WhyType.ORDER.compare(a.type(), b.type());

	public String descriptor() {
		return params.stream()
				.filter(e -> !e.isThis())
				.map(e -> e.type().getDescriptor())
				.collect(Collectors.joining(
						"",
						IdentifierEscaper.DESCRIPTOR_SECTION_SEPARATOR,
						IdentifierEscaper.DESCRIPTOR_SECTION_SEPARATOR + returnType.getDescriptor()));
	}

	@Override
	public int compareTo(WhyFunctionSignature o) {
		int a = this.className.compareTo(o.className);
		if (a != 0) return a;
		int b = this.name.compareTo(o.name);
		if (b != 0) return b;
		int c = PARAMETERS_ORDER.compare(this.params, o.params);
		if (c != 0) return c;
		return WhyType.ORDER.compare(this.returnType, o.returnType);
	}
}