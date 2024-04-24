package byteback.whyml.vimp;

import byteback.analysis.Inline;
import byteback.analysis.Namespace;
import byteback.analysis.VimpCondition;
import byteback.analysis.util.SootHosts;
import byteback.whyml.syntax.function.WhyCondition;
import byteback.whyml.syntax.function.WhyFunctionDeclaration;
import byteback.whyml.syntax.function.WhyFunctionParam;
import byteback.whyml.syntax.function.WhyFunctionSignature;
import byteback.whyml.syntax.type.WhyType;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import soot.BooleanType;
import soot.SootMethod;

public class VimpMethodParser {
	private final VimpMethodParamParser paramParser;
	private final VimpClassNameParser classNameParser;
	private final TypeResolver typeResolver;

	public VimpMethodParser(VimpMethodParamParser paramParser,
							VimpClassNameParser classNameParser,
							TypeResolver typeResolver) {
		this.paramParser = paramParser;
		this.classNameParser = classNameParser;
		this.typeResolver = typeResolver;
	}

	public static Optional<WhyFunctionDeclaration> declaration(final SootMethod method) {
		// prelude functions must not be translated, as they are declared in the prelude
		if (SootHosts.hasAnnotation(method, Namespace.PRELUDE_ANNOTATION)) {
			return Optional.empty();
		}

		// true if annotated with @Pure
		final boolean isPure = SootHosts.hasAnnotation(method, Namespace.PURE_ANNOTATION);

		// true if annotated with @Predicate
		final boolean isPredicate = SootHosts.hasAnnotation(method, Namespace.PREDICATE_ANNOTATION);

		return Optional.of(!isPure && !isPredicate
				? WhyFunctionDeclaration.PROGRAM
				: method.getReturnType() == BooleanType.v()
				? WhyFunctionDeclaration.PREDICATE
				: WhyFunctionDeclaration.FUNCTION);
	}


	public Optional<WhyFunctionSignature> signature(SootMethod method,
													List<VimpCondition> vimpConditions,
													WhyFunctionDeclaration decl,
													WhyResolver resolver) {
		if (Inline.parse(method).must()) { // if the function must be inlined, it does not have a signature for decl
			return Optional.empty();
		}

		final List<WhyFunctionParam> params = paramParser.parseParams(method);
		final WhyType returnType = typeResolver.resolveType(method.getReturnType());

		final VimpConditionParser conditionParser = new VimpConditionParser(
				classNameParser,
				paramParser,
				resolver,
				params,
				returnType
		);

		final List<WhyCondition> conditions = vimpConditions.stream()
				.map(conditionParser::transform)
				.toList();

		return Optional.of(new WhyFunctionSignature(
				decl,
				classNameParser.parse(method.getDeclaringClass()),
				method.getName(),
				params,
				returnType,
				conditions));
	}
}
