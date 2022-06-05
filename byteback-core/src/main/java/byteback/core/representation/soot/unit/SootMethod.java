package byteback.core.representation.soot.unit;

import byteback.core.representation.soot.annotation.SootAnnotation;
import byteback.core.representation.soot.annotation.SootAnnotationElement;
import byteback.core.representation.soot.body.SootBody;
import byteback.core.representation.soot.type.SootType;
import byteback.core.util.Lazy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import soot.Local;
import soot.jimple.internal.JimpleLocal;
import soot.tagkit.VisibilityAnnotationTag;

public class SootMethod {

	static String formatSignature(final String name, final Collection<SootType> parameterTypes,
			final SootType returnType) {
		final StringBuilder builder = new StringBuilder();
		builder.append(returnType);
		builder.append(" ");
		builder.append(name);
		builder.append(formatParameters(parameterTypes));

		return builder.toString();
	}

	static String formatParameters(final Iterable<SootType> parameterTypes) {
		final StringBuilder builder = new StringBuilder();
		final Iterator<SootType> iterator = parameterTypes.iterator();
		builder.append("(");

		while (iterator.hasNext()) {
			builder.append(iterator.next().toString());

			if (iterator.hasNext()) {
				builder.append(",");
			}
		}

		builder.append(")");

		return builder.toString();
	}

	private final soot.SootMethod sootMethod;

	private final Lazy<SootBody> body;

	/**
	 * Constructor for the Soot method intermediate representation.
	 *
	 * @param sootMethod
	 *            The wrapped {@code SootMethod} instance.
	 */
	public SootMethod(final soot.SootMethod sootMethod) {
		this.sootMethod = sootMethod;
		this.body = Lazy.from(() -> new SootBody(sootMethod.retrieveActiveBody()));
	}

	public String getName() {
		return sootMethod.getName();
	}

	public String getIdentifier() {
		final StringBuilder builder = new StringBuilder();
		builder.append(getName());
		builder.append(formatParameters(parameterTypes()::iterator));

		return builder.toString();
	}

	public Stream<SootType> parameterTypes() {
		return sootMethod.getParameterTypes().stream().map(SootType::new);
	}

	public List<SootType> getParameterTypes() {
		return parameterTypes().collect(Collectors.toList());
	}

	public int getParameterCount() {
		return sootMethod.getParameterCount();
	}

	public SootType getReturnType() {
		return new SootType(sootMethod.getReturnType());
	}

	public SootBody getBody() {
		return body.get();
	}

	public boolean hasBody() {
		return sootMethod.hasActiveBody() || sootMethod.isConcrete();
	}

	public SootClass getSootClass() {
		return new SootClass(sootMethod.getDeclaringClass());
	}

	public Collection<Local> getFakeParameterLocals() {
		final List<Local> parameterLocals = new ArrayList<>();

		if (!sootMethod.isStatic()) {
			parameterLocals.add(new JimpleLocal("this", sootMethod.getDeclaringClass().getType()));
		}

		for (int i = 0; i < getParameterCount(); ++i) {
			final String name = "p" + i;
			parameterLocals.add(new JimpleLocal(name, sootMethod.getParameterType(i)));
		}

		return parameterLocals;
	}

	public Stream<SootAnnotation> annotations() {
		final VisibilityAnnotationTag tag = (VisibilityAnnotationTag) sootMethod.getTag("VisibilityAnnotationTag");

		if (tag != null) {
			return tag.getAnnotations().stream().map(SootAnnotation::new);
		} else {
			return Stream.empty();
		}
	}

	public Optional<SootAnnotation> annotation(final String name) {
		return annotations(name).findFirst();
	}

	public Stream<SootAnnotation> annotations(final String name) {
		return annotations().filter((tag) -> tag.getTypeName().equals(name));
	}

	public Stream<SootAnnotationElement> annotationValues(final String name) {
		return annotations(name).flatMap((annotation) -> annotation.getValue().stream());
	}

	public int getNumber() {
		return sootMethod.getNumber();
	}

	public boolean isStatic() {
		return sootMethod.isStatic();
	}

	@Override
	public boolean equals(Object object) {
		return object instanceof SootMethod && getNumber() == ((SootMethod) object).getNumber();
	}

}
