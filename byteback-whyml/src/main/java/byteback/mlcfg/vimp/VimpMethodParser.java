package byteback.mlcfg.vimp;

import byteback.mlcfg.identifiers.FQDNEscaper;
import byteback.mlcfg.identifiers.Identifier;
import byteback.mlcfg.identifiers.IdentifierEscaper;
import byteback.mlcfg.syntax.WhyMethod;
import byteback.mlcfg.syntax.types.WhyType;
import java.util.List;
import java.util.Optional;
import soot.AbstractJasminClass;
import soot.SootMethod;
import soot.Type;
import soot.VoidType;

public class VimpMethodParser {
	private final IdentifierEscaper identifierEscaper;
	private final FQDNEscaper fqdnEscaper;
	private final TypeResolver typeResolver;

	public VimpMethodParser(IdentifierEscaper identifierEscaper, FQDNEscaper fqdnEscaper, TypeResolver typeResolver) {
		this.identifierEscaper = identifierEscaper;
		this.fqdnEscaper = fqdnEscaper;
		this.typeResolver = typeResolver;
	}

	public WhyMethod parse(SootMethod method) {
		final String name = method.getName();
		final String descriptor = AbstractJasminClass.jasminDescriptorOf(method.makeRef());
		final Type sootReturnType = method.getReturnType();

		final Identifier.L identifier = identifierEscaper.escapeL(name + descriptor);
		final List<WhyType> parameterTypes = method.getParameterTypes().stream().map(typeResolver::resolveType).toList();
		final Optional<WhyType> returnType = sootReturnType.equals(VoidType.v())
				? Optional.empty()
				: Optional.of(typeResolver.resolveType(sootReturnType));

		return new WhyMethod(
				identifier,
				fqdnEscaper.escape(method.getDeclaringClass().getName()),
				method.isStatic(),
				parameterTypes,
				returnType);
	}
}
