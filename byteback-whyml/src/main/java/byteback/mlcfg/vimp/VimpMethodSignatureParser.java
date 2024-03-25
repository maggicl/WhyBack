package byteback.mlcfg.vimp;

import byteback.analysis.Namespace;
import byteback.analysis.util.SootHosts;
import byteback.mlcfg.identifiers.Identifier;
import byteback.mlcfg.identifiers.IdentifierEscaper;
import byteback.mlcfg.syntax.WhyFunctionKind;
import byteback.mlcfg.syntax.WhyFunctionSignature;
import byteback.mlcfg.syntax.types.WhyJVMType;
import byteback.mlcfg.syntax.types.WhyType;
import java.util.List;
import java.util.Optional;
import soot.AbstractJasminClass;
import soot.SootMethod;
import soot.Type;

public class VimpMethodSignatureParser {
	private final IdentifierEscaper identifierEscaper;
	private final VimpClassNameParser classNameParser;
	private final TypeResolver typeResolver;

	public VimpMethodSignatureParser(IdentifierEscaper identifierEscaper, VimpClassNameParser classNameParser,
									 TypeResolver typeResolver) {
		this.identifierEscaper = identifierEscaper;
		this.classNameParser = classNameParser;
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

	public Optional<WhyFunctionSignature> parse(SootMethod method) {
		return whyFunctionKind(method).map(whyFunctionKind -> {
			final String name = method.getName();
			final String descriptor = AbstractJasminClass.jasminDescriptorOf(method.makeRef());
			final Type sootReturnType = method.getReturnType();

			final Identifier.L identifier = identifierEscaper.escapeL(name + descriptor);
			final List<WhyType> parameterTypes = method.getParameterTypes().stream().map(typeResolver::resolveType).toList();
			final WhyType returnType = typeResolver.resolveType(sootReturnType);

			if (whyFunctionKind == WhyFunctionKind.PREDICATE && returnType != WhyJVMType.BOOL) {
				throw new IllegalStateException("return type of a predicate must be a boolean");
			}

			return new WhyFunctionSignature(
					identifier,
					classNameParser.parse(method.getDeclaringClass()),
					whyFunctionKind,
					parameterTypes,
					returnType);
		});
	}
}
