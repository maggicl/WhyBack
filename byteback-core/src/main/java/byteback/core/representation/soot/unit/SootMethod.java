package byteback.core.representation.soot.unit;

import byteback.core.representation.soot.annotation.SootAnnotation;
import byteback.core.representation.soot.annotation.SootAnnotationElement;
import byteback.core.representation.soot.body.SootBody;
import byteback.core.representation.soot.type.SootType;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import byteback.core.util.Lazy;
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

  static String formatParameters(final Collection<SootType> parameterTypes) {
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
   * @param sootMethod The wrapped {@code SootMethod} instance.
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
    builder.append(formatParameters(getParameterTypes()));

    return builder.toString();
  }

  public List<SootType> getParameterTypes() {
    return sootMethod.getParameterTypes().stream().map(SootType::new).collect(Collectors.toList());
  }

  public SootType getReturnType() {
    return new SootType(sootMethod.getReturnType());
  }

  public SootBody getBody() {
    return body.get();
  }

  public SootClass getSootClass() {
    return new SootClass(sootMethod.getDeclaringClass());
  }

  public Optional<SootAnnotation> getAnnotation(final String name) {
    return getAnnotations(name).findFirst();
  }

  public Stream<SootAnnotation> getAnnotations(final String name) {
    return annotations().filter((tag) -> tag.getTypeName().equals(name));
  }

  public Stream<SootAnnotationElement> getAnnotationValues(final String name) {
    return getAnnotations(name).flatMap((annotation) -> annotation.getValue().stream());
  }

  public Stream<SootAnnotation> annotations() {
    final VisibilityAnnotationTag tag = (VisibilityAnnotationTag) sootMethod.getTag("VisibilityAnnotationTag");

    if (tag != null) {
      return tag.getAnnotations().stream().map(SootAnnotation::new);
    } else {
      return Stream.empty();
    }
  }

  public int getNumber() {
    return sootMethod.getNumber();
  }

  @Override
  public boolean equals(Object object) {
    return object instanceof SootMethod && getNumber() == ((SootMethod) object).getNumber();
  }

}
