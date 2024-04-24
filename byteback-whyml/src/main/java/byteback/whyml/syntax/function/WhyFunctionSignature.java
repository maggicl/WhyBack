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
		WhyType returnType,
		List<WhyCondition> conditions) implements Comparable<WhyFunctionSignature> {

	private static final Comparator<List<WhyFunctionParam>> PARAMETERS_ORDER =
			(ListComparator<WhyFunctionParam>) (a, b) -> WhyType.ORDER.compare(a.type(), b.type());

	public static String descriptor(List<WhyFunctionParam> params, WhyType returnType) {
		return params.stream().map(e -> e.type().getDescriptor())
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
