package byteback.whyml.vimp;

import byteback.analysis.Namespace;
import byteback.analysis.util.AnnotationElems;
import byteback.analysis.util.SootAnnotations;
import byteback.analysis.util.SootHosts;
import byteback.whyml.identifiers.Identifier;
import byteback.whyml.identifiers.IdentifierEscaper;
import byteback.whyml.syntax.function.VimpCondition;
import byteback.whyml.syntax.function.VimpMethod;
import byteback.whyml.syntax.function.WhyFunctionKind;
import byteback.whyml.syntax.function.WhyFunctionParam;
import byteback.whyml.syntax.function.WhyFunctionSignature;
import byteback.whyml.syntax.type.WhyJVMType;
import byteback.whyml.syntax.type.WhyReference;
import byteback.whyml.syntax.type.WhyType;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import soot.AbstractJasminClass;
import soot.SootMethod;
import soot.Type;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationTag;

public class VimpMethodParser {
	private final IdentifierEscaper identifierEscaper;
	private final VimpClassNameParser classNameParser;
	private final TypeResolver typeResolver;

	public VimpMethodParser(IdentifierEscaper identifierEscaper, VimpClassNameParser classNameParser,
							TypeResolver typeResolver) {
		this.identifierEscaper = identifierEscaper;
		this.classNameParser = classNameParser;
		this.typeResolver = typeResolver;
	}

	public static Optional<VimpCondition> getCondition(final AnnotationTag tag) {
		final String type = tag.getType();

		boolean isRequires = Namespace.REQUIRE_ANNOTATION.equals(type);

		if (isRequires || Namespace.ENSURE_ANNOTATION.equals(type)) {
			final AnnotationElem elem = SootAnnotations.getValue(tag).orElseThrow();
			final String value = new AnnotationElems.StringElemExtractor().visit(elem);

			return Optional.of(isRequires ? new VimpCondition.Requires(value) : new VimpCondition.Ensures(value));
		} else if (Namespace.RETURN_ANNOTATION.equals(type)) {
			final Optional<String> when = SootAnnotations.getElem(tag, "when")
					.map(new AnnotationElems.StringElemExtractor()::visit);
			return Optional.of(new VimpCondition.Returns(when));
		} else if (Namespace.RAISE_ANNOTATION.equals(type)) {
			final Optional<String> when = SootAnnotations.getElem(tag, "when")
					.map(new AnnotationElems.StringElemExtractor()::visit);

			final AnnotationElem exceptionElem = SootAnnotations.getElem(tag, "exception").orElseThrow();
			final String exception = new AnnotationElems.ClassElemExtractor().visit(exceptionElem)
					.replaceAll("^L", "") // normalize from JVM name to Java name
					.replaceAll(";$", "")
					.replace('/', '.');

			return Optional.of(new VimpCondition.Raises(when, exception));
		}

		return Optional.empty();
	}

	public Optional<WhyFunctionKind> whyFunctionKind(final SootMethod method) {
		if (SootHosts.hasAnnotation(method, Namespace.PRELUDE_ANNOTATION)) {
			return Optional.empty();
		} else if (SootHosts.hasAnnotation(method, Namespace.PURE_ANNOTATION)) {
			return Optional.of(SootHosts.hasAnnotation(method, Namespace.PREDICATE_ANNOTATION)
					? WhyFunctionKind.PURE_PREDICATE
					: WhyFunctionKind.PURE);
		} else if (SootHosts.hasAnnotation(method, Namespace.PREDICATE_ANNOTATION)) {
			return Optional.of(WhyFunctionKind.PREDICATE);
		} else if (method.isStatic()) {
			return Optional.of(WhyFunctionKind.STATIC_METHOD);
		} else {
			return Optional.of(WhyFunctionKind.INSTANCE_METHOD);
		}
	}

	public Optional<VimpMethod> reference(SootMethod method) {
		final Identifier.FQDN clazz = classNameParser.parse(method.getDeclaringClass());

		return whyFunctionKind(method).map(k ->
				new VimpMethod(
						clazz,
						method.getName(),
						AbstractJasminClass.jasminDescriptorOf(method.makeRef()),
						k)
		);
	}

	public Optional<WhyFunctionSignature> signature(VimpMethod ref, SootMethod method) {
		if (ref.kind() == WhyFunctionKind.PREDICATE) {
			return Optional.empty();
		}

		final Type sootReturnType = method.getReturnType();

		final List<WhyType> parameterTypes = method.getParameterTypes().stream().map(typeResolver::resolveType).toList();
		final WhyType returnType = typeResolver.resolveType(sootReturnType);

		if (ref.kind() == WhyFunctionKind.PURE_PREDICATE && returnType != WhyJVMType.BOOL) {
			throw new IllegalStateException("return type of a predicate must be a boolean");
		}

		final Optional<WhyFunctionParam> thisParam = ref.kind() != WhyFunctionKind.INSTANCE_METHOD
				? Optional.empty()
				: Optional.of(new WhyFunctionParam(
				Identifier.Special.THIS,
				new WhyReference(ref.className()),
				true));

		final List<WhyFunctionParam> paramsList = IntStream.range(0, parameterTypes.size())
				.mapToObj(i -> new WhyFunctionParam(
						Identifier.Special.methodParam(i),
						parameterTypes.get(i),
						false))
				.toList();

		final var annotations = SootHosts.getAnnotations(method)
				.flatMap(SootAnnotations::getAnnotations)
				.map(VimpMethodParser::getCondition)
				.flatMap(Optional::stream)
				.toList();

		return Optional.of(new WhyFunctionSignature(ref, thisParam, paramsList, returnType, annotations));
	}
}
