package byteback.whyml.vimp;

import byteback.analysis.util.SootBodies;
import byteback.whyml.identifiers.Identifier;
import byteback.whyml.identifiers.IdentifierEscaper;
import byteback.whyml.syntax.function.WhyLocal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import soot.Local;
import soot.SootMethod;
import soot.jimple.internal.JimpleLocal;

public class VimpMethodParamParser {
	private final IdentifierEscaper identifierEscaper;
	private final TypeResolver typeResolver;

	public VimpMethodParamParser(IdentifierEscaper identifierEscaper, TypeResolver typeResolver) {
		this.identifierEscaper = identifierEscaper;
		this.typeResolver = typeResolver;
	}

	private static Optional<Local> getThisLocal(final SootMethod method) {
		if (method.hasActiveBody()) {
			return SootBodies.getThisLocal(method.getActiveBody());
		} else if (!method.isStatic()) {
			return Optional.of(new JimpleLocal("this", method.getDeclaringClass().getType()));
		} else {
			return Optional.empty();
		}
	}

	private static List<Local> getLocals(final SootMethod method) {
		if (method.hasActiveBody()) {
			return method.getActiveBody().getParameterLocals();
		} else {
			final List<Local> parameterLocals = new ArrayList<>(method.getParameterCount());

			for (int i = 0; i < method.getParameterCount(); ++i) {
				parameterLocals.add(new JimpleLocal(Integer.toString(i), method.getParameterType(i)));
			}

			return parameterLocals;
		}
	}

	private WhyLocal localToParam(Local local, boolean isThis) {
		return new WhyLocal(
				identifierEscaper.escapeParam(local.getName()),
				typeResolver.resolveType(local.getType()),
				isThis
		);
	}

	public Stream<Identifier.L> paramNames(final SootMethod method) {
		return Stream.concat(getThisLocal(method).stream(), getLocals(method).stream())
				.map(Local::getName)
				.map(identifierEscaper::escapeParam);
	}

	public List<WhyLocal> parseParams(SootMethod method) {
		return Stream.concat(
				getThisLocal(method).stream().map(local -> localToParam(local, true)),
				getLocals(method).stream().map(local -> localToParam(local, false))
		).toList();
	}
}
