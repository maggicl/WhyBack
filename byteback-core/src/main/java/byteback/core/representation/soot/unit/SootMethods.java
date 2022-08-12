package byteback.core.representation.soot.unit;

import byteback.core.representation.soot.annotation.SootAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import soot.Local;
import soot.SootMethod;
import soot.jimple.internal.JimpleLocal;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationTag;
import soot.tagkit.VisibilityAnnotationTag;

public class SootMethods {

	public static List<Local> makeFakeParameterLocals(final SootMethod method) {
		final List<Local> parameterLocals = new ArrayList<>();

		if (!method.isStatic()) {
			parameterLocals.add(new JimpleLocal("this", method.getDeclaringClass().getType()));
		}

		for (int i = 0; i < method.getParameterCount(); ++i) {
			final String name = "p" + i;
			parameterLocals.add(new JimpleLocal(name, method.getParameterType(i)));
		}

		return parameterLocals;
	}

	public static Stream<AnnotationTag> getAnnotations(final SootMethod method) {
		final var tag = (VisibilityAnnotationTag) method.getTag("VisibilityAnnotationTag");

		if (tag != null) {
			return tag.getAnnotations().stream();
		} else {
			return Stream.empty();
		}
	}

	public static Optional<AnnotationTag> getAnnotation(final SootMethod method, final String name) {
		return getAnnotations(method)
			.filter((tag) -> tag.getType().equals(name)).findFirst();
	}

	public Optional<AnnotationElem> getAnnotationValue(final SootMethod method, final String name) {
		return getAnnotation(method, name)
			.flatMap((annotation) -> SootAnnotations.getValue(annotation));
	}

	public static boolean hasAnnotation(final SootMethod method, final String name) {
		return getAnnotation(method, name).isPresent();
	}

	public static boolean hasBody(final SootMethod method) {
		try {
			return !method.isAbstract() && method.retrieveActiveBody() != null;
		} catch (final RuntimeException exception) {
			return false;
		}
	}

}
