package byteback.mlcfg.vimp;

import byteback.analysis.Namespace;
import byteback.analysis.util.SootHosts;
import byteback.mlcfg.identifiers.FQDNEscaper;
import byteback.mlcfg.identifiers.Identifier;
import byteback.mlcfg.identifiers.IdentifierEscaper;
import byteback.mlcfg.syntax.WhyFunction;
import byteback.mlcfg.syntax.WhyFunctionKind;
import byteback.mlcfg.syntax.types.WhyPrimitive;
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

	public Optional<WhyFunctionKind> whyFunctionKind(final SootMethod method) {
		if (SootHosts.hasAnnotation(method, Namespace.PRELUDE_ANNOTATION)) {
			return Optional.empty();
		} else if (SootHosts.hasAnnotation(method, Namespace.PREDICATE_ANNOTATION)) {
			return Optional.of(WhyFunctionKind.PREDICATE);
		} else if (SootHosts.hasAnnotation(method, Namespace.PURE_ANNOTATION)) {
			return Optional.of(WhyFunctionKind.PURE_FUNCTION);
		} else if (method.isStatic()) {
			return Optional.of(WhyFunctionKind.STATIC_METHOD);
		} else {
			return Optional.of(WhyFunctionKind.INSTANCE_METHOD);
		}
	}

	public Optional<WhyFunction> parse(SootMethod method) {
		return whyFunctionKind(method).map(whyFunctionKind -> {
			final String name = method.getName();
			final String descriptor = AbstractJasminClass.jasminDescriptorOf(method.makeRef());
			final Type sootReturnType = method.getReturnType();

			final Identifier.L identifier = identifierEscaper.escapeL(name + descriptor);
			final List<WhyType> parameterTypes = method.getParameterTypes().stream().map(typeResolver::resolveType).toList();
			final Optional<WhyType> returnType = sootReturnType.equals(VoidType.v())
					? Optional.empty()
					: Optional.of(typeResolver.resolveType(sootReturnType));

			if (whyFunctionKind == WhyFunctionKind.PREDICATE && returnType.filter(e -> e == WhyPrimitive.BOOL).isEmpty()) {
				throw new IllegalStateException("return type of a predicate must be a boolean");
			}

			return new WhyFunction(
					identifier,
					fqdnEscaper.escape(method.getDeclaringClass().getName(), method.getDeclaringClass().getPackageName().isEmpty()),
					whyFunctionKind,
					parameterTypes,
					returnType);
		});
	}
}
