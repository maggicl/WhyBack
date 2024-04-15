package byteback.whyml.vimp;

import byteback.analysis.Namespace;
import byteback.analysis.util.AnnotationElems;
import byteback.analysis.util.SootAnnotations;
import byteback.analysis.util.SootHosts;
import byteback.whyml.identifiers.Identifier;
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
import soot.BooleanType;
import soot.SootMethod;
import soot.Type;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationTag;

public class VimpMethodParser {
	private final VimpClassNameParser classNameParser;
	private final TypeResolver typeResolver;

	public VimpMethodParser(VimpClassNameParser classNameParser, TypeResolver typeResolver) {
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

	public WhyFunctionKind.Inline inline(final SootMethod method) {
		// true if annotated with @Pure
		final boolean isPure = SootHosts.hasAnnotation(method, Namespace.PURE_ANNOTATION);

		// true if annotated with @Predicate
		final boolean isPredicate = SootHosts.hasAnnotation(method, Namespace.PREDICATE_ANNOTATION);

		// if @Predicate and @Pure, may be inlined
		// if only @Predicate, MUST be inlined (to make old(...) work)
		// if only @Pure, MUST NOT be inlined (as old(...) won't work in a declaration)
		return isPure && isPredicate
				? WhyFunctionKind.Inline.OPTIONAL
				: isPredicate
				? WhyFunctionKind.Inline.REQUIRED
				: WhyFunctionKind.Inline.NEVER;
	}

	private Optional<WhyFunctionKind.Declaration> declaration(final SootMethod method) {
		// prelude functions must not be translated, as they are declared in the prelude
		if (SootHosts.hasAnnotation(method, Namespace.PRELUDE_ANNOTATION)) {
			return Optional.empty();
		}

		// true if annotated with @Pure
		final boolean isPure = SootHosts.hasAnnotation(method, Namespace.PURE_ANNOTATION);

		// true if annotated with @Predicate
		final boolean isPredicate = SootHosts.hasAnnotation(method, Namespace.PREDICATE_ANNOTATION);

		return Optional.of(!isPure && !isPredicate
				? WhyFunctionKind.Declaration.PROGRAM
				: method.getReturnType() == BooleanType.v()
				? WhyFunctionKind.Declaration.PREDICATE
				: WhyFunctionKind.Declaration.FUNCTION);
	}

	public Optional<VimpMethod> reference(SootMethod method) {
		final Identifier.FQDN clazz = classNameParser.parse(method.getDeclaringClass());

		return declaration(method).map(k ->
				new VimpMethod(
						clazz,
						method.getName(),
						method.isStatic()
								? Optional.empty()
								: Optional.of(method.getDeclaringClass().getType()),
						method.getParameterTypes(),
						method.getReturnType(),
						k)
		);
	}

	public Optional<WhyFunctionSignature> signature(VimpMethod ref, SootMethod method) {
		final WhyFunctionKind.Inline inline = inline(method);

		if (inline.must()) { // if the function must be inlined, it does not have a signature for decl
			return Optional.empty();
		}

		final Type sootReturnType = ref.returnType();

		final List<WhyType> parameterTypes = ref.parameterTypes().stream().map(typeResolver::resolveType).toList();
		final WhyType returnType = typeResolver.resolveType(sootReturnType);

		if (ref.decl() == WhyFunctionKind.Declaration.PREDICATE && returnType != WhyJVMType.BOOL) {
			throw new IllegalStateException("return type of a predicate must be a boolean");
		}

		final Optional<WhyFunctionParam> thisParam = ref.thisType().map(e ->
				new WhyFunctionParam(
						Identifier.Special.methodParam(0),
						new WhyReference(ref.className()),
						true));

		final List<WhyFunctionParam> paramsList = IntStream.range(0, parameterTypes.size())
				.mapToObj(i -> new WhyFunctionParam(
						Identifier.Special.methodParam(i + (method.isStatic() ? 0 : 1)),
						parameterTypes.get(i),
						false))
				.toList();

		final var annotations = SootHosts.getAnnotations(method)
				.flatMap(SootAnnotations::getAnnotations)
				.map(VimpMethodParser::getCondition)
				.flatMap(Optional::stream)
				.toList();

		return Optional.of(new WhyFunctionSignature(ref, thisParam, paramsList, returnType, inline, annotations));
	}
}
