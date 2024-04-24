package byteback.analysis;

import byteback.analysis.util.AnnotationElems;
import byteback.analysis.util.SootAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import soot.BooleanType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.VoidType;
import soot.tagkit.AbstractHost;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationTag;

public sealed abstract class VimpCondition {
	private static SootMethod resolveConditionMethod(SootMethod scope, String name, boolean hasResult) {
		final List<Type> parameters = new ArrayList<>(scope.getParameterTypes());
		final Type returnType = scope.getReturnType();

		if (!VoidType.v().equals(returnType) && hasResult) {
			parameters.add(returnType);
		}

		return scope.getDeclaringClass().getMethod(name, parameters, BooleanType.v());
	}

	public static Optional<VimpCondition> parse(final SootMethod scope, final AnnotationTag tag) {
		final String type = tag.getType();

		boolean isRequires = Namespace.REQUIRE_ANNOTATION.equals(type);

		if (isRequires || Namespace.ENSURE_ANNOTATION.equals(type)) {
			final AnnotationElem elem = SootAnnotations.getValue(tag).orElseThrow();
			final String value = new AnnotationElems.StringElemExtractor().visit(elem);
			final SootMethod method = resolveConditionMethod(scope, value, !isRequires);
			return Optional.of(isRequires ? new VimpCondition.Requires(method) : new VimpCondition.Ensures(method));
		} else if (Namespace.RETURN_ANNOTATION.equals(type)) {
			final Optional<SootMethod> when = SootAnnotations.getElem(tag, "when")
					.map(new AnnotationElems.StringElemExtractor()::visit)
					.map(e -> resolveConditionMethod(scope, e, false));

			return Optional.of(new VimpCondition.Returns(when));
		} else if (Namespace.RAISE_ANNOTATION.equals(type)) {
			final Optional<SootMethod> when = SootAnnotations.getElem(tag, "when")
					.map(new AnnotationElems.StringElemExtractor()::visit)
					.map(e -> resolveConditionMethod(scope, e, false));

			final AnnotationElem exceptionElem = SootAnnotations.getElem(tag, "exception").orElseThrow();
			final String exception = Namespace.stripDescriptor(new AnnotationElems.ClassElemExtractor().visit(exceptionElem));
			return Optional.of(new VimpCondition.Raises(when, Scene.v().getSootClass(exception)));
		}

		return Optional.empty();
	}

	public abstract <T> T transform(Transformer<T> transformer);

	public abstract Stream<? extends AbstractHost> getHosts();

	public interface Transformer<T> {
		default T transform(VimpCondition cond) {
			return cond.transform(this);
		}

		T transformRequires(Requires r);

		T transformEnsures(Ensures r);

		T transformReturns(Returns r);

		T transformRaises(Raises r);
	}

	public static final class Requires extends VimpCondition {
		private final SootMethod value;

		public Requires(SootMethod value) {
			this.value = value;
		}

		public SootMethod getValue() {
			return value;
		}

		@Override
		public <T> T transform(Transformer<T> transformer) {
			return transformer.transformRequires(this);
		}

		@Override
		public Stream<? extends AbstractHost> getHosts() {
			return Stream.of(value);
		}
	}

	public static final class Ensures extends VimpCondition {
		private final SootMethod value;

		public Ensures(SootMethod value) {
			this.value = value;
		}

		public SootMethod getValue() {
			return value;
		}

		@Override
		public <T> T transform(Transformer<T> transformer) {
			return transformer.transformEnsures(this);
		}

		@Override
		public Stream<? extends AbstractHost> getHosts() {
			return Stream.of(value);
		}
	}

	public static final class Returns extends VimpCondition {
		private final Optional<SootMethod> when;

		public Returns(Optional<SootMethod> when) {
			this.when = when;
		}

		public Optional<SootMethod> getWhen() {
			return when;
		}

		@Override
		public <T> T transform(Transformer<T> transformer) {
			return transformer.transformReturns(this);
		}

		@Override
		public Stream<? extends AbstractHost> getHosts() {
			return when.stream();
		}
	}

	public static final class Raises extends VimpCondition {
		private final Optional<SootMethod> when;
		private final SootClass exception;

		public Raises(Optional<SootMethod> when, SootClass exception) {
			this.when = when;
			this.exception = exception;
		}

		public Optional<SootMethod> getWhen() {
			return when;
		}

		public SootClass getException() {
			return exception;
		}

		@Override
		public <T> T transform(Transformer<T> transformer) {
			return transformer.transformRaises(this);
		}

		@Override
		public Stream<? extends AbstractHost> getHosts() {
			return Stream.concat(when.stream(), Stream.of(exception));
		}
	}
}
